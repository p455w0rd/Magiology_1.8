package com.magiology.render.shaders.core;

import java.nio.FloatBuffer;
import java.util.List;

import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;

import com.magiology.util.utilclasses.DataStalker;

public class ShaderUniformEditor{
	public static void setUniform(ShaderGroup sg,String uniformName,float... values){
		List listShaders=DataStalker.getVariable(ShaderGroup.class, "listShaders", sg);
		if(listShaders.isEmpty())return;
		setUniform(((Shader)listShaders.get(0)).getShaderManager().getShaderUniform(uniformName), values);
	}
	public static void setUniform(ShaderUniform uniform,float... values){
		uniform.set(values);
	}
	public static float[] getUniform(ShaderGroup sg,String uniformName){
		List listShaders=DataStalker.getVariable(ShaderGroup.class, "listShaders", sg);
		if(listShaders.isEmpty())return null;
		ShaderUniform uniform=((Shader)listShaders.get(0)).getShaderManager().getShaderUniform(uniformName);
		if(uniform==null)return null;
		return getUniform(uniform);
	}
	public static float[] getUniform(ShaderUniform uniform){
		FloatBuffer values=DataStalker.getVariable(ShaderUniform.class, "field_148098_f", uniform);
		if(values==null)return null;
		int size=DataStalker.getVariable(ShaderUniform.class, "field_148103_c", uniform);
		values.position(0);
		float[] result=new float[size];
		for(int i=0;i<size;i++)result[i]=values.get();
		return result;
	}
}