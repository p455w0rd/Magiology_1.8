package com.magiology.core.init;

import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.registry.*;

public class MRecepies{

	public static void init(){
		GameRegistry.addSmelting(Items.diamond_axe,new ItemStack(Items.diamond, 3), 10);
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.farmland, 8), "AAA", "ABA", "AAA", 'A', Blocks.dirt, 'B', Items.wooden_hoe);
		GameRegistry.addShapedRecipe(new ItemStack(Items.repeater),"   ", "ABA", "CCC", 'A', Blocks.redstone_torch, 'B', Items.redstone , 'C', Blocks.stone_slab);
		
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.CoalLevel2, 1), "AA", "AA", "  ", 'A', Blocks.coal_ore); GameRegistry.addSmelting(MBlocks.CoalLevel2, new ItemStack(Items.coal, 8), 5F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.CoalLevel3, 1), "AA", "AA", "  ", 'A', MBlocks.CoalLevel2); GameRegistry.addSmelting(MBlocks.CoalLevel3, new ItemStack(Items.coal, 48), 40F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.CoalLevel4, 1), "AA", "AA", "  ", 'A', MBlocks.CoalLevel3); GameRegistry.addSmelting(MBlocks.CoalLevel4, new ItemStack(Items.coal, 256), 200F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.CoalLevel5, 1), "AA", "AA", "  ", 'A', MBlocks.CoalLevel4); GameRegistry.addSmelting(MBlocks.CoalLevel5, new ItemStack(Items.coal, 1536), 1000F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.SuperDuperCoal, 1), "AA", "AA", "  ", 'A', MBlocks.CoalLevel5);GameRegistry.addSmelting(MBlocks.SuperDuperCoal, new ItemStack(Items.coal, 12288), 5000F);
	
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.IronLevel2, 1), "AA", "AA", "  ", 'A', Blocks.iron_ore); GameRegistry.addSmelting(MBlocks.IronLevel2, new ItemStack(Items.iron_ingot, 6), 5F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.IronLevel3, 1), "AA", "AA", "  ", 'A', MBlocks.IronLevel2); GameRegistry.addSmelting(MBlocks.IronLevel3, new ItemStack(Items.iron_ingot, 32), 40F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.IronLevel4, 1), "AA", "AA", "  ", 'A', MBlocks.IronLevel3); GameRegistry.addSmelting(MBlocks.IronLevel4, new ItemStack(Items.iron_ingot, 160), 200F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.IronLevel5, 1), "AA", "AA", "  ", 'A', MBlocks.IronLevel4); GameRegistry.addSmelting(MBlocks.IronLevel5, new ItemStack(Items.iron_ingot, 896), 1000F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.SuperDuperIron, 1), "AA", "AA", "  ", 'A', MBlocks.IronLevel5); GameRegistry.addSmelting(MBlocks.SuperDuperIron, new ItemStack(Items.iron_ingot, 5121 ), 5000F);
	
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.GoldLevel2, 1), "AA", "AA", "  ", 'A', Blocks.gold_ore); GameRegistry.addSmelting(MBlocks.GoldLevel2, new ItemStack(Items.gold_ingot, 6), 5F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.GoldLevel3, 1), "AA", "AA", "  ", 'A', MBlocks.GoldLevel2); GameRegistry.addSmelting(MBlocks.GoldLevel3, new ItemStack(Items.gold_ingot, 32), 40F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.GoldLevel4, 1), "AA", "AA", "  ", 'A', MBlocks.GoldLevel3); GameRegistry.addSmelting(MBlocks.GoldLevel4, new ItemStack(Items.gold_ingot, 160), 200F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.GoldLevel5, 1), "AA", "AA", "  ", 'A', MBlocks.GoldLevel4); GameRegistry.addSmelting(MBlocks.GoldLevel5, new ItemStack(Items.gold_ingot, 896), 1000F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.SuperDuperGold, 1), "AA", "AA", "  ", 'A', MBlocks.GoldLevel5); GameRegistry.addSmelting(MBlocks.SuperDuperGold, new ItemStack(Items.gold_ingot, 5121), 5000F);
	
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.DiamondLevel2, 1), "AA", "AA", "  ", 'A', Blocks.diamond_ore); GameRegistry.addSmelting(MBlocks.DiamondLevel2, new ItemStack(Items.diamond, 4), 5F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.DiamondLevel3, 1), "AA", "AA", "  ", 'A', MBlocks.DiamondLevel2); GameRegistry.addSmelting(MBlocks.DiamondLevel3, new ItemStack(Items.diamond, 24), 40F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.DiamondLevel4, 1), "AA", "AA", "  ", 'A', MBlocks.DiamondLevel3); GameRegistry.addSmelting(MBlocks.DiamondLevel4, new ItemStack(Items.diamond, 128), 200F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.DiamondLevel5, 1), "AA", "AA", "  ", 'A', MBlocks.DiamondLevel4); GameRegistry.addSmelting(MBlocks.DiamondLevel5, new ItemStack(Items.diamond, 768), 1000F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.SuperDuperDiamond, 1), "AA", "AA", "  ", 'A', MBlocks.DiamondLevel5); GameRegistry.addSmelting(MBlocks.SuperDuperDiamond, new ItemStack(Items.diamond, 6144), 5000F);
	
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.LapisLazuliLevel2, 1), "AA", "AA", "  ", 'A', Blocks.lapis_ore); GameRegistry.addSmelting(MBlocks.LapisLazuliLevel2, new ItemStack(Items.dye, 24, 4), 5F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.LapisLazuliLevel3, 1), "AA", "AA", "  ", 'A', MBlocks.LapisLazuliLevel2); GameRegistry.addSmelting(MBlocks.LapisLazuliLevel3, new ItemStack(Items.dye, 144, 4), 40F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.LapisLazuliLevel4, 1), "AA", "AA", "  ", 'A', MBlocks.LapisLazuliLevel3); GameRegistry.addSmelting(MBlocks.LapisLazuliLevel4, new ItemStack(Items.dye, 768, 4), 200F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.LapisLazuliLevel5, 1), "AA", "AA", "  ", 'A', MBlocks.LapisLazuliLevel4); GameRegistry.addSmelting(MBlocks.LapisLazuliLevel5, new ItemStack(Items.dye, 4608, 4), 1000F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.SuperDuperLapisLazuli, 1), "AA", "AA", "  ", 'A', MBlocks.LapisLazuliLevel5); GameRegistry.addSmelting(MBlocks.SuperDuperLapisLazuli, new ItemStack(Items.dye, 36864, 4), 5000F);
	
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.RedstoneLevel2, 1), "AA", "AA", "  ", 'A', Blocks.redstone_ore); GameRegistry.addSmelting(MBlocks.RedstoneLevel2, new ItemStack(Items.diamond, 4), 5F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.RedstoneLevel3, 1), "AA", "AA", "  ", 'A', MBlocks.RedstoneLevel2); GameRegistry.addSmelting(MBlocks.RedstoneLevel3, new ItemStack(Items.diamond, 4), 5F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.RedstoneLevel4, 1), "AA", "AA", "  ", 'A', MBlocks.RedstoneLevel3); GameRegistry.addSmelting(MBlocks.RedstoneLevel4, new ItemStack(Items.diamond, 4), 5F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.RedstoneLevel5, 1), "AA", "AA", "  ", 'A', MBlocks.RedstoneLevel4); GameRegistry.addSmelting(MBlocks.RedstoneLevel5, new ItemStack(Items.diamond, 4), 5F);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.SuperDuperRedstone, 1), "AA", "AA", "  ", 'A', MBlocks.RedstoneLevel5);GameRegistry.addSmelting(MBlocks.SuperDuperRedstone, new ItemStack(Items.diamond, 4), 5F);
	
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.EmeraldLevel2, 1), "AA", "AA", "  ", 'A', Blocks.emerald_ore);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.EmeraldLevel3, 1), "AA", "AA", "  ", 'A', MBlocks.EmeraldLevel2);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.EmeraldLevel4, 1), "AA", "AA", "  ", 'A', MBlocks.EmeraldLevel3);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.EmeraldLevel5, 1), "AA", "AA", "  ", 'A', MBlocks.EmeraldLevel4);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.SuperDuperEmerald, 1), "AA", "AA", "  ", 'A', MBlocks.EmeraldLevel5);
		
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.NetherQuartzLevel2, 1), "AA", "AA", "  ", 'A', Blocks.quartz_ore);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.NetherQuartzLevel3, 1), "AA", "AA", "  ", 'A', MBlocks.NetherQuartzLevel2);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.NetherQuartzLevel4, 1), "AA", "AA", "  ", 'A', MBlocks.NetherQuartzLevel3);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.NetherQuartzLevel5, 1), "AA", "AA", "  ", 'A', MBlocks.NetherQuartzLevel4);
		GameRegistry.addShapedRecipe(new ItemStack(MBlocks.SuperDuperNetherQuartz, 1), "AA", "AA", "  ", 'A', MBlocks.NetherQuartzLevel5);
	}
	
}
