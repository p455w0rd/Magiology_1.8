package com.magiology.handlers.animationhandlers.thehand;

import java.util.Arrays;
import java.util.function.Consumer;

import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.ObjectHolder;

public class HandData{
		
		public float[] base,thumb,fingers[];
		
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
		public HandData add(HandData handData){
			HandData result=new HandData(false);
			result.base=UtilM.addToFloatArray(base, handData.base);
			result.thumb=UtilM.addToFloatArray(thumb, handData.thumb);
			result.fingers=UtilM.addToDoubleFloatArray(fingers, handData.fingers);
			return result;
		}
		@Override
		protected Object clone(){
			HandData result=new HandData(false);
			result.base=base.clone();
			result.thumb=thumb.clone();
			result.fingers=new float[fingers.length][0];
			for(int i=0;i<fingers.length;i++)fingers[i]=Arrays.copyOf(fingers[i], fingers[i].length);
			return result;
		}
	}