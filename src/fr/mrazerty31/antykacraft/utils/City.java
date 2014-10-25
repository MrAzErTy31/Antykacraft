package fr.mrazerty31.antykacraft.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;

import fr.mrazerty31.antykacraft.Antykacraft;

public class City {

	/* Fields */

	private String name, displayName, empire;
	private Team team;
	private boolean raid;
	private double money;
	private int account;
	public static List<City> cities = new ArrayList<City>();
	public static HashMap<Team, String> teamDisplay = new HashMap<Team, String>();

	/* Constructors */

	public City(String name, String displayName) {
		this.setName(name);
		this.setDisplayName(displayName);
		this.setRaid(false);
	}

	public City(String name, String displayName, Team team, String empire) {
		this.setName(name);
		this.setDisplayName(displayName);
		this.setTeam(team);
		this.setRaid(false);
		this.setEmpire(empire);
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
				BigDecimal money = Economy.getMoneyExact(p.getName());
				totalAmount += money.doubleValue();
			} catch (UserDoesNotExistException e) {
				e.printStackTrace();
			}
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

	/* Static Methods */

	public static City getCity(String name) {
		City city = null;
		for(City c : cities) {
			if(c.getName().equalsIgnoreCase(name))
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
			if(ct.getTeam().equals(Teams.getTeam(p))) {
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

	public static void init() {
		Scoreboard sb = Antykacraft.sbManager.getMainScoreboard();
		City tikal = new City("tikal", "Tikal", sb.getTeam("Mayas"), "maya");
		cities.add(tikal);
		City thebes = new City("thebes", "Thèbes", sb.getTeam("Egypte"), "égyptien");
		cities.add(thebes);
		City athenes = new City("athenes", "Athènes", sb.getTeam("Grece"), "grec");
		cities.add(athenes);
		for(City c : cities) {
			c.updateMoney();
			c.updateAccount();
		}

		/* Others */

		teamDisplay.put(sb.getTeam("Mayas"), "Tikal");
		teamDisplay.put(sb.getTeam("Egypte"), "Thèbes");
		teamDisplay.put(sb.getTeam("Grecs"), "Athènes");
	}


}
