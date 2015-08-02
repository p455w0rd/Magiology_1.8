package com.magiology.mcobjects.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class NetworkPointer extends Item{
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		if(!stack.hasTagCompound())stack.setTagCompound(new NBTTagCompound());
		if(player.isSneaking()){
			NBTTagCompound NBT=stack.getTagCompound();
			NBT.setInteger("xLink", pos.getX());
			NBT.setInteger("yLink", pos.getY());
			NBT.setInteger("zLink", pos.getZ());
			return true;
		}
		return false;
	}
	
}
