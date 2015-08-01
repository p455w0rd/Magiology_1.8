package com.magiology.forgepowered.packets;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.vectors.Vec3M;

public class ClickHologramPacket extends AbstractToServerMessage{
	
	private Vec3M pos;
	private int pos;
	
	public ClickHologramPacket(){}
	public ClickHologramPacket(Vec3M pos,BlockPos pos){
		this.pos=pos;
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeFloat((float)pos.xCoord);
		buffer.writeFloat((float)pos.yCoord);
		buffer.writeFloat((float)pos.zCoord);
	}

	@Override
	public void read(PacketBuffer buffer)throws IOException{
		x=buffer.readInt();
		y=buffer.readInt();
		z=buffer.readInt();
		pos=Helper.Vec3M(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
	}

	@Override
	public void process(EntityPlayer player, Side side){
		TileEntity test=player.worldObj.getTileEntity(pos);
		if(test instanceof TileEntityHologramProjector){
			TileEntityHologramProjector tile=(TileEntityHologramProjector)test;
			tile.point.isPointing=true;
			tile.point.pointedPos=pos;
			tile.point.pointingPlayer=player;
			tile.onPressed(player);
		}
	}

}
