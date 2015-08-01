package com.magiology.forgepowered.packets.generic;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import com.magiology.forgepowered.packets.AbstractToServerMessage;
import com.magiology.handelers.GenericPacketEventHandeler;

public class GenericServerVoidPacket extends AbstractToServerMessage{
	
	private int eventId;
	
	public GenericServerVoidPacket(){}
	public GenericServerVoidPacket(int eventId){
		this.eventId=eventId;
		GenericPacketEventHandeler.addNewVoidPacketEvent(eventId, Minecraft.getMinecraft().thePlayer,Side.CLIENT);
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
	public void process(EntityPlayer player, Side side){
		GenericPacketEventHandeler.addNewVoidPacketEvent(eventId, player,Side.SERVER);
	}

}
