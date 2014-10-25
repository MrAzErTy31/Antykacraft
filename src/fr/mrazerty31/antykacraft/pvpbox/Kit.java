package fr.mrazerty31.antykacraft.pvpbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.mrazerty31.antykacraft.libs.ItemLib;
import fr.mrazerty31.antykacraft.utils.Utils;

public class Kit {
	public static List<Kit> kits = new ArrayList<Kit>();
	private PotionEffect[] effects;
	private ItemStack[] armor, items;
	private ItemStack displayItem;
	private String name;
	private double maxLife;
	public HashMap<ItemStack, MainAbility> rightAbilities = new HashMap<ItemStack, MainAbility>();
	public HashMap<ItemStack, MainAbility> leftAbilities = new HashMap<ItemStack, MainAbility>();
	public HashMap<ItemStack, MainAbility> hitAbilities = new HashMap<ItemStack, MainAbility>();

	public Kit(String name, ItemStack[] items, ItemStack[] armor, PotionEffect[] effects, ItemStack displayItem) {
		this.setName(name);
		this.setItems(items);
		this.setArmor(armor);
		this.setEffects(effects);
		this.setMaxLife(20D);
		this.setDisplayItem(displayItem);
	}

	public Kit(String name, ItemStack[] items, ItemStack[] armor, PotionEffect[] effects, double maxLife, ItemStack displayItem) {
		this.setName(name);
		this.setItems(items);
		this.setArmor(armor);
		this.setEffects(effects);
		this.setMaxLife(maxLife);
		this.setDisplayItem(displayItem);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemStack[] getItems() {
		return items;
	}

	public void setItems(ItemStack[] items) {
		this.items = items;
	}

	public ItemStack[] getArmor() {
		return armor;
	}

	public void setArmor(ItemStack[] armor) {
		this.armor = armor;
	}

	public PotionEffect[] getEffects() {
		return effects;
	}

	public void setEffects(PotionEffect[] effects) {
		this.effects = effects;
	}

	public ItemStack getDisplayItem() {
		return displayItem;
	}

	public void setDisplayItem(ItemStack displayItem) {
		this.displayItem = displayItem;
	}

	public double getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(double maxLife) {
		this.maxLife = maxLife;
	}

	public static void init() {
		ItemStack[] items, armor;
		ItemStack head;
		PotionEffect[] effects;

		/* Guerrier */

		items = new ItemStack[] {new ItemStack(Material.IRON_SWORD)};
		head = ItemLib.addEnchantments(ItemLib.createItem(Material.SKULL_ITEM, 1, (byte) 2, "Warrior Head", null), 
				new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL}, new int[] {1});
		armor = new ItemStack[] {new ItemStack(Material.GOLD_BOOTS), new ItemStack(Material.IRON_LEGGINGS),
				new ItemStack(Material.IRON_CHESTPLATE), head};
		Kit warrior = new Kit("Guerrier", items, armor, null, PvPBoxItems.warriorKit);
		kits.add(warrior);

		/* Archer */

		items = new ItemStack[] {new ItemStack(Material.WOOD_SWORD), 
				ItemLib.addEnchantments(new ItemStack(Material.BOW), new Enchantment[] {Enchantment.ARROW_INFINITE, Enchantment.DURABILITY}, new int[] {1, 127}),
				ItemLib.addDisplayName(new ItemStack(Material.ARROW), "§aSalve"),};
		head = ItemLib.addEnchantments(ItemLib.createItem(Material.SKULL_ITEM, 1, (byte) 0, "Archer Head", null),
				new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL}, new int[] {1});
		armor = new ItemStack[] {ItemLib.addEnchantments(new ItemStack(Material.GOLD_BOOTS), new Enchantment[] {Enchantment.DURABILITY}, new int[] {127}),
				ItemLib.addEnchantments(new ItemStack(Material.GOLD_LEGGINGS), new Enchantment[] {Enchantment.DURABILITY}, new int[] {127}),
				ItemLib.addEnchantments(new ItemStack(Material.GOLD_CHESTPLATE), new Enchantment[] {Enchantment.DURABILITY}, new int[] {127}),
				head};
		Kit archer = new Kit("Archer", items, armor, null, PvPBoxItems.archerKit);
		archer.rightAbilities.put(items[2], KitAbilities.salve());
		kits.add(archer);

		/* Reaper */

		items = new ItemStack[] {ItemLib.addEnchantments(ItemLib.createItem(Material.IRON_HOE, 1, (short) 0, "§8Faux", null), new Enchantment[] {Enchantment.DAMAGE_ALL}, new int[] {4}),
				ItemLib.createPotion(1, 16425), ItemLib.createPotion(1, 16420)};
		head = ItemLib.createItem(Material.SKULL_ITEM, 1, (byte) 1, "Reaper Head", null);
		armor = new ItemStack[] {ItemLib.addEnchantments(ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), Color.BLACK), new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL}, new int[] {1}),
				ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.BLACK), new ItemStack(Material.DIAMOND_CHESTPLATE), head};
		Kit reaper = new Kit("Reaper", items, armor, null, PvPBoxItems.reaperKit);
		kits.add(reaper);

		/* Wizard */

		items = new ItemStack[] {new ItemStack(Material.IRON_SPADE), ItemLib.createPotion(1, 16393), ItemLib.createPotion(1, 16386),
				ItemLib.createPotion(1, 16453), ItemLib.createPotion(1, 16417)};
		head = ItemLib.addEnchantments(ItemLib.createPlayerHead("MHF_Villager", "Wizard Head"), new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL}, new int[] {1});
		armor = new ItemStack[] {ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), Color.YELLOW),
				ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.YELLOW), 
				ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.YELLOW), head};
		Kit wizard = new Kit("Wizard", items, armor, null, PvPBoxItems.wizardKit);
		kits.add(wizard);

		/* Ombre */

		ItemStack trollerStick = ItemLib.addEnchantments(ItemLib.createItem(Material.STICK, 1, (short) 0, "§dTroller Stick", null), 
				new Enchantment[] {Enchantment.KNOCKBACK, Enchantment.DAMAGE_ALL}, new int[] {3, 2});
		items = new ItemStack[] {new ItemStack(Material.SNOW_BALL, 64), new ItemStack(Material.SNOW_BALL, 64), trollerStick, 
				ItemLib.createPotion(1, 16456), ItemLib.createPotion(1, 16458)};
		head = ItemLib.addEnchantments(ItemLib.createItem(Material.SKULL_ITEM, 1, (short) 3, "Troller Head", null), 
				new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL}, new int[] {1});
		armor = new ItemStack[] {ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), Color.BLACK),
				ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.BLACK), 
				ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.BLACK), head};
		Kit troller = new Kit("Trolleur", items, armor, null, PvPBoxItems.trollerKit);
		kits.add(troller);

		/* Vampire */

		items = new ItemStack[] {ItemLib.addEnchantments(ItemLib.createItem(Material.GHAST_TEAR, 1, (short) 0, "§4Croc", 
		ItemLib.createLore(new String[] {ChatColor.GRAY+"Life Steal I", ChatColor.GRAY+"Sharpness II"})), 
		new Enchantment[] {Enchantment.DAMAGE_ALL}, new int[] {2})};
		armor = new ItemStack[] {ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), Color.BLACK),
				ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.BLACK), 
				ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.BLACK),
				new ItemStack(Material.SKULL, 1, (byte)1)};
		effects = new PotionEffect[] {};
		Kit vampire = new Kit("Vampire", items, armor, effects, PvPBoxItems.vampireKit);
		vampire.hitAbilities.put(items[0], KitAbilities.vampireSteal());
		kits.add(vampire);

		/* Ninja */

		items = new ItemStack[] {ItemLib.addEnchantments(ItemLib.createItem(Material.STONE_SWORD, 1, (short) 0, "Katana", null), 
				new Enchantment[] {Enchantment.DAMAGE_ALL, Enchantment.KNOCKBACK}, new int[] {1, 1}),
				ItemLib.addDisplayName(new ItemStack(Material.NETHER_STAR, 3), "Shuriken")};
		armor = new ItemStack[] {ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), Color.BLACK),
				ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.BLACK), 
				ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.BLACK),
				ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_HELMET), Color.BLACK)};
		effects = new PotionEffect[] {new PotionEffect(PotionEffectType.SPEED, (int) Utils.calculateTicks(3600), 0)};
		Kit ninja = new Kit("Ninja", items, armor, effects, PvPBoxItems.ninjaKit);
		ninja.rightAbilities.put(items[1], KitAbilities.shuriken());
		kits.add(ninja);

		/* Tank */

		items = new ItemStack[] {ItemLib.addEnchantments(ItemLib.createItem(Material.STONE_AXE, 1, (short) 0, ChatColor.GOLD + "Hache de bourrin", null),
				new Enchantment[] {Enchantment.DURABILITY}, new int[] {127})};
		armor = new ItemStack[] {ItemLib.addEnchantments(new ItemStack(Material.DIAMOND_BOOTS), new Enchantment[] {Enchantment.DURABILITY}, new int[] {1}), 
				ItemLib.addEnchantments(new ItemStack(Material.DIAMOND_LEGGINGS), new Enchantment[] {Enchantment.DURABILITY}, new int[] {1}), 
				ItemLib.addEnchantments(new ItemStack(Material.DIAMOND_CHESTPLATE), new Enchantment[] {Enchantment.DURABILITY}, new int[] {1}), 
				ItemLib.addEnchantments(ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_HELMET), Color.BLUE), 
						new Enchantment[] {Enchantment.DURABILITY}, new int[] {1})};
		effects = new PotionEffect[] {new PotionEffect(PotionEffectType.SLOW, (int) Utils.calculateTicks(3600), 1),
				new PotionEffect(PotionEffectType.WEAKNESS, (int) Utils.calculateTicks(3600), 0)};
		Kit tank = new Kit("Tank", items, armor, effects, 32D, PvPBoxItems.tankKit);
		kits.add(tank);

		/* Rabbit */

		items = new ItemStack[] {ItemLib.addEnchantments(ItemLib.createItem(Material.CARROT_ITEM, 1, (short) 0, "§cFire §6Carrot", null), 
				new Enchantment[] {Enchantment.DAMAGE_ALL, Enchantment.FIRE_ASPECT}, new int[] {2, 1})};
		armor = new ItemStack[] {ItemLib.addEnchantments(ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), Color.WHITE), 
				new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL}, new int[] {1}),
						ItemLib.addEnchantments(ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.WHITE), 
								new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL}, new int[] {1}), 
								ItemLib.addEnchantments(ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.WHITE), 
										new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL}, new int[] {1}),
										ItemLib.addEnchantments(ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_HELMET), Color.WHITE), 
												new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL}, new int[] {1})};
		effects = new PotionEffect[] {new PotionEffect(PotionEffectType.JUMP, (int) Utils.calculateTicks(3600), 2),
				new PotionEffect(PotionEffectType.SPEED, (int) Utils.calculateTicks(3600), 1)};
		Kit rabbit = new Kit("Rabbit", items, armor, effects, PvPBoxItems.rabbitKit);
		kits.add(rabbit);

		/* Ghost */

		items = new ItemStack[] {ItemLib.addEnchantments(ItemLib.createItem(Material.BONE, 1, (short) 0, ChatColor.DARK_GRAY + "Os du spectre", null), 
				new Enchantment[] {Enchantment.DAMAGE_ALL}, new int[] {1}), ItemLib.createPotion(3, 16460)};
		armor = new ItemStack[] {};
		effects = new PotionEffect[] {new PotionEffect(PotionEffectType.INVISIBILITY, (int) Utils.calculateTicks(3600), 0),
				new PotionEffect(PotionEffectType.WEAKNESS, (int) Utils.calculateTicks(3600), 0)};
		Kit ghost = new Kit("Fantôme", items, armor, effects, PvPBoxItems.ghostKit);
		kits.add(ghost);

		/* Rainbbow */

		items = new ItemStack[] {ItemLib.addEnchantments(ItemLib.createItem(Material.RED_ROSE, 1, (short) 4, "Tulipe Rouge", null), new Enchantment[] {Enchantment.FIRE_ASPECT}, new int[] {1}),
				ItemLib.addEnchantments(ItemLib.createItem(Material.RED_ROSE, 1, (short) 6, "Tulipe Blanche", null), new Enchantment[] {Enchantment.KNOCKBACK}, new int[] {1}),
				ItemLib.addEnchantments(ItemLib.createItem(Material.RED_ROSE, 1, (short) 5, "Tulipe Rouge", null), new Enchantment[] {Enchantment.DAMAGE_ALL}, new int[] {3}),
				ItemLib.addEnchantments(ItemLib.createItem(Material.RED_ROSE, 1, (short) 7, "Tulipe Rose", null), 
						new Enchantment[] {Enchantment.DAMAGE_UNDEAD, Enchantment.DAMAGE_ARTHROPODS, Enchantment.DURABILITY}, new int[] {5, 5, 127})};
		head = ItemLib.createPlayerHead("Rainbbow", PvPBoxItems.rainbbowName + " Head");
		armor = new ItemStack[] {ItemLib.colorLeatherArmor(ItemLib.createItem(Material.LEATHER_BOOTS, 1, (short) 0, PvPBoxItems.rainbbowName + " Boots", null), Color.GREEN),
				ItemLib.colorLeatherArmor(ItemLib.createItem(Material.LEATHER_LEGGINGS, 1, (short) 0, PvPBoxItems.rainbbowName + " Leggings", null), Color.BLUE), 
				ItemLib.colorLeatherArmor(ItemLib.createItem(Material.LEATHER_CHESTPLATE, 1, (short) 0, PvPBoxItems.rainbbowName + " Chestplate", null), Color.YELLOW), head};
		effects = new PotionEffect[] {new PotionEffect(PotionEffectType.INVISIBILITY, (int) Utils.calculateTicks(3600), 0),
				new PotionEffect(PotionEffectType.WEAKNESS, (int) Utils.calculateTicks(3600), 0)};
		Kit rainbbow = new Kit(PvPBoxItems.rainbbowName, items, armor, null, PvPBoxItems.rainbbowKit);
		kits.add(rainbbow);

		/* Sacha */

		items = new ItemStack[] {ItemLib.createItem(Material.MONSTER_EGG, 1, (short) 50, ChatColor.RED + "Voltorbe" + ChatColor.WHITE + ", go !", null),
				ItemLib.createItem(Material.MONSTER_EGG, 1, (short) 54, ChatColor.LIGHT_PURPLE + "Grodoudou" + ChatColor.WHITE + ", go !", null),
				ItemLib.createItem(Material.MONSTER_EGG, 1, (short) 52, ChatColor.GREEN + "Mimigal" + ChatColor.WHITE + ", go !", null),
				ItemLib.addEnchantments(ItemLib.createItem(Material.LEASH, 1, (short) 0, "Coup d'fouet ! *schlak*", null), 
						new Enchantment[] {Enchantment.DAMAGE_ALL}, new int[] {1})};
		armor = new ItemStack[] {ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), Color.RED),
				ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.RED), 
				ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.RED),
				ItemLib.colorLeatherArmor(new ItemStack(Material.LEATHER_HELMET), Color.RED)};
		Kit sacha = new Kit("Sacha", items, armor, null, PvPBoxItems.sachaKit);
		kits.add(sacha);
	}
	
	/* A faire :
	 * Ombre
	 * Loup
	 * Invisibilité dans les fougères
	 */
}
