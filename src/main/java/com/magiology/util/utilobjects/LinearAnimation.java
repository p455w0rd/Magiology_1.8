package com.magiology.util.utilobjects;

import java.util.ArrayList;
import java.util.List;

import com.magiology.util.utilobjects.vectors.Vec3M;

public class LinearAnimation{
	
	private DoubleObject<Vec3M[], Float>[] animationData;
	
	public LinearAnimation(DoubleObject<Vec3M[], Float>...data){
		List<DoubleObject<Vec3M[], Float>> dataSorted=new ArrayList<DoubleObject<Vec3M[], Float>>();
		
		for(DoubleObject<Vec3M[], Float> pos:data){
			boolean added=false;
			
			for(int i=0;i<dataSorted.size();i++){
				if(dataSorted.get(i).obj2>pos.obj2){
					dataSorted.add(i, pos);
					i=dataSorted.size();
					added=true;
				}
			}
			if(!added)dataSorted.add(pos);
		}
		animationData=dataSorted.toArray(new DoubleObject[0]);
	}
	
	public Vec3M[] get(int partId,float animationPos){
		DoubleObject<Vec3M[], Float> before=animationData[partId],after=animationData[partId+1];
		Vec3M[] result=new Vec3M[before.obj1.length];
		if(animationPos>=after.obj2)return after.obj1;
		if(animationPos<=before.obj2)return before.obj1;
		float 
		precentageDifference=after.obj2-before.obj2,
		precentageStart=animationPos-before.obj2,
		precentage=precentageStart/precentageDifference;
		
		
		
		for(int i=0;i<result.length;i++){
			Vec3M pos1=before.obj1[i], pos2=after.obj1[i];
			result[i]=pos2.subtract(pos1).mul(precentage).addVector(pos1);
		}
		
		return result;
	}
	public Vec3M[] get(float animationPos){
		int pos=0;
		
		for(int i=0;i<animationData.length-1;i++){
			if(animationData[pos+1].obj2<animationPos)pos=i;
			else i=animationData.length;
		}
		return get(pos, animationPos);
	}
}
