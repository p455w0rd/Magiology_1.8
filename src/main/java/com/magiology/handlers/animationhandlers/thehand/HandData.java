package com.magiology.handlers.animationhandlers.thehand;

import java.util.Arrays;
import java.util.function.Consumer;

import com.magiology.api.Calculable;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.ArrayMath;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.ObjectHolder;

public class HandData implements Calculable<HandData>{
	public float calciferiumPrecentage,base[],thumb[],fingers[][];
	
	public HandData(){
		this(true);
	}
	public HandData(boolean init){
		if(init){
			base=new float[]{0,0,0, 0,0,0};
			thumb=new float[]{0,0,0, 0, 0};
			fingers=new float[][]{{0,0, 0, 0},{0,0, 0, 0},{0,0, 0, 0},{0,0, 0, 0}};
		}
	}
	
	public HandData setForEach(Consumer<ObjectHolder<Float>> consumer){
		for(int i=0;i<base.length;i++){
			ObjectHolder<Float> num=new ObjectHolder<Float>(base[i]);
			consumer.accept(num);
			base[i]=num.getVar();
		}
		for(int i=0;i<thumb.length;i++){
			ObjectHolder<Float> num=new ObjectHolder<Float>(thumb[i]);
			consumer.accept(num);
			thumb[i]=num.getVar();
		}
		for(int i=0;i<fingers.length;i++){
			for(int j=0;j<fingers[i].length;j++){
				ObjectHolder<Float> num=new ObjectHolder<Float>(fingers[i][j]);
				consumer.accept(num);
				fingers[i][j]=num.getVar();
			}
		}
		return this;
	}
	
	public void set(HandData handData){
		base=handData.base;
		thumb=handData.thumb;
		fingers=handData.fingers;
	}
	public HandData Clone(){
		return (HandData)clone();
	}
	@Override
	public Object clone(){
		HandData result=new HandData(false);
		result.base=base.clone();
		result.thumb=thumb.clone();
		result.fingers=new float[fingers.length][0];
		for(int i=0;i<fingers.length;i++)result.fingers[i]=Arrays.copyOf(fingers[i], fingers[i].length);
		result.calciferiumPrecentage=calciferiumPrecentage;
		return result;
	}
	
	@Override
	public HandData add(HandData handData){
		HandData result=new HandData(false);
		result.base=MathUtil.addToFloatArray(base, handData.base);
		result.thumb=MathUtil.addToFloatArray(thumb, handData.thumb);
		result.fingers=new float[fingers.length][];
		for(int i=0;i<result.fingers.length;i++)result.fingers[i]=ArrayMath.calc(fingers[i], handData.fingers[i], '+');
		return result;
	}
	@Override
	public HandData sub(HandData handData){
		HandData result=new HandData(false);
		result.base=ArrayMath.calc(base, handData.base, '-');
		result.thumb=ArrayMath.calc(thumb, handData.thumb, '-');
		result.fingers=new float[fingers.length][];
		for(int i=0;i<result.fingers.length;i++)result.fingers[i]=ArrayMath.calc(fingers[i], handData.fingers[i], '-');
		return result;
	}
	@Override
	public HandData mul(HandData handData){
		HandData result=new HandData(false);
		result.base=ArrayMath.calc(base, handData.base, '*');
		result.thumb=ArrayMath.calc(thumb, handData.thumb, '*');
		result.fingers=new float[fingers.length][];
		for(int i=0;i<result.fingers.length;i++)result.fingers[i]=ArrayMath.calc(fingers[i], handData.fingers[i], '*');
		return result;
	}
	@Override
	public HandData div(HandData handData){
		HandData result=new HandData(false);
		result.base=ArrayMath.calc(base, handData.base, '/');
		result.thumb=ArrayMath.calc(thumb, handData.thumb, '/');
		result.fingers=new float[fingers.length][];
		for(int i=0;i<result.fingers.length;i++)result.fingers[i]=ArrayMath.calc(fingers[i], handData.fingers[i], '/');
		return result;
	}
	@Override
	public HandData add(float var){
		HandData result=new HandData(false);
		result.base=ArrayMath.calc(base, var, '+');
		result.thumb=ArrayMath.calc(thumb, var, '+');
		result.fingers=new float[fingers.length][];
		for(int i=0;i<result.fingers.length;i++)result.fingers[i]=ArrayMath.calc(fingers[i], var, '+');
		return result;
	}
	@Override
	public HandData sub(float var){
		HandData result=new HandData(false);
		result.base=ArrayMath.calc(base, var, '-');
		result.thumb=ArrayMath.calc(thumb, var, '-');
		result.fingers=new float[fingers.length][];
		for(int i=0;i<result.fingers.length;i++)result.fingers[i]=ArrayMath.calc(fingers[i], var, '-');
		return result;
	}
	@Override
	public HandData mul(float var){
		HandData result=new HandData(false);
		result.base=ArrayMath.calc(base, var, '*');
		result.thumb=ArrayMath.calc(thumb, var, '*');
		result.fingers=new float[fingers.length][];
		for(int i=0;i<result.fingers.length;i++)result.fingers[i]=ArrayMath.calc(fingers[i], var, '*');
		return result;
	}
	@Override
	public HandData div(float var){
		HandData result=new HandData(false);
		result.base=ArrayMath.calc(base, var, '/');
		result.thumb=ArrayMath.calc(thumb, var, '/');
		result.fingers=new float[fingers.length][];
		for(int i=0;i<result.fingers.length;i++)result.fingers[i]=ArrayMath.calc(fingers[i], var, '/');
		return result;
	}
	@Override
	public String toString(){
		return new StringBuilder()
			.append("HandData[base")
			.append(UtilM.arrayToString(base))
			.append(", thumb")
			.append(UtilM.arrayToString(thumb))
			.append(", fingers")
			.append(UtilM.arrayToString(fingers))
			.append("]")
		.toString();
	}
}