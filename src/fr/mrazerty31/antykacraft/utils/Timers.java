package fr.mrazerty31.antykacraft.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.mrazerty31.antykacraft.Antykacraft;

public class Timers {
	public static Scoreboard sb;
	public static List<Raid> raids = new ArrayList<Raid>();

	public static void init() {
		sb = Antykacraft.sbManager.getNewScoreboard();
	}

	public static void startEventTimer(final Player p, int c) {
		/* Première phase */
		final int t = c * 60; // Conversion en secondes
		final Antykacraft plugin = Antykacraft.instance;
		Bukkit.getScheduler().runTaskTimer(plugin, new BukkitRunnable() {
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
					Timers.eventScoreboard(p, t, 0);
					cancel();
				}
			}
		}, 0L, 20L);
	}

	public static void raid(Player p, City a, City b) {
		int m = 20, s = 0;
		Raid raid = new Raid(p, a, b);
		raids.add(raid);
		raid.getAttacker().setRaid(true);
		raid.getAttacker().setisOnRaid(true);
		raid.getDefender().setisOnRaid(true);
		raidScoreboard(raid, m, s);
		raidSbTimer(m, s, raid);
		for(Team t : Antykacraft.sbManager.getMainScoreboard().getTeams()) {
			try {
				Team te = sb.registerNewTeam(t.getName());
				te.setAllowFriendlyFire(false);
				City ct = City.getCityByTeam(t);
				te.setPrefix("" + ct.getColor());
				for(OfflinePlayer pl : t.getPlayers()) {
					te.addPlayer(pl);
				} for(Player pla : Bukkit.getOnlinePlayers()) {
					pla.setPlayerListName(p.getName());
				}
			} catch(Exception ex) {}
		}
	}

	public static void eventScoreboard(Player p, int min, int sec) {
		try{sb.getObjective("event").unregister();} catch(Exception e) {}
		Objective time = sb.registerNewObjective("event", "dummy");
		time.setDisplayName("§a§lChrono");
		time.setDisplaySlot(DisplaySlot.SIDEBAR);
		time.getScore("§6>> " + ChatColor.AQUA + formatTime(min, sec)).setScore(1);
		eventSbTimer(min, sec, p);
	}

	public static void raidScoreboard(Raid r, int min, int sec) {
		City a = r.getAttacker();
		City b = r.getDefender();
		try{
			sb.getObjective("raid").unregister();
		} catch(Exception e) {}
		Objective time = sb.registerNewObjective("raid", "dummy");
		time.setDisplayName("§6§lRaid");
		time.setDisplaySlot(DisplaySlot.SIDEBAR);
		time.getScore("§6" + a.getFactionName() + " (" + Raid.raidKills.get(a) + " kills)").setScore(5);
		time.getScore("§6---- VS ----").setScore(4);
		time.getScore("§6" + b.getFactionName() + " (" + Raid.raidKills.get(b) + " kills)").setScore(3);
		time.getScore("§6--------").setScore(2);
		time.getScore("§6>> " + ChatColor.AQUA + formatTime(min, sec)).setScore(1);
		for(Player pl : Bukkit.getWorld("world").getPlayers())
			pl.setScoreboard(sb);
	}

	static void eventSbTimer(int m, int s, final Player p) {
		final int totalSeconds = (m * 60) + s;
		new BukkitRunnable() {
			int tscds = totalSeconds + 1;
			public void run() {
				if(tscds > 0) {
					tscds--;
					eventScoreboard(p, cStSaM(tscds)[0], cStSaM(tscds)[1]);
				} else {
					endEventTimer();
					cancel();
				}
			}
		}.runTaskTimer(Antykacraft.instance, 0L, 20L);
	}

	public static void raidSbTimer(int m, int s, final Raid r) {
		final int totalSeconds = (m * 60) + s;
		new BukkitRunnable() {
			int tscds = totalSeconds;
			public void run() {
				if(!r.isFinished()) {
					if(tscds > 0) {
						raidScoreboard(r, cStSaM(tscds)[0], cStSaM(tscds)[1]);
						tscds--;
					} else {
						r.setFinish();
						endRaid();
						cancel();
					}
				} else cancel();
			}
		}.runTaskTimer(Antykacraft.instance, 0L, 20L);
	}

	static void endEventTimer() {
		Bukkit.broadcastMessage("§6[AntykaCraft] " + ChatColor.GREEN + "Fin du jeu !!");
		sb.clearSlot(DisplaySlot.SIDEBAR);
		sb.getObjective("event").unregister();
		for(Player p : Bukkit.getOnlinePlayers())
			p.setScoreboard(Antykacraft.sbManager.getMainScoreboard());
	}

	public static void endRaid() {
		Bukkit.broadcastMessage("§6[AntykaCraft] " + ChatColor.GREEN + "Fin du raid !!");
		sb.clearSlot(DisplaySlot.SIDEBAR);
		sb.getObjective("raid").unregister();
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