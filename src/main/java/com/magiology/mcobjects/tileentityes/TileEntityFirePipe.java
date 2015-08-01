package com.magiology.mcobjects.tileentityes;

import java.util.List;

import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.api.power.ISidedPower;
import com.magiology.api.power.PowerCore;
import com.magiology.forgepowered.packets.NotifyPointedBoxChangePacket;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateablePipe;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.objhelper.SlowdownHelper;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;
import com.magiology.objhelper.helpers.PowerHelper;
import com.magiology.objhelper.helpers.SideHelper;
import com.magiology.upgrades.RegisterUpgrades;
import com.magiology.upgrades.RegisterUpgrades.Container;
import com.magiology.upgrades.RegisterUpgrades.UpgradeType;

public class TileEntityFirePipe extends TileEntityPow implements MultiColisionProvider,UpdateablePipe{
	PowerHelper PH=new PowerHelper();
	SlowdownHelper optimizer=new SlowdownHelper(40);
	SlowdownHelper optimizer2=new SlowdownHelper(6);
	
	public EnumFacing[] connections = new EnumFacing[6];
	public boolean[] shouldConnectionsBeRendered=new boolean[6];
	public EnumFacing[] connectionsToObjInMe = new EnumFacing[6];
	public EnumFacing[] connectionsToObjOut = new EnumFacing[6];
	public EnumFacing[] strateConnection = new EnumFacing[3];
	public EnumFacing DCFFL=EnumFacing.UP;
	public boolean isSolidDown;
	public boolean isSolidUp;
	public int texAnim=0;
	public AxisAlignedBB pointId,prevPointId;
	public boolean texAnimP2=true;
	public AxisAlignedBB[] collisionBoxes=null;
	
	public TileEntityFirePipe(){
		super(null, null, 1, 5, 50, 3000);
		initUpgrades(Container.FirePipe);
		setColisionBoxes();
	}
	
	
	@Override
	public void addToReadFromNBT(NBTTagCompound NBTTC){
    	for(int i=0;i<6;i++){
    		bannedConnections[i]=NBTTC.getBoolean("bannedConnections"+i);
    		int con=NBTTC.getInteger("connections"+i);
    		if(con<6&&con>-1)connections[i]=EnumFacing.getFront(con);
    		else connections[i]=null;
    	}
    }
	
    @Override
	public void addToWriteToNBT(NBTTagCompound NBTTC){
    	for (int i=0;i<6;i++){
    		NBTTC.setBoolean("bannedConnections"+i, bannedConnections[i]);
    		NBTTC.setInteger("connections"+i, SideHelper.ForgeDirgetOrientationInverted(connections[i]));
    	}
    }
	
	@Override
	public void update(){
		power(true);
		
		//Get if/what side is first
		hasPriorityUpg=false;
		for(int i=0;i<containerItems.length;i++)if(containerItems[i]!=null&&containerItems[i].hasTagCompound()){
			UpgradeType type=RegisterUpgrades.getItemUpgradeType(RegisterUpgrades.getItemUpgradeID(containerItems[i].getItem()));
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
//			Helper.spawnEnitiyFX(new EntitySmokeFX(worldObj, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0, 0.2, 0));
//		}
		PowerHelper.sortSides(this);
	}
	
	public void texAnimation(){
		if(texAnimP2)texAnim++;
		else texAnim--;
		
		if(texAnim>=10)texAnimP2=false;
		if(texAnim<=1)texAnimP2=true;
	}
	
	public void spawnParticles(){
		if(Helper.RB(0.2)&&currentEnergy+100>maxEnergyBuffer)
		{
			if(strateConnection[0]==null&&strateConnection[1]==null&&strateConnection[2]==null){
				if(Helper.RB(0.33)&&isSolidDown==false&&connections[1]==null)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, pos.getX()+0.5, pos.getY()+0.5-p*2, pos.getZ()+0.5, 0, -0.1, 0));
				if(Helper.RB(0.33)&&isSolidUp==false&&connections[0]==null)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, pos.getX()+0.5, pos.getY()+0.5+p*2, pos.getZ()+0.5, 0, 0.05, 0));
				if(Helper.RB(0.33)&&connections[2]==null)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5-p*2, 0, 0, -0.1));
				if(Helper.RB(0.33)&&connections[4]==null)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5+p*2, 0, 0, 0.1));
				if(Helper.RB(0.33)&&connections[3]==null)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, pos.getX()+0.5+p*2, pos.getY()+0.5, pos.getZ()+0.5, 0.1, 0, 0));
				if(Helper.RB(0.33)&&connections[5]==null)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, pos.getX()+0.5-p*2, pos.getY()+0.5, pos.getZ()+0.5, -0.1, 0, 0));
			}
		}
	}

	public void updatestand(){
		if(worldObj.isSideSolid(pos.getX(), pos.getY()-1, pos.getZ(), EnumFacing.DOWN)==true&&connections[1]==null)isSolidDown=true;
		else isSolidDown=false;
		
		if(worldObj.isSideSolid(pos.getX(), pos.getY()+1, pos.getZ(), EnumFacing.UP)==true&&connections[0]==null)isSolidUp=true;
		else isSolidUp=false;
	}
	@Override
	public void updateConnections(){
		if(H.isRemote(worldObj))updatestand();
		if(isTPipe(0)||isTRand(SideHelper.offset(0, pos.getX()), SideHelper.Y(0, pos.getY()), SideHelper.Z(0, pos.getZ()))) connections[0] = EnumFacing.UP;else connections[0] = null;
		if(isTPipe(1)||isTRand(SideHelper.offset(1, pos.getX()), SideHelper.Y(1, pos.getY()), SideHelper.Z(1, pos.getZ()))||(worldObj.getTileEntity(SideHelper.offset(1, pos.getX()), SideHelper.Y(1, pos.getY()), SideHelper.Z(1, pos.getZ()))instanceof TileEntityFireExhaust)) connections[1] = EnumFacing.DOWN;else connections[1] = null;
		if(isTPipe(2)||isTRand(SideHelper.offset(2, pos.getX()), SideHelper.Y(2, pos.getY()), SideHelper.Z(2, pos.getZ()))) connections[2] = EnumFacing.NORTH;else connections[2] = null;
		if(isTPipe(3)||isTRand(SideHelper.offset(3, pos.getX()), SideHelper.Y(3, pos.getY()), SideHelper.Z(3, pos.getZ()))) connections[3] = EnumFacing.EAST;else connections[3] = null;
		if(isTPipe(4)||isTRand(SideHelper.offset(4, pos.getX()), SideHelper.Y(4, pos.getY()), SideHelper.Z(4, pos.getZ()))) connections[4] = EnumFacing.SOUTH;else connections[4] = null;
		if(isTPipe(5)||isTRand(SideHelper.offset(5, pos.getX()), SideHelper.Y(5, pos.getY()), SideHelper.Z(5, pos.getZ()))) connections[5] = EnumFacing.WEST;else connections[5] = null;
		boolean[] in1={},out1={};
		TileEntity[] tiles=new TileEntity[6];
		for(int a=0;a<6;a++){
			tiles[a]=worldObj.getTileEntity(SideHelper.offset(a, pos.getX()), SideHelper.Y(a, pos.getY()), SideHelper.Z(a, pos.getZ()));
			if(tiles[a]==null){
				in1 =ArrayUtils.add( in1, false);
				out1=ArrayUtils.add(out1, false);
			}else if(tiles[a] instanceof ISidedPower){
				in1 =ArrayUtils.add( in1, ((ISidedPower)tiles[a]).getSendOnSide(SideHelper.getOppositeSide(a)));
				out1=ArrayUtils.add(out1, ((ISidedPower)tiles[a]).getReceiveOnSide(SideHelper.getOppositeSide(a)));
			}else{
				in1 =ArrayUtils.add( in1, false);
				out1=ArrayUtils.add(out1, false);
			}
		}
		if(in1[0]&&!isTPipe(0))connectionsToObjInMe[0]=EnumFacing.UP;   else connectionsToObjInMe[0]=null;
		if(in1[1]&&!isTPipe(1))connectionsToObjInMe[1]=EnumFacing.DOWN; else connectionsToObjInMe[1]=null;
		if(in1[2]&&!isTPipe(2))connectionsToObjInMe[2]=EnumFacing.NORTH;else connectionsToObjInMe[2]=null;
		if(in1[3]&&!isTPipe(3))connectionsToObjInMe[3]=EnumFacing.EAST; else connectionsToObjInMe[3]=null;
		if(in1[4]&&!isTPipe(4))connectionsToObjInMe[4]=EnumFacing.SOUTH;else connectionsToObjInMe[4]=null;
		if(in1[5]&&!isTPipe(5))connectionsToObjInMe[5]=EnumFacing.WEST; else connectionsToObjInMe[5]=null;
			
		if(out1[0]&&!isTPipe(0))connectionsToObjOut[0]=EnumFacing.UP;   else connectionsToObjOut[0]=null;
		if(out1[1]&&!isTPipe(1))connectionsToObjOut[1]=EnumFacing.DOWN; else connectionsToObjOut[1]=null;
		if(out1[2]&&!isTPipe(2))connectionsToObjOut[2]=EnumFacing.NORTH;else connectionsToObjOut[2]=null;
		if(out1[3]&&!isTPipe(3))connectionsToObjOut[3]=EnumFacing.EAST; else connectionsToObjOut[3]=null;
		if(out1[4]&&!isTPipe(4))connectionsToObjOut[4]=EnumFacing.SOUTH;else connectionsToObjOut[4]=null;
		if(out1[5]&&!isTPipe(5))connectionsToObjOut[5]=EnumFacing.WEST; else connectionsToObjOut[5]=null;
		
//		Helper.printInln(worldObj.getTileEntity(SideHelper.X(SideHelper.WEST(), pos.getX()), SideHelper.Y(SideHelper.WEST(), pos.getY()), SideHelper.Z(SideHelper.WEST(), pos.getZ())));
		
		for(int a=0;a<6;a++){
			shouldConnectionsBeRendered[a]=true;
			if(connectionsToObjInMe[a]!=null)connections[a]=connectionsToObjInMe[a];
			else if(connectionsToObjOut[a]!=null)connections[a]=connectionsToObjOut[a];
		}
		
		if(isTLamp(pos.add(0,-1,0))&&connections[1]!=null){
			DCFFL=EnumFacing.UP;
		}else DCFFL = null;
		
		for(int a=0;a<6;a++){
			if(bannedConnections[a]==true){
				connections[a]=null;
				connectionsToObjInMe[a]=null;
				connectionsToObjOut[a]=null;
			}
		}
		if(((connections[0]!=null&&DCFFL==null)&&connections[1]!=null)&&(connections[2]==null&&connections[3]==null&&connections[4]==null&&connections[5]==null))strateConnection[0] = EnumFacing.UP;else strateConnection[0] = null;
		if((connections[3]!=null&&connections[5]!= null)&&(connections[0]==null&&(connections[1]==null&&DCFFL==null)&&connections[2]==null&&connections[4]==null))strateConnection[1] = EnumFacing.WEST;else strateConnection[1] = null;
		if((connections[2]!=null&&connections[4]!= null)&&(connections[0]==null&&(connections[1]==null&&DCFFL==null)&&connections[3]==null&&connections[5]==null))strateConnection[2] = EnumFacing.SOUTH;else strateConnection[2] = null;
		
		if(DCFFL!=null){
			connectionsToObjInMe[1] = EnumFacing.DOWN;
			shouldConnectionsBeRendered[1]=false;
		}
		setColisionBoxes();
		for(int a=0;a<6;a++){
			boolean in=connectionsToObjInMe[a]!=null,out=connectionsToObjOut[a]!=null,inOut=connections[a]!=null;
			setReceaveOnSide(a, ((inOut||in)&&out==false));
			setSendOnSide   (a, (inOut||out)&&in==false);
		}
	}
	@Override
	public boolean getReceiveOnSide(int direction){
		boolean in=connectionsToObjInMe[direction]!=null,out=connectionsToObjOut[direction]!=null,inOut=connections[direction]!=null;
		if(inOut&&!in&&!out)return true;
		if(in)return true;
		if(direction==0&&DCFFL!=null)return true;
		return false;
	}
	@Override
	public boolean getSendOnSide(int direction){
		boolean in=connectionsToObjInMe[direction]!=null,out=connectionsToObjOut[direction]!=null,inOut=connections[direction]!=null;
		if(inOut&&!in&&!out)return true;
		if(out)return true;
		return false;
	}
	
	public boolean isTOSO(BlockPos pos){
		return worldObj.getTileEntity(pos)instanceof TileEntityOreStructureCore;
	}
	
	public boolean isTPipe(int side){
		BlockPos pos1=SideHelper.offset(side,pos);
		int dir=0;
		switch(side){
		case 0:{dir=1;}break;
		case 1:{dir=0;}break;
		case 2:{dir=4;}break;
		case 3:{dir=5;}break;
		case 4:{dir=2;}break;
		case 5:{dir=3;}break;
		}
		boolean return1=false;
		TileEntity tile=worldObj.getTileEntity(pos1);
		if(tile instanceof TileEntityFirePipe){
			if(((TileEntityFirePipe)tile).bannedConnections[dir]==false){
				return1=true;
			}
		}
		
		return return1;
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
		doSidedPowerTransferBasedOnConections(isRepeatable);
		if(Helper.RInt(5)==0){
			int side=Helper.RInt(6);
			TileEntity tile=worldObj.getTileEntity(SideHelper.offset(side, pos));
			
			if(connections[side]!=null&&connectionsToObjInMe[side]==null&&connectionsToObjOut[side]==null&&tile instanceof TileEntityFirePipe&&getCurrentEnergy()>0&&((TileEntityFirePipe)tile).getCurrentEnergy()<((TileEntityFirePipe)tile).getMaxEnergyBuffer()-1){
				PowerHelper.tryToDrainFromTo(this, tile, 1,side);
			}
			
		}
//		if(isRepeatable){
//
//		}
//		else worldObj.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0.1, 0.1, 0.1);
	}
	/**
	 * Based on variable: connections[]
	 * @param isRepeatable
	 */
	public void doSidedPowerTransferBasedOnConections(boolean isRepeatable){
		//Get a random order of sides that are going to be done
		int[] randSides=SideHelper.randomizeSides();
		//Set the first loop of sending power to a specific side if there is one
		if(hasPriorityUpg){
			int pos=-1;
			for(int a=0;a<randSides.length;a++)if(randSides[a]==FirstSide){
				pos=a;
				continue;
			}if(pos!=0){
				int first=randSides[0];
				int oneThatShouldBeFirst=randSides[pos];
				randSides[0]=oneThatShouldBeFirst;
				randSides[pos]=first;
			}
		}
		//try to send/receive power from all sides
		for (int i=0;i<randSides.length;i++){
			int side=randSides[i];
			TileEntity ab=worldObj.getTileEntity(SideHelper.offset(side,pos));
			//if there is nothing to interact with than skip the process (only for optimization)
			boolean var1=ab!=null&&ab instanceof PowerCore&&ab instanceof ISidedPower;
			//Is next to a special pipe
			boolean var2=false;
			if(var1){
				//special interaction for pipes that contains Priority upgrade
				if(hasPriorityUpg){
					TileEntity a=worldObj.getTileEntity(SideHelper.offset(randSides[0],pos));
					TileEntityPow tile=a instanceof TileEntityPow?(TileEntityPow)a:null;
					if(tile!=null){
						if(i!=0)var1=false;
						if((float)tile.currentEnergy/(float)tile.maxEnergyBuffer>0.9)var1=true;
					}
				}
				//special interaction for pipes that are next to a pipe that contains Priority upgrade
				else{
					TileEntityFirePipe tile=ab instanceof TileEntityFirePipe?(TileEntityFirePipe)ab:null;
					if(tile!=null&&tile.hasPriorityUpg){
						if(SideHelper.offset(tile.FirstSide,tile.pos).equals(pos)){
							var1=false;
							var2=true;
						}
					}
				}
			}
			if(var1){
				TileEntityPow a=hasPriorityUpg||var2?null:getPipeNextToAnotherPipe(side);
				
				if(connections[side]!=null&&connectionsToObjInMe[side]==null&&connectionsToObjOut[side]==null){
					doCustomSidedPowerTransfer(side,ab instanceof TileEntityFirePipe?(var2?-1:(hasPriorityUpg&&FirstSide==side?1:-1)):0);
					if(isRepeatable){
						TileEntity tile=worldObj.getTileEntity(SideHelper.offset(side,pos));
						if(tile instanceof TileEntityFirePipe)((TileEntityFirePipe)tile).power(false);
					}
				}else{
					if(connectionsToObjInMe[side]!=null)doCustomSidedPowerTransfer(side,0);
					else if(connectionsToObjOut[side]!=null)doCustomSidedPowerTransfer(side,1);
				}
			}
		}
	}
	/**
	 * @param side : transfer energy to specific side
	 * @param type : send to something is 1 and send out itself is 0
	 */
	public void doCustomSidedPowerTransfer(int side,int type){
		TileEntity tile=worldObj.getTileEntity(SideHelper.offset(side,pos));
		TransferEnergyToPosition(SideHelper.offset(side,pos), type,side);
	}
	/**
	 * This gets coordinates from given side and if there is a pipe it searches for pipes that are connected to that found pipe and returns a random one if there is more than 1 pipe
	 * @param side
	 * @return TileEntityPow that is pipe
	 */
	public TileEntityFirePipe getPipeNextToAnotherPipe(int side){
		TileEntityFirePipe result=null;
		BlockPos pos1=SideHelper.offset(side,pos);
		TileEntity firstPipe=worldObj.getTileEntity(pos1);
		int[] randSides=SideHelper.randomizeSides();
		if(firstPipe instanceof TileEntityFirePipe)
		for(int a=0;a<6;a++){int rSide=randSides[a];if(rSide!=SideHelper.getOppositeSide(side)&&connections[side]!=null){
			BlockPos pos2=SideHelper.offset(rSide,pos1);
			TileEntity tile=worldObj.getTileEntity(pos);
			if(tile instanceof TileEntityFirePipe&&((TileEntityFirePipe) firstPipe).connections[rSide]!=null){
				result=(TileEntityFirePipe)tile;
				a=6;
			}
		}}
		return result;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param type : send to something is 1 and send out itself is 0
	 * @param side
	 */
	public void TransferEnergyToPosition(BlockPos pos,int type, int side){
		TileEntity tileEn=worldObj.getTileEntity(pos);
		/**Power visual debug (shows path-finding in particles)*/
//		if(Helper.RInt(25)==0)Helper.spawnEntityFX(new EntityMovingParticleFX(getWorldObj(), pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5,tileEn.pos.getX()+0.5, tileEn.pos.getY()+0.5, tileEn.pos.getZ()+0.5, 300, type==1?1:0.2, type==0?1:0.2, type==-1?1:0.2,0.4));
		if(type==-1&&tileEn instanceof TileEntityFirePipe){
			for(int l=0;l<20;l++){
				PowerHelper.tryToEquateEnergy(this, tileEn, PowerHelper.getMaxSpeed(tileEn, this),side);
				PowerHelper.tryToEquateEnergy(this, tileEn, PowerHelper.getMiddleSpeed(tileEn, this),side);
				PowerHelper.tryToEquateEnergy(this, tileEn, PowerHelper.getMinSpeed(tileEn, this),side);
			}
		}
		else if(tileEn instanceof TileEntityPow){
			switch(type){
			case 0:{
				side=SideHelper.getOppositeSide(side);
				for(int l=0;l<20;l++){
					PowerHelper.tryToDrainFromTo(tileEn, this, PowerHelper.getMaxSpeed(tileEn, this),side);
					PowerHelper.tryToDrainFromTo(tileEn, this, PowerHelper.getMiddleSpeed(tileEn, this),side);
					PowerHelper.tryToDrainFromTo(tileEn, this, PowerHelper.getMinSpeed(tileEn, this),side);
				}
			}break;
			case 1:{
				for(int l=0;l<20;l++){
					PowerHelper.tryToDrainFromTo(this, tileEn, PowerHelper.getMaxSpeed(tileEn, this),side);
					PowerHelper.tryToDrainFromTo(this, tileEn, PowerHelper.getMiddleSpeed(tileEn, this),side);
					PowerHelper.tryToDrainFromTo(this, tileEn, PowerHelper.getMinSpeed(tileEn, this),side);
				}
			}break;
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 40096.0D;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        AxisAlignedBB bb = new AxisAlignedBB(pos.getX(), pos.getY()-(DCFFL!=null?0.5:0), pos.getZ(), pos.getX()+1, pos.getY()+1, pos.getZ()+1);
        return bb;
    }
	
	@Override
	public void setColisionBoxes(){
		collisionBoxes=new AxisAlignedBB[]{
				connections[5]!=null?getExpectedColisionBoxes()[0]:null,
				connections[1]!=null?getExpectedColisionBoxes()[1]:null,
				connections[2]!=null?getExpectedColisionBoxes()[2]:null,
				connections[3]!=null?getExpectedColisionBoxes()[3]:null,
				connections[0]!=null?getExpectedColisionBoxes()[4]:null,
				connections[4]!=null?getExpectedColisionBoxes()[5]:null,
									 getExpectedColisionBoxes()[6],
				DCFFL!=null?         getExpectedColisionBoxes()[7]:null,
				DCFFL!=null?         getExpectedColisionBoxes()[8]:null,
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
		if(!H.isRemote(this))return;
		if(Helper.AxisAlignedBBEqual(pointId,prevPointId))return;
		Helper.sendMessage(new NotifyPointedBoxChangePacket(this));
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
	public void getValidTileEntitys(List<Class> included, List<Class> excluded){
		
	}


	@Override
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array, int side){
		return false;
	}


	@Override
	public void getBoxesOnSide(List<AxisAlignedBB> result, int side){
		switch(side){
		case 0:if(connections[side]!=null){
			result.add(collisionBoxes[4]);
		}break;
		case 1:if(connections[side]!=null){
			if(DCFFL==null)result.add(collisionBoxes[1]);
			else result.add(collisionBoxes[8]);
			result.add(collisionBoxes[7]);
		}break;
		case 2:if(connections[side]!=null){
			result.add(collisionBoxes[2]);
		}break;
		case 3:if(connections[side]!=null){
			result.add(collisionBoxes[3]);
		}break;
		case 4:if(connections[side]!=null){
			result.add(collisionBoxes[5]);
		}break;
		case 5:if(connections[side]!=null){
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
}
