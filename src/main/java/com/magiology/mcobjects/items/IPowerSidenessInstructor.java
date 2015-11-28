package com.magiology.mcobjects.items;

import java.util.*;

import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.api.power.*;
import com.magiology.core.init.*;
import com.magiology.handlers.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.m_extension.*;

public class IPowerSidenessInstructor extends ItemM{
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player){
		NBTUtil.createNBT(itemStack);
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,List list, boolean par4){
		list.add(EnumChatFormatting.BLUE+"Right click on a block to configure the power interaction on a side.");
	}
	
	@Override
	public boolean isFull3D(){return true;}
	
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		NBTUtil.createNBT(itemstack);
		boolean isIt=false;
		if(world.getTileEntity(pos) instanceof ISidedPower){
			isIt=true;
			GuiHandlerM.openGui(player, MGui.GuiISidedPowerInstructor, pos);
		}
		
		
		return isIt;
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5){
		NBTUtil.createNBT(itemstack);
	}

	
}
