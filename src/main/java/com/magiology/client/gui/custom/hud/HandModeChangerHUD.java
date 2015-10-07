package com.magiology.client.gui.custom.hud;

import java.awt.Color;

import net.minecraft.client.gui.FontRenderer;

import org.lwjgl.opengl.GL11;

import com.magiology.core.init.MItems;
import com.magiology.handlers.animationhandlers.TheHandHandler;
import com.magiology.handlers.animationhandlers.TheHandHandler.HandComonPositions;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.Util;

public class HandModeChangerHUD extends HUD{
	public static HandModeChangerHUD instance=new HandModeChangerHUD();
	private ExtendedPlayerData data;
	public float handAlpha=0,lastHandAlpha;
	
	private HandModeChangerHUD(){}
	
	@Override
	public void render(int xScreen, int yScreen, float partialTicks){
		if(data==null)data=ExtendedPlayerData.get(player);
		if(data==null)return;
		if(data.player!=player){
			data=ExtendedPlayerData.get(player);
		}if(data==null)return;
		FontRenderer fr=TessUtil.getFontRenderer();
		if(Util.isNull(player,fr))return;
		if(handAlpha>0&&Util.isItemInStack(MItems.TheHand, player.getCurrentEquippedItem())){
			float HandAlpha=Util.calculateRenderPos(lastHandAlpha,handAlpha);
			int slot=player.inventory.currentItem;
			GL11.glPushMatrix();
			
			int posId=0;
			for(int b=0;b<HandComonPositions.values().length;b++){
				if(HandComonPositions.values()[b].equals(TheHandHandler.getActivePosition(player))){
					posId=b;
					continue;
				}
			}
			int a1=-1,a2=-1,a3=-1;
			if(posId==1){
				a1=posId-1;
				a2=posId;
				a3=posId+1;
			}else if(posId==0){
				a1=2;
				a2=posId;
				a3=posId+1;
			}else{
				a1=1;
				a2=posId;
				a3=0;
			}
			String up="string "+a1,now="string "+a2,down="string "+a3;
			
			GL11U.SetUpOpaqueRendering(1);
			GL11.glTranslated(slot*20+xScreen/2-95, yScreen-38, 0);
			
			GL11.glTranslated(0, -HandAlpha*20+20, 0);
			GL11.glTranslated(13, 0, 0);
			GL11.glScaled(HandAlpha, HandAlpha, HandAlpha);
			GL11.glRotatef(HandAlpha*90-90, 0, 0, 1);
			GL11.glTranslated(-13, 0, 0);
			
			GL11.glTranslated(-10+HandAlpha*10, 0, 0);
			fr.drawStringWithShadow(up, 0,-9, Color.WHITE.hashCode());
			GL11.glTranslated(10-HandAlpha*10, 0, 0);
			fr.drawStringWithShadow(now, 0, 0, Color.WHITE.hashCode());
			GL11.glTranslated(10-HandAlpha*10, 0, 0);
			fr.drawStringWithShadow(down, 0, 9, Color.WHITE.hashCode());
			
			
			GL11U.EndOpaqueRendering();
			GL11.glPopMatrix();
		}
		
	}
	@Override
	public void update(){
		if(Util.isNull(Util.getThePlayer()))return;
		lastHandAlpha=handAlpha;
		handAlpha+=Util.getThePlayer().isSneaking()?0.25:-0.25;
		handAlpha=Util.keepValueInBounds(handAlpha, 0, 1);
	}
}
