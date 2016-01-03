package com.magiology.util.utilobjects.m_extension;

import java.nio.*;
import java.util.*;

import org.apache.logging.log4j.*;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.util.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

import static net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage.*;
import static net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType.*;

public class ModelRednerer{
	public ByteBuffer byteBuffer;
	private IntBuffer rawIntBuffer;
	private FloatBuffer rawFloatBuffer;
	public VertexFormat vertexFormat;
	
	public int vertexCount,brightness,color,rawBufferIndex,normal,byteIndex,bufferSize;
	private double textureU,textureV;
	

	public ModelRednerer(int bufferSizeIn){
		bufferSize=bufferSizeIn;
		byteBuffer=GLAllocation.createDirectByteBuffer(bufferSizeIn*4);
		rawIntBuffer=byteBuffer.asIntBuffer();
		rawFloatBuffer=byteBuffer.asFloatBuffer();
		vertexFormat=new VertexFormat();
		vertexFormat.setElement(new VertexFormatElement(0,FLOAT,POSITION, 3));
	}

	private void growBuffer(){
		bufferSize*=2;
		ByteBuffer bytebuffer=GLAllocation
				.createDirectByteBuffer(bufferSize*4);
		rawIntBuffer.position(0);
		bytebuffer.asIntBuffer().put(rawIntBuffer);
		byteBuffer=bytebuffer;
		rawIntBuffer=byteBuffer.asIntBuffer();
		rawFloatBuffer=byteBuffer.asFloatBuffer();
	}
	
	public void setTextureUV(double p_178992_1_, double p_178992_3_){
		if(!vertexFormat.hasElementOffset(0) && !vertexFormat.hasElementOffset(1)){
			VertexFormatElement vertexformatelement=new VertexFormatElement(0, FLOAT,UV, 2);
			vertexFormat.setElement(vertexformatelement);
		}
		textureU=p_178992_1_;
		textureV=p_178992_3_;
	}
	
	public void addVertexWithUV(double p_178985_1_, double p_178985_3_,double p_178985_5_, double p_178985_7_, double p_178985_9_){
		setTextureUV(p_178985_7_, p_178985_9_);
		addVertex(p_178985_1_, p_178985_3_, p_178985_5_);
	}
	
	public void addVertex(double p_178984_1_, double p_178984_3_,double p_178984_5_){
		checkAndGrow();
		
		List list=vertexFormat.getElements();
		Iterator iterator=list.iterator();

		while (iterator.hasNext()){
			VertexFormatElement vertexformatelement=(VertexFormatElement) iterator
					.next();
			int i=vertexformatelement.getOffset() >> 2;
			int j=rawBufferIndex+i;

			switch (ModelRednerer.SwitchEnumUsage.VALUES[vertexformatelement
					.getUsage().ordinal()]){
			case 1:
				rawIntBuffer.put(j,Float.floatToRawIntBits((float) (p_178984_1_)));
				rawIntBuffer.put(j+1,Float.floatToRawIntBits((float) (p_178984_3_)));
				rawIntBuffer.put(j+2,Float.floatToRawIntBits((float) (p_178984_5_)));
				break;
			case 2:
				rawIntBuffer.put(j, color);
				break;
			case 3:
				if (vertexformatelement.getIndex() == 0){
					rawIntBuffer.put(j,Float.floatToRawIntBits((float) textureU));
					rawIntBuffer.put(j+1,Float.floatToRawIntBits((float) textureV));
				} else{
					rawIntBuffer.put(j, brightness);
				}

				break;
			case 4:
				rawIntBuffer.put(j, normal);
			}
		}

		rawBufferIndex += vertexFormat.getNextOffset() >> 2;
		++vertexCount;
	}
	
	/**
	*Marks the current renderer data as dirty and needing to be updated.
	 */
	public void setNormal(float p_178980_1_, float p_178980_2_, float p_178980_3_){
		if (!vertexFormat.hasNormal()){
			VertexFormatElement vertexformatelement=new VertexFormatElement(0, BYTE,NORMAL, 3);
			vertexFormat.setElement(vertexformatelement);
			vertexFormat.setElement(new VertexFormatElement(0,UBYTE,PADDING, 1));
		}
		byte b2=(byte) ((int) (p_178980_1_*127.0F));
		byte b0=(byte) ((int) (p_178980_2_*127.0F));
		byte b1=(byte) ((int) (p_178980_3_*127.0F));
		normal=b2 & 255 | (b0 & 255) << 8 | (b1 & 255) << 16;
	}
	
	public void reset(){
		if(vertexCount>0){
			byteBuffer.position(0);
			byteBuffer.limit(rawBufferIndex*4);
		}
		byteIndex=rawBufferIndex*4;
	}
	
	public int getModelID(){
		return byteIndex;
	}
	
	public int getDrawMode(){
		return 7;
	}

	public void checkAndGrow(){
		if(rawBufferIndex>=bufferSize-vertexFormat.getNextOffset())growBuffer();
	}
	
	@SideOnly(Side.CLIENT)
	private static final class SwitchEnumUsage{
		static final int[] VALUES=new int[VertexFormatElement.EnumUsage.values().length];
		private static final String __OBFID="CL_00002569";
		
		static{
			try{VALUES[POSITION.ordinal()]=1;}catch(NoSuchFieldError var4){}
			try{VALUES[COLOR.ordinal()]=2;}catch(NoSuchFieldError var3){}
			try{VALUES[UV.ordinal()]=3;}catch(NoSuchFieldError var2){}
			try{VALUES[NORMAL.ordinal()]=4;}catch(NoSuchFieldError var1){}
		}
	}
}
