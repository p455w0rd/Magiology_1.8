package com.magiology.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkCommandCenter;

public class CommandCenterContainer extends Container{
	
	public CommandCenterContainer(EntityPlayer player,TileEntityNetworkCommandCenter tile){
		for(int i=0;i<4;i++){for(int j=0;j<4;j++){
			this.addSlotToContainer(new Slot(tile,j+4*i,j*18,18*i));
		}}
		try{
			for(int i=0;i<3;i++){for(int j=0;j<9;j++){
					this.addSlotToContainer(new Slot(player.inventory,9+j+9*i,8+j*18,84+18*i));
			}}
			for(int i=0;i<9;i++)this.addSlotToContainer(new Slot(player.inventory,i,8+i*18,142));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public boolean canInteractWith(EntityPlayer player){
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber){
        return null;
        
    }
}
