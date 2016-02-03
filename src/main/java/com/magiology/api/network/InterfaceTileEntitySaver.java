package com.magiology.api.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public interface InterfaceTileEntitySaver{
	public void readFromNBT(NBTTagCompound NBT);
	public void writeToNBT(NBTTagCompound NBT);
	public TileEntity getBoundTile();
	public void setBoundTile(TileEntity tile);
}
