package fr.mrazerty31.antykacraft.pvpbox.spells;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.mrazerty31.antykacraft.Antykacraft;
import fr.mrazerty31.antykacraft.libs.ItemLib;
import fr.mrazerty31.antykacraft.listener.PvPBoxListener;
import fr.mrazerty31.antykacraft.pvpbox.kits.Kit;
import fr.mrazerty31.antykacraft.pvpbox.spells.MainAbility.RightAbility;

public class SpellUtil {
	public static HashMap<ItemStack, MainAbility> spells = new HashMap<ItemStack, MainAbility>();
	public static List<ItemStack> items = new ArrayList<ItemStack>();
	
	
	public static void addSpell(Player p, ItemStack i, MainAbility a) {
		Kit k = PvPBoxListener.playerKits.get(p);
		k.rightAbilities.put(i, a);
		p.getInventory().setItem(8, i);
	}
	
	/* Spells */
	
	// Flash
	
	public static RightAbility flash() {
		return new RightAbility(((int)40)) {
			public void run(final Player p) {
				//ParticleEffect.EXPLODE.display(p.getLocation().add(0, 1.75, 0), 0, 0, 0, 3, 5);
				p.teleport(p.getLineOfSight((Set<Material>) null, 5).get(4).getLocation());
				Bukkit.getScheduler().scheduleSyncDelayedTask(Antykacraft.instance, new Runnable() {
					public void run() {
						//ParticleEffect.EXPLODE.display(p.getLocation().add(0, 1.75, 0), 0, 0, 0, 3, 5);
					}
				}, 2L);
			}
		};
	}
	
	// Heal
	
	public static RightAbility heal() {
		return new RightAbility(((int)40)) {
			public void run(Player p) {
				if((((p.getHealth() + 6D) > 20D)) )
					p.setHealth(20D);
				else p.setHealth(p.getHealth() + 6D);
			}
		};
	}
	
	/* Items */
	
	// Flash
	
	public static ItemStack flashItem() {
		return ItemLib.createItem(Material.GOLD_NUGGET, 1, (short) 0, "§eSaut éclair", null);
	}
	
	// Heal 
	
	public static ItemStack healItem() {
		return ItemLib.createItem(Material.SEEDS, 1, (short) 0, "§aSoins", null);
	}
	
	/* Link */
	
	public static void linkSpells() {
		spells.put(flashItem(), flash());
		spells.put(healItem(), heal());
		items.add(flashItem());
		items.add(healItem());
	}
}
