package com.magiology.mcobjects.tileentityes;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;

import com.magiology.core.init.MBlocks;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPowGen;
import com.magiology.objhelper.SlowdownHelper;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.PowerHelper;

public class TileEntityBigFurnaceCore extends TileEntityPowGen{
	
	SlowdownHelper optimizer=new SlowdownHelper(40);
	float p=1F/16F;
	int c1;
	public int fuelTicks,maxfuelTicks;
	
	public boolean isMultiblockHelper=false;

	public TileEntityBigFurnaceCore(){
		super(null,null,1, 20, 200,5000,2500);
	}

	@Override
	public void generateFunction(){
		if(getFuel()==1&&canAcceptThatMuchEnergy(25)){
			addEnergy(25);
			subtractFuel(1);
		}
		if(getFuel()<=10&&canAcceptThatMuchEnergy(25*4)){
			addEnergy(25*4);
			subtractFuel(4);
		}
		if(getFuel()<=20&&getFuel()>10&&canAcceptThatMuchEnergy(25*8)){
			addEnergy(25*8);
			subtractFuel(8);
		}
	}
	
	@Override
	public void updateEntity(){
		super.updateEntity();
		this.power();
		if(optimizer.isTimeWithAddProgress()){
			this.isMultiblock();
			if(isMultiblockHelper){
				this.detectAndReplace(xCoord+2,yCoord+1,zCoord  , Blocks.nether_brick, MBlocks.BFCPowerOut);
				this.detectAndReplace(xCoord-2,yCoord+1,zCoord  , Blocks.nether_brick, MBlocks.BFCPowerOut);
				this.detectAndReplace(xCoord  ,yCoord+1,zCoord+2, Blocks.nether_brick, MBlocks.BFCPowerOut);
				this.detectAndReplace(xCoord  ,yCoord+1,zCoord-2, Blocks.nether_brick, MBlocks.BFCPowerOut);
			}
			
		}
		if(worldObj.isRemote){
			if(isMultiblockHelper){if(canGeneratePower(25)){
				worldObj.spawnParticle("smoke", xCoord-1+Helper.RF()*3,yCoord+2+Helper.RF(),zCoord-1.5,0,0,-0.1);
				worldObj.spawnParticle("smoke", xCoord-1+Helper.RF()*3,yCoord+2+Helper.RF(),zCoord+2.5,0,0,0.1);
				worldObj.spawnParticle("smoke", xCoord-1.5,yCoord+2+Helper.RF(),zCoord-1+Helper.RF()*3,-0.1,0,0);
				worldObj.spawnParticle("smoke", xCoord+2.5,yCoord+2+Helper.RF(),zCoord-1+Helper.RF()*3,0.1,0,0);
				worldObj.spawnParticle("smoke", xCoord  ,yCoord-1+Helper.RF(),zCoord+Helper.RF(),0,0,0);
				worldObj.spawnParticle("smoke", xCoord+1,yCoord-1+Helper.RF(),zCoord+Helper.RF(),0,0,0);
				worldObj.spawnParticle("smoke", xCoord+Helper.RF(),yCoord-1+Helper.RF(),zCoord  ,0,0,0);
				worldObj.spawnParticle("smoke", xCoord+Helper.RF(),yCoord-1+Helper.RF(),zCoord+1,0,0,0);
				worldObj.spawnParticle("smoke", xCoord+Helper.RF(),yCoord-1.1,zCoord+Helper.RF(),0,0,0);
				
				String txture=Helper.RInt(5)!=0?"tx1":"tx3";
				int tim=Helper.RInt(40)==0?15:0;
				EntitySmoothBubleFX sb1=new EntitySmoothBubleFX(worldObj, 
						xCoord-0.5+Helper.RF()*2,yCoord+3,zCoord-0.5+Helper.RF()*2,//pos
						0.15-0.3*Helper.RF(),0.4+Helper.CRandF(0.1),0.15-0.3*Helper.RF(),//speed
						1200+Helper.RInt(700),2-(txture.equals("tx3")?1:0)+Helper.RInt(2)+tim,5,true,1,txture, 
								1, txture=="tx3"?1:0.2+Helper.RF()*0.5, txture=="tx3"?1:0.2+Helper.RF()*0.2, 1, 0.99),
								sb2=new EntitySmoothBubleFX(worldObj, 
										xCoord-0.5+Helper.RF()*2,yCoord+3,zCoord-0.5+Helper.RF()*2,0.15-0.3*Helper.RF(),0,0.15-0.3*Helper.RF(),
										1000+Helper.RInt(500),5,-5,1, 
										1, 0.2+Helper.RF()*0.5, 0.2+Helper.RF()*0.2, 1, 0.99);
				Helper.spawnEntityFX(sb1);
				Helper.spawnEntityFX(sb2);
				if(tim==0)sb1.noClip=true;
			}}
			else worldObj.spawnParticle("smoke", xCoord+Helper.RF()  ,yCoord+1,zCoord+Helper.RF(),0,0,0);
		}
		PowerHelper.sortSides(this);
	}
	
	public void power(){
		if(canGeneratePower(25))generateFunction();
	}
	
	
	public void detectAndReplace(int x, int y, int z,Block block, Block replace){
		if(worldObj.getBlock(pos)==block)worldObj.setBlock(pos, replace);
	}
	
	public void isMultiblock(){
		c1=0;
		for(int x1=-1;x1<=1;x1++){for(int y1=1;y1<=2;y1++){for(int z1=-1;z1<=1;z1++){
			if(worldObj.getBlock(xCoord+x1, yCoord+y1, zCoord+z1)==MBlocks.FireLamp)c1+=1;
		}}}
		for(int x1=-1;x1<=1;x1+=2){
		    if(
		       worldObj.getBlock(xCoord+2*x1, yCoord+2, zCoord+1)==Blocks.iron_bars&&
			   worldObj.getBlock(xCoord+2*x1, yCoord+2, zCoord  )==Blocks.iron_bars&&
			   worldObj.getBlock(xCoord+2*x1, yCoord+2, zCoord-1)==Blocks.iron_bars&&
			   
			   worldObj.getBlock(xCoord+1, yCoord+2, zCoord+2*x1)==Blocks.iron_bars&&
			   worldObj.getBlock(xCoord  , yCoord+2, zCoord+2*x1)==Blocks.iron_bars&&
			   worldObj.getBlock(xCoord-1, yCoord+2, zCoord+2*x1)==Blocks.iron_bars&&
			   
			   
			   worldObj.getBlock(xCoord+2*x1, yCoord+4, zCoord+2)==Blocks.nether_brick_stairs&&
			   worldObj.getBlock(xCoord+2*x1, yCoord+4, zCoord+1)==Blocks.nether_brick_stairs&&
			   worldObj.getBlock(xCoord+2*x1, yCoord+4, zCoord  )==Blocks.nether_brick_stairs&&
			   worldObj.getBlock(xCoord+2*x1, yCoord+4, zCoord-1)==Blocks.nether_brick_stairs&&
			   worldObj.getBlock(xCoord+2*x1, yCoord+4, zCoord-2)==Blocks.nether_brick_stairs&&
			   
			   worldObj.getBlock(xCoord+1, yCoord+4, zCoord+2*x1)==Blocks.nether_brick_stairs&&
			   worldObj.getBlock(xCoord  , yCoord+4, zCoord+2*x1)==Blocks.nether_brick_stairs&&
			   worldObj.getBlock(xCoord-1, yCoord+4, zCoord+2*x1)==Blocks.nether_brick_stairs&&
			   
			   
			   worldObj.getBlock(xCoord+1, yCoord  , zCoord+2*x1)==Blocks.obsidian&&
			   worldObj.getBlock(xCoord-1, yCoord  , zCoord+2*x1)==Blocks.obsidian&&
			   worldObj.getBlock(xCoord+1, yCoord+3, zCoord+2*x1)==Blocks.obsidian&&
			   worldObj.getBlock(xCoord-1, yCoord+3, zCoord+2*x1)==Blocks.obsidian&&
			   
			   worldObj.getBlock(xCoord+2*x1, yCoord  , zCoord+1)==Blocks.obsidian&&
			   worldObj.getBlock(xCoord+2*x1, yCoord  , zCoord-1)==Blocks.obsidian&&
			   worldObj.getBlock(xCoord+2*x1, yCoord+3, zCoord+1)==Blocks.obsidian&&
			   worldObj.getBlock(xCoord+2*x1, yCoord+3, zCoord-1)==Blocks.obsidian&&
			   
			   worldObj.getBlock(xCoord+2*x1, yCoord+1, zCoord+1)==Blocks.nether_brick&&
			  (worldObj.getBlock(xCoord+2*x1, yCoord+1, zCoord  )==Blocks.nether_brick||
			   worldObj.getBlock(xCoord+2*x1, yCoord+1, zCoord  )==MBlocks.BFCPowerOut)&&
			   worldObj.getBlock(xCoord+2*x1, yCoord  , zCoord  )==Blocks.nether_brick&&
			   worldObj.getBlock(xCoord+2*x1, yCoord+3, zCoord  )==Blocks.nether_brick&&
			   worldObj.getBlock(xCoord+2*x1, yCoord+1, zCoord-1)==Blocks.nether_brick&&
			   
			   worldObj.getBlock(xCoord+1, yCoord+1, zCoord+2*x1)==Blocks.nether_brick&&
			  (worldObj.getBlock(xCoord  , yCoord+1, zCoord+2*x1)==Blocks.nether_brick||
			   worldObj.getBlock(xCoord  , yCoord+1, zCoord+2*x1)==MBlocks.BFCPowerOut)&&
			   worldObj.getBlock(xCoord  , yCoord  , zCoord+2*x1)==Blocks.nether_brick&&
			   worldObj.getBlock(xCoord  , yCoord+3, zCoord+2*x1)==Blocks.nether_brick&&
			   worldObj.getBlock(xCoord-1, yCoord+1, zCoord+2*x1)==Blocks.nether_brick
			   )c1+=1;
		}
		
			if(c1==20&&
			   worldObj.getBlock(xCoord+2, yCoord  , zCoord+2)==Blocks.iron_block&&
			   worldObj.getBlock(xCoord-2, yCoord  , zCoord+2)==Blocks.iron_block&&
			   worldObj.getBlock(xCoord-2, yCoord  , zCoord-2)==Blocks.iron_block&&
			   worldObj.getBlock(xCoord+2, yCoord  , zCoord-2)==Blocks.iron_block&&
			   
			   worldObj.getBlock(xCoord+2, yCoord+3, zCoord+2)==Blocks.iron_block&&
			   worldObj.getBlock(xCoord-2, yCoord+3, zCoord+2)==Blocks.iron_block&&
			   worldObj.getBlock(xCoord-2, yCoord+3, zCoord-2)==Blocks.iron_block&&
			   worldObj.getBlock(xCoord+2, yCoord+3, zCoord-2)==Blocks.iron_block&&
			   
			   worldObj.getBlock(xCoord-1, yCoord, zCoord-1)==Blocks.stonebrick&&
			   worldObj.getBlock(xCoord  , yCoord, zCoord-1)==Blocks.stonebrick&&
			   worldObj.getBlock(xCoord+1, yCoord, zCoord-1)==Blocks.stonebrick&&
			   worldObj.getBlock(xCoord+1, yCoord, zCoord  )==Blocks.stonebrick&&
			   worldObj.getBlock(xCoord+1, yCoord, zCoord  )==Blocks.stonebrick&&
			   worldObj.getBlock(xCoord+1, yCoord, zCoord+1)==Blocks.stonebrick&&
			   worldObj.getBlock(xCoord  , yCoord, zCoord+1)==Blocks.stonebrick&&
			   worldObj.getBlock(xCoord+1, yCoord, zCoord+1)==Blocks.stonebrick&&
			   
			   worldObj.getBlockMetadata(xCoord+2, yCoord+4, zCoord+1)==1&&
			   worldObj.getBlockMetadata(xCoord+2, yCoord+4, zCoord  )==1&&
			   worldObj.getBlockMetadata(xCoord+2, yCoord+4, zCoord-1)==1&&
			   worldObj.getBlockMetadata(xCoord-2, yCoord+4, zCoord+1)==0&&
			   worldObj.getBlockMetadata(xCoord-2, yCoord+4, zCoord  )==0&&
			   worldObj.getBlockMetadata(xCoord-2, yCoord+4, zCoord-1)==0&&
			   
			   worldObj.getBlockMetadata(xCoord+1, yCoord+4, zCoord+2)==3&&
			   worldObj.getBlockMetadata(xCoord  , yCoord+4, zCoord+2)==3&&
			   worldObj.getBlockMetadata(xCoord-1, yCoord+4, zCoord+2)==3&&
			   worldObj.getBlockMetadata(xCoord+1, yCoord+4, zCoord-2)==2&&
			   worldObj.getBlockMetadata(xCoord  , yCoord+4, zCoord-2)==2&&
			   worldObj.getBlockMetadata(xCoord-1, yCoord+4, zCoord-2)==2&&

			  (worldObj.getBlockMetadata(xCoord+2, yCoord+4, zCoord-2)==1||worldObj.getBlockMetadata(xCoord+2, yCoord+4, zCoord-2)==2)&&
			  (worldObj.getBlockMetadata(xCoord+2, yCoord+4, zCoord+2)==1||worldObj.getBlockMetadata(xCoord+2, yCoord+4, zCoord+2)==3)&&
			  (worldObj.getBlockMetadata(xCoord-2, yCoord+4, zCoord-2)==0||worldObj.getBlockMetadata(xCoord-2, yCoord+4, zCoord-2)==2)&&
			  (worldObj.getBlockMetadata(xCoord-2, yCoord+4, zCoord+2)==0||worldObj.getBlockMetadata(xCoord-2, yCoord+4, zCoord+2)==3)&&
			   
			   worldObj.getTileEntity(xCoord, yCoord-1, zCoord)instanceof TileEntityChest
			   ){
				this.isMultiblockHelper=true;
			}
			else this.isMultiblockHelper=false;
	}

	@Override
	public boolean canGeneratePowerAddon(){
		return isMultiblockHelper;
	}
	
}
