package com.magiology.util.renderers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

import com.magiology.util.utilobjects.IndexedModel;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class MultiTransfromModel{
	
	private List<int[]> matrices=new ArrayList<>();
	private IndexedModel model;
	
	public MultiTransfromModel(IndexedModel model){
		this.model=model;
	}
	
	public void draw(final List<Matrix4f> transformations){
		List<Integer> indices=model.getIndices();
		List<Vec3M> vertices=getTransfromed(transformations);
		
		VertexRenderer buff=TessUtil.getVB();
		buff.cleanUp();
		indices.forEach(index->buff.addVertexWithUV(vertices.get(index), 0, 0));
		buff.draw();
	}
	
	public List<Vec3M> getTransfromed(final List<Matrix4f> transformations){
		if(matrices.size()!=transformations.size())throw new IllegalStateException("incorrect matrix list sizes! "+matrices.size()+" vs "+transformations.size());
		
		List<Vec3M> vertices=new ArrayList<>();
		model.getVertices().forEach(vertex->vertices.add(vertex.copy()));
		
		for(int i=matrices.size()-1;i>-1;i--){
			int[] matrixIndices=matrices.get(i);
			if(matrixIndices.length==0){
				Matrix4f vert=transformations.get(i);
				vertices.forEach(ver->ver.transformSelf(vert));
			}
			else for(int j=0;j<matrixIndices.length;j++)vertices.get(matrixIndices[j]).transformSelf(transformations.get(i));
		}
		
		return vertices;
	}
	
	public void addMatrix(int[] effectedVertices){
		matrices.add(effectedVertices);
	}
	public void removeMatrix(int id){
		matrices.remove(id);
	}
	public List<Integer> getIndices(){
		return model.getIndices();
	}

	public IndexedModel getChild(){
		return model;
	}
}
