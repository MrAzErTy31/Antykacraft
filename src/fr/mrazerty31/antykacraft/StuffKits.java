package fr.mrazerty31.antykacraft;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Color;
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
		Enchantment[] enchants = new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.DURABILITY};
		int[] levels = new int[] {1, 255};

		pInv.clear();
		pInv.addItem(ItemLib.addEnchantments(new ItemStack(Material.IRON_SWORD), new Enchantment[] {Enchantment.DAMAGE_ALL,  Enchantment.DURABILITY}, new int[] {0, 255}));
		ItemStack helmet = ItemLib.addEnchantments(new ItemStack(Material.IRON_HELMET), enchants, levels),
				chestplate = ItemLib.addEnchantments(new ItemStack(Material.IRON_CHESTPLATE), enchants, levels),
				leggings = ItemLib.addEnchantments(new ItemStack(Material.IRON_LEGGINGS), enchants, levels),
				boots = ItemLib.addEnchantments(new ItemStack(Material.IRON_BOOTS), enchants, levels);
		pInv.setArmorContents(new ItemStack[] {boots, leggings, chestplate, helmet});
		p.setHealth(p.getMaxHealth());
		p.setFoodLevel(20);
		p.sendMessage(ChatColor.GREEN + "Tu as bien reçu le stuff PvP");
	}
	
	public static void bowSpleef(Player p) {
		PlayerInventory pInv = p.getInventory();
		pInv.addItem(ItemLib.addEnchantments(ItemLib.addDisplayName(new ItemStack(Material.BOW), "§4§lBoom Bow"),
				new Enchantment[] {Enchantment.ARROW_FIRE, Enchantment.ARROW_INFINITE}, new int[] {1, 1}));
		pInv.addItem(new ItemStack(Material.ARROW));
		ItemStack[] armor = ItemLib.getFullColoredArmor(Color.RED);
		for(ItemStack it : armor)
			ItemLib.addLore(it, Arrays.asList(new String[] {ChatColor.GRAY + "Unbreakable"}));
		pInv.setArmorContents(armor);
	}
}
