package com.magiology.client.render.tilerender;

import net.minecraft.tileentity.*;

import org.lwjgl.opengl.*;

import com.magiology.client.render.*;
import com.magiology.mcobjects.tileentityes.*;
import com.magiology.util.renderers.*;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilobjects.m_extension.*;

public class RenderFireMatrixTransferer extends TileEntitySpecialRendererM {

	private final float p=1F/16F;
	VertixBuffer buf=Render.NVB();
	
	float pos=0,ballRotation=0;
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		
		if(ballRotation>360)ballRotation-=360;
		ballRotation++;
		
		if(tileentity instanceof TileEntityFireMatrixTransferer){
			pos=((TileEntityFireMatrixTransferer)tileentity).Pos;
			this.ballRotation=((TileEntityFireMatrixTransferer)tileentity).ballRotation;
		}
		this.bindTexture(Textures.FireMatrixTransfererBase);
		GL11.glTranslated(x,y,z);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		
		for(int i=0;i<4;i++){
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(-i*90, 0, 1, 0);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			
			GL11.glTranslatef(p*5.5F, -p*3, p*1.5F);

			GL11.glRotated(-45, 0, 1, 0);
			GL11.glRotated(30, 0, 0, 1);
			drawWhaterThatIs();
			GL11.glRotated(-30, 0, 0, 1);
			GL11.glRotated(45, 0, 1, 0);
			
			GL11.glTranslatef(-p*5.5F, p*3, -p*1.5F);
			

			GL11.glTranslatef(p*1F, -p*2, -p*3F);
			GL11.glRotated(-45, 0, 1, 0);
			GL11.glRotated(-15, 0, 0, 1);
			drawWhaterThatIs2();
			GL11.glRotated(15, 0, 0, 1);
			GL11.glRotated(45, 0, 1, 0);
			GL11.glTranslatef(-p*1F, p*2, p*3F);
			
			GL11.glTranslatef(-p*5, p*10.5F,-p*9);
			GL11.glRotated(-45, 0, 1, 0);
			GL11.glRotated(-75, 0, 0, 1);
			drawWhaterThatIs3();
			GL11.glRotated(75, 0, 0, 1);
			GL11.glRotated(45, 0, 1, 0);
			GL11.glTranslatef(p*5, -p*10.5F, p*9);
			
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(i*90, 0, 1, 0);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			
		}
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11U.setUpOpaqueRendering(1);
		GL11.glColor4d(1, 0.01, 0.01, 0.2);
		
		GL11.glTranslatef(0.5F, p*9+pos,0.5F);

		GL11.glRotated(ballRotation, 0, 1, 0);
		GL11.glRotated(ballRotation+20, 0, 0, 1);
		
		
		drawBall();
		
		GL11.glRotated(ballRotation+90, 0, -2, 0);
		drawBall();
		GL11.glRotated(-ballRotation-90, 0, -2, 0);
		
		GL11.glRotated(ballRotation+72, 1, 1, 0);
		drawBall();
		GL11.glRotated(-ballRotation-72, 1, 1, 0);
		
		GL11.glRotated(ballRotation+64, 0, 1, 1);
		drawBall();
		GL11.glRotated(-ballRotation-64, 0, 1, 1);
		
		GL11.glRotated(ballRotation+170, 1, 0, 1);
		drawBall();
		GL11.glRotated(-ballRotation-170, 1, 0, 1);
		

		GL11.glRotated(-ballRotation-231, 1, 1, 0);
		drawBall();
		GL11.glRotated(ballRotation+231, 1, 1, 0);
		
		GL11.glRotated(-ballRotation-267, 0, 1, 1);
		drawBall();
		GL11.glRotated(ballRotation+267, 0, 1, 1);
		
		GL11.glRotated(-ballRotation-192, 1, 0, 1);
		drawBall();
		GL11.glRotated(ballRotation+192, 1, 0, 1);
		
		
		GL11.glRotated(-ballRotation-20, 0, 0, 1);
		GL11.glRotated(-ballRotation, 0, 1, 0);
		GL11.glTranslatef(-0.5F, -p*9-pos, -0.5F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11U.endOpaqueRendering();
		 
		drawBase();
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
	}
	
	public void drawBase() {
		{
			double minx=p*4;double miny=p*0;double minz=p*4;
			double maxx=p*12;double maxy=p*3.5;double maxz=p*12;

			this.bindTexture(Textures.FireMatrixTransfererBase);

			this.drawCube(minx, miny, minz, maxx, maxy, maxz);
			
			this.drawCube(p*6,p*3.5,p*6,p*10,p*5,p*10);
		}
	}
	public void drawWhaterThatIs(){
		{
			double minx=p*2;double miny=p*4;double minz=p*2;
			double maxx=p*4;double maxy=p*9;double maxz=p*4;
			
			this.bindTexture(Textures.FireMatrixTransfererBase);
			
			this.drawCube(minx, miny, minz, maxx, maxy, maxz);
		}
		
		
	}
	public void drawWhaterThatIs2(){
		{
			double minx=p*2.25;double miny=p*9;double minz=p*2.25;
			double maxx=p*3.75;double maxy=p*15;double maxz=p*3.75;
			
			this.bindTexture(Textures.FireMatrixTransfererBase);
			

			this.drawCube(minx, miny, minz, maxx, maxy, maxz);
		}
	}
	public void drawWhaterThatIs3(){
		{
			double minx=p*2.5;double miny=p*15;double minz=p*2.5;
			double maxx=p*3.5;double maxy=p*18;double maxz=p*3.5;
			
			this.bindTexture(Textures.FireMatrixTransfererBase);
			

			this.drawCube(minx, miny, minz, maxx, maxy, maxz);
		}
	}
	public void drawBall(){
		{
			double minx=p*-0.75;double miny=p*-1.25;double minz=p*-0.75;
			double maxx=p*0.75;double maxy=p*0.25;double maxz=p*0.75;
			
			this.bindTexture(Textures.FireMatrixTransfererBase);
			

			this.drawCube(minx, miny, minz, maxx, maxy, maxz);
		}
	}
	public void drawCube(double minx,double miny,double minz,double maxx,double maxy,double maxz){
		buf.cleanUp();
		buf.addVertexWithUV(minx, miny, maxz, 0, 1);
		buf.addVertexWithUV(minx, miny, minz, 0, 0);
		buf.addVertexWithUV(maxx, miny, minz, 1, 0);
		buf.addVertexWithUV(maxx, miny, maxz, 1, 1);
		buf.addVertexWithUV(maxx, maxy, maxz, 1, 1);
		buf.addVertexWithUV(maxx, maxy, minz, 1, 0);
		buf.addVertexWithUV(minx, maxy, minz, 0, 0);
		buf.addVertexWithUV(minx, maxy, maxz, 0, 1);
		buf.addVertexWithUV(maxx, maxy, minz,  1, 1);
		buf.addVertexWithUV(maxx, miny, minz,  1, 0);
		buf.addVertexWithUV(minx, miny , minz, 0, 0);
		buf.addVertexWithUV(minx, maxy, minz,  0, 1);
		buf.addVertexWithUV(minx, maxy, maxz,  0, 1);
		buf.addVertexWithUV(minx, miny , maxz, 0, 0);
		buf.addVertexWithUV(maxx, miny, maxz,  1, 0);
		buf.addVertexWithUV(maxx, maxy, maxz,  1, 1);
		buf.addVertexWithUV(minx, maxy, minz, minz, miny);
		buf.addVertexWithUV(minx, miny, minz, minz, maxy);
		buf.addVertexWithUV(minx, miny, maxz, maxz, maxy);
		buf.addVertexWithUV(minx, maxy, maxz, maxz, miny);
		buf.addVertexWithUV(maxx, maxy, maxz,  1, 1);
		buf.addVertexWithUV(maxx, miny,  maxz, 1, 0);
		buf.addVertexWithUV(maxx, miny,  minz, 0, 0);
		buf.addVertexWithUV(maxx, maxy, minz,  0, 1);
		buf.draw();
	}
	
	
}
