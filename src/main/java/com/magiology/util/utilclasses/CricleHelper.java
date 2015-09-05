package com.magiology.util.utilclasses;


public class CricleHelper{
	//init
	private static int resolution=3;
	private static float[] 
		cos=new float[361*resolution],
		sin=cos.clone();
	//load
	static{
		for(int i=0;i<cos.length;i++){
			float rad=(float)(Math.toRadians(i)/resolution);
			cos[i]=(float) Math.cos(rad);
			sin[i]=(float) Math.sin(rad);
		}
	}
	//use----
	public static float sin(double angleD){
		int angle=fix(angleD);
		try{
			return sin[angle];
		}catch(Exception e){
			e.printStackTrace();
			return sin[0];
		}
	}
	public static float cos(double angleD){
		int angle=fix(angleD);
		try{
			return cos[angle];
		}catch(Exception e){
			e.printStackTrace();
			return cos[0];
		}
	}
	//------
	private static int fix(double angle){
		while(angle<0)angle+=360;
		while(angle>360)angle-=360;
		return (int)(angle*resolution);
	}
}
