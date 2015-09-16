package com.magiology.forgepowered.packets.packets.generic;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.handelers.GenericPacketEventHandeler;
import com.magiology.util.utilclasses.Util.U;

public class GenericServerIntPacket extends AbstractToServerMessage{
	
	private int data,eventId;
	
	
	public GenericServerIntPacket(){}
	public GenericServerIntPacket(int eventId,int data){
		this.data=data;
		this.eventId=eventId;
		GenericPacketEventHandeler.addNewIntegerPacketEvent(eventId, data, U.getMC().thePlayer,Side.CLIENT);
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
		GenericPacketEventHandeler.addNewIntegerPacketEvent(eventId, data, player,Side.SERVER);
		return null;
	}

}
