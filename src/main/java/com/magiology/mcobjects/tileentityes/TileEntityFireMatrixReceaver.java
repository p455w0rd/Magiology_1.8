package com.magiology.mcobjects.tileentityes;

import com.magiology.mcobjects.effect.EntityMovingParticleFX;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.UtilM;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFireMatrixReceaver extends TileEntityPow{
	
	public BlockPos transferp=new BlockPos(0,0,0);
	int optimizer1=0,rXZ=30,rY=10;
	public boolean hasTransferPoint=false;
	public float rotation,prevRotation;
	TileEntity tile1;
	
	public TileEntityFireMatrixReceaver(){
		super(new boolean[]{false,false,false,false,false,false,true,true,true,true,true,true}, null, 1, 2, 10, 1500);
	}
	
	@Override
	public void update(){
		prevRotation=rotation;
		rotation+=3;
		if(rotation>360)rotation-=360;
		this.pover();
		if(optimizer1++>4){
			optimizer1=0;
			
			updateTransferPoint();
		}
		PowerUtil.sortSides(this);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound NBTTC){
		super.readFromNBT(NBTTC);
		transferp=readPos(NBTTC, "target");
	}
	@Override
	public void writeToNBT(NBTTagCompound NBTTC){
		super.writeToNBT(NBTTC);
		writePos(NBTTC, transferp, "target");
	}
	
	public void updateTransferPoint(){
		
		tile1=worldObj.getTileEntity(transferp);
		if(tile1 instanceof TileEntityFireMatrixTransferer){
			hasTransferPoint=true;
		}else{
			hasTransferPoint=false;
			transferp=pos.add(0,0,0);
		}
		
		if(hasTransferPoint){
			if(transferp.getX()>x()&&transferp.getX()-rXZ>x()){transferp=pos.add(0,0,0);}
			if(transferp.getZ()>z()&&transferp.getZ()-rXZ>z()){transferp=pos.add(0,0,0);}
			if(transferp.getY()>y()&&transferp.getY()-rXZ>y()){transferp=pos.add(0,0,0);}
			
			if(transferp.getX()<x()&&transferp.getX()+rXZ<x()){transferp=pos.add(0,0,0);}
			if(transferp.getZ()<z()&&transferp.getZ()+rXZ<z()){transferp=pos.add(0,0,0);}
			if(transferp.getY()<y()&&transferp.getY()+rXZ<y()){transferp=pos.add(0,0,0);}
		}
		
	}
	
	public void pover(){
		tryToSuckPowerFromTP();
	}
	
	
	

	int randum=10;
	int otimizer=20;
	private void tryToSuckPowerFromTP(){
		otimizer++;
		TileEntity tile1=worldObj.getTileEntity(transferp);
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
			
			
			if(sent&&worldObj.isRemote)UtilM.spawnEntityFX(new EntityMovingParticleFX(worldObj, transferp.getX()+0.5, transferp.getY()+((TileEntityFireMatrixTransferer) tile1).Pos+0.5, transferp.getZ()+0.5,
					x()+0.5, y()+0.5, z()+0.5, size/2, 1, 0.2+worldObj.rand.nextFloat()*0.5, 0.2+worldObj.rand.nextFloat()*0.2,0.5));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared(){
		return 40096.0D;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox(){
		AxisAlignedBB bb = new AxisAlignedBB(pos, pos.add(1,1,1));
		return bb;
	}

	@Override
	public void updateConnections(){
		UpdateablePipeHandler.onConnectionUpdate(this);
		
	}
	
}
