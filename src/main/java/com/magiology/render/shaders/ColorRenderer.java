package com.magiology.render.shaders;

import com.magiology.objhelper.helpers.Helper;
import com.magiology.render.shaders.core.ShaderAspectRenderer;
import com.magiology.render.shaders.core.ShaderUniformEditor;

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
		r=Helper.fluctuator(30, 78)*0.5F+0.5F;
		g=Helper.fluctuator(74, 752)*0.5F+0.5F;
		b=Helper.fluctuator(50, 0)*0.5F+0.5F;
	}
	
	@Override
	public void redner(){
		ShaderUniformEditor.setUniform(uniforms.get(0), Helper.calculateRenderPos(rPrev, r),Helper.calculateRenderPos(gPrev, g),Helper.calculateRenderPos(bPrev, b));
	}
	
	@Override
	public boolean getConditionForActivation(){
		return false;
	}
}
