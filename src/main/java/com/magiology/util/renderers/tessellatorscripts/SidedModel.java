package com.magiology.util.renderers.tessellatorscripts;

import net.minecraft.util.EnumFacing;

import com.magiology.util.renderers.NormalizedVertixBufferModel;
import com.magiology.util.utilobjects.DoubleObject;

public class SidedModel{
	
	public NormalizedVertixBufferModel[] models=new NormalizedVertixBufferModel[6];
	
	public SidedModel(DoubleObject<NormalizedVertixBufferModel,int[]>... modelsFormat){
		set(modelsFormat);
	}
	
	public void draw(boolean[] sides){
		for(int i=0;i<models.length;i++){
			NormalizedVertixBufferModel model=models[i];
			if(sides[i]&&model!=null){
				model.pushMatrix();
				switch(i){
				case 0:{
					model.rotateAt(0.5, 0.5, 0.5, 90, 0, 0);
					model.draw();
				}break;
				case 1:{
					model.rotateAt(0.5, 0.5, 0.5, -90, 0, 0);
					model.draw();
				}break;
				case 2:{
					model.rotateAt(0.5, 0.5, 0.5, 0, 180, 0);
					model.draw();
				}break;
				case 3:{
					model.draw();
				}break;
				case 4:{
					model.rotateAt(0.5, 0.5, 0.5, 0, -90, 0);
					model.draw();
				}break;
				case 5:{
					model.rotateAt(0.5, 0.5, 0.5, 0, 90, 0);
					model.draw();
				}break;
				}
				
				model.popMatrix();
			}
		}
	}
	
	public void set(DoubleObject<NormalizedVertixBufferModel,int[]>... modelsFormat){
		for(DoubleObject<NormalizedVertixBufferModel, int[]> i:modelsFormat)for(int j:i.obj2)set(i.obj1,j);
	}
	public void set(NormalizedVertixBufferModel model,int id){
		models[id]=model;
	}
	public NormalizedVertixBufferModel get(int id){
		return models[id];
	}
}
