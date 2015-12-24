package com.magiology.client.render.shaders.core;

import java.nio.*;
import java.util.*;

import net.minecraft.client.*;
import net.minecraft.client.shader.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.util.utilclasses.*;

public abstract class ShaderAspectRenderer{
	
	protected Minecraft mc=UtilM.getMC();
	protected World world=UtilM.getTheWorld();
	protected EntityPlayer player=UtilM.getThePlayer();
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
		if(listShaders!=null)for(String i:uniformNames){
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
	
	protected static final float[] getUniform(ShaderUniform uniform){
		FloatBuffer values=DataStalker.getVariable(ShaderUniform.class, "field_148098_f", uniform);
		if(values==null)return null;
		int size=DataStalker.getVariable(ShaderUniform.class, "field_148103_c", uniform);
		return values.array();
//		values.position(0);
//		float[] result=new float[size];
//		for(int i=0;i<size;i++)result[i]=values.get();
//		return result;
	}
	protected static final float[] getUniform(ShaderGroup sg,String uniformName){
		List listShaders=DataStalker.getVariable(ShaderGroup.class, "listShaders", sg);
		if(listShaders.isEmpty())return null;
		ShaderUniform uniform=((Shader)listShaders.get(0)).getShaderManager().getShaderUniform(uniformName);
		if(uniform==null)return null;
		return ShaderAspectRenderer.getUniform(uniform);
	}
	protected static final void setUniform(ShaderUniform uniform,float... values){
		uniform.set(values);
	}
	protected static final void setUniform(ShaderGroup sg,String uniformName,float... values){
		List listShaders=DataStalker.getVariable(ShaderGroup.class, "listShaders", sg);
		if(listShaders.isEmpty())return;
		ShaderAspectRenderer.setUniform(((Shader)listShaders.get(0)).getShaderManager().getShaderUniform(uniformName), values);
	}
}
