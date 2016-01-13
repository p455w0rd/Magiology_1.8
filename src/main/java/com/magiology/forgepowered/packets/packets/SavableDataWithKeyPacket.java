package com.magiology.forgepowered.packets.packets;

import java.io.*;

import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.api.*;
import com.magiology.api.network.*;
import com.magiology.forgepowered.packets.core.*;
import com.magiology.util.utilclasses.*;

public class SavableDataWithKeyPacket extends AbstractToClientMessage{
	private BlockPos pos;
	private String key;
	private SavableData data;
	public SavableDataWithKeyPacket(){}
	public SavableDataWithKeyPacket(NetworkInterface tile,String key, SavableData data){
		super(new SendingTarget(tile.getHost().getWorld(), tile.getHost().getWorld().provider.getDimensionId()));
		pos=tile.getHost().getPos();
		this.key=key;
		this.data=data;
	}
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		writePos(buffer, pos);
		writeString(buffer, key);
		writeSavableData(buffer, data);
	}
	@Override
	public void read(PacketBuffer buffer)throws IOException{
		pos=readPos(buffer);
		key=readString(buffer);
		data=readSavableData(buffer);
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		World world=player.worldObj;
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof NetworkInterface){
			((NetworkInterface)tile).setInteractData(key, data);
			return null;
		}
		PrintUtil.println("PACKET HAS FAILED TO DELIVER THE DATA!\n");
		return null;
	}
}