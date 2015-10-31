package fr.mrazerty31.antykacraft.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import fr.mrazerty31.antykacraft.Antykacraft;
import fr.mrazerty31.antykacraft.libs.ParticleEffect;
import fr.mrazerty31.antykacraft.pvpbox.PvPBoxConfig;
import fr.mrazerty31.antykacraft.pvpbox.PvPBoxGui;
import fr.mrazerty31.antykacraft.pvpbox.kits.Kit;
import fr.mrazerty31.antykacraft.pvpbox.spells.MainAbility.HitAbility;
import fr.mrazerty31.antykacraft.pvpbox.spells.MainAbility.RightAbility;

public class PvPBoxListener implements Listener {
	private List<Material> allowClick = new ArrayList<Material>();
	public static HashMap<Player, Kit> playerKits = new HashMap<Player, Kit>();
	public static List<Player> cooldown = new ArrayList<Player>();
	public static List<Player> exploImmu = new ArrayList<Player>();

	public PvPBoxListener() {
		allowClick.add(Material.ENCHANTMENT_TABLE);
		allowClick.add(Material.STONE_BUTTON);
		allowClick.add(Material.WOOD_BUTTON);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Antykacraft.instance, new Runnable() {
			public void run() {
				try {
					for(Player p : Bukkit.getWorld("event").getPlayers()) {
						if(playerKits.containsKey(p)) {
							if(playerKits.get(p).getName().equals("Fantôme")) {
								ParticleEffect ghost = new ParticleEffect(ParticleEffect.ParticleType.SPELL, 0, 0, 0);
								Location l = p.getLocation();
								l.add(0, 1, 0);
								ghost.sendToLocation(l);
							}
						}
					}
				} catch(NullPointerException npe) {}
			}
		}, 0, 30);
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(p.getWorld().getName().equalsIgnoreCase("event")) {
			if(playerKits.containsKey(p)) playerKits.remove(p);
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			p.setMaxHealth(20D);
			for(PotionEffect ef : p.getActivePotionEffects())
				p.removePotionEffect(ef.getType());
			p.teleport(Bukkit.getWorld("event").getSpawnLocation());
		} 
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent e) {
		if(playerKits.containsKey(e.getPlayer())) 
			playerKits.remove(e.getPlayer());
	}

	@EventHandler
	public void playerDeath(PlayerDeathEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(p.getWorld().getName().equals("event")) {
				if(playerKits.containsKey(p)) {
					playerDeath(p);		
					try {
						playerKill(p.getKiller());
					} catch(NullPointerException npe) {}
					finally {
						String deathMessage = e.getDeathMessage();
						e.setDeathMessage("");
						for(Player pl : Bukkit.getWorld("event").getPlayers())
							pl.sendMessage(deathMessage);
						Kit.resetPvPBoxPlayer(p);
					}
				}
			}
		}
	}

	@EventHandler
	public void dropItem(PlayerDropItemEvent e) {
		if(playerKits.containsKey(e.getPlayer()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerLossFood(FoodLevelChangeEvent e) {
		if ((e.getEntity() instanceof Player)) {
			Player p = (Player)e.getEntity();
			if (p.getWorld().getName().equalsIgnoreCase("event"))
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void changeWorld(PlayerChangedWorldEvent e) {
		final Player p = e.getPlayer();
		if(p.getWorld().getName().equals("event")) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			for(PotionEffect ef : p.getActivePotionEffects())
				p.removePotionEffect(ef.getType());
			p.setLevel(0);
			p.setExp(0F);
		}
		try{playerKits.remove(p);}
		catch(Exception ex){};
		p.setMaxHealth(20D);
		p.setScoreboard(Antykacraft.sbManager.getMainScoreboard());
	}

	@EventHandler
	public void selectKit(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		Player p = (Player) e.getWhoClicked();
		ItemStack i = e.getCurrentItem();
		if(p.getWorld().getName().equals("event")) {
			if(inv.getName().equals(PvPBoxGui.pvpBoxKitSelector(p).getName())) {
				if(!i.getType().equals(Material.THIN_GLASS)) {
					PlayerInventory pInv = p.getInventory();
					for(PotionEffect effect : p.getActivePotionEffects()) p.removePotionEffect(effect.getType());
					pInv.clear();
					pInv.setArmorContents(null);
					Kit kit = null;
					for(Kit k : Kit.kits) {
						try {
							if(k.getDisplayItem().equals(i)) {
								for(ItemStack it : k.getItems())
									pInv.addItem(it);
								pInv.setArmorContents(k.getArmor());
								for(PotionEffect ef : k.getEffects())
									p.addPotionEffect(ef);
								p.setMaxHealth(k.getMaxLife());
								p.setHealth(k.getMaxLife());
								p.setGameMode(GameMode.SURVIVAL);
								p.sendMessage(Antykacraft.prefix + "§eTu as choisi le kit \"" + k.getName() + "§e\"");
								kit = k;
								break;
							}
						} catch(NullPointerException ex) {}
					}
					playerKits.put(p, kit);
					teleportToPvPBoxArena(p);
				} e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void breakBlock(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(p.getWorld().getName().equals("event")) {
			if(!p.hasPermission("antykacraft.event.break"))
				e.setCancelled(true);
			if(playerKits.containsKey(p))
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void placeBlock(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(p.getWorld().getName().equals("event")) {
			if(playerKits.containsKey(p))
				e.setCancelled(true);
			if(!p.hasPermission("antykacraft.event.break"))
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void playerInteract(PlayerInteractEvent e) {
		try {
			Player p = e.getPlayer();
			if(p.getWorld().getName().equals("event")) {
				if(playerKits.containsKey(p))
					if(!allowClick.contains(e.getClickedBlock().getType()))
						e.setCancelled(true);
				if(p.getItemInHand().getType() == Material.POTION)
					e.setCancelled(false);
			}
		} catch(Exception ex) {}
	}

	@EventHandler
	public void playerInteractEntity(PlayerInteractEntityEvent e) {
		if(playerKits.containsKey(e.getPlayer())) {
			if(e.getPlayer().getWorld().getName().equals("event")) {
				e.setCancelled(true);
			}
		}
	}

	private void teleportToPvPBoxArena(Player p) {
		p.teleport(PvPBoxConfig.getPvPBoxArenaLocation(PvPBoxConfig.getDefaultPvPBoxArena()));
		p.closeInventory();
	}

	private void playerDeath(Player p) {
		try {
			if(p.getWorld().getName().equals("event")) {
				PvPBoxConfig.addDeath(p);
				try {playerKits.remove(p);}
				catch(Exception ex) {}
			}
		} catch(NullPointerException npe) {}
	}

	private void playerKill(Player p) {
		try {
			if(p.getWorld().getName().equals("event")) {
				p.setLevel(p.getLevel() + 1);
				PvPBoxConfig.addKill(p);
				p.getInventory().addItem(new ItemStack(Material.INK_SACK, 1, (byte) 4));
			}
		} catch(NullPointerException npe) {}
	}

	@EventHandler
	public void rightAbility(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(p.getWorld().getName().equals("event")) {
				if(playerKits.containsKey(p)) {
					try {
						ItemStack iHand = e.getItem();
						Kit k = playerKits.get(p);
						final RightAbility ability = (RightAbility)k.rightAbilities.get(iHand);
						if(!cooldown.contains(p)) {
							ability.run(p);
							cooldown.add(p);
							Bukkit.getScheduler().scheduleSyncDelayedTask(Antykacraft.instance, new Runnable() {
								public void run() {cooldown.remove(p);}
							}, k.rightAbilities.get(iHand).getCooldown());
						}
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}

	@EventHandler
	public void hitAbility(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			final Player p = (Player)e.getDamager();
			final Player d = (Player)e.getEntity();
			if(playerKits.containsKey(p) && playerKits.containsKey(d)) {
				try {
					ItemStack iHand = p.getItemInHand();
					Kit k = playerKits.get(p);
					final HitAbility ability = (HitAbility)k.hitAbilities.get(iHand);
					if(!cooldown.contains(p)) {
						ability.run(p, d, e.getDamage());
						cooldown.add(p);
						Bukkit.getScheduler().scheduleSyncDelayedTask(Antykacraft.instance, new Runnable() {
							public void run() {cooldown.remove(p);}
						}, k.hitAbilities.get(iHand).getCooldown());
					}
				} catch(Exception ex) {}
			}
		}
	}

	@EventHandler
	public void playerPickupArrow(PlayerPickupItemEvent e) {
		if(AntykacraftListener.isInEventWorld(e.getPlayer())) {
			Player p = e.getPlayer();
			if(playerKits.containsKey(p)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void fallingblock(EntityChangeBlockEvent e) {
		if(e.getEntity().getWorld().getName().equals("event")) {
			if(e.getEntity() instanceof FallingBlock) {
				FallingBlock b = (FallingBlock) e.getEntity();
				if(b.getMaterial().equals(Material.COBBLESTONE)) {
					e.setCancelled(true);
				} else if(b.getMaterial().equals(Material.TNT)) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void explosion(ExplosionPrimeEvent e) {
		if(e.getEntity().getWorld().getName().equals("event")) {
			if(e.getEntityType() == EntityType.PRIMED_TNT) {
				e.setCancelled(true);
				e.setRadius(0);
				e.setFire(false);
			}
		}
	}

	@EventHandler
	public void playerDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(p.getWorld().getName().equals("event")) {
				if(playerKits.containsKey(p)) {
					if(e.getCause() == DamageCause.BLOCK_EXPLOSION) {
						if(exploImmu.contains(p)) {
							e.setCancelled(true);
						} else {
							e.setDamage(4D);
						}
					}
				}
			}
		}
	}
}
