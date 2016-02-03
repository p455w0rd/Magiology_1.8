package com.magiology.api.network.interfaces.registration;

import com.magiology.api.network.InterfaceTileEntitySaver;
import com.magiology.api.network.NetworkInterface;
import com.magiology.api.network.WorldNetworkInterface;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

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
	public static TileEntityNetworkController getBrain(TileEntity tile){
		WorldNetworkInterface Interface=get(tile);
		return TileToInterfaceHelper.getBrain(tile, Interface);
	}
	public static NetworkInterface getConnectedInterface(TileEntity tile){
		WorldNetworkInterface Interface=get(tile);
		return TileToInterfaceHelper.getConnectedInterface(tile, Interface);
	}
	//------------
	
	
	
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
		public static TileEntityNetworkController getBrain(TileEntity tile,WorldNetworkInterface Interface){
			((InterfaceTileEntitySaver)Interface).setBoundTile(tile);
			return Interface.getBrain();
		}
		public static NetworkInterface getConnectedInterface(TileEntity tile,WorldNetworkInterface Interface){
			((InterfaceTileEntitySaver)Interface).setBoundTile(tile);
			return Interface.getConnectedInterface();
		}
		//------------
	}
	public static WorldNetworkInterface get(World world,BlockPos pos){
		return InterfaceRegistration.getInterface(world,pos);
	}
	public static WorldNetworkInterface get(TileEntity tile){
		return InterfaceRegistration.getInterface(tile);
	}
}
