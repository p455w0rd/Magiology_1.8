package com.magiology.api.lang.bridge;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.magiology.api.lang.ProgramHandeler;
import com.magiology.api.network.skeleton.TileEntityNetworkInteract;
import com.magiology.util.utilobjects.m_extension.BlockPosM;


public class WorldWrapper{
	private static World world;
	public static void setBlockAccess(World access){
		world=access;
	}
	public static Block getBlock(InterfaceWrapper pos){
		return pos.getBlock();
	}
	public static Object getBlockState(InterfaceWrapper pos,String stateName){
		return pos.getBlockState(stateName);
	}
	public static int getRedstone(InterfaceWrapper pos, int side){
		return pos.getRedstone(side);
	}
	public static int getRedstone(InterfaceWrapper pos, EnumFacing side){
		return pos.getRedstone(side);
	}
	public static Object getBlock(BlockPosM pos){
		Object tile=getInterface(pos);
		return tile instanceof TileEntityNetworkInteract?((InterfaceWrapper)tile).getBlock():ProgramHandeler.JSNull("No interface");
	}
	public static Object getBlockState(BlockPosM pos,String stateName){
		Object tile=getInterface(pos);
		return tile instanceof TileEntityNetworkInteract?((InterfaceWrapper)tile).getBlockState(stateName):ProgramHandeler.JSNull("No interface");
	}
	public static Object getRedstone(BlockPosM pos, int side){
		Object tile=getInterface(pos);
		return tile instanceof TileEntityNetworkInteract?((InterfaceWrapper)tile).getRedstone(side):ProgramHandeler.JSNull("No interface");
	}
	public static Object getRedstone(BlockPosM pos, EnumFacing side){
		Object tile=getInterface(pos);
		return tile instanceof TileEntityNetworkInteract?((InterfaceWrapper)tile).getRedstone(side):ProgramHandeler.JSNull("No interface");
	}
	public static Object getInterface(BlockPosM pos){
		TileEntityNetworkInteract tile=pos.getTile(world, TileEntityNetworkInteract.class);
		return InterfaceWrapper.New(tile);
	}
}