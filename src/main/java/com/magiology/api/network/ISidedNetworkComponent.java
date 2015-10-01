package com.magiology.api.network;

import net.minecraft.tileentity.TileEntity;

public interface ISidedNetworkComponent extends NetworkBaseComponent{
	public boolean getAccessibleOnSide(int side);
	public boolean isSideEnabled(int side);
	public void setAccessibleOnSide(int side,boolean accessible);
	public TileEntity getHost();
	public int  getOrientation();
	public void setOrientation(int orientation);
	
	public class ISidedNetworkComponentHandler extends NetworkBaseComponentHandler{
		
	}

}
