package com.magiology.render.tilerender;

import java.text.DecimalFormat;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.magiology.mcobjects.tileentityes.TileEntityRemotePowerCounter;
import com.magiology.objhelper.Get.Render;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;
import com.magiology.objhelper.helpers.renderers.NormalizedVertixBuffer;
import com.magiology.objhelper.helpers.renderers.TessHelper;
import com.magiology.render.Textures;

public class RenderRemotePowerCounter extends TileEntitySpecialRenderer {
	
	World world;
	int metadata=0;
	Block block;
	
	double powerBar;
	int maxPB,currentP;
	float p=1F/16F;
	NormalizedVertixBuffer buf=Render.NVB();
	FontRenderer fr=Helper.getFontRenderer();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentit, double x1, double y1, double z1, float f,int pass) {
		TileEntityRemotePowerCounter tileentity=(TileEntityRemotePowerCounter)tileentit;
		world=tileentity.getWorld();
		metadata=H.getBlockMetadata(tileentity.getWorld(), tileentity.getPos());
		
		float x=0,y=0,z=0,xr=0,yr=0,zr=0;
		switch (metadata){
		case 0:{
			zr=-90;
			y=p*6;
		}break;
		case 1:{
			zr=90;
			y=p*-6;
		}break;
		case 2:{
			yr=90;
			z=p*6;
		}break;
		case 3:{
			yr=-90;
			z=p*-6;
		}break;
		case 4:{
			x=p*6;
		}break;
		case 5:{
			yr=180;
			x=-p*6;
		}break;
		default:{
		}break;
			
		}
		
		block=H.getBlock(world,tileentity.getPos());
		
		
		powerBar=tileentity.powerBar;
		maxPB=tileentity.maxPB;
		currentP=tileentity.currentP;
		
        GL11.glTranslated(x1,y1,z1);
		GL11.glTranslatef(x,y,z);
        GL11.glTranslated(0.5, 0.5, 0.5);
	    GL11.glRotatef(-zr, 0, 0, 1);
	    GL11.glRotatef(-yr, 0, 1, 0);
	    GL11.glRotatef(-xr, 1, 0, 0);
        GL11.glTranslated(-0.5, -0.5+p*1, -0.5);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		drawCore(tileentity.getPos().getX(),tileentity.getPos().getY(),tileentity.getPos().getZ());
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1, 1, 1, 1);
		drawText();
		
		GL11.glEnable(GL11.GL_LIGHTING);
        
        GL11.glTranslated(0.5, 0.5-p*1, 0.5);
        GL11.glRotatef(xr, 1, 0, 0);
        GL11.glRotatef(yr, 0, 1, 0);
        GL11.glRotatef(zr, 0, 0, 1);
        GL11.glTranslated(-0.5, -0.5, -0.5);
	    GL11.glTranslatef(-x, -y, -z);
		GL11.glTranslated(-x1, -y1, -z1);
		
		
		
		
	}
	
	public void drawText(){
		float x=p*8-0.001F;
		float y=p*9;
		float z=p*3;
		float xr=90;
		float yr=90;
		float zr=90;
		float scale=0.0049F;
		
		GL11.glTranslatef(x,y,z);
	    GL11.glRotatef(-zr, 0, 0, 1);GL11.glRotatef(-yr, 0, 1, 0);GL11.glRotatef(-xr, 1, 0, 0);
	    GL11.glScalef(scale, scale, scale);
		
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        String pauwa=Integer.toString(maxPB)+"/"+Integer.toString(currentP);
        double Precent=currentP!=maxPB?(powerBar*100>0?powerBar*100:0):100;
        DecimalFormat df = new DecimalFormat("###.##");
        String PrecentS=Precent<=0?(currentP>0?"Almost empty":"Empty"):(df.format(Precent)+"%");
        
        fr.drawString(pauwa, 0, 0, 11111);
        fr.drawString(PrecentS, 0, 10, 11111);
        fr.drawString(block.getLocalizedName(), 0, 20, 11111);
        
	    GL11.glScalef(1F/scale, 1F/scale, 1F/scale);
        GL11.glRotatef(xr, 1, 0, 0);GL11.glRotatef(yr, 0, 1, 0);GL11.glRotatef(zr, 0, 0, 1);
	    GL11.glTranslatef(-x, -y, -z);
	}

	public void drawCore(int x, int y, int z){
			
			TessHelper.bindTexture(Textures.PowerCounterEnergyBar);
			double var1=powerBar;
			double var2=p*5+p*4*var1;
			double var3=1-var1;
			
			{
			buf.addVertexWithUV(p*8-0.001, var2, p*11, 0, var3);
			buf.addVertexWithUV(p*8-0.001, p*5,  p*11, 0, 1);
			buf.addVertexWithUV(p*8-0.001, p*5,  p*13, 1, 1);
			buf.addVertexWithUV(p*8-0.001, var2, p*13, 1, var3);
			buf.draw();
			}
			
			
				double minx=p*8;double miny=p*4;double minz=p*2;
				double maxx=p*10;double maxy=p*10;double maxz=p*14;
				

				TessHelper.bindTexture(Textures.vanillaBrick);
				
				
				{
				buf.addVertexWithUV(minx, maxy, minz, minz, miny);
				buf.addVertexWithUV(minx, miny, minz, minz, maxy);
				buf.addVertexWithUV(minx, miny, maxz, maxz, maxy);
				buf.addVertexWithUV(minx, maxy, maxz, maxz, miny);
				}
				
				
				{
				buf.addVertexWithUV(maxx, maxy, maxz,  1, 1);
				buf.addVertexWithUV(maxx, miny,  maxz, 1, 0);
				buf.addVertexWithUV(maxx, miny,  minz, 0, 0);
				buf.addVertexWithUV(maxx, maxy, minz,  0, 1);
				}
				
				
				{
				buf.addVertexWithUV(minx, maxy, maxz,  0, 1);
				buf.addVertexWithUV(minx, miny , maxz, 0, 0);
				buf.addVertexWithUV(maxx, miny, maxz,  1, 0);
				buf.addVertexWithUV(maxx, maxy, maxz,  1, 1);
				}
				
				
				{
				buf.addVertexWithUV(maxx, maxy, minz,  1, 1);
				buf.addVertexWithUV(maxx, miny, minz,  1, 0);
				buf.addVertexWithUV(minx, miny , minz, 0, 0);
				buf.addVertexWithUV(minx, maxy, minz,  0, 1);
				}
				
				
				{
				buf.addVertexWithUV(maxx, maxy, maxz, 1, 1);
				buf.addVertexWithUV(maxx, maxy, minz, 1, 0);
				buf.addVertexWithUV(minx, maxy, minz, 0, 0);
				buf.addVertexWithUV(minx, maxy, maxz, 0, 1);
				}
				
				
				{
				buf.addVertexWithUV(minx, miny, maxz, 0, 1);
				buf.addVertexWithUV(minx, miny, minz, 0, 0);
				buf.addVertexWithUV(maxx, miny, minz, 1, 0);
				buf.addVertexWithUV(maxx, miny, maxz, 1, 1);
			}
			buf.draw();
			
		
	}
	
	

}
