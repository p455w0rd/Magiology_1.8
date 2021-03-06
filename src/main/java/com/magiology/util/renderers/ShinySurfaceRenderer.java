package com.magiology.util.renderers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MatrixUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.codeinsert.ObjectProcessor;
import com.magiology.util.utilobjects.vectors.Vec3M;

/**
 * Created by LapisSea on 25.1.2016..
 */
public class ShinySurfaceRenderer extends VertexRenderer{

	private ColorF baseColor=new ColorF();

	private Vec3M modelOriginPos=new Vec3M();
	
	private ObjectProcessor<Double> lightStrenghtFilter;
	
	private ObjectProcessor<ColorF> lightColorFilter;
	
	public List<SpecularLight> lights=new ArrayList<>();
	
	public Matrix4f modelTransf=new Matrix4f();
	
	@Override
	public Renderer.RendererBase getRenderer(){
		return Renderer.POS_UV_COLOR_NORMAL;
	}

	@Override
	protected void tesselateQuads(ShadedQuad triangle){
		if(lights.isEmpty()){
			for(int b=0;b<4;b++){
				Vec3M
					finalVec=MatrixUtil.transformVector(new Vec3M(triangle.pos4[b].vector3D.xCoord, triangle.pos4[b].vector3D.yCoord, triangle.pos4[b].vector3D.zCoord), transformation),
					finalNormal=MatrixUtil.transformVector(triangle.normal1.add(0,0,0), new Vector3f(),rotation.x,rotation.y,rotation.z,1).normalize();
				Renderer.POS_UV_COLOR_NORMAL.addVertex(finalVec, triangle.pos4[b].texturePositionX, triangle.pos4[b].texturePositionY,baseColor,finalNormal);
			}
			return;
		}
		final boolean usesFilters=lightStrenghtFilter!=null&&lightColorFilter!=null;
		
		Vec3M
			finalNormal=MatrixUtil.transformVector(triangle.normal1.add(0,0,0), new Vector3f(),rotation.x,rotation.y,rotation.z,1).normalize(),
			playerPos=PartialTicksUtil.calculatePos(UtilM.getThePlayer()).add(0, UtilM.getThePlayer().getEyeHeight(),0);
		List<Vec3M> reflectionVects=new ArrayList<>();
		lights.forEach(light->reflectionVects.add(light.getLigtDirection().mul(-1).reflect(MatrixUtil.transformVector(finalNormal, modelTransf))));
		
		for(int b=0;b<4;b++){
			Vec3M finalVec=MatrixUtil.transformVector(new Vec3M(triangle.pos4[b].vector3D.xCoord, triangle.pos4[b].vector3D.yCoord, triangle.pos4[b].vector3D.zCoord), transformation);
			
			ColorF lightsColor=new ColorF(0,0,0,0);
			
			calcLightReflection(finalVec, finalNormal, playerPos, lightsColor, reflectionVects, usesFilters);
			
			lightsColor.a=0;
			Renderer.POS_UV_COLOR_NORMAL.addVertex(finalVec, triangle.pos4[b].texturePositionX, triangle.pos4[b].texturePositionY,baseColor.add(lightsColor),finalNormal);
		}
	}
	
	protected ColorF calcLightReflection(Vec3M pos, Vec3M normal, Vec3M playerPos, ColorF color, List<Vec3M> reflectionVects, boolean usesFilters){
		Vec3M toCameraVec=playerPos.sub(modelOriginPos).sub(pos);
		for(int i=0;i<lights.size();i++){
			
			double diff=usesFilters?
				lightStrenghtFilter.pocess(reflectionVects.get(i).lightProduct(toCameraVec)):
				reflectionVects.get(i).lightProduct(toCameraVec);
				
			if(diff>0){
				SpecularLight light=lights.get(i);
				
				ColorF specular=usesFilters?
					lightColorFilter.pocess(light.getColor().mul(Math.pow(diff,light.getPow()))):
					light.getColor().mul(Math.pow(diff,light.getPow()));
				color.r+=specular.r;
				color.g+=specular.g;
				color.b+=specular.b;
			}
		}
		
		return null;
	}

	public ColorF getBaseColor(){
		return baseColor;
	}

	public void setBaseColor(ColorF baseColor){
		this.baseColor=baseColor==null?new ColorF(1,1,1,1):baseColor;
	}

	public Vec3M getModelOriginPos(){
		return modelOriginPos;
	}

	public void setModelOriginPos(Vec3M modelOriginPos){
		this.modelOriginPos=modelOriginPos==null?new Vec3M():modelOriginPos;
	}

	public void addLight(SpecularLight light){
		light.lightDirection=light.lightDirection.normalize();
		lights.add(light);
	}
	public ObjectProcessor<Double> getLightStrenghtFilter(){
		return lightStrenghtFilter;
	}

	public void setLightStrenghtFilter(ObjectProcessor<Double> lightStrenghtFilter){
		this.lightStrenghtFilter=lightStrenghtFilter;
	}

	public ObjectProcessor<ColorF> getLightColorFilter(){
		return lightColorFilter;
	}

	public void setLightColorFilter(ObjectProcessor<ColorF> lightColorFilter){
		this.lightColorFilter=lightColorFilter;
	}

	public static class SpecularLight{
		private float pow;
		private ColorF color;
		private Vec3M lightDirection;

		public SpecularLight(Vec3M lightDirection, ColorF color, float pow){
			this.lightDirection=lightDirection;
			this.color=color;
			this.pow=pow;
		}

		public float getPow(){
			return pow;
		}

		public void setPow(float pow){
			this.pow=pow;
		}

		public ColorF getColor(){
			return color;
		}

		public void setColor(ColorF color){
			this.color=color;
		}

		public Vec3M getLigtDirection(){
			return lightDirection;
		}

		public void setLightDirection(Vec3M lightDirection){
			this.lightDirection=lightDirection;
		}
	}
}
