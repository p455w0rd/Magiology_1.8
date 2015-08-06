package com.magiology.mcobjects.tileentityes.network;

import java.util.List;

import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.skeleton.TileEntityNetwork;
import com.magiology.forgepowered.event.ForcePipeUpdate;
import com.magiology.objhelper.SlowdownHelper;
import com.magiology.objhelper.helpers.NetworkHelper;

public class TileEntityNetworkConductor extends TileEntityNetwork implements IUpdatePlayerListBox{
	
	private SlowdownHelper optimizer=new SlowdownHelper(40);
	
	public TileEntityNetworkConductor(){}
	
	@Override
	public void update(){
		if(getBrain()==null){
			findBrain();
			ForcePipeUpdate.updatePipe(worldObj, pos);
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
		collisionBoxes[0]=connections[5]!=null?new AxisAlignedBB(0,   p*6, p*6, p*6,  p*10, p*10):null;
		collisionBoxes[1]=connections[1]!=null?new AxisAlignedBB(p*6, 0,   p*6, p*10, p*6,  p*10):null;
		collisionBoxes[2]=connections[2]!=null?new AxisAlignedBB(p*6, p*6, 0,   p*10, p*10, p*6 ):null;
		collisionBoxes[3]=connections[3]!=null?new AxisAlignedBB(p*10,p*6, p*6, 1,    p*10, p*10):null;
		collisionBoxes[4]=connections[0]!=null?new AxisAlignedBB(p*6, p*10,p*6, p*10, 1,    p*10):null;
		collisionBoxes[5]=connections[4]!=null?new AxisAlignedBB(p*6, p*6, p*10,p*10, p*10, 1   ):null;
		collisionBoxes[6]=                     new AxisAlignedBB(p*6, p*6, p*6, p*10, p*10, p*10);
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
