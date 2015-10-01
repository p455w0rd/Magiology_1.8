package com.magiology.gui.custom.hud;

import java.awt.Color;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.WorldRenderer;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import com.magiology.forgepowered.packets.packets.generic.GenericServerIntPacket;
import com.magiology.handlers.animationhandlers.WingsFromTheBlackFireHandler;
import com.magiology.handlers.animationhandlers.WingsFromTheBlackFireHandler.Positions;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.Util;

public class WingModeChangerHUD extends HUD{
	public static WingModeChangerHUD instance=new WingModeChangerHUD();
	private ExtendedPlayerData extendedData;
	private ComplexPlayerRenderingData data;
	private int selectionId;
	Positions[] validPoss;
	Positions curentPoss;
	double[][] criclePoss={Util.cricleXZ(0),Util.cricleXZ(60),Util.cricleXZ(120),Util.cricleXZ(180),Util.cricleXZ(240),Util.cricleXZ(300),Util.cricleXZ(0)};
	private float sliderPos,prevSliderPos,sliderSpeed,sliderWantedPos,alpha,prevAlpha;
	private boolean isExited=true;
	float[][] backgroundColor,prevBackgroundColor;
	int width=0;
	FontRenderer fr;
	private WingModeChangerHUD(){}
	@Override
	public void render(int xScreen, int yScreen, float partialTicks){
		Positions[] poss=Positions.values();
		//loading up
		if(Util.isNull(extendedData,data,fr,validPoss,Util.getTheWorld(),backgroundColor)){
			if(extendedData==null)extendedData=ExtendedPlayerData.get(player);
			if(data==null)data=ComplexPlayerRenderingData.get(player);
			fr=Util.getFontRenderer();
			if(validPoss==null)for(int a=0;a<poss.length;a++)if(poss[a].wanted&&poss[a]!=Positions.FlyBackvardPos&&poss[a]!=Positions.FlyForvardPos)validPoss=ArrayUtils.add(validPoss, poss[a]);
			backgroundColor=new float[validPoss.length][3];
			prevBackgroundColor=backgroundColor.clone();
			return;
		}if(player==null)return;
		if(data.player!=player){
			extendedData=ExtendedPlayerData.get(player);
		}
		if(extendedData==null)return;
		for(Positions poz:poss)width=Math.max(width, fr.getStringWidth(poz.name()));
		//end
		player=Util.getThePlayer();
		float calcAlpha=Util.calculateRenderPos(prevAlpha, alpha);
		GL11U.SetUpOpaqueRendering(1);
		if(WingsFromTheBlackFireHandler.getIsActive(player)){
			String poz=WingsFromTheBlackFireHandler.getPos(player)+"/"+Positions.get(WingsFromTheBlackFireHandler.getPosId(player));
			GL11.glPushMatrix();
			GL11.glTranslated(xScreen-fr.getStringWidth(poz)*0.7, yScreen-fr.FONT_HEIGHT*0.7,0);
			GL11U.scaled(0.7);
			Color c=new Color(255,255,255,(int)(255*Util.keepValueInBounds(calcAlpha+0.25, 0, 1)));
			fr.drawStringWithShadow(poz, -1,-1, c.hashCode());
			GL11.glPopMatrix();
		}
		
		if(calcAlpha<0.01){
			GL11U.EndOpaqueRendering();
			return;
		}
		double slide=Util.calculateRenderPos(prevSliderPos, sliderPos);
		
		float offset=calcAlpha*10-10;
		float reducedScale=1,clipping=275-yScreen;
		if(clipping>0){
			clipping=(float)((clipping*4.5)/Math.sqrt(clipping));
			reducedScale-=clipping/200;
		}
		GL11.glTranslated(xScreen-width/2, -(validPoss.length)*(width/4), 0);
		GL11U.rotateXYZ(offset*6,-offset*9, 0);
		GL11.glTranslated(-offset, -slide, 0);
		if(clipping>0){
			GL11U.scaled(reducedScale);
			GL11.glTranslated(0, clipping/2F, 0);
		}
		renderSlider();
		GL11U.EndOpaqueRendering();
	}
	private void renderSlider(){
		float calcAlpha=Util.calculateRenderPos(prevAlpha, alpha),mainAlpha=255*calcAlpha;
		if(calcAlpha<=0.01)return;
		int id=0;
		int nextLineOffset=width/2+fr.FONT_HEIGHT/2;
		WorldRenderer tess=TessUtil.getWR();
		float[][] calcBackgroundColor=backgroundColor.clone();
		if(player.fallDistance>4)for(int a=0;a<criclePoss.length;a++)for(int b=0;b<2;b++){
			criclePoss[a][b]+=Util.CRandF(0.01);
		}
		for(int a=0;a<calcBackgroundColor.length;a++)for(int b=0;b<3;b++){
			calcBackgroundColor[a][b]=Util.calculateRenderPos(backgroundColor[a][b], prevBackgroundColor[a][b]);
		}
		for(int nj=0;nj<3;nj++)for(int pozId=0;pozId<validPoss.length;pozId++){
			Positions poz=validPoss[pozId];
			int var=Math.abs((id-validPoss.length)-selectionId);
			Color color=new Color(255,255,255,(int)(mainAlpha*(var==0?1:var==1?0.5:var==2?0.2:0.025)));
			if(color.getAlpha()>6){
				if(nj!=1)color=new Color(255,155,155,(int)(mainAlpha*(var==0?1:var==1?0.5:var==2?0.2:0.025)));
				if(var==0)color=new Color(125,125,255);
				
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glColor4d(
				nj==1?calcBackgroundColor[pozId][0]:1,nj==1?calcBackgroundColor[pozId][1]:0.7,nj==1?calcBackgroundColor[pozId][2]:0.7,0.4*calcAlpha*((color.getAlpha())/255F));
				tess.startDrawing(GL11.GL_TRIANGLES);
				for(int l=0;l<criclePoss.length-1;l++){
						tess.addVertex(0, 0, 0);
						tess.addVertex(criclePoss[l][0]*nextLineOffset, criclePoss[l][1]*nextLineOffset/2+fr.FONT_HEIGHT/2, 0);
						tess.addVertex(criclePoss[l+1][0]*nextLineOffset, criclePoss[l+1][1]*nextLineOffset/2+fr.FONT_HEIGHT/2, 0);
				}
				TessUtil.draw();
				GL11.glColor4d(0.8,0.8,0.8,calcAlpha*((color.getAlpha())/255F));
				GL11.glLineWidth(1.5F);
				tess.startDrawing(GL11.GL_LINES);
				for(int l=0;l<criclePoss.length-1;l++){
					tess.addVertex(criclePoss[l][0]*nextLineOffset, criclePoss[l][1]*nextLineOffset/2+fr.FONT_HEIGHT/2, 0);
					tess.addVertex(criclePoss[l+1][0]*nextLineOffset, criclePoss[l+1][1]*nextLineOffset/2+fr.FONT_HEIGHT/2, 0);
				}
				TessUtil.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				
				String waring=player.motionY<-0.2&&(poz==Positions.NormalPos||poz==Positions.ProtectivePos)?"!! ":"";
				GL11.glPushMatrix();
				GL11.glTranslated(-fr.getStringWidth(poz.name())/2-fr.getStringWidth(waring), 0, 0);
				fr.drawStringWithShadow(waring+poz.name(), 0,0, color.hashCode());
				GL11.glPopMatrix();
			}
			GL11.glTranslated(0, nextLineOffset+2, 0);
			id++;
		}
		if(player.fallDistance>4)for(int a=0;a<criclePoss.length;a++)for(int b=0;b<2;b++){
			criclePoss=new double[][]{Util.cricleXZ(0),Util.cricleXZ(60),Util.cricleXZ(120),Util.cricleXZ(180),Util.cricleXZ(240),Util.cricleXZ(300),Util.cricleXZ(0)};
		}
	}
	private void onExit(){
		if(WingsFromTheBlackFireHandler.getIsActive(player))Util.sendMessage(new GenericServerIntPacket(6, validPoss[selectionId].id));
	}
	private void onOpen(){}
	@Override
	public void update(){
		if(Util.isNull(extendedData,data,player,validPoss,Util.getTheWorld()))return;
		int nextLineOffset=width/2+fr.FONT_HEIGHT;
		prevBackgroundColor=backgroundColor.clone();
		prevSliderPos=sliderPos;
		prevAlpha=alpha;
		sliderSpeed=Util.handleSpeedFolower(sliderSpeed,sliderPos,sliderWantedPos,15F);
		sliderSpeed*=0.7;
		double multiplayer=Math.abs((sliderPos-sliderWantedPos)/nextLineOffset);
		multiplayer=Util.keepValueInBounds(multiplayer, 0, 1);
		sliderSpeed*=multiplayer;
		sliderPos+=sliderSpeed;
		int perPos=((int)(sliderWantedPos/nextLineOffset));
		if(perPos>validPoss.length-1)sliderWantedPos=0;
		if(perPos<0)sliderWantedPos=(validPoss.length-1)*nextLineOffset;
		selectionId=(int)((sliderPos+nextLineOffset/2)/nextLineOffset);
		selectionId=(int)Util.keepValueInBounds(selectionId, 0, 4);
		curentPoss=Positions.values()[validPoss[selectionId].id];
		alpha+=0.2F*(isExited?-1:1);
		double noise=0.05,speed=0.15;
		for(int a=0;a<backgroundColor.length;a++){
			if(a==selectionId){
				backgroundColor[a][0]=(float)Util.slowlyEqalize(backgroundColor[a][0], 1+Util.CRandF(noise),speed);
				backgroundColor[a][1]=(float)Util.slowlyEqalize(backgroundColor[a][1], 0.2+Util.CRandF(noise),speed);
				backgroundColor[a][2]=(float)Util.slowlyEqalize(backgroundColor[a][2], 0.2+Util.CRandF(noise),speed);
			}else{
				backgroundColor[a][0]=(float)Util.slowlyEqalize(backgroundColor[a][0], 0.2+Util.CRandF(noise),speed);
				backgroundColor[a][1]=(float)Util.slowlyEqalize(backgroundColor[a][1], 0.2+Util.CRandF(noise),speed);
				backgroundColor[a][2]=(float)Util.slowlyEqalize(backgroundColor[a][2], 0.2+Util.CRandF(noise),speed);
			}
			
			backgroundColor[a][0]=Util.keepValueInBounds(backgroundColor[a][0], 0, 1);
			backgroundColor[a][1]=Util.keepValueInBounds(backgroundColor[a][1], 0, 1);
			backgroundColor[a][2]=Util.keepValueInBounds(backgroundColor[a][2], 0, 1);
		}
		alpha=Util.keepValueInBounds(alpha, 0F, 1);
		boolean prevIsExited=isExited;
		isExited=!GuiScreen.isCtrlKeyDown()||!WingsFromTheBlackFireHandler.getIsActive(player);
		if(prevIsExited!=isExited){
			if(isExited)onExit();
			else onOpen();
		}
	}
	public void next(){
		if(!isExited)sliderWantedPos-=width/2+fr.FONT_HEIGHT;
	}
	public void prev(){
		if(!isExited)sliderWantedPos+=width/2+fr.FONT_HEIGHT;
	}
}
