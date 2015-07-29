package com.magiology.mcobjects.tileentityes;

import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityM;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityBFCPowerOut extends TileEntityM{
	
	public ForgeDirection[] CallDir = new ForgeDirection[4];
	public ForgeDirection[] PallDir = new ForgeDirection[4];
	int optimizer1=0;
	int optimizer2=0;
	int optimizer3=0;
	public float animationP1=0;
	public float animationP2=0;
	
	
	@Override
	public void updateEntity()
	{
		if(optimizer1++>=60){
			optimizer1=0;
			if(getTEBFC(xCoord-2, yCoord-1, zCoord))CallDir[0]=ForgeDirection.EAST;
			else CallDir[0]=null;
			
			if(getTEBFC(xCoord+2, yCoord-1, zCoord))CallDir[1]=ForgeDirection.WEST;
			else CallDir[1]=null;
			
			if(getTEBFC(xCoord, yCoord-1, zCoord-2))CallDir[2]=ForgeDirection.NORTH;
			else CallDir[2]=null;
			
			if(getTEBFC(xCoord, yCoord-1, zCoord+2))CallDir[3]=ForgeDirection.SOUTH;
			else CallDir[3]=null;
			
			if(optimizer3++>=2){
				optimizer3=0;
				
				if(CallDir[0]==null&&CallDir[1]==null&&CallDir[2]==null&&CallDir[3]==null){
					worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.nether_brick);
				}
			}
		}
		if(optimizer2++>=10){
			optimizer2=0;
			if(CallDir[0]!=null&&getPipe(xCoord+1, yCoord, zCoord))PallDir[0]=ForgeDirection.EAST;
			else PallDir[0]=null;
			
			if(CallDir[1]!=null&&getPipe(xCoord-1, yCoord, zCoord))PallDir[1]=ForgeDirection.WEST;
			else PallDir[1]=null;
			
			if(CallDir[2]!=null&&getPipe(xCoord, yCoord, zCoord+1))PallDir[2]=ForgeDirection.NORTH;
			else PallDir[2]=null;
			
			if(CallDir[3]!=null&&getPipe(xCoord, yCoord, zCoord-1))PallDir[3]=ForgeDirection.SOUTH;
			else PallDir[3]=null;
		}
		
		sendEnergy();
		
		this.animaion();
	}
	
	
	public void sendEnergy(){
		TileEntityBigFurnaceCore tile;
		if(PallDir[0]!=null){
			tile=(TileEntityBigFurnaceCore)worldObj.getTileEntity(xCoord-2, yCoord-1, zCoord);
		}
		else if(PallDir[1]!=null){
			tile=(TileEntityBigFurnaceCore)worldObj.getTileEntity(xCoord+2, yCoord-1, zCoord);
		}
		else if(PallDir[2]!=null){
			tile=(TileEntityBigFurnaceCore)worldObj.getTileEntity(xCoord, yCoord-1, zCoord-2);
		}
		else if(PallDir[3]!=null){
			tile=(TileEntityBigFurnaceCore)worldObj.getTileEntity(xCoord, yCoord-1, zCoord+2);
		}
		else tile=null;
		
		if(tile!=null){
			int x;
			int y;
			int z;
			
			if(PallDir[0]!=null){x=xCoord+1; y=yCoord; z=zCoord;}
			else if(PallDir[1]!=null){x=xCoord-1; y=yCoord; z=zCoord;}
			else if(PallDir[2]!=null){x=xCoord; y=yCoord; z=zCoord+1;}
			else if(PallDir[3]!=null){x=xCoord; y=yCoord; z=zCoord-1;}
			else {x=0;y=0;z=0;}
			
			
			if(worldObj.getTileEntity(x, y, z)instanceof TileEntityPow){
				TileEntityPow pipe= (TileEntityPow) worldObj.getTileEntity(x, y, z);
				for(int a=0;a<10;a++)if(tile.getCurrentEnergy()>=tile.getMaxTSpeed()){
					if(pipe.getCurrentEnergy()+tile.getMaxTSpeed()<=pipe.getMaxEnergyBuffer()){
						pipe.addEnergy(tile.getMaxTSpeed());
						tile.subtractEnergy(tile.getMaxTSpeed());
					}
					else if(pipe.getCurrentEnergy()+tile.getMinTSpeed()<=pipe.getMaxEnergyBuffer()){
						pipe.addEnergy(tile.getMinTSpeed());
						tile.subtractEnergy(tile.getMinTSpeed());
					}
				}
				if(pipe.getCurrentEnergy()==2999){
					pipe.addEnergy(1);
					tile.subtractEnergy(1);
				}
			}
		}
	}
	
	
	public void animaion(){
//		animationP2=0;
		float p=1F/16F;
		if(PallDir[0]!=null||PallDir[1]!=null||PallDir[2]!=null||PallDir[3]!=null){
			if(animationP2==0){
			if(animationP1<0)animationP1+=0.01;
			else animationP1=0;
			}
			
			if(animationP1==0){
				if(animationP2<p/2)animationP2+=0.001;
				else animationP2=p/2;
			}
			
		}
		else{
			if(animationP2==0){
				if(animationP1>-p*3+0.01F)animationP1-=0.01;
				else animationP1=-p*3+0.01F;
			}
			
				if(animationP2>0)animationP2-=0.001;
				else animationP2=0;
			
			
		}
	}
	public boolean getTEBFC(int x, int y, int z){
		if(worldObj.getTileEntity(x, y, z)instanceof TileEntityBigFurnaceCore){
			TileEntityBigFurnaceCore tile= (TileEntityBigFurnaceCore)worldObj.getTileEntity(x, y, z);
			return tile.isMultiblockHelper==true;
		}
		else return false;
	}
	public boolean getPipe(int x, int y, int z){
			return worldObj.getTileEntity(x, y, z)instanceof TileEntityPow;
		
	}
}
