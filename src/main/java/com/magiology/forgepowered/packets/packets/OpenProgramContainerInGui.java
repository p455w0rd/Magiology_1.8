package com.magiology.forgepowered.packets.packets;

import java.io.*;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.api.lang.program.*;
import com.magiology.client.gui.container.*;
import com.magiology.forgepowered.packets.core.*;
import com.magiology.mcobjects.items.*;
import com.magiology.mcobjects.tileentityes.network.*;
import com.magiology.util.utilobjects.m_extension.*;

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
				if(test instanceof TileEntityNetworkProgramHolder){
					TileEntityNetworkProgramHolder tile=(TileEntityNetworkProgramHolder)test;
					ItemStack stack=tile.getStackInSlot(slotId);
					if(stack!=null&&stack.hasTagCompound()){
						ProgramDataBase.code_set(ProgramContainer.getId(stack), name, data);
					}
				}
			}else{
				ItemStack stack=player.inventory.mainInventory[slotId];
				if(stack!=null&&stack.hasTagCompound()){
					ProgramDataBase.code_set(ProgramContainer.getId(stack), name, data);
				}
			}
			return null;
		}

	}
}
