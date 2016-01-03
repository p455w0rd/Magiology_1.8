package com.magiology.util.renderers;

import java.nio.*;
import java.util.*;

import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.*;

import org.lwjgl.opengl.*;

import com.magiology.util.renderers.VertixBuffer.ShadedTriangle;
import com.magiology.util.renderers.glstates.*;
import com.magiology.util.utilobjects.m_extension.*;

public class OpenGlModel{
	private ModelRednerer renderer=new ModelRednerer(4096);
	
	public GlStateCell glStateCell;
	
	public OpenGlModel(VertixBuffer model){
		VertexModel transformedModel=new VertexModel();
		
		model.transformAndSaveTo(transformedModel);
		transformedModel.recalculateNormals();
		
		for(ShadedTriangle shadedTriangle:transformedModel.shadedTriangles){
			renderer.setNormal(shadedTriangle.normal.getX(), shadedTriangle.normal.getY(), shadedTriangle.normal.getZ());
			for(int i=0;i<2;i++){
				PositionTextureVertex pos=shadedTriangle.pos3[i];
				renderer.addVertexWithUV(pos.vector3D.xCoord, pos.vector3D.yCoord, pos.vector3D.zCoord, pos.texturePositionX, pos.texturePositionY);
			}
		}
	}

	public void draw(){
		renderer.reset();
		if(glStateCell==null){
			draw(renderer.getModelID());
		}else{
			glStateCell.set();
			draw(renderer.getModelID());
			glStateCell.reset();
		}
	}
	
	private int draw(int modelID){
		if(modelID>0){
			VertexFormat vertexformat=renderer.vertexFormat;
			int j=vertexformat.getNextOffset();
			ByteBuffer bytebuffer=renderer.byteBuffer;
			List list=vertexformat.getElements();
			Iterator iterator=list.iterator();
			VertexFormatElement vertexElement;
			VertexFormatElement.EnumUsage enumusage;
			
			while(iterator.hasNext()?(vertexElement=(VertexFormatElement)iterator.next())!=null:false)
				vertexElement.getUsage().preDraw(vertexElement, j, bytebuffer);
			
			GL11.glDrawArrays(renderer.getDrawMode(), 0,renderer.vertexCount);
			iterator=list.iterator();
			
			while(iterator.hasNext()?(vertexElement=(VertexFormatElement)iterator.next())!=null:false)
				vertexElement.getUsage().postDraw(vertexElement, j, bytebuffer);
		}
		return modelID;
	}

}
