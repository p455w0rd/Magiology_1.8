package com.magiology.render.shaders;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.core.init.MEvents;
import com.magiology.modedmcstuff.AdvancedPhysicsFloat;
import com.magiology.modedmcstuff.ColorF;
import com.magiology.objhelper.helpers.Cricle;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.render.shaders.core.ShaderAspectRenderer;
import com.magiology.render.shaders.core.ShaderUniformEditor;

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
		ShaderUniformEditor.setUniform(uniforms.get(0), Helper.calculateRenderPos(rPrev, r),Helper.calculateRenderPos(gPrev, g),Helper.calculateRenderPos(bPrev, b));
		ShaderUniformEditor.setUniform(uniforms.get(1), Helper.calculateRenderPos(prevTolerance, tolerance));
		ShaderUniformEditor.setUniform(uniforms.get(2), Helper.calculateRenderPos(prevIntensity, intensity));
	}
	
	@Override
	public boolean getConditionForActivation(){
		return false;
	}
}
