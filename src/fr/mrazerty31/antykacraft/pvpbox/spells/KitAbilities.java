package fr.mrazerty31.antykacraft.pvpbox.spells;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.mrazerty31.antykacraft.Antykacraft;
import fr.mrazerty31.antykacraft.listener.PvPBoxListener;
import fr.mrazerty31.antykacraft.pvpbox.spells.MainAbility.RightAbility;
import fr.mrazerty31.antykacraft.utils.Utils;

public class KitAbilities {

	/* Ninja */

	public static RightAbility shuriken() {
		return new RightAbility(6) {
			public void run(final Player p) {
				final Item i = p.getWorld().dropItem(p.getEyeLocation(), new ItemStack(Material.NETHER_STAR));
				i.setPickupDelay(9999);
				i.setVelocity(p.getLocation().getDirection().multiply(2.0D));
				new BukkitRunnable() {
					public void run() {
						for (Player pl : Bukkit.getWorld("event").getPlayers()) {
							if (!p.equals(pl)) {
								if (!i.isDead()) {
									if (i.getLocation().distanceSquared(pl.getLocation()) <= 1.0D) {
										pl.getWorld().playSound(i.getLocation(), Sound.SILVERFISH_HIT, 1.0F, 1.2F);
										pl.damage(8D);
										i.remove();
										cancel();
									}
								} else cancel();
							}
						}
					}
				}.runTaskTimer(Antykacraft.instance, 0L, 1L);
				Bukkit.getScheduler().scheduleSyncDelayedTask(Antykacraft.instance, new BukkitRunnable() {
					public void run() {
						i.remove();
					}
				}, 60L);
			}
		};
	}

	/* Archer */

	public static RightAbility salve() {
		return new RightAbility(10) {
			public void run(Player p) {
				float ang = 70F;
				for(int i = 1; i <= 6; i++) {
					ang += 5;
					Arrow a = (Arrow) p.launchProjectile(Arrow.class);
					a.setShooter(p);
					a.setFireTicks(20);
					a.setBounce(true);
					a.setVelocity(Utils.setAngle(p, ang, 2D));
				}
			}
		};
	}

	/* Golem */

	public static RightAbility golem() {
		return new RightAbility(6) {
			@SuppressWarnings("deprecation")
			public void run(final Player p) {
				final FallingBlock f = p.getWorld().spawnFallingBlock(p.getEyeLocation(), Material.COBBLESTONE, (byte) 0);
				f.setVelocity(p.getLocation().getDirection().multiply(1.75D));
				BukkitRunnable bR = new BukkitRunnable() {
					public void run() {
						for(Player pl : Bukkit.getWorld("event").getPlayers()) {
							if(!p.equals(pl)) {
								if(!f.isDead()) {
									if(f.getLocation().distanceSquared(p.getLocation()) <= 1.75) {
										pl.damage(5D);
										pl.getWorld().playSound(f.getLocation(), Sound.DIG_STONE, 1.0F, 1.35F);
										pl.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, 30));
										f.remove();
										cancel();
									}
								} else cancel();
							}
						}
					}
				};
				bR.runTaskTimer(Antykacraft.instance, 0L, 1L);
				Bukkit.getScheduler().scheduleSyncDelayedTask(Antykacraft.instance, new BukkitRunnable() {
					public void run() {
						f.remove();
					}
				}, 40L);
			}
		};
	}

	/* Pyromane */

	public static RightAbility crache() {
		return new RightAbility(15) {
			public void run(Player p) {

			}
		};
	}

	/* Bomberman */

	public static RightAbility bomb() {
		return new RightAbility(2) {
			@SuppressWarnings("deprecation")
			public void run(final Player p) {
				final FallingBlock tnt = p.getWorld().spawnFallingBlock(p.getEyeLocation(), Material.TNT, (byte) 0);
				tnt.setVelocity(p.getLocation().getDirection().multiply(1.35D));
				tnt.setDropItem(false);
				PvPBoxListener.exploImmu.add(p);
				Bukkit.getScheduler().scheduleSyncDelayedTask(Antykacraft.instance, new BukkitRunnable() {
					public void run() {
						Location l = tnt.getLocation();
						tnt.remove();
						p.getWorld().createExplosion(l, 3.5F, false);
						Bukkit.getScheduler().scheduleSyncDelayedTask(Antykacraft.instance, new Runnable() {
							public void run() {
								try{PvPBoxListener.exploImmu.remove(p);}
								catch(Exception e) {}
							}
						}, 10L);
					}
				}, 20L);
			}
		};
	}

	/* Pirate */

	public static RightAbility revolver() {
		return new RightAbility(5) {
			public void run(final Player p) {
				final Item it = p.getWorld().dropItem(p.getEyeLocation(), new ItemStack(Material.COAL_BLOCK));
				p.getWorld().playSound(p.getEyeLocation(), Sound.EXPLODE, 1.0F, 1.0F);
				it.setVelocity(p.getLocation().getDirection().multiply(1.85D));
				BukkitRunnable bR = new BukkitRunnable() {
					public void run() {
						for(Player pl : Bukkit.getWorld("event").getPlayers()) {
							if(!p.equals(pl)) {
								if(!it.isDead()) {
									if(it.getLocation().distanceSquared(p.getLocation()) <= 1.65) {
										pl.damage(5D);
										pl.getWorld().playSound(it.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
										it.remove();
										cancel();
									}
								} else cancel();
							}
						}
					}
				};
				bR.runTaskTimer(Antykacraft.instance, 0L, 1L);
				Bukkit.getScheduler().scheduleSyncDelayedTask(Antykacraft.instance, new BukkitRunnable() {
					public void run() {
						it.remove();
					}
				}, 50L);
			}
		};
	}
}
