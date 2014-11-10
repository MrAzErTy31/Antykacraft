package fr.mrazerty31.antykacraft.listener;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.mrazerty31.antykacraft.Antykacraft;
import fr.mrazerty31.antykacraft.utils.ConfigManager;

public class AntykacraftListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if(p.getName().equalsIgnoreCase("Susucre_")) 
			p.setPlayerListName(ChatColor.DARK_GREEN + "Susucre");
		else if(p.getName().equalsIgnoreCase("Illinor_"))
			p.setPlayerListName(ChatColor.DARK_GREEN + "Illinor");
		if(!Antykacraft.manload) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "manload");
			Antykacraft.manload = true;
		} if(ConfigManager.announceIsToggled()) {
			p.sendMessage("§6[Annonce] §a" + ConfigManager.getAnnounce());
		}
	}

	@SuppressWarnings("rawtypes")
	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		try {

			Iterator bi = e.blockList().iterator();
			while (bi.hasNext()) if (((Block)bi.next()).getType() != Material.TNT) bi.remove();
		} catch(Exception ex) {}
	}

	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		if(e.getPlayer().getWorld().getName().equals("world")) {
			if(e.getBlock().getType() == Material.TNT) 
				e.setCancelled(true);
		}
	}

	/*
	@EventHandler
	public void changeWorld(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		String worldName = p.getWorld().getName();
		if(worldName.equals("map_build")) {
			if(!Utils.buildAllow.contains(p) || !p.isOp()) {
				p.sendMessage(Antykacraft.prefix + "§cTPs interdits Map build !");
				p.teleport(Bukkit.getWorld("world").getSpawnLocation());
			}
		}
	}
	*/
}
