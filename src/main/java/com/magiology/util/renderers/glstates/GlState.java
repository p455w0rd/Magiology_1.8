package com.magiology.util.renderers.glstates;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.*;

import com.magiology.util.renderers.*;

public class GlState{
	
	public static final GlState 
		NORMAL=new GlState(new int[]{GL11.GL_DEPTH, GL11.GL_TEXTURE_2D, GL11.GL_CULL_FACE, GL11.GL_LIGHTING}, new int[]{GL11.GL_BLEND}, ()->{
			GL11.glLineWidth(1);
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.99F);
			GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}),
		STANDARD_OPAQUE=new GlState(new int[]{GL11.GL_BLEND}, new int[]{GL11.GL_ALPHA_TEST}, ()->{
			glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0F);
		});
	
	
	private int[] enable={},disable={};
	private Runnable custom;
	
	public GlState(Runnable custom){
		this.custom=custom;
	}
	public GlState(int[] enable, int[] disable,Runnable custom){
		this(enable,disable);
		this.custom=custom;
	}
	public GlState(int[] enable, int[] disable){
		this.enable=enable;
		this.disable=disable;
	}
	public void configureOpenGl(){
		for(int i:enable)GL11.glEnable(i);
		for(int i:disable)GL11.glDisable(i);
		if(custom!=null)custom.run();
	}
}
