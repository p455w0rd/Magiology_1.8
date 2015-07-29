package com.magiology.objhelper.vectors;

import net.minecraft.util.Vec3;

public class Vec3F{
	public float x,y,z;
	public Vec3F(float x1, float y1, float z1){
		x=x1;
		y=y1;
		z=z1;
	}
	public Vec3F(){}
	public Vec3F set(float x1, float y1, float z1){
		x=x1;
		y=y1;
		z=z1;
		return this;
	}
	public Vec3F add(float x1, float y1, float z1){
		x+=x1;
		y+=y1;
		z+=z1;
		return this;
	}
	public Vec3 toVec3(){
		return Vec3.createVectorHelper(x, y, z);
	}
}