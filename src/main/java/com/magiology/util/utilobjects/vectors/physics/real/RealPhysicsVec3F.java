package com.magiology.util.utilobjects.vectors.physics.real;

import java.util.ArrayList;
import java.util.List;

import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.world.World;

public class RealPhysicsVec3F extends AbstractRealPhysicsVec3F{
	
	private float weight=0.1F,airBorneFriction=0.95F,surfaceFriction=0.95F,bounciness=0;
	private Vec3M pos,prevPos=new Vec3M(),velocity=new Vec3M(),prevVelocity=new Vec3M(),lastPos=new Vec3M(),posBackup=new Vec3M();
	private boolean isWorldClipping=true,willColideWithBlocks=true;
	private World world;
	private List<Float> stress;
	
	
	public RealPhysicsVec3F(World world, Vec3M pos){
		super(world, pos);
		if(this.stress==null){
			this.stress=new ArrayList<>();
			for(int i=0;i<5;i++)this.stress.add(0F);
		}
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
		if(MathUtil.isNumValid(pos.x))this.pos=pos;
		if(!MathUtil.isNumValid(this.pos.x))this.pos=posBackup;
		else posBackup=this.pos;
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
		if(MathUtil.isNumValid(velocity.x))this.velocity=velocity;
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
	@Override
	public float getStress(){
		if(this.stress==null){
			this.stress=new ArrayList<>();
			for(int i=0;i<5;i++)this.stress.add(0F);
			return 0;
		}
		float sum=0;
		for(Float f:stress)sum+=f;
//		if(Double.isInfinite(sum/stress.size()))PrintUtil.println(sum,stress.size());
		return sum/stress.size();
	}
	@Override
	public void setStress(float stress){
		if(this.stress==null){
			this.stress=new ArrayList<>();
			for(int i=0;i<5;i++)this.stress.add(0F);
		}
		if(!MathUtil.isNumValid(stress))return;
		this.stress.remove(0);
		this.stress.add(stress);
	}

	@Override
	public boolean getWillColideWithBlocks(){
		return willColideWithBlocks;
	}

	@Override
	public void setWillColideWithBlocks(boolean colide){
		willColideWithBlocks=colide;
	}
}
