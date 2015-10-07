package com.magiology.client.render.shaders.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.magiology.util.utilclasses.DataStalker;
import com.magiology.util.utilclasses.Util;

public abstract class ShaderAspectRenderer{
	
	protected Minecraft mc=Util.getMC();
	protected World world=Util.getTheWorld();
	protected EntityPlayer player=Util.getThePlayer();
	protected List<ShaderUniform> uniforms=new ArrayList<ShaderUniform>();
	private final ResourceLocation shaderLocation;
	private final String[] uniformNames;
	private final int shaderId;
	
	public ShaderAspectRenderer(String shaderName,int shaderId,String... uniformNames){
		shaderLocation=new ResourceLocation("shaders/post/"+shaderName+".json");
		this.uniformNames=uniformNames;
		this.shaderId=shaderId;
	}
	public void init(ShaderRunner handler){
		List listShaders=DataStalker.getVariable(ShaderGroup.class, "listShaders", handler.shaders[shaderId]);
		if(listShaders!=null&&!listShaders.isEmpty())for(String i:uniformNames){
			ShaderUniform uniform=((Shader)listShaders.get(0)).getShaderManager().getShaderUniform(i);
			if(uniform!=null)uniforms.add(uniform);
		}
	}

	public abstract void update();
	public abstract void redner();
	public abstract boolean getConditionForActivation();
	
	public final String[] getUniformNames(){
		return uniformNames;
	}
	public final ResourceLocation getShaderLocation(){
		return shaderLocation;
	}
	public final int getShaderId(){
		return shaderId;
	}
}