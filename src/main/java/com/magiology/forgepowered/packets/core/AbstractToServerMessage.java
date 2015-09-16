package com.magiology.forgepowered.packets.core;

import net.minecraftforge.fml.relauncher.Side;


public abstract class AbstractToServerMessage extends AbstractPacket{
	
	public static void registerNewMessage(Class<? extends AbstractToServerMessage> serverMessage){
		registerPacket(serverMessage, Side.SERVER);
	}
}
