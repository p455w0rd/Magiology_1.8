package com.magiology.mcobjects.items;

import java.util.*;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.util.utilobjects.m_extension.*;
public class BedrockDust extends ItemM{
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		return false;
	}
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean is){
		list.add("Harder than bedrock but softer than wool.");
		list.add("Click on ground or on existing pile to use.");
		list.add("When stacked(to 4) you can burn it to get an ingot!");
		list.add("BURN BABY! BURN! ;)");
	}
}
