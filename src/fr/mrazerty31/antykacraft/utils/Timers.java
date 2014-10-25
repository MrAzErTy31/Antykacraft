package fr.mrazerty31.antykacraft.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.mrazerty31.antykacraft.Antykacraft;

public class Timers {
	public static BukkitTask fTask, eTask, rTask;
	public static Scoreboard sb;

	public static void init() {
		sb = Antykacraft.sbManager.getNewScoreboard();
	}
	
	public static void startEventTimer(final Player p, int c) {
		/* Première phase */
		final int t = c * 60; // Conversion en secondes
		final Antykacraft plugin = Antykacraft.instance;
			fTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
				int startingTime = 11;
				int countdown = t;
				public void run() {
					if(startingTime != 0) {
						startingTime--;
						if(startingTime == 10)
							for(Player p : Bukkit.getWorld("event").getPlayers())
								p.sendMessage(ChatColor.GREEN + "Début du jeu dans " + "§6" + startingTime + " secondes");
						else if(startingTime <= 5 && startingTime > 1) {
							for(Player p : Bukkit.getWorld("event").getPlayers()) {
								p.sendMessage(ChatColor.GREEN + "Début du jeu dans " + "§6" + startingTime + " secondes");
								p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
							}
						} else if(startingTime == 1) {
							for(Player p : Bukkit.getWorld("event").getPlayers()) {
								p.sendMessage(ChatColor.GREEN + "Début du jeu dans " + "§6" + startingTime + " seconde");
								p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
							}
						}
					} else {
						String m = cStSaM(countdown)[0] > 1 ? "minutes" : "minute";
						p.sendMessage(ChatColor.GREEN + "Le jeu commence ! Et il finit dans " + cStSaM(countdown)[0] + " " + m + ".");
						Bukkit.getScheduler().cancelTask(fTask.getTaskId());
						Timers.eventScoreboard(p, t, 0);
					}
				}
			}, 0L, 20L);
	}
	
	public static void raid(Player p, City c, int m, int s) {
		raidScoreboard(p, c, m, s);
		raidSbTimer(m, s, p, c);
		for(Team t : Antykacraft.sbManager.getMainScoreboard().getTeams()) {
			Team te = sb.registerNewTeam(t.getName());
			for(OfflinePlayer pl : t.getPlayers())
				te.addPlayer(pl);
			te.setAllowFriendlyFire(false);
			te.setPrefix(te.getPrefix());
		}
	}
	
	public static void eventScoreboard(Player p, int min, int sec) {
		try{Bukkit.getScheduler().cancelTask(fTask.getTaskId());}catch(Exception e) {}
		try{sb.getObjective("event").unregister();} catch(Exception e) {}
		Objective time = sb.registerNewObjective("event", "dummy");
		time.setDisplayName("§a§lChrono");
		time.setDisplaySlot(DisplaySlot.SIDEBAR);
		time.getScore("§6>> " + ChatColor.AQUA + formatTime(min, sec)).setScore(1);
		eventSbTimer(min, sec, p);
	}
	
	public static void raidScoreboard(Player p, City c, int min, int sec) {
		try{
			sb.getObjective("raid").unregister();
		} catch(Exception e) {}
		Objective time = sb.registerNewObjective("raid", "dummy");
		time.setDisplayName("§6§lRaid");
		time.setDisplaySlot(DisplaySlot.SIDEBAR);
			time.getScore("§6" + City.getPlayerCity(p).getDisplayName()).setScore(5);
			time.getScore("§6---- VS ----").setScore(4);
			time.getScore("§6" + c.getDisplayName()).setScore(3);
		time.getScore("§6--------").setScore(2);
		time.getScore("§6>> " + ChatColor.AQUA + formatTime(min, sec)).setScore(1);
		for(Player pl : Bukkit.getWorld("world").getPlayers())
			pl.setScoreboard(sb);
	}

	static void eventSbTimer(int m, int s, final Player p) {
		final int totalSeconds = (m * 60) + s;
		eTask = Bukkit.getScheduler().runTaskTimer(Antykacraft.instance, new Runnable() {
			int tscds = totalSeconds + 1;
			public void run() {
				if(tscds > 0) {
					tscds--;
					eventScoreboard(p, cStSaM(tscds)[0], cStSaM(tscds)[1]);
				} else endEventTimer(eTask.getTaskId());
			}
		}, 0L, 20L);
	}
	
	public static void raidSbTimer(int m, int s, final Player p, final City c) {
		final int totalSeconds = (m * 60) + s;
		rTask = Bukkit.getScheduler().runTaskTimer(Antykacraft.instance, new Runnable() {
			int tscds = totalSeconds;
			public void run() {
				if(tscds > 0) {
					raidScoreboard(p, c, cStSaM(tscds)[0], cStSaM(tscds)[1]);
					tscds--;
				} else endRaid(rTask.getTaskId());
			}
		}, 0L, 20L);
	}
	
	static void endEventTimer(int taskid) {
		Bukkit.broadcastMessage("§6[AntykaCraft] " + ChatColor.GREEN + "Fin du jeu !!");
		sb.clearSlot(DisplaySlot.SIDEBAR);
		sb.getObjective("event").unregister();
		Bukkit.getScheduler().cancelTask(taskid);
		for(Player p : Bukkit.getOnlinePlayers())
			p.setScoreboard(Antykacraft.sbManager.getMainScoreboard());
	}
	
	static void endRaid(int taskid) {
		Bukkit.broadcastMessage("§6[AntykaCraft] " + ChatColor.GREEN + "Fin du raid !!");
		sb.clearSlot(DisplaySlot.SIDEBAR);
		sb.getObjective("raid").unregister();
		Bukkit.getScheduler().cancelTask(taskid);
		for(Player p : Bukkit.getOnlinePlayers())
			p.setScoreboard(Antykacraft.sbManager.getMainScoreboard());
	}

	static String formatTime(int m, int s) {
		String st = "";
		if(m < 10 && s < 10) {
			st = "0" + m + ":0" + s;
		} else if(m < 10 && s >= 10) {
			st = "0" + m + ":" + s;
		} else if(s < 10 && m >= 10) {
			st = m + ":0" + s;
		} else if(s >= 10 && m >= 10) {
			st = m + ":" + s;
		}	
		return st;
	}

	/** 
	 * @param Time to convert
	 * @return int[] : 0 => Min, 1 => Secs
	 */
	static int[] cStSaM(int t) {
		int[] sAM = new int[2];
		double dM = (double) t / 60.0;
		int m = (int) dM;
		int s = (int) (60 * (dM - m));
		sAM[0] = m;
		sAM[1] = s;
		return sAM;
	}

	public static void startAnnounceTimer() {
		Bukkit.getScheduler().runTaskTimer(Antykacraft.instance, new Runnable() {
			public void run() {
				if(ConfigManager.announceIsToggled()) {
					Bukkit.broadcastMessage("§6[Annonce] §a" + ConfigManager.getAnnounce());
				}
			}
		}, 0L, 36000L); // 30 minutes
	}
}