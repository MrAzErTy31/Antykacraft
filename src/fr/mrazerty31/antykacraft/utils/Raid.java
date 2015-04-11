package fr.mrazerty31.antykacraft.utils;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class Raid {
	private City attack;
	private City defend;
	private Player launcher;
	private boolean isFinished;
	public static HashMap<City, Integer> raidKills = new HashMap<City, Integer>();
	
	public Raid(Player launcher, City attack, City defend) {
		this.launcher = launcher;
		this.attack = attack;
		this.defend = defend;
		this.isFinished = false;
	}
	
	public Player getLauncher() {
		return launcher;
	}
	
	public void setLauncher(Player launcher) {
		this.launcher = launcher;
	}
	
	public City getAttacker() {
		return attack;
	}
	
	public void setAttacker(City attack) {
		this.attack = attack;
	}
	
	public City getDefender() {
		return defend;
	}
	
	public void setDefender(City defend) {
		this.defend = defend;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public void setFinish() {
		this.isFinished = true;
		getAttacker().setisOnRaid(false);
		getDefender().setisOnRaid(false);
		raidKills.clear();
	}
}
