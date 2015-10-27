package com.magiology.core.init;

import com.magiology.forgepowered.packets.core.AbstractPacket;
import com.magiology.forgepowered.packets.packets.*;
import com.magiology.forgepowered.packets.packets.generic.GenericServerIntPacket;
import com.magiology.forgepowered.packets.packets.generic.GenericServerStringPacket;
import com.magiology.forgepowered.packets.packets.generic.GenericServerVoidPacket;
import com.magiology.io.WorldData.SyncClientsWorldData;
import com.magiology.io.WorldData.SyncServerWorldData;

public class MPackets{

	public static void preInit(){
		AbstractPacket.registerNewMessage(RightClickBlockPacket.class);
		AbstractPacket.registerNewMessage(TileRedstone.class);
		AbstractPacket.registerNewMessage(OpenGuiPacket.class);
		AbstractPacket.registerNewMessage(GenericServerIntPacket.class);
		AbstractPacket.registerNewMessage(GenericServerVoidPacket.class);
		AbstractPacket.registerNewMessage(GenericServerStringPacket.class);
		AbstractPacket.registerNewMessage(UploadPlayerDataPacket.class);
		AbstractPacket.registerNewMessage(ClickHologramPacket.class);
		AbstractPacket.registerNewMessage(RenderObjectUploadPacket.class);
		AbstractPacket.registerNewMessage(NotifyPointedBoxChangePacket.class);
		AbstractPacket.registerNewMessage(OpenProgramContainerInGui.class);
		AbstractPacket.registerNewMessage(OpenProgramContainerInGui.ExitGui.class);
		AbstractPacket.registerNewMessage(HologramProjectorUpload.class);
		AbstractPacket.registerNewMessage(SendPlayerDataPacket.class);
		AbstractPacket.registerNewMessage(SavableDataWithKeyPacket.class);
		AbstractPacket.registerNewMessage(SyncServerWorldData.class);
		AbstractPacket.registerNewMessage(SyncClientsWorldData.class);
	}

}
