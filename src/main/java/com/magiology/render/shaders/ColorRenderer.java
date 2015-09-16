package com.magiology.render.shaders;

import com.magiology.render.shaders.core.ShaderAspectRenderer;
import com.magiology.render.shaders.core.ShaderUniformEditor;
import com.magiology.util.utilclasses.Util;

public class ColorRenderer extends ShaderAspectRenderer{
	public float r=1,g=1,b=1,rPrev=1,gPrev=1,bPrev=1;
	public static ColorRenderer instance;
	
	public ColorRenderer(){
		super("blurM",0,"color");
		instance=this;
	}
	
	@Override
	public void update(){
		rPrev=r;
		gPrev=g;
		bPrev=b;
		r=Util.fluctuator(30, 78)*0.5F+0.5F;
		g=Util.fluctuator(74, 752)*0.5F+0.5F;
		b=Util.fluctuator(50, 0)*0.5F+0.5F;
	}
	
	@Override
	public void redner(){
		ShaderUniformEditor.setUniform(uniforms.get(0), Util.calculateRenderPos(rPrev, r),Util.calculateRenderPos(gPrev, g),Util.calculateRenderPos(bPrev, b));
	}
	
	@Override
	public boolean getConditionForActivation(){
		return false;
	}
}
