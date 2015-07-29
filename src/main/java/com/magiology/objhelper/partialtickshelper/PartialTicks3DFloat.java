package com.magiology.objhelper.partialtickshelper;


public class PartialTicks3DFloat{
	PartialTicksFloat x,y,z;
	public PartialTicks3DFloat(float x,float y,float z){
		this.x=new PartialTicksFloat(x);
		this.y=new PartialTicksFloat(y);
		this.z=new PartialTicksFloat(z);
	}
	public PartialTicks3DFloat tickUpdate(float x,float y,float z){
		this.x.tickUpdate(x);
		this.y.tickUpdate(y);
		this.z.tickUpdate(z);
		return this;
	}
	public float[] getRenderXYZ(){
		return new float[]{x.getRenderVar(),y.getRenderVar(),z.getRenderVar()};
	}
	public float getRenderX(){return x.getRenderVar();}
	public float getRenderY(){return y.getRenderVar();}
	public float getRenderZ(){return z.getRenderVar();}
	public PartialTicks3DFloat setXYZ(float x,float y,float z){
		this.x.setVar(x);
		this.y.setVar(y);
		this.z.setVar(z);
		return this;
	}
}
