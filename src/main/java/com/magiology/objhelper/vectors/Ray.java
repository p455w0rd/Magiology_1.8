package com.magiology.objhelper.vectors;



public class Ray{
	public Vec3M from,to;
	public Ray(Vec3M from, Vec3M dir){
		this.from=from;
		this.to=dir;
	}
}