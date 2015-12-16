package com.magiology.util.utilobjects.vectors;

import org.lwjgl.util.vector.Vector2f;

public class QuadUV{
	public float x1,y1,x2,y2,x3,y3,x4,y4;
	public QuadUV(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
		this.x1=x1;this.x2=x2;this.x3=x3;this.x4=x4;this.y1=y1;this.y2=y2;this.y3=y3;this.y4=y4;
	}
	public QuadUV mirror(){
		return new QuadUV(x2, y2, x1, y1, x4, y4, x3, y3);
	}
	public QuadUV rotate(){
		return new QuadUV(x4, y4, x1, y1, x2, y2, x3, y3);
	}
	public QuadUV copy(){
		return new QuadUV(x1, y1, x2, y2, x3, y3, x4, y4);
	}
	public static QuadUV all(){
		return all.copy();
	}
	public QuadUV edit(float x1_a, float y1_a, float x2_a, float y2_a, float x3_a, float y3_a, float x4_a, float y4_a){
		return new QuadUV(x1+x1_a, y1+y1_a, x2+x2_a, y2+y2_a, x3+x3_a, y3+y3_a, x4+x4_a, y4+y4_a);
	}
	public QuadUV set(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
		return new QuadUV(x1, y1, x2, y2, x3, y3, x4, y4);
	}
	public Vector2f getUV(int id){
		switch(id){
		case 0:return new Vector2f(x1,y1);
		case 1:return new Vector2f(x2,y2);
		case 2:return new Vector2f(x3,y3);
		case 3:return new Vector2f(x4,y4);
		default:return null;
		}
	}
	private static QuadUV all=new QuadUV(1,1,1,0,0,0,0,1);
}