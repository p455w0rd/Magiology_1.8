package com.magiology.mcobjects.items;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.magiology.api.power.ISidedPower;
import com.magiology.core.init.MGui;
import com.magiology.mcobjects.ItemM;
import com.magiology.util.utilclasses.Helper;
import com.magiology.util.utilclasses.Helper.H;

public class IPowerSidenessInstructor extends ItemM{
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player){
		H.createNBT(itemStack);
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,List list, boolean par4){
		list.add(EnumChatFormatting.BLUE+"Right click on a block to configure the power interaction on a side.");
	}
	
	@Override
	public boolean isFull3D(){return true;}
	
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		H.createNBT(itemstack);
		boolean isIt=false;
		if(world.getTileEntity(pos) instanceof ISidedPower){
			isIt=true;
			Helper.openGui(player, MGui.GuiISidedPowerInstructor, pos);
		}
		
		
		return isIt;
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5){
		H.createNBT(itemstack);
	}

	
}
