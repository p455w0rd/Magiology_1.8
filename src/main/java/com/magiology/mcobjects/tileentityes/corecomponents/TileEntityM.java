package com.magiology.mcobjects.tileentityes.corecomponents;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.magiology.objhelper.helpers.Helper.H;

public abstract class TileEntityM extends TileEntity{
	public static final float p=1F/16F;
	@Override public Packet getDescriptionPacket(){NBTTagCompound tag=new NBTTagCompound();writeToNBT(tag);return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);}
    @Override public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet){readFromNBT(packet.func_148857_g());}
    
    public void sync(){
    	if(H.isRemote(this))return;
    	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    	markDirty();
    }
    public void write3I(NBTTagCompound NBT,int x,int y,int z,String name){NBT.setInteger(name+"X", x);NBT.setInteger(name+"Y", y);NBT.setInteger(name+"Z", z);}
    public int[] read3I(NBTTagCompound NBT,String name){return new int[]{ NBT.getInteger(name+"X"),   NBT.getInteger(name+"Y"),   NBT.getInteger(name+"Z")};  }
}
