package com.magiology.mcobjects.tileentityes;

import com.magiology.core.init.MBlocks;
import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityM;
import com.magiology.objhelper.SlowdownHelper;

public class TileEntityBigChunksOOre extends TileEntityM{
	
	
	SlowdownHelper optimizer=new SlowdownHelper(10);
	public float rotation=0;
	public float animP1=0;
	public int BlockType=0;
	int state=0;
	
	
	public TileEntityBigChunksOOre(){}
	
	@Override
	public void updateEntity(){
		
		if(BlockType==2){
		if(state==0)animP1+=10;
		else if(state==1)animP1-=2;
		
		if(animP1<1)state=3;
		else if(animP1>600)state=1;
		}
		else{
			rotation=0;
			animP1=0;
			state=0;
		}
		
		if(optimizer.isTimeWithAddProgress()){
			if(worldObj.getBlock(xCoord, yCoord+1, zCoord)==MBlocks.BigChunksOOre&&
				worldObj.getBlock(xCoord, yCoord-1, zCoord)==MBlocks.BigChunksOOre&&
				worldObj.getBlock(xCoord, yCoord+2, zCoord)!=MBlocks.BigChunksOOre&&
				worldObj.getBlock(xCoord, yCoord-2, zCoord)!=MBlocks.BigChunksOOre)BlockType=2;
			
			else if(worldObj.getBlock(xCoord, yCoord-1, zCoord)!=MBlocks.BigChunksOOre&&
				worldObj.getBlock(xCoord, yCoord+1, zCoord)==MBlocks.BigChunksOOre&&
				worldObj.getBlock(xCoord, yCoord+2, zCoord)==MBlocks.BigChunksOOre&&
				worldObj.getBlock(xCoord, yCoord+3, zCoord)!=MBlocks.BigChunksOOre)BlockType=3;
			
			else if(worldObj.getBlock(xCoord, yCoord+1, zCoord)!=MBlocks.BigChunksOOre&&
				worldObj.getBlock(xCoord, yCoord-1, zCoord)==MBlocks.BigChunksOOre&&
				worldObj.getBlock(xCoord, yCoord-2, zCoord)==MBlocks.BigChunksOOre&&
				worldObj.getBlock(xCoord, yCoord-3, zCoord)!=MBlocks.BigChunksOOre)BlockType=1;
			else BlockType=-1;
		}
		
//		stzate=1;
		
		
		
	}
	
}
