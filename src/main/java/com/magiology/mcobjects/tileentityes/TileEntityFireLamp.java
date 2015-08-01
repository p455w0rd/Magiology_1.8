package com.magiology.mcobjects.tileentityes;

import static com.magiology.api.power.SixSidedBoolean.Modifier.*;
import static com.magiology.objhelper.helpers.SideHelper.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.magiology.api.power.SixSidedBoolean;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.effect.EntitySparkFX;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPowGen;
import com.magiology.objhelper.SlowdownHelper;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.PowerHelper;
import com.magiology.objhelper.helpers.SideHelper;
import com.magiology.objhelper.vectors.Vec3M;
import com.magiology.upgrades.RegisterUpgrades.Container;

public class TileEntityFireLamp extends TileEntityPowGen{
	EffectRenderer efr=Minecraft.getMinecraft().effectRenderer;
	SlowdownHelper optimizer=new SlowdownHelper(20),optimizer2=new SlowdownHelper(4), optimizer3=new SlowdownHelper(3);
	
	public int XZr=15,Yr=4,mode=0;
	public boolean isInMultiblockStructureBFCHelper=false,IsControlledByOSC=false,active,first=true,onOf=false;
	
	
	TileEntity tileE;
	
	public BlockPos control,c;
	public TileEntityControlBlock controlBlock=null;
	
	public EnumFacing[] canAcceptEnergyOnSide=new EnumFacing[6];
	public EnumFacing[] canSendEnergyOnSide=new EnumFacing[6];
	public EnumFacing connection=null;
	public TileEntityFireLamp(){
		super(SixSidedBoolean.create(First6True,Include,UP(),Last6True,Include,DOWN()).sides,SixSidedBoolean.lastGen.sides.clone(), 1, 2, 5, 115, 100);
		initUpgrades(Container.FireGen);
	}
	
	@Override
	public void addToReadFromNBT(NBTTagCompound NBTTC){
		fuel=NBTTC.getInteger("FT");
		maxFuel=NBTTC.getInteger("MFT");
		IsControlledByOSC=NBTTC.getBoolean("ICBOSC");
		control=readPos(NBTTC, "C");
	}
	@Override
	public void addToWriteToNBT(NBTTagCompound NBTTC){
		NBTTC.setInteger("FT", fuel);
		NBTTC.setInteger("MFT", maxFuel);
		NBTTC.setBoolean("ICBOSC", IsControlledByOSC);
		writePos(NBTTC, control, "C");
	}
	
	@Override
	public void update(){
		if(connection!=null)mode=1;
		else mode=0;
		
		if(control.equals(pos))active=false;
		else active=true;
		TileEntity TileEn=worldObj.getTileEntity(control);
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
				if(control.getX()>pos.getX()&&control.getX()-XZr>pos.getX()){control=pos.add(0, 0, 0);}
				if(control.getZ()>pos.getZ()&&control.getZ()-XZr>pos.getZ()){control=pos.add(0, 0, 0);}
				if(control.getY()>pos.getY()&&control.getY()-Yr>pos.getY()) {control=pos.add(0, 0, 0);}
				
				if(control.getX()<pos.getX()&&control.getX()+XZr<pos.getX()){control=pos.add(0, 0, 0);}
				if(control.getZ()<pos.getZ()&&control.getZ()+XZr<pos.getZ()){control=pos.add(0, 0, 0);}
				if(control.getY()<pos.getY()&&control.getY()+Yr<pos.getY()) {control=pos.add(0, 0, 0);}
			}
		}
		
		
		PowerHelper.sortSides(this);
	}
	public void updateConnections(){
		TileEntity tile=worldObj.getTileEntity(pos.add(0, -1, 0));
		if(tile instanceof TileEntityFirePipe)connection=((TileEntityFirePipe)tile).connections[0]!=null?EnumFacing.UP:null;
		else connection=null;
	}
	@SideOnly(Side.CLIENT)
	public void spawnPaticle(boolean isforced){
		if(!worldObj.isRemote)return;
		if(connection!=null){
			worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX()+p*4+(Helper.RF()*p*8), pos.getY()+p*11, pos.getZ()+p*4+(Helper.RF()*p*8), 0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+p*4+(Helper.RF()*p*8), pos.getY()+p*11, pos.getZ()+p*4+(Helper.RF()*p*8), 0.025-(Helper.RF()*0.05), (Helper.RF()*0.2), 0.025-(Helper.RF()*0.05));
		}
		if(optimizer3.isTimeWithAddProgress())return;
		
		if(canGeneratePower(3)||isforced){
			if(optimizer2.isTimeWithAddProgress()){
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, pos.getX()+p*4, pos.getY()+p*11, pos.getZ()+p*4, -0.02, -0.005, -0.02));
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, pos.getX()+p*12, pos.getY()+p*11, pos.getZ()+p*4, 0.02, -0.005, -0.02));
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, pos.getX()+p*12, pos.getY()+p*11, pos.getZ()+p*12, 0.02, -0.005, 0.02));
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, pos.getX()+p*4, pos.getY()+p*11, pos.getZ()+p*12, -0.02, -0.005, 0.02));
				Helper.spawnEntityFX(new EntitySparkFX(worldObj, pos.getX()+0.5, pos.getY()+p*5.5, pos.getZ()+0.5, 0.5F/16F, 0.1F,1,5,100,new Vec3M(0,0,0)));
			}else{
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, pos.getX()+p*4, pos.getY()+p*11, pos.getZ()+p*4, -0.02, -0.005, -0.02),16);
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, pos.getX()+p*12, pos.getY()+p*11, pos.getZ()+p*4, 0.02, -0.005, -0.02),16);
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, pos.getX()+p*12, pos.getY()+p*11, pos.getZ()+p*12, 0.02, -0.005, 0.02),16);
				Helper.spawnEntityFX(new EntitySmokeFX(worldObj, pos.getX()+p*4, pos.getY()+p*11, pos.getZ()+p*12, -0.02, -0.005, 0.02),16);
			}
			if(Helper.RB())Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj, pos.getX()+p*6+(Helper.RF()*p*4), pos.getY()+p*11-Helper.RF()*p*5, pos.getZ()+p*6+(Helper.RF()*p*4), Helper.CRandF(0.005), -0.01, Helper.CRandF(0.005), 300, 2, -1, 1, 0.2+Helper.RF()*0.5, 0.2+Helper.RF()*0.2, 0.7),20);
			else Helper.spawnEntityFX(new EntitySmoothBubleFX(worldObj, pos.getX()+0.35+(Helper.RF()*0.35), pos.getY()+Helper.RF()*p*5, pos.getZ()+0.35+(Helper.RF()*0.35), Helper.CRandF(0.005), 0.01, Helper.CRandF(0.005), 300, 2, 1, 1, 0.2+Helper.RF()*0.5, 0.2+Helper.RF()*0.2, 0.7),20);
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
		TileEntity aa=worldObj.getTileEntity(pos.add(0,1,0));
		if(aa instanceof TileEntityFirePipe){
			TileEntityFirePipe pipe= (TileEntityFirePipe)aa;
			if(PowerHelper.tryToDrainFromTo(this, pipe, PowerHelper.getHowMuchToSendFromToForDrain(this, pipe),SideHelper.ForgeDirgetOrientationInverted(EnumFacing.UP)))if(worldObj.isRemote&&optimizer3.progress==0){
				EntitySmoothBubleFX particle=new EntitySmoothBubleFX(worldObj, pos.getX()+0.5, pos.getY()+p*15, pos.getZ()+0.5, 0, 0.02, 0, 180, 2.5, 0.5, 1, 0.2+Helper.RF()*0.5, 0.2+Helper.RF()*0.2, 1);
				if(Helper.RInt(3)==0)Helper.spawnEntityFX(particle);
				else Helper.spawnEntityFX(particle,20);
			}
		}
	}
	
	public void isInMultiblockStructureBFC(){
		for(int x1=-1;x1<=1;x1++){for(int y1=-2;y1<=-1;y1++){for(int z1=-1;z1<=1;z1++){
			
			tileE=worldObj.getTileEntity(pos.add(x1,y1,z1));
			if(tileE instanceof TileEntityBigFurnaceCore){
				TileEntityBigFurnaceCore BFC=(TileEntityBigFurnaceCore)tileE;
				c=pos.add(x1,y1,z1);
				
				control=c.add(0, 0, 0);
				
				if(BFC.isMultiblockHelper)c=new BlockPos(c.getX(),pos.getY()+y1,c.getZ());
				else c=new BlockPos(c.getX(),10000,c.getZ());
				x1=2;
				y1=2;
				z1=2;
			}
			else{
				c=new BlockPos(c.getX(),10000,c.getZ());
		}}}
			
		}
		if(c.getY()==10000)isInMultiblockStructureBFCHelper=false;
		else isInMultiblockStructureBFCHelper=true;
	}
	
	public void updateControlBlock(){
		TileEntity tileTest=worldObj.getTileEntity(control);
		if(IsControlledByOSC&&tileTest instanceof TileEntityOreStructureCore){
			return;
		}
		else IsControlledByOSC=false;
		
		if(tileTest instanceof TileEntityControlBlock){
			return;
		}
		else{
			control=pos.add(0,0,0);
		}
	}
	
	public void getExistingControlBlockinR(int xz2,int y2){
		int x1=0,y1=0,z1=0;
		
		for(int x=-xz2;x<xz2+1;x++){
			for(int y=-y2;y<y2+1;y++){
				for(int z=-xz2;z<xz2+1;z++){
					if(worldObj.getTileEntity(pos.add(x,y,z))instanceof TileEntityControlBlock){
						x1=x;y1=y;z1=z;
						x=xz2;y=y2;z=xz2;
					}
				}
			}
		}
		
		control=pos.add(pos);
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
		dc=new AxisAlignedBB(
			dc.maxX+(control.getX()-pos.getX()>0?control.getX()-pos.getX():0),
			dc.maxY+(control.getY()-pos.getY()>0?control.getY()-pos.getY():0),
			dc.maxZ+(control.getZ()-pos.getZ()>0?control.getZ()-pos.getZ():0),
			dc.minX+(control.getX()-pos.getX()<0?control.getX()-pos.getX():0),
			dc.minY+(control.getY()-pos.getY()<0?control.getY()-pos.getY():0),
			dc.minZ+(control.getZ()-pos.getZ()<0?control.getZ()-pos.getZ():0));
		return dc;
	}
}
