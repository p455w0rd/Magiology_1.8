package com.magiology.gui.custom.hud;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.magiology.api.updateable.Updater;
import com.magiology.gui.GuiUpdater.Updateable;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.AdvancedPhysicsFloat;

public class FakeMessageHUD extends HUD{
	
	private static List<Message> messages=new ArrayList<Message>();

	private static FakeMessageHUD instance;
	private static int timeout;
	public static FakeMessageHUD get(){
		if(instance==null)instance=new FakeMessageHUD();
		return instance;
	}
	private FakeMessageHUD(){}
	
    @Override
	public void render(int xScreen, int yScreen, float partialTicks){
    	timeout=10;
    	GL11.glPushMatrix();
    	GL11.glTranslatef(xScreen-1, yScreen, 0);
    	GL11U.SetUpOpaqueRendering(1);
		for(Message msg:messages)if(!msg.isDead()){
			GL11.glTranslatef(0, -Get.Render.FR().FONT_HEIGHT-1, 0);
			msg.redner();
		}
		GL11U.EndOpaqueRendering();
		GL11.glPopMatrix();
	}
    @Override
	public void update(){
    	if(timeout>0)timeout--;
    	else return;
    	for(int i=0;i<messages.size();i++){
    		if(messages.get(i).isDead())messages.remove(i);
    	}
    	Updater.update(messages);
    }
    
    public static void addMessage(Message msg){
    	int mach=-1;
    	for(int i=0;i<messages.size();i++){
			if(messages.get(i).isSameId(msg)){
				mach=i;
				break;
			}
		}
    	if(mach!=-1){
    		messages.get(mach).age=80;
    	}
    	messages.add(0, msg);
    }
	public static class Message implements Updateable{
		private ColorF color,prevColor;
		private String text,id;
		protected AdvancedPhysicsFloat pos;
		private int age;
		private boolean isDead=false;
		public Message(ColorF color, String text, String id){
			this.color=this.prevColor=color;
			this.text=text;
			this.id=id;
			
		}
		@Override
		public void update(){
			prevColor=color;
			if(age>80)color.a-=0.1;
			color.a=U.keepValueInBounds(color.a, 0, 1);
			if(color.a<=0)isDead=true;
			age++;
		}
		public void redner(){
			ColorF color=Util.calculateRenderColor(prevColor, this.color);
			int sw=Get.Render.FR().getStringWidth(text);
			float animation=(color.a-1);
			GL11.glPushMatrix();
			if(animation<0){
				GL11U.rotateXYZAt(0, animation*40,0, -sw/2F, Get.Render.FR().FONT_HEIGHT/2F, 0);
				GL11U.rotateXYZAt(animation*90, 0,0, -sw/2F, Get.Render.FR().FONT_HEIGHT/2F, 0);
			}
			GL11.glColor4f(0, 0, 0, 0.3F*color.a);
			GL11U.texture(false);
			drawRect(1, 1, -sw-1, -1, 0, 0, sw+2, Get.Render.FR().FONT_HEIGHT+1);
			GL11U.texture(true);
			Get.Render.FR().drawStringWithShadow(text, -sw, 0, color.toCode());
			GL11.glPopMatrix();
		}
		public boolean isSameId(Message msg){
			if(id==null)return false;
			return this.id.equals(msg.id);
		}
		public boolean isDead(){
			return isDead;
		}
	}
}
