package com.magiology.util.utilobjects.vectors;

import com.magiology.util.utilclasses.*;

public class Plane{
	public Vec3M q,r,s,normal;
	
	public Plane(Vec3M q,Vec3M r,Vec3M s){
		this.q=q;
		this.r=r;
		this.s=s;
		normal=UtilM.getNormal(q, r, s);
    }
}