package com.magiology.client.gui.container;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.tileentity.*;

public class ISidedPowerInstructorContainer extends Container{
	
	public TileEntity tile;
	EntityPlayer player;
	public ISidedPowerInstructorContainer(EntityPlayer player,TileEntity tile){
		this.tile=tile;
		this.player=player;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player){
		return true;
	}
	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_){
		 super.onContainerClosed(p_75134_1_);
	}
}
