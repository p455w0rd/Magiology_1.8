package com.magiology.structures;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class BlockAt{
	public Block bl;
	public int x,y,z,metadata=0;
	public BlockAt(Block block,BlockPos pos){
		this.bl=block;
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public void setXZ(int x,int z){
		this.x=x;
		this.z=z;
	}
}
