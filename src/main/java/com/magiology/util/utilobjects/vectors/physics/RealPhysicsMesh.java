package com.magiology.util.utilobjects.vectors.physics;

import java.awt.PointerInfo;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.ObjectProcessor;
import com.magiology.util.utilobjects.vectors.Vec2i;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.world.World;

public class RealPhysicsMesh{
	
	private List<AbstractRealPhysicsVec3F> vertices=new ArrayList<>();
	private List<Vec2i> indices=new ArrayList<>();
	private List<RealPhysicsMeshHook> physicsHooks=new ArrayList<>();
	private boolean originalDistancesStatic=true,needsUpdateOrderUpdate=true;
	private World world;
	private int[] orderOfUpdates,orderOfConnections;
	private float originalDistances[],distanceFixingSpeed=3;
	
	
	
	public RealPhysicsMesh(final World world, List<Vec3M> vertices, List<Vec2i> indices){
		this.world=world;
		vertices.forEach(pos->{
			this.vertices.add(new RealPhysicsVec3F(world, pos));
		});
		PrintUtil.println("-----");
		this.indices=indices;
		orderOfUpdates=new int[vertices.size()];
		orderOfConnections=new int[indices.size()];
		originalDistances=new float[indices.size()];
		for(int i=0;i<originalDistances.length;i++){
			Vec2i connection=indices.get(i);
			originalDistances[i]=(float)vertices.get(connection.x).distanceTo(vertices.get(connection.y));
		}
	}
	
	public void update(){
		vertices.forEach(vert->PrintUtil.println(vert.getPos()));
		PrintUtil.println("-----");
		if(needsUpdateOrderUpdate)updateOrderOfUpdates();
		vertices.forEach(vert->PrintUtil.println(vert.getPos()));
		PrintUtil.println("-----");
		
//		List<DoubleObject<Vec3M, Integer>> hooks=new ArrayList<>();
//		physicsHooks.forEach(hook->hooks.add(hook.getHook()));
		
//		hooks.forEach(hook->vertices.get(hook.obj2).moveTo(hook.obj1));
		
//		for(int i=hooks.size();i<orderOfUpdates.length;i++)vertices.get(orderOfUpdates[i]).update();
//		if(originalDistancesStatic)for(int i=1;i<orderOfConnections.length;i++){
//			Vec2i connection=indices.get(i);
//			boolean xFirst=connection.x<connection.y;
//			int first=xFirst?connection.x:connection.y,second=xFirst?connection.y:connection.x;
//			RealPhysicsVec3F vec1=vertices.get(first),vec2=vertices.get(second);
//			
//			boolean 
//				hooked=first<hooks.size(),
//				bothHooked=hooked&&second<hooks.size();
//			if(!bothHooked){
//				Vec3M pos1=vec1.getPos().copy(),pos2=vec2.getPos();
//				Vec3M originalDistance=pos1.subtract(pos2),newDistance=originalDistance.normalize().mul(originalDistances[i]);
//				float 
//					lenght=originalDistance.length(),
//					difference=newDistance.length()-lenght;
//				if(lenght>originalDistances[i]){
////					if(hooked)vec2.setVelocity(vec2.getVelocity().addVector(pos1.addVector(newDistance).subtract(vec2.getPos())));
////					else{
////						Vec3M correctDistance=originalDistance.normalize().mul(difference/2);
////						vec1.setVelocity(vec1.getVelocity().addVector(pos1.addVector(correctDistance).subtract(vec1.getPos())));
////						vec2.setVelocity(vec2.getVelocity().addVector(pos2.subtract(correctDistance).subtract(vec2.getPos())));
////					}
//					if(hooked)vec2.moveTo(pos1.addVector(newDistance));
//					else{
//						Vec3M correctDistance=originalDistance.normalize().mul(difference/2);
//						vec1.moveTo(pos1.addVector(correctDistance));
//						vec2.moveTo(pos1.subtract(correctDistance));
//					}
//				}
//			}
//		}
//		vertices.forEach(a->PrintUtil.println(a.getPos()));
	}
	
	private int id=0,idCon=0,prevIndexedCount=0;
	private List<Boolean> unindexedIDs=new ArrayList<>(),unindexedConnections=new ArrayList<>();
	
	private void updateOrderOfUpdates(){
		for(int i=0;i<orderOfUpdates.length;i++)orderOfUpdates[i]=i;
		for(int i=0;i<orderOfConnections.length;i++)orderOfConnections[i]=i;
//		if(physicsHooks.isEmpty()){
//			for(int i=0;i<orderOfUpdates.length;i++)orderOfUpdates[i]=i;
//			for(int i=0;i<orderOfConnections.length;i++)orderOfConnections[i]=i;
//			return;
//		}
//		for(int i=0;i<orderOfUpdates.length;i++)unindexedIDs.add(true);
//		for(int i=0;i<orderOfConnections.length;i++)unindexedConnections.add(true);
//			
//		for(int i=0;i<physicsHooks.size();i++)indexUpdate(physicsHooks.get(i).vertexID);
//		
//		prevIndexedCount=physicsHooks.size();
//		while(unindexedIDs.contains(true)){
//			for(int i=0;i<indices.size();i++){
//				Vec2i vec2i=indices.get(i);
//				for(RealPhysicsMeshHook hook:physicsHooks){
//					if(vec2i.x==hook.vertexID)indexUpdate(vec2i.y);
//					else if(vec2i.y==hook.vertexID)indexUpdate(vec2i.x);
//				}
//			}
//			int indexedCount=0;
//			for(boolean bol:unindexedIDs)if(bol)indexedCount++;
//			if(prevIndexedCount==indexedCount){
//				PrintUtil.println(prevIndexedCount,indexedCount);
//				List<Integer> unindexed=new ArrayList<>();
//				for(int i=0;i<vertices.size();i++)if(ArrayUtils.contains(orderOfUpdates, i))unindexed.add(i);
//				throw new IllegalStateException("Mesh can't be indexed! This maybe caused by not all vertices beeing conected. IDs of unindexed all vertices: "+unindexed);
//			}
//		}
//		
//		
//		physicsHooks.forEach(hook->indexConnection(idCon));
//		
//		while(unindexedConnections.contains(true)){
//			for(int i=0;i<indices.size();i++){
//				Vec2i vec2i=indices.get(i);
//				int smallestId=Integer.MAX_VALUE;
//				for(int j=0;j<idCon;i++){
//					Vec2i possiblyConnected=indices.get(orderOfConnections[j]);
//					if(possiblyConnected.x==vec2i.x||possiblyConnected.y==vec2i.y)smallestId=Math.min(smallestId, orderOfConnections[j]);
//				}
//				indexConnection(smallestId);
//			}
//		}
	}
	
	private void indexConnection(int data){
		if(unindexedConnections.get(data)){
			orderOfConnections[idCon]=data;
			unindexedConnections.set(id, false);
			idCon++;
		}
	}
	private void indexUpdate(int data){
		if(unindexedIDs.get(data)){
			orderOfUpdates[id]=data;
			unindexedIDs.set(id, false);
			id++;
			prevIndexedCount++;
		}
	}
	
	public class RealPhysicsMeshHook{
		
		private int vertexID;
		private ObjectProcessor<Vec3M> hook;
		
		private RealPhysicsMeshHook(int vertexID, ObjectProcessor<Vec3M> hook){
			this.vertexID=vertexID;
			this.hook=hook;
		}
		
		private DoubleObject<Vec3M, Integer> getHook(){
			return new DoubleObject<Vec3M, Integer>(hook.pocess(vertices.get(vertexID).getPos(), RealPhysicsMesh.this,this), vertexID);
		}
	}
	
	public void addWorldHook(int vertexID, ObjectProcessor<Vec3M> hook){
		final RealPhysicsMeshHook newHook=new RealPhysicsMeshHook(vertexID, hook);
		physicsHooks.removeIf(hook0->hook0.vertexID==newHook.vertexID);
		physicsHooks.add(newHook);
		needsUpdateOrderUpdate=true;
	}
	public RealPhysicsMeshHook remove(int hookID){
		return physicsHooks.remove(hookID);
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

	public boolean isOriginalDistancesStatic(){
		return originalDistancesStatic;
	}

	public void setOriginalDistancesStatic(boolean originalDistancesStatic){
		this.originalDistancesStatic=originalDistancesStatic;
	}

	public World getWorld(){
		return world;
	}

	public void setWorld(World world){
		this.world=world;
		vertices.forEach(vertex->vertex.setWorld(world));
	}
}
