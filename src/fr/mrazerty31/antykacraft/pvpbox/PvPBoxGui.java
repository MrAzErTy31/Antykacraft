package fr.mrazerty31.antykacraft.pvpbox;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.mrazerty31.antykacraft.libs.ItemLib;
import fr.mrazerty31.antykacraft.pvpbox.kits.Kit;
import fr.mrazerty31.antykacraft.pvpbox.spells.SpellUtil;

public class PvPBoxGui {
	public static ItemStack glasspane = ItemLib.createItem(Material.THIN_GLASS, 1, (short) 0, " ", null);
	public static Inventory pvpBoxKitSelector(Player p) {
		Inventory inv = Bukkit.createInventory(p, InventoryType.CHEST.getDefaultSize() * 2, "Sélection du kit PvPBox");
		for(Kit k : Kit.kits) {
			ItemStack it = k.getDisplayItem();
			it.getItemMeta().setLore(getLore(k));
			inv.addItem(it);
		}
		fillEmpty(inv);
		return inv;
	}

	public static Inventory spellInventory(Player p) {
		Inventory inv = Bukkit.createInventory(p, 9, "Sort Secondaire");
		for(ItemStack it : SpellUtil.items) {
			inv.addItem(it);
		} fillEmpty(inv);
		return inv;
	}

	public static void fillEmpty(Inventory inv) {
		for(int i = 0; i < inv.getSize(); i++) if(inv.getItem(i) == null) inv.setItem(i, glasspane);
	}

	public static List<String> getLore(Kit k) {
		List<String> lore = new ArrayList<String>();
		lore.add("§6---------------");
		lore.add("§cAttaque:   " + getRekt(k.getDescAttack()) + " ");
		lore.add("§aVie:         " + getRekt(k.getDescHealth()) + " ");
		lore.add("§bMagie:      " + getRekt(k.getDescMagic()) + " ");
		lore.add("§5Difficulté: " + getRekt(k.getDescAttack()) + " ");
		lore.add("§6---------------");
		return lore;
	}

	static String getRekt(byte b) {
		String s = "";
		for(int i = 1; i <= b; i++) {
			s += "█";
		} return s;
	}
}
