package com.magiology.gui.guiContainer;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class GuiControlBockContainerSlot extends Slot{
	public GuiControlBockContainerSlot(IInventory inv, int id, int x, int y){
		super(inv, id, x, y);
	}
	@Override
	public boolean isItemValid(ItemStack stack){
		if(TileEntityFurnace.getItemBurnTime(stack)>0)return true;
		return false;
	}
}
