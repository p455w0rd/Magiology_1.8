package com.magiology.util.renderers.tessellatorscripts;

import com.magiology.util.renderers.*;
import com.magiology.util.utilobjects.*;

public class SidedModel{
	
	public NormalizedVertixBufferModel[][] models=new NormalizedVertixBufferModel[6][0];
	
	public SidedModel(DoubleObject<NormalizedVertixBufferModel[],int[]>... modelsFormat){
		set(modelsFormat);
	}
	
	public void draw(boolean[] sides){
		for(int i=0;i<models.length;i++){
			NormalizedVertixBufferModel[] modelArray=models[i];
			if(sides[i]&&modelArray!=null&&modelArray.length>0)for(NormalizedVertixBufferModel model:modelArray){
				model.pushMatrix();
				switch(i){
				case 0:{
					model.rotateAt(0.5, 0.5, 0.5, 90, 0, 0);
				}break;
				case 1:{
					model.rotateAt(0.5, 0.5, 0.5, -90, 0, 0);
				}break;
				case 2:{
					model.rotateAt(0.5, 0.5, 0.5, 0, 180, 0);
				}break;
				case 3:{
					
				}break;
				case 4:{
					model.rotateAt(0.5, 0.5, 0.5, 0, -90, 0);
				}break;
				case 5:{
					model.rotateAt(0.5, 0.5, 0.5, 0, 90, 0);
				}break;
				}
				model.draw();
				
				model.popMatrix();
			}
		}
	}
	
	public void set(DoubleObject<NormalizedVertixBufferModel[],int[]>... modelsFormat){
		for(DoubleObject<NormalizedVertixBufferModel[], int[]> i:modelsFormat)for(int j:i.obj2)set(j, i.obj1);
	}
	public void set(int id, NormalizedVertixBufferModel... model){
		models[id]=model;
	}
	public NormalizedVertixBufferModel[] get(int id){
		return models[id];
	}
}
