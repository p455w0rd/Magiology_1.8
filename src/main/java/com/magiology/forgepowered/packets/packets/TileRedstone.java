package com.magiology.forgepowered.packets.packets;
import java.io.*;

import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.forgepowered.packets.core.*;
import com.magiology.mcobjects.tileentityes.*;
import com.magiology.util.utilclasses.*;


public class TileRedstone extends AbstractToServerMessage{
	 private int id;
	 private BlockPos pos;
	 public TileRedstone(){}
	 public TileRedstone(TileEntityControlBlock tile){
		 id=tile.redstoneC;
		 pos=tile.getPos();
	 }
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		 buffer.writeInt(id);;
		 writePos(buffer, pos);
	}
	@Override
	public void read(PacketBuffer buffer)throws IOException{
		 id=buffer.readInt();
		 pos=readPos(buffer);
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		 World world=player.worldObj;
		 TileEntity tile=world.getTileEntity(pos);
		 if(tile!=null&&tile instanceof TileEntityControlBlock){
			 ((TileEntityControlBlock)tile).redstoneC=id;
			 return null;
		 }
		 PrintUtil.println("PACKET HAS FAILED TO DELIVER THE DATA!\n");
		return null;
	}
}