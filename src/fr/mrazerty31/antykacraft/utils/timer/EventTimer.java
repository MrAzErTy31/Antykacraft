package fr.mrazerty31.antykacraft.utils.timer;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import fr.mrazerty31.antykacraft.Antykacraft;
import fr.mrazerty31.antykacraft.utils.TimeUtil;
import fr.mrazerty31.antykacraft.utils.TitleUtil;

public class EventTimer {
	private static EventTimer timer;
	private int time, duration;
	private Scoreboard scoreboard;
	private boolean started, stop;

	public EventTimer(int duration) {
		this.duration = duration;
		this.time = duration*60;
		this.scoreboard = Antykacraft.sbManager.getNewScoreboard();
		this.started = false;
		this.stop = false;
		timer = this;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getCurrentTime() {
		return time;
	}

	public void setCurrentTime(int time) {
		this.time = time;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public void setScoreboard(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}
	
	public void start() {
		final List<Player> players = Bukkit.getWorld("event").getPlayers();
		new BukkitRunnable() {
			int countdown = 10;
			public void run() {
				if(!stop) {
					if(countdown > 0) {
						if(countdown == 10) {
							for(Player p : players) {
								TitleUtil.sendTitle(p, 10, 20, 10, "&aDébut du jeu dans", "&6&l" + countdown + " &r&6secondes");
							}
						} else if(countdown <= 5) {
							for(Player p : players) {
								TitleUtil.sendTitle(p, 5, 20, 5, "&aDébut du jeu dans", "&6&l" + countdown + " &r&6seconde" + (countdown > 1 ? "s" : ""));
								p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
							}
						}
						countdown--;
					} else {
						if(!started) {
							for(Player p : players) {
								TitleUtil.sendTitle(p, 15, 30, 15, "&aLe jeu commence !", "&6Fin dans &l" + duration + " &r&6minutes");
								p.setScoreboard(scoreboard);
							}
							started = true;
						} else {
							scoreboard();
							if(time > 0) {
								time--;
							} else {
								stop();
								this.cancel();
							}
						}
					}
				} else {
					this.cancel();
				}
			}
		}.runTaskTimer(Antykacraft.instance, 0L, 20L);
	}

	public void stop() {
		List<Player> players = Bukkit.getWorld("event").getPlayers();
		this.stop = true;
		for(Player p : players) {
			p.setScoreboard(Antykacraft.sbManager.getMainScoreboard());
			p.sendMessage("§6[Antykacraft] §aLe jeu est fini !");
		}
		try {
			scoreboard.getObjective("timer").unregister();
		} catch(Exception ex) {}
	}

	private void scoreboard() {
		try {
			scoreboard.getObjective("timer").unregister();
		} catch(Exception ex) {}
		Objective timer = scoreboard.registerNewObjective("timer", "dummy");
		timer.setDisplayName("§6§lTimer");
		timer.setDisplaySlot(DisplaySlot.SIDEBAR);
		timer.getScore("§6§l>> §r§6"+TimeUtil.formatTime(TimeUtil.convertTime(time)[0], TimeUtil.convertTime(time)[1])).setScore(0);
	}

	public static EventTimer getCurrentTimer() {
		return timer;
	}
}
