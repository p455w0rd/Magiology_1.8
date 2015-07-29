package com.magiology.structures;

import net.minecraft.block.Block;

public class BlockAt{
	public Block bl;
	public int x,y,z,metadata=0;
	public BlockAt(Block block,int x,int y,int z){
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
