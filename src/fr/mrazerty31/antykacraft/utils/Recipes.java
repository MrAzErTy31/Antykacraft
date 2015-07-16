package fr.mrazerty31.antykacraft.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Recipes {
	public static void init() {
		ShapedRecipe quartz = new ShapedRecipe(new ItemStack(Material.QUARTZ_BLOCK));
		quartz.shape(new String[] {"QN", "NQ"});
		quartz.setIngredient('Q', Material.QUARTZ);
		quartz.setIngredient('N', Material.NETHERRACK);
		
		Bukkit.addRecipe(quartz);
	}
}
