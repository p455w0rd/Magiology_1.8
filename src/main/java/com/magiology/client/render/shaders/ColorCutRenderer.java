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
		r=1F;
		g=0F;
		b=0F;
		tolerance=0.5F;
		intensity=0.3F;
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void redner(){
		ShaderUniformEditor.setUniform(uniforms.get(0), UtilM.calculatePos(rPrev, r),UtilM.calculatePos(gPrev, g),UtilM.calculatePos(bPrev, b));
		ShaderUniformEditor.setUniform(uniforms.get(1), UtilM.calculatePos(prevTolerance, tolerance));
		ShaderUniformEditor.setUniform(uniforms.get(2), UtilM.calculatePos(prevIntensity, intensity));
	}
	
	@Override
	public boolean getConditionForActivation(){
		return false;
	}
}
