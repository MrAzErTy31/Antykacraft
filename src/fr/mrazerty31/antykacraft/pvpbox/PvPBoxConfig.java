package fr.mrazerty31.antykacraft.pvpbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

import fr.mrazerty31.antykacraft.Antykacraft;
import fr.mrazerty31.antykacraft.utils.Utils;

public class PvPBoxConfig {
	private static FileConfiguration config = Antykacraft.config;

	/** GETTERS **/

	public static String getDefaultPvPBoxArena() {
		return config.getString("antykacraft.pvpbox.defaultArena");
	}

	public static Location getPvPBoxArenaLocation(String arena) {
		double x = config.getDouble("antykacraft.pvpbox.arenas." + arena + ".location.x");
		double y = config.getDouble("antykacraft.pvpbox.arenas." + arena + ".location.y");
		double z = config.getDouble("antykacraft.pvpbox.arenas." + arena + ".location.z");
		float yaw = (float) config.getDouble("antykacraft.pvpbox.arenas." + arena + ".location.yaw");
		float pitch = (float) config.getDouble("antykacraft.pvpbox.arenas." + arena + ".location.pitch");
		return new Location(Bukkit.getWorld("event"), x, y, z, yaw, pitch);
	}

	public static boolean pvpBoxArenaExist(String arena) {
		return config.getConfigurationSection("antykacraft.pvpbox.arenas").getKeys(false).contains(arena);
	}

	public static String getPvPBoxArenaList() {
		Set<String> configSecList = config.getConfigurationSection("antykacraft.pvpbox.arenas").getKeys(false);
		String message = "";
		if(configSecList.size() != 0) {
			message = ChatColor.GOLD + "Liste des arènes PvPBox (" + ChatColor.AQUA + configSecList.size() + ChatColor.GOLD + ") : \n";
			for(String sec : configSecList) {
				message += ChatColor.GOLD + "- " + ChatColor.AQUA + Utils.wordMaj(sec) + "\n";
			}
		} else {
			message = ChatColor.RED + "Il n'y a aucune arène PvPBox pour le moment ...";
		}
		return message;
	}

	public static List<UUID> getVipsUUIDs() {
		Set<String> vips = config.getConfigurationSection("antykacraft.vips").getKeys(false);
		List<UUID> vipsList = new ArrayList<UUID>();
		for(String vipString : vips) vipsList.add(UUID.fromString(vipString));
		return vipsList;
	}

	public static Set<String> getVipsName() {
		return config.getConfigurationSection("antykacraft.vips").getKeys(true);
	}

	public static int getPvPBoxArenaTotalNumber() {
		return config.getConfigurationSection("antykacraft.pvpbox.arenas").getKeys(false).size();
	}

	public static int getPvPBoxKills(Player p) {
		return config.getInt("antykacraft.pvpbox.stats." + p.getUniqueId() + ".kills");
	}

	public static int getPvPBoxDeaths(Player p) {
		return config.getInt("antykacraft.pvpbox.stats." + p.getUniqueId() + ".deaths");
	}

	public static String getRatioPvPBoxRank(int limit) {
		if(limit != 0) {
			String message = ChatColor.GOLD + "[PvPBox] " + ChatColor.GREEN + "Voici le classement PvPBox (ratio) : \n" + ChatColor.GOLD + "--------------------------------\n";
			ConfigurationSection configsec = config.getConfigurationSection("antykacraft.pvpbox.stats");
			Set<String> uuids = configsec.getKeys(false);
			List<Double> pvpboxPlayerRatio = new ArrayList<Double>();
			Multimap<Double, String> pK = TreeMultimap.create();
			if(!(limit <= uuids.size())) limit = uuids.size();
			for(String s : uuids) {
				String p = config.getString("antykacraft.pvpbox.stats." + s + ".name");
				int k = config.getInt("antykacraft.pvpbox.stats." + s + ".kills"),
					d = config.getInt("antykacraft.pvpbox.stats." + s + ".deaths");
				double r = getFreeRatio(k, d);
				pK.put(r, p);
				pvpboxPlayerRatio.add(r);
			}
			Collections.sort(pvpboxPlayerRatio);
			Collections.reverse(pvpboxPlayerRatio);
			int pseudo = 0;
			for(int i = 0; i < limit; i++) {
				Object[] f = (Object[]) pK.get(pvpboxPlayerRatio.get(i)).toArray();
				if(((String) f[pseudo]).contains("_")) Utils.replaceLast(((String) f[pseudo]), "_", "");
				message += ChatColor.GOLD + "" + (i + 1) + ". " + f[pseudo] + " : " + ChatColor.AQUA + pvpboxPlayerRatio.get(i) + " (" + getOfflinePlayerPvPBoxKills((String) f[pseudo]) + " Kills & " + getOfflinePlayerPvPBoxDeaths((String) f[pseudo]) + " Morts).\n";
				if(pK.get(pvpboxPlayerRatio.get(i)).size() > 1) {
					if(pseudo == pK.get(pvpboxPlayerRatio.get(i)).size() - 1) pseudo = 0;
					else pseudo ++;
				}
			}
			return message;
		} else return "";
	}

	public static String getPvPBoxRank(int limit) {
		if(limit != 0) {
			String message = ChatColor.GOLD + "[PvPBox] " + ChatColor.GREEN + "Voici le classement PvPBox (kills) : \n" + ChatColor.GOLD + "--------------------------------\n";
			ConfigurationSection configsec = config.getConfigurationSection("antykacraft.pvpbox.stats");
			Set<String> uuids = configsec.getKeys(false);
			List<Integer> pvpboxPlayerKills = new ArrayList<Integer>();
			Multimap<Integer, String> pK = TreeMultimap.create();
			if(!(limit <= uuids.size())) limit = uuids.size();
			for(String s : uuids) {
				String p = config.getString("antykacraft.pvpbox.stats." + s + ".name");
				Integer k = config.getInt("antykacraft.pvpbox.stats." + s + ".kills");
				pK.put(k, p);
				pvpboxPlayerKills.add(k);
			}
			Collections.sort(pvpboxPlayerKills);
			Collections.reverse(pvpboxPlayerKills);
			int pseudo = 0;
			for(int i = 0; i < limit; i++) {
				Object[] f = (Object[]) pK.get(pvpboxPlayerKills.get(i)).toArray();
				if(((String) f[pseudo]).contains("_")) Utils.replaceLast(((String) f[pseudo]), "_", "");
				message += ChatColor.GOLD + "" + (i + 1) + ". " + f[pseudo] + " : " + ChatColor.AQUA + pvpboxPlayerKills.get(i) + " Kills\n";
				if(pK.get(pvpboxPlayerKills.get(i)).size() > 1) {
					if(pseudo == pK.get(pvpboxPlayerKills.get(i)).size() - 1) pseudo = 0;
					else pseudo ++;
				}
			}
			return message;
		} else return "";
	}

	public static double getPlayerRatio(Player p) {
		double ratio = 0;
		if(!(getPvPBoxKills(p) == 0) && !(getPvPBoxDeaths(p) == 0)) 
			ratio = (double) (((double) getPvPBoxKills(p)) / ((double) getPvPBoxDeaths(p)));
		return Utils.round(ratio, 3);
	}

	public static double getFreeRatio(int kills, int deaths) {
		double ratio = 0;
		if(!(kills == 0) && !(deaths == 0)) 
			ratio = (double) ((double) kills / ((double) deaths));
		return Utils.round(ratio, 3);
	}

	public static String getOfflinePlayerPvPBoxStats(String p, boolean himself) {
		String message = "", namePl = "";
		ConfigurationSection configsec = config.getConfigurationSection("antykacraft.pvpbox.stats");
		Set<String> uuids = configsec.getKeys(false);
		boolean exist = false;
		int kills = 0, death = 0;
		for(String s : uuids) {
			namePl = config.getString("antykacraft.pvpbox.stats." + s + ".name");
			if(namePl.equalsIgnoreCase(p)) {
				kills = config.getInt("antykacraft.pvpbox.stats." + s + ".kills");
				death = config.getInt("antykacraft.pvpbox.stats." + s + ".deaths");
				exist = true;
				break;
			}
		}
		if(exist == true) {
			if(himself == false) 	
				message += ChatColor.GOLD + "[PvPBox] " + ChatColor.GREEN + "Voici les stats PvPBox de " + namePl + " : \n" + ChatColor.GOLD + "--------------------------------\n";
			else 
				message = ChatColor.GOLD + "[PvPBox] " + ChatColor.GREEN + "Voici vos stats PvPBox : \n" + ChatColor.GOLD + "--------------------------------\n";
			message += ChatColor.GOLD + "- Kills : " + ChatColor.AQUA + kills + "\n";
			message += ChatColor.GOLD + "- Morts : " + ChatColor.AQUA + death + "\n";
			message += ChatColor.GOLD + "- Ratio : " + ChatColor.AQUA + getFreeRatio(kills, death);
		} else message = ChatColor.GOLD + "[PvPBox] " + ChatColor.RED + "Le joueur spécifié n'existe pas ou n'a pas encore fait de PvPBox.";

		return message;
	}
	
	public static int getOfflinePlayerPvPBoxKills(String p) {
		int k = 0;
		String namePl;
		ConfigurationSection configsec = config.getConfigurationSection("antykacraft.pvpbox.stats");
		Set<String> uuids = configsec.getKeys(false);
		for(String s : uuids) {
			namePl = config.getString("antykacraft.pvpbox.stats." + s + ".name");
			if(namePl.equalsIgnoreCase(p)) {
				k = config.getInt("antykacraft.pvpbox.stats." + s + ".kills");
				break;
			}
		}
		return k;
	}
	
	public static int getOfflinePlayerPvPBoxDeaths(String p) {
		int d = 0;
		String namePl;
		ConfigurationSection configsec = config.getConfigurationSection("antykacraft.pvpbox.stats");
		Set<String> uuids = configsec.getKeys(false);
		for(String s : uuids) {
			namePl = config.getString("antykacraft.pvpbox.stats." + s + ".name");
			if(namePl.equalsIgnoreCase(p)) {
				d = config.getInt("antykacraft.pvpbox.stats." + s + ".deaths");
				break;
			}
		}
		return d;
	}
	
	public static double getOfflinePlayerPvPBoxRatio(String p) {
		return getFreeRatio(getOfflinePlayerPvPBoxKills(p), getOfflinePlayerPvPBoxDeaths(p));
	}

	/** SETTERS **/

	public static void createNewPvPBoxArena(Player p, String name) {
		config.set("antykacraft.pvpbox.arenas." + name + ".location.x", p.getLocation().getBlockX());
		config.set("antykacraft.pvpbox.arenas." + name + ".location.y", p.getLocation().getBlockY());
		config.set("antykacraft.pvpbox.arenas." + name + ".location.z", p.getLocation().getBlockZ());
		config.set("antykacraft.pvpbox.arenas." + name + ".location.yaw", p.getLocation().getYaw());
		config.set("antykacraft.pvpbox.arenas." + name + ".location.pitch", p.getLocation().getPitch());

		Antykacraft.instance.saveConfig();
	}

	public static void setDefaultPvPBoxArena(String name) {
		config.set("antykacraft.pvpbox.defaultArena", name);
		if(config.getConfigurationSection("antykacraft.pvpbox.arenas").getKeys(false).size() == 0) 
			config.set("antykacraft.pvpbox.arenas.defaultArena", "none");
		Antykacraft.instance.saveConfig();
	}

	public static void removePvPBoxArena(String name) {
		config.set("antykacraft.pvpbox.arenas." + name, null);
		if(config.getConfigurationSection("antykacraft.pvpbox.arenas").getKeys(false).contains(name)) 
			config.set("antykacraft.pvpbox.arenas.defaultArena", "none");
		Antykacraft.instance.saveConfig();
	}

	public static void setVip(Player p) {
		config.set("antykacraft.vip." + p.getUniqueId(), p.getName());
		Antykacraft.instance.saveConfig();
	}

	public static void addKill(Player p) {
		int newKills = getPvPBoxKills(p) + 1;
		if(config.getString("antykacraft.pvpbox.stats." + p.getUniqueId() + ".name") == null) 
			config.set("antykacraft.pvpbox.stats." + p.getUniqueId() + ".name", p.getName());
		config.set("antykacraft.pvpbox.stats." + p.getUniqueId() + ".kills", newKills);
		Antykacraft.instance.saveConfig();
	}

	public static void addDeath(Player p) {
		int newDeaths = getPvPBoxDeaths(p) + 1;
		if(config.getString("antykacraft.pvpbox.stats." + p.getUniqueId() + ".name") == null) 
			config.set("antykacraft.pvpbox.stats." + p.getUniqueId() + ".name", p.getName());
		config.set("antykacraft.pvpbox.stats." + p.getUniqueId() + ".deaths", newDeaths);
		Antykacraft.instance.saveConfig();
	}
}
