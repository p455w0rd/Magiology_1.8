package com.magiology.util.utilobjects.vectors;

import net.minecraft.util.BlockPos;

public class Pos extends BlockPos{
	public Pos(int x, int y, int z){
		super(x, y, z);
	}
	public Pos(){
		this(0,1000,0);
	}

}
