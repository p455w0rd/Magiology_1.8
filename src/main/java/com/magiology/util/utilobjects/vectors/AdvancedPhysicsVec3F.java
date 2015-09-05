package com.magiology.util.utilobjects.vectors;

public class AdvancedPhysicsVec3F{
	
	public AdvancedPhysicsFloat x,y,z;
	
	public AdvancedPhysicsVec3F(Vec3M startingPoint,Vec3M acceleration, boolean[] simple){
		x=new AdvancedPhysicsFloat(startingPoint.getX(), acceleration.getX(),simple[0]);
		y=new AdvancedPhysicsFloat(startingPoint.getY(), acceleration.getY(),simple[1]);
		z=new AdvancedPhysicsFloat(startingPoint.getZ(), acceleration.getZ(),simple[2]);
	}
	public AdvancedPhysicsVec3F(Vec3M startingPoint,Vec3M acceleration){
		x=new AdvancedPhysicsFloat(startingPoint.getX(), acceleration.getX());
		y=new AdvancedPhysicsFloat(startingPoint.getY(), acceleration.getY());
		z=new AdvancedPhysicsFloat(startingPoint.getZ(), acceleration.getZ());
	}
	public void update(){
		x.update();
		y.update();
		z.update();
	}
	public Vec3M getPoint(){
		return new Vec3M(x.getPoint(), y.getPoint(), z.getPoint());
	}
	public void bounce(float multiplyer){
		x.bounce(multiplyer);
		y.bounce(multiplyer);
		z.bounce(multiplyer);
	}
	public void addWall(String key, float wallPos,boolean pointOnSideOfTheWall){
		x.addWall(key, wallPos, pointOnSideOfTheWall);
		y.addWall(key, wallPos, pointOnSideOfTheWall);
		z.addWall(key, wallPos, pointOnSideOfTheWall);
	}
	public Object[][] removeWall(String key){
		Object[][] Return=new Object[3][2];
		Return[0]=x.removeWall(key);
		Return[1]=y.removeWall(key);
		Return[2]=z.removeWall(key);
		return Return;
	}
	public void removeWalls(){
		x.removeWalls();
		y.removeWalls();
		z.removeWalls();
	}
	public Vec3M difference(){
		return new Vec3M(x.difference(), y.difference(), z.difference());
	}
}
