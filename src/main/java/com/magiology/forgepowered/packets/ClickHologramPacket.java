package com.magiology.forgepowered.packets;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class ClickHologramPacket extends AbstractToServerMessage{
	
	private Vec3M pos;
	private BlockPos bPos;
	
	public ClickHologramPacket(){}
	public ClickHologramPacket(Vec3M pos,BlockPos bPos){
		this.bPos=bPos;
		this.pos=pos;
	}
	
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		writePos(buffer, bPos);
		writeVec3M(buffer, pos);
	}

	@Override
	public void read(PacketBuffer buffer)throws IOException{
		bPos=readPos(buffer);
		pos=readVec3M(buffer);
	}

	@Override
	public void process(EntityPlayer player, Side side){
		TileEntity test=player.worldObj.getTileEntity(bPos);
		if(test instanceof TileEntityHologramProjector){
			TileEntityHologramProjector tile=(TileEntityHologramProjector)test;
			tile.point.isPointing=true;
			tile.point.pointedPos=pos;
			tile.point.pointingPlayer=player;
			tile.onPressed(player);
		}
	}
}
