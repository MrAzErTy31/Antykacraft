package fr.mrazerty31.antykacraft.utils;

import org.bukkit.Bukkit;

import fr.mrazerty31.antykacraft.Antykacraft;

public class TimeUtil {
	public static String formatTime(int m, int s) {
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

	public static int[] convertTime(int t) {
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