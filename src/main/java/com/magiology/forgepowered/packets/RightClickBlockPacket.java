package com.magiology.forgepowered.packets;

import java.io.IOException;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;

public class RightClickBlockPacket extends AbstractToServerMessage{
	
	private byte side=-1;
	private BlockPos pos;
	
	public RightClickBlockPacket(){}
	public RightClickBlockPacket(int x,int y,int z,byte side){
		this.side=side;
		this.pos=new BlockPos(x, y, z);
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
	public void process(EntityPlayer player, Side side){
		IBlockState state=player.worldObj.getBlockState(pos);
		state.getBlock().onBlockActivated(player.worldObj, pos, state, player, EnumFacing.getFront(this.side), 0.5F, 0.5F, 0.5F);
	}

}
