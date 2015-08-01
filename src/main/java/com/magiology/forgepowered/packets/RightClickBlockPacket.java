package com.magiology.forgepowered.packets;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

public class RightClickBlockPacket extends AbstractToServerMessage{
	
	private byte side=-1;
	private int x=-1,y=-1,z=-1;
	
	public RightClickBlockPacket(){}
	public RightClickBlockPacket(int x,int y,int z,byte side){
		this.side=side;
		this.x=x;
		this.y=y;
		this.z=z;
	}
	@Override
	public void write(PacketBuffer buffer) throws IOException{
		buffer.writeByte(side);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		side=buffer.readByte();
		x=buffer.readInt();
		y=buffer.readInt();
		z=buffer.readInt();
	}
	@Override
	public void process(EntityPlayer player, Side side){
		player.worldObj.getBlock(pos).onBlockActivated(player.worldObj, pos, player, this.side, 0.5F, 0.5F, 0.5F);
	}

}
