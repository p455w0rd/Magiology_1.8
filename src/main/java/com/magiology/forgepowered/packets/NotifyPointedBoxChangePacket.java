package com.magiology.forgepowered.packets;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider.MultiColisionProviderRayTracer;

public class NotifyPointedBoxChangePacket extends AbstractToServerMessage{
	BlockPos pos;
	private int id;
	public NotifyPointedBoxChangePacket(){}
	public<T extends TileEntity&MultiColisionProvider> NotifyPointedBoxChangePacket(T tile){
		pos=tile.getPos();
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
		TileEntity tile=player.worldObj.getTileEntity(pos);
		if(tile instanceof MultiColisionProvider){
			((MultiColisionProvider)tile).setPointedBox(((MultiColisionProvider)tile).getBoxes()[id]);
		}
	}

}
