package com.magiology.core.init;

import com.magiology.forgepowered.packets.*;
import com.magiology.forgepowered.packets.generic.GenericServerIntPacket;
import com.magiology.forgepowered.packets.generic.GenericServerStringPacket;
import com.magiology.forgepowered.packets.generic.GenericServerVoidPacket;

public class MPackets{

	public static void preInit(){
		AbstractToServerMessage.registerNewMessage(RightClickBlockPacket.class);
		AbstractToServerMessage.registerNewMessage(TileRedstone.class);
		AbstractToServerMessage.registerNewMessage(OpenGuiPacket.class);
		AbstractToServerMessage.registerNewMessage(GenericServerIntPacket.class);
		AbstractToServerMessage.registerNewMessage(GenericServerVoidPacket.class);
		AbstractToServerMessage.registerNewMessage(GenericServerStringPacket.class);
		AbstractToServerMessage.registerNewMessage(UploadPlayerDataPacket.class);
		AbstractToServerMessage.registerNewMessage(ClickHologramPacket.class);
		AbstractToServerMessage.registerNewMessage(RenderObjectUploadPacket.class);
		AbstractToServerMessage.registerNewMessage(NotifyPointedBoxChangePacket.class);
		
		AbstractToClientMessage.registerNewMessage(SendPlayerDataPacket.class);
		AbstractToClientMessage.registerNewMessage(SavableDataWithKeyPacket.class);
	}

}
