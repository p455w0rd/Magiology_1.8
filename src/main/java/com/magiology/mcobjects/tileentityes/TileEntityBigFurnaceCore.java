package com.magiology.mcobjects.tileentityes;

import com.magiology.core.init.MBlocks;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPowGen;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.SlowdownUtil;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;

public class TileEntityBigFurnaceCore extends TileEntityPowGen{
	
	SlowdownUtil optimizer=new SlowdownUtil(40);
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
	public void update(){
		this.power();
		if(optimizer.isTimeWithAddProgress()){
			this.isMultiblock();
			if(isMultiblockHelper){
				this.detectAndReplace(pos.add(2,1,0), Blocks.nether_brick, MBlocks.bigFurnacePowerOut);
				this.detectAndReplace(pos.add(-2,1,0), Blocks.nether_brick, MBlocks.bigFurnacePowerOut);
				this.detectAndReplace(pos.add(0,1,2), Blocks.nether_brick, MBlocks.bigFurnacePowerOut);
				this.detectAndReplace(pos.add(0,1,-2), Blocks.nether_brick, MBlocks.bigFurnacePowerOut);
			}
			
		}
		if(worldObj.isRemote){
			if(isMultiblockHelper){if(canGeneratePower(25)){
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x()-1+RandUtil.RF()*3,y()+2+RandUtil.RF(),z()-1.5,0,0,-0.1);
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x()-1+RandUtil.RF()*3,y()+2+RandUtil.RF(),z()+2.5,0,0,0.1);
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x()-1.5,y()+2+RandUtil.RF(),z()-1+RandUtil.RF()*3,-0.1,0,0);
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x()+2.5,y()+2+RandUtil.RF(),z()-1+RandUtil.RF()*3,0.1,0,0);
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x()  ,y()-1+RandUtil.RF(),z()+RandUtil.RF(),0,0,0);
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x()+1,y()-1+RandUtil.RF(),z()+RandUtil.RF(),0,0,0);
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x()+RandUtil.RF(),y()-1+RandUtil.RF(),z()  ,0,0,0);
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x()+RandUtil.RF(),y()-1+RandUtil.RF(),z()+1,0,0,0);
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x()+RandUtil.RF(),y()-1.1,z()+RandUtil.RF(),0,0,0);
				
				String txture=RandUtil.RI(5)!=0?"tx1":"tx3";
				int tim=RandUtil.RI(40)==0?15:0;
				EntitySmoothBubleFX sb1=new EntitySmoothBubleFX(worldObj, 
						x()-0.5+RandUtil.RF()*2,y()+3,z()-0.5+RandUtil.RF()*2,//pos
						0.15-0.3*RandUtil.RF(),0.4+RandUtil.CRF(0.1),0.15-0.3*RandUtil.RF(),//speed
						1200+RandUtil.RI(700),2-(txture.equals("tx3")?1:0)+RandUtil.RI(2)+tim,5,true,1,txture, 
								1, txture=="tx3"?1:0.2+RandUtil.RF()*0.5, txture=="tx3"?1:0.2+RandUtil.RF()*0.2, 1, 0.99),
								sb2=new EntitySmoothBubleFX(worldObj, 
										x()-0.5+RandUtil.RF()*2,y()+3,z()-0.5+RandUtil.RF()*2,0.15-0.3*RandUtil.RF(),0,0.15-0.3*RandUtil.RF(),
										1000+RandUtil.RI(500),5,-5,1, 
										1, 0.2+RandUtil.RF()*0.5, 0.2+RandUtil.RF()*0.2, 1, 0.99);
				UtilM.spawnEntityFX(sb1);
				UtilM.spawnEntityFX(sb2);
				if(tim==0)sb1.noClip=true;
			}}
			else worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x()+RandUtil.RF()  ,y()+1,z()+RandUtil.RF(),0,0,0);
		}
		PowerUtil.sortSides(this);
	}
	
	public void power(){
		if(canGeneratePower(25))generateFunction();
	}
	
	
	public void detectAndReplace(BlockPos pos,Block block, Block replace){
		if(U.getBlock(worldObj, pos)==block)U.setBlock(worldObj, pos, replace);
	}
	
	public void isMultiblock(){
		c1=0;
		for(int x1=-1;x1<=1;x1++){for(int y1=1;y1<=2;y1++){for(int z1=-1;z1<=1;z1++){
			if(U.getBlock(worldObj,pos.add(x1,y1,z1))==MBlocks.fireLamp)c1+=1;
		}}}
		for(int x1=-1;x1<=1;x1+=2){
			if(
			   U.getBlock(worldObj,pos.add(2*x1,2,1))==Blocks.iron_bars&&
			   U.getBlock(worldObj,pos.add(2*x1, 2, 0  ))==Blocks.iron_bars&&
			   U.getBlock(worldObj,pos.add(2*x1, 2, -1))==Blocks.iron_bars&&
			   
			   U.getBlock(worldObj,pos.add(1, 2, 2*x1))==Blocks.iron_bars&&
			   U.getBlock(worldObj,pos.add(0  , 2, 2*x1))==Blocks.iron_bars&&
			   U.getBlock(worldObj,pos.add(-1, 2, 2*x1))==Blocks.iron_bars&&
			   
			   
			   U.getBlock(worldObj,pos.add(2*x1, 4, 2))==Blocks.nether_brick_stairs&&
			   U.getBlock(worldObj,pos.add(2*x1, 4, 1))==Blocks.nether_brick_stairs&&
			   U.getBlock(worldObj,pos.add(2*x1, 4, 0  ))==Blocks.nether_brick_stairs&&
			   U.getBlock(worldObj,pos.add(2*x1, 4, -1))==Blocks.nether_brick_stairs&&
			   U.getBlock(worldObj,pos.add(2*x1, 4, -2))==Blocks.nether_brick_stairs&&
			   
			   U.getBlock(worldObj,pos.add(1, 4, 2*x1))==Blocks.nether_brick_stairs&&
			   U.getBlock(worldObj,pos.add(0  , 4, 2*x1))==Blocks.nether_brick_stairs&&
			   U.getBlock(worldObj,pos.add(-1, 4, 2*x1))==Blocks.nether_brick_stairs&&
			   
			   
			   U.getBlock(worldObj,pos.add(1, 0  , 2*x1))==Blocks.obsidian&&
			   U.getBlock(worldObj,pos.add(-1, 0  , 2*x1))==Blocks.obsidian&&
			   U.getBlock(worldObj,pos.add(1, 3, 2*x1))==Blocks.obsidian&&
			   U.getBlock(worldObj,pos.add(-1, 3, 2*x1))==Blocks.obsidian&&
			   
			   U.getBlock(worldObj,pos.add(2*x1, 0  , 1))==Blocks.obsidian&&
			   U.getBlock(worldObj,pos.add(2*x1, 0  , -1))==Blocks.obsidian&&
			   U.getBlock(worldObj,pos.add(2*x1, 3, 1))==Blocks.obsidian&&
			   U.getBlock(worldObj,pos.add(2*x1, 3, -1))==Blocks.obsidian&&
			   
			   U.getBlock(worldObj,pos.add(2*x1, 1, 1))==Blocks.nether_brick&&
			  (U.getBlock(worldObj,pos.add(2*x1, 1, 0  ))==Blocks.nether_brick||
			   U.getBlock(worldObj,pos.add(2*x1, 1, 0  ))==MBlocks.bigFurnacePowerOut)&&
			   U.getBlock(worldObj,pos.add(2*x1, 0  , 0  ))==Blocks.nether_brick&&
			   U.getBlock(worldObj,pos.add(2*x1, 3, 0  ))==Blocks.nether_brick&&
			   U.getBlock(worldObj,pos.add(2*x1, 1, -1))==Blocks.nether_brick&&
			   
			   U.getBlock(worldObj,pos.add(1, 1, 2*x1))==Blocks.nether_brick&&
			  (U.getBlock(worldObj,pos.add(0  , 1, 2*x1))==Blocks.nether_brick||
			   U.getBlock(worldObj,pos.add(0  , 1, 2*x1))==MBlocks.bigFurnacePowerOut)&&
			   U.getBlock(worldObj,pos.add(0  , 0  , 2*x1))==Blocks.nether_brick&&
			   U.getBlock(worldObj,pos.add(0  , 3, 2*x1))==Blocks.nether_brick&&
			   U.getBlock(worldObj,pos.add(-1, 1, 2*x1))==Blocks.nether_brick
			   )c1+=1;
		}
		
			if(c1==20&&
			   U.getBlock(worldObj,pos.add(2, 0  , 2))==Blocks.iron_block&&
			   U.getBlock(worldObj,pos.add(-2, 0  , 2))==Blocks.iron_block&&
			   U.getBlock(worldObj,pos.add(-2, 0  , -2))==Blocks.iron_block&&
			   U.getBlock(worldObj,pos.add(2, 0  , -2))==Blocks.iron_block&&
			   
			   U.getBlock(worldObj,pos.add(2, 3, 2))==Blocks.iron_block&&
			   U.getBlock(worldObj,pos.add(-2, 3, 2))==Blocks.iron_block&&
			   U.getBlock(worldObj,pos.add(-2, 3, -2))==Blocks.iron_block&&
			   U.getBlock(worldObj,pos.add(2, 3, -2))==Blocks.iron_block&&
			   
			   U.getBlock(worldObj,pos.add(-1, 0, -1))==Blocks.stonebrick&&
			   U.getBlock(worldObj,pos.add(0  , 0, -1))==Blocks.stonebrick&&
			   U.getBlock(worldObj,pos.add(1, 0, -1))==Blocks.stonebrick&&
			   U.getBlock(worldObj,pos.add(1, 0, 0  ))==Blocks.stonebrick&&
			   U.getBlock(worldObj,pos.add(1, 0, 0  ))==Blocks.stonebrick&&
			   U.getBlock(worldObj,pos.add(1, 0, 1))==Blocks.stonebrick&&
			   U.getBlock(worldObj,pos.add(0  , 0, 1))==Blocks.stonebrick&&
			   U.getBlock(worldObj,pos.add(1, 0, 1))==Blocks.stonebrick&&
			   
			   U.getBlockMetadata(worldObj,pos.add(2, 4, 1))==1&&
			   U.getBlockMetadata(worldObj,pos.add(2, 4, 0  ))==1&&
			   U.getBlockMetadata(worldObj,pos.add(2, 4, -1))==1&&
			   U.getBlockMetadata(worldObj,pos.add(-2, 4, 1))==0&&
			   U.getBlockMetadata(worldObj,pos.add(-2, 4, 0  ))==0&&
			   U.getBlockMetadata(worldObj,pos.add(-2, 4, -1))==0&&
			   
			   U.getBlockMetadata(worldObj,pos.add(1, 4, 2))==3&&
			   U.getBlockMetadata(worldObj,pos.add(0  , 4, 2))==3&&
			   U.getBlockMetadata(worldObj,pos.add(-1, 4, 2))==3&&
			   U.getBlockMetadata(worldObj,pos.add(1, 4, -2))==2&&
			   U.getBlockMetadata(worldObj,pos.add(0  , 4, -2))==2&&
			   U.getBlockMetadata(worldObj,pos.add(-1, 4, -2))==2&&

			  (U.getBlockMetadata(worldObj,pos.add(2, 4, -2))==1||U.getBlockMetadata(worldObj,pos.add(2, 4, -2))==2)&&
			  (U.getBlockMetadata(worldObj,pos.add(2, 4, 2))==1||U.getBlockMetadata(worldObj,pos.add(2, 4, 2))==3)&&
			  (U.getBlockMetadata(worldObj,pos.add(-2, 4, -2))==0||U.getBlockMetadata(worldObj,pos.add(-2, 4, -2))==2)&&
			  (U.getBlockMetadata(worldObj,pos.add(-2, 4, 2))==0||U.getBlockMetadata(worldObj,pos.add(-2, 4, 2))==3)
			   ){
				this.isMultiblockHelper=true;
			}
			else this.isMultiblockHelper=false;
	}

	@Override
	public boolean canGeneratePowerAddon(){
		return isMultiblockHelper;
	}

	@Override
	public void updateConnections(){
		UpdateablePipeHandler.onConnectionUpdate(this);
		
	}
	
}
