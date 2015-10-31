package fr.mrazerty31.antykacraft.utils;

import org.bukkit.configuration.file.FileConfiguration;

import fr.mrazerty31.antykacraft.Antykacraft;
import fr.mrazerty31.antykacraft.utils.roleplay.Faction;

public class ConfigManager {
	public static FileConfiguration config;
	
	public static void init() {
		config = Antykacraft.config;
	}
		
	public static int getFactionAccount(Faction f) {
		return config.getInt("factions.account." + f.getName() + ".balance");
	}
	
	public static void saveFactionAccount(Faction f) {
		config.set("factions.account." + f.getName() + ".balance", f.getAccount());
		Antykacraft.instance.saveConfig();
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
