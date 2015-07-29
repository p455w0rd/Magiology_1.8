package com.magiology.api.network;

import com.magiology.api.network.NetworkBaseInterface.InteractType;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;

public interface NetworkInterfaceProvider extends BasicNetworkInterfaceProvider{
	public long getCard();
	public void onNetworkActionInvoked(NetworkBaseInterface Interface,String action,Object... data);
	public TileEntityNetworkController getBrain();
	public NetworkBaseInterface getConnectedInterface();
	public InteractType[] getInteractTypes();
}
