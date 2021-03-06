package com.magiology.api.connection;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public interface IConnectionProvider{
	public TileEntity getHost();
	public IConnection[] getConnections();
	public boolean isStrate(EnumFacing facing);
}
