package com.magiology.forgepowered.packets.packets;

import java.io.*;

import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.forgepowered.packets.core.*;
import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.util.utilobjects.vectors.*;

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
	public IMessage process(EntityPlayer player, Side side){
		TileEntity test=player.worldObj.getTileEntity(bPos);
		if(test instanceof TileEntityHologramProjector){
			TileEntityHologramProjector tile=(TileEntityHologramProjector)test;
			tile.point.isPointing=true;
			tile.point.pointedPos=pos;
			tile.point.pointingPlayer=player;
			tile.onPressed(player);
		}
		return null;
	}
}
