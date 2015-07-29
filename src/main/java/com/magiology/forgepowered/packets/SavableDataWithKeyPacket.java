package com.magiology.forgepowered.packets;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.magiology.api.SavableData;
import com.magiology.api.network.NetworkBaseInterface;

import cpw.mods.fml.relauncher.Side;

public class SavableDataWithKeyPacket extends AbstractToClientMessage{
	private int id,x,y,z;
	private String key;
	private SavableData data;
	public SavableDataWithKeyPacket(){}
	public SavableDataWithKeyPacket(NetworkBaseInterface tile,String key, SavableData data){
		super(new SendingTarget(tile.getHost().getWorldObj(), tile.getHost().getWorldObj().provider.dimensionId));
		x=tile.getHost().xCoord;
		y=tile.getHost().yCoord;
		z=tile.getHost().zCoord;
		this.key=key;
		this.data=data;
	}
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		writeString(buffer, key);
		writeSavableData(buffer, data);
	}
	@Override
	public void read(PacketBuffer buffer)throws IOException{
		x=buffer.readInt();
		y=buffer.readInt();
		z=buffer.readInt();
		key=readString(buffer);
		data=readSavableData(buffer);
	}
	@Override
	public void process(EntityPlayer player, Side side){
		World world=player.worldObj;
		TileEntity tile=world.getTileEntity(x, y, z);
		if(tile instanceof NetworkBaseInterface){
			((NetworkBaseInterface)tile).setInteractData(key, data);
			return;
		}
		System.out.print("PACKET HAS FAILED TO DELIVER THE DATA!\n");
	}
}