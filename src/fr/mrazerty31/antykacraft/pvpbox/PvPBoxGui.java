package fr.mrazerty31.antykacraft.pvpbox;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.mrazerty31.antykacraft.libs.ItemLib;

public class PvPBoxGui {
	public static ItemStack glasspane = ItemLib.createItem(Material.THIN_GLASS, 1, (short) 0, " ", null);
	public static Inventory pvpBoxKitSelector(Player p) {
		Inventory inv = Bukkit.createInventory(p, InventoryType.CHEST.getDefaultSize() * 2, "Sélection du kit PvPBox");
		inv.setItem(11, PvPBoxItems.warriorKit);
		inv.setItem(12, PvPBoxItems.archerKit);
		inv.setItem(13, PvPBoxItems.vampireKit);
		inv.setItem(14, PvPBoxItems.wizardKit);
		inv.setItem(15, PvPBoxItems.reaperKit);
		inv.setItem(21, PvPBoxItems.sachaKit);
		inv.setItem(23, PvPBoxItems.trollerKit);
		inv.setItem(29, PvPBoxItems.ninjaKit);
		inv.setItem(30, PvPBoxItems.ghostKit);
		inv.setItem(31, PvPBoxItems.rainbbowKit);
		inv.setItem(32, PvPBoxItems.rabbitKit);
		inv.setItem(33, PvPBoxItems.tankKit);
		for(int i = 0; i < inv.getSize(); i++) if(inv.getItem(i) == null) inv.setItem(i, glasspane);
		return inv;
	}
}
