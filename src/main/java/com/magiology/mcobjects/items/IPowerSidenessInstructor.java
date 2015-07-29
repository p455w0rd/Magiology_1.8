package com.magiology.mcobjects.items;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.magiology.api.power.ISidedPower;
import com.magiology.core.init.MGui;
import com.magiology.objhelper.helpers.Helper;

public class IPowerSidenessInstructor extends Item{
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player){
	    itemStack.stackTagCompound = new NBTTagCompound();
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,List list, boolean par4){
		list.add(EnumChatFormatting.BLUE+"Right click on a block to configure the power interaction on a side.");
	}
	
	@Override
	public boolean isFull3D(){return true;}
	
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float x2, float y2, float z2){
		if(itemstack.stackTagCompound == null)itemstack.stackTagCompound = new NBTTagCompound();
		boolean isIt=false;
		if(world.getTileEntity(x, y, z) instanceof ISidedPower){
			isIt=true;
			Helper.openGui(player, MGui.GuiISidedPowerInstructor, x, y, z);
		}
		
		
		return isIt;
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5){
		if(itemstack.stackTagCompound==null)itemstack.stackTagCompound=new NBTTagCompound();
	}

	
}
