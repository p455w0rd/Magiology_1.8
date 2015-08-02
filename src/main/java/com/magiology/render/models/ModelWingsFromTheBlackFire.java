package com.magiology.render.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData.CyborgWingsFromTheBlackFireData;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.tessellatorscripts.ComplexCubeModel;

public class ModelWingsFromTheBlackFire extends ModelBiped{
	float p=1F/16F;
	static ComplexCubeModel[] models;
	static ComplexCubeModel[][] modelsBack;
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
		if(models==null)for(int a=0;a<renderRotations.length;a++){
			float thickness=0;
			if(a>0)thickness=(((float)a)/((float)renderRotations.length))*p;
			models=ArrayUtils.add(models, new ComplexCubeModel(thickness, thickness, 0, p*3-thickness, p*3-thickness, p*7));
			ComplexCubeModel[] WingPart={};
			for(int b=0;b<5;b++){
				float njnj=(float)(renderRotations.length-a)/(float)renderRotations.length;
				WingPart=ArrayUtils.add(WingPart, new ComplexCubeModel(thickness, thickness+njnj*(b*0.02F), thickness/2, p*3-thickness, p*3-thickness-njnj*(b*0.02F), p*8-thickness));
			}
			modelsBack=ArrayUtils.add(modelsBack, WingPart);
		}
		//rendering
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPushMatrix();
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
		GL11.glPopMatrix();
	}
	public void renderWingBase(float[][] renderRotations,boolean side, CyborgWingsFromTheBlackFireData data){
		renderRotations=renderRotations.clone();
		
		for(int a=0;a<renderRotations.length;a++){
			renderRotations[a][1]*=-1;
			renderRotations[a][2]*=-1;
		}
		GL11.glPushMatrix();
		for(int a1=0;a1<models.length;a1++){
			ComplexCubeModel a=models[a1];
			float precent=(float)a1/(float)models.length;
			GL11H.rotateXYZAt(renderRotations[a1][0],renderRotations[a1][1],renderRotations[a1][2], a.points[0].x/2, a.points[0].y/2, 0, true);
			GL11.glColor4d(0.2+precent*0.3,0.3+precent*0.2,0.4+precent*0.6,1);
			a.draw();
			GL11.glPushMatrix();
			GL11.glTranslated((a.points[3].x-a.points[4].x)*(side?-1:1), 0, 0);
			for(int l=0;l<modelsBack[a1].length;l++){
				ComplexCubeModel part=modelsBack[a1][l];
				double 
				r=0.3+l*0.1,
				g=0.4+precent*0.4,
				b=0.5+precent*0.5;
				if(l==4||a1==0||a1==6){
					r-=0.25;
					g-=0.3;
					b-=0.3;
				}
				r=r>1?1:r;
				g=g>1?1:g;
				b=b>1?1:b;
				GL11.glColor4d(r,g,b,1);
				part.draw(); 
				GL11.glTranslatef((float)(part.points[3].x-part.points[4].x)*(side?-1:1), 0, -p/2*(l*0.6F));
				GL11H.rotateXYZ(3, 2, 0);
			}
			GL11.glColor4d(1,1,1,1);
			GL11.glPopMatrix();
			GL11.glTranslatef(0, 0, a.points[3].getZ());
		}
		GL11.glPopMatrix();
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
