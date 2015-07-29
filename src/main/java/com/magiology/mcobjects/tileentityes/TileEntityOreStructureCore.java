package com.magiology.mcobjects.tileentityes;

import com.magiology.core.init.MBlocks;
import com.magiology.mcobjects.effect.EntityCustomfireFX;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.effect.EntitySparkFX;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.objhelper.SlowdownHelper;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.PowerHelper;
import com.magiology.objhelper.helpers.SideHelper;
import com.magiology.structures.Structure;
import com.magiology.structures.Structures;
import com.magiology.upgrades.RegisterUpgrades.Container;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityOreStructureCore extends TileEntityPow{

	Structure mainMultiBlock,diamondAddon,bedrockAddon,beaconAddon,pipeStructure[]=new Structure[3];
	SlowdownHelper optimizer=new SlowdownHelper(30),optimizer2=new SlowdownHelper(20),optimizer3=new SlowdownHelper(10);
	public int go=0,set=0,yes=10,no=6,processing=0,level=-1;
	public boolean firstTime,validOreInPlace,updateStructureHelper;
	public boolean[] structureUpg=new boolean[4];
	int[] xs={3,-3, 3,-3,-3,-2,-3,-2,3,2,3,2},ys={3, 3, 3, 3, 4, 4, 4, 4,4,4,4,4},zs={3, 3,-3,-3,-2,-3,2,3,-2,-3,2,3};
	Block[] block={
			Blocks.iron_ore,Blocks.tnt, MBlocks.IronLevel2, MBlocks.IronLevel3, MBlocks.IronLevel4, MBlocks.IronLevel5, MBlocks.SuperDuperIron,
			Blocks.gold_ore, MBlocks.GoldLevel2, MBlocks.GoldLevel3, MBlocks.GoldLevel4, MBlocks.GoldLevel5, MBlocks.SuperDuperGold,
			Blocks.coal_ore, MBlocks.CoalLevel2, MBlocks.CoalLevel3, MBlocks.CoalLevel4, MBlocks.CoalLevel5, MBlocks.SuperDuperCoal,
			Blocks.diamond_ore, MBlocks.DiamondLevel2, MBlocks.DiamondLevel3, MBlocks.DiamondLevel4, MBlocks.DiamondLevel5, MBlocks.SuperDuperDiamond,
			Blocks.emerald_ore, MBlocks.EmeraldLevel2, MBlocks.EmeraldLevel3, MBlocks.EmeraldLevel4, MBlocks.EmeraldLevel5, MBlocks.SuperDuperEmerald,
			Blocks.quartz_ore, MBlocks.NetherQuartzLevel2, MBlocks.NetherQuartzLevel3, MBlocks.NetherQuartzLevel4, MBlocks.NetherQuartzLevel5, MBlocks.SuperDuperNetherQuartz,
			Blocks.lapis_ore, MBlocks.LapisLazuliLevel2, MBlocks.LapisLazuliLevel3, MBlocks.LapisLazuliLevel4, MBlocks.LapisLazuliLevel5, MBlocks.SuperDuperLapisLazuli
			};
	
	public TileEntityOreStructureCore(){
		super(null, null, 1, 5, 50, 500000);
		mainMultiBlock=Structures.generateNewStructure(2);
		diamondAddon=Structures.generateNewStructure(3);
		bedrockAddon=Structures.generateNewStructure(4);
		beaconAddon=Structures.generateNewStructure(5);
		setReceaveOnSide(SideHelper.UP(), true);
		setReceaveOnSide(SideHelper.DOWN(), true);
		initUpgrades(Container.Machine);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
	    super.readFromNBT(nbt);
	    updateStructureHelper=nbt.getBoolean("USH");
	    for(int a=0;a<structureUpg.length;a++)structureUpg[a]=nbt.getBoolean("HP"+a);
	    level=nbt.getInteger("LVL");
	}
	@Override
	public void writeToNBT(NBTTagCompound nbt){
	    super.writeToNBT(nbt);
	    nbt.setBoolean("USH", updateStructureHelper);
	    for(int a=0;a<structureUpg.length;a++)nbt.setBoolean("HP"+a, structureUpg[a]);
	    nbt.setInteger("LVL", level);
	}
	
	@Override
	public void updateEntity(){
			super.updateEntity();
			if(mainMultiBlock==null){
				mainMultiBlock=Structures.generateNewStructure(2);
				diamondAddon=Structures.generateNewStructure(3);
				bedrockAddon=Structures.generateNewStructure(4);
				beaconAddon=Structures.generateNewStructure(5);
				pipeStructure=new Structure[3];
				pipeStructure[0]=Structures.generateNewStructure(6);
				pipeStructure[1]=Structures.generateNewStructure(7);
				pipeStructure[2]=Structures.generateNewStructure(8);
				go=5;
				updateStructure();
			}
			
			if(worldObj.getTotalWorldTime()%20==0){
				pipeStructure=new Structure[3];
				pipeStructure[0]=Structures.generateNewStructure(6);
				pipeStructure[1]=Structures.generateNewStructure(7);
				pipeStructure[2]=Structures.generateNewStructure(8);
			}
			
			mainMultiBlock.checkForNextBlock(worldObj, xCoord, yCoord, zCoord);
			if((worldObj.getTotalWorldTime()  )%2==0)beaconAddon.checkForNextBlock(worldObj, xCoord, yCoord, zCoord);
			if((worldObj.getTotalWorldTime()+1)%2==0){
				diamondAddon.checkForNextBlock(worldObj, xCoord, yCoord, zCoord);
				bedrockAddon.checkForNextBlock(worldObj, xCoord, yCoord, zCoord);
			}
			if(worldObj.getTotalWorldTime()%3==0){
				structureUpg[1]=beaconAddon.isStructureCompleate();
				structureUpg[2]=diamondAddon.isStructureCompleate();
				structureUpg[3]=bedrockAddon.isStructureCompleate();
				Structures.updateArray(pipeStructure, worldObj, xCoord, yCoord, zCoord);
				level=0;
				for(boolean bol:structureUpg)if(bol)level++;
			}
			processing();
			if(worldObj.isRemote){
				spawnProcesParticelsHandeler();
				spawnCustomFire();
			}
			
			//This is for slowing down updateEntity to 0.5s for most of things to reduce lag. :)
			   //___________________________________________________________________________________
			{
				if(optimizer.isTimeWithAddProgress()){
				//------------------------------
					if(updateStructureHelper==true){
						if(worldObj.isAirBlock(xCoord+3, yCoord+1, zCoord  ))worldObj.setBlock(xCoord+3, yCoord+1, zCoord,     MBlocks.FireGun);
						if(worldObj.isAirBlock(xCoord-3, yCoord+1, zCoord  ))worldObj.setBlock(xCoord-3, yCoord+1, zCoord,     MBlocks.FireGun);
						if(worldObj.isAirBlock(xCoord,   yCoord+1, zCoord+3))worldObj.setBlock(xCoord,   yCoord+1, zCoord+3,   MBlocks.FireGun);
						if(worldObj.isAirBlock(xCoord,   yCoord+1, zCoord-3))worldObj.setBlock(xCoord,   yCoord+1, zCoord-3,   MBlocks.FireGun);
						if(worldObj.isAirBlock(xCoord,   yCoord+4, zCoord  ))worldObj.setBlock(xCoord,   yCoord+4, zCoord, MBlocks.FireExhaust);
					}
					this.updateStructure();
				}
				//------------------------------
				//___________________________________________________________________________________
				
			}
			PowerHelper.sortSides(this);
		
	}
	
	public void spawnProcesParticelsHandeler(){
		if(updateStructureHelper)this.spawnParticleDa();
		else this.spawnParticleNe();
		if(validOreInPlace==true&&updateStructureHelper==true){
			if(worldObj.isRemote){
				if(processing<16){
					processingParticels();
					for(int a=0;a<xs.length;a++){
						TileEntity tileTest=worldObj.getTileEntity(xs[a]+xCoord, ys[a]+yCoord, zs[a]+zCoord);
						if(tileTest instanceof TileEntityFireLamp){
							TileEntityFireLamp tile=(TileEntityFireLamp)tileTest;
						if(worldObj.getTotalWorldTime()%2==0)tile.spawnPaticle(true);
						}
					}
				}
				if(processing>16&&processing<19)processingParticelsEnding();
				if(processing==19)processingParticelsEnding2();
				
				if(processing>3) spawnBubles(1);
				if(processing>8) spawnBubles(2);
				if(processing>13)spawnBubles(3);
			}
			if(processing==1)firstTime=true;
			if(processing==16){
				if(firstTime==true&&worldObj.isRemote)processingParticelsForGunz();
				firstTime=false;
			}
		}
	}
	
	public void updateStructure(){
		go++;
		if(go>set)go=go-set;
		checkForValidOre();
		if(go==6){
	        if(mainMultiBlock.isStructureCompleate()){
				set=yes;
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);
				updateStructureHelper=true;
				findLevelOfStructure();
				
				for(int a=0;a<xs.length;a++){
					TileEntity tileTest=worldObj.getTileEntity(xs[a]+xCoord, ys[a]+yCoord, zs[a]+zCoord);
					if(tileTest instanceof TileEntityFireLamp){
						TileEntityFireLamp tile=(TileEntityFireLamp)tileTest;
						
						tile.IsControlledByOSC=true;
						tile.controlX=xCoord;
						tile.controlY=yCoord;
						tile.controlZ=zCoord;
					}
				}
				
			}
			else{
				set=no;
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 2, 2);
				worldObj.createExplosion((Entity)null, xCoord+0.5, yCoord+2, zCoord+0.5, 1, false);
				updateStructureHelper=false;
			}
		}
	}
	public void findLevelOfStructure(){
		if(pipeStructure[0]==null)return;
		int lvl=0;
		structureUpg[0]=false;
		
		if(pipeStructure[0].isStructureCompleate()&&Helper.booleanToInt(pipeStructure[1].isStructureCompleate())+Helper.booleanToInt(pipeStructure[2].isStructureCompleate())==1){
			structureUpg[0]=true;
			lvl++;
			
		}
		
		
	}

	public void checkForValidOre(){
		if(Helper.isNull(block,worldObj))return;
		boolean ok=false;
		for(int a=0;a<block.length;a++){
			if(block[a]==worldObj.getBlock(xCoord, yCoord+1, zCoord)){
				a=block.length;
				ok=true;
			}
		}
		if(ok)validOreInPlace=true;
		else validOreInPlace=false;
	}
	
	public void processing(){
		if(updateStructureHelper==true){
			if(optimizer2.isTimeWithAddProgress()&&validOreInPlace&&currentEnergy>100)this.processing++;
			if(validOreInPlace&&this.currentEnergy>100)currentEnergy-=6;
			if(validOreInPlace!=true)processing=0;
			if(processing>20)processing=processing-processing;
			if(processing==20){
				if(worldObj.isRemote){
					Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(xCoord, yCoord+1, zCoord, worldObj.getBlock(xCoord, yCoord+1, zCoord), 0);
					Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5, yCoord+1.5, zCoord+0.5,  0, 0.06, 0,600,99,0.1,true,2,"tx3", 0, 0, 1, 1, 0.99));
					Helper.spawnEntityFX(new EntitySparkFX(worldObj, xCoord+0.5, yCoord+100, zCoord+0.5, 0.5F, 4F, 1, 4, 130, Helper.Vec3(0, -2F, 0)));
				}
				else worldObj.setBlock(xCoord, yCoord+1, zCoord, Blocks.air);
				processing=0;
			}
		}
		else processing=0;
	}
	
	public void processingParticels(){
		if(worldObj.getTotalWorldTime()%4!=0){
			worldObj.spawnParticle("flame", xCoord+Helper.RF(p*7)+p*5, yCoord+5-p*12, zCoord+Helper.RF(p*7)+p*5,  0, -0.05-0.1*Helper.RF(2), 0);
			worldObj.spawnParticle("flame", xCoord+Helper.RF(p*7)+p*5, yCoord+5-p*12, zCoord+Helper.RF(p*7)+p*5,  0, -0.05-0.1*Helper.RF(2), 0);
			worldObj.spawnParticle("smoke", xCoord+Helper.RF(p*7)+p*5, yCoord+5-p*12, zCoord+Helper.RF(p*7)+p*5,  0, -0.05-0.1*Helper.RF(2), 0);
			worldObj.spawnParticle("smoke", xCoord+Helper.RF(p*7)+p*5, yCoord+5-p*12, zCoord+Helper.RF(p*7)+p*5,  -0.05+0.1*Helper.RF(2), -0.05-0.1*Helper.RF(2), 0);
			worldObj.spawnParticle("smoke", xCoord+Helper.RF(p*7)+p*5, yCoord+5-p*12, zCoord+Helper.RF(p*7)+p*5,  -0.05+0.1*Helper.RF(2), -0.05-0.1*Helper.RF(2), 0);
			worldObj.spawnParticle("smoke", xCoord+Helper.RF(p*7)+p*5, yCoord+5-p*12, zCoord+Helper.RF(p*7)+p*5,  -0.05+0.1*Helper.RF(2), -0.05-0.1*Helper.RF(2), 0);
		}
		else{
			Helper.spawnEntityFX(new EntityFlameFX(worldObj, xCoord+Helper.RF(p*7)+p*5, yCoord+5-p*12, zCoord+Helper.RF(p*7)+p*5,  0, -0.05-0.1*Helper.RF(2), 0));
			Helper.spawnEntityFX(new EntityFlameFX(worldObj, xCoord+Helper.RF(p*7)+p*5, yCoord+5-p*12, zCoord+Helper.RF(p*7)+p*5,  0, -0.05-0.1*Helper.RF(2), 0));
		}

	}
	public void processingParticelsForGunz(){
		for(int k=0; k<10; k++){
		Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+2.55, yCoord+1.3, zCoord+0.5, -0.4, 0.025-0.05*Helper.RF(), 0.025-0.05*Helper.RF(),250,3+Helper.RInt(2),0.1,1, 1, 0, 0, 0.7, 0.99));
		worldObj.spawnParticle("smoke", xCoord+2.7, yCoord+1.35, zCoord+0.5, -0.05, 0.025-0.05*Helper.RF(), 0.025-0.05*Helper.RF());
		Helper.spawnEntityFX(new EntitySparkFX(worldObj, xCoord+2.55, yCoord+1.3, zCoord+0.5, 0.5F/16F, 0.1F,1,6,50,Helper.Vec3(-0.05F,0,0)));
		
		Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord-1.7, yCoord+1.35, zCoord+0.5, 0.3, 0.025-0.05*Helper.RF(), 0.025-0.05*Helper.RF(),250,3+Helper.RInt(2),0.1,1, 1, 0, 0, 0.7, 0.99));
		worldObj.spawnParticle("smoke", xCoord-1.7, yCoord+1.35, zCoord+0.5, 0.05, 0.025-0.05*Helper.RF(), 0.025-0.05*Helper.RF());
		Helper.spawnEntityFX(new EntitySparkFX(worldObj, xCoord-1.7, yCoord+1.35, zCoord+0.5, 0.5F/16F, 0.1F,1,6,50, Helper.Vec3(0.05F,0,0)));
		
		Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5, yCoord+1.35, zCoord+2.7, 0.025-0.05*Helper.RF(), 0.025-0.05*Helper.RF(), -0.3,250,3+Helper.RInt(2),0.1,1, 1, 0, 0, 0.7, 0.99));
		worldObj.spawnParticle("smoke", xCoord+0.5, yCoord+1.35, zCoord+2.7, 0.025-0.05*Helper.RF(), 0.025-0.05*Helper.RF(), -0.05);
		Helper.spawnEntityFX(new EntitySparkFX(worldObj, xCoord+0.5, yCoord+1.35, zCoord+2.7, 0.5F/16F, 0.1F,1,6,50, Helper.Vec3(0,0,-0.05F)));
		
		Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5, yCoord+1.35, zCoord-1.7, 0.025-0.05*Helper.RF(), 0.025-0.05*Helper.RF(), 0.3,250,3+Helper.RInt(2),0.1,1, 1, 0, 0, 0.7, 0.99));
		worldObj.spawnParticle("smoke", xCoord+0.5, yCoord+1.35, zCoord-1.7, 0.025-0.05*Helper.RF(), 0.025-0.05*Helper.RF(), 0.05);
		Helper.spawnEntityFX(new EntitySparkFX(worldObj, xCoord+0.5, yCoord+1.35, zCoord-1.7, 0.5F/16F, 0.1F,1,6,50,Helper.Vec3( 0,0, 0.05F)));
		}
	}
	public void processingParticelsEnding(){
		for(int r=0; r<2;r++){
		worldObj.spawnParticle("flame", xCoord+1.08, yCoord+2, zCoord+Helper.RF(), 0, -0.1*Helper.RF(2), 0);
		worldObj.spawnParticle("flame", xCoord+Helper.RF(), yCoord+2, zCoord+1.08, 0, -0.1*Helper.RF(2), 0);
		worldObj.spawnParticle("flame", xCoord+Helper.RF(), yCoord+2, zCoord-0.08, 0, -0.1*Helper.RF(2), 0);
			worldObj.spawnParticle("flame", xCoord-0.08, yCoord+2, zCoord+Helper.RF(), 0, -0.1*Helper.RF(2), 0);
		}
		worldObj.spawnParticle("lava", xCoord+Helper.RF(), yCoord+2, zCoord+Helper.RF(), 0, 0, 0);
		
		for(int f=0; f<10;f++)worldObj.spawnParticle("smoke", xCoord-2.5+Helper.RF(5), yCoord+1.01, zCoord-2.5+Helper.RF(5), 0, 0.1*Helper.RF(2), 0);
		
	}
	public void processingParticelsEnding2(){
		for(int l=0; l<3;l++){
			worldObj.spawnParticle("cloud", xCoord+1.08,                          yCoord+1+Helper.RF(), zCoord+Helper.RF(), 0, 0, 0);
			worldObj.spawnParticle("cloud", xCoord+Helper.RF(), yCoord+1+Helper.RF(), zCoord+1.08,                          0, 0, 0);
			worldObj.spawnParticle("cloud", xCoord+Helper.RF(), yCoord+1+Helper.RF(), zCoord-0.08,                          0, 0, 0);
			worldObj.spawnParticle("cloud", xCoord-0.08,                          yCoord+1+Helper.RF(), zCoord+Helper.RF(), 0, 0, 0);
			worldObj.spawnParticle("cloud", xCoord+Helper.RF(), yCoord+2,                               zCoord+Helper.RF(), 0, 0, 0);
		}
		
		for(int l=0; l<4;l++)worldObj.spawnParticle("lava", xCoord-2.5+Helper.RF(5), yCoord+1.21, zCoord-2.5+Helper.RF(5), 0, 0.1*Helper.RF(2), 0);
	
		worldObj.spawnParticle("largesmoke", xCoord+1.58,                          yCoord+1+Helper.RF(), zCoord+Helper.RF(2), 0, 0, 0);
		worldObj.spawnParticle("largesmoke", xCoord+Helper.RF(2), yCoord+1+Helper.RF(), zCoord+1.58,                          0, 0, 0);
		worldObj.spawnParticle("largesmoke", xCoord+Helper.RF(2), yCoord+1+Helper.RF(), zCoord-0.58,                          0, 0, 0);
		worldObj.spawnParticle("largesmoke", xCoord-0.58,                          yCoord+1+Helper.RF(), zCoord+Helper.RF(2), 0, 0, 0);
		worldObj.spawnParticle("largesmoke", xCoord+Helper.RF(2), yCoord+2.5,                             zCoord+Helper.RF(2), 0, 0, 0);
		Helper.spawnEntityFX(new EntitySparkFX(worldObj, xCoord+Helper.RF()*5-2, yCoord+1, zCoord+Helper.RF()*5-2, 0.5F/16F, 0.1F,1,4,40, Helper.Vec3(0,0.03F,0)));
	}
	public void spawnCustomFire(){
		if(updateStructureHelper==true){
			if(worldObj.isRemote){
				if(!structureUpg[0]){
					if(optimizer3.progress==1+Helper.RInt(1)){
						if(Helper.RB())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, xCoord+3.5+Helper.RF()*p, yCoord+4-p*5, zCoord+3.5+Helper.RF()*p, -0.05, 0.3, -0.05,false,1));
						if(Helper.RB())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, xCoord-2.5+Helper.RF()*p, yCoord+4-p*5, zCoord+3.5+Helper.RF()*p,  0.05, 0.3, -0.05,false,1));
						if(Helper.RB())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, xCoord+3.5+Helper.RF()*p, yCoord+4-p*5, zCoord-2.5+Helper.RF()*p, -0.05, 0.3,  0.05,false,1));
						if(Helper.RB())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, xCoord-2.5+Helper.RF()*p, yCoord+4-p*5, zCoord-2.5+Helper.RF()*p,  0.05, 0.3,  0.05,false,1));
						
						if(Helper.RB())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, xCoord+2.5+Helper.RF()*p, yCoord+5-p*5, zCoord+3.5+Helper.RF()*p, -0.05, 0.2, -0.072,false,1));
						if(Helper.RB())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, xCoord-1.5+Helper.RF()*p, yCoord+5-p*5, zCoord+3.5+Helper.RF()*p,  0.05, 0.2, -0.072,false,1));
						if(Helper.RB())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, xCoord+2.5+Helper.RF()*p, yCoord+5-p*5, zCoord-2.5+Helper.RF()*p, -0.05, 0.2,  0.072,false,1));
						if(Helper.RB())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, xCoord-1.5+Helper.RF()*p, yCoord+5-p*5, zCoord-2.5+Helper.RF()*p,  0.05, 0.2,  0.072,false,1));
						
						if(Helper.RB())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, xCoord+3.5+Helper.RF()*p, yCoord+5-p*5, zCoord+2.5+Helper.RF()*p, -0.072, 0.2, -0.05,false,1));
						if(Helper.RB())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, xCoord-2.5+Helper.RF()*p, yCoord+5-p*5, zCoord+2.5+Helper.RF()*p,  0.072, 0.2, -0.05,false,1));
						if(Helper.RB())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, xCoord+3.5+Helper.RF()*p, yCoord+5-p*5, zCoord-1.5+Helper.RF()*p, -0.072, 0.2,  0.05,false,1));
						if(Helper.RB())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, xCoord-2.5+Helper.RF()*p, yCoord+5-p*5, zCoord-1.5+Helper.RF()*p,  0.072, 0.2,  0.05,false,1));
					}
				}
				else if(optimizer3.progress==1&&Helper.RInt(3)==0)Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, xCoord+0.5+Helper.RF()*p, yCoord+6-p*6, zCoord+0.5+Helper.RF()*p, 0.5-Helper.RF(), 0.2+0.3*Helper.RF(), 0.5-Helper.RF(),true,1));
			}
		}
	}
	public void spawnBubles(int number){
		for(int pm=-1;pm<=1;pm+=2){
			if(number==1){
				Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5-4*pm, yCoord+1.2, zCoord+0.5-2,  0, 0, 0,150,3+Helper.RInt(2),1,true,1,"tx1", 0, 0.25, 0.8, 0.5, 0.99));
				Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5-4*pm, yCoord+1.2, zCoord+0.5+2,  0, 0, 0,150,3+Helper.RInt(2),1,true,1,"tx1", 0, 0.25, 0.8, 0.5, 0.99));
				Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5+2*pm, yCoord+1.2, zCoord+0.5-4,  0, 0, 0,150,3+Helper.RInt(2),1,true,1,"tx1", 0, 0.25, 0.8, 0.5, 0.99));
				Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5+2*pm, yCoord+1.2, zCoord+0.5+4,  0, 0, 0,150,3+Helper.RInt(2),1,true,1,"tx1", 0, 0.25, 0.8, 0.5, 0.99));
			}
			if(number==2){
				Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5-5*pm, yCoord+1.2, zCoord+0.5-1,  0, 0, 0,150,3+Helper.RInt(2),1,true,1,"tx1", 0.5, 0, 1, 0.5, 0.99));
				Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5-5*pm, yCoord+1.2, zCoord+0.5+1,  0, 0, 0,150,3+Helper.RInt(2),1,true,1,"tx1", 0.5, 0, 1, 0.5, 0.99));
				Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5+1*pm, yCoord+1.2, zCoord+0.5-5,  0, 0, 0,150,3+Helper.RInt(2),1,true,1,"tx1", 0.5, 0, 1, 0.5, 0.99));
				Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5+1*pm, yCoord+1.2, zCoord+0.5+5,  0, 0, 0,150,3+Helper.RInt(2),1,true,1,"tx1", 0.5, 0, 1, 0.5, 0.99));
			}
			if(number==3){
				Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5+5*pm, yCoord+1.2, zCoord+0.5,  0, 0, 0,150,3+Helper.RInt(2),1,true,1,"tx1", 1, 0, 0, 0.5, 0.99));
				Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj,xCoord+0.5, yCoord+1.2, zCoord+0.5+5*pm,  0, 0, 0,150,3+Helper.RInt(2),1,true,1,"tx1", 1, 0, 0, 0.5, 0.99));
			}
		}
	}
	
	public void spawnParticleDa(){
		if(optimizer3.isTimeWithAddProgress()){
			EntitySmoothBubleFX a=new EntitySmoothBubleFX(worldObj,0.5+-15+Helper.RInt(30)+xCoord, 1.5+Helper.RInt(10)+yCoord, 0.5+-15+Helper.RInt(30)+zCoord,
					0.04*(Helper.RB()?1:-1), 0.04*(Helper.RB()?1:-1), 0.04*(Helper.RB()?1:-1),
					300+Helper.RInt(300),0,0,true,2,"tx1",Helper.RF(),Helper.RF(),Helper.RF(),1, 0.99);
			Helper.spawnEntityFX(a);
			a.noClip=true;
		}
		
		for (int la = 0; la < 4; ++la){
			double v0=Helper.CRandD(3);
			double v2=Helper.CRandD(3);
			double v1=Helper.CRandD(1);
	        if(Helper.RB(5))worldObj.spawnParticle("smoke", xCoord+0.5F+v0, yCoord + 1.1F, zCoord+0.5F+v2, -v0/18, v1/2, -v2/18);
	        else Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+0.5F+v0, yCoord + 1.1F, zCoord+0.5F+v2, -v0/18, v1/2, -v2/18));
	        }
		 	if(this.optimizer2.progress==1){
	        worldObj.spawnParticle("lava", xCoord-7+Helper.RF(), yCoord+1, zCoord-3+Helper.RF(), 0, 0, 0);
	        worldObj.spawnParticle("lava", xCoord-7+Helper.RF(), yCoord+1, zCoord+3+Helper.RF(), 0, 0, 0);
	        worldObj.spawnParticle("lava", xCoord+7+Helper.RF(), yCoord+1, zCoord-3+Helper.RF(), 0, 0, 0);
	        worldObj.spawnParticle("lava", xCoord+7+Helper.RF(), yCoord+1, zCoord+3+Helper.RF(), 0, 0, 0);
	        worldObj.spawnParticle("lava", xCoord-3+Helper.RF(), yCoord+1, zCoord-7+Helper.RF(), 0, 0, 0);
	        worldObj.spawnParticle("lava", xCoord+ 3F + Helper.RF(), yCoord + 1, zCoord- 7 + Helper.RF(), 0, 0, 0);
	        worldObj.spawnParticle("lava", xCoord-3+Helper.RF(), yCoord+1, zCoord+7 + Helper.RF(), 0, 0, 0);
	        worldObj.spawnParticle("lava", xCoord+3+Helper.RF(), yCoord+1, zCoord+7+Helper.RF(), 0, 0, 0);
			}
		}
	public void spawnParticleNe(){
		for (int la = 0; la < 1; ++la)worldObj.spawnParticle("lava", xCoord+Helper.RF(), yCoord+1,zCoord+Helper.RF(), 0,0,0);
	}


}
