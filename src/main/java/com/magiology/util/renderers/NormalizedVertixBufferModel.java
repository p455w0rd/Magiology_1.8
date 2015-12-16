package com.magiology.util.renderers;

import java.util.*;

import com.magiology.util.renderers.glstates.*;
import com.magiology.util.utilclasses.Get.Render;

public class NormalizedVertixBufferModel extends NormalizedVertixBuffer{
	private boolean isInit=false;
	
	public GlStateCell glStateCell;
	
	protected static NormalizedVertixBufferModel create(){
		return new NormalizedVertixBufferModel();
	}
	private NormalizedVertixBufferModel(){
		this.renderer=Render.WR();
		
	}
	protected void init(List<ShadedTriangle> shadedTriangles){
		if(isInit)return;isInit=true;
		this.shadedTriangles=new ArrayList<ShadedTriangle>();
		this.shadedTriangles.addAll(shadedTriangles);
	}
	@Override
	public void cleanUp(){}
	
	@Override
	public void draw(){
		if(glStateCell!=null){
			glStateCell.set();
			super.draw();
			glStateCell.reset();
		}else super.draw();
	}
	
}