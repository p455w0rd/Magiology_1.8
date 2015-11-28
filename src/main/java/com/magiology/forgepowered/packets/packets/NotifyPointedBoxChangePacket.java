package com.magiology.forgepowered.packets.packets;

import java.io.*;

import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.forgepowered.packets.core.*;
import com.magiology.mcobjects.tileentityes.corecomponents.*;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider.MultiColisionProviderRayTracer;

public class NotifyPointedBoxChangePacket extends AbstractToServerMessage{
	BlockPos pos;
	private int id;
	public NotifyPointedBoxChangePacket(){}
	public<T extends TileEntity&MultiColisionProvider> NotifyPointedBoxChangePacket(T tile){
		pos=tile.getPos();
		id=MultiColisionProviderRayTracer.getPointedId(tile);
	}
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		writePos(buffer, pos);
		buffer.writeInt(id);
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		pos=readPos(buffer);
		id=buffer.readInt();
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		TileEntity tile=player.worldObj.getTileEntity(pos);
		if(tile instanceof MultiColisionProvider){
			((MultiColisionProvider)tile).setPointedBox(((MultiColisionProvider)tile).getBoxes()[id]);
		}
		return null;
	}

}
