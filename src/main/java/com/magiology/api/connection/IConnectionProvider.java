package com.magiology.api.connection;

import net.minecraft.tileentity.*;
import net.minecraft.util.*;

public interface IConnectionProvider{
	public TileEntity getHost();
	public IConnection[] getConnections();
	public boolean isStrate(EnumFacing facing);
}
