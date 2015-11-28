package com.magiology.client.gui.container;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;

import com.magiology.mcobjects.tileentityes.hologram.*;

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
