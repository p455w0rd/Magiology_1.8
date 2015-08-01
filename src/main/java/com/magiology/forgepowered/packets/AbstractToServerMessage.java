package com.magiology.forgepowered.packets;


public abstract class AbstractToServerMessage extends AbstractPacket{
	
	public static void registerNewMessage(Class<? extends AbstractToServerMessage> serverMessage){
		registerPacket(serverMessage, Side.SERVER);
	}
}
