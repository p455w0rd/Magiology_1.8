package com.magiology.mcobjects.tileentityes.network;

import java.util.*;

import net.minecraft.server.gui.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;

import com.magiology.api.connection.*;
import com.magiology.api.network.*;
import com.magiology.api.network.skeleton.*;
import com.magiology.forgepowered.events.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.*;

public class TileEntityNetworkConductor extends TileEntityNetwork implements IUpdatePlayerListBox{
	
	private SlowdownUtil optimizer=new SlowdownUtil(40);
	
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
		UpdateablePipeHandler.setConnections(connections, this);
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
		return NetworkUtil.canConnect(this, (ISidedNetworkComponent)tile);
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
