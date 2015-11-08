package com.magiology.forgepowered.packets.packets;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import com.magiology.api.lang.ProgramHolder;
import com.magiology.client.gui.container.CommandCenterContainer;
import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.mcobjects.items.ProgramContainer;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkCommandHolder;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

public class OpenProgramContainerInGui extends AbstractToServerMessage{
	
	private int slotId;
	
	public OpenProgramContainerInGui(){}
	public OpenProgramContainerInGui(int slotId){
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
		try{
			if(player.openContainer instanceof CommandCenterContainer){
				CommandCenterContainer container=((CommandCenterContainer)player.openContainer);
				ItemStack stack=((Slot)container.inventorySlots.get(slotId)).getStack();
				stack.getItem().onItemRightClick(stack, player.worldObj, player);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static class ExitGui extends AbstractToServerMessage{
		
		private int slotId;
		private String data,name;
		private BlockPosM tilePos;
		
		public ExitGui(){}
		public ExitGui(int slotId, String data, String name, BlockPos tilePos){
			this.slotId=slotId;
			this.tilePos=tilePos!=null?new BlockPosM(tilePos):null;
			this.data=data;
			this.name=name;
		}
		
		
		@Override
		public void write(PacketBuffer buffer)throws IOException{
			buffer.writeInt(slotId);
			writeString(buffer, data);
			writeString(buffer, name);
			buffer.writeBoolean(tilePos!=null);
			if(tilePos!=null)buffer.writeBlockPos(tilePos);
		}

		@Override
		public void read(PacketBuffer buffer)throws IOException{
			slotId=buffer.readInt();
			data=readString(buffer);
			name=readString(buffer);
			if(buffer.readBoolean())tilePos=new BlockPosM(buffer.readBlockPos());
		}

		@Override
		public IMessage process(EntityPlayer player, Side side){
			if(tilePos!=null){
				TileEntity test=tilePos.getTile(player.worldObj);
				if(test instanceof TileEntityNetworkCommandHolder){
					TileEntityNetworkCommandHolder tile=(TileEntityNetworkCommandHolder)test;
					ItemStack stack=tile.getStackInSlot(slotId);
					if(stack!=null&&stack.hasTagCompound()){
						ProgramHolder.code_register(ProgramContainer.getId(stack), data);
						ProgramContainer.setName(stack, name);
					}
				}
			}else{
				ItemStack stack=player.inventory.mainInventory[slotId];
				if(stack!=null&&stack.hasTagCompound()){
					ProgramHolder.code_register(ProgramContainer.getId(stack), data);
					ProgramContainer.setName(stack, name);
				}
			}
			return null;
		}

	}
}
