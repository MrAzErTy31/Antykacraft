package fr.mrazerty31.antykacraft.utils;

import org.bukkit.configuration.file.FileConfiguration;

import fr.mrazerty31.antykacraft.Antykacraft;

public class ConfigManager {
	public static FileConfiguration config;
	
	public static void init() {
		config = Antykacraft.config;
	}
	
	public static void saveCityMoney(City c) {
		config.set("cities.account." + c.getName() + ".money", c.getMoney());
		Antykacraft.instance.saveConfig();
	}
	
	public static int getCityMoney(City c) {
		return config.getInt("cities.account." + c.getName() + ".money");
	}
	
	public static void setAnnounce(String announce) {
		config.set("antykacraft.announce.message", announce);
		Antykacraft.instance.saveConfig();
	}
	
	public static String getAnnounce() {
		return config.getString("antykacraft.announce.message", "Annonce de base"); 
	}
	
	public static void setAnnounceToggle(boolean b) {
		config.set("antykacraft.announce.toggle", b);
		Antykacraft.instance.saveConfig();
	}
	
	public static boolean announceIsToggled() {
		return config.getBoolean("antykacraft.announce.toggle", false);
	}
}
