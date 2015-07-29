package com.magiology.gui.guiContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

import com.magiology.Annotations.GUINeedsWorldUpdates;
import com.magiology.Annotations.GUIWorldUpdater;

@GUINeedsWorldUpdates
public class GuiISidedPowerInstructorContainer extends Container{
	
	public TileEntity tile;
	public int side;
	EntityPlayer player;
	public GuiISidedPowerInstructorContainer(EntityPlayer player,TileEntity tile){
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
	@GUIWorldUpdater
	public void pickupTickingFromTileEntityEveryTick(){
		
		
		
	}
}
