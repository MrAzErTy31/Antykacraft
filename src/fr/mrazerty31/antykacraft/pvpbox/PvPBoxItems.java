package fr.mrazerty31.antykacraft.pvpbox;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.mrazerty31.antykacraft.libs.ItemLib;

public class PvPBoxItems {

	public static ItemStack warriorKit, archerKit, reaperKit, wizardKit, trollerKit, ninjaKit, 
	tankKit, rabbitKit, ghostKit, assassinKit, golemKit, bombermanKit, pirateKit;

	public static void init(){
		warriorKit = ItemLib.createItem(Material.IRON_SWORD, 1, (byte) 0, "§7Guerrier", null);
		archerKit = ItemLib.createItem(Material.BOW, 1, (byte) 0, "§bArcher", null);
		reaperKit = ItemLib.createItem(Material.IRON_HOE, 1, (byte) 0, "§8Reaper", null);
		wizardKit = ItemLib.createItem(Material.BLAZE_ROD, 1, (byte) 0, "§5Sorcier", null);
		trollerKit = ItemLib.createItem(Material.SKULL_ITEM, 1, (byte) 3, "§6Trolleur", null);
		ninjaKit = ItemLib.createItem(Material.STONE_SWORD, 1, (short) 0, "§rNinja", null);
		tankKit = ItemLib.createItem(Material.DIAMOND_CHESTPLATE, 1, (short) 0, "§1Tank", null);
		rabbitKit = ItemLib.createItem(Material.CARROT_ITEM, 1, (short) 0, "§aRabbit", null);
		ghostKit = ItemLib.createItem(Material.BONE, 1, (short) 0, "§7Fantôme", null);
		assassinKit = ItemLib.createItem(Material.NETHER_STALK, 1, (short) 0, "§4Assassin", null);
		golemKit = ItemLib.createItem(Material.COBBLESTONE, 1, (short) 0, "§7Golem", null);
		bombermanKit = ItemLib.createItem(Material.TNT, 1, (short) 0,  "§cBom" + "§rber" + "§cman", null);
		pirateKit = ItemLib.createItem(Material.SKULL_ITEM, 1, (short) 0, "§ePirate", null); 
	}
}
