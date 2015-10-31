package fr.mrazerty31.antykacraft.utils.roleplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;

import fr.mrazerty31.antykacraft.Antykacraft;
import fr.mrazerty31.antykacraft.utils.ConfigManager;
import fr.mrazerty31.antykacraft.utils.Utils;

public class Faction {
	private static List<Faction> factions = new ArrayList<Faction>();
	private String name, displayName, raidName, empireName;
	private ChatColor color;
	private Team team;
	private boolean raid, isOnRaid;
	private int account;
	private double balance;

	public Faction(String name, Team team, String displayName, String empireName, String raidName, ChatColor color) {
		this.name = name;
		this.team = team;
		this.displayName = displayName;
		this.empireName = empireName;
		this.raidName = raidName;
		this.color = color;
		this.raid = false;
		this.isOnRaid = false;

		factions.add(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void updateMoney() {
		double totalAmount = 0;
		for(String p : team.getEntries()) {
			try {
				if(Economy.playerExists(p)) {
					double money = Economy.getMoneyExact(p).doubleValue();
					totalAmount += money;
				}
			} catch (UserDoesNotExistException e) {}
		}
		setBalance(totalAmount);
	}

	public double addBalance(double amount) {
		setBalance(getBalance() + amount);
		return getBalance();
	}

	public double removeBalance(double amount) {
		setBalance(getBalance() - amount);
		return getBalance();
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
		saveAccount();
	}

	public int addAccount(int amount) {
		account += amount;
		return account;
	}

	public int removeAccount(int amount) {
		account -= amount;
		return account;
	}

	public void saveAccount() {
		ConfigManager.saveFactionAccount(this);
	}

	public void updateAccount() {
		this.account = ConfigManager.getFactionAccount(this);
		saveAccount();
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmpireName() {
		return empireName;
	}

	public void setEmpireName(String empireName) {
		this.empireName = empireName;
	}

	public String getRaidName() {
		return raidName;
	}

	public void setRaidName(String raidName) {
		this.raidName = raidName;
	}

	public ChatColor getColor() {
		return color;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	public boolean hasRaid() {
		return raid;
	}

	public void setHasRaid(boolean raid) {
		this.raid = raid;
	}

	public boolean isOnRaid() {
		return isOnRaid;
	}

	public void setOnRaid(boolean isOnRaid) {
		this.isOnRaid = isOnRaid;
	}

	public static List<Faction> getFactions() {
		return factions;
	}

	public static int getFactionIndex(Team t) {
		int index = 0;
		for(int i = 0; i < factions.size(); i++) {
			if(factions.get(i).getTeam().equals(t))
				index = i;
		} return index;
	}

	public static Faction getPlayerFaction(Player p) {
		Faction f = null;
		for(Faction ft : factions) {
			if(ft.getTeam().hasEntry(p.getName())) {
				f = ft;
			}
		} return f;
	}

	public static Faction getFactionByTeam(Team t) {
		Faction f = null;
		for(Faction ft : factions) {
			if(ft.getTeam().equals(t)) {
				f = ft;
			}
		} return f;
	}

	public static boolean isFaction(String name) {
		boolean is = false;
		for(Faction f : factions)
			if(f.getName().equalsIgnoreCase(name)) 
				is = true;
		return is;
	}

	public static String balance() {
		String message = Antykacraft.prefix + "§a Classement des factions par richesse : \n";
		List<Double> money = new ArrayList<Double>();
		for(Faction f : factions) {
			f.updateMoney();
			money.add(f.getBalance());
		}
		Collections.sort(money);
		Collections.reverse(money);
		for(int i = 0; i < factions.size(); i++) {
			Faction f = null;
			for(Faction fc : factions) {
				if(fc.getBalance() == money.get(i)) {
					f = fc;
				}
			}
			if(f.getBalance() != 0) {
				message += "§a"+(i+1)+". §6" + Utils.wordMaj(f.getName()) + "§a : §6" + Utils.round((f.getBalance()), 2) + " Drachmes\n";
			}
		}
		return message;
	}

	public static void raid(Player p, Faction a, Faction b) {
		if(!a.equals(b)) {
			if(!a.hasRaid()) {
				if(!a.isOnRaid) {
					Raid raid = new Raid(p, a, b);
					raid.start();
					Bukkit.broadcastMessage(Antykacraft.prefix + "§a" + a.getRaidName() + " lance(nt) un raid contre " + b.getRaidName() + "!");
				} else p.sendMessage(Antykacraft.prefix + "§cVous êtes déjà dans un raid !");
			} else p.sendMessage(Antykacraft.prefix + "§cVotre faction a déjà raid aujourd'hui !");
		} else p.sendMessage(Antykacraft.prefix + "§cVous ne pouvez pas raider votre propre faction !");
	}

	public static Faction getFaction(String name) {
		Faction faction = null;
		for(Faction fac : factions) {
			if(fac.getName().equals(name)) {
				faction = fac;
			}
		}
		return faction;
	}

	@SuppressWarnings("unused")
	public static void init() {
		Scoreboard sb = Antykacraft.sbManager.getMainScoreboard();
		Faction egypte = new Faction("egypte", sb.getTeam("Egypte"), "Egypte", "Egyptien", "le Royaume d'Egypte", ChatColor.BLUE);
		Faction mayas = new Faction("mayas", sb.getTeam("Mayas"), "Mayas", "Maya", "les Mayas", ChatColor.DARK_GREEN);
		Faction rome = new Faction("rome", sb.getTeam("Rome"), "Rome", "Romain", "l'Empire Romain", ChatColor.RED);
		Faction athenes = new Faction("athenes", sb.getTeam("Athènes"), "Grec",  "Athènes", "Athènes", ChatColor.AQUA);
		Faction mauryas = new Faction("mauryas", sb.getTeam("Maurya"), "Mauryas", "Maurya", "l'Empire Maurya", ChatColor.LIGHT_PURPLE);
		Faction perse = new Faction("perse", sb.getTeam("Perse"), "Perse", "Perses", "l'Empire Perse", ChatColor.YELLOW);
		Faction macedoine = new Faction("macedoine", sb.getTeam("Macédoine"), "Macédoine", "Macédoniens", "Macédoine", ChatColor.DARK_PURPLE);

		for(Faction f : factions) {
			f.updateAccount();
			f.updateMoney();
		}
	}
}
