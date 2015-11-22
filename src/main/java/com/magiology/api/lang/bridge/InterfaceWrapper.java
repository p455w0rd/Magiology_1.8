package com.magiology.api.lang.bridge;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;

import com.magiology.api.network.WorldNetworkInterface;
import com.magiology.api.network.skeleton.TileEntityNetworkInteract;
import com.magiology.mcobjects.tileentityes.hologram.ICommandInteract;
import com.magiology.util.utilobjects.m_extension.BlockPosM;


public class InterfaceWrapper{
	
	private TileEntityNetworkInteract src;
	public InterfaceWrapper(TileEntityNetworkInteract src){
		this.src=src;
	}
	public Block getBlock(){
		return src.getPos().getBlock(src.getWorld());
	}
	public Object getBlockState(String stateName){
		Iterator iterator=src.getPos().getState(src.getWorld()).getProperties().entrySet().iterator();
		Entry entry;
		while(iterator.hasNext()){
			entry=(Entry)iterator.next();
			if(((IProperty)entry.getKey()).getName().equals(stateName))return entry.getValue();
		}
		return "undefined";
	}
	public int getRedstone(int side){
		return getFacedPos().getRedstonePower(src.getWorld(), EnumFacing.getFront(side));
	}
	public int getRedstone(EnumFacing side){
		return getFacedPos().getRedstonePower(src.getWorld(), side);
	}
	
	public Object getCommandInteractors(){
		WorldNetworkInterface Interface=src.getInterfaceProvider();
		if(Interface==null)return "undefined";
		List<ICommandInteract> list=Interface.getCommandInteractors();
		if(list==null)return "undefined";
		return list.toArray();
	}
	
	public BlockPosM getFacedPos(){
		return src.getPos().offset(EnumFacing.getFront(src.getOrientation()));
	}
	public Block getInterfaceBlock(){
		return src.getPos().getBlock(src.getWorld());
	}
	public Object getInterfaceBlockState(String stateName){
		Iterator iterator=src.getPos().getState(src.getWorld()).getProperties().entrySet().iterator();
		Entry entry;
		while(iterator.hasNext()){
			entry=(Entry)iterator.next();
			if(((IProperty)entry.getKey()).getName().equals(stateName))return entry.getValue();
		}
		return "undefined";
	}
	public int getInterfaceRedstone(int side){
		return src.getPos().getRedstonePower(src.getWorld(), EnumFacing.getFront(side));
	}
	public int getInterfaceRedstone(EnumFacing side){
		return src.getPos().getRedstonePower(src.getWorld(), side);
	}
}
