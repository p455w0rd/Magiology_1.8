package com.magiology.mcobjects.tileentityes;

import net.minecraft.util.AxisAlignedBB;

import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.PowerHelper;
import com.magiology.upgrades.RegisterUpgrades.Container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityFireMatrixTransferer extends TileEntityPow{
	
	public float pos=0,speed=0,ballRotation=0;
	
	public TileEntityFireMatrixTransferer(){
		super(new boolean[]{true,false,true,true,true,true,false,false,false,false,false,false}, null, 1, 2, 10, 5500);
		this.initUpgrades(Container.TeleTransfer);
	}
	
	@Override
	public void updateEntity(){
		super.updateEntity();
		ballRotation+=10;
		
		if(pos<p*1.5)speed+=0.0015;
		else if(pos>p*1.5)speed-=0.0015;
		
		pos+=speed;
		for(int a=0;a<2;a++){
			Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+p*6.5+p*3*Helper.RF(),yCoord+p*6,zCoord+p*6.5+p*3*Helper.RF(),0,0.01+0.01*Helper.RF(),0,150+worldObj.rand.nextInt(10),1,2,1, 1, 0.2+Helper.RF()*0.5, 0.2+Helper.RF()*0.2, 1, 0.99));
		}
		if(worldObj.rand.nextInt(3)!=0){
			Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+p*6,yCoord+p*12.5,zCoord+p*6,0.01,0.005*Helper.RF()+0.02,0.01,100+worldObj.rand.nextInt(10),2,-3,1, 1, 0.2+Helper.RF()*0.5, 0.2+Helper.RF()*0.2, 1, 0.98));
			Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+p*10,yCoord+p*12.5,zCoord+p*6,-0.01,0.005*Helper.RF()+0.02,0.01,100+worldObj.rand.nextInt(10),2,-3,1, 1, 0.2+Helper.RF()*0.5, 0.2+Helper.RF()*0.2, 1, 0.98));
			Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+p*10,yCoord+p*12.5,zCoord+p*10,-0.01,0.005*Helper.RF()+0.02,-0.01,100+worldObj.rand.nextInt(10),2,-3,1, 1, 0.2+Helper.RF()*0.5, 0.2+Helper.RF()*0.2, 1, 0.98));
			Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+p*6,yCoord+p*12.5,zCoord+p*10,0.01,0.005*Helper.RF()+0.02,-0.01,100+worldObj.rand.nextInt(10),2,-3,1, 1, 0.2+Helper.RF()*0.5, 0.2+Helper.RF()*0.2, 1, 0.98));
		}
		
		PowerHelper.sortSides(this);
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
        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1);
        return bb;
    }
}
