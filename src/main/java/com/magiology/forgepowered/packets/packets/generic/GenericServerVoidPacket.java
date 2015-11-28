package com.magiology.forgepowered.packets.packets.generic;

import java.io.*;

import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.forgepowered.packets.core.*;
import com.magiology.handlers.*;
import com.magiology.util.utilclasses.UtilM.U;

public class GenericServerVoidPacket extends AbstractToServerMessage{
	
	private int eventId;
	
	public GenericServerVoidPacket(){}
	public GenericServerVoidPacket(int eventId){
		this.eventId=eventId;
		GenericPacketEventHandler.addNewVoidPacketEvent(eventId, U.getMC().thePlayer,Side.CLIENT);
	}
	@Override
	public void write(PacketBuffer buffer) throws IOException{
		buffer.writeInt(eventId);
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		eventId=buffer.readInt();
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		GenericPacketEventHandler.addNewVoidPacketEvent(eventId, player,Side.SERVER);
		return null;
	}

}
