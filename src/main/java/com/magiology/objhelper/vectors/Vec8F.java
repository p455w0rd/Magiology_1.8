package com.magiology.objhelper.vectors;

public class Vec8F{
	public float x1,y1,x2,y2,x3,y3,x4,y4;
	public Vec8F(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
		this.x1=x1;this.x2=x2;this.x3=x3;this.x4=x4;this.y1=y1;this.y2=y2;this.y3=y3;this.y4=y4;
	}
	public Vec8F mirror(){
		return new Vec8F(x2, y2, x1, y1, x4, y4, x3, y3);
	}
	public Vec8F rotate(){
		return new Vec8F(x4, y4, x1, y1, x2, y2, x3, y3);
	}
	public Vec8F copy(){
		return new Vec8F(x1, y1, x2, y2, x3, y3, x4, y4);
	}
	public static Vec8F all(){
		return all.copy();
	}
	public Vec8F edit(float x1_a, float y1_a, float x2_a, float y2_a, float x3_a, float y3_a, float x4_a, float y4_a){
		return new Vec8F(x1+x1_a, y1+y1_a, x2+x2_a, y2+y2_a, x3+x3_a, y3+y3_a, x4+x4_a, y4+y4_a);
	}
	public Vec8F set(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
		return new Vec8F(x1, y1, x2, y2, x3, y3, x4, y4);
	}
	private static Vec8F all=new Vec8F(1,1,1,0,0,0,0,1);
}