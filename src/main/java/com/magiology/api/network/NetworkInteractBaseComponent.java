package com.magiology.api.network;


public interface NetworkInteractBaseComponent extends ISidedNetworkComponent{
	
	public void update();
	public NetworkInterface getInterface();
	public boolean hasAnInterface();
	public boolean isActive();
}
