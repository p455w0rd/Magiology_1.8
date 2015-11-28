package com.magiology.api.network.interfaces.registration;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.api.network.*;
import com.magiology.util.utilclasses.*;

public class InterfaceRegistration{
	
	private static Map<Class<Block>,BasicWorldNetworkInterface> blockFinder=new HashMap<Class<Block>,BasicWorldNetworkInterface>();
	private static Map<Class<TileEntity>,BasicWorldNetworkInterface> tileFinder=new HashMap<Class<TileEntity>,BasicWorldNetworkInterface>();
	private static BasicWorldNetworkInterface defaultInterface=new DefaultInterface();
	
	public static <T extends BasicWorldNetworkInterface> T registerInterfaceToBlock(T Interface,Class<Block> marker){
		if(blockFinder.keySet().contains(marker)){
			UtilM.println("Block:",marker.getClass().getSimpleName(),"is already bound to an interface:",blockFinder.get(marker));
			return Interface;
		}
		blockFinder.put(marker, Interface);
		return Interface;
	}
	
	public static <T extends BasicWorldNetworkInterface> T registerInterfaceToTileEntity(T Interface,Class<? extends TileEntity> marker){
		if(tileFinder.keySet().contains(marker)){
			UtilM.println("TileEntity:",marker.getClass().getSimpleName(),"is already bound to an interface:",tileFinder.get(marker));
			return Interface;
		}
		tileFinder.put((Class<TileEntity>)marker, Interface);
		return Interface;
	}
	
	public static BasicWorldNetworkInterface getBasicInterface(World world, BlockPos pos){
		TileEntity tile=world.getTileEntity(pos);
		if(tile!=null){
			BasicWorldNetworkInterface interf=tileFinder.get(tile.getClass());
			if(interf!=null)return interf;
		}
		Block block=UtilM.getBlock(world, pos);
		if(block!=null){
			BasicWorldNetworkInterface interf=blockFinder.get(block.getClass());
			if(interf!=null)return interf;
		}
		return defaultInterface;
	}
	public static WorldNetworkInterface getInterface(TileEntity tile){
		if(tile!=null){
			BasicWorldNetworkInterface test=tileFinder.get(tile.getClass());
			WorldNetworkInterface interf=test instanceof WorldNetworkInterface?(WorldNetworkInterface)test:null;
			return interf;
		}
		return null;
	}
	public static WorldNetworkInterface getInterface(World world, BlockPos pos){
		TileEntity tile=world.getTileEntity(pos);
		WorldNetworkInterface inter=getInterface(tile);
		if(inter!=null)return inter;
		Block block=UtilM.getBlock(world, pos);
		if(block!=null){
			BasicWorldNetworkInterface test=blockFinder.get(block.getClass());
			WorldNetworkInterface interf=test instanceof WorldNetworkInterface?(WorldNetworkInterface)test:null;
			return interf;
		}
		return null;
	}
}
