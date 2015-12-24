package com.magiology.mcobjects.tileentityes.network;

import java.util.*;

import net.minecraft.server.gui.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;

import com.magiology.api.connection.*;
import com.magiology.api.network.*;
import com.magiology.api.network.skeleton.*;
import com.magiology.forgepowered.events.*;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile.*;
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
		EnumFacing[] sides=new EnumFacing[6];
		UpdateablePipeHandler.setConnections(sides, this);
		for(int i=0;i<sides.length;i++)connections[i].setMain(sides[i]!=null);
		setColisionBoxes();
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
	@Override
	public void getValidTileEntitys(List<Class> included, List<Class> excluded){
		included.add(ISidedNetworkComponent.class);
	}
	@Override
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array, int side){
		return NetworkUtil.canConnect(this, (ISidedNetworkComponent)tile);
	}

	@Override
	public IConnection[] getConnections(){
		return connections;
	}

	@Override
	public boolean isStrate(EnumFacing facing){
		if(facing==EnumFacing.UP||facing==EnumFacing.DOWN||facing==null){
			if(((connections[0].getMain())&&connections[1].getMain())&&(connections[2].getMain()==false&&connections[3].getMain()==false&&connections[4].getMain()==false&&connections[5].getMain()==false)){
//				Helper.printInln("1");
				return true;
			}
		}
		if(facing==EnumFacing.WEST||facing==EnumFacing.EAST||facing==null){
			if((connections[4].getMain()&&connections[5].getMain())&&(connections[1].getMain()==false&&(connections[0].getMain()==false)&&connections[2].getMain()==false&&connections[3].getMain()==false)){
//				Helper.printInln("2");
				return true;
			}
		}
		if(facing==EnumFacing.SOUTH||facing==EnumFacing.NORTH||facing==null){
			if((connections[2].getMain()&&connections[3].getMain())&&(connections[1].getMain()==false&&(connections[0].getMain()==false)&&connections[4].getMain()==false&&connections[5].getMain()==false)){
//				Helper.printInln("3");
				return true;
			}
		}
		return false;
	}
}
