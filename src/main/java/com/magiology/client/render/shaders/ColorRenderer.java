package com.magiology.client.render.shaders;

import com.magiology.client.render.shaders.core.ShaderAspectRenderer;
import com.magiology.util.utilclasses.UtilM;

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
		r=UtilM.fluctuate(30, 78)*0.5F+0.5F;
		g=UtilM.fluctuate(74, 752)*0.5F+0.5F;
		b=UtilM.fluctuate(50, 0)*0.5F+0.5F;
	}
	
	@Override
	public void redner(){
		ShaderAspectRenderer.setUniform(uniforms.get(0), UtilM.calculatePos(rPrev, r),UtilM.calculatePos(gPrev, g),UtilM.calculatePos(bPrev, b));
	}
	
	@Override
	public boolean getConditionForActivation(){
		return false;
	}
}
