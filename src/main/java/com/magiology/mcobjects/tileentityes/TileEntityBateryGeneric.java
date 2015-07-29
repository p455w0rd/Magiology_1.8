package com.magiology.mcobjects.tileentityes;

import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.objhelper.helpers.PowerHelper;
import com.magiology.objhelper.helpers.SideHelper;

public class TileEntityBateryGeneric extends TileEntityPow{
	
	
	
	public TileEntityBateryGeneric(boolean[] allowedSidedPower,boolean[] sidedPower, int minTSpeed, int middleTSpeed,int maxTSpeed, int maxEnergyBuffer) {
		super(allowedSidedPower, sidedPower, minTSpeed, middleTSpeed, maxTSpeed,maxEnergyBuffer);
	}
	
	public void fromBateryToBatery(){
		int[] s=SideHelper.randomizeSides();
		for(int a=0;a<6;a++){
			int side=s[a];
			int x=SideHelper.X(side,xCoord), y=SideHelper.Y(side,yCoord), z=SideHelper.Z(side,zCoord);
			if(this.isAnyBatery(x,y,z)){
				TileEntityBateryGeneric tile= (TileEntityBateryGeneric) worldObj.getTileEntity(x,y,z);
				
				PowerHelper.tryToEquateEnergy(this, tile, PowerHelper.getMaxSpeed(this, tile),side);
				PowerHelper.tryToEquateEnergy(this, tile, PowerHelper.getMiddleSpeed(this, tile),side);
				PowerHelper.tryToEquateEnergy(this, tile, PowerHelper.getMinSpeed(this, tile),side);
			}
			
		}
	}

	@Override
	public void updateEntity(){
		super.updateEntity();
		this.power();
		PowerHelper.sortSides(this);
	}
	
	public void power(){
		fromBateryToBatery();
	}
}
