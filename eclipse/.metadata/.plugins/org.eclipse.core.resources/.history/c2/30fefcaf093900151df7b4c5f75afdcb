package com.magiology.mcobjects.tileentityes.baterys;

import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.tileentityes.TileEntityBateryGeneric;
import com.magiology.objhelper.helpers.Helper;

public class TileEntityBateryL100 extends TileEntityBateryGeneric {
	public TileEntityBateryL100(){
		super(null, null, 1, 5, 50, 2147483640);
	}
	@Override
	public void updateEntity(){
		super.updateEntity();
		if(worldObj.isRemote){
			Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.2, yCoord+0.5, zCoord+0.5,  0.01+0.001-0.002*worldObj.rand.nextFloat(), 0,     0.001-0.002*worldObj.rand.nextFloat(),     100, 3, 0.6+0.001-0.002*worldObj.rand.nextFloat(),1, 1,worldObj.rand.nextFloat()/4,worldObj.rand.nextFloat()/4, 0.5, 0.99));
			Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.8, yCoord+0.5, zCoord+0.5, -0.01+0.001-0.002*worldObj.rand.nextFloat(), 0,     0.001-0.002*worldObj.rand.nextFloat(),     100, 3, 0.6+0.001-0.002*worldObj.rand.nextFloat(),1, 1,worldObj.rand.nextFloat()/4,worldObj.rand.nextFloat()/4, 0.3, 0.99));
			Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5, yCoord+0.2, zCoord+0.5,  0.001-0.002*worldObj.rand.nextFloat(),      0.003, 0.001-0.002*worldObj.rand.nextFloat(),     100, 3, 0.6+0.001-0.002*worldObj.rand.nextFloat(),1, 1,worldObj.rand.nextFloat()/4,worldObj.rand.nextFloat()/4, 0.3, 0.99));
			Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5, yCoord+0.8, zCoord+0.5,  0.001-0.002*worldObj.rand.nextFloat(),     -0.01,  0.001-0.002*worldObj.rand.nextFloat(),     100, 3, 0.6+0.001-0.002*worldObj.rand.nextFloat(),1, 1,worldObj.rand.nextFloat()/4,worldObj.rand.nextFloat()/4, 0.3, 0.99));
			Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5, yCoord+0.5, zCoord+0.2,  0.001-0.002*worldObj.rand.nextFloat(),      0,     0.01+0.001-0.002*worldObj.rand.nextFloat(),100, 3, 0.6+0.001-0.002*worldObj.rand.nextFloat(),1, 1,worldObj.rand.nextFloat()/4,worldObj.rand.nextFloat()/4, 0.3, 0.99));
			Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5, yCoord+0.5, zCoord+0.8,  0.001-0.002*worldObj.rand.nextFloat(),      0,    -0.01+0.001-0.002*worldObj.rand.nextFloat(),100, 3, 0.6+0.001-0.002*worldObj.rand.nextFloat(),1, 1,worldObj.rand.nextFloat()/4,worldObj.rand.nextFloat()/4, 0.3, 0.99));
		}
	}
}