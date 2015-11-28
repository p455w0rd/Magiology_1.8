package com.magiology.util.renderers.tessellatorscripts;

import net.minecraft.client.renderer.*;

import org.lwjgl.opengl.*;

import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.*;

public class Drawer{
	
	private static WorldRenderer renderer=Get.Render.WR();
	private static Tessellator tessellator=Get.Render.T();
	private Drawer(){}
	public static void draw(){
		tessellator.draw();
	}
	public static void startDrawing(int type){
		renderer.startDrawing(type);
	}
	public static void startDrawingLines(){
		startDrawing(GL11.GL_LINES);
	}
	public static void startDrawingQuads(){
		renderer.startDrawingQuads();
	}
	public static void addVertex(double x,double y,double z){
		renderer.addVertex(x, y, z);
	}
	public static void addVertexWithUV(double x,double y,double z,double u,double v){
		renderer.addVertexWithUV(x, y, z, u, v);
	}
	public static void setBrightness(int brightness){
		renderer.setBrightness(brightness);
	}
	public static void setColor(float r, float g, float b, float a){
		renderer.setColorRGBA_F(r,g,b,a);
	}
	public static void setColor(ColorF color){
		renderer.setColorRGBA_F(color.r,color.g,color.b,color.a);
	}
	public static void setColor(float r, float g, float b){
		renderer.setColorOpaque_F(r,g,b);
	}
	public static void setTranslation(double x, double y, double z){
		renderer.setTranslation(x, y, z);
	}
	
}
