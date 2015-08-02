package com.magiology.render.itemrender;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData.PowerHandData;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData.PowerHandData_sub_fingerData;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.objhelper.vectors.Vec8F;
import com.magiology.render.Textures;

public class ItemRendererTheHand implements IItemRenderer{
	private final float p=1F/16F;
	public ResourceLocation[] blank1={new ResourceLocation("noTexture")};
	public ComplexCubeModel base;
	float g=1F/3F;
	public FingerModel[] fingers;
	public ItemRendererTheHand(){}
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type){return type!=ItemRenderType.INVENTORY;}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper){
		return true;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... objs){
		if(base==null||fingers==null)init();
		EntityPlayer player=null;
		PowerHandData data=null;
		try{player=(EntityPlayer)objs[1];}catch(Exception e){}
		if(player==null)return;
		data=ComplexPlayerRenderingData.getFastPowerHandData(player);
		if(data==null)return;
		
		GL11.glPushMatrix();
		if(type==ItemRenderType.ENTITY){
			GL11.glTranslatef(-0.7F, -0.9F,-0.1F);
		}else if(type==ItemRenderType.EQUIPPED){
			
			GL11.glRotated(-110, 1, -0.4, -0.2);
			GL11.glTranslated(-0.6, -2.2,-1.5);
			GL11.glScaled(1.2, 1.2, 1.2);
			long time=Minecraft.getSystemTime();
			GL11.glTranslated(0, data.thirdPresonPos, 0);
			GL11.glRotated(-data.thirdPresonPosSpeed*350+20, 1, 0, 0);
		}
		
		float[] handRotationRender=Helper.calculateRenderPosArray(data.prevHandRotationCalc,data.handRotationCalc);
		float x1=p*6F,y1=p*10,z1=p*2;
		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON)GL11.glTranslated(handRotationRender[3]/30,handRotationRender[4]/30,handRotationRender[5]/30);
		GL11.glTranslated(x1,y1,z1);
		GL11.glRotated(-20, 0, 1, 0);
		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON)GL11H.rotateXYZ(handRotationRender[0],handRotationRender[1],handRotationRender[2]);
		GL11.glTranslated(-x1, -y1, -z1);
		GL11.glRotated(20, 1, 0, 1);
		if(type==ItemRenderType.INVENTORY)GL11.glTranslatef(0.4F, 0.7F, -0.6F);
		else GL11.glTranslatef(0.6F, 0.7F, -0.3F);
		
		base.draw();
		boolean first=true;
		float x=-p*0.9F,y=-p*0.4F,z=p*5.5F;
		GL11.glTranslatef(x,y,z);
		for(int a=0;a<fingers.length;a++){
			FingerModel a1=fingers[a];
			a1.draw(data.fingerData[a]);
			if(first){
				GL11.glTranslatef(-x,-y,-z);
				GL11.glTranslatef(0.002F, p,0.03F);
				GL11.glRotated(10, 0, 1, 0);
				first=false;
			}else{
				GL11.glTranslatef(p*1.8F, 0,0);
				GL11.glRotated(-10.0/2.0, 0, 1, 0);
			}
		}
		GL11.glPopMatrix();
	}
	public class FingerModel{
		public ComplexCubeModel[]cubeModels;
		public FingerModel(float[] xyzPosRot1,float[] xyzPosRot2,float[] xyzPosRot3,float[][] boxes,Vec8F[][] txtpoints,ResourceLocation[][] sidedtextures){
			cubeModels=new ComplexCubeModel[]{
					new ComplexCubeModel(boxes[0][0],boxes[0][1],boxes[0][2], boxes[0][3], boxes[0][4], boxes[0][5],txtpoints[0], sidedtextures[0]),
					new ComplexCubeModel(boxes[1][0],boxes[1][1],boxes[1][2], boxes[1][3], boxes[1][4], boxes[1][5],txtpoints[1], sidedtextures[1]),
					new ComplexCubeModel(boxes[2][0],boxes[2][1],boxes[2][2], boxes[2][3], boxes[2][4], boxes[2][5],txtpoints[2], sidedtextures[2])};
		}
		public void draw(PowerHandData_sub_fingerData data){
			GL11.glPushMatrix();
			for(int a=0;a<cubeModels.length;a++){
				float[] rot=Helper.calculateRenderPosArray(data.prevcalcXyzPosRot[a], data.calcXyzPosRot[a]);
				GL11H.rotateXYZAt(rot[3],rot[4],rot[5],rot[0],rot[1]+
						(cubeModels[a].points[2].y-cubeModels[a].points[4].y)/2F
						,rot[2]);
				GL11.glTranslated(0.0001,0,-(cubeModels[a].points[2].z-cubeModels[a].points[1].z));
				cubeModels[a].draw();
			}
			GL11.glPopMatrix();
		}
	}
	public void init(){
		base=new ComplexCubeModel(0, 0, 0, p*7, p*2.5F, p*9,new Vec8F[]{Vec8F.all().rotate().mirror(),Vec8F.all().rotate().mirror(),Vec8F.all().mirror(),Vec8F.all().mirror(),Vec8F.all().mirror(),Vec8F.all().mirror()}, new ResourceLocation[]{Textures.handBaseSide,Textures.handBaseSide,Textures.handBaseTop,Textures.handBaseBotom,Textures.handBaseSide2,Textures.handBaseSide2});
		fingers=new FingerModel[]{
				//#thumb
				new FingerModel(
						new float[]{p*0F,p*0F,p*0F, /**/-10,20,0},
						new float[]{p*0.5F,p*0.5F,p*0.5F, 0,-15,0},																						 new float[]{0,0,0,0,0,0},
						new float[][]{
						{0,0,0,p*1.8F,p*1.8F,p*4.2F},
						{0,0,0,p*1.8F,p*1.799F,p*2.5F},
						{0,0,0,0,0,0}},
						new Vec8F[][]{
								{Vec8F.all().rotate().mirror().edit(0,-0.27F,0,-0.27F,0,0,0,0),Vec8F.all().rotate().mirror().edit(0,0,0,0,0,0.27F,0,0.27F),Vec8F.all().mirror().edit(0,0.27F,0,0,0,0,0,0.27F),Vec8F.all().edit(0,-0.27F,0,0,0,0,0,-0.27F),Vec8F.all(),Vec8F.all(),},{
								 Vec8F.all().rotate().mirror().edit(0,0,0,0,0,0.73F,0,0.73F),Vec8F.all().rotate().mirror().edit(0,-0.73F,0,-0.73F,0,0,0,0),Vec8F.all().mirror().edit(0,0,0,-0.73F,0,-0.73F,0,0),Vec8F.all().edit(0,0,0,0.73F,0,0.73F,0,0),Vec8F.all(),Vec8F.all(),},{Vec8F.all()}},
						new ResourceLocation[][]{
							  	  new ResourceLocation[]{Textures.handThumbSide,Textures.handThumbSide,Textures.handThumbTop,Textures.handThumbBottom,Textures.handThumbTxtClip,Textures.handThumbEnd
								},new ResourceLocation[]{Textures.handThumbSide,Textures.handThumbSide,Textures.handThumbTop,Textures.handThumbBottom,Textures.handThumbTxtClip,Textures.handThumbEnd},blank1}),
						
				new FingerModel(new float[]{0,p*0.75F,0, /**/0,0,0}, new float[]{0,p*0.5F,0, 0,0,0}, new float[]{0,p*0.5F,0, 0,0,0},
						new float[][]{
						{0,0,0,p*1.5F,p*1.499F,p*3},
						{0,0,0,p*1.5F,p*1.5F,p*2.5F},
						{0,0,0,p*1.5F,p*1.5F,p*2F}},
						new Vec8F[][]{{
							   Vec8F.all().rotate().mirror().edit(0, 0, 0, 0, 0, g*2, 0, g*2),Vec8F.all().rotate().mirror().edit(0, 0, 0, 0, 0, g*2, 0, g*2),Vec8F.all().mirror().edit(0, g*2, 0, 0, 0, 0, 0, g*2),Vec8F.all().mirror().edit(0, g*2, 0, 0, 0, 0, 0, g*2),Vec8F.all(),Vec8F.all()
							},{Vec8F.all().rotate().mirror().edit(0, -g, 0, -g, 0, g, 0, g),Vec8F.all().rotate().mirror().edit(0, -g, 0, -g, 0, g, 0, g),Vec8F.all().mirror().edit(0, g, 0, -g, 0, -g, 0, g),Vec8F.all().mirror().edit(0, g, 0, -g, 0, -g, 0, g),Vec8F.all(),Vec8F.all()
							},{Vec8F.all().rotate().mirror().edit(0, -g*2, 0, -g*2, 0, 0, 0, 0),Vec8F.all().rotate().mirror().edit(0, -g*2, 0, -g*2, 0, 0, 0, 0),Vec8F.all().mirror().edit(0, 0, 0, -g*2, 0, -g*2, 0, 0),Vec8F.all().mirror().edit(0, 0, 0, -g*2, 0, -g*2, 0, 0),Vec8F.all(),Vec8F.all()}},
						new ResourceLocation[][]{
						  new ResourceLocation[]{Textures.handNormalFingerSide,Textures.handNormalFingerSide,Textures.handNormalFingerTop,Textures.handNormalFingerBottom,Textures.handNormalFingerTxtClip,Textures.handNormalFingerTxtClip
						},new ResourceLocation[]{Textures.handNormalFingerSide,Textures.handNormalFingerSide,Textures.handNormalFingerTop,Textures.handNormalFingerBottom,Textures.handNormalFingerTxtClip,Textures.handNormalFingerTxtClip
						},new ResourceLocation[]{Textures.handNormalFingerSide,Textures.handNormalFingerSide,Textures.handNormalFingerTop,Textures.handNormalFingerBottom,Textures.handnormalFingerEnd,Textures.handNormalFingerTxtClip}}),
				new FingerModel(new float[]{0,p*0.75F,0, 0,0,0}, new float[]{0,p*0.5F,0, 0,0,0}, new float[]{0,p*0.5F,0, 0,0,0},
						new float[][]{
						{0,0,0,p*1.5F,p*1.499F,p*3},
						{0,0,0,p*1.5F,p*1.5F,p*2.5F},
						{0,0,0,p*1.5F,p*1.5F,p*2F}},
						new Vec8F[][]{{
							   Vec8F.all().rotate().mirror().edit(0, 0, 0, 0, 0, g*2, 0, g*2),Vec8F.all().rotate().mirror().edit(0, 0, 0, 0, 0, g*2, 0, g*2),Vec8F.all().mirror().edit(0, g*2, 0, 0, 0, 0, 0, g*2),Vec8F.all().mirror().edit(0, g*2, 0, 0, 0, 0, 0, g*2),Vec8F.all(),Vec8F.all()
							},{Vec8F.all().rotate().mirror().edit(0, -g, 0, -g, 0, g, 0, g),Vec8F.all().rotate().mirror().edit(0, -g, 0, -g, 0, g, 0, g),Vec8F.all().mirror().edit(0, g, 0, -g, 0, -g, 0, g),Vec8F.all().mirror().edit(0, g, 0, -g, 0, -g, 0, g),Vec8F.all(),Vec8F.all()
							},{Vec8F.all().rotate().mirror().edit(0, -g*2, 0, -g*2, 0, 0, 0, 0),Vec8F.all().rotate().mirror().edit(0, -g*2, 0, -g*2, 0, 0, 0, 0),Vec8F.all().mirror().edit(0, 0, 0, -g*2, 0, -g*2, 0, 0),Vec8F.all().mirror().edit(0, 0, 0, -g*2, 0, -g*2, 0, 0),Vec8F.all(),Vec8F.all()}},
						new ResourceLocation[][]{
						  new ResourceLocation[]{Textures.handNormalFingerSide,Textures.handNormalFingerSide,Textures.handNormalFingerTop,Textures.handNormalFingerBottom,Textures.handNormalFingerTxtClip,Textures.handNormalFingerTxtClip
						},new ResourceLocation[]{Textures.handNormalFingerSide,Textures.handNormalFingerSide,Textures.handNormalFingerTop,Textures.handNormalFingerBottom,Textures.handNormalFingerTxtClip,Textures.handNormalFingerTxtClip
						},new ResourceLocation[]{Textures.handNormalFingerSide,Textures.handNormalFingerSide,Textures.handNormalFingerTop,Textures.handNormalFingerBottom,Textures.handnormalFingerEnd,Textures.handNormalFingerTxtClip}}),
				new FingerModel(new float[]{0,p*0.75F,0, 0,0,0}, new float[]{0,p*0.5F,0, 0,0,0}, new float[]{0,p*0.5F,0, 0,0,0},
						new float[][]{
						{0,0,0,p*1.5F,p*1.499F,p*3},
						{0,0,0,p*1.5F,p*1.5F,p*2.5F},
						{0,0,0,p*1.5F,p*1.5F,p*2F}},
						new Vec8F[][]{{
							   Vec8F.all().rotate().mirror().edit(0, 0, 0, 0, 0, g*2, 0, g*2),Vec8F.all().rotate().mirror().edit(0, 0, 0, 0, 0, g*2, 0, g*2),Vec8F.all().mirror().edit(0, g*2, 0, 0, 0, 0, 0, g*2),Vec8F.all().mirror().edit(0, g*2, 0, 0, 0, 0, 0, g*2),Vec8F.all(),Vec8F.all()
							},{Vec8F.all().rotate().mirror().edit(0, -g, 0, -g, 0, g, 0, g),Vec8F.all().rotate().mirror().edit(0, -g, 0, -g, 0, g, 0, g),Vec8F.all().mirror().edit(0, g, 0, -g, 0, -g, 0, g),Vec8F.all().mirror().edit(0, g, 0, -g, 0, -g, 0, g),Vec8F.all(),Vec8F.all()
							},{Vec8F.all().rotate().mirror().edit(0, -g*2, 0, -g*2, 0, 0, 0, 0),Vec8F.all().rotate().mirror().edit(0, -g*2, 0, -g*2, 0, 0, 0, 0),Vec8F.all().mirror().edit(0, 0, 0, -g*2, 0, -g*2, 0, 0),Vec8F.all().mirror().edit(0, 0, 0, -g*2, 0, -g*2, 0, 0),Vec8F.all(),Vec8F.all()}},
						new ResourceLocation[][]{
						  new ResourceLocation[]{Textures.handNormalFingerSide,Textures.handNormalFingerSide,Textures.handNormalFingerTop,Textures.handNormalFingerBottom,Textures.handNormalFingerTxtClip,Textures.handNormalFingerTxtClip
						},new ResourceLocation[]{Textures.handNormalFingerSide,Textures.handNormalFingerSide,Textures.handNormalFingerTop,Textures.handNormalFingerBottom,Textures.handNormalFingerTxtClip,Textures.handNormalFingerTxtClip
						},new ResourceLocation[]{Textures.handNormalFingerSide,Textures.handNormalFingerSide,Textures.handNormalFingerTop,Textures.handNormalFingerBottom,Textures.handnormalFingerEnd,Textures.handNormalFingerTxtClip}}),
				new FingerModel(new float[]{0,p*0.75F,0, 0,0,0}, new float[]{0,p*0.5F,0, 0,0,0}, new float[]{0,p*0.5F,0, 0,0,0},
						new float[][]{
						{0,0,0,p*1.5F,p*1.499F,p*3},
						{0,0,0,p*1.5F,p*1.5F,p*2.5F},
						{0,0,0,p*1.5F,p*1.5F,p*2F}},
						new Vec8F[][]{{
							   Vec8F.all().rotate().mirror().edit(0, 0, 0, 0, 0, g*2, 0, g*2),Vec8F.all().rotate().mirror().edit(0, 0, 0, 0, 0, g*2, 0, g*2),Vec8F.all().mirror().edit(0, g*2, 0, 0, 0, 0, 0, g*2),Vec8F.all().mirror().edit(0, g*2, 0, 0, 0, 0, 0, g*2),Vec8F.all(),Vec8F.all()
							},{Vec8F.all().rotate().mirror().edit(0, -g, 0, -g, 0, g, 0, g),Vec8F.all().rotate().mirror().edit(0, -g, 0, -g, 0, g, 0, g),Vec8F.all().mirror().edit(0, g, 0, -g, 0, -g, 0, g),Vec8F.all().mirror().edit(0, g, 0, -g, 0, -g, 0, g),Vec8F.all(),Vec8F.all()
							},{Vec8F.all().rotate().mirror().edit(0, -g*2, 0, -g*2, 0, 0, 0, 0),Vec8F.all().rotate().mirror().edit(0, -g*2, 0, -g*2, 0, 0, 0, 0),Vec8F.all().mirror().edit(0, 0, 0, -g*2, 0, -g*2, 0, 0),Vec8F.all().mirror().edit(0, 0, 0, -g*2, 0, -g*2, 0, 0),Vec8F.all(),Vec8F.all()}},
						new ResourceLocation[][]{
						  new ResourceLocation[]{Textures.handNormalFingerSide,Textures.handNormalFingerSide,Textures.handNormalFingerTop,Textures.handNormalFingerBottom,Textures.handNormalFingerTxtClip,Textures.handNormalFingerTxtClip
						},new ResourceLocation[]{Textures.handNormalFingerSide,Textures.handNormalFingerSide,Textures.handNormalFingerTop,Textures.handNormalFingerBottom,Textures.handNormalFingerTxtClip,Textures.handNormalFingerTxtClip
						},new ResourceLocation[]{Textures.handNormalFingerSide,Textures.handNormalFingerSide,Textures.handNormalFingerTop,Textures.handNormalFingerBottom,Textures.handnormalFingerEnd,Textures.handNormalFingerTxtClip}})
		};
	}
}
