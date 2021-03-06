package com.magiology.mcobjects.tileentityes;

import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;

import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFireMatrixTransferer extends TileEntityPow{
	
	public float Pos=0,speed=0,ballRotation=0;
	
	public TileEntityFireMatrixTransferer(){
		super(new boolean[]{true,false,true,true,true,true,false,false,false,false,false,false}, null, 1, 2, 10, 5500);
	}
	
	@Override
	public void update(){
		ballRotation+=10;
		
		if(Pos<p*1.5)speed+=0.0015;
		else if(Pos>p*1.5)speed-=0.0015;
		
		Pos+=speed;
		for(int a=0;a<2;a++){
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()+p*6.5+p*3*RandUtil.RF(),y()+p*6,z()+p*6.5+p*3*RandUtil.RF(),0,0.01+0.01*RandUtil.RF(),0,150+worldObj.rand.nextInt(10),1,2,1, 1, 0.2+RandUtil.RF()*0.5, 0.2+RandUtil.RF()*0.2, 1, 0.99));
		}
		if(worldObj.rand.nextInt(3)!=0){
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()+p*6,y()+p*12.5,z()+p*6,0.01,0.005*RandUtil.RF()+0.02,0.01,100+worldObj.rand.nextInt(10),2,-3,1, 1, 0.2+RandUtil.RF()*0.5, 0.2+RandUtil.RF()*0.2, 1, 0.98));
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()+p*10,y()+p*12.5,z()+p*6,-0.01,0.005*RandUtil.RF()+0.02,0.01,100+worldObj.rand.nextInt(10),2,-3,1, 1, 0.2+RandUtil.RF()*0.5, 0.2+RandUtil.RF()*0.2, 1, 0.98));
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()+p*10,y()+p*12.5,z()+p*10,-0.01,0.005*RandUtil.RF()+0.02,-0.01,100+worldObj.rand.nextInt(10),2,-3,1, 1, 0.2+RandUtil.RF()*0.5, 0.2+RandUtil.RF()*0.2, 1, 0.98));
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()+p*6,y()+p*12.5,z()+p*10,0.01,0.005*RandUtil.RF()+0.02,-0.01,100+worldObj.rand.nextInt(10),2,-3,1, 1, 0.2+RandUtil.RF()*0.5, 0.2+RandUtil.RF()*0.2, 1, 0.98));
		}
		
		PowerUtil.sortSides(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 40096.0D;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		AxisAlignedBB bb = new AxisAlignedBB(pos, pos.add(1,1,1));
		return bb;
	}

	@Override
	public void updateConnections(){
		UpdateablePipeHandler.onConnectionUpdate(this);
		
	}
}
