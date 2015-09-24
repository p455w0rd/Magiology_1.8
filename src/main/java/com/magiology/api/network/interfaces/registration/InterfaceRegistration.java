package com.magiology.api.network.interfaces.registration;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.magiology.api.network.BasicWorldNetworkInterface;
import com.magiology.api.network.WorldNetworkInterface;
import com.magiology.util.utilclasses.Util;

public class InterfaceRegistration{
	
	private static Map<Class<Block>,BasicWorldNetworkInterface> blockFinder=new HashMap<Class<Block>,BasicWorldNetworkInterface>();
	private static Map<Class<TileEntity>,BasicWorldNetworkInterface> tileFinder=new HashMap<Class<TileEntity>,BasicWorldNetworkInterface>();
	private static BasicWorldNetworkInterface defaultInterface=new DefaultInterface();
	
	public static <T extends BasicWorldNetworkInterface> T registerInterfaceToBlock(T Interface,Class<Block> marker){
		if(blockFinder.keySet().contains(marker)){
			Util.printInln("Block:",marker.getClass().getSimpleName(),"is already bound to an interface:",blockFinder.get(marker));
			return Interface;
		}
		blockFinder.put(marker, Interface);
		return Interface;
	}
	
	public static <T extends BasicWorldNetworkInterface> T registerInterfaceToTileEntity(T Interface,Class<? extends TileEntity> marker){
		if(tileFinder.keySet().contains(marker)){
			Util.printInln("TileEntity:",marker.getClass().getSimpleName(),"is already bound to an interface:",tileFinder.get(marker));
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
		Block block=Util.getBlock(world, pos);
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
		Block block=Util.getBlock(world, pos);
		if(block!=null){
			BasicWorldNetworkInterface test=blockFinder.get(block.getClass());
			WorldNetworkInterface interf=test instanceof WorldNetworkInterface?(WorldNetworkInterface)test:null;
			return interf;
		}
		return null;
	}
}
