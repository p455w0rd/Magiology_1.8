package com.magiology.util.utilobjects.vectors.physics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.PartialTicksUtil;

public class PhysicsFloat{
	
	private Map<String, Float>   wallsF=new HashMap<String, Float>();
	private Map<String, Boolean> wallsB=new HashMap<String, Boolean>();
	public boolean simpleVersion=false;
	public float point,prevPoint,wantedPoint,speed,friction=1,acceleration,bounciness;
	
	public PhysicsFloat(float startingPoint,float acceleration, boolean simple){
		this(startingPoint, acceleration);
		simpleVersion=simple;
	}
	public PhysicsFloat(float startingPoint,float acceleration){
		prevPoint=point=wantedPoint=startingPoint;
		this.acceleration=acceleration;
	}
	public void update(){
		prevPoint=point;
		if(simpleVersion){
			point=UtilM.slowlyEqualize(point, wantedPoint, acceleration);
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
		return PartialTicksUtil.calculatePos(prevPoint, point);
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
