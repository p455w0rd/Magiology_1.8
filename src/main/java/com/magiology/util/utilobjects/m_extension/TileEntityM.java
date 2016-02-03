package com.magiology.util.utilobjects.m_extension;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public abstract class TileEntityM extends TileEntity/* implements IUpdatePlayerListBox*/{
	public static final float p=1F/16F;
	@Override public Packet getDescriptionPacket(){NBTTagCompound tag=new NBTTagCompound();writeToNBT(tag);return new S35PacketUpdateTileEntity(pos, 1, tag);}
	@Override public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet){readFromNBT(packet.getNbtCompound());}
	
	public void sync(){
		if(worldObj==null||worldObj.isRemote)return;
		worldObj.markBlockForUpdate(pos);
		markDirty();
	}
	public void write3I(NBTTagCompound NBT,int x,int y,int z,String name){NBT.setInteger(name+"X", pos.getX());NBT.setInteger(name+"Y", pos.getY());NBT.setInteger(name+"Z", pos.getZ());}
	public int[] read3I(NBTTagCompound NBT,String name){return new int[]{ NBT.getInteger(name+"X"),   NBT.getInteger(name+"Y"),   NBT.getInteger(name+"Z")};  }
	public void writePos(NBTTagCompound NBT,BlockPos pos,String name){NBT.setInteger(name+"X", pos.getX());NBT.setInteger(name+"Y", pos.getY());NBT.setInteger(name+"Z", pos.getZ());}
	public BlockPos readPos(NBTTagCompound NBT,String name){return new BlockPos(NBT.getInteger(name+"X"),   NBT.getInteger(name+"Y"),   NBT.getInteger(name+"Z"));  }
	public int x(){return pos.getX();}
	public int y(){return pos.getY();}
	public int z(){return pos.getZ();}
	@Override
	public BlockPosM getPos(){
		return new BlockPosM(super.getPos());
	}
}
