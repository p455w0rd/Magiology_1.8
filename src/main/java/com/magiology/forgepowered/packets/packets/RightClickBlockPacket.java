package com.magiology.forgepowered.packets.packets;

import java.io.*;

import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.forgepowered.packets.core.*;

public class RightClickBlockPacket extends AbstractToServerMessage{
	
	private byte side=-1;
	private BlockPos pos;
	
	public RightClickBlockPacket(){}
	public RightClickBlockPacket(BlockPos pos,byte side){
		this.side=side;
		this.pos=pos;
	}
	@Override
	public void write(PacketBuffer buffer) throws IOException{
		buffer.writeByte(side);
		writePos(buffer, pos);
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		side=buffer.readByte();
		pos=readPos(buffer);
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		IBlockState state=player.worldObj.getBlockState(pos);
		state.getBlock().onBlockActivated(player.worldObj, pos, state, player, EnumFacing.getFront(this.side), 0.5F, 0.5F, 0.5F);
		return null;
	}

}
