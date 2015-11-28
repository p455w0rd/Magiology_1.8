package com.magiology.client.gui.guiutil.container;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;

public class ControlBockContainerSlot extends Slot{
	public ControlBockContainerSlot(IInventory inv, int id, int x, int y){
		super(inv, id, x, y);
	}
	@Override
	public boolean isItemValid(ItemStack stack){
		if(TileEntityFurnace.getItemBurnTime(stack)>0)return true;
		return false;
	}
}
