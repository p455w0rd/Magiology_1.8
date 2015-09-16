package com.magiology.gui.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.magiology.core.MReference;
import com.magiology.gui.container.UpgradeContainer;
import com.magiology.gui.custom.guiparticels.GuiStandardFX;
import com.magiology.mcobjects.effect.GuiParticle;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;

public class GuiUpgrade extends GuiContainerAndGuiParticles{
	
	ResourceLocation main= new ResourceLocation(MReference.MODID,"/textures/gui/UpgradeGui.png");
	ResourceLocation texture1=new ResourceLocation(MReference.MODID+":/textures/particle/SmoothBuble1.png");
	int particleAttractionTime,mouseX,mouseY;
	double guiAlpha=1;
	private final TileEntityPow tileCB;
	
	public GuiUpgrade(InventoryPlayer pInventory,TileEntityPow tileCB){
		super(new UpgradeContainer(pInventory,tileCB));
		
		this.tileCB=tileCB;
		this.xSize=176;
		this.ySize=132;
	}
	@Override
	public void drawGuiContainerForegroundLayer(int x,int y){
		GL11.glPushMatrix();
		GL11.glScaled(0.7,0.7,0.7);
		int result=guiParticles.size();
		for(GuiParticle as:guiParticles)if(as.isDead)result--;
		if(tileCB.container!=null)this.drawString(fontRendererObj,tileCB.container.toString()+" upgrade.    Number of GUI particles: "+result, 3, -11, Util.rgbPrecentageToCode(1, 1, 1, 1));
		
		GL11.glPopMatrix();
		
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float v1, int x, int y){
		mouseX=x;
		mouseY=y;
		TessUtil.getWR();
		this.renderParticles(v1);
		
		double GL11alpha=1;
		boolean isInBlend=false;
		if((x<guiLeft||y<guiTop)||(x>guiLeft+xSize||y>guiTop+ySize));else
		if(isCtrlKeyDown()&&isShiftKeyDown()){
			isInBlend=true;
			GL11alpha-=0.4;
			{
				double multiplayer=1,xMul=1,yMul=1;
				int xOffset=guiLeft+xSize/2,yOffset=guiLeft+ySize/4;
				int xMouseMod=Math.abs(x-xOffset),yMouseMod=Math.abs(y-yOffset);
				xMul=(float)xMouseMod/(float)xOffset;
				yMul=(float)yMouseMod/(float)yOffset;
				
				multiplayer=Math.max(xMul, yMul)+0.5;
				if(multiplayer>1)multiplayer=1;
				GL11alpha*=multiplayer;
			}
		}
		if(!isInBlend){
			guiAlpha+=0.06+Util.CRandF(0.05);
			if(guiAlpha>1)guiAlpha=1;
		}
		for(int ad=0;ad<30;ad++)guiAlpha=Util.slowlyEqalize(guiAlpha, GL11alpha, 0.001);
		if(guiAlpha<1){
			GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		GL11.glColor4d(1, 1, 1, guiAlpha);
		U.getMC().getTextureManager().bindTexture(main);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 6);
		this.drawTexturedModalRect(guiLeft, guiTop+6, 0, 7, xSize, 17);
		this.drawTexturedModalRect(guiLeft, guiTop+23, 0, 25, xSize, ySize);
		int number=tileCB.containerItems.length,offsetx=xSize/2-number*9,offsety=6;
		GL11.glColor4d(1, 1, 1, guiAlpha/2);
		for(int a=0;a<number;a++){
			this.drawTexturedModalRect(guiLeft+offsetx, guiTop+offsety, 176, 0, 18, 18);
			offsetx+=18;
		}
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glDisable(GL11.GL_BLEND);
		TessUtil.drawSlotLightMapWcustomSizes(this, 7+guiLeft, 7+guiTop, 16, 27,false,false);
		TessUtil.drawPlayerIntoGUI(15+guiLeft, 30+guiTop, 10, guiLeft-x+16 , guiTop-y+25, mc.thePlayer);
	}
	
	@Override
	public void initGui(){
		super.initGui();
	}
	
	int mouseStartX,mouseStartY;
	
	@Override
	protected void mouseClicked(int x, int y, int var1) throws IOException{
		super.mouseClicked(x,y,var1);
		if(particleAttractionTime>=0){
			for(int c=0;c<guiParticles.size();c++){
				GuiParticle ab=guiParticles.get(c);
				if(ab!=null&&ab.standardFX==GuiStandardFX.StarterFX&&!ab.isDead){
					spawnGuiParticle(new GuiParticle(ab.xPos,ab.yPos,10, -ab.xSpeed, -ab.ySpeed,1,0.7,0.7,  1,0.2+Util.RF()*0.5, 0.2+Util.RF()*0.2,texture1,GuiStandardFX.SummonedFX));
					ab.xSpeed*=1.6;
					ab.ySpeed*=1.6;
				}
			}
			particleAttractionTime=-20;
		}
		mouseStartX=x;
		mouseStartY=y;
		if(isCtrlKeyDown()){
			if((x<guiLeft||y<guiTop)||(x>guiLeft+xSize||y>guiTop+ySize)){
				double[] ab=Util.cricleXZ(Util.RInt(360));
				spawnGuiParticle(new GuiParticle(x,y,100, ab[0]*20, ab[1]*20,1,0.7,0.7,Util.RF(),Util.RF(), Util.RF(),texture1,GuiStandardFX.SummonedFX));
			}
		}
    }
	
	@Override
	protected void mouseClickMove(int x1, int y1, int lastButtonClicked, long totalMoved){
		super.mouseClickMove(x1, y1, lastButtonClicked, totalMoved);
    }
	
	@Override
	public void update(){
		if(particleAttractionTime>0)particleAttractionTime--;
		else if(particleAttractionTime<0)particleAttractionTime++;
		if(Util.RInt(40)==0)spawnGuiParticle(new GuiParticle(Util.RInt(height), Util.RInt(width),600, 0,0,80,0.02,0.7,Util.RF(),Util.RF(),Util.RF(),texture1,GuiStandardFX.BigCloudFX));
		this.updateParticles();
		int amount=10+Util.RInt(10);
		while(guiParticles.size()<amount){
			double[] ab=Util.cricleXZ(Util.RInt(360));
			spawnGuiParticle(new GuiParticle(width/2, height/2,0, ab[0], ab[1],1.3,0.7,0.7,1,0.2+Util.RF()*0.5, 0.2+Util.RF()*0.2,texture1,GuiStandardFX.StarterFX));
		}
		if(Mouse.isButtonDown(Mouse.getButtonIndex("BUTTON0"))){
			if(isCtrlKeyDown()){
				if(!isShiftKeyDown()){
					if((mouseStartX<guiLeft||mouseStartY<guiTop)||(mouseStartX>guiLeft+xSize||mouseStartY>guiTop+ySize))
					for(int c=0;c<guiParticles.size();c++){
						GuiParticle ab=guiParticles.get(c);
						if(ab!=null&&ab.standardFX==GuiStandardFX.StarterFX&&!ab.isDead){
							ab.xSpeed-=(float)(mouseStartX-mouseX)/50;
							ab.ySpeed-=(float)(mouseStartY-mouseY)/50;
						}
					}
				}
				if(isShiftKeyDown()){
					if((mouseStartX<guiLeft||mouseStartY<guiTop)||(mouseStartX>guiLeft+xSize||mouseStartY>guiTop+ySize))
					for(int c=0;c<guiParticles.size();c++){
						GuiParticle ab=guiParticles.get(c);
						if(ab!=null&&ab.standardFX==GuiStandardFX.StarterFX&&!ab.isDead){
							ab.xSpeed*=0.98;
							ab.ySpeed*=0.98;
							ab.xSpeed-=(float)(ab.xPos-mouseX)/50.0+Util.CRandF(0.3);
							ab.ySpeed-=(float)(ab.yPos-mouseY)/50.0+Util.CRandF(0.3);
						}
					}
				}
			}
	    }
	}
	@Override
	public void onGuiClosed(){
		super.onGuiClosed();
	}
	@Override
	protected void actionPerformed(GuiButton b){
		switch (b.id){
		case 1:{
		}break;
		case 2:{
			 
		}break;
		}
	}
}
