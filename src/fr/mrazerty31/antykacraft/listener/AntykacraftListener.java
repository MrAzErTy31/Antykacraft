package fr.mrazerty31.antykacraft.listener;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import fr.mrazerty31.antykacraft.Antykacraft;
import fr.mrazerty31.antykacraft.utils.ConfigManager;
import fr.mrazerty31.antykacraft.utils.roleplay.Faction;
import fr.mrazerty31.antykacraft.utils.roleplay.Raid;

public class AntykacraftListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
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

	@EventHandler
	public void armorUnbreak(EntityDamageByEntityEvent e) {
		if((e.getDamager() instanceof Player) && (e.getEntity() instanceof Player)) {
			Player p = (Player) e.getEntity();
			Player d = (Player) e.getDamager();
			if(isInEventWorld(p) && isInEventWorld(d)) {
				try {
					for(ItemStack it : p.getInventory().getArmorContents()) {
						it.setDurability(it.getDurability());
					}
				} catch(Exception ex) {}
			}
		}
	}

	@EventHandler
	public void killOnRaid(PlayerDeathEvent e) {
		if(e.getEntity().getKiller() instanceof Player) {
			Player a = e.getEntity();
			Player b = a.getKiller();
			try {
				if(Faction.getPlayerFaction(a).isOnRaid() && Faction.getPlayerFaction(b).isOnRaid()) {
					Faction fB = Faction.getPlayerFaction(b);
					for(Raid raid : Raid.getRaids()) {
						if(raid.getAttacker().equals(fB) || raid.getDefender().equals(fB)) {
							int currentKills = raid.getKills().get(fB);
							try {
								raid.getKills().remove(fB);
							} catch(Exception ex) {
								ex.printStackTrace();
							} finally {
								raid.getKills().put(fB, currentKills + 1);
							}
						}
					}
				}
			} catch(NullPointerException npe) {}
		}
	}

	public static boolean isInEventWorld(Player p) {
		if(p.getWorld().getName().equalsIgnoreCase("event")) 
			return true;
		else return false;
	}
}
