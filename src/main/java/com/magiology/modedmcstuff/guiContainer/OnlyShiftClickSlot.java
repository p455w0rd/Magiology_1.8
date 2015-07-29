package com.magiology.modedmcstuff.guiContainer;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class OnlyShiftClickSlot extends Slot{
	public OnlyShiftClickSlot(IInventory iInv, int id,int x, int y){
		super(iInv, id,x,y);
	}
}
