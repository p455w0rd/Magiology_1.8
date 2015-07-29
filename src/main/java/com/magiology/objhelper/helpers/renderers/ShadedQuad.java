package com.magiology.objhelper.helpers.renderers;

import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.renderers.tessellatorscripts.TexturedTriangle;
import com.magiology.objhelper.vectors.Vec3F;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;

import org.apache.commons.lang3.ArrayUtils;

public class ShadedQuad{
	static Tessellator tess=Tessellator.instance;
	static PositionTextureVertex[] list=null;
	public static boolean isInISBRHMode=false;
	public static void drawQuad(){
		if(list==null)return;
		if(list.length<4)return;
		if(isInISBRHMode){
			PositionTextureVertex[] vertexPositions={list[0],list[1],list[2],list[3]};
			Vec3 vec3=vertexPositions[1].vector3D.subtract(vertexPositions[0].vector3D);
	        Vec3 vec31=vertexPositions[1].vector3D.subtract(vertexPositions[2].vector3D);
	        Vec3 vec32=vec31.crossProduct(vec3).normalize();
			tess.setNormal((float)vec32.xCoord,(float)vec32.yCoord,(float)vec32.zCoord);
			for(PositionTextureVertex point:list){
				tess.addVertexWithUV(point.vector3D.xCoord, point.vector3D.yCoord, point.vector3D.zCoord, point.texturePositionX, point.texturePositionY);
			}
		}else{
			if(Helper.getFPS()<50){
				new TexturedQuad(new PositionTextureVertex[]{list[0],list[1],list[2],list[3]}).draw(tess, 1);
			}else{
				new TexturedTriangle(new PositionTextureVertex[]{list[0],list[1],list[2]}).draw();
				new TexturedTriangle(new PositionTextureVertex[]{list[2],list[3],list[0]}).draw();
			}
		}
		list=null;
	}
	public static void addVertexWithUV(Vec3F point3D,double txX, double txY){
		list=ArrayUtils.add(list,new PositionTextureVertex(point3D.x,point3D.y,point3D.z,(float)txX,(float)txY));
		if(isInISBRHMode)drawQuad();
	}
	public static void addVertexWithUV(double x, double y, double z, double txX, double txY){
		list=ArrayUtils.add(list,new PositionTextureVertex((float)x, (float)y, (float)z, (float)txX, (float)txY));
		if(isInISBRHMode)drawQuad();
	}
	public static void addVertex(double x, double y, double z){
		list=ArrayUtils.add(list,new PositionTextureVertex((float)x, (float)y, (float)z, 0, 0));
		if(isInISBRHMode)drawQuad();
	}
	public static void addVertex(Vec3F point3D){
		list=ArrayUtils.add(list,new PositionTextureVertex(point3D.x,point3D.y,point3D.z, 0, 0));
		if(isInISBRHMode)drawQuad();
	}
	public static void addVertexWithUVWRender(double x, double y, double z, double txX, double txY){
		addVertexWithUV(x, y, z, txX, txY);
		drawQuad();
	}
	public static void addVertexWithUVWRender(Vec3F point3D,double txX, double txY){
		addVertexWithUV(point3D.x,point3D.y,point3D.z, txX, txY);
		drawQuad();
	}
	public static void addVertexWRender(double x, double y, double z){
		addVertex(x, y, z);
		drawQuad();
	}
	public static void addVertexWRender(Vec3F point3D){
		addVertex(point3D.x,point3D.y,point3D.z);
		drawQuad();
	}
}
