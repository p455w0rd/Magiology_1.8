package com.magiology.objhelper.vectors;

import net.minecraft.util.Vec3;

import com.magiology.objhelper.helpers.Helper;

public class Plane{
	public Vec3 q,r,s,normal;
	
	public Plane(Vec3 q,Vec3 r,Vec3 s){
		this.q=q;
		this.r=r;
		this.s=s;
		normal=Helper.getNormal(q, r, s);
    }
}