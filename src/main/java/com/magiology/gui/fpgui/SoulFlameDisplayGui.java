package com.magiology.gui.fpgui;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.magiology.core.MReference;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;
import com.magiology.objhelper.Get.Render;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.TessHelper;

public class SoulFlameDisplayGui extends FirstPersonGui{
	public static SoulFlameDisplayGui instance=new SoulFlameDisplayGui();
	private ExtendedPlayerData data;
	private int soulFlame,maxSoulFlame,fireId;
	private float pngW=1F/41F,pngH=1/75F;
	@Override
	public void render(int xScreen, int yScreen, float partialTicks){
		if(data==null)data=ExtendedPlayerData.get(player);
		if(data==null)return;
		if(data.player!=player){
			data=ExtendedPlayerData.get(player);
		}
		if(data==null)return;
		maxSoulFlame=data.maxSoulFlame;
		soulFlame=data.soulFlame;
		TessHelper.bindTexture(new ResourceLocation(MReference.MODID,"/textures/gui/fp/soulFlame.png"));
		GL11H.SetUpOpaqueRendering(1);
		GL11.glTranslatef(0, 10, 0);
		renderSlider();
		renderMain();
		renderFire();
		GL11H.EndOpaqueRendering();
		Render.FR().drawStringWithShadow(" "+soulFlame+"/"+maxSoulFlame, 0, 90, Color.WHITE.hashCode());
	}
	private void renderMain(){
		drawRect(pngW, pngH, 0, 0, 0, 0, 28, 75);
	}
	private void renderSlider(){
		float buffer=(float)soulFlame/(float)maxSoulFlame,alpha=(float)(soulFlame+maxSoulFlame*0.3)/maxSoulFlame;
		GL11.glColor4d(1, 1, 1, 0.7*alpha);
		drawRect(pngW, pngH, 10, 1+44-44*buffer, 28, 44-44*buffer, 13, 44*buffer+1);
		GL11.glColor4d(1, 1, 1, 1);
	}
	private void renderFire(){
		long time=player.worldObj.getTotalWorldTime();
		int speed=120,cut=(int)(time%speed);
		if(cut>speed/2)cut=speed-cut;
		float yellow=((float)cut/(float)speed)*2;
		GL11.glColor4d(1, yellow, 0.4, 1);
		drawRect(pngW, pngH, 13, 48, 29, 45+fireId*7, 7, 6);
		GL11.glColor4d(1, 1, 1, 1);
	}
	@Override
	public void update(){
		if(player==null)return;
		if(player.worldObj.getTotalWorldTime()%10==0)fireId++;
		if(fireId>2)fireId=0;
	}
}
