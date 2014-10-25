package fr.mrazerty31.antykacraft.pvpbox;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.mrazerty31.antykacraft.Antykacraft;
import fr.mrazerty31.antykacraft.pvpbox.MainAbility.HitAbility;
import fr.mrazerty31.antykacraft.pvpbox.MainAbility.RightAbility;
import fr.mrazerty31.antykacraft.utils.Utils;

public class KitAbilities {

	/* RIGHT CLICK */

	public static RightAbility shuriken() {
		return new RightAbility(1) {
			public void run(Player p) {
				final Item shuriken = p.getWorld().dropItem(p.getEyeLocation(), new ItemStack(Material.NETHER_STAR));
				shuriken.setVelocity(p.getLocation().getDirection().multiply(1.25D));
				Bukkit.getScheduler().scheduleSyncDelayedTask(Antykacraft.instance, new Runnable() {
					public void run() {shuriken.remove();}
				}, 30L);
				while(!shuriken.isDead()) {
					for(Entity en : p.getWorld().getEntities()) {
						if(en.getType() == EntityType.PLAYER) {
							Player plr = (Player) en;
							List<Player> hit = new ArrayList<Player>();
							if(plr != p && !hit.contains(plr)) {
								if(shuriken.getLocation().distanceSquared(en.getLocation()) <= 0.75) {
									plr.setHealth(plr.getHealth() - 3D);
									hit.add(plr);
								}
							}
						}
					}
				}
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

	/* HIT */

	public static HitAbility vampireSteal() {
		return new HitAbility() {
			public void run(Player p, Player d, double damage) {
				try {
					if(damage >= 1) {
						p.setHealth(p.getHealth() + (((int)damage)/2.5));
					} else p.setHealth(p.getHealth() + 0.5);
				} catch(Exception ex) {
					p.setHealth(20D);
				}
			}
		};
	}

	/* LEFT CLICK */

}
