package com.magiology.mcobjects.tileentityes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import com.magiology.mcobjects.effect.EntityMovingParticleFX;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.PowerHelper;
import com.magiology.upgrades.RegisterUpgrades.Container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityFireMatrixReceaver extends TileEntityPow{
	
	public int[] transferp=new int[3];
	int optimizer1=0,rXZ=30,rY=10;
	public boolean hasTransferPoint=false;
	public float rotation,prevRotation;
	TileEntity tile1;
	
	public TileEntityFireMatrixReceaver(){
		super(new boolean[]{false,false,false,false,false,false,true,true,true,true,true,true}, null, 1, 2, 10, 1500);
		this.initUpgrades(Container.TeleTransfer);
	}
	
	@Override
	public void updateEntity(){
		super.updateEntity();
		prevRotation=rotation;
		rotation+=3;
		if(rotation>360)rotation-=360;
		this.pover();
		if(optimizer1++>4){
			optimizer1=0;
			
			updateTransferPoint();
		}
		PowerHelper.sortSides(this);
	}
	
	@Override
	public void addToReadFromNBT(NBTTagCompound NBTTC){
		for(int a=0;a<transferp.length;a++)transferp[a]=NBTTC.getInteger("target"+a);
	}
	@Override
	public void addToWriteToNBT(NBTTagCompound NBTTC){
		for(int a=0;a<transferp.length;a++)NBTTC.setInteger("target"+a, transferp[a]);
	}
	
	public void updateTransferPoint(){
		
		tile1=worldObj.getTileEntity(transferp[0], transferp[1], transferp[2]);
		if(tile1 instanceof TileEntityFireMatrixTransferer){
			hasTransferPoint=true;
		}else{
			hasTransferPoint=false;
			transferp[0]=xCoord;
			transferp[1]=yCoord;
			transferp[2]=zCoord;
		}
		
		if(hasTransferPoint){
			if(transferp[0]>xCoord)if(transferp[0]-rXZ>xCoord){transferp[0]=xCoord;transferp[2]=zCoord;transferp[1]=yCoord;}
			if(transferp[2]>zCoord)if(transferp[2]-rXZ>zCoord){transferp[0]=xCoord;transferp[2]=zCoord;transferp[1]=yCoord;}
			if(transferp[1]>yCoord)if(transferp[1]-rXZ>yCoord) {transferp[0]=xCoord;transferp[2]=zCoord;transferp[1]=yCoord;}
			
			if(transferp[0]<xCoord)if(transferp[0]+rXZ<xCoord){transferp[0]=xCoord;transferp[2]=zCoord;transferp[1]=yCoord;}
			if(transferp[2]<zCoord)if(transferp[2]+rXZ<zCoord){transferp[0]=xCoord;transferp[2]=zCoord;transferp[1]=yCoord;}
			if(transferp[1]<yCoord)if(transferp[1]+rXZ<yCoord) {transferp[0]=xCoord;transferp[2]=zCoord;transferp[1]=yCoord;}
		}
		
	}
	
	public void pover(){
		tryToSuckPowerFromTP();
	}
	
	
	

	int randum=10;
	int otimizer=20;
	private void tryToSuckPowerFromTP(){
		otimizer++;
		TileEntity tile1=worldObj.getTileEntity(transferp[0], transferp[1], transferp[2]);
		if(tile1 instanceof TileEntityFireMatrixTransferer&&otimizer>randum){
			otimizer=0;
			randum=worldObj.rand.nextInt(7)-3+20;
			TileEntityFireMatrixTransferer tile=(TileEntityFireMatrixTransferer)tile1;
			boolean sent=false;
			int size=0;
			for(int l=0;l<200;l++){
				if(this.currentEnergy+this.maxTSpeed<=this.maxEnergyBuffer&&tile.currentEnergy>=tile.maxTSpeed){
					this.currentEnergy+=tile.maxTSpeed;
					tile.currentEnergy-=tile.maxTSpeed;
					size+=tile.maxTSpeed;
					sent=true;
				}else l=200;}
			for(int l=0;l<200;l++){
				if(this.currentEnergy+this.middleTSpeed<=this.maxEnergyBuffer&&tile.currentEnergy>=tile.middleTSpeed){
					this.currentEnergy+=tile.middleTSpeed;
					tile.currentEnergy-=tile.middleTSpeed;
					size+=tile.middleTSpeed;
					sent=true;
				}else l=200;}
			for(int l=0;l<200;l++){
				if(this.currentEnergy+this.minTSpeed<=this.maxEnergyBuffer&&tile.currentEnergy>=tile.minTSpeed){
					this.currentEnergy+=tile.minTSpeed;
					tile.currentEnergy-=tile.minTSpeed;
					size+=tile.minTSpeed;
					sent=true;
				}else l=200;}
			
			
			if(sent&&worldObj.isRemote)Helper.spawnEntityFX(new EntityMovingParticleFX(worldObj, 
					transferp[0]+0.5, transferp[1]+((TileEntityFireMatrixTransferer) tile1).pos+0.5, transferp[2]+0.5,
					xCoord+0.5, yCoord+0.5, zCoord+0.5, size/2, 1, 0.2+worldObj.rand.nextFloat()*0.5, 0.2+worldObj.rand.nextFloat()*0.2,0.5));
		}
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
