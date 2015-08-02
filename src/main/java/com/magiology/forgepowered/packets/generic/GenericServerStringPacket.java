package com.magiology.forgepowered.packets.generic;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import com.magiology.forgepowered.packets.AbstractToServerMessage;
import com.magiology.handelers.GenericPacketEventHandeler;

public class GenericServerStringPacket extends AbstractToServerMessage{
	
	private int eventId;
	private String data;
	
	public GenericServerStringPacket(){}
	public GenericServerStringPacket(int eventId,String data){
		this.data=data;
		this.eventId=eventId;
		GenericPacketEventHandeler.addNewStringPacketEvent(eventId, data, Minecraft.getMinecraft().thePlayer,Side.CLIENT);
	}
	@Override
	public void write(PacketBuffer buffer) throws IOException{
		buffer.writeInt(data.length());
		for(int a=0;a<data.length();a++)buffer.writeChar(data.toCharArray()[a]);
		buffer.writeInt(eventId);
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		int lenght=buffer.readInt();
		String string="";
		for(int a=0;a<lenght;a++)string+=""+buffer.readChar();
		data=string;
		eventId=buffer.readInt();
	}
	@Override
	public void process(EntityPlayer player, Side side){
		GenericPacketEventHandeler.addNewStringPacketEvent(eventId, data, player,Side.SERVER);
	}

}
