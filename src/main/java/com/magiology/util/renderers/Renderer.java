package com.magiology.util.renderers;

import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.util.*;

import org.lwjgl.opengl.*;

import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.vectors.*;

public class Renderer{
	
	private static Renderer instance=new Renderer();
	public static final PosRenderer POS=instance.new PosRenderer();
	
	private static WorldRenderer renderer=TessUtil.getWR();
	private static Tessellator tessellator=TessUtil.getT();
	
	private Renderer(){}
	
	public static void draw(){
		tessellator.draw();
	}
	
	public static void begin(int type, VertexFormat format){
		renderer.begin(type, format);
	}
	public static void begin(int type){
		begin(type, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
	}
	
	public static void beginLines(){
		renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
	}
	
	public static void beginQuads(){
		renderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
	}
	
	public static void addVertexWithData(PositionTextureVertex vertex, float xNormal, float yNormal, float zNormal){
		addVertexData(vertex.vector3D,vertex.texturePositionX, vertex.texturePositionY, xNormal, yNormal, zNormal);
	}
	
	public static void addVertexData(Vec3 pos,double u,double v,float xNormal,float yNormal,float zNormal){
		addVertexData(pos.xCoord, pos.yCoord, pos.zCoord, u, v, xNormal, yNormal, zNormal);
	}
	public static void addVertexData(Vec3M pos,double u,double v,Vec3M normal){
		addVertexData(pos.x, pos.y, pos.z, u, v, (float)normal.x, (float)normal.y, (float)normal.z);
	}
	public static void addVertexData(Vec3M pos,double u,double v,float xNormal,float yNormal,float zNormal){
		addVertexData(pos.x, pos.y, pos.z, u, v, xNormal, yNormal, zNormal);
	}
	public static void addVertexData(double xPos,double yPos,double zPos,double u,double v,float xNormal,float yNormal,float zNormal){
		addPos(xPos, yPos, zPos).addUV(u, v).addNormal(xNormal, yNormal, zNormal).endVertex();
	}
	public static Renderer addVertexData(double x,double y,double z,double u,double v){
		addPos(x, y, z).addUV(u, v);
		return instance;
	}
	
	
	public static Renderer addNormal(float x, float y, float z){
		renderer.normal(x, y, z);
		return instance;
	}
	public static Renderer addNormal(Vec3M normal){
		return addNormal(normal.getX(), normal.getY(), normal.getZ());
	}
	public static Renderer addUV(double u,double v){
		renderer.tex(u, v);
		return instance;
	}
	public static Renderer addPos(double x,double y,double z){
		renderer.pos(x, y, z);
		return instance;
	}
	
	
	public static Renderer setTranslation(double x, double y, double z){
		renderer.setTranslation(x, y, z);
		return instance;
	}
	
	public static Renderer setColor(float r, float g, float b, float a){
		renderer.color(r, g, b, a);
		return instance;
	}
	public static Renderer setColor(float r, float g, float b){
		setColor(r, g, b, 1);
		return instance;
	}
	
	public static void endVertex(){
		renderer.endVertex();
	}

	public Renderer lightmap(int j, int k){
		renderer.lightmap(j, k);
		return instance;
	}
	public static void setWorldRederer(WorldRenderer renderer){
		Renderer.renderer=renderer;
	}
	
	
	private static class PosRenderer{
		public static void begin(int type){
			renderer.begin(type, DefaultVertexFormats.POSITION);
		}
		public static void addVertex(Vec3M pos){
			addPos(pos.x, pos.y, pos.z);
		}
		public static void addVertex(double x, double y, double z){
			addPos(x, y, z);
		}
	}
}
