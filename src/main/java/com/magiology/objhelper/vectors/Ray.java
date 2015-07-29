package com.magiology.objhelper.vectors;

import net.minecraft.util.Vec3;


public class Ray{
	public Vec3 from,to;
	public Ray(Vec3 from, Vec3 dir){
		this.from=from;
		this.to=dir;
	}
}