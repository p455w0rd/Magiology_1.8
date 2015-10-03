package com.magiology.util.utilobjects.m_extension;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class BlockPosM extends BlockPos{
	
	public BlockPosM(){
		super(0,0,0);
	}
	public BlockPosM(int x, int y, int z){
		super(x, y, z);
	}
	public BlockPosM(double x, double y, double z){
		super(x, y, z);
	}
	public BlockPosM(Entity source){
		this(source.posX, source.posY, source.posZ);
	}
	public BlockPosM(Vec3 source){
		this(source.xCoord, source.yCoord, source.zCoord);
	} 
	public BlockPosM(Vec3M source){
		this(source.x, source.y, source.z);
	}
	public BlockPosM(Vec3i source){
		this(source!=null?source.getX():0, source!=null?source.getY():0, source!=null?source.getZ():0);
	}
	
	public<T extends TileEntity> T getTile(World world,Class<T> type){
		try{
			return (T)getTile(world);
		}catch(Exception e){
			return null;
		}
	}
	public TileEntity getTile(World world){
		return world.getTileEntity(this);
	}
	public Block getBlock(World world){
		return Util.getBlock(world, this);
	}
	public IBlockState getState(World world){
		return world.getBlockState(this);
	}
	public Vec3M vecAdd(Vec3M vec){
		vec.x+=getX();
		vec.y+=getY();
		vec.z+=getZ();
		return vec;
	}
	public Vec3M vecSub(Vec3M vec){
		vec.x-=getX();
		vec.y-=getY();
		vec.z-=getZ();
		return vec;
	}
	
	public BlockPos conv(){
		return new BlockPos(getX(), getY(), getZ());
	}
	public static BlockPosM get(BlockPos pos){
		return new BlockPosM(pos);
	}
}
