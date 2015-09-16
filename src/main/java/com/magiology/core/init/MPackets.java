package com.magiology.core.init;

import com.magiology.forgepowered.packets.core.AbstractToClientMessage;
import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.forgepowered.packets.packets.ClickHologramPacket;
import com.magiology.forgepowered.packets.packets.NotifyPointedBoxChangePacket;
import com.magiology.forgepowered.packets.packets.OpenGuiPacket;
import com.magiology.forgepowered.packets.packets.RenderObjectUploadPacket;
import com.magiology.forgepowered.packets.packets.RightClickBlockPacket;
import com.magiology.forgepowered.packets.packets.SavableDataWithKeyPacket;
import com.magiology.forgepowered.packets.packets.SendPlayerDataPacket;
import com.magiology.forgepowered.packets.packets.TileRedstone;
import com.magiology.forgepowered.packets.packets.UploadPlayerDataPacket;
import com.magiology.forgepowered.packets.packets.generic.GenericServerIntPacket;
import com.magiology.forgepowered.packets.packets.generic.GenericServerStringPacket;
import com.magiology.forgepowered.packets.packets.generic.GenericServerVoidPacket;

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
