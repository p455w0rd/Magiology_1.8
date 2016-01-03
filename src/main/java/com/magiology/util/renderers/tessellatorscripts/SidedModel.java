package com.magiology.util.renderers.tessellatorscripts;

import com.magiology.util.renderers.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.vectors.*;

public class SidedModel{
	
	public VertexModel[][] models=new VertexModel[6][0];
	public OpenGlModel[][] compiledModels;
	private boolean shouldCompile;
	
	public final Vec3M[] rotations={
		new Vec3M( 90,   0, 0),
		new Vec3M(-90,   0, 0),
		new Vec3M(  0, 180, 0),
		new Vec3M(  0,   0, 0),
		new Vec3M(  0, -90, 0),
		new Vec3M(  0,  90, 0)
	};
	
	private int curentSide;
	
	public SidedModel(DoubleObject<VertexModel[],int[]>... modelsFormat){
		set(modelsFormat);
	}
	
	
	public void draw(boolean[] sides){
		if(shouldCompile)compile();
		for(int i=0;i<compiledModels.length;i++){
			OpenGlModel[] modelArray=compiledModels[i];
			if(sides[i]&&modelArray!=null){
				curentSide=i;
				for(OpenGlModel model:modelArray){
					if(model!=null){
						model.draw();
					}
				}
			}
		}
	}
	private void compile(){
		
		compiledModels=new OpenGlModel[models.length][0];
		
		for(int i=0;i<compiledModels.length;i++){
			compiledModels[i]=new OpenGlModel[models[i].length];
			for(int j=0;j<compiledModels[i].length;j++){
				
				VertixBuffer buff=new VertixBuffer();
				
				VertexModel buffer=models[i][j].exportToNoramlisedVertixBufferModel();
				
				buffer.rotateAt(0.5, 0.5, 0.5, rotations[i].x, rotations[i].y, rotations[i].z);
				
				compiledModels[i][j]=new OpenGlModel(buffer);
				compiledModels[i][j].glStateCell=models[i][j].glStateCell;
				
			}
		}
		shouldCompile=false;
	}
	
	public void set(DoubleObject<VertexModel[],int[]>... modelsFormat){
		for(DoubleObject<VertexModel[], int[]> i:modelsFormat)for(int j:i.obj2)set(j, i.obj1);
	}
	public void set(int id, VertexModel... model){
		models[id]=model;
		shouldCompile=true;
	}
	public VertexModel[] get(int id){
		return models[id];
	}
	
	public int getCurentSide(){
		return curentSide;
	}
}
