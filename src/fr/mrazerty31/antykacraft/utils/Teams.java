package fr.mrazerty31.antykacraft.utils;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import fr.mrazerty31.antykacraft.Antykacraft;

public class Teams {
	
	public static Team[] cityTeams() {
		Team egypte = Antykacraft.sbManager.getMainScoreboard().getTeam("Egypte"),
			mayas = Antykacraft.sbManager.getMainScoreboard().getTeam("Mayas"),
			grece = Antykacraft.sbManager.getMainScoreboard().getTeam("Grece");
		return new Team[] {egypte, mayas, grece};
	}
	
	@SuppressWarnings("deprecation")
	public static Team getTeam(Player p) {
		return Antykacraft.sbManager.getMainScoreboard().getPlayerTeam(p);
	}
}
