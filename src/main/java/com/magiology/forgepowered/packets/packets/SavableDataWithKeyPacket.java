package com.magiology.forgepowered.packets.packets;

import java.io.IOException;

import com.magiology.api.SavableData;
import com.magiology.api.network.NetworkInterface;
import com.magiology.forgepowered.packets.core.AbstractToClientMessage;
import com.magiology.util.utilclasses.PrintUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

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