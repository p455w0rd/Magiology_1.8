package com.magiology.forgepowered.packets.generic;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import com.magiology.forgepowered.packets.AbstractToServerMessage;
import com.magiology.handelers.GenericPacketEventHandeler;

public class GenericServerIntPacket extends AbstractToServerMessage{
	
	private int data,eventId;
	
	public GenericServerIntPacket(){}
	public GenericServerIntPacket(int eventId,int data){
		this.data=data;
		this.eventId=eventId;
		GenericPacketEventHandeler.addNewIntegerPacketEvent(eventId, data, Minecraft.getMinecraft().thePlayer,Side.CLIENT);
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
	public void process(EntityPlayer player, Side side){
		GenericPacketEventHandeler.addNewIntegerPacketEvent(eventId, data, player,Side.SERVER);
	}

}
