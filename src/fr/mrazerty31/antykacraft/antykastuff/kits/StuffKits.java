package fr.mrazerty31.antykacraft.antykastuff.kits;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.mrazerty31.antykacraft.libs.ItemLib;

public class StuffKits {
	public static void antykaStuff(String ville, Player p) {
		Color leatherColor = Color.WHITE;
		Enchantment[] leatherEnchants = new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.DURABILITY};
		int[] enchantsLevels = new int[] {2, 127};

		ItemStack ironSword = new ItemStack(Material.IRON_SWORD),
				arrow = new ItemStack(Material.ARROW, 64),
				bow = new ItemStack(Material.BOW),
				leatherHelmet = new ItemStack(Material.LEATHER_HELMET),
				ironChestPlate = new ItemStack(Material.IRON_CHESTPLATE),
				ironLeggings = new ItemStack(Material.IRON_LEGGINGS),
				ironBoots = new ItemStack(Material.IRON_BOOTS);
		if (ville.equalsIgnoreCase("tikal")) leatherColor = Color.GREEN;
		else if (ville.equalsIgnoreCase("thebes"))  leatherColor = Color.BLUE;
		else if(ville.equalsIgnoreCase("athenes")) leatherColor = Color.YELLOW;
		else leatherColor = Color.WHITE;
		ItemLib.colorLeatherArmor(leatherHelmet, leatherColor);
		ItemLib.addEnchantments(leatherHelmet, leatherEnchants, enchantsLevels);

		PlayerInventory pInv = p.getInventory();

		pInv.clear();
		pInv.addItem(ironSword);
		pInv.addItem(bow);
		pInv.addItem(arrow);
		pInv.addItem(leatherHelmet);
		pInv.addItem(leatherHelmet);
		pInv.setArmorContents(new ItemStack[] {ironBoots, ironLeggings, ironChestPlate, leatherHelmet});

		p.setHealth(p.getMaxHealth());
		p.setFoodLevel(20);
	}

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

	public static void bwKit(Player p) {
		PlayerInventory pInv = p.getInventory();
		Enchantment[] enchants = new Enchantment[] {Enchantment.DURABILITY};
		int[] levels = new int[] {127};
		pInv.clear();

		pInv.addItem(ItemLib.addEnchantments(new ItemStack(Material.STONE_AXE), enchants, levels));
		pInv.addItem(ItemLib.addEnchantments(new ItemStack(Material.BOW), new Enchantment[] {Enchantment.ARROW_INFINITE}, new int[] {1}));
		pInv.addItem(new ItemStack(Material.FLINT_AND_STEEL));
		pInv.addItem(new ItemStack(Material.TNT, 6));
		pInv.addItem(new ItemStack(Material.LOG, 16));
		pInv.setArmorContents(new ItemStack[] {ItemLib.addEnchantments(new ItemStack(Material.GOLD_BOOTS), enchants, levels), 
				ItemLib.addEnchantments(new ItemStack(Material.GOLD_LEGGINGS), enchants, levels), 
				ItemLib.addEnchantments(new ItemStack(Material.GOLD_CHESTPLATE), enchants, levels), 
				ItemLib.addEnchantments(new ItemStack(Material.GOLD_HELMET), enchants, levels)});
	}
}
