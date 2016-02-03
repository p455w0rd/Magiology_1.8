package com.magiology.mcobjects.tileentityes.baterys;

import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.tileentityes.TileEntityBateryGeneric;
import com.magiology.util.utilclasses.UtilM;

public class TileEntityBateryL100 extends TileEntityBateryGeneric {
	public TileEntityBateryL100(){
		super(null, null, 1, 5, 50, 2147483640);
	}
	@Override
	public void update(){
		super.update();
		if(worldObj.isRemote){
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()+0.2, y()+0.5, z()+0.5,  0.01+0.001-0.002*worldObj.rand.nextFloat(), 0,	 0.001-0.002*worldObj.rand.nextFloat(),	 100, 3, 0.6+0.001-0.002*worldObj.rand.nextFloat(),1, 1,worldObj.rand.nextFloat()/4,worldObj.rand.nextFloat()/4, 0.5, 0.99));
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()+0.8, y()+0.5, z()+0.5, -0.01+0.001-0.002*worldObj.rand.nextFloat(), 0,	 0.001-0.002*worldObj.rand.nextFloat(),	 100, 3, 0.6+0.001-0.002*worldObj.rand.nextFloat(),1, 1,worldObj.rand.nextFloat()/4,worldObj.rand.nextFloat()/4, 0.3, 0.99));
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()+0.5, y()+0.2, z()+0.5,  0.001-0.002*worldObj.rand.nextFloat(),	  0.003, 0.001-0.002*worldObj.rand.nextFloat(),	 100, 3, 0.6+0.001-0.002*worldObj.rand.nextFloat(),1, 1,worldObj.rand.nextFloat()/4,worldObj.rand.nextFloat()/4, 0.3, 0.99));
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()+0.5, y()+0.8, z()+0.5,  0.001-0.002*worldObj.rand.nextFloat(),	 -0.01,  0.001-0.002*worldObj.rand.nextFloat(),	 100, 3, 0.6+0.001-0.002*worldObj.rand.nextFloat(),1, 1,worldObj.rand.nextFloat()/4,worldObj.rand.nextFloat()/4, 0.3, 0.99));
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()+0.5, y()+0.5, z()+0.2,  0.001-0.002*worldObj.rand.nextFloat(),	  0,	 0.01+0.001-0.002*worldObj.rand.nextFloat(),100, 3, 0.6+0.001-0.002*worldObj.rand.nextFloat(),1, 1,worldObj.rand.nextFloat()/4,worldObj.rand.nextFloat()/4, 0.3, 0.99));
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()+0.5, y()+0.5, z()+0.8,  0.001-0.002*worldObj.rand.nextFloat(),	  0,	-0.01+0.001-0.002*worldObj.rand.nextFloat(),100, 3, 0.6+0.001-0.002*worldObj.rand.nextFloat(),1, 1,worldObj.rand.nextFloat()/4,worldObj.rand.nextFloat()/4, 0.3, 0.99));
		}
	}
}