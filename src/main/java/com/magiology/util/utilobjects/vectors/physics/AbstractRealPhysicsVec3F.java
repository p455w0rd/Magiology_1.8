package com.magiology.util.utilobjects.vectors.physics;

import java.util.ArrayList;
import java.util.List;

import com.magiology.util.utilclasses.PhysicsUtil;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilobjects.ObjectProcessor;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public abstract class AbstractRealPhysicsVec3F{
	
	public AbstractRealPhysicsVec3F(World world, Vec3M pos){
		setWorld(world);
		setPos(pos);
		setPrevPos(pos);
	}
	public void update(){
		setPos(getPos().copy());
		
		setPrevVelocity(getVelocity().copy());
		getVelocity().y-=getMass();
		move(getVelocity());
		mulVelocity(getAirBorneFriction());
	}
	public void move(Vec3M move){
		getVelocity().y-=getMass();
		setLastPos(getPos());
		addPos(move);
		checkWorldClipping(getPos(), getLastPos());
	}
	public void moveTo(Vec3M destination){
		move(destination.subtract(getPos()));
	}
	private void checkWorldClipping(Vec3M start, Vec3M end){
		if(isWorldClipping()){
			try{
				MovingObjectPosition hit=getWorld().rayTraceBlocks(start.conv(), end.conv());
				if(hit!=null&&hit.typeOfHit!=MovingObjectType.MISS){
					surfaceHit(hit);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void surfaceHit(MovingObjectPosition hit){
		mulVelocity(getSurfaceFriction());
		Vec3M hitNormal=new Vec3M().offset(hit.sideHit);
		setPos(Vec3M.conv(hit.hitVec));
		
		Vec3M hitMul=hitNormal.mul(getBounciness()).abs();
		mulVelocity(new Vec3M(hitMul.x==0?1:-hitMul.x, hitMul.y==0?1:-hitMul.y, hitMul.z==0?1:-hitMul.z));
	}
	
	public void applyForce(Vec3M force){
		addVelocity(PhysicsUtil.getAcceleration(getMass(), force));
	}
	
	public void mulVelocity(float mul){
		mulVelocity(new Vec3M(mul, mul, mul));
	}
	public void addVelocity(Vec3M add){
		setVelocity(getVelocity().addVector(add));
	}
	public void mulVelocity(Vec3M mul){
		setVelocity(getVelocity().mul(mul));
	}
	public void velocityTo(Vec3M pos){
		addVelocity(pos.subtract(getPos()));
	}
	
	public void addPos(Vec3M add){
		setPos(getPos().addVector(add));
	}
	
	public abstract float getMass();
	public abstract void setMass(float weight);
	public abstract float getAirBorneFriction();
	public abstract void setAirBorneFriction(float airBorneFriction);
	public abstract float getSurfaceFriction();
	public abstract void setSurfaceFriction(float surfaceFriction);
	public abstract float getBounciness();
	public abstract void setBounciness(float bounciness);
	public abstract Vec3M getPos();
	public abstract void setPos(Vec3M pos);
	public abstract Vec3M getPrevPos();
	public abstract void setPrevPos(Vec3M prevPos);
	public abstract Vec3M getVelocity();
	public abstract void setVelocity(Vec3M velocity);
	public abstract Vec3M getPrevVelocity();
	public abstract void setLastPos(Vec3M lastPos);
	public abstract Vec3M getLastPos();
	public abstract void setPrevVelocity(Vec3M prevVelocity);
	public abstract boolean isWorldClipping();
	public abstract void setWorldClipping(boolean isWorldClipping);
	public abstract World getWorld();
	public abstract void setWorld(World world);
}
