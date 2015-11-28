package com.magiology.mcobjects.items;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.util.utilobjects.m_extension.*;

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
	public static BlockPosM getTarget(ItemStack stack){
		NBTTagCompound nbt=stack.getTagCompound();
		if(nbt==null)return BlockPosM.ORIGIN;
		return new BlockPosM(nbt.getInteger("xLink"),nbt.getInteger("yLink"),nbt.getInteger("zLink"));
	}
}
