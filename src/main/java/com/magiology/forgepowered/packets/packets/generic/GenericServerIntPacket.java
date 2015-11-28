package com.magiology.forgepowered.packets.packets.generic;

import java.io.*;

import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.forgepowered.packets.core.*;
import com.magiology.handlers.*;
import com.magiology.util.utilclasses.UtilM.U;

public class GenericServerIntPacket extends AbstractToServerMessage{
	
	private int data,eventId;
	
	
	public GenericServerIntPacket(){}
	public GenericServerIntPacket(int eventId,int data){
		this.data=data;
		this.eventId=eventId;
		GenericPacketEventHandler.addNewIntegerPacketEvent(eventId, data, U.getMC().thePlayer,Side.CLIENT);
	}
	@Override
	public void write(PacketBuffer buffer) throws IOException{
		buffer.writeInt(data);
		buffer.writeInt(eventId);
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		data=buffer.readInt();
		eventId=buffer.readInt();
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		GenericPacketEventHandler.addNewIntegerPacketEvent(eventId, data, player,Side.SERVER);
		return null;
	}

}
