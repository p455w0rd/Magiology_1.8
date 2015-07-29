package com.magiology.mcobjects.tileentityes;

import java.util.List;

import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityFirePipe extends TileEntityPow implements MultiColisionProvider,UpdateablePipe{
	PowerHelper PH=new PowerHelper();
	SlowdownHelper optimizer=new SlowdownHelper(40);
	SlowdownHelper optimizer2=new SlowdownHelper(6);
	
	public ForgeDirection[] connections = new ForgeDirection[6];
	public boolean[] shouldConnectionsBeRendered=new boolean[6];
	public ForgeDirection[] connectionsToObjInMe = new ForgeDirection[6];
	public ForgeDirection[] connectionsToObjOut = new ForgeDirection[6];
	public ForgeDirection[] strateConnection = new ForgeDirection[3];
	public ForgeDirection DCFFL=ForgeDirection.UP;
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
    		connections[i]=ForgeDirection.getOrientation(NBTTC.getInteger("connections"+i));
    		if(connections[i]==ForgeDirection.UNKNOWN)connections[i]=null;
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
	public void updateEntity(){
		super.updateEntity();
		power(true);
		
		//Get if/what side is first
		hasPriorityUpg=false;
		for(int i=0;i<containerItems.length;i++)if(containerItems[i]!=null&&containerItems[i].hasTagCompound()){
			UpgradeType type=RegisterUpgrades.getItemUpgradeType(RegisterUpgrades.getItemUpgradeID(containerItems[i].getItem()));
			if(type==UpgradeType.Priority){
				hasPriorityUpg=true;
				FirstSide=containerItems[i].stackTagCompound.getInteger("side");
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
//			Helper.spawnEnitiyFX(new EntitySmokeFX(worldObj, xCoord+0.5, yCoord+0.5, zCoord+0.5, 0, 0.2, 0));
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
				if(Helper.RB(0.33)&&isSolidDown==false&&connections[1]==null)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+0.5, yCoord+0.5-p*2, zCoord+0.5, 0, -0.1, 0));
				if(Helper.RB(0.33)&&isSolidUp==false&&connections[0]==null)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+0.5, yCoord+0.5+p*2, zCoord+0.5, 0, 0.05, 0));
				if(Helper.RB(0.33)&&connections[2]==null)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+0.5, yCoord+0.5, zCoord+0.5-p*2, 0, 0, -0.1));
				if(Helper.RB(0.33)&&connections[4]==null)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+0.5, yCoord+0.5, zCoord+0.5+p*2, 0, 0, 0.1));
				if(Helper.RB(0.33)&&connections[3]==null)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+0.5+p*2, yCoord+0.5, zCoord+0.5, 0.1, 0, 0));
				if(Helper.RB(0.33)&&connections[5]==null)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, xCoord+0.5-p*2, yCoord+0.5, zCoord+0.5, -0.1, 0, 0));
			}
		}
	}

	public void updatestand(){
		if(worldObj.isSideSolid(xCoord, yCoord-1, zCoord, ForgeDirection.DOWN)==true&&connections[1]==null)isSolidDown=true;
		else isSolidDown=false;
		
		if(worldObj.isSideSolid(xCoord, yCoord+1, zCoord, ForgeDirection.UP)==true&&connections[0]==null)isSolidUp=true;
		else isSolidUp=false;
	}
	@Override
	public void updateConnections(){
		if(H.isRemote(worldObj))updatestand();
		if(isTPipe(0)||isTRand(SideHelper.X(0, xCoord), SideHelper.Y(0, yCoord), SideHelper.Z(0, zCoord))) connections[0] = ForgeDirection.UP;else connections[0] = null;
		if(isTPipe(1)||isTRand(SideHelper.X(1, xCoord), SideHelper.Y(1, yCoord), SideHelper.Z(1, zCoord))||(worldObj.getTileEntity(SideHelper.X(1, xCoord), SideHelper.Y(1, yCoord), SideHelper.Z(1, zCoord))instanceof TileEntityFireExhaust)) connections[1] = ForgeDirection.DOWN;else connections[1] = null;
		if(isTPipe(2)||isTRand(SideHelper.X(2, xCoord), SideHelper.Y(2, yCoord), SideHelper.Z(2, zCoord))) connections[2] = ForgeDirection.NORTH;else connections[2] = null;
		if(isTPipe(3)||isTRand(SideHelper.X(3, xCoord), SideHelper.Y(3, yCoord), SideHelper.Z(3, zCoord))) connections[3] = ForgeDirection.EAST;else connections[3] = null;
		if(isTPipe(4)||isTRand(SideHelper.X(4, xCoord), SideHelper.Y(4, yCoord), SideHelper.Z(4, zCoord))) connections[4] = ForgeDirection.SOUTH;else connections[4] = null;
		if(isTPipe(5)||isTRand(SideHelper.X(5, xCoord), SideHelper.Y(5, yCoord), SideHelper.Z(5, zCoord))) connections[5] = ForgeDirection.WEST;else connections[5] = null;
		boolean[] in1={},out1={};
		TileEntity[] tiles=new TileEntity[6];
		for(int a=0;a<6;a++){
			tiles[a]=worldObj.getTileEntity(SideHelper.X(a, xCoord), SideHelper.Y(a, yCoord), SideHelper.Z(a, zCoord));
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
		if(in1[0]&&!isTPipe(0))connectionsToObjInMe[0]=ForgeDirection.UP;   else connectionsToObjInMe[0]=null;
		if(in1[1]&&!isTPipe(1))connectionsToObjInMe[1]=ForgeDirection.DOWN; else connectionsToObjInMe[1]=null;
		if(in1[2]&&!isTPipe(2))connectionsToObjInMe[2]=ForgeDirection.NORTH;else connectionsToObjInMe[2]=null;
		if(in1[3]&&!isTPipe(3))connectionsToObjInMe[3]=ForgeDirection.EAST; else connectionsToObjInMe[3]=null;
		if(in1[4]&&!isTPipe(4))connectionsToObjInMe[4]=ForgeDirection.SOUTH;else connectionsToObjInMe[4]=null;
		if(in1[5]&&!isTPipe(5))connectionsToObjInMe[5]=ForgeDirection.WEST; else connectionsToObjInMe[5]=null;
			
		if(out1[0]&&!isTPipe(0))connectionsToObjOut[0]=ForgeDirection.UP;   else connectionsToObjOut[0]=null;
		if(out1[1]&&!isTPipe(1))connectionsToObjOut[1]=ForgeDirection.DOWN; else connectionsToObjOut[1]=null;
		if(out1[2]&&!isTPipe(2))connectionsToObjOut[2]=ForgeDirection.NORTH;else connectionsToObjOut[2]=null;
		if(out1[3]&&!isTPipe(3))connectionsToObjOut[3]=ForgeDirection.EAST; else connectionsToObjOut[3]=null;
		if(out1[4]&&!isTPipe(4))connectionsToObjOut[4]=ForgeDirection.SOUTH;else connectionsToObjOut[4]=null;
		if(out1[5]&&!isTPipe(5))connectionsToObjOut[5]=ForgeDirection.WEST; else connectionsToObjOut[5]=null;
		
//		Helper.printInln(worldObj.getTileEntity(SideHelper.X(SideHelper.WEST(), xCoord), SideHelper.Y(SideHelper.WEST(), yCoord), SideHelper.Z(SideHelper.WEST(), zCoord)));
		
		for(int a=0;a<6;a++){
			shouldConnectionsBeRendered[a]=true;
			if(connectionsToObjInMe[a]!=null)connections[a]=connectionsToObjInMe[a];
			else if(connectionsToObjOut[a]!=null)connections[a]=connectionsToObjOut[a];
		}
		
		if(isTLamp(xCoord, yCoord-1, zCoord)&&connections[1]!=null){
			DCFFL=ForgeDirection.UP;
		}else DCFFL = null;
		
		for(int a=0;a<6;a++){
			if(bannedConnections[a]==true){
				connections[a]=null;
				connectionsToObjInMe[a]=null;
				connectionsToObjOut[a]=null;
			}
		}
		if(((connections[0]!=null&&DCFFL==null)&&connections[1]!=null)&&(connections[2]==null&&connections[3]==null&&connections[4]==null&&connections[5]==null))strateConnection[0] = ForgeDirection.UP;else strateConnection[0] = null;
		if((connections[3]!=null&&connections[5]!= null)&&(connections[0]==null&&(connections[1]==null&&DCFFL==null)&&connections[2]==null&&connections[4]==null))strateConnection[1] = ForgeDirection.WEST;else strateConnection[1] = null;
		if((connections[2]!=null&&connections[4]!= null)&&(connections[0]==null&&(connections[1]==null&&DCFFL==null)&&connections[3]==null&&connections[5]==null))strateConnection[2] = ForgeDirection.SOUTH;else strateConnection[2] = null;
		
		if(DCFFL!=null){
			connectionsToObjInMe[1] = ForgeDirection.DOWN;
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
	
	public boolean isTOSO(int x1, int y1, int z1){
		return worldObj.getTileEntity(x1, y1, z1)instanceof TileEntityOreStructureCore;
	}
	
	public boolean isTPipe(int side){
		int x=SideHelper.X(side,xCoord),y=SideHelper.Y(side,yCoord),z=SideHelper.Z(side,zCoord),dir=0;
		switch(side){
		case 0:{dir=1;}break;
		case 1:{dir=0;}break;
		case 2:{dir=4;}break;
		case 3:{dir=5;}break;
		case 4:{dir=2;}break;
		case 5:{dir=3;}break;
		}
		boolean return1=false;
		TileEntity tile=worldObj.getTileEntity(x, y, z);
		if(tile instanceof TileEntityFirePipe){
			if(((TileEntityFirePipe)tile).bannedConnections[dir]==false){
				return1=true;
			}
		}
		
		return return1;
	}
	public boolean isTLamp(int x1, int y1, int z1){
		return worldObj.getTileEntity(x1, y1, z1)instanceof TileEntityFireLamp;
	}
	public boolean isTRand(int x1, int y1, int z1){
		boolean is=false;
		TileEntity tile1=worldObj.getTileEntity(x1, y1, z1);
		if(tile1 instanceof TileEntityBFCPowerOut)is=true;
		
		return is;
	}
	public void power(boolean isRepeatable){
		doSidedPowerTransferBasedOnConections(isRepeatable);
		if(Helper.RInt(5)==0){
			int side=Helper.RInt(6);
			TileEntity tile=worldObj.getTileEntity(SideHelper.X(side, xCoord),SideHelper.Y(side, yCoord),SideHelper.Z(side, zCoord));
			
			if(connections[side]!=null&&connectionsToObjInMe[side]==null&&connectionsToObjOut[side]==null&&tile instanceof TileEntityFirePipe&&getCurrentEnergy()>0&&((TileEntityFirePipe)tile).getCurrentEnergy()<((TileEntityFirePipe)tile).getMaxEnergyBuffer()-1){
				PowerHelper.tryToDrainFromTo(this, tile, 1,side);
			}
			
		}
//		if(isRepeatable){
//
//		}
//		else worldObj.spawnParticle("flame", xCoord+0.5, yCoord+0.5, zCoord+0.5, 0.1, 0.1, 0.1);
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
			TileEntity ab=worldObj.getTileEntity(SideHelper.X(side,xCoord), SideHelper.Y(side,yCoord), SideHelper.Z(side,zCoord));
			//if there is nothing to interact with than skip the process (only for optimization)
			boolean var1=ab!=null&&ab instanceof PowerCore&&ab instanceof ISidedPower;
			//Is next to a special pipe
			boolean var2=false;
			if(var1){
				//special interaction for pipes that contains Priority upgrade
				if(hasPriorityUpg){
					TileEntity a=worldObj.getTileEntity(SideHelper.X(randSides[0],xCoord), SideHelper.Y(randSides[0],yCoord), SideHelper.Z(randSides[0],zCoord));
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
						if(SideHelper.X(tile.FirstSide,tile.xCoord)==xCoord&&
						   SideHelper.Y(tile.FirstSide,tile.yCoord)==yCoord&&
						   SideHelper.Z(tile.FirstSide,tile.zCoord)==zCoord){
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
						TileEntity tile=worldObj.getTileEntity(SideHelper.X(side,xCoord), SideHelper.Y(side,yCoord), SideHelper.Z(side,zCoord));
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
		TileEntity tile=worldObj.getTileEntity(SideHelper.X(side,xCoord), SideHelper.Y(side,yCoord), SideHelper.Z(side,zCoord));
		TransferEnergyToPosition(SideHelper.X(side,xCoord), SideHelper.Y(side,yCoord), SideHelper.Z(side,zCoord), type,side);
	}
	/**
	 * This gets coordinates from given side and if there is a pipe it searches for pipes that are connected to that found pipe and returns a random one if there is more than 1 pipe
	 * @param side
	 * @return TileEntityPow that is pipe
	 */
	public TileEntityFirePipe getPipeNextToAnotherPipe(int side){
		TileEntityFirePipe result=null;
		int x1=SideHelper.X(side,xCoord), y1=SideHelper.Y(side,yCoord), z1=SideHelper.Z(side,zCoord);
		TileEntity firstPipe=worldObj.getTileEntity(x1,y1,z1);
		int[] randSides=SideHelper.randomizeSides();
		if(firstPipe instanceof TileEntityFirePipe)
		for(int a=0;a<6;a++){int rSide=randSides[a];if(rSide!=SideHelper.getOppositeSide(side)&&connections[side]!=null){
			int x2=SideHelper.X(rSide,x1), y2=SideHelper.Y(rSide,y1), z2=SideHelper.Z(rSide,z1);
			TileEntity tile=worldObj.getTileEntity(x2,y2,z2);
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
	public void TransferEnergyToPosition(int x,int y,int z,int type, int side){
		TileEntity tileEn=worldObj.getTileEntity(x, y, z);
		/**Power visual debug (shows path-finding in particles)*/
//		if(Helper.RInt(25)==0)Helper.spawnEntityFX(new EntityMovingParticleFX(getWorldObj(), xCoord+0.5, yCoord+0.5, zCoord+0.5,tileEn.xCoord+0.5, tileEn.yCoord+0.5, tileEn.zCoord+0.5, 300, type==1?1:0.2, type==0?1:0.2, type==-1?1:0.2,0.4));
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
        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(xCoord, yCoord-(DCFFL!=null?0.5:0), zCoord, xCoord+1, yCoord+1, zCoord+1);
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
			Helper.AxisAlignedBB(0      ,p*6,p*6,p*6,p*10,p*10),
			Helper.AxisAlignedBB(p*6,0      ,p*6,p*10,p*6,p*10),
			Helper.AxisAlignedBB(p*6,p*6,0      ,p*10,p*10,p*6),
			Helper.AxisAlignedBB(p*10,p*6,p*6,1      ,p*10,p*10),
			Helper.AxisAlignedBB(p*6,p*10,p*6,p*10,1      ,p*10),
			Helper.AxisAlignedBB(p*6,p*6,p*10,p*10,p*10,1      ),
			Helper.AxisAlignedBB(p*6, p*6, p*6, p*10, p*10, p*10),
			Helper.AxisAlignedBB(p*4.5F,-p*4.7F,p*4.5F,p*11.5F,p*0.1F,p*11.5F),
			Helper.AxisAlignedBB(p*6,0 ,p*6,p*10,p*6F,p*10),
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
