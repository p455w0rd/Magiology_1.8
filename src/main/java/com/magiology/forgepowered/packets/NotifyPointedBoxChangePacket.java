package com.magiology.forgepowered.packets;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider.MultiColisionProviderRayTracer;

public class NotifyPointedBoxChangePacket extends AbstractToServerMessage{
	private int pos[],id;
	public NotifyPointedBoxChangePacket(){}
	public<T extends TileEntity&MultiColisionProvider> NotifyPointedBoxChangePacket(T tile){
		pos=new int[]{tile.xCoord,tile.yCoord,tile.zCoord};
		id=MultiColisionProviderRayTracer.getPointedId(tile);
	}
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		writePos(buffer, pos);
		buffer.writeInt(id);
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		pos=readPos(buffer);
		id=buffer.readInt();
	}
	@Override
	public void process(EntityPlayer player, Side side){
		TileEntity tile=player.worldObj.getTileEntity(pos[0], pos[1], pos[2]);
		if(tile instanceof MultiColisionProvider){
			((MultiColisionProvider)tile).setPointedBox(((MultiColisionProvider)tile).getBoxes()[id]);
		}
	}

}
