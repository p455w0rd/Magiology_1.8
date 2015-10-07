package com.magiology.client.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.magiology.core.init.MItems;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkCommandHolder;
import com.magiology.util.utilclasses.Util;

public class CommandCenterContainer extends Container{
	
	public int selectedSlotId=-1;
	public TileEntityNetworkCommandHolder tile;
	
	public CommandCenterContainer(EntityPlayer player,TileEntityNetworkCommandHolder tile){
		this.tile=tile;
		for(int i=0;i<3;i++)for(int j=0;j<9;j++)addSlotToContainer(new Slot(player.inventory,9+j+9*i,8+j*18,84+18*i));
		for(int i=0;i<9;i++)addSlotToContainer(new Slot(player.inventory,i,8+i*18,142));
		for(int i=0;i<4;i++)for(int j=0;j<4;j++)addSlotToContainer(new CommandCenterContainerSlot(tile,j+4*i,j*18+53,18*i+7));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player){
		return true;
	}
	@Override
	public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer player){
		if(slotId<0)return super.slotClick(slotId, clickedButton, mode, player);
		Slot clickedSlot=(Slot)inventorySlots.get(slotId);
		switch (clickedButton){
		case 0:if(selectedSlotId==slotId-36)selectedSlotId=-1;break;
		case 1:if(Util.isItemInStack(MItems.commandContainer, clickedSlot.getStack())&&clickedSlot.inventory instanceof TileEntityNetworkCommandHolder){
			selectedSlotId=slotId-36;
			return clickedSlot.getStack();
		}break;
		}
		return super.slotClick(slotId, clickedButton, mode, player);
	}
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber){
		try{
			Slot clickedSlot=(Slot)inventorySlots.get(slotNumber);
			if(clickedSlot==null||!clickedSlot.getHasStack())return null;
			ItemStack itemstack=null;
			ItemStack itemstack1=clickedSlot.getStack();
			itemstack=itemstack1.copy();
			if(clickedSlot.inventory instanceof TileEntityNetworkCommandHolder){
				if(!mergeItemStack(itemstack1, 27, 36, false))if(!mergeItemStack(itemstack1, 0, 27, false))return null;
			}else if(Util.isItemInStack(MItems.commandContainer, itemstack1)){
				if(!mergeItemStack(itemstack1, 36, inventorySlots.size(), false))return null;
			}
			if(itemstack1.stackSize==0)clickedSlot.putStack((ItemStack)null);
			else                       clickedSlot.onSlotChanged();
			if(itemstack1.stackSize==itemstack.stackSize)return null;
			clickedSlot.onPickupFromSlot(player, itemstack1);
	        return itemstack;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
    }
	public static class CommandCenterContainerSlot extends Slot{
		public CommandCenterContainerSlot(IInventory inventoryIn, int index, int xPosition, int yPosition){
			super(inventoryIn, index, xPosition, yPosition);
		}
		@Override
		public boolean isItemValid(ItemStack stack){
			return Util.isItemInStack(MItems.commandContainer, stack);
		}
	}
}