package fr.mrazerty31.antykacraft.pvpbox;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.mrazerty31.antykacraft.Antykacraft;
import fr.mrazerty31.antykacraft.pvpbox.MainAbility.RightAbility;
import fr.mrazerty31.antykacraft.utils.Utils;

public class KitAbilities {

	public static RightAbility shuriken() {
		return new RightAbility(10) {
			public void run(final Player p) {
				final Item i = p.getWorld().dropItem(p.getEyeLocation(), new ItemStack(Material.NETHER_STAR));
				i.setPickupDelay(9999);
				i.setVelocity(p.getLocation().getDirection().multiply(2D));
				try {
					Bukkit.getScheduler().scheduleSyncRepeatingTask(Antykacraft.instance, new BukkitRunnable() {
						public void run() {
							for(Player pl : Bukkit.getWorld("event").getPlayers()) {
								if(!p.equals(pl)) {
									if(!i.isDead()) {
										if(i.getLocation().distanceSquared(pl.getLocation()) <= 0.75) {
											pl.damage(5D);
											i.remove();
											cancel();
										}
									} else cancel();
								}
							}
						}
					}, 0L, 1L);
				} catch(Exception ex) {}
				Bukkit.getScheduler().scheduleSyncDelayedTask(Antykacraft.instance, new BukkitRunnable() {
					public void run() {
						i.remove();
					}
				}, 50L);
			}
		};
	}

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
}
