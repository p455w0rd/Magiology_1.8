package com.magiology.api.network;

import java.util.*;

import com.magiology.api.lang.*;
import com.magiology.mcobjects.tileentityes.network.*;

public interface WorldNetworkInterface extends BasicWorldNetworkInterface{
	public long getCard();
	public void onNetworkActionInvoked(NetworkInterface Interface,String action,Object... data);
	public TileEntityNetworkController getBrain();
	public NetworkInterface getConnectedInterface();
	public List<ICommandInteract> getCommandInteractors();
}
