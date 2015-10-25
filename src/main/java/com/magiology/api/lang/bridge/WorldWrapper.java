package com.magiology.api.lang.bridge;

import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.magiology.util.utilobjects.m_extension.BlockPosM;


public class WorldWrapper{
	private static World world;
	public static void setBlockAccess(World access){
		world=access;
	}
	public static Block getBlock(BlockPosM pos){
		return pos.getBlock(world);
	}
	public static Object getBlockState(BlockPosM pos,String stateName){
		Iterator iterator=pos.getState(world).getProperties().entrySet().iterator();
		Entry entry;
		while(iterator.hasNext()){
			entry=(Entry)iterator.next();
			if(((IProperty)entry.getKey()).getName().equals(stateName))return entry.getValue();
		}
		return "undefined";
	}
	public static int getRedstone(BlockPosM pos,EnumFacing side){
		return pos.getRedstonePower(world, side);
	}
}
