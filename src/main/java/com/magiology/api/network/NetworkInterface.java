package com.magiology.api.network;

import java.util.*;

import net.minecraft.item.*;
import net.minecraft.tileentity.*;

import com.magiology.mcobjects.tileentityes.network.*;

public interface NetworkInterface{
	
	public TileEntityNetworkController getBrain();
	public WorldNetworkInterface getInterfaceProvider();
	public Map<String,Object> getData();
	public Object getInteractData(String string);
	public  void  setInteractData(String string,Object object);
	public long getActiveCard();
	public TileEntity getHost();
	public List<TileEntityNetworkRouter> getPointerContainers();
	public List<ItemStack> getPointers();
	
}
