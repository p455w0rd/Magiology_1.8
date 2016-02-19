package com.magiology.util.utilobjects.vectors.physics;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilobjects.ObjectProcessor;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class RealPhysicsVec3F extends AbstractRealPhysicsVec3F{
	
	private float weight=0.1F,airBorneFriction=0.95F,surfaceFriction=0.95F,bounciness=0;
	private Vec3M pos,prevPos=new Vec3M(),velocity=new Vec3M(),prevVelocity=new Vec3M(),lastPos=new Vec3M();
	private boolean isWorldClipping=true;
	private World world;
	private int moveCount;
	
	public RealPhysicsVec3F(World world, Vec3M pos){
		super(world, pos);
	}
	
	public float getMass(){
		return weight;
	}
	public void setMass(float weight){
		this.weight=weight;
	}
	public float getAirBorneFriction(){
		return airBorneFriction;
	}
	public void setAirBorneFriction(float airBorneFriction){
		this.airBorneFriction=airBorneFriction;
	}
	public float getSurfaceFriction(){
		return surfaceFriction;
	}
	public void setSurfaceFriction(float surfaceFriction){
		this.surfaceFriction=surfaceFriction;
	}
	public float getBounciness(){
		return bounciness;
	}
	public void setBounciness(float bounciness){
		this.bounciness=bounciness;
	}
	public Vec3M getPos(){
		return pos;
	}
	public void setPos(Vec3M pos){
		this.pos=pos;
	}
	public Vec3M getPrevPos(){
		return prevPos;
	}
	public void setPrevPos(Vec3M prevPos){
		this.prevPos=prevPos;
	}
	public Vec3M getVelocity(){
		return velocity;
	}
	public void setVelocity(Vec3M velocity){
		this.velocity=velocity;
	}
	public Vec3M getPrevVelocity(){
		return prevVelocity;
	}
	public void setPrevVelocity(Vec3M prevVelocity){
		this.prevVelocity=prevVelocity;
	}
	public boolean isWorldClipping(){
		return isWorldClipping;
	}
	public void setWorldClipping(boolean isWorldClipping){
		this.isWorldClipping=isWorldClipping;
	}
	public World getWorld(){
		return world;
	}
	public void setWorld(World world){
		this.world=world;
	}
	@Override
	public void setLastPos(Vec3M lastPos){
		this.lastPos=lastPos;
	}
	@Override
	public Vec3M getLastPos(){
		return lastPos;
	}
}
