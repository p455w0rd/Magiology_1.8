package com.magiology.client.render.shaders;

import com.magiology.client.render.shaders.core.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.math.*;
import com.magiology.util.utilobjects.vectors.*;

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
			float rotation=UtilM.RF(360);
			x.wantedPoint=CricleUtil.sin(rotation)*2;
			y.wantedPoint=CricleUtil.cos(rotation)*2;
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
