package com.magiology.mcobjects.tileentityes;

import static com.magiology.api.power.SixSidedBoolean.Modifier.*;
import static com.magiology.objhelper.helpers.SideHelper.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import com.magiology.api.power.SixSidedBoolean;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.effect.EntitySparkFX;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPowGen;
import com.magiology.objhelper.SlowdownHelper;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.PowerHelper;
import com.magiology.objhelper.helpers.SideHelper;
import com.magiology.upgrades.RegisterUpgrades.Container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityFireLamp extends TileEntityPowGen{
	EffectRenderer efr=Minecraft.getMinecraft().effectRenderer;
	SlowdownHelper optimizer=new SlowdownHelper(20),optimizer2=new SlowdownHelper(4), optimizer3=new SlowdownHelper(3);
	
	public int XZr=15,Yr=4,mode=0;
	public boolean isInMultiblockStructureBFCHelper=false,IsControlledByOSC=false,active,first=true,onOf=false;
	
	
	TileEntity tileE;
	int xc,yc,zc;
	
	public int controlX,controlY,controlZ;
	public TileEntityControlBlock controlBlock=null;
	
	public ForgeDirection[] canAcceptEnergyOnSide=new ForgeDirection[6];
	public ForgeDirection[] canSendEnergyOnSide=new ForgeDirection[6];
	public ForgeDirection connection=null;
	public TileEntityFireLamp(){
		super(SixSidedBoolean.create(First6True,Include,UP(),Last6True,Include,DOWN()).sides,SixSidedBoolean.lastGen.sides.clone(), 1, 2, 5, 115, 100);
		initUpgrades(Container.FireGen);
	}
	
	@Override
	public void addToReadFromNBT(NBTTagCompound NBTTC){
		fuel=NBTTC.getInteger("FT");
		maxFuel=NBTTC.getInteger("MFT");
		IsControlledByOSC=NBTTC.getBoolean("ICBOSC");
		controlX=NBTTC.getInteger("XC");
		controlY=NBTTC.getInteger("YC");
		controlZ=NBTTC.getInteger("ZC");
	}
	@Override
	public void addToWriteToNBT(NBTTagCompound NBTTC){
		NBTTC.setInteger("FT", fuel);
		NBTTC.setInteger("MFT", maxFuel);
		NBTTC.setBoolean("ICBOSC", IsControlledByOSC);
		NBTTC.setInteger("XC", controlX);
		NBTTC.setInteger("YC", controlY);
		NBTTC.setInteger("ZC", controlZ);
	}
	
	@Override
	public void updateEntity(){
		super.updateEntity();
		if(connection!=null)mode=1;
		else mode=0;
		
		if(controlX==xCoord&&controlY==yCoord&&controlZ==zCoord)active=false;
		else active=true;
		TileEntity TileEn=worldObj.getTileEntity(controlX, controlY, controlZ);
		if(TileEn instanceof TileEntityControlBlock){
			controlBlock=(TileEntityControlBlock)TileEn;
		}else controlBlock=null;
		
		if(controlBlock!=null)onOf=controlBlock.onOf;
		else onOf=false;
		
		
		if(active&&onOf){
			power();
			if(isInMultiblockStructureBFCHelper!=true)spawnPaticle(false);
		}
		
		if(optimizer.isTimeWithAddProgress()){
			isInMultiblockStructureBFC();
			updateConnections();
			if(isInMultiblockStructureBFCHelper!=true)updateControlBlock();
			
			
			if(first==true){
				first=false;
				getExistingControlBlockinR(XZr,Yr);
			}else{
				if(controlX>xCoord)if(controlX-XZr>xCoord){controlX=xCoord;controlZ=zCoord;controlY=yCoord;}
				if(controlZ>zCoord)if(controlZ-XZr>zCoord){controlX=xCoord;controlZ=zCoord;controlY=yCoord;}
				if(controlY>yCoord)if(controlY-Yr>yCoord) {controlX=xCoord;controlZ=zCoord;controlY=yCoord;}
				
				if(controlX<xCoord)if(controlX+XZr<xCoord){controlX=xCoord;controlZ=zCoord;controlY=yCoord;}
				if(controlZ<zCoord)if(controlZ+XZr<zCoord){controlX=xCoord;controlZ=zCoord;controlY=yCoord;}
				if(controlY<yCoord)if(controlY+Yr<yCoord) {controlX=xCoord;controlZ=zCoord;controlY=yCoord;}
			}
		}
		
		
		PowerHelper.sortSides(this);
	}
	public void updateConnections(){
		TileEntity tile=worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
		if(tile instanceof TileEntityFirePipe)connection=((TileEntityFirePipe)tile).connections[0]!=null?ForgeDirection.UP:null;
		else connection=null;
	}
	@SideOnly(Side.CLIENT)
	public void spawnPaticle(boolean isforced){
		if(!worldObj.isRemote)return;
		if(connection!=null){
			worldObj.spawnParticle("lava", xCoord+p*4+(Helper.RF()*p*8), yCoord+p*11, zCoord+p*4+(Helper.RF()*p*8), 0, 0, 0);
			worldObj.spawnParticle("flame", xCoord+p*4+(Helper.RF()*p*8), yCoord+p*11, zCoord+p*4+(Helper.RF()*p*8), 0.025-(Helper.RF()*0.05), (Helper.RF()*0.2), 0.025-(Helper.RF()*0.05));
		}
		if(optimizer3.isTimeWithAddProgress())return;
		
		if(canGeneratePower(3)||isforced){
			if(optimizer2.isTimeWithAddProgress()){
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+p*4, yCoord+p*11, zCoord+p*4, -0.02, -0.005, -0.02));
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+p*12, yCoord+p*11, zCoord+p*4, 0.02, -0.005, -0.02));
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+p*12, yCoord+p*11, zCoord+p*12, 0.02, -0.005, 0.02));
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+p*4, yCoord+p*11, zCoord+p*12, -0.02, -0.005, 0.02));
				Helper.spawnEntityFX(new EntitySparkFX(worldObj, xCoord+0.5, yCoord+p*5.5, zCoord+0.5, 0.5F/16F, 0.1F,1,5,100,Vec3.createVectorHelper(0,0,0)));
			}else{
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+p*4, yCoord+p*11, zCoord+p*4, -0.02, -0.005, -0.02),16);
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+p*12, yCoord+p*11, zCoord+p*4, 0.02, -0.005, -0.02),16);
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+p*12, yCoord+p*11, zCoord+p*12, 0.02, -0.005, 0.02),16);
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+p*4, yCoord+p*11, zCoord+p*12, -0.02, -0.005, 0.02),16);
			}
			if(Helper.RB())Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj, xCoord+p*6+(Helper.RF()*p*4), yCoord+p*11-Helper.RF()*p*5, zCoord+p*6+(Helper.RF()*p*4), Helper.CRandF(0.005), -0.01, Helper.CRandF(0.005), 300, 2, -1, 1, 0.2+Helper.RF()*0.5, 0.2+Helper.RF()*0.2, 0.7),20);
			else Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj, xCoord+0.35+(Helper.RF()*0.35), yCoord+Helper.RF()*p*5, zCoord+0.35+(Helper.RF()*0.35), Helper.CRandF(0.005), 0.01, Helper.CRandF(0.005), 300, 2, 1, 1, 0.2+Helper.RF()*0.5, 0.2+Helper.RF()*0.2, 0.7),20);
		}
		
		
	}
	
	public void power(){
		if(isInMultiblockStructureBFCHelper==true){
			currentEnergy=0;
			controlBlock=null;
		}
		else if(canGeneratePower(3))generateFunction();
		
		if(connection!=null&&currentEnergy>5){
			currentEnergy-=5;
		}
		
		
		if(controlBlock!=null){
			for(int a=0;a<2;a++)if(controlBlock.tank>0&&fuel<maxFuel){
				fuel+=1;
				controlBlock.tank-=1;
			}
		}
		tryToSendEnergyToObj();
	}
	
	
	public void tryToSendEnergyToObj(){
		TileEntity aa=worldObj.getTileEntity(xCoord, yCoord+1, zCoord);
		if(aa instanceof TileEntityFirePipe){
			TileEntityFirePipe pipe= (TileEntityFirePipe)aa;
			if(PowerHelper.tryToDrainFromTo(this, pipe, PowerHelper.getHowMuchToSendFromToForDrain(this, pipe),SideHelper.ForgeDirgetOrientationInverted(ForgeDirection.UP)))if(worldObj.isRemote&&optimizer3.progress==0){
				EntitySmoothBubleFX particle=new EntitySmoothBubleFX(worldObj, xCoord+0.5, yCoord+p*15, zCoord+0.5, 0, 0.02, 0, 180, 2.5, 0.5, 1, 0.2+Helper.RF()*0.5, 0.2+Helper.RF()*0.2, 1);
				if(Helper.RInt(3)==0)Helper.spawnEntityFX(particle);
				else Helper.spawnEntityFX(particle,20);
			}
		}
	}
	
	public void isInMultiblockStructureBFC(){
		for(int x1=-1;x1<=1;x1++){for(int y1=-2;y1<=-1;y1++){for(int z1=-1;z1<=1;z1++){
			
			tileE=worldObj.getTileEntity(xCoord+x1, yCoord+y1, zCoord+z1);
			if(tileE instanceof TileEntityBigFurnaceCore){
				TileEntityBigFurnaceCore BFC=(TileEntityBigFurnaceCore) worldObj.getTileEntity(xCoord+x1, yCoord+y1, zCoord+z1);
				xc=xCoord+x1;
				yc=yCoord+y1;
				zc=zCoord+z1;
				
				controlX=xc;
				controlY=yc;
				controlZ=zc;
				
				if(BFC.isMultiblockHelper)yc=yCoord+y1;
				else yc=10000;
				x1=2;
				y1=2;
				z1=2;
			}
			else{
			yc=10000;
		}}}
			
		}
			if(yc==10000)isInMultiblockStructureBFCHelper=false;
			else isInMultiblockStructureBFCHelper=true;
	}
	
	public void updateControlBlock(){
		TileEntity tileTest=worldObj.getTileEntity(controlX,controlY,controlZ);
		if(IsControlledByOSC&&tileTest instanceof TileEntityOreStructureCore){
			return;
		}
		else IsControlledByOSC=false;
		
		if(tileTest instanceof TileEntityControlBlock){
			return;
		}
		else{
			controlX=xCoord;
			controlY=yCoord;
			controlZ=zCoord;
		}
	}
	
	public void getExistingControlBlockinR(int xz2,int y2){
		int x1=0,y1=0,z1=0;
		
		for(int x=-xz2;x<xz2+1;x++){
			for(int y=-y2;y<y2+1;y++){
				for(int z=-xz2;z<xz2+1;z++){
					if(worldObj.getTileEntity(x+xCoord, y+yCoord, z+zCoord)instanceof TileEntityControlBlock){
						x1=x;y1=y;z1=z;
						x=xz2;y=y2;z=xz2;
					}
				}
			}
		}
		
		controlX=x1+xCoord;
		controlY=y1+yCoord;
		controlZ=z1+zCoord;
	}

	@Override
	public void generateFunction(){
		currentEnergy+=3;
		fuel--;
	}

	@Override
	public boolean canGeneratePowerAddon(){
		return onOf&&fuel>0;
	}
	@Override
	public AxisAlignedBB getRenderBoundingBox(){
		AxisAlignedBB dc=super.getRenderBoundingBox();
		dc.maxX+=controlX-xCoord>0?controlX-xCoord:0;
		dc.maxY+=controlY-yCoord>0?controlY-yCoord:0;
		dc.maxZ+=controlZ-zCoord>0?controlZ-zCoord:0;
		dc.minX+=controlX-xCoord<0?controlX-xCoord:0;
		dc.minY+=controlY-yCoord<0?controlY-yCoord:0;
		dc.minZ+=controlZ-zCoord<0?controlZ-zCoord:0;
		return dc;
	}
}
