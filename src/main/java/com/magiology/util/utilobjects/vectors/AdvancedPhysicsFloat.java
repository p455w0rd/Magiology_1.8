package com.magiology.util.utilobjects.vectors;

import java.util.*;

import com.magiology.util.utilclasses.*;

public class AdvancedPhysicsFloat{
	
	private Map<String, Float>   wallsF=new HashMap<String, Float>();
	private Map<String, Boolean> wallsB=new HashMap<String, Boolean>();
	public boolean simpleVersion=false;
	public float point,prevPoint,wantedPoint,speed,friction=1,acceleration,bounciness;
	
	public AdvancedPhysicsFloat(float startingPoint,float acceleration, boolean simple){
		this(startingPoint, acceleration);
		simpleVersion=simple;
	}
	public AdvancedPhysicsFloat(float startingPoint,float acceleration){
		prevPoint=point=wantedPoint=startingPoint;
		this.acceleration=acceleration;
	}
	public void update(){
		prevPoint=point;
		if(simpleVersion){
			point=(float)UtilM.slowlyEqalize(point, wantedPoint, acceleration);
		}else{
			speed=UtilM.handleSpeedFolower(speed, point, wantedPoint, acceleration);
			speed*=friction;
			point+=speed;
			if(!wallsF.isEmpty()){
				Iterator<Float> fi=wallsF.values().iterator();
				Iterator<Boolean> bi=wallsB.values().iterator();
				try{
					while(fi.hasNext()){
						float   f=fi.next();
						boolean b=bi.next();
						if(b){
							if(point>f){
								bounce(bounciness);
								point=f;
							}
						}else{
							if(point<f){
								bounce(bounciness);
								point=f;
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
	}
	public float getPoint(){
		return UtilM.calculatePos(prevPoint, point);
	}
	public void bounce(float multiplyer){
		speed*=-multiplyer;
	}
	public void addWall(String key, float wallPos,boolean pointOnSideOfTheWall){
		wallsF.put(key, wallPos);
		wallsB.put(key, pointOnSideOfTheWall);
	}
	public Object[] removeWall(String key){
		Object[] Return=new Object[2];
		Return[0]=wallsF.remove(key);
		Return[1]=wallsB.remove(key);
		return Return;
	}
	public void removeWalls(){
		wallsF.clear();
		wallsB.clear();
	}
	public float difference(){
		return Math.abs(point-wantedPoint);
	}
}
