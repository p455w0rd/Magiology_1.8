package com.magiology.gui.custom.hud;

import java.awt.Color;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.WorldRenderer;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import com.magiology.forgepowered.packets.generic.GenericServerIntPacket;
import com.magiology.handelers.animationhandelers.WingsFromTheBlackFireHandeler;
import com.magiology.handelers.animationhandelers.WingsFromTheBlackFireHandeler.Positions;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;
import com.magiology.util.renderers.GL11H;
import com.magiology.util.renderers.TessHelper;
import com.magiology.util.utilclasses.Helper;

public class WingModeChangerHUD extends HUD{
	public static WingModeChangerHUD instance=new WingModeChangerHUD();
	private ExtendedPlayerData extendedData;
	private ComplexPlayerRenderingData data;
	private int selectionId;
	Positions[] validPoss;
	Positions curentPoss;
	double[][] criclePoss={Helper.cricleXZ(0),Helper.cricleXZ(60),Helper.cricleXZ(120),Helper.cricleXZ(180),Helper.cricleXZ(240),Helper.cricleXZ(300),Helper.cricleXZ(0)};
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
		if(Helper.isNull(extendedData,data,fr,validPoss,Helper.getTheWorld(),backgroundColor)){
			if(extendedData==null)extendedData=ExtendedPlayerData.get(player);
			if(data==null)data=ComplexPlayerRenderingData.get(player);
			fr=Helper.getFontRenderer();
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
		player=Helper.getThePlayer();
		float calcAlpha=Helper.calculateRenderPos(prevAlpha, alpha);
		GL11H.SetUpOpaqueRendering(1);
		if(WingsFromTheBlackFireHandeler.getIsActive(player)){
			String poz=WingsFromTheBlackFireHandeler.getPos(player)+"/"+Positions.get(WingsFromTheBlackFireHandeler.getPosId(player));
			GL11.glPushMatrix();
			GL11.glTranslated(xScreen-fr.getStringWidth(poz)*0.7, yScreen-fr.FONT_HEIGHT*0.7,0);
			GL11H.scaled(0.7);
			Color c=new Color(255,255,255,(int)(255*Helper.keepValueInBounds(calcAlpha+0.25, 0, 1)));
			fr.drawStringWithShadow(poz, -1,-1, c.hashCode());
			GL11.glPopMatrix();
		}
		
		if(calcAlpha<0.01){
			GL11H.EndOpaqueRendering();
			return;
		}
		double slide=Helper.calculateRenderPos(prevSliderPos, sliderPos);
		
		float offset=calcAlpha*10-10;
		float reducedScale=1,clipping=275-yScreen;
		if(clipping>0){
			clipping=(float)((clipping*4.5)/Math.sqrt(clipping));
			reducedScale-=clipping/200;
		}
		GL11.glTranslated(xScreen-width/2, -(validPoss.length)*(width/4), 0);
		GL11H.rotateXYZ(offset*6,-offset*9, 0);
		GL11.glTranslated(-offset, -slide, 0);
		if(clipping>0){
			GL11H.scaled(reducedScale);
			GL11.glTranslated(0, clipping/2F, 0);
		}
		renderSlider();
		GL11H.EndOpaqueRendering();
	}
	private void renderSlider(){
		float calcAlpha=Helper.calculateRenderPos(prevAlpha, alpha),mainAlpha=255*calcAlpha;
		if(calcAlpha<=0.01)return;
		int id=0;
		int nextLineOffset=width/2+fr.FONT_HEIGHT/2;
		WorldRenderer tess=TessHelper.getWR();
		float[][] calcBackgroundColor=backgroundColor.clone();
		if(player.fallDistance>4)for(int a=0;a<criclePoss.length;a++)for(int b=0;b<2;b++){
			criclePoss[a][b]+=Helper.CRandF(0.01);
		}
		for(int a=0;a<calcBackgroundColor.length;a++)for(int b=0;b<3;b++){
			calcBackgroundColor[a][b]=Helper.calculateRenderPos(backgroundColor[a][b], prevBackgroundColor[a][b]);
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
				TessHelper.draw();
				GL11.glColor4d(0.8,0.8,0.8,calcAlpha*((color.getAlpha())/255F));
				GL11.glLineWidth(1.5F);
				tess.startDrawing(GL11.GL_LINES);
				for(int l=0;l<criclePoss.length-1;l++){
					tess.addVertex(criclePoss[l][0]*nextLineOffset, criclePoss[l][1]*nextLineOffset/2+fr.FONT_HEIGHT/2, 0);
					tess.addVertex(criclePoss[l+1][0]*nextLineOffset, criclePoss[l+1][1]*nextLineOffset/2+fr.FONT_HEIGHT/2, 0);
				}
				TessHelper.draw();
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
			criclePoss=new double[][]{Helper.cricleXZ(0),Helper.cricleXZ(60),Helper.cricleXZ(120),Helper.cricleXZ(180),Helper.cricleXZ(240),Helper.cricleXZ(300),Helper.cricleXZ(0)};
		}
	}
	private void onExit(){
		if(WingsFromTheBlackFireHandeler.getIsActive(player))Helper.sendMessage(new GenericServerIntPacket(6, validPoss[selectionId].id));
	}
	private void onOpen(){}
	@Override
	public void update(){
		if(Helper.isNull(extendedData,data,player,validPoss,Helper.getTheWorld()))return;
		int nextLineOffset=width/2+fr.FONT_HEIGHT;
		prevBackgroundColor=backgroundColor.clone();
		prevSliderPos=sliderPos;
		prevAlpha=alpha;
		sliderSpeed=Helper.handleSpeedFolower(sliderSpeed,sliderPos,sliderWantedPos,15F);
		sliderSpeed*=0.7;
		double multiplayer=Math.abs((sliderPos-sliderWantedPos)/nextLineOffset);
		multiplayer=Helper.keepValueInBounds(multiplayer, 0, 1);
		sliderSpeed*=multiplayer;
		sliderPos+=sliderSpeed;
		int perPos=((int)(sliderWantedPos/nextLineOffset));
		if(perPos>validPoss.length-1)sliderWantedPos=0;
		if(perPos<0)sliderWantedPos=(validPoss.length-1)*nextLineOffset;
		selectionId=(int)((sliderPos+nextLineOffset/2)/nextLineOffset);
		selectionId=(int)Helper.keepValueInBounds(selectionId, 0, 4);
		curentPoss=Positions.values()[validPoss[selectionId].id];
		alpha+=0.2F*(isExited?-1:1);
		double noise=0.05,speed=0.15;
		for(int a=0;a<backgroundColor.length;a++){
			if(a==selectionId){
				backgroundColor[a][0]=(float)Helper.slowlyEqalize(backgroundColor[a][0], 1+Helper.CRandF(noise),speed);
				backgroundColor[a][1]=(float)Helper.slowlyEqalize(backgroundColor[a][1], 0.2+Helper.CRandF(noise),speed);
				backgroundColor[a][2]=(float)Helper.slowlyEqalize(backgroundColor[a][2], 0.2+Helper.CRandF(noise),speed);
			}else{
				backgroundColor[a][0]=(float)Helper.slowlyEqalize(backgroundColor[a][0], 0.2+Helper.CRandF(noise),speed);
				backgroundColor[a][1]=(float)Helper.slowlyEqalize(backgroundColor[a][1], 0.2+Helper.CRandF(noise),speed);
				backgroundColor[a][2]=(float)Helper.slowlyEqalize(backgroundColor[a][2], 0.2+Helper.CRandF(noise),speed);
			}
			
			backgroundColor[a][0]=Helper.keepValueInBounds(backgroundColor[a][0], 0, 1);
			backgroundColor[a][1]=Helper.keepValueInBounds(backgroundColor[a][1], 0, 1);
			backgroundColor[a][2]=Helper.keepValueInBounds(backgroundColor[a][2], 0, 1);
		}
		alpha=Helper.keepValueInBounds(alpha, 0F, 1);
		boolean prevIsExited=isExited;
		isExited=!GuiScreen.isCtrlKeyDown()||!WingsFromTheBlackFireHandeler.getIsActive(player);
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
