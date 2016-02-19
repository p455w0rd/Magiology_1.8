package com.magiology.util.utilobjects.vectors.physics;

import java.awt.PointerInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.util.renderers.Renderer;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.ObjectProcessor;
import com.magiology.util.utilobjects.vectors.Vec2i;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.world.World;

public class RealPhysicsMesh{
	
	private List<AbstractRealPhysicsVec3F> vertices=new ArrayList<>();
	private List<Vec2i> indices=new ArrayList<>();
	private List<RealPhysicsMeshHook> physicsHooks=new ArrayList<>();
	private boolean needsUpdateOrderUpdate=true;
	private MaterialStrategy interactStrategy=MaterialStrategy.STATIC_DISTANCE;
	private World world;
	private float originalDistances[],distanceFixingSpeed=3,noise=0;
	
	public static enum MaterialStrategy{
		STATIC_DISTANCE(         new ObjectProcessor<Boolean>(){@Override public Boolean pocess(Boolean object, Object... objects){return true;}},true),
		NO_INTERACTION(          new ObjectProcessor<Boolean>(){@Override public Boolean pocess(Boolean object, Object... objects){return false;}},false),
		ONLY_SMALLER_SUPPRESSING(new ObjectProcessor<Boolean>(){@Override public Boolean pocess(Boolean object, Object... objects){return ((Float)objects[0])<0;}},true),
		ONLY_BIGGER_SUPPRESSING( new ObjectProcessor<Boolean>(){@Override public Boolean pocess(Boolean object, Object... objects){return ((Float)objects[0])>0;}},true);
		
		private final ObjectProcessor<Boolean> check;
		public final boolean shouldCheck;
		
		private MaterialStrategy(ObjectProcessor<Boolean> check,boolean shouldCheck){
			this.check=check;
			this.shouldCheck=shouldCheck;
		}
		
		public boolean shouldInteract(float difference){
			return check.pocess(false, difference);
		}
	}
	
	public RealPhysicsMesh(final World world, final List<Vec3M> vertices, final List<Vec2i> indices, final AbstractRealPhysicsVec3F example){
		this.world=world;
		vertices.forEach(pos->{
			RealPhysicsVec3F vec3f=new RealPhysicsVec3F(world, pos);
			vec3f.setAirBorneFriction(example.getAirBorneFriction());
			vec3f.setBounciness(example.getBounciness());
			vec3f.setMass(example.getMass());
			vec3f.addPos(example.getPos());
			vec3f.setPrevPos(vec3f.getPos());
			vec3f.setLastPos(vec3f.getPos());
			vec3f.setPrevVelocity(example.getPrevVelocity());
			vec3f.setSurfaceFriction(example.getSurfaceFriction());
			vec3f.setVelocity(example.getVelocity());
			vec3f.setWorldClipping(example.isWorldClipping());
			this.vertices.add(vec3f);
		});
		this.indices=indices;
		originalDistances=new float[indices.size()];
		for(int i=0;i<originalDistances.length;i++){
			Vec2i connection=indices.get(i);
			originalDistances[i]=(float)vertices.get(connection.x).distanceTo(vertices.get(connection.y));
		}
	}
	
	public void update(){
		noise=0.01F;
		physicsHooks.forEach(hook->{
			DoubleObject<Vec3M, Integer> result=hook.getHook();
			AbstractRealPhysicsVec3F vertex=vertices.get(result.obj2);
			vertex.setPos(result.obj1);
			vertex.setVelocity(new Vec3M());
		});
		if(noise>0)vertices.forEach(vert->vert.addVelocity(new Vec3M(UtilM.CRandF(noise), UtilM.CRandF(noise), UtilM.CRandF(noise))));
		
		if(interactStrategy.shouldCheck){
			for(int pos=0;pos<indices.size();pos++){
				Vec2i con=indices.get(pos);
				
				if(con.x>con.y)con=new Vec2i(con.y, con.x);

				boolean
					xHooked=isVertexHooked(con.x),
					yHooked=isVertexHooked(con.y);
				
				if(!xHooked||!yHooked){
					AbstractRealPhysicsVec3F 
						vertex1=vertices.get(con.x),
						vertex2=vertices.get(con.y);
					Vec3M distance=vertex1.getPos().subtract(vertex2.getPos()).add(vertex1.getVelocity()).add(vertex2.getVelocity().mul(-1));
					float d1stance=distance.length();
					float difference=d1stance-originalDistances[pos];
					if(interactStrategy.shouldInteract(difference)){
						Vec3M 
							acteleration1=vertex1.getPrevVelocity().subtract(vertex1.getVelocity()),
							acteleration2=vertex1.getPrevVelocity().subtract(vertex1.getVelocity());
						
						float freakingOutSafety=2F;
						Vec3M fix=distance.normalize().mul(-difference/freakingOutSafety);
						float
							mul1=xHooked? 0:yHooked?2: 1,
							mul2=xHooked?-2:yHooked?0:-1;
						
						vertex1.addVelocity(fix.mul(mul1));
						vertex2.addVelocity(fix.mul(mul2));
					}
				}
			}
		}
		vertices.forEach(AbstractRealPhysicsVec3F::update);
	}
	
	
	public class RealPhysicsMeshHook{
		
		private int vertexID;
		private ObjectProcessor<Vec3M> hook;
		
		private RealPhysicsMeshHook(int vertexID, ObjectProcessor<Vec3M> hook){
			this.vertexID=vertexID;
			this.hook=hook;
		}
		
		private DoubleObject<Vec3M, Integer> getHook(){
			return new DoubleObject<Vec3M, Integer>(hook.pocess(vertices.get(vertexID).getPos(), RealPhysicsMesh.this,this,vertexID), vertexID);
		}
	}
	
	public void addWorldHook(int vertexID, ObjectProcessor<Vec3M> hook){
		final RealPhysicsMeshHook newHook=new RealPhysicsMeshHook(vertexID, hook);
		physicsHooks.removeIf(hook0->hook0.vertexID==vertexID);
		physicsHooks.add(newHook);
		needsUpdateOrderUpdate=true;
	}
	public RealPhysicsMeshHook remove(int hookID){
		return physicsHooks.remove(hookID);
	}
	
	protected boolean isVertexHooked(int id){
		for(RealPhysicsMeshHook hook:physicsHooks)if(hook.vertexID==id)return true;
		return false;
	}
	
	public List<AbstractRealPhysicsVec3F> getVertices(){
		return vertices;
	}

	public void setVertices(List<AbstractRealPhysicsVec3F> vertices){
		this.vertices=vertices;
	}

	public List<Vec2i> getIndices(){
		return indices;
	}

	public void setIndices(List<Vec2i> indices){
		this.indices=indices;
	}

	public World getWorld(){
		return world;
	}

	public void setWorld(World world){
		this.world=world;
	}

	public MaterialStrategy getInteractStrategy(){
		return interactStrategy;
	}

	public void setInteractStrategy(MaterialStrategy interactStrategy){
		this.interactStrategy=interactStrategy==null?MaterialStrategy.NO_INTERACTION:interactStrategy;
	}
}
