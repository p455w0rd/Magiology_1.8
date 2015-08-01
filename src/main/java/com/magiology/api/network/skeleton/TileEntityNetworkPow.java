package com.magiology.api.network.skeleton;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.forgepowered.packets.NotifyPointedBoxChangePacket;
import com.magiology.mcobjects.blocks.BlockContainerMultiColision;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateablePipe;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;
import com.magiology.objhelper.helpers.SideHelper;

public abstract class TileEntityNetworkPow extends TileEntityPow implements MultiColisionProvider,ISidedNetworkComponent,UpdateablePipe{
	public EnumFacing[] connections = new EnumFacing[6];
	public EnumFacing[] strateConnection = new EnumFacing[3];
	public boolean[] bannedConnections = new boolean[6],accessibleSides={true,true,true,true,true,true};
	public boolean canPathFindTheBrain,didCheckSides=false;
	
	public AxisAlignedBB pointId,prevPointId;
	public AxisAlignedBB[] collisionBoxes=null;

	protected int[] brainLoadup={0,0,0,1};
	
	public TileEntityNetworkPow(boolean[] allowedSidedPower,boolean[] sidedPower,int minTSpeed, int middleTSpeed, int maxTSpeed, int maxEnergyBuffer){
		super(allowedSidedPower, sidedPower, minTSpeed, middleTSpeed, maxTSpeed, maxEnergyBuffer);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		brainLoadup=ArrayUtils.add(read3I(NBT, "brainP"),-1);
		canPathFindTheBrain=NBT.getBoolean("brainPath");
	}
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		write3I(NBT, brainLoadup[0], brainLoadup[1], brainLoadup[2], "brainP");
		NBT.setBoolean("brainPath", canPathFindTheBrain);
	}
	public void checkBrainConnection(){
		canPathFindTheBrain=getBrain()!=null?getBrain().isInNetwork(this):false;
	}
	public void findBrain(){
		int side=-1;TileEntity test=null;
		for(int i=0;i<this.connections.length;i++)if(this.connections[i]!=null&&
				(test=worldObj.getTileEntity(SideHelper.offset(i, xCoord), SideHelper.Y(i, yCoord), SideHelper.Z(i, zCoord)))instanceof ISidedNetworkComponent&&((ISidedNetworkComponent)test).getBrain()!=null){
			side=i;i=this.connections.length;
		}
		if(side!=-1){
			ISidedNetworkComponent component=(ISidedNetworkComponent) worldObj.getTileEntity(SideHelper.offset(side, xCoord), SideHelper.Y(side, yCoord), SideHelper.Z(side, zCoord));
			if(component!=null)NetworkBaseComponentHandeler.setBrain(component.getBrain(), this);
		}
	}

	protected boolean isInitialized=false;
	public abstract void initNetworkComponent();
	@Override
	public TileEntity getHost(){
		return this;
	}
	@Override
	public boolean isInitialized(){
		return isInitialized;
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
	public long getNetworkId(){
		return getBrain().getNetworkId();
	}
	@Override
	public boolean getAccessibleOnSide(int side){
		return connections[side]!=null;
	}
	@Override
	public int getOrientation(){
		return worldObj.getBlockMetadata(pos);
	}
	@Override
	public void initTheComponent(){
		isInitialized=true;
		initNetworkComponent();
		setColisionBoxes();
	}
	@Override
	public void setAccessibleOnSide(int side, boolean accessible){
		accessibleSides[side]=accessible;}
	@Override
	public void setOrientation(int orientation){
		worldObj.setBlockMetadataWithNotify(pos, orientation, 0);
	}
	@Override
	public void setBrain(TileEntityNetworkController brain){
		if(brain!=null){
			brainLoadup[0]=brain.xCoord;
			brainLoadup[1]=brain.yCoord;
			brainLoadup[2]=brain.zCoord;
		}
	}
	@Override
	public TileEntityNetworkController getBrain(){
		if(!canPathFindTheBrain)return null;
		TileEntity tile=worldObj.getTileEntity(brainLoadup[0], brainLoadup[1], brainLoadup[2]);
		if(!(tile instanceof TileEntityNetworkController))return null;
		return (TileEntityNetworkController)tile;
	}
	@Override
	public AxisAlignedBB[] getActiveBoxes(){
		AxisAlignedBB[] Result={};
		if(collisionBoxes!=null)for(int i=0;i<collisionBoxes.length;i++){
			if(collisionBoxes[i]!=null)Result=ArrayUtils.add(Result, collisionBoxes[i]);
		}else updateConnections();
		return Result;
	}
	@Override
	public boolean isSideEnabled(int side){
		return accessibleSides[side];
	}
	@Override
	public AxisAlignedBB getRenderBoundingBox(){
		if(blockType instanceof BlockContainerMultiColision){
			AxisAlignedBB bb=((BlockContainerMultiColision)blockType).getResetBoundsOptional(worldObj, pos);
			if(bb!=null)return bb.addCoord(pos);
		}
		return super.getRenderBoundingBox();
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
				                     getExpectedColisionBoxes()[6]
		};
	}
	
	@Override
	public void getBoxesOnSide(List<AxisAlignedBB> result, int side){
		switch(side){
		case 0:if(connections[side]!=null){
			result.add(collisionBoxes[4]);
		}break;
		case 1:if(connections[side]!=null){
			result.add(collisionBoxes[1]);
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
		case 1:result.add(getExpectedColisionBoxes()[1]);break;
		case 2:result.add(getExpectedColisionBoxes()[2]);break;
		case 3:result.add(getExpectedColisionBoxes()[3]);break;
		case 4:result.add(getExpectedColisionBoxes()[5]);break;
		case 5:result.add(getExpectedColisionBoxes()[0]);break;
		}
	}
	protected AxisAlignedBB[] expectedBoxes=new AxisAlignedBB[]{
			new AxisAlignedBB(0,   p*6, p*6, p*6,  p*10, p*10),
			new AxisAlignedBB(p*6, 0,   p*6, p*10, p*6,  p*10),
			new AxisAlignedBB(p*6, p*6, 0,   p*10, p*10, p*6 ),
			new AxisAlignedBB(p*10,p*6, p*6, 1,    p*10, p*10),
			new AxisAlignedBB(p*6, p*10,p*6, p*10, 1,    p*10),
			new AxisAlignedBB(p*6, p*6, p*10,p*10, p*10, 1   ),
			new AxisAlignedBB(p*6, p*6, p*6, p*10, p*10, p*10)
	};
	@Override
	public AxisAlignedBB[] getExpectedColisionBoxes(){
		return expectedBoxes;
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
