package com.magiology.api.power;

public interface PowerCore{

	public int getCurrentEnergy();
	public int getMaxTSpeed();
	public int getMiddleTSpeed();
	public int getMinTSpeed();
	public int getMaxEnergyBuffer();
	public int getModedMaxEnergyBuffer();
	public int getModedMaxTSpeed();
	
	public void setCurrentEnergy(int Int);
	public void setMaxTSpeed(int Int);
	public void setMiddleTSpeed(int Int);
	public void setMinTSpeed(int Int);
	public void setMaxEnergyBuffer(int Int);
	public void setModedMaxEnergyBuffer(int Int);
	public void setModedMaxTSpeed(int Int);
	
	public void addEnergy(int amount);
	public void subtractEnergy(int amount);
	
}
