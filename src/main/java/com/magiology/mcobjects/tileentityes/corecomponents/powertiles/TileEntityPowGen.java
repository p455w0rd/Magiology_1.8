package com.magiology.mcobjects.tileentityes.corecomponents.powertiles;

import net.minecraft.nbt.NBTTagCompound;

import com.magiology.api.power.PowerProducer;

public abstract class TileEntityPowGen extends TileEntityPow implements PowerProducer{
	
	public TileEntityPowGen(boolean[] allowedSidedPower, boolean[] sidedPower,int minTSpeed, int middleTSpeed, int maxTSpeed, int maxEnergyBuffer, int maxFuel){
		super(allowedSidedPower, sidedPower, minTSpeed, middleTSpeed, maxTSpeed,maxEnergyBuffer);
		this.maxFuel=maxFuel;
	}
	public int fuel,maxFuel;
	@Override
	public int getFuel(){
		return fuel;
	}
	@Override
	public int getMaxFuel(){
		return maxFuel;
	}
	@Override
	public void setFuel(int Int){
		fuel=Int;
	}
	@Override
	public void setMaxFuel(int Int){
		maxFuel=Int;
	}
	@Override
	public void addFuel(int Int){
		fuel+=Int;
	}
	@Override
	public void subtractFuel(int Int){
		fuel-=Int;
	}
	@Override
	public boolean canGeneratePower(int Add){
		return canGeneratePowerAddon()&&(getMaxEnergyBuffer()>=getEnergy()+Add?true:false);
	}
	@Override public abstract void generateFunction();
	@Override public abstract boolean canGeneratePowerAddon();

	@Override
	public void readFromItemOnPlace(NBTTagCompound NBT){
		super.readFromItemOnPlace(NBT);
		setFuel(NBT.getInteger(SAVE_TO_ITEM_PREFIX+"fuel"));
	}
	@Override
	public void writeToItemOnWrenched(NBTTagCompound NBT){
		super.writeToItemOnWrenched(NBT);
		NBT.setInteger(SAVE_TO_ITEM_PREFIX+"fuel", getFuel());
		NBT.setInteger(SAVE_TO_ITEM_PREFIX+"fuelMax", getMaxFuel());
	}
}