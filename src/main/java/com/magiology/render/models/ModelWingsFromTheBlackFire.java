package com.magiology.render.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import com.magiology.core.Config;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData.CyborgWingsFromTheBlackFireData;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.NormalizedVertixBuffer;
import com.magiology.objhelper.helpers.renderers.TessHelper;
import com.magiology.objhelper.helpers.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.objhelper.vectors.Vec8F;
import com.magiology.render.Textures;

public class ModelWingsFromTheBlackFire extends ModelBiped{
	private static float p=1F/16F;
	private static ComplexCubeModel[] models;
	private static ComplexCubeModel[][] modelsBack;
	private static NormalizedVertixBuffer buff=TessHelper.getNVB();
	
	public static void wings3D(boolean bol){
		if(models==null)return;
		for(int a=0;a<models.length;a++)models[a].willSideRender=new boolean[]{bol,bol,bol,true,bol,bol};
		for(int a=0;a<modelsBack.length;a++)for(int b=0;b<modelsBack[0].length;b++)modelsBack[a][b].willSideRender=new boolean[]{bol,bol,bol,true,bol,bol};
	}
	private static Vec8F[] genUV(int x1,int y1){
		float x=(1F*x1)/7F,y=y1/6F,x2=2/14F,y2=1/6F;
		return new Vec8F[]{new Vec8F(
				x,    y+y2,
				x,    y,
				x+x2, y, 
				x+x2, y+y2
				).rotate().mirror()};
	}
	
	private static void init(float[][] renderRotations){
		for(int a=0;a<renderRotations.length;a++){
			float thickness=0;
			if(a>0)thickness=(((float)a)/((float)renderRotations.length))*p;
			models=ArrayUtils.add(models, new ComplexCubeModel(thickness, thickness, 0, p*3-thickness, p*3-thickness, p*7,genUV(a, 0),new ResourceLocation[]{Textures.WingColors}));
			ComplexCubeModel[] WingPart={};
			for(int b=0;b<5;b++){
				float njnj=(float)(renderRotations.length-a)/(float)renderRotations.length;
				WingPart=ArrayUtils.add(WingPart, new ComplexCubeModel(thickness, thickness+njnj*(b*0.02F), thickness/2, p*3-thickness, p*3-thickness-njnj*(b*0.02F), p*8-thickness,genUV(a, b),new ResourceLocation[]{Textures.WingColors}));
			}
			modelsBack=ArrayUtils.add(modelsBack, WingPart);
			wings3D(Config.isWingsThick());
		}
		
		for(int a=0;a<modelsBack.length;a++){
			for(int b=0;b<modelsBack[a].length;b++)for(int c=0;c<models[a].UVs.length;c++){
				if(c==0)modelsBack[a][b].UVs[c]=genUV(a, b+1)[0].rotate();
				else if(c==1)modelsBack[a][b].UVs[c]=genUV(a, b+1)[0].mirror().rotate();
				else if(c==4){
					modelsBack[a][b].UVs[c]=genUV(a, b+1)[0];
					modelsBack[a][b].UVs[c].x2=modelsBack[a][b].UVs[c].x4;
					modelsBack[a][b].UVs[c].x3=modelsBack[a][b].UVs[c].x1;
				}
				else modelsBack[a][b].UVs[c]=genUV(a, b+1)[0];
				
				if(c==0)modelsBack[a][b].UVs2[c]=genUV(a, b+1)[0].rotate();
				else if(c==1)modelsBack[a][b].UVs2[c]=genUV(a, b+1)[0].mirror().rotate();
				else if(c==4){
					modelsBack[a][b].UVs2[c]=genUV(a, b+1)[0];
					modelsBack[a][b].UVs2[c].x2=modelsBack[a][b].UVs[c].x4;
					modelsBack[a][b].UVs2[c].x3=modelsBack[a][b].UVs[c].x1;
					modelsBack[a][b].UVs2[c]=modelsBack[a][b].UVs2[c].rotate().mirror().rotate().rotate().rotate();
				}
				else modelsBack[a][b].UVs2[c]=genUV(a, b+1)[0].rotate().mirror().rotate().rotate().rotate();
			}
			for(int c=0;c<models[a].UVs.length;c++){
				//uv 1
				if(c==0){
					models[a].UVs[c]=genUV(a, 0)[0].mirror().rotate();
					models[a].UVs[c].y2=models[a].UVs[c].y4;
					models[a].UVs[c].y3=models[a].UVs[c].y1;
					models[a].UVs[c]=models[a].UVs[c].rotate().mirror().rotate().rotate().rotate();
				}
				else if(c==1)models[a].UVs[c]=genUV(a, 0)[0].mirror().rotate();
				else if(c==4){
					models[a].UVs[c]=genUV(a, 0)[0];
					models[a].UVs[c].x2=models[a].UVs[c].x4;
					models[a].UVs[c].x3=models[a].UVs[c].x1;
				}
				else models[a].UVs[c]=genUV(a, 0)[0];
				//uv 2
				if(c==0)models[a].UVs2[c]=genUV(a, 0)[0].rotate();
				else if(c==1){
					models[a].UVs2[c]=genUV(a, 0)[0].mirror().rotate();
					models[a].UVs2[c].y2=models[a].UVs[c].y4;
					models[a].UVs2[c].y3=models[a].UVs[c].y1;
				}
				else if(c==4){
					models[a].UVs2[c]=genUV(a, 0)[0];
					models[a].UVs2[c].x2=models[a].UVs[c].x4;
					models[a].UVs2[c].x3=models[a].UVs[c].x1;
					models[a].UVs2[c]=models[a].UVs2[c].rotate().mirror().rotate().rotate().rotate();
				}
				else models[a].UVs2[c]=genUV(a, 0)[0] .rotate().mirror().rotate().rotate().rotate();
			}
		}
	}
	
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		//calculating and safety
		if(!(entity instanceof EntityPlayer))return;
		EntityPlayer player=(EntityPlayer)entity;
		CyborgWingsFromTheBlackFireData data=validateForRender(player);
		if(data==null)return;
		float rotation=0;
		rotation=Helper.calculateRenderPos(data.prevPlayerAngle, data.playerAngle);
		float[][] renderRotations=new float[7][3];
		for(int a=0;a<data.calcRotationAnglesBase.length;a++)for(int a1=0;a1<data.calcRotationAnglesBase[a].length;a1++)renderRotations[a][a1]=Helper.calculateRenderPos(data.calcPrevRotationAnglesBase[a][a1],data.calcRotationAnglesBase[a][a1]);
		if(models==null)init(renderRotations);
		//rendering
		GL11H.protect();
		GL11H.culFace(Config.isWingsThick());
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glTranslated(-p*1.5,-p*0.5,p*3.5);
		GL11H.rotateXYZ(-rotation, 0, 0);
		GL11.glPushMatrix();
		GL11.glTranslated(-p*4, 0, 0);
		GL11H.rotateXYZAt(0,-90+10, 0,  p*1.5, p*1.5,0);
		renderWingBase(renderRotations,false,data);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslated(p*4, 0, 0);
		GL11H.rotateXYZAt(0, 90-10, 0, p*1.5, p*1.5,0);
		renderWingBase(renderRotations,true,data);
		GL11.glPopMatrix();
		GL11H.endProtection();
//		Config.setWingsThick(true);
	}
	public void renderWingBase(float[][] renderRotations,boolean side, CyborgWingsFromTheBlackFireData data){
		for(int a=0;a<renderRotations.length;a++){
			renderRotations[a][1]*=-1;
			renderRotations[a][2]*=-1;
		}
		if(!side){
			for(int a=0;a<modelsBack.length;a++){
				for(int b=0;b<modelsBack[a].length;b++)modelsBack[a][b].flipUVs();
				models[a].flipUVs();
			}
		}
		GL11.glPushMatrix();
		for(int a1=0;a1<models.length;a1++){
			ComplexCubeModel a=models[a1];
			float precent=(float)a1/(float)models.length;
			GL11H.rotateXYZAt(renderRotations[a1][0],renderRotations[a1][1],renderRotations[a1][2], a.points[0].x/2, a.points[0].y/2, 0, true);
			a.draw();
			GL11.glPushMatrix();
			GL11.glTranslated((a.points[3].x-a.points[4].x)*(side?-1:1), 0, 0);
			for(int l=0;l<modelsBack[a1].length;l++){
				ComplexCubeModel part=modelsBack[a1][l];
				part.draw(); 
				GL11.glTranslatef((float)(part.points[3].x-part.points[4].x)*(side?-1:1), 0, -p/2*(l*0.6F));
				GL11H.rotateXYZ(3, 2, 0);
			}
			GL11.glColor4d(1,1,1,1);
			GL11.glPopMatrix();
			GL11.glTranslatef(0, 0, a.points[3].getZ());
		}
		GL11.glPopMatrix();
		if(!side){
			for(int a=0;a<modelsBack.length;a++){
				for(int b=0;b<modelsBack[a].length;b++)modelsBack[a][b].flipUVs();
				models[a].flipUVs();
			}
		}
	}
	
	
	
	//static...
	private CyborgWingsFromTheBlackFireData validateForRender(EntityPlayer player){
		ComplexPlayerRenderingData playerRenderingData=ComplexPlayerRenderingData.get(player);
		if(playerRenderingData==null){
			ComplexPlayerRenderingData.registerEntityPlayerRenderer(player);
			ComplexPlayerRenderingData.get(player).getCyborgWingsFromTheBlackFireData();
		}
		return ComplexPlayerRenderingData.get(player).getCyborgWingsFromTheBlackFireData();
	}
}
