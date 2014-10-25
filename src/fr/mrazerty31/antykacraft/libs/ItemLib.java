package fr.mrazerty31.antykacraft.libs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItemLib extends JavaPlugin
{
	/* Item Manipulation */

	public static ItemStack createItem(Material material, int amount, short data, String name, List<String> lore) {
		ItemStack item = new ItemStack(material, amount, data);
		ItemMeta MetaItem = item.getItemMeta();
		if(name != null) MetaItem.setDisplayName(name);
		if(lore != null) MetaItem.setLore(null); MetaItem.setLore(lore);
		item.setItemMeta(MetaItem);

		return item;
	}

	public static ItemStack addDisplayName(ItemStack item, String name) {
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		return item;
	}

	public static ItemStack addLore(ItemStack item, List<String> lore) {
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		return item;
	}

	public static ItemStack addEnchantments(ItemStack item, Enchantment[] enchants, int[] levels) {
		ItemMeta MetaItem = item.getItemMeta();
		for(int i = 0; i <= enchants.length - 1; i++) {
			item.addUnsafeEnchantment(enchants[i], levels[i]);
			MetaItem.addEnchant(enchants[i], levels[i], true);
		}	
		return item;
	}

	public static ItemStack colorLeatherArmor(ItemStack armor, Color color) {
		LeatherArmorMeta metaArmor = (LeatherArmorMeta) armor.getItemMeta();
		metaArmor.setColor(color);
		armor.setItemMeta(metaArmor);
		return armor;
	}
	
	public static ItemStack createPotion(int amount, int data) {
		return new ItemStack(Material.POTION, amount, (short) data);
	}
	
	public static ItemStack createPlayerHead(String playerName, String display) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta headMeta = (SkullMeta) head.getItemMeta();
		headMeta.setOwner(playerName);
		if(display != null) headMeta.setDisplayName(display);
		head.setItemMeta(headMeta);
		return head;
	}

	public static ItemStack addCustomPotionEffect(ItemStack i, PotionEffectType p, int d, int a) {
		PotionMeta pM = (PotionMeta) i.getItemMeta();
		PotionEffect pE = new PotionEffect(p, d, a);
		pM.addCustomEffect(pE, false);
		i.setItemMeta(pM);
		return i;
	}
	
	public static ItemStack setCustomPotionEffect(ItemStack i, PotionEffectType p, int d, int a) {
		PotionMeta pM = (PotionMeta) i.getItemMeta();
		PotionEffect pE = new PotionEffect(p, d, a);
		pM.addCustomEffect(pE, true);
		i.setItemMeta(pM);
		return i;
	}
	
	/* Formatting Utils */
	
	public static List<String> createLore(String[] lore) {
		List<String> itemLore = new ArrayList<String>();
		for(String lorePart : lore) itemLore.add(lorePart);
		return itemLore;
	}
}
