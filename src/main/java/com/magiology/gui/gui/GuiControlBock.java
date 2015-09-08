package com.magiology.gui.gui;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.magiology.core.MReference;
import com.magiology.forgepowered.packets.TileRedstone;
import com.magiology.gui.GuiUpdater.Updateable;
import com.magiology.gui.container.ControlBockContainer;
import com.magiology.gui.guiutil.gui.buttons.CustomButton;
import com.magiology.mcobjects.tileentityes.TileEntityControlBlock;
import com.magiology.util.renderers.GL11H;
import com.magiology.util.renderers.TessHelper;
import com.magiology.util.utilclasses.Helper;

public class GuiControlBock extends GuiContainer implements Updateable{
	
	ResourceLocation main= new ResourceLocation(MReference.MODID,"/textures/gui/GuiControlBock.png");
	ResourceLocation EnergyBar=new ResourceLocation(MReference.MODID,"/textures/models/PowerCounter/EnergyBar.png");
	
	DrawThatSexyDotHelper dot1,dot2,dot3;
	CustomButton CustomButton;
	double dotS1=0,dotS2=0,dotS3=0;
	
	private TileEntityControlBlock tileCB;
	
	public GuiControlBock(InventoryPlayer pInventory,TileEntityControlBlock tileCB){
		super(new ControlBockContainer(pInventory,tileCB));
		this.tileCB=tileCB;
		this.xSize=176;
		this.ySize=166;
		
	}
	@Override
	public void drawGuiContainerForegroundLayer(int a,int b){
		String text1="null",text2="Redstone ctrl";
		switch (tileCB.redstoneC){
		case 0:text1="Turn OFF";break;
		case 1:text1="Ignore";break;
		case 2:text1="Turn ON";break;
		}
		int colorGray=Helper.colorToCode(Color.GRAY);
		int first=-1,second=-1;
		if(tileCB.redstoneC==0)second=colorGray;
		if(tileCB.redstoneC==1)first=colorGray;
		if(tileCB.redstoneC==2){
			second=first=colorGray;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		}
		this.drawString(fontRendererObj,"ON", 14, 11, first);
		this.drawString(fontRendererObj,"OFF",12, 25, second);
		if(tileCB.redstoneC==2){
			GL11.glColor4d(1, 1, 1, 1);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glDisable(GL11.GL_BLEND);
		}
		
		this.drawString(fontRendererObj, text1, 5, 64, -1);
		this.drawString(fontRendererObj, text2, 5, 74, -1);
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float v1, int v2, int v3){
		if(dot1==null){
			dot1=new DrawThatSexyDotHelper(guiLeft+9-6, guiTop+8-6,  214-6, 67-6, 16, 16, 1.5);
			dot2=new DrawThatSexyDotHelper(guiLeft+9-6, guiTop+22-6, 214-6, 67-6, 16, 16,1.5);
			dot3=new DrawThatSexyDotHelper(guiLeft+11-6,guiTop+35-6, 214-6, 67-6, 16, 16, 1.5);
		}
		if(CustomButton!=null){
			CustomButton.update(guiLeft+40, guiTop+40);
		}
		TessHelper.bindTexture(main);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.9F);
		GL11.glTranslated(0, 0, 1);
		drawSmartShit(false);
		GL11.glTranslated(0, 0, -1);
		if(tileCB.redstoneC==2){
		this.drawTexturedModalRect(guiLeft+11, guiTop+10, 206, 51, 19, 10);
		this.drawTexturedModalRect(guiLeft+11, guiTop+24, 206, 51, 19, 10);
		}
		GL11H.SetUpOpaqueRendering(2);
		drawSmartShit(true);
		GL11H.EndOpaqueRendering();
		
		dot1.render(guiLeft+9-6, guiTop+8-6);
		dot2.render(guiLeft+9-6, guiTop+22-6);
		dot3.render(guiLeft+11-6,guiTop+35-6);

		TessHelper.bindTexture(EnergyBar);
		double scale=((float)(tileCB.tank)/(float)(tileCB.maxT))*100.0;
		if(scale>1)scale=1;
		
		GL11.glTranslated(guiLeft+142-6, guiTop+43-12.5, 0);
		GL11.glScaled(0.1, 0.1, 1);
		this.drawRect(0,(float)(256-(256*scale)), 0, (float)(256-(256*scale)), 128, (float)(256*scale));
		GL11.glScaled(10, 10, 1);
		GL11.glTranslated(-guiLeft-142+6, -guiTop-43+12.5, 0);
		
	}
	
	public void drawSmartShit(boolean type){
		double angle=Helper.calculateRenderPos(tileCB.prevAngle,tileCB.angle);
		switch (tileCB.redstoneC){
		case 0:{
			this.drawTexturedModalRect(guiLeft+12, guiTop+35, 176, 21, 16, 16);
			this.drawTexturedModalRect(guiLeft-1, guiTop+12, 176, 106, 17, 36);
		}break;
		case 1:{
			this.drawTexturedModalRect(guiLeft+12, guiTop+35, 176, 72, 16, 16);
			this.drawTexturedModalRect(guiLeft+2, guiTop+27, 176, 143, 17, 19);
			
		}break;
		case 2:{
			this.drawTexturedModalRect(guiLeft+12, guiTop+35, 176, 38, 16, 16);
			this.drawTexturedModalRect(guiLeft+23, guiTop+39, 176, 89, 19, 16);
			
		}break;
		}
		this.drawTexturedModalRect(guiLeft+48, guiTop+60, 176, 38+17, 16, 16);
		GL11.glTranslated(guiLeft+31, guiTop+18, 0);
		GL11.glTranslated(20.5, 4.5, 0);
		GL11.glRotated(angle, 0, 0, 1);
		GL11.glTranslated(-20.5, -4.5, 0);
		int glowSize=6;
		this.drawTexturedModalRect(-glowSize+1, -glowSize, 202-glowSize, 30-glowSize, 25+glowSize*2, 9+glowSize*2);
		if(type)this.drawTexturedModalRect(-glowSize+1, -glowSize, 202-glowSize, 30-glowSize, 25+glowSize*2, 9+glowSize*2);
		GL11.glTranslated(20.5, 4.5, 0);
		GL11.glRotated(-angle, 0, 0, 1);
		GL11.glTranslated(-20.5, -4.5, 0);
		GL11.glTranslated(-(guiLeft+31), -(guiTop+18), 0);
		double thingyPos=Helper.calculateRenderPos(tileCB.prevThingyPos, tileCB.thingyPos);
		{
			GL11.glTranslated(guiLeft+83, guiTop+79, 0);
			GL11.glTranslated(-thingyPos*5, 0, 0);
			this.drawTexturedModalRect(0, 0, 204, 0, 19, 10);
			
			GL11.glTranslated(2*thingyPos*5, 0, 0);
			GL11.glTranslated(10, 0, 0);
			GL11.glRotated(180, 0, 1, 0);
			GL11.glDisable(GL11.GL_CULL_FACE);
			this.drawTexturedModalRect(0, 0, 204, 0, 19, 10);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glRotated(180, 0,-1, 0);
			GL11.glTranslated(-10, 0, 0);
			GL11.glTranslated(-(guiLeft+83), -(guiTop+79), 0);
		}
		GL11.glTranslated(-thingyPos*5, 0, 0);
		
	}
	
	
	
	 @Override
	 public void initGui(){
		 super.initGui();
		 if(CustomButton==null)CustomButton=new CustomButton(1, guiLeft+40, guiTop+40, 20, 20, null,"textures/gui/widgetsAdd.png");
		 this.buttonList.add(CustomButton);
	 }
	 
	 @Override
	 protected void actionPerformed(GuiButton b){
		 
		 switch (b.id){
		 case 1:{
			 tileCB.redstoneC++;
			 if(tileCB.redstoneC>2)tileCB.redstoneC=0;
			 if(CustomButton!=null){
				 CustomButton.rGoal=Helper.RD();
				 CustomButton.gGoal=Helper.RD();
				 CustomButton.bGoal=Helper.RD();
			 }
			 if(dot1!=null){
				 dot1.glow+=20;
				 dot2.glow+=20;
				 dot3.glow+=20;
				 dot1.scale*=1.6;
				 dot2.scale*=1.6;
				 dot3.scale*=1.6;
			 }
		 }break;
		 case 2:{
			 
		 }break;
		 
		 }
		 Helper.sendMessage(new TileRedstone(tileCB));
		 tileCB.getWorld().markBlockForUpdate(tileCB.getPos());
		 
	 }
	 
	 @Override
	public void update(){
		if(dot1==null){
			dot1=new DrawThatSexyDotHelper(guiLeft+9-6, guiTop+8-6,  214-6, 67-6, 16, 16, 1.5);
			dot2=new DrawThatSexyDotHelper(guiLeft+9-6, guiTop+22-6, 214-6, 67-6, 16, 16,1.5);
			dot3=new DrawThatSexyDotHelper(guiLeft+11-6,guiTop+35-6, 214-6, 67-6, 16, 16, 1.5);
		}
		dotS1+=Helper.CRandD(5);
		dotS2+=Helper.CRandD(5);
		dotS3+=Helper.CRandD(5);
		dotS1*=0.99;
		dotS2*=0.99;
		dotS3*=0.99;
		dot1.update(tileCB.angle*10+dotS2/5, 3,false);
		dot2.update(tileCB.angle*10+dotS1/5, 3,false);
		dot3.update(tileCB.angle*10+dotS3/5, 3,false);
		dot1.finishTheLoop();
		dot2.finishTheLoop();
		dot3.finishTheLoop();
	 }
	 
	 protected void drawRect(float x, float y,float tx, float yt, float xp, float yp){
		 float f = 0.00390625F;
	     float f1 = 0.00390625F;
	     WorldRenderer renderer = TessHelper.getWR();
	     renderer.startDrawingQuads();
	     renderer.addVertexWithUV(x + 0, y + yp, this.zLevel, (tx + 0) * f, (yt + yp) * f1);
	     renderer.addVertexWithUV(x + xp, y + yp, this.zLevel, (tx+ xp) * f, (yt + yp) * f1);
	     renderer.addVertexWithUV(x + xp, y + 0, this.zLevel, (tx + xp) * f, (yt + 0) * f1);
	     renderer.addVertexWithUV(x + 0, y + 0, this.zLevel, (tx + 0) * f, (yt + 0) * f1);
	     TessHelper.draw();
	}
}