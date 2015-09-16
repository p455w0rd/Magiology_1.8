package com.magiology.render.tilerender;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import org.lwjgl.opengl.GL11;

import com.magiology.mcobjects.tileentityes.TileEntityFireGun;
import com.magiology.render.Textures;
import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

public class RenderFireGun extends TileEntitySpecialRendererM {
	
	private final float p= 1F/16F;
	private final float tW=1F/72F;
	private final float tH=1F/64F;
	NormalizedVertixBuffer buf=Render.NVB();
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		TileEntityFireGun dir= (TileEntityFireGun) tileentity;
		buf.cleanUp();
		GL11.glTranslated(x,y,z);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		for(int i=0; i<dir.rotation.length; i++){
			if(dir.rotation[i] != null){
				drawGunStand(dir.rotation[i], tileentity,x,y,z,f);
				drawGun(dir.rotation[i], tileentity,x,y,z,f);
			}
		}
		
		if(dir.rotation[0]==null)if(dir.rotation[1]==null&&dir.rotation[2]==null&&dir.rotation[3]==null){
			drawGunStand(EnumFacing.SOUTH, tileentity,x,y,z,f);
		}
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
	}
	
	public void drawGunStand(EnumFacing dir, TileEntity tileentity, double x, double y, double z, float f){
		this.bindTexture(Textures.FireGunGun);

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		if(dir.equals(EnumFacing.WEST)){
			GL11.glRotatef(180, 0, 1, 0);
		}
		else if (dir.equals(EnumFacing.SOUTH)){
			GL11.glRotatef(-90, 0, 1, 0);
		}
		else if (dir.equals(EnumFacing.EAST)){
		}
		else if (dir.equals(EnumFacing.NORTH)){
			GL11.glRotatef(90, 0, 1, 0);
		}
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		{
		buf.addVertexWithUV(p*8, p*4, p*9, tW*0, tH*0);
		buf.addVertexWithUV(p*8, p*0, p*9, tW*0, tH*32);
		buf.addVertexWithUV(p*10,p*0, p*9, tW*16,tH*32);
		buf.addVertexWithUV(p*10,p*4, p*9, tW*16,tH*0);

		buf.addVertexWithUV(p*10,p*4, p*7, tW*16,tH*0);
		buf.addVertexWithUV(p*10,p*0, p*7, tW*16,tH*32);
		buf.addVertexWithUV(p*8, p*0, p*7, tW*0, tH*32);
		buf.addVertexWithUV(p*8, p*4, p*7, tW*0, tH*0);
		
		buf.addVertexWithUV(p*8, p*4, p*7, tW*16,tH*0);
		buf.addVertexWithUV(p*8, p*0, p*7, tW*16,tH*32);
		buf.addVertexWithUV(p*8, p*0, p*9, tW*0, tH*32);
		buf.addVertexWithUV(p*8, p*4, p*9, tW*0, tH*0);

		buf.addVertexWithUV(p*10, p*4, p*9, tW*0, tH*0);
		buf.addVertexWithUV(p*10, p*0, p*9, tW*0, tH*32);
		buf.addVertexWithUV(p*10, p*0, p*7, tW*16,tH*32);
		buf.addVertexWithUV(p*10, p*4, p*7, tW*16,tH*0);
		buf.draw();
		}
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		if(dir.equals(EnumFacing.WEST)){
			GL11.glRotatef(-180, 0, 1, 0);
		}
		else if (dir.equals(EnumFacing.SOUTH)){
			GL11.glRotatef(90, 0, 1, 0);
		}
		else if (dir.equals(EnumFacing.EAST)){
		}
		else if (dir.equals(EnumFacing.NORTH)){
			GL11.glRotatef(-90, 0, 1, 0);
		}
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	}
	public void drawGun(EnumFacing dir, TileEntity tileentity, double x, double y, double z, float f){
		TileEntityFireGun isit= (TileEntityFireGun) tileentity;
		GL11.glPushMatrix();
		this.bindTexture(Textures.FireGunGun);

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		if(dir.equals(EnumFacing.WEST)){
			GL11.glRotatef(180, 0, 1, 0);
		}
		else if (dir.equals(EnumFacing.SOUTH)){
			GL11.glRotatef(-90, 0, 1, 0);
		}
		else if (dir.equals(EnumFacing.EAST)){
		}
		else if (dir.equals(EnumFacing.NORTH)){
			GL11.glRotatef(90, 0, 1, 0);
		}
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		{
			{
			GL11.glTranslated(-Util.calculateRenderPos(isit.prevAnimation, isit.animation),0,0);
			buf.addVertexWithUV(p*5,  p*7, p*9.5,  tW*16, tH*0);
			buf.addVertexWithUV(p*5,  p*4, p*9.5,  tW*16, tH*24);
			buf.addVertexWithUV(p*12, p*4, p*9.5,  tW*72, tH*24);
			buf.addVertexWithUV(p*12, p*7, p*9.5,  tW*72, tH*0);

			buf.addVertexWithUV(p*12, p*7, p*6.5,  tW*72, tH*0);
			buf.addVertexWithUV(p*12, p*4, p*6.5,  tW*72, tH*24);
			buf.addVertexWithUV(p*5,  p*4, p*6.5,  tW*16, tH*24);
			buf.addVertexWithUV(p*5,  p*7, p*6.5,  tW*16, tH*0);
			
			buf.addVertexWithUV(p*12, p*7, p*9.5,  tW*72, tH*0);
			buf.addVertexWithUV(p*12, p*7, p*6.5,  tW*72, tH*24);
			buf.addVertexWithUV(p*5,  p*7, p*6.5,  tW*16, tH*24);
			buf.addVertexWithUV(p*5,  p*7, p*9.5,  tW*16, tH*0);

			buf.addVertexWithUV(p*12, p*7, p*9.5,  tW*24, tH*64);
			buf.addVertexWithUV(p*12, p*4, p*9.5,  tW*24, tH*40);
			buf.addVertexWithUV(p*12, p*4, p*6.5,  tW*0, tH*40);
			buf.addVertexWithUV(p*12, p*7, p*6.5,  tW*0, tH*64);
			
			buf.addVertexWithUV(p*5, p*7, p*6.5,  tW*0, tH*40);
			buf.addVertexWithUV(p*5, p*4, p*6.5,  tW*0, tH*64);
			buf.addVertexWithUV(p*5, p*4, p*9.5,  tW*24, tH*64);
			buf.addVertexWithUV(p*5, p*7, p*9.5,  tW*24, tH*40);
			}
			
			{
			buf.addVertexWithUV(p*12, p*6.5, p*9,  tW*16, tH*24);
			buf.addVertexWithUV(p*12, p*4.5, p*9,  tW*16, tH*40);
			buf.addVertexWithUV(p*19, p*4.5, p*9,  tW*72,tH*40);
			buf.addVertexWithUV(p*19, p*6.5, p*9,  tW*72,tH*24);

			buf.addVertexWithUV(p*19, p*6.5, p*7,  tW*72,tH*24);
			buf.addVertexWithUV(p*19, p*4.5, p*7,  tW*72,tH*40);
			buf.addVertexWithUV(p*12, p*4.5, p*7,  tW*16, tH*40);
			buf.addVertexWithUV(p*12, p*6.5, p*7,  tW*16, tH*24);
			
			buf.addVertexWithUV(p*19, p*6.5, p*9,  tW*72,tH*24);
			buf.addVertexWithUV(p*19, p*6.5, p*7,  tW*72,tH*40);
			buf.addVertexWithUV(p*12, p*6.5, p*7,  tW*16, tH*40);
			buf.addVertexWithUV(p*12, p*6.5, p*9,  tW*16, tH*24);

			buf.addVertexWithUV(p*19, p*6.5, p*9,  tW*24, tH*56);
			buf.addVertexWithUV(p*19, p*4.5, p*9,  tW*24, tH*40);
			buf.addVertexWithUV(p*19, p*4.5, p*7,  tW*40,  tH*40);
			buf.addVertexWithUV(p*19, p*6.5, p*7,  tW*40,  tH*56);
			}
			
			{
			buf.addVertexWithUV(p*19, p*5.5, p*8.5,  tW*0, tH*32);
			buf.addVertexWithUV(p*19, p*4.5, p*8.5,  tW*0, tH*40);
			buf.addVertexWithUV(p*22, p*4.5, p*8.5,  tW*16,tH*40);
			buf.addVertexWithUV(p*22, p*5.5, p*8.5,  tW*16,tH*32);
				
			buf.addVertexWithUV(p*22, p*5.5, p*7.5,  tW*16,tH*32);
			buf.addVertexWithUV(p*22, p*4.5, p*7.5,  tW*16,tH*40);
			buf.addVertexWithUV(p*19, p*4.5, p*7.5,  tW*0, tH*40);
			buf.addVertexWithUV(p*19, p*5.5, p*7.5,  tW*0, tH*32);
				
			buf.addVertexWithUV(p*22, p*5.5, p*8.5,  tW*16,tH*32);
			buf.addVertexWithUV(p*22, p*5.5, p*7.5,  tW*16,tH*40);
			buf.addVertexWithUV(p*19, p*5.5, p*7.5,  tW*0, tH*40);
			buf.addVertexWithUV(p*19, p*5.5, p*8.5,  tW*0, tH*32);
				
			buf.addVertexWithUV(p*22, p*5.5, p*8.5,  tW*40, tH*40);
			buf.addVertexWithUV(p*22, p*4.5, p*8.5,  tW*40, tH*48);
			buf.addVertexWithUV(p*22, p*4.5, p*7.5,  tW*48, tH*48);
			buf.addVertexWithUV(p*22, p*5.5, p*7.5,  tW*48, tH*40);
			}
			buf.draw();
		}
		GL11.glPopMatrix();
	}
}
