package com.magiology.forgepowered.packets.packets;
import java.io.*;

import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.tileentity.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.forgepowered.packets.core.*;
import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.m_extension.*;


public class HologramProjectorUpload extends AbstractToServerMessage{
	 private boolean[] highlights;
	 private BlockPosM pos;
	 public HologramProjectorUpload(){}
	 public HologramProjectorUpload(TileEntityHologramProjector tile){
		 pos=new BlockPosM(tile.getPos());
		 highlights=tile.highlighs;
	 }
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		 buffer.writeInt(highlights.length);
		 writePos(buffer, pos);
		 for(int i=0;i<highlights.length;i++)buffer.writeBoolean(highlights[i]);
	}
	@Override
	public void read(PacketBuffer buffer)throws IOException{
		 int j=buffer.readInt();
		 pos=new BlockPosM(readPos(buffer));
		 highlights=new boolean[j];
		 for(int i=0;i<j;i++)highlights[i]=buffer.readBoolean();
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		 TileEntity tile=pos.getTile(player.worldObj);
		 if(tile instanceof TileEntityHologramProjector){
			 ((TileEntityHologramProjector)tile).highlighs=highlights;
			 return null;
		 }
		 UtilM.println("PACKET HAS FAILED TO DELIVER THE DATA!");
		return null;
	}
}