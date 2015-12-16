package com.magiology.util.renderers.glstates;

import com.magiology.util.renderers.glstates.*;

public class GlStateCell{
	
	private GlState set,reset;
	private boolean suchChangeMuchMeme;
	
	public GlStateCell(GlState set, GlState reset){
		this.set=set;
		this.reset=reset;
		suchChangeMuchMeme=set==reset;
	}
	
	public void set(){
		set.configureOpenGl();
	}
	public void reset(){
		if(!suchChangeMuchMeme)set.configureOpenGl();
	}
	
}
