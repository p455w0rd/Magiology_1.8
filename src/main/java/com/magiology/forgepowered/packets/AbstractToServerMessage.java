package com.magiology.forgepowered.packets;

import cpw.mods.fml.relauncher.Side;

public abstract class AbstractToServerMessage extends AbstractPacket{
	
	public static void registerNewMessage(Class<? extends AbstractToServerMessage> serverMessage){
		registerPacket(serverMessage, Side.SERVER);
	}
}
