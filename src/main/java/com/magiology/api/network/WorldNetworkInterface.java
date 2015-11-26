package com.magiology.api.network;

import java.util.List;

import com.magiology.api.lang.ICommandInteract;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;

public interface WorldNetworkInterface extends BasicWorldNetworkInterface{
	public long getCard();
	public void onNetworkActionInvoked(NetworkInterface Interface,String action,Object... data);
	public TileEntityNetworkController getBrain();
	public NetworkInterface getConnectedInterface();
	public List<ICommandInteract> getCommandInteractors();
}
