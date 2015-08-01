package com.magiology.mcobjects.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
public class BedrockDust extends ItemFlintAndSteel{
	@Override
	public boolean onItemUse (ItemStack is, EntityPlayer player, World w, BlockPos pos, int l, float f, float f1, float f2){
		boolean return1=false;
		if(!w.isRemote){
			
		}
		return return1;
	}
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean is){
		list.add("Harder than bedrock but softer than wool.");
		list.add("Click on ground or on existing pile to use.");
		list.add("When stacked(to 4) you can burn it to get an ingot!");
		list.add("BURN BABY! BURN! ;)");
	}
}
