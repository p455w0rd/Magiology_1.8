package com.magiology.mcobjects.tileentityes;

import net.minecraft.init.Blocks;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityM;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.objhelper.helpers.Helper.H;
import com.magiology.objhelper.vectors.Pos;

public class TileEntityBFCPowerOut extends TileEntityM implements IUpdatePlayerListBox{
	
	public EnumFacing[] CallDir = new EnumFacing[4];
	public EnumFacing[] PallDir = new EnumFacing[4];
	int optimizer1=0;
	int optimizer2=0;
	int optimizer3=0;
	public float animationP1=0;
	public float animationP2=0;
	
	
	@Override
	public void update(){
		if(optimizer1++>=60){
			optimizer1=0;
			if(getTEBFC(pos.add(-2, -1, 0)))CallDir[0]=EnumFacing.EAST;
			else CallDir[0]=null;
			
			if(getTEBFC(pos.add(2, -1, 0)))CallDir[1]=EnumFacing.WEST;
			else CallDir[1]=null;
			
			if(getTEBFC(pos.add(0, -1, -2)))CallDir[2]=EnumFacing.NORTH;
			else CallDir[2]=null;
			
			if(getTEBFC(pos.add(0, -1, 2)))CallDir[3]=EnumFacing.SOUTH;
			else CallDir[3]=null;
			
			if(optimizer3++>=2){
				optimizer3=0;
				
				if(CallDir[0]==null&&CallDir[1]==null&&CallDir[2]==null&&CallDir[3]==null){
					H.setBlock(worldObj,pos, Blocks.nether_brick);
				}
			}
		}
		if(optimizer2++>=10){
			optimizer2=0;
			if(CallDir[0]!=null&&getPipe(pos.add(1, 0, 0)))PallDir[0]=EnumFacing.EAST;
			else PallDir[0]=null;
			
			if(CallDir[1]!=null&&getPipe(pos.add(-1, 0, 0)))PallDir[1]=EnumFacing.WEST;
			else PallDir[1]=null;
			
			if(CallDir[2]!=null&&getPipe(pos.add(0,0,1)))PallDir[2]=EnumFacing.NORTH;
			else PallDir[2]=null;
			
			if(CallDir[3]!=null&&getPipe(pos.add(0,0,-1)))PallDir[3]=EnumFacing.SOUTH;
			else PallDir[3]=null;
		}
		
		sendEnergy();
		
		this.animaion();
	}
	
	
	public void sendEnergy(){
		TileEntityBigFurnaceCore tile;
		if(PallDir[0]!=null){
			tile=(TileEntityBigFurnaceCore)worldObj.getTileEntity(pos.add(-2, -1, 0));
		}
		else if(PallDir[1]!=null){
			tile=(TileEntityBigFurnaceCore)worldObj.getTileEntity(pos.add(2, -1, 0));
		}
		else if(PallDir[2]!=null){
			tile=(TileEntityBigFurnaceCore)worldObj.getTileEntity(pos.add(0, -1, -2));
		}
		else if(PallDir[3]!=null){
			tile=(TileEntityBigFurnaceCore)worldObj.getTileEntity(pos.add(0, -1, 2));
		}
		else tile=null;
		
		if(tile!=null){
			BlockPos pos=new Pos();
			
			if(PallDir[0]!=null)pos=new Pos(1, 0, 0);
			else if(PallDir[1]!=null)pos=new Pos(1, 0, 0);
			else if(PallDir[2]!=null)pos=new Pos(0, 0, 1);
			else if(PallDir[3]!=null)pos=new Pos(0, 0, -1);
			
			
			if(worldObj.getTileEntity(pos)instanceof TileEntityPow){
				TileEntityPow pipe= (TileEntityPow) worldObj.getTileEntity(pos);
				for(int a=0;a<10;a++)if(tile.getEnergy()>=tile.getMaxTSpeed()){
					if(pipe.getEnergy()+tile.getMaxTSpeed()<=pipe.getMaxEnergyBuffer()){
						pipe.addEnergy(tile.getMaxTSpeed());
						tile.subtractEnergy(tile.getMaxTSpeed());
					}
					else if(pipe.getEnergy()+tile.getMinTSpeed()<=pipe.getMaxEnergyBuffer()){
						pipe.addEnergy(tile.getMinTSpeed());
						tile.subtractEnergy(tile.getMinTSpeed());
					}
				}
				if(pipe.getEnergy()==2999){
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
	public boolean getTEBFC(BlockPos pos){
		if(worldObj.getTileEntity(pos)instanceof TileEntityBigFurnaceCore){
			TileEntityBigFurnaceCore tile= (TileEntityBigFurnaceCore)worldObj.getTileEntity(pos);
			return tile.isMultiblockHelper==true;
		}
		else return false;
	}
	public boolean getPipe(BlockPos pos){
			return worldObj.getTileEntity(pos)instanceof TileEntityPow;
		
	}
}
