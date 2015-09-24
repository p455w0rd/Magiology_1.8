package com.magiology.forgepowered.packets.packets;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.gui.container.CommandCenterContainer;

public class ClickCommandContainerInGui extends AbstractToServerMessage{
	
	private int slotId;
	
	public ClickCommandContainerInGui(){}
	public ClickCommandContainerInGui(int slotId){
		this.slotId=slotId;
	}
	
	
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		buffer.writeInt(slotId);
	}

	@Override
	public void read(PacketBuffer buffer)throws IOException{
		slotId=buffer.readInt();
	}

	@Override
	public IMessage process(EntityPlayer player, Side side){
		if(player.openContainer instanceof CommandCenterContainer){
			CommandCenterContainer container=((CommandCenterContainer)player.openContainer);
			ItemStack stack=((Slot)container.inventorySlots.get(slotId)).getStack();
			stack.getItem().onItemRightClick(stack, player.worldObj, player);
		}
		return null;
	}

}
