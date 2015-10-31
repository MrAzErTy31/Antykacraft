package fr.mrazerty31.antykacraft.utils.roleplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.mrazerty31.antykacraft.Antykacraft;
import fr.mrazerty31.antykacraft.utils.TimeUtil;

public class Raid {
	private Faction attack, defend;
	private Player launcher;
	private boolean isFinished;
	private Scoreboard scoreboard;
	private int time;
	private static List<Raid> raids = new ArrayList<Raid>();
	private HashMap<Faction, Integer> kills = new HashMap<Faction, Integer>();

	public Raid(Player launcher, Faction attack, Faction defend) {
		this.launcher = launcher;
		this.attack = attack;
		this.defend = defend;
		this.isFinished = false;
		this.scoreboard = Antykacraft.sbManager.getNewScoreboard();
		this.time = 20*60;
		this.kills.put(attack, 0);
		this.kills.put(defend, 0);

		raids.add(this);
	}

	public Player getStarter() {
		return launcher;
	}

	public void setStarter(Player launcher) {
		this.launcher = launcher;
	}

	public Faction getAttacker() {
		return attack;
	}

	public void setAttacker(Faction attack) {
		this.attack = attack;
	}

	public Faction getDefender() {
		return defend;
	}

	public void setDefender(Faction defend) {
		this.defend = defend;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinish() {
		try {
		this.isFinished = true;
		getAttacker().setOnRaid(false);
		getDefender().setOnRaid(false);
		kills.clear();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public void setScoreboard(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public HashMap<Faction, Integer> getKills() {
		return kills;
	}
	
	public void setKills(HashMap<Faction, Integer> kills) {
		this.kills = kills;
	}

	public void start() {
		getAttacker().setHasRaid(true);
		getAttacker().setOnRaid(true);
		getDefender().setOnRaid(true);
		scoreboard();
		timer();
		for(Team t : Antykacraft.sbManager.getMainScoreboard().getTeams()) {
			try {
				Team te = scoreboard.registerNewTeam(t.getName());
				te.setAllowFriendlyFire(false);
				Faction ct = Faction.getFactionByTeam(t);
				te.setPrefix("" + ct.getColor());
				for(String pl : t.getEntries()) {
					te.addEntry(pl);
				} for(Player pla : Bukkit.getOnlinePlayers()) {
					pla.setPlayerListName(pla.getName());
				}
			} catch(Exception ex) {}
		}
	}

	private void scoreboard() {
		Faction a = getAttacker();
		Faction b = getDefender();
		try{
			scoreboard.getObjective("raid").unregister();
		} catch(Exception e) {}
		Objective raid = scoreboard.registerNewObjective("raid", "dummy");
		raid.setDisplayName("§6§lRaid");
		raid.setDisplaySlot(DisplaySlot.SIDEBAR);
		raid.getScore("§6" + a.getRaidName() + " (" + kills.get(a) + " kills)").setScore(5);
		raid.getScore("§6---- VS ----").setScore(4);
		raid.getScore("§6" + b.getRaidName() + " (" + kills.get(b) + " kills)").setScore(3);
		raid.getScore("§6--------").setScore(2);
		raid.getScore("§6>> §b"+TimeUtil.formatTime(TimeUtil.convertTime(time)[0], TimeUtil.convertTime(time)[1])).setScore(1);
		for(Player pl : Bukkit.getWorld("world").getPlayers())
			pl.setScoreboard(scoreboard);
	}

	private void timer() {
		new BukkitRunnable() {
			public void run() {
				if(!isFinished()) {
					if(time > 0) {
						scoreboard();
						time--;
					} else {
						setFinish();
						stop();
						this.cancel();
					}
				} else this.cancel();
			}
		}.runTaskTimer(Antykacraft.instance, 0L, 20L);
	}

	public void stop() {
		setFinish();
		Bukkit.broadcastMessage("§6[AntykaCraft] " + ChatColor.GREEN + "Fin du raid !!");
		scoreboard.clearSlot(DisplaySlot.SIDEBAR);
		try {
			scoreboard.getObjective("raid").unregister();
		} catch(NullPointerException npe) {}
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.setScoreboard(Antykacraft.sbManager.getMainScoreboard());
		}
	}

	public static List<Raid> getRaids() {
		return raids;
	}
}
