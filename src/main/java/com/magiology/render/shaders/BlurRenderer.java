package com.magiology.render.shaders;

import com.magiology.objhelper.helpers.Cricle;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.vectors.AdvancedPhysicsFloat;
import com.magiology.render.shaders.core.ShaderAspectRenderer;
import com.magiology.render.shaders.core.ShaderUniformEditor;

public class BlurRenderer extends ShaderAspectRenderer{
	public static BlurRenderer instance;
	
	public AdvancedPhysicsFloat x,y;
	
	public BlurRenderer(){
		super("blurM",0,"BlurDir");
		instance=this;
		x=new AdvancedPhysicsFloat(1, 0.08F);
		y=new AdvancedPhysicsFloat(0, 0.08F);
		x.friction=0.95F;
		y.friction=0.95F;
	}
	
	@Override
	public void update(){
		x.update();
		y.update();
		if(world.getTotalWorldTime()%10==0){
			float rotation=Helper.RF(360);
			x.wantedPoint=Cricle.sin(rotation)*2;
			y.wantedPoint=Cricle.cos(rotation)*2;
		}
	}
	
	@Override
	public void redner(){
		ShaderUniformEditor.setUniform(uniforms.get(0), x.getPoint(),y.getPoint());
	}
	
	@Override
	public boolean getConditionForActivation(){
		return false;
	}
}
