package com.magiology.gui.custom;

import java.util.ArrayList;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.core.MReference;
import com.magiology.gui.custom.hud.HUD;
import com.magiology.util.renderers.GL11H;
import com.magiology.util.renderers.TessHelper;
import com.magiology.util.utilclasses.Helper.H;
import com.magiology.util.utilobjects.ColorF;

public class DownloadingIcon {
	private static ResourceLocation main=new ResourceLocation(MReference.MODID,"/textures/gui/downloading.png");
	
	private static ArrayList<Drop> drops=new ArrayList<Drop>();
	private static int timeout=0;
	
	public static void update(){
		if(timeout>0)timeout--;
		else return;
		if(H.RB(0.5))drops.add(new Drop(new Vector2f(H.RF(130)+14, 60),H.RF(0.25)+0.75F));
		for(int i=0;i<drops.size();i++){
			Drop drop=drops.get(i);
			if(!drop.dead){
				drop.speed++;
				drop.prevPos=drop.pos;
				
				drop.pos.y+=drop.speed;
				drop.alpha=(260F-drop.pos.y)/260*2;
				if(drop.alpha<0)drop.dead=true;
			}else drops.remove(i);
		}
	}
	public static void draw(ColorF color){
		timeout=40;
		GL11.glPushMatrix();
		TessHelper.bindTexture(main);
		
		for(Drop drop:drops){
			GL11.glPushMatrix();
			GL11.glColor4f(1, 1, 1, drop.alpha);
			GL11.glTranslated(H.calculateRenderPos(drop.prevPos.x, drop.pos.x), H.calculateRenderPos(drop.prevPos.y, drop.pos.y), 0);
			GL11H.rotateXYZAt(0, 0, -drop.scale*drop.alpha*180, 14, 25, 0);
			
			HUD.drawRect(1F/368, 1F/399, 0, 0, 340, 0, 28, 51);
			
			GL11.glPopMatrix();
		}
		GL11H.color(color);
		HUD.drawRect(1F/368, 1F/399, 0, 0, 170, 0, 170, 399);
		GL11.glColor4f(1, 1, 1, 1);
		HUD.drawRect(1F/368, 1F/399, 0, 0, 0, 0, 170, 399);
		GL11.glPopMatrix();
	}
	private static class Drop{
		public boolean dead=false;
		public Vector2f pos,prevPos;
		public float alpha,scale,speed;
		public Drop(Vector2f pos, float scale){
			this.pos = pos;
			this.scale = scale;
		}
		
	}
}
