package com.magiology.api.network.interfaces.registration;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.magiology.api.network.BasicNetworkInterfaceProvider;
import com.magiology.api.network.NetworkInterfaceProvider;
import com.magiology.util.utilclasses.Util;

public class InterfaceRegistration{
	
	private static Map<Class<Block>,BasicNetworkInterfaceProvider> blockFinder=new HashMap<Class<Block>,BasicNetworkInterfaceProvider>();
	private static Map<Class<TileEntity>,BasicNetworkInterfaceProvider> tileFinder=new HashMap<Class<TileEntity>,BasicNetworkInterfaceProvider>();
	private static BasicNetworkInterfaceProvider defaultInterface=new DefaultInterface();
	
	public static <T extends BasicNetworkInterfaceProvider> T registerInterfaceToBlock(T Interface,Class<Block> marker){
		if(blockFinder.keySet().contains(marker)){
			Util.printInln("Block:",marker.getClass().getSimpleName(),"is already bound to an interface:",blockFinder.get(marker));
			return Interface;
		}
		blockFinder.put(marker, Interface);
		return Interface;
	}
	
	public static <T extends BasicNetworkInterfaceProvider> T registerInterfaceToTileEntity(T Interface,Class<? extends TileEntity> marker){
		if(tileFinder.keySet().contains(marker)){
			Util.printInln("TileEntity:",marker.getClass().getSimpleName(),"is already bound to an interface:",tileFinder.get(marker));
			return Interface;
		}
		tileFinder.put((Class<TileEntity>)marker, Interface);
		return Interface;
	}
	
	public static BasicNetworkInterfaceProvider getBasicInterface(World world, BlockPos pos){
		TileEntity tile=world.getTileEntity(pos);
		if(tile!=null){
			BasicNetworkInterfaceProvider interf=tileFinder.get(tile.getClass());
			if(interf!=null)return interf;
		}
		Block block=Util.getBlock(world, pos);
		if(block!=null){
			BasicNetworkInterfaceProvider interf=blockFinder.get(block.getClass());
			if(interf!=null)return interf;
		}
		return defaultInterface;
	}
	public static NetworkInterfaceProvider getInterface(TileEntity tile){
		if(tile!=null){
			BasicNetworkInterfaceProvider test=tileFinder.get(tile.getClass());
			NetworkInterfaceProvider interf=test instanceof NetworkInterfaceProvider?(NetworkInterfaceProvider)test:null;
			return interf;
		}
		return null;
	}
	public static NetworkInterfaceProvider getInterface(World world, BlockPos pos){
		TileEntity tile=world.getTileEntity(pos);
		NetworkInterfaceProvider inter=getInterface(tile);
		if(inter!=null)return inter;
		Block block=Util.getBlock(world, pos);
		if(block!=null){
			BasicNetworkInterfaceProvider test=blockFinder.get(block.getClass());
			NetworkInterfaceProvider interf=test instanceof NetworkInterfaceProvider?(NetworkInterfaceProvider)test:null;
			return interf;
		}
		return null;
	}
}
