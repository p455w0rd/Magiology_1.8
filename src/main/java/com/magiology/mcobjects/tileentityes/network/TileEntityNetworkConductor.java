package com.magiology.mcobjects.tileentityes.network;

import java.util.List;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.skeleton.TileEntityNetwork;
import com.magiology.forgepowered.event.ForcePipeUpdate;
import com.magiology.objhelper.SlowdownHelper;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.NetworkHelper;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityNetworkConductor extends TileEntityNetwork{
	
	private SlowdownHelper optimizer=new SlowdownHelper(40);
	
	public TileEntityNetworkConductor(){}
	
	@Override
	public void updateEntity(){
		if(getBrain()==null){
			findBrain();
			ForcePipeUpdate.updatePipe(worldObj, xCoord, yCoord, zCoord);
		}
		checkBrainConnection();
		if(optimizer.isTimeWithAddProgress())updateConnections();
	}
	@Override
	public void initNetworkComponent(){}
	@Override
	public void updateConnections(){
		UpdateablePipeHandeler.setConnections(connections, this);
		setColisionBoxes();
	}
	@Override
	public void setColisionBoxes(){
		if(collisionBoxes==null)collisionBoxes=new AxisAlignedBB[7];
		collisionBoxes[0]=connections[5]!=null?Helper.AxisAlignedBB(0,   p*6, p*6, p*6,  p*10, p*10):null;
		collisionBoxes[1]=connections[1]!=null?Helper.AxisAlignedBB(p*6, 0,   p*6, p*10, p*6,  p*10):null;
		collisionBoxes[2]=connections[2]!=null?Helper.AxisAlignedBB(p*6, p*6, 0,   p*10, p*10, p*6 ):null;
		collisionBoxes[3]=connections[3]!=null?Helper.AxisAlignedBB(p*10,p*6, p*6, 1,    p*10, p*10):null;
		collisionBoxes[4]=connections[0]!=null?Helper.AxisAlignedBB(p*6, p*10,p*6, p*10, 1,    p*10):null;
		collisionBoxes[5]=connections[4]!=null?Helper.AxisAlignedBB(p*6, p*6, p*10,p*10, p*10, 1   ):null;
		collisionBoxes[6]=                     Helper.AxisAlignedBB(p*6, p*6, p*6, p*10, p*10, p*10);
	}
	@Override
	public void getValidTileEntitys(List<Class> included, List<Class> excluded){
		included.add(ISidedNetworkComponent.class);
	}
	@Override
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array, int side){
		return NetworkHelper.canConnect(this, (ISidedNetworkComponent)tile);
	}
}
