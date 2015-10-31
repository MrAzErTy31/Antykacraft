package fr.mrazerty31.antykacraft.utils.roleplay;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class City {

	/* Fields */

	private String name, displayName;
	private ChatColor color;
	private Faction faction;
	public static List<City> cities = new ArrayList<City>();
	public City(String name, String displayName, Faction faction) {
		this.setName(name);
		this.setDisplayName(displayName);
		this.setFaction(faction);
		this.setColor(color);
		cities.add(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
	public void setColor(ChatColor color) {
		this.color = color;
	}
	
	/* Static Methods */

	public static City getCity(String name) {
		City city = null;
		for(City c : cities) {
			if(c.getName().equalsIgnoreCase(name))
				city = c;
		} return city;
	}

	public static City getCityByFaction(String faction) {
		City city = null;
		for(City c : cities) {
			if(c.getFaction().getName().equalsIgnoreCase(faction))
				city = c;
		} return city;
	}

	public static boolean isCity(String name) {
		boolean is = false;
		for(City ci : City.cities)
			if(ci.getName().equalsIgnoreCase(name)) 
				is = true;
		return is;
	}

	/*try {
			if(!City.getPlayerCity(p).isOnRaid()) {
			City c = City.getCity(n.toLowerCase());
			if(!c.equals(City.getPlayerCity(p))) {
				Bukkit.broadcastMessage(Antykacraft.prefix + "§a" + p.getName() + "(" + City.getPlayerCity(p).getDisplayName() + 
						") lance un raid contre " + c.getDisplayName() + " !");
				Timers.raid(p, c, 20, 0);
				c.setRaid(true);
			} else p.sendMessage(Antykacraft.prefix + ChatColor.RED + "Vous ne pouvez pas raid contre votre faction !");
			}
		} catch(Exception e) {p.sendMessage(Antykacraft.prefix + "§cVille inconnue.");
		e.printStackTrace();
	 */

	@SuppressWarnings("unused")
	public static void init() {
		City tikal = new City("tikal", "Tikal", Faction.getFaction("mayas"));
	}
}
