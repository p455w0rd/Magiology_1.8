package com.magiology.mcobjects.tileentityes;

import net.minecraft.util.BlockPos;

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
			BlockPos pos1=SideHelper.offset(side,pos);
			if(this.isAnyBatery(pos1)){
				TileEntityBateryGeneric tile= (TileEntityBateryGeneric) worldObj.getTileEntity(pos1);
				
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
