package com.magiology.util.renderers;

import com.magiology.util.renderers.glstates.GlStateCell;

import java.util.ArrayList;
import java.util.List;

public class VertexModel extends VertexRenderer{
	private boolean isInit=false;
	
	public GlStateCell glStateCell;
	
	protected static VertexModel create(){
		return new VertexModel();
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