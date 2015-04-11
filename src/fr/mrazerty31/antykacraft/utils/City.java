package fr.mrazerty31.antykacraft.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;

import fr.mrazerty31.antykacraft.Antykacraft;

public class City {

	/* Fields */

	private String name, displayName, empire, faction, alternateName;
	private ChatColor color;
	private Team team;
	private boolean raid, isOnRaid;
	private double money;
	private int account;
	public static List<City> cities = new ArrayList<City>();
	public static HashMap<Team, String> teamDisplay = new HashMap<Team, String>();

	/* Constructors */

	public City(String name, String displayName, String empire, String faction, String alternateName, ChatColor color) {
		this.setName(name);
		this.setDisplayName(displayName);
		this.setRaid(false);
		this.setisOnRaid(false);
		this.setEmpire(empire);
		this.setFactionName(faction);
		this.setAlternateName(alternateName);
		this.setColor(color);
	}

	public City(String name, String displayName, Team team, String empire, String faction, String alternateName, ChatColor color) {
		this.setName(name);
		this.setDisplayName(displayName);
		this.setTeam(team);
		this.setRaid(false);
		this.setisOnRaid(false);
		this.setEmpire(empire);
		this.setFactionName(faction);
		this.setAlternateName(alternateName);
		this.setColor(color);
	}

	/* Getters / Setters */

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

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public boolean hasRaid() {
		return raid;
	}

	public void setRaid(boolean raid) {
		this.raid = raid;
	}

	public double getTotalMoney() {
		return this.money;
	}

	public void updateMoney() {
		double totalAmount = 0;
		for(OfflinePlayer p : team.getPlayers()) {
			try {
				@SuppressWarnings("deprecation")
				double money = Economy.getMoney(p.getName().toLowerCase());
				totalAmount += money;
			} catch (UserDoesNotExistException e) {}
		}
		money = totalAmount;
	}

	public int getMoney() {
		return this.account;
	}

	public void setMoney(int amount) {
		this.account = amount;
	}

	public int addMoney(int amount) {
		this.account += amount;
		return this.account;
	}

	public int removeMoney(int amount) {
		this.account -= amount;
		return this.account;
	}

	public void saveMoney() {
		ConfigManager.saveCityMoney(this);
	} 

	public void updateAccount() {
		this.account = ConfigManager.getCityMoney(this);
	}

	public String getEmpire() {
		return this.empire;
	}

	public void setEmpire(String empire) {
		this.empire = empire;
	}

	public ChatColor getColor() {
		return color;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	public void setisOnRaid(boolean b) {
		this.isOnRaid = b;
	}

	public boolean isOnRaid() {
		return this.isOnRaid;
	}
	
	public String getFactionName() {
		return this.faction;
	}
	
	public void setFactionName(String faction) {
		this.faction = faction;
	}
	
	public String getAlternateName() {
		return this.alternateName;
	}
	
	public void setAlternateName(String alternateName) {
		this.alternateName = alternateName;
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
			if(c.getFactionName().equalsIgnoreCase(faction))
				city = c;
		} return city;
	}

	public static int getTeamIndex(Team t) {
		int index = 0;
		for(int i = 0; i < cities.size(); i++) {
			if(cities.get(i).getTeam().equals(t))
				index = i;
		} return index;
	}

	public static City getPlayerCity(Player p) {
		City c = null;
		for(City ct : cities) {
			if(ct.getTeam().getPlayers().contains(p)) {
				c = ct;
			}
		} return c;
	}

	public static City getCityByTeam(Team t) {
		City c = null;
		for(City ct : cities) {
			if(ct.getTeam().equals(t)) {
				c = ct;
			}
		} return c;
	}

	public static boolean isCity(String name) {
		boolean is = false;
		for(City ci : City.cities)
			if(ci.getName().equalsIgnoreCase(name)) 
				is = true;
		return is;
	}
	
	public static boolean isFaction(String faction) {
		boolean is = false;
		for(City ci : City.cities)
			if(ci.getFactionName().equalsIgnoreCase(faction)) 
				is = true;
		return is;
	}

	public static String balanceCity() {
		String message = Antykacraft.prefix + "§a Classement des factions par richesse : \n";
		List<Double> money = new ArrayList<Double>();
		for(City c : cities)
			money.add(c.getTotalMoney());
		Collections.sort(money);
		Collections.reverse(money);
		for(int i = 0; i < money.size(); i++) {
			City c = null;
			for(City ci : cities) 
				if(ci.getTotalMoney() == money.get(i))
					c = ci;
			message += "§a"+(i+1)+". §6" + Utils.wordMaj(c.getEmpire()) + "§a : §6" + Utils.round((c.getTotalMoney() + c.getMoney()), 2) + " Drachmes\n";
		}
		return message;
	}

	public static void raid(Player p, City a, City b) {
		if(!a.equals(b)) {
			if(!a.hasRaid()) {
				if(!a.isOnRaid) {
					Timers.raid(p, a, b);
					Raid.raidKills.put(a, 0);
					Raid.raidKills.put(b, 0);
					Bukkit.broadcastMessage(Antykacraft.prefix + "§aLes" + a.getFactionName() + " lancent un raid contre " + b.getAlternateName() + "!");
				} else p.sendMessage(Antykacraft.prefix + "§cVous êtes déjà dans un raid !");
			} else p.sendMessage(Antykacraft.prefix + "§cVotre faction a déjà raid aujourd'hui !");
		} else p.sendMessage(Antykacraft.prefix + "§cVous ne pouvez pas raider votre propre faction !");
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

	public static void init() {
		Scoreboard sb = Antykacraft.sbManager.getMainScoreboard();
		City tikal = new City("tikal", "Tikal", sb.getTeam("Mayas"), "maya", "Mayas", "les Mayas", ChatColor.DARK_GREEN);
		cities.add(tikal);
		City thebes = new City("memphis", "Memphis", sb.getTeam("Egypte"), "égyptien", "Egyptiens", "L'Egypte", ChatColor.BLUE);
		cities.add(thebes);
		City rome = new City("rome", "Rome", sb.getTeam("Rome"), "romain", "Romains", "Rome", ChatColor.RED);
		cities.add(rome);
		for(City c : cities) {
			c.updateMoney();
			c.updateAccount();
		}

		/* Others */

		teamDisplay.put(sb.getTeam("Mayas"), "Tikal");
		teamDisplay.put(sb.getTeam("Egypte"), "Memphis");
		teamDisplay.put(sb.getTeam("Rome"), "Rome");
	}
}
