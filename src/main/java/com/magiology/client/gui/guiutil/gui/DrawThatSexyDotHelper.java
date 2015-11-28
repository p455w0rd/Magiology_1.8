package com.magiology.client.gui.guiutil.gui;

import net.minecraft.client.gui.*;

import org.lwjgl.opengl.*;

import com.magiology.util.utilclasses.*;

public class DrawThatSexyDotHelper extends Gui{
	
	public int x,y,xTexture,yTexture,xSize,ySize,glow;
	public double scale,prevScale,scalingSpeed,trueScale,rotation,prevRotation,rotatingSpeed,transparency=1;
	boolean isUpdated=false;
	
	public DrawThatSexyDotHelper(int x,int y, int xTexture, int yTexture, int xSize, int ySize, double scale){
		this.x=x;
		this.y=y;
		this.xTexture=xTexture;
		this.yTexture=yTexture;
		this.xSize=xSize;
		this.ySize=ySize;
		this.trueScale=scale;
		rotation=UtilM.CRandD(5);
	}
	
	public void update(double rot,int glow,boolean var1){
		prevRotation=rotation;
		prevScale=scale;
		if(!isUpdated){
			if(scale<0){
				scale=0;
				scalingSpeed*=-0.3;
			}
			isUpdated=true;
			if(var1)transparency+=0.05;
			rotatingSpeed*=0.9;
			rotatingSpeed=rot;
			if(this.glow>glow*10)this.glow--;
			else if(this.glow<glow*10)this.glow++;
			if(this.scale>trueScale)this.scalingSpeed-=0.005;
			else if(this.scale<trueScale)this.scalingSpeed+=0.005;
			this.scale*=0.98;
			this.scale+=scalingSpeed;
			rotatingSpeed*=0.2;
			this.rotation+=this.rotatingSpeed;
		}
	}
	
	public void updateAndRender(double rotation,int x3,int y3,int glow,boolean var1){
		render(x3,y3);
		this.update(rotation,glow,var1);
	}
	
	public void render(int x3,int y3){
		isUpdated=false;
		x=x3;y=y3;
		if(!isUpdated){
			double scale=UtilM.calculatePos(prevScale, this.scale);
			double rotation=UtilM.calculatePos(prevRotation, this.rotation);
			
			double scal=scale-1.2;
			double xR=x-scal*7.5,yR=y-scal*7.5;
			double xof=9+scal*7.5,yof=9+scal*7.5;
			GL11.glTranslated(xR, yR, 40);
			//......................................
			GL11.glTranslated(xof, yof, 0);
			GL11.glRotated(rotation, 0, 0, 1);
			GL11.glTranslated(-xof, -yof, 0);
			GL11.glScaled(scale, scale, scale);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.99F);
			GL11.glTranslated(0, 0, 1);
			
			drawTexturedModalRect(0,0,xTexture,yTexture,xSize,ySize);

			GL11.glDepthMask(false);
			GL11.glTranslated(0, 0, -1);
			//---------------------------------------------
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
			GL11.glEnable(GL11.GL_BLEND);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
	        
			for(int a=0;a<this.glow/11;a++)drawTexturedModalRect(0,0,xTexture,yTexture,xSize,ySize);
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glScaled(1/scale, 1/scale, 1/scale);
			GL11.glTranslated(xof, yof, 0);
			GL11.glRotated(-rotation, 0, 0, 1);
			GL11.glTranslated(-xof, -yof, 0);
			//......................................
			GL11.glTranslated(-xR, -yR, -40);
			GL11.glDepthMask(true);
			
		}
	}
	
	
	public void finishTheLoop(){
		transparency-=0.05;
	}
	
}
