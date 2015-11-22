package com.magiology.api.network;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkRouter;

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
