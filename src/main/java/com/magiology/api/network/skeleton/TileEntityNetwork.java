package com.magiology.api.network.skeleton;

import java.util.*;

import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;

import org.apache.commons.lang3.*;

import com.magiology.api.connection.*;
import com.magiology.api.network.*;
import com.magiology.forgepowered.packets.packets.*;
import com.magiology.mcobjects.blocks.*;
import com.magiology.mcobjects.tileentityes.corecomponents.*;
import com.magiology.mcobjects.tileentityes.network.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.m_extension.*;

public abstract class TileEntityNetwork extends TileEntityM implements MultiColisionProvider,ISidedNetworkComponent,UpdateableTile{
	public boolean[] accessibleSides={true,true,true,true,true,true};
	public boolean canPathFindTheBrain,didCheckSides=false;
	
	public IConnection[] connections=IConnectionFactory.NewArray(this, ConnectionType.Energy);
	
	public AxisAlignedBB pointId,prevPointId;
	public AxisAlignedBB[] collisionBoxes=null;

	protected BlockPos brainLoadup=new BlockPos(new Vec3(0,0,0));
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		brainLoadup=readPos(NBT, "brainP");
		canPathFindTheBrain=NBT.getBoolean("brainPath");
	}
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		writePos(NBT, brainLoadup, "brainP");
		NBT.setBoolean("brainPath", canPathFindTheBrain);
	}
	public void checkBrainConnection(){
		if(getBrain()!=null)canPathFindTheBrain=getBrain().isInNetwork(this);
		if(!canPathFindTheBrain)for(int i=0;i<6;i++)if(getAccessibleOnSide(i)){
			TileEntity test=worldObj.getTileEntity(SideUtil.offsetNew(i, pos));
			if(test instanceof ISidedNetworkComponent){
				ISidedNetworkComponent t=(ISidedNetworkComponent)test;
				if(t.getBrain()!=null){
					setBrain(t.getBrain());
					canPathFindTheBrain=true;
					getBrain().restartNetwork();
				}
			}
		}
		if(getBrain()!=null)canPathFindTheBrain=getBrain().isInNetwork(this);
	}
	public void findBrain(){
		int side=-1;TileEntity test=null;
		for(int i=0;i<connections.length;i++)if(this.connections[i].getMain()&&
				(test=worldObj.getTileEntity(SideUtil.offsetNew(i, getPos())))instanceof ISidedNetworkComponent&&((ISidedNetworkComponent)test).getBrain()!=null){
			side=i;i=this.connections.length;
		}
		if(side!=-1){
			ISidedNetworkComponent component=(ISidedNetworkComponent) worldObj.getTileEntity(SideUtil.offsetNew(side, pos));
			if(component!=null)NetworkBaseComponentHandler.setBrain(component.getBrain(), this);
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
		if(!U.isRemote(this))return;
		if(UtilM.AxisAlignedBBEqual(pointId,prevPointId))return;
		UtilM.sendMessage(new NotifyPointedBoxChangePacket(this));
	}
	@Override
	public long getNetworkId(){
		return getBrain().getNetworkId();
	}
	@Override
	public boolean getAccessibleOnSide(int side){
		return connections[side].getMain()&&accessibleSides[side];
	}
	@Override
	public int getOrientation(){
		return hasWorldObj()?UtilM.getBlockMetadata(worldObj,pos):-1;
	}
	@Override
	public void initTheComponent(){
		isInitialized=true;initNetworkComponent();setColisionBoxes();
	}
	@Override
	public void setAccessibleOnSide(int side, boolean accessible){
		accessibleSides[side]=accessible;
	}
	@Override
	public void setOrientation(int orientation){
		UtilM.setMetadata(worldObj, pos, orientation);
	}
	@Override
	public void setBrain(TileEntityNetworkController brain){
		if(brain!=null)brainLoadup=brain.getPos().add(0, 0, 0);
	}
	@Override
	public TileEntityNetworkController getBrain(){
		if(!canPathFindTheBrain)return null;
		TileEntity tile=worldObj.getTileEntity(brainLoadup);
		if(!(tile instanceof TileEntityNetworkController))return null;
		return (TileEntityNetworkController)tile;
	}
	@Override public AxisAlignedBB[] getActiveBoxes(){
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
			if(bb!=null)return bb.addCoord(pos.getX(),pos.getY(),pos.getZ());
		}
		return super.getRenderBoundingBox();
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
				                     getExpectedColisionBoxes()[6]
		};
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
	public void getBoxesOnSide(List<AxisAlignedBB> result, int side){
		switch(side){
		case 0:if(connections[side].getMain()){
			result.add(collisionBoxes[4]);
		}break;
		case 1:if(connections[side].getMain()){
			result.add(collisionBoxes[1]);
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
		case 0:{
			result.add(getExpectedColisionBoxes()[4]);
		}break;
		case 1:{
			result.add(getExpectedColisionBoxes()[1]);
		}break;
		case 2:{
			result.add(getExpectedColisionBoxes()[2]);
		}break;
		case 3:{
			result.add(getExpectedColisionBoxes()[3]);
		}break;
		case 4:{
			result.add(getExpectedColisionBoxes()[5]);
		}break;
		case 5:{
			result.add(getExpectedColisionBoxes()[0]);
		}break;
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
