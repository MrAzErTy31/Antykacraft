package fr.mrazerty31.antykacraft.pvpbox;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import fr.mrazerty31.antykacraft.libs.ItemLib;

public class PvPBoxItems {

	public static ItemStack warriorKit, archerKit, reaperKit, wizardKit, trollerKit, vampireKit, ninjaKit, tankKit, rabbitKit, ghostKit, rainbbowKit, sachaKit;
	public static Enchantment[] archerArmorEnchants;
	public static int[] archerArmorEnchantsLevels;
	public static String rainbbowName = ChatColor.DARK_PURPLE + "R" + ChatColor.BLUE + "a" + ChatColor.AQUA + "i" + ChatColor.GREEN + "n" + ChatColor.YELLOW + "b" + ChatColor.GOLD + "b" + ChatColor.RED + "o" + ChatColor.DARK_RED + "w" + ChatColor.RESET;

	public static void init(){
		warriorKit = ItemLib.createItem(Material.IRON_SWORD, 1, (byte) 0, ChatColor.GRAY + "Guerrier", null);
		archerKit = ItemLib.createItem(Material.BOW, 1, (byte) 0, ChatColor.AQUA + "Archer", null);
		archerArmorEnchants = new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.DURABILITY};
		archerArmorEnchantsLevels = new int[] {2, 5};
		reaperKit = ItemLib.createItem(Material.IRON_HOE, 1, (byte) 0, ChatColor.DARK_GRAY + "Reaper", null);
		wizardKit = ItemLib.createItem(Material.BLAZE_ROD, 1, (byte) 0, ChatColor.DARK_PURPLE + "Sorcier", null);
		trollerKit = ItemLib.createItem(Material.SKULL_ITEM, 1, (byte) 3, ChatColor.GOLD + "Trolleur", null);
		vampireKit = ItemLib.createItem(Material.GHAST_TEAR, 1, (short) 1, ChatColor.DARK_RED + "Vampire", null);
		ninjaKit = ItemLib.createItem(Material.STONE_SWORD, 1, (short) 0, "§rNinja", null);
		tankKit = ItemLib.createItem(Material.DIAMOND_CHESTPLATE, 1, (short) 0, ChatColor.DARK_RED + "Tank", null);
		rabbitKit = ItemLib.createItem(Material.CARROT_ITEM, 1, (short) 0, ChatColor.GREEN + "Rabbit", null);
		ghostKit = ItemLib.createItem(Material.BONE, 1, (short) 0, ChatColor.GRAY + "Fantôme", null);
		String rainbbowName = ChatColor.DARK_PURPLE + "R" + ChatColor.BLUE + "a" + ChatColor.AQUA + "i" + ChatColor.GREEN + "n" + ChatColor.YELLOW + "b" + ChatColor.GOLD + "b" + ChatColor.RED + "o" + ChatColor.DARK_RED + "w";
		rainbbowKit = ItemLib.createItem(Material.RED_ROSE, 1, (short) 0, rainbbowName, null);
		sachaKit = ItemLib.createItem(Material.MONSTER_EGG, 1, (short) 96, "Sacha", null);
	}
}
