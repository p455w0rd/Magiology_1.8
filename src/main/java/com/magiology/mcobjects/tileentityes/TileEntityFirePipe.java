package com.magiology.mcobjects.tileentityes;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.api.power.ISidedPower;
import com.magiology.forgepowered.event.ForcePipeUpdate;
import com.magiology.forgepowered.packets.packets.NotifyPointedBoxChangePacket;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.Container;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.UpgradeType;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.m_extension.effect.EntitySmokeFXM;

public class TileEntityFirePipe extends TileEntityPow implements MultiColisionProvider{
	PowerUtil PH=new PowerUtil();
	SlowdownUtil optimizer=new SlowdownUtil(40);
	SlowdownUtil optimizer2=new SlowdownUtil(6);
	
	public boolean
		DCFFL,
		isSolidDown,
		isSolidUp,
		texAnimP2=true;
	public int texAnim=0;
	public AxisAlignedBB 
		pointId,
		prevPointId,
		collisionBoxes[];
	
	public TileEntityFirePipe(){
		super(null, null, 1, 5, 50, 3000);
		initUpgrades(Container.FirePipe);
		setColisionBoxes();
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound NBTTC){
		super.readFromNBT(NBTTC);
    	ForcePipeUpdate.updatein3by3(worldObj, pos);
    }
	
    @Override
	public void writeToNBT(NBTTagCompound NBTTC){
    	super.writeToNBT(NBTTC);
    }
	
	@Override
	public void update(){
		power(true);
		
		//Get if/what side is first
		hasPriorityUpg=false;
		for(int i=0;i<containerItems.length;i++)if(containerItems[i]!=null&&containerItems[i].hasTagCompound()){
			UpgradeType type=RegisterItemUpgrades.getItemUpgradeType(RegisterItemUpgrades.getItemUpgradeID(containerItems[i].getItem()));
			if(type==UpgradeType.Priority){
				hasPriorityUpg=true;
				FirstSide=containerItems[i].getTagCompound().getInteger("side");
				continue;
			}
		}
		
		texAnimation();
		
		if(optimizer.isTimeWithAddProgress()){
			updateConnections();
		}
		if(optimizer2.isTimeWithAddProgress()&&worldObj.isRemote){
			spawnParticles();
		}
//		if(worldObj.isRemote)for(int i=0;i<containerItems.length;i++)if(containerItems[i]!=null){
//			Helper.spawnEnitiyFX(new EntitySmokeFXM(worldObj, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0, 0.2, 0));
//		}
		PowerUtil.sortSides(this);
	}
	
	public void texAnimation(){
		if(texAnimP2)texAnim++;
		else texAnim--;
		
		if(texAnim>=10)texAnimP2=false;
		if(texAnim<=1)texAnimP2=true;
	}
	
	public void spawnParticles(){
		if(Util.RB(0.2)&&currentEnergy+100>maxEnergyBuffer)
		{
			if(!connections[0].hasOpposite()&&!connections[2].hasOpposite()&&!connections[4].hasOpposite()){
				if(Util.RB(0.33)&&isSolidDown==false&&connections[1]==null)Util.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+0.5, pos.getY()+0.5-p*2, pos.getZ()+0.5, 0, -0.1, 0));
				if(Util.RB(0.33)&&isSolidUp==false&&connections[0]==null)Util.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+0.5, pos.getY()+0.5+p*2, pos.getZ()+0.5, 0, 0.05, 0));
				if(Util.RB(0.33)&&connections[2]==null)Util.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5-p*2, 0, 0, -0.1));
				if(Util.RB(0.33)&&connections[4]==null)Util.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5+p*2, 0, 0, 0.1));
				if(Util.RB(0.33)&&connections[3]==null)Util.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+0.5+p*2, pos.getY()+0.5, pos.getZ()+0.5, 0.1, 0, 0));
				if(Util.RB(0.33)&&connections[5]==null)Util.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+0.5-p*2, pos.getY()+0.5, pos.getZ()+0.5, -0.1, 0, 0));
			}
		}
	}

	public void updatestand(){
		if(worldObj.isSideSolid(pos.add(0, -1, 0), EnumFacing.DOWN)==true&&connections[1]==null)isSolidDown=true;
		else isSolidDown=false;
		
		if(worldObj.isSideSolid(pos.add(0, 1, 0), EnumFacing.UP)==true&&connections[0]==null)isSolidUp=true;
		else isSolidUp=false;
	}
	@Override
	public void updateConnections(){
		if(U.isRemote(worldObj))updatestand();
//		for(int i=0;i<6;i++)connections[i].setIsMainAutomatic(false);
		boolean[] in1={},out1={};
		TileEntity[] tiles=SideUtil.getTilesOnSides(this);
		for(int a=0;a<6;a++){
			if(tiles[a]==null){
				in1 =ArrayUtils.add( in1, false);
				out1=ArrayUtils.add(out1, false);
			}else if(tiles[a] instanceof ISidedPower){
				in1 =ArrayUtils.add( in1, ((ISidedPower)tiles[a]).getIn(SideUtil.getOppositeSide(a)));
				out1=ArrayUtils.add(out1, ((ISidedPower)tiles[a]).getOut(SideUtil.getOppositeSide(a)));
			}else{
				in1 =ArrayUtils.add( in1, false);
				out1=ArrayUtils.add(out1, false);
			}
		}
		
		connections[0].setMain(isTPipe(0)||isTRand(SideUtil.offsetNew(0, pos)));
		connections[1].setMain(isTPipe(1)||isTRand(SideUtil.offsetNew(1, pos))||(worldObj.getTileEntity(SideUtil.offsetNew(1, pos)))instanceof TileEntityFireExhaust);
		connections[2].setMain(isTPipe(2)||isTRand(SideUtil.offsetNew(2, pos)));
		connections[3].setMain(isTPipe(3)||isTRand(SideUtil.offsetNew(3, pos)));
		connections[4].setMain(isTPipe(4)||isTRand(SideUtil.offsetNew(4, pos)));
		connections[5].setMain(isTPipe(5)||isTRand(SideUtil.offsetNew(5, pos)));
		
		connections[0].setOut(out1[0]);
		connections[1].setOut(out1[1]);
		connections[2].setOut(out1[2]);
		connections[3].setOut(out1[3]);
		connections[4].setOut(out1[4]);
		connections[5].setOut(out1[5]);
		
		connections[0].setIn(in1[0]);
		connections[1].setIn(in1[1]);
		connections[2].setIn(in1[2]);
		connections[3].setIn(in1[3]);
		connections[4].setIn(in1[4]);
		connections[5].setIn(in1[5]);
		
		
		for(int a=0;a<6;a++){
			connections[a].setWillRender(true);
			connections[a].setForce(true);
			if(connections[a].getIn()||connections[a].getOut())connections[a].setMain(true);
			if(connections[a].isBanned())connections[a].clear();
		}
		DCFFL = isTLamp(pos.add(0,-1,0))&&connections[0]!=null&&!connections[EnumFacing.DOWN.getIndex()].isBanned();
		
		
		if(DCFFL){
			connections[0].setMain(false);
			connections[0].setForce(false);
			connections[0].setWillRender(false);
		}
		setColisionBoxes();
		for(int a=0;a<6;a++){
			boolean in=connections[a].getIn(),out=connections[a].getOut(),inOut=connections[a].getMain();
			setReceaveOnSide(a, ((inOut||in)&&out==false));
			setSendOnSide   (a, (inOut||out)&&in==false);
		}
	}
	@Override
	public boolean getIn(int direction){
		boolean in=connections[direction].getIn(),out=connections[direction].getOut(),inOut=connections[direction].getMain();
		if(inOut&&!in&&!out)return true;
		if(in)return true;
		if(direction==0&&DCFFL)return true;
		return false;
	}
	@Override
	public boolean getOut(int direction){
		boolean in=connections[direction].getIn(),out=connections[direction].getOut(),inOut=connections[direction].getMain();
		if(inOut&&!in&&!out)return true;
		if(out)return true;
		return false;
	}
	
	public boolean isTOSO(BlockPos pos){
		return worldObj.getTileEntity(pos)instanceof TileEntityOreStructureCore;
	}
	
	public boolean isTPipe(int side){
		BlockPos pos1=SideUtil.offsetNew(side,pos);
		int dir=SideUtil.getOppositeSide(side);
		TileEntity tile=worldObj.getTileEntity(pos1);
		if(tile instanceof TileEntityFirePipe){
			if(!((TileEntityFirePipe)tile).connections[dir].isBanned()){
				return true;
			}
		}
		return false;
	}
	public boolean isTLamp(BlockPos pos){
		return worldObj.getTileEntity(pos)instanceof TileEntityFireLamp;
	}
	public boolean isTRand(BlockPos pos){
		boolean is=false;
		TileEntity tile1=worldObj.getTileEntity(pos);
		if(tile1 instanceof TileEntityBFCPowerOut)is=true;
		
		return is;
	}
	public void power(boolean isRepeatable){
		handleStandardPowerTransmission(isRepeatable);
		if(Util.RInt(5)==0){
			int side=Util.RInt(6);
			TileEntity tile=worldObj.getTileEntity(SideUtil.offsetNew(side, pos));
			
			if(connections[side].getMain()&&connections[side].getIn()&&connections[side].getOut()&&tile instanceof TileEntityFirePipe&&getEnergy()>0&&((TileEntityFirePipe)tile).getEnergy()<((TileEntityFirePipe)tile).getMaxEnergy()-1){
				PowerUtil.tryToDrainFromTo(this, tile, 1,side);
			}
			
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        AxisAlignedBB bb = new AxisAlignedBB(pos.getX(), pos.getY()-(DCFFL?0.5:0), pos.getZ(), pos.getX()+1, pos.getY()+1, pos.getZ()+1);
        return bb;
    }
	
	@Override
	public void setColisionBoxes(){
		collisionBoxes=new AxisAlignedBB[]{
				connections[5].getMain()?getExpectedColisionBoxes()[3]:null,
				connections[1].getMain()?getExpectedColisionBoxes()[4]:null,
				connections[2].getMain()?getExpectedColisionBoxes()[2]:null,
				connections[3].getMain()?getExpectedColisionBoxes()[5]:null,
				connections[0].getMain()?getExpectedColisionBoxes()[1]:null,
				connections[4].getMain()?getExpectedColisionBoxes()[0]:null,
						    			 getExpectedColisionBoxes()[6],
				DCFFL?    			     getExpectedColisionBoxes()[7]:null,
				DCFFL?   			     getExpectedColisionBoxes()[8]:null,
		};
	}
	private AxisAlignedBB[] expectedBoxes=new AxisAlignedBB[]{
			new AxisAlignedBB(0      ,p*6,p*6,p*6,p*10,p*10),
			new AxisAlignedBB(p*6,0      ,p*6,p*10,p*6,p*10),
			new AxisAlignedBB(p*6,p*6,0      ,p*10,p*10,p*6),
			new AxisAlignedBB(p*10,p*6,p*6,1      ,p*10,p*10),
			new AxisAlignedBB(p*6,p*10,p*6,p*10,1      ,p*10),
			new AxisAlignedBB(p*6,p*6,p*10,p*10,p*10,1      ),
			new AxisAlignedBB(p*6, p*6, p*6, p*10, p*10, p*10),
			new AxisAlignedBB(p*4.5F,-p*4.7F,p*4.5F,p*11.5F,p*0.1F,p*11.5F),
			new AxisAlignedBB(p*6,0 ,p*6,p*10,p*6F,p*10),
	};
	@Override
	public AxisAlignedBB[] getExpectedColisionBoxes(){
		return expectedBoxes;
	}
	
	@Override public void setPrevPointedBox(AxisAlignedBB box){prevPointId=box;}
	@Override public AxisAlignedBB getPrevPointedBox(){return prevPointId;}
	@Override public AxisAlignedBB getPointedBox(){return pointId;}
	@Override public void setPointedBox(AxisAlignedBB box){
		setPrevPointedBox(pointId);
		pointId=box;
		detectAndSendChanges();
	}
	@Override
	public void detectAndSendChanges(){
		if(!U.isRemote(this))return;
		if(Util.AxisAlignedBBEqual(pointId,prevPointId))return;
		Util.sendMessage(new NotifyPointedBoxChangePacket(this));
	}
	
	@Override
	public AxisAlignedBB[] getActiveBoxes(){
		AxisAlignedBB[] Result={};
//		System.out.print(collisionBoxes+" "+worldObj.isRemote+"\n");
		if(collisionBoxes!=null)
		for(int i=0;i<collisionBoxes.length;i++)if(collisionBoxes[i]!=null)Result=ArrayUtils.add(Result, collisionBoxes[i]);
		return Result;
	}
	
	@Override
	public void getBoxesOnSide(List<AxisAlignedBB> result, int side){
		switch(side){
		case 0:if(connections[side].getMain()){
			result.add(collisionBoxes[4]);
		}break;
		case 1:if(connections[side].getMain()){
			if(!DCFFL)result.add(collisionBoxes[1]);
			else result.add(collisionBoxes[8]);
			result.add(collisionBoxes[7]);
		}break;
		case 2:if(connections[side].getMain()){
			result.add(collisionBoxes[2]);
		}break;
		case 3:if(connections[side].getMain()){
			result.add(collisionBoxes[3]);
		}break;
		case 4:if(connections[side].getMain()){
			result.add(collisionBoxes[5]);
		}break;
		case 5:if(connections[side].getMain()){
			result.add(collisionBoxes[0]);
		}break;
		}
	}
	@Override
	public void getExpectedBoxesOnSide(List<AxisAlignedBB> result, int side){
		switch(side){
		case 0:result.add(getExpectedColisionBoxes()[4]);break;
		case 1:{
			result.add(getExpectedColisionBoxes()[1]);
			result.add(getExpectedColisionBoxes()[8]);
			result.add(getExpectedColisionBoxes()[7]);
		}break;
		case 2:result.add(getExpectedColisionBoxes()[2]);break;
		case 3:result.add(getExpectedColisionBoxes()[3]);break;
		case 4:result.add(getExpectedColisionBoxes()[5]);break;
		case 5:result.add(getExpectedColisionBoxes()[0]);break;
		}
	}
	
	@Override
	public AxisAlignedBB getMainBox(){
		return collisionBoxes[6];
	}
	
	@Override
	public AxisAlignedBB[] getBoxes(){
		return collisionBoxes;
	}
	@Override
	public boolean isPowerKeptOnWrench(){
		return false;
	}
	@Override
	public boolean isStrate(EnumFacing facing){
		if(facing==EnumFacing.UP||facing==EnumFacing.DOWN||facing==null){
			if(((connections[0].getMain()&&!DCFFL)&&connections[1].getMain())&&(connections[2].getMain()==false&&connections[3].getMain()==false&&connections[4].getMain()==false&&connections[5].getMain()==false)){
//				Helper.printInln("1");
				return true;
			}
		}
		if(facing==EnumFacing.WEST||facing==EnumFacing.EAST||facing==null){
			if((connections[4].getMain()&&connections[5].getMain())&&(connections[1].getMain()==false&&(connections[0].getMain()==false&&!DCFFL)&&connections[2].getMain()==false&&connections[3].getMain()==false)){
//				Helper.printInln("2");
				return true;
			}
		}
		if(facing==EnumFacing.SOUTH||facing==EnumFacing.NORTH||facing==null){
			if((connections[2].getMain()&&connections[3].getMain())&&(connections[1].getMain()==false&&(connections[0].getMain()==false&&!DCFFL)&&connections[4].getMain()==false&&connections[5].getMain()==false)){
//				Helper.printInln("3");
				return true;
			}
		}
		return false;
	}
}
