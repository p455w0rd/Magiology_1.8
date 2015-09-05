package com.magiology.mcobjects.tileentityes.network;

import java.util.List;

import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;

import com.magiology.api.connection.IConnection;
import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.skeleton.TileEntityNetwork;
import com.magiology.forgepowered.event.ForcePipeUpdate;
import com.magiology.util.utilclasses.NetworkHelper;
import com.magiology.util.utilobjects.SlowdownHelper;

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
	@Override
	public void getValidTileEntitys(List<Class> included, List<Class> excluded){
		included.add(ISidedNetworkComponent.class);
	}
	@Override
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array, int side){
		return NetworkHelper.canConnect(this, (ISidedNetworkComponent)tile);
	}

	@Override
	public IConnection[] getConnections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isStrate(EnumFacing facing) {
		// TODO Auto-generated method stub
		return false;
	}
}
