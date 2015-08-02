package com.magiology.mcobjects.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class NetworkPointer extends Item{
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y,int z, int md, float xHit, float yHit, float zHit){
		if(!stack.hasTagCompound())stack.setTagCompound(new NBTTagCompound());
		if(player.isSneaking()){
			NBTTagCompound NBT=stack.getTagCompound();
			NBT.setInteger("xLink", x);
			NBT.setInteger("yLink", y);
			NBT.setInteger("zLink", z);
			return true;
		}
		return false;
	}
	
}
