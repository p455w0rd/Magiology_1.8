package com.magiology.client.render.shaders;

import com.magiology.client.render.shaders.core.*;
import com.magiology.util.utilclasses.*;

public class ColorCutRenderer extends ShaderAspectRenderer{
	public static ColorCutRenderer instance;
	
	public float r=1,g=1,b=1,rPrev=1,gPrev=1,bPrev=1,tolerance=0.3F,intensity=0.9F,prevTolerance=0.3F,prevIntensity=0.9F;
	
	public ColorCutRenderer(){
		super("colorCutM",1,"color","tolerance","intensity");
		instance=this;
	}
	
	@Override
	public void update(){
		rPrev=r;
		gPrev=g;
		bPrev=b;
		prevTolerance=tolerance;
		prevIntensity=intensity;
		r=UtilM.fluctuator(97, 0);
		g=UtilM.fluctuator(140, 320);
		b=UtilM.fluctuator(203, 563);
		tolerance=0.5F;
		intensity=0.8F;
	}
	
	@Override
	public void redner(){
		ShaderAspectRenderer.setUniform(uniforms.get(0), UtilM.calculatePos(rPrev, r),UtilM.calculatePos(gPrev, g),UtilM.calculatePos(bPrev, b));
		ShaderAspectRenderer.setUniform(uniforms.get(1), UtilM.calculatePos(prevTolerance, tolerance));
		ShaderAspectRenderer.setUniform(uniforms.get(2), UtilM.calculatePos(prevIntensity, intensity));
	}
	
	@Override
	public boolean getConditionForActivation(){
		return false;
	}
}
