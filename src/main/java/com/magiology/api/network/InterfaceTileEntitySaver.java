package com.magiology.api.network;

import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;

public interface InterfaceTileEntitySaver{
	public void readFromNBT(NBTTagCompound NBT);
	public void writeToNBT(NBTTagCompound NBT);
	public TileEntity getBoundTile();
	public void setBoundTile(TileEntity tile);
}
