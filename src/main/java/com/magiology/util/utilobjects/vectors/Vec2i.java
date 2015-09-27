package com.magiology.util.utilobjects.vectors;

import static com.magiology.util.utilclasses.Util.*;

import java.util.Objects;

import net.minecraft.util.EnumFacing;

public class Vec2i{
	public static final Vec2i zero=new Vec2i(0, 0);

	public int x, y;

	public Vec2i(int x, int y){
		this.x=x;
		this.y=y;
	}

	public Vec2i offset(EnumFacing dir){
		return new Vec2i(x+dir.getFrontOffsetX(), y+dir.getFrontOffsetZ());
	}

	public double distanceTo(Vec2i other){
		return Math.sqrt(Math.pow(other.x-x, 2)+Math.pow(other.y-y, 2));
	}

	@Override
	public int hashCode(){
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj){
		if(this==obj)return true;
		if(obj==null)return false;
		if(!Instanceof(this, obj))return false;
		Vec2i other=(Vec2i)obj;
		return x==other.x&&y==other.y;
	}

	@Override
	public String toString(){
		return "Vec2i["+x+", "+y+"]";
	}
	
	public Vec2i add(int var){
		return new Vec2i(x+var, y+var);
	}
	public Vec2i add(int x1, int y1){
		return new Vec2i(x+x1, y+y1);
	}
}
