package com.magiology.client.render.shaders;

import com.magiology.client.render.shaders.core.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.math.*;
import com.magiology.util.utilobjects.vectors.*;

public class BlurRenderer extends ShaderAspectRenderer{
	public static BlurRenderer instance;
	
	public boolean shouldRender=false;
	
	public AdvancedPhysicsFloat x=new AdvancedPhysicsFloat(1, 0.08F),y=new AdvancedPhysicsFloat(1, 0.08F);
	
	public BlurRenderer(){
		super("blurM",0,"BlurDir");
		instance=this;
		x.friction=0.95F;
		y.friction=0.95F;
	}
	
	@Override
	public void update(){
		x.update();
		y.update();
		if(Math.sqrt(x.speed*x.speed+y.speed*y.speed)<0.2){
			float rotation=UtilM.RF(360);
			x.wantedPoint=CricleUtil.sin(rotation)*2;
			y.wantedPoint=CricleUtil.cos(rotation)*2;
		}
	}
	
	@Override
	public void redner(){
		setUniform(uniforms.get(0), x.getPoint(),y.getPoint());
	}
	
	@Override
	public boolean getConditionForActivation(){
		return shouldRender;
	}
}
