package com.magiology.util.utilobjects;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.magiology.util.utilclasses.Helper;

public class ColorF{
	public float r,g,b,a;
	
	public ColorF(double r, double g, double b, double a){
		this.r=(float)Helper.keepValueInBounds(r, 0, 1);
		this.g=(float)Helper.keepValueInBounds(g, 0, 1);
		this.b=(float)Helper.keepValueInBounds(b, 0, 1);
		this.a=(float)Helper.keepValueInBounds(a, 0, 1);
	}
	public ColorF(){
		this(1,1,1,1);
	}
	public ColorF blackNWhite(){
		float sum=(r+g+b)/3F;
		r=sum;
		g=sum;
		b=sum;
		return this;
	}
	public ColorF negative(){
		r=1-r;
		g=1-g;
		b=1-b;
		return this;
	}
	public ColorF disablBlend(){
		a=1;
		return this;
	}
	public void bind(){
		GL11.glColor4f(r,g,b,a);
	}
	public int toCode(){
		return new Color(r,g,b,a).hashCode();
	}
	public ColorF mix(Color color){
		return mix(convert(color));
	}
	public ColorF mix(ColorF color){
		return new ColorF((r+color.r)/2F,(g+color.g)/2F,(b+color.b)/2F,(a+color.a)/2F);
	}
	public ColorF mix(Color color, float scale){
		return mix(convert(color),scale);
	}
	public ColorF mix(ColorF color, float scale){
		return new ColorF((r*scale+color.r/scale)/2F,(g*scale+color.g/scale)/2F,(b*scale+color.b/scale)/2F,(a*scale+color.a/scale)/2F);
	}
	public ColorF set(float modifier, char c){
		modifier=Helper.keepValueInBounds(modifier, 0, 1);
		return new ColorF(c=='r'?modifier:r, c=='g'?modifier:g, c=='b'?modifier:b, c=='a'?modifier:a);
	}
	public ColorF copy(){
		return new ColorF(r, g, b, a);
	}
	@Override
	public String toString(){
		return "("+(r+"").substring(0, Math.min((r+"").length(),4))+", "+(g+"").substring(0, Math.min((g+"").length(),4))+", "+(b+"").substring(0, Math.min((b+"").length(),4))+", "+(a+"").substring(0, Math.min((a+"").length(),4))+")";
	}
	public static ColorF convert(Color color){
		return new ColorF((float)color.getRed()/256F, (float)color.getGreen()/256F, (float)color.getBlue()/256F, (float)color.getAlpha()/256F);
	}
	public ColorF mul(double r, double g, double b, double a){
		return new ColorF(this.r*r, this.g*g, this.b*b, this.a*a);
	}
	public ColorF mul(double var){
		return new ColorF(r*var, g*var, b*var, a*var);
	}
}