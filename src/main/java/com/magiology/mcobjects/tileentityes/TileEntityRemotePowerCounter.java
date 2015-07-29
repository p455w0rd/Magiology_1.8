package com.magiology.mcobjects.tileentityes;

import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityM;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRemotePowerCounter extends TileEntityM{
	
	public Block block;
	public TileEntity tile1;
	public double powerBar;
	public int maxPB,currentP,x, y, z;
	
	@Override
	public void updateEntity(){
		boolean okBlock=true;
		int metadata=worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		x=xCoord; y=yCoord; z=zCoord;
		
		switch (metadata) {
		case 0:{
			y++;
		}break;
		case 1:{
			y--;
		}break;
		case 2:{
			z++;
		}break;
		case 3:{
			z--;
		}break;
		case 4:{
			x++;
		}break;
		case 5:{
			x--;
		}break;
		default:
			break;
		}

		block=worldObj.getBlock(x, y, z);
		tile1=worldObj.getTileEntity(x, y, z);
		
		
		{
			if(tile1 instanceof TileEntityPow){
				powerBar=(float)((TileEntityPow)tile1).currentEnergy/(float)((TileEntityPow)tile1).maxEnergyBuffer;
				currentP=((TileEntityPow)tile1).currentEnergy;
				maxPB=((TileEntityPow)tile1).maxEnergyBuffer;
			}else if(tile1 instanceof TileEntityBigFurnaceCore){
				powerBar=(float)((TileEntityBigFurnaceCore)tile1).currentEnergy/(float)((TileEntityBigFurnaceCore)tile1).maxEnergyBuffer;
				
				currentP=((TileEntityBigFurnaceCore)tile1).currentEnergy;
				maxPB=((TileEntityBigFurnaceCore)tile1).maxEnergyBuffer;
			}else if(tile1 instanceof TileEntityBFCPowerOut){
				int x1=0;int y1=1000;int z1=0;
					
				     if(((TileEntityBFCPowerOut)tile1).CallDir[0]!=null){x1=x-2;y1=y-1;z1=z;}
				else if(((TileEntityBFCPowerOut)tile1).CallDir[1]!=null){x1=x+2;y1=y-1;z1=z;}
				else if(((TileEntityBFCPowerOut)tile1).CallDir[2]!=null){x1=x;y1=y-1;z1=z-2;}
				else if(((TileEntityBFCPowerOut)tile1).CallDir[3]!=null){x1=x;y1=y-1;z1=z+2;}
				
				if(y1!=1000){
					x=x1;y=y1;z=z1;
					tile1= worldObj.getTileEntity(x1, y1, z1);
					powerBar=(float)((TileEntityBigFurnaceCore)tile1).currentEnergy/(float)((TileEntityBigFurnaceCore)tile1).maxEnergyBuffer;
					currentP=((TileEntityBigFurnaceCore)tile1).currentEnergy;
					maxPB=((TileEntityBigFurnaceCore)tile1).maxEnergyBuffer;
				}
			}else if(tile1 instanceof TileEntityBateryGeneric){
				powerBar=(float)((TileEntityBateryGeneric)tile1).currentEnergy/(float)((TileEntityBateryGeneric)tile1).maxEnergyBuffer;
				currentP=((TileEntityBateryGeneric)tile1).currentEnergy;
				maxPB=((TileEntityBateryGeneric)tile1).maxEnergyBuffer;
			}else if(tile1 instanceof TileEntityFireLamp){
				powerBar=(float)((TileEntityFireLamp)tile1).currentEnergy/(float)((TileEntityFireLamp)tile1).maxEnergyBuffer;
				currentP=((TileEntityFireLamp)tile1).currentEnergy;
				maxPB=((TileEntityFireLamp)tile1).maxEnergyBuffer;
			}else if(tile1 instanceof TileEntityRemotePowerCounter){
				powerBar=(float)((TileEntityRemotePowerCounter)tile1).powerBar;
				currentP=((TileEntityRemotePowerCounter)tile1).currentP;
				maxPB=((TileEntityRemotePowerCounter)tile1).maxPB;
			}
			else okBlock=false;
			
			if(okBlock==false){
				currentP=0;
				maxPB=0;
			}
			
			if(powerBar>0)powerBar+=-0.01;
			
		}
		
	}
	
	
}
