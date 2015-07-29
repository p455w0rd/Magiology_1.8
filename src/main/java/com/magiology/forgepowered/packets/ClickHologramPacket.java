package com.magiology.forgepowered.packets;

import java.io.IOException;

import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.objhelper.helpers.Helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import cpw.mods.fml.relauncher.Side;

public class ClickHologramPacket extends AbstractToServerMessage{
	
	private Vec3 pos;
	private int x, y, z;
	
	public ClickHologramPacket(){}
	public ClickHologramPacket(Vec3 pos,int x,int y,int z){
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
		pos=Helper.Vec3(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
	}

	@Override
	public void process(EntityPlayer player, Side side){
		TileEntity test=player.worldObj.getTileEntity(x, y, z);
		if(test instanceof TileEntityHologramProjector){
			TileEntityHologramProjector tile=(TileEntityHologramProjector)test;
			tile.point.isPointing=true;
			tile.point.pointedPos=pos;
			tile.point.pointingPlayer=player;
			tile.onPressed(player);
		}
	}

}
