package com.magiology.api.lang.bridge;

import java.util.*;
import java.util.Map.Entry;

import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.google.common.collect.*;
import com.magiology.api.lang.*;
import com.magiology.api.lang.program.*;
import com.magiology.api.network.*;
import com.magiology.util.utilobjects.m_extension.*;


public class InterfaceWrapper{
	
	private NetworkInterface interf;
	private ISidedNetworkComponent comp;
	public static Object New(Object src){
		if(src!=null&&src instanceof NetworkInterface&&src instanceof ISidedNetworkComponent)return new InterfaceWrapper((NetworkInterface&ISidedNetworkComponent)src);
		return ProgramCommon.JSNull("No interface");
	}
	private <T extends NetworkInterface&ISidedNetworkComponent>InterfaceWrapper(T src){
		interf=src;
		comp=src;
	}private World getWorld(){
		return interf.getHost().getWorld();
	}
	private BlockPosM getPos(){
		return BlockPosM.get(interf.getHost().getPos());
	}
	
	
	
	public Block getBlock(){
		return getFacedPos().getBlock(getWorld());
	}
	public Object getBlockState(String stateName){
		ImmutableSet set=getFacedPos().getState(getWorld()).getProperties().entrySet();
		Iterator iterator=set.iterator();
		if(set.size()==0)return ProgramCommon.JSNull("No states");
		Entry entry;
		while(iterator.hasNext()){
			entry=(Entry)iterator.next();
			if(((IProperty)entry.getKey()).getName().equals(stateName))return entry.getValue();
		}
		return ProgramCommon.JSNull("State \""+stateName+"\" does not exist!");
	}
	public int getRedstone(int side){
		return getFacedPos().getRedstonePower(getWorld(), EnumFacing.getFront(side));
	}
	public int getRedstone(EnumFacing side){
		return getFacedPos().getRedstonePower(getWorld(), side);
	}
	
	public Object getCommandInteractors(){
		WorldNetworkInterface Interface=interf.getInterfaceProvider();
		if(Interface==null)return ProgramCommon.JSNull("Block is not able to contain command interactors");
		List<ICommandInteract> list=Interface.getCommandInteractors();
		if(list==null)return ProgramCommon.JSNull("No command interactors found");
		return new CommandInteractorList(list);
	}
	
	public BlockPosM getFacedPos(){
		return getPos().offset(EnumFacing.getFront(comp.getOrientation()));
	}
	public Block getInterfaceBlock(){
		return getPos().getBlock(getWorld());
	}
	public Object getInterfaceBlockState(String stateName){
		ImmutableSet set=getPos().getState(getWorld()).getProperties().entrySet();
		Iterator iterator=set.iterator();
		if(set.size()==0)return ProgramCommon.JSNull("No states");
		Entry entry;
		while(iterator.hasNext()){
			entry=(Entry)iterator.next();
			if(((IProperty)entry.getKey()).getName().equals(stateName))return entry.getValue();
		}
		return "undefined";
	}
	public int getInterfaceRedstone(int side){
		return getWorld().getRedstonePower(getPos(), EnumFacing.getFront(side));
	}
	public int getInterfaceRedstone(EnumFacing side){
		return getWorld().getRedstonePower(getPos(), side);
	}
}
