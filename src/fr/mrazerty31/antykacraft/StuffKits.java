package fr.mrazerty31.antykacraft;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.mrazerty31.antykacraft.libs.ItemLib;

public class StuffKits {
	public static void vipArmor(String armorType, Player p) {
		String helmetName = armorType.toUpperCase() + "_HELMET";
		String chestplateName = armorType.toUpperCase() + "_CHESTPLATE";
		String leggingsName = armorType.toUpperCase() + "_LEGGINGS";
		String bootsName = armorType.toUpperCase() + "_BOOTS";
		p.getInventory().addItem(ItemLib.addEnchantments(ItemLib.createItem(Material.valueOf(helmetName), 1, (short) 0, ChatColor.GOLD + "Casque du VIP", null), new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.DURABILITY}, new int[] {10, 10}));
		p.getInventory().addItem(ItemLib.addEnchantments(ItemLib.createItem(Material.valueOf(chestplateName), 1, (short) 0, ChatColor.GOLD + "Plastron du VIP", null), new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.DURABILITY}, new int[] {10, 10}));
		p.getInventory().addItem(ItemLib.addEnchantments(ItemLib.createItem(Material.valueOf(leggingsName), 1, (short) 0, ChatColor.GOLD + "Pantalon du VIP", null), new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.DURABILITY}, new int[] {10, 10}));
		p.getInventory().addItem(ItemLib.addEnchantments(ItemLib.createItem(Material.valueOf(bootsName), 1, (short) 0, ChatColor.GOLD + "Bottes du VIP", null), new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.DURABILITY}, new int[] {10, 10}));
		p.sendMessage(ChatColor.GREEN + "Votre armure vip de type \"" + armorType + "\" a bien été reçue !");
	}

	public static void pvpStuff(Player p) {
		PlayerInventory pInv = p.getInventory();
		Enchantment[] enchants = new Enchantment[] {Enchantment.DURABILITY};
		int[] levels = new int[] {3};

		pInv.clear();
		pInv.addItem(new ItemStack(Material.DIAMOND_SWORD));
		pInv.addItem(new ItemStack(Material.BOW));
		pInv.addItem(new ItemStack(Material.ARROW, 64));
		pInv.setArmorContents(new ItemStack[] {
				ItemLib.addEnchantments(new ItemStack(Material.DIAMOND_BOOTS), enchants, levels), 
				ItemLib.addEnchantments(new ItemStack(Material.DIAMOND_LEGGINGS), enchants, levels), 
				ItemLib.addEnchantments(new ItemStack(Material.DIAMOND_CHESTPLATE), enchants, levels), 
				ItemLib.addEnchantments(new ItemStack(Material.DIAMOND_HELMET), enchants, levels)});
		p.setHealth(p.getMaxHealth());
		p.setFoodLevel(20);
		p.sendMessage(ChatColor.GREEN + "Tu as bien reçu le stuff PvP");
	}
}
