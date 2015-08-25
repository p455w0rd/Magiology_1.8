package com.magiology.objhelper.helpers.renderers;

import java.util.ArrayList;
import java.util.List;

import com.magiology.objhelper.Get.Render;

public class NormalizedVertixBufferModel extends NormalizedVertixBuffer{
	private boolean isInit=false;
	
	
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
}