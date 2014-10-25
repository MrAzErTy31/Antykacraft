package fr.mrazerty31.antykacraft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;

public class Recipes {
	
		public static void init() {
		/* Recipes Variables */

		ItemStack ChiseledSB = new ItemStack(Material.SMOOTH_BRICK, 1, (byte)3);
		@SuppressWarnings("deprecation")
		MaterialData SlabSB = new MaterialData(Material.STEP, (byte)5);
		ShapelessRecipe mossyCobble = new ShapelessRecipe(new ItemStack(Material.MOSSY_COBBLESTONE)),
				mossySB = new ShapelessRecipe(new ItemStack(Material.SMOOTH_BRICK, 1, (byte)1)),
				crackedSB = new ShapelessRecipe(new ItemStack(Material.SMOOTH_BRICK, 1, (byte)2));
		ShapedRecipe chiseledStoneBrick = new ShapedRecipe(ChiseledSB);

		/* Mossy Cobblestone Recipe */

		mossyCobble.addIngredient(1, Material.VINE);
		mossyCobble.addIngredient(1, Material.COBBLESTONE);

		/* Mossy Stone Brick Recipe */

		mossySB.addIngredient(1, Material.VINE);
		mossySB.addIngredient(1, Material.SMOOTH_BRICK);

		/* Cracked Stone Brick Recipe */

		crackedSB.addIngredient(1, Material.COBBLESTONE);
		crackedSB.addIngredient(1, Material.SMOOTH_BRICK);

		/* Chiseled Stone Brick Recipe */

		chiseledStoneBrick.shape(new String[] {"S","S"});
		chiseledStoneBrick.setIngredient('S', SlabSB);
		
		/* Saving to Server */

		Bukkit.addRecipe(chiseledStoneBrick);
		Bukkit.addRecipe(mossyCobble);
		Bukkit.addRecipe(mossySB);
		Bukkit.addRecipe(crackedSB);
	}
}
