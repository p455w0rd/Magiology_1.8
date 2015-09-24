package com.magiology.api.network.interfaces.registration;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.magiology.api.network.InterfaceTileEntitySaver;
import com.magiology.api.network.NetworkBaseInterface;
import com.magiology.api.network.NetworkBaseInterface.DataOutput;
import com.magiology.api.network.NetworkBaseInterface.DataOutput.DataOutputDesc;
import com.magiology.api.network.NetworkBaseInterface.InteractType;
import com.magiology.api.network.WorldNetworkInterface;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.util.utilobjects.DoubleObject;

public class InterfaceBinder{
	
	public static void readInterfaceFromNBT(TileEntity tile,NBTTagCompound NBT){
		WorldNetworkInterface Interface=get(tile);
		TileToInterfaceHelper.readFromNBT(tile, Interface, NBT);
	}
	public static void writeInterfaceToNBT(TileEntity tile, NBTTagCompound NBT){
		WorldNetworkInterface Interface=get(tile);
		TileToInterfaceHelper.writeToNBT(tile, Interface, NBT);
	}
	//------------
	public long getCard(TileEntity tile){
		WorldNetworkInterface Interface=get(tile);
		return TileToInterfaceHelper.getCard(tile, Interface);
	}
	public static void onNetworkActionInvoked(TileEntity tile,NetworkBaseInterface NetInterface,String action,Object... data){
		WorldNetworkInterface Interface=get(tile);
		TileToInterfaceHelper.onNetworkActionInvoked(tile, Interface, NetInterface, action, data);
	}
	public static TileEntityNetworkController getBrain(TileEntity tile){
		WorldNetworkInterface Interface=get(tile);
		return TileToInterfaceHelper.getBrain(tile, Interface);
	}
	public static NetworkBaseInterface getConnectedInterface(TileEntity tile){
		WorldNetworkInterface Interface=get(tile);
		return TileToInterfaceHelper.getConnectedInterface(tile, Interface);
	}
	public static InteractType[] getInteractTypes(TileEntity tile){
		WorldNetworkInterface Interface=get(tile);
		return TileToInterfaceHelper.getInteractTypes(tile, Interface);
	}
	//------------
	public static DoubleObject<DataOutput,Object> getDataOutput(TileEntity tile,DataOutputDesc desc){
		WorldNetworkInterface Interface=get(tile);
		return TileToInterfaceHelper.getDataOutput(tile, Interface, desc);
	}
	
	
	
	public static class TileToInterfaceHelper{
		public static void readFromNBT(TileEntity tile,WorldNetworkInterface Interface,NBTTagCompound NBT){
			((InterfaceTileEntitySaver)Interface).setBoundTile(tile);
			((InterfaceTileEntitySaver)Interface).readFromNBT(NBT);
		}
		public static void  writeToNBT(TileEntity tile,WorldNetworkInterface Interface,NBTTagCompound NBT){
			((InterfaceTileEntitySaver)Interface).setBoundTile(tile);
			((InterfaceTileEntitySaver)Interface).writeToNBT(NBT);
		}
		//------------
		public static long getCard(TileEntity tile,WorldNetworkInterface Interface){
			((InterfaceTileEntitySaver)Interface).setBoundTile(tile);
			return Interface.getCard();
		}
		public static void onNetworkActionInvoked(TileEntity tile,WorldNetworkInterface Interface,NetworkBaseInterface NetInterface,String action,Object... data){
			((InterfaceTileEntitySaver)Interface).setBoundTile(tile);
			NetInterface.onInvokedFromNetwork(action, data);
		}
		public static TileEntityNetworkController getBrain(TileEntity tile,WorldNetworkInterface Interface){
			((InterfaceTileEntitySaver)Interface).setBoundTile(tile);
			return Interface.getBrain();
		}
		public static NetworkBaseInterface getConnectedInterface(TileEntity tile,WorldNetworkInterface Interface){
			((InterfaceTileEntitySaver)Interface).setBoundTile(tile);
			return Interface.getConnectedInterface();
		}
		public static InteractType[] getInteractTypes(TileEntity tile,WorldNetworkInterface Interface){
			((InterfaceTileEntitySaver)Interface).setBoundTile(tile);
			return Interface.getInteractTypes();
		}
		//------------
		public static DoubleObject<DataOutput,Object> getDataOutput(TileEntity tile,WorldNetworkInterface Interface,DataOutputDesc desc){
			((InterfaceTileEntitySaver)Interface).setBoundTile(tile);
			return Interface.getData(desc);
		}
	}
	public static WorldNetworkInterface get(World world,BlockPos pos){
		return InterfaceRegistration.getInterface(world,pos);
	}
	public static WorldNetworkInterface get(TileEntity tile){
		return InterfaceRegistration.getInterface(tile);
	}
}
