package com.magiology.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;

public class GuiObjectCustomizeContainer extends Container{
	
	EntityPlayer player;
	TileEntityHologramProjector hologramProjector;
	
	public GuiObjectCustomizeContainer(EntityPlayer player, TileEntityHologramProjector hologramProjector){
		this.player=player;
		this.hologramProjector=hologramProjector;
	}
	
	
	@Override
	public boolean canInteractWith(EntityPlayer player){return true;}
	
}
