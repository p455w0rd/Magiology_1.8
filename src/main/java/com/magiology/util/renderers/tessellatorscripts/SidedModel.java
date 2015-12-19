package com.magiology.util.renderers.tessellatorscripts;

import java.awt.*;

import com.magiology.util.renderers.*;
import com.magiology.util.utilobjects.*;

public class SidedModel{
	
	public NormalizedVertixBufferModel[][] models=new NormalizedVertixBufferModel[6][0],compiledModels;
	private boolean shouldCompile;
	
	public SidedModel(DoubleObject<NormalizedVertixBufferModel[],int[]>... modelsFormat){
		set(modelsFormat);
	}
	
	
	public void draw(boolean[] sides){
		if(shouldCompile)compile();
		for(int i=0;i<compiledModels.length;i++){
			NormalizedVertixBufferModel[] modelArray=compiledModels[i];
			if(sides[i]&&modelArray!=null)for(NormalizedVertixBufferModel model:modelArray)if(model!=null)model.draw();
		}
	}
	private void compile(){
		
		compiledModels=new NormalizedVertixBufferModel[models.length][0];
		
		for(int i=0;i<compiledModels.length;i++){
			compiledModels[i]=new NormalizedVertixBufferModel[models[i].length];
			for(int j=0;j<compiledModels[i].length;j++){
				
				NormalizedVertixBuffer buff=new NormalizedVertixBuffer();
				
				compiledModels[i][j]=models[i][j].exportToNoramlisedVertixBufferModel();
				
				switch(i){
				case 0:compiledModels[i][j].rotateAt(0.5, 0.5, 0.5, 90, 0, 0);break;
				case 1:compiledModels[i][j].rotateAt(0.5, 0.5, 0.5, -90, 0, 0);break;
				case 2:compiledModels[i][j].rotateAt(0.5, 0.5, 0.5, 0, 180, 0);break;
				case 3:break;
				case 4:compiledModels[i][j].rotateAt(0.5, 0.5, 0.5, 0, -90, 0);break;
				case 5:compiledModels[i][j].rotateAt(0.5, 0.5, 0.5, 0, 90, 0);break;
				}
				
				compiledModels[i][j].glStateCell=models[i][j].glStateCell;
				
			}
		}
		shouldCompile=false;
	}
	
	public void set(DoubleObject<NormalizedVertixBufferModel[],int[]>... modelsFormat){
		for(DoubleObject<NormalizedVertixBufferModel[], int[]> i:modelsFormat)for(int j:i.obj2)set(j, i.obj1);
	}
	public void set(int id, NormalizedVertixBufferModel... model){
		models[id]=model;
		shouldCompile=true;
	}
	public NormalizedVertixBufferModel[] get(int id){
		return models[id];
	}
}
