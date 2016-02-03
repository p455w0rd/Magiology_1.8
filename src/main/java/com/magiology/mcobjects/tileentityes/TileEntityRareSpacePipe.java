package com.magiology.mcobjects.tileentityes;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.forgepowered.events.ForcePipeUpdate;
import com.magiology.forgepowered.packets.packets.NotifyPointedBoxChangePacket;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityConnectionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityRareSpacePipe extends TileEntityConnectionProvider implements MultiColisionProvider,ITickable{
	
	public EnumFacing connections[] = new EnumFacing[6],
		connectionsToObjInMe[] = new EnumFacing[6],
		connectionsToObjOut[] = new EnumFacing[6],
		strateConnection[] = new EnumFacing[3];
	public boolean bannedConnections[] = new boolean[6];
	public AxisAlignedBB 
		pointId,
		prevPointId,
		collisionBoxes[];
	
	public TileEntityRareSpacePipe(){
		setColisionBoxes();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound NBTTC){
		
		for(int i=0;i<6;i++){
			bannedConnections[i]=NBTTC.getBoolean("bannedConnections"+i);
			int con=NBTTC.getInteger("connections"+i);
			if(con<6&&con>-1)connections[i]=EnumFacing.getFront(con);
			else connections[i]=null;
		}
		setColisionBoxes();
		ForcePipeUpdate.updatein3by3(worldObj, pos);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound NBTTC){
		for (int i=0;i<6;i++){
			NBTTC.setBoolean("bannedConnections"+i, bannedConnections[i]);
			NBTTC.setInteger("connections"+i, SideUtil.enumFacingOrientation(connections[i]));
		}
	}
	
	@Override
	public void update(){
		updateConnections();
	}
	
	@Override
	public void updateConnections(){
		if(!hasWorldObj())return;
		UpdateableTile.UpdateablePipeHandler.setConnections(connections, this);
		setColisionBoxes();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		AxisAlignedBB bb = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY()+1, pos.getZ()+1);
		return bb;
	}
	
	@Override
	public void setColisionBoxes(){
		collisionBoxes=new AxisAlignedBB[]{
				connections[5]!=null?getExpectedColisionBoxes()[3]:null,
				connections[1]!=null?getExpectedColisionBoxes()[4]:null,
				connections[2]!=null?getExpectedColisionBoxes()[2]:null,
				connections[3]!=null?getExpectedColisionBoxes()[5]:null,
				connections[0]!=null?getExpectedColisionBoxes()[1]:null,
				connections[4]!=null?getExpectedColisionBoxes()[0]:null,
									 getExpectedColisionBoxes()[6]
		};
	}
	private AxisAlignedBB[] expectedBoxes=new AxisAlignedBB[]{
			new AxisAlignedBB(0	  ,p*6,p*6,p*6,p*10,p*10),
			new AxisAlignedBB(p*6,0	  ,p*6,p*10,p*6,p*10),
			new AxisAlignedBB(p*6,p*6,0	  ,p*10,p*10,p*6),
			new AxisAlignedBB(p*10,p*6,p*6,1	  ,p*10,p*10),
			new AxisAlignedBB(p*6,p*10,p*6,p*10,1	  ,p*10),
			new AxisAlignedBB(p*6,p*6,p*10,p*10,p*10,1	  ),
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
		if(UtilM.AxisAlignedBBEqual(pointId,prevPointId))return;
		UtilM.sendMessage(new NotifyPointedBoxChangePacket(this));
	}
	
	@Override
	public AxisAlignedBB[] getActiveBoxes(){
		AxisAlignedBB[] Result={};
		if(collisionBoxes!=null)
		for(int i=0;i<collisionBoxes.length;i++)if(collisionBoxes[i]!=null)Result=ArrayUtils.add(Result, collisionBoxes[i]);
		return Result;
	}


	@Override
	public void getValidTileEntitys(List<Class> included, List<Class> excluded){
		included.add(IInventory.class);
		included.add(TileEntityRareSpacePipe.class);
	}
	
	@Override
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array, int side){
		if(tile instanceof ISidedInventory){
			return ((ISidedInventory)tile).getSlotsForFace(EnumFacing.getFront(side))!=null&&((ISidedInventory)tile).getSlotsForFace(EnumFacing.getFront(side)).length>0;
//			return false;
		}
		return true;
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
