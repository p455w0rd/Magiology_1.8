package com.magiology.render.tilerender.hologram;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import com.magiology.Annotations.GUINeedsWorldUpdates;
import com.magiology.Annotations.GUIWorldUpdater;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;

@GUINeedsWorldUpdates
public class GuiObjectCustomizeContainer extends Container{
	
	EntityPlayer player;
	TileEntityHologramProjector hologramProjector;
	
	public GuiObjectCustomizeContainer(EntityPlayer player, TileEntityHologramProjector hologramProjector){
		this.player=player;
		this.hologramProjector=hologramProjector;
	}
	
	
	@Override
	public boolean canInteractWith(EntityPlayer player){return true;}
	
	@GUIWorldUpdater
	public void updateGui(){
		
	}
}
