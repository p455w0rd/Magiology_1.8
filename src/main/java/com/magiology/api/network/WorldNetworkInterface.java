package com.magiology.api.network;

import java.util.List;

import com.magiology.api.network.NetworkBaseInterface.InteractType;
import com.magiology.mcobjects.tileentityes.hologram.ICommandInteract;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;

public interface WorldNetworkInterface extends BasicWorldNetworkInterface{
	public long getCard();
	public void onNetworkActionInvoked(NetworkBaseInterface Interface,String action,Object... data);
	public TileEntityNetworkController getBrain();
	public NetworkBaseInterface getConnectedInterface();
	public InteractType[] getInteractTypes();
	public List<ICommandInteract> getCommandInteractors();
}
