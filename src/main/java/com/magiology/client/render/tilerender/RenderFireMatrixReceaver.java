package com.magiology.client.render.tilerender;

import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import com.magiology.client.render.Textures;
import com.magiology.client.render.aftereffect.LongAfterRenderRenderer;
import com.magiology.client.render.aftereffect.TwoDotsLineRender;
import com.magiology.forgepowered.events.client.RenderLoopEvents;
import com.magiology.mcobjects.tileentityes.TileEntityFireMatrixReceaver;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;
import com.magiology.util.utilobjects.vectors.TwoDots;

public class RenderFireMatrixReceaver extends TileEntitySpecialRendererM{
	
	NormalizedVertixBuffer buf=Render.NVB();
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float partialTicks) {
		TileEntityFireMatrixReceaver tile=(TileEntityFireMatrixReceaver) tileEntity;
		
		boolean var1=true;
		for(int a=0;a<RenderLoopEvents.universalLongRender.size();a++){
			LongAfterRenderRenderer ab=RenderLoopEvents.universalLongRender.get(a);
			if(ab instanceof TwoDotsLineRender&&!((TwoDotsLineRender)ab).isDead())if(((TwoDotsLineRender)ab).tile==tile){
				if(tile.hasTransferPoint){
					var1=false;
				}else{
					ab.kill();
				}
			}
		}
		if(var1)RenderLoopEvents.spawnLARR(new TwoDotsLineRender(new TwoDots(tile.x()+0.5, tile.y()+0.5, tile.z()+0.5, tile.transferp.getX()+0.5, tile.transferp.getY()+0.5, tile.transferp.getZ()+0.5),tile));
		
		float rotation=Util.calculateRenderPos(tile.prevRotation,tile.rotation);
		GL11.glPushMatrix();
		GL11.glTranslated(posX,posY,posZ);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		this.bindTexture(Textures.FireMatrixReceaverBase);
		

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11U.SetUpOpaqueRendering(1);
		GL11.glColor4d(1, 0.01, 0.01, 0.2);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		GL11.glRotated(rotation, 0, 1, 0);
		GL11.glRotated(rotation+20, 0, 0, 1);
		
		float noise=PowerUtil.getPowerPrecentage(tile)/50.0F;
		this.drawCube(p*-0.75+Util.CRandF(noise), p*-1.25+Util.CRandF(noise), p*-0.75+Util.CRandF(noise), p*0.75+Util.CRandF(noise), p*0.25+Util.CRandF(noise), p*0.75+Util.CRandF(noise));
		
		GL11.glRotated(rotation+90, 0, -2, 0);
		this.drawCube(p*-0.75+Util.CRandF(noise), p*-1.25+Util.CRandF(noise), p*-0.75+Util.CRandF(noise), p*0.75+Util.CRandF(noise), p*0.25+Util.CRandF(noise), p*0.75+Util.CRandF(noise));
		GL11.glRotated(-rotation-90, 0, -2, 0);
		
		GL11.glRotated(rotation+72, 1, 1, 0);
		this.drawCube(p*-0.75+Util.CRandF(noise), p*-1.25+Util.CRandF(noise), p*-0.75+Util.CRandF(noise), p*0.75+Util.CRandF(noise), p*0.25+Util.CRandF(noise), p*0.75+Util.CRandF(noise));
		GL11.glRotated(-rotation-72, 1, 1, 0);
		
		GL11.glRotated(rotation+64, 0, 1, 1);
		this.drawCube(p*-0.75+Util.CRandF(noise), p*-1.25+Util.CRandF(noise), p*-0.75+Util.CRandF(noise), p*0.75+Util.CRandF(noise), p*0.25+Util.CRandF(noise), p*0.75+Util.CRandF(noise));
		GL11.glRotated(-rotation-64, 0, 1, 1);
		
		GL11.glRotated(rotation+170, 1, 0, 1);
		this.drawCube(p*-0.75+Util.CRandF(noise), p*-1.25+Util.CRandF(noise), p*-0.75+Util.CRandF(noise), p*0.75+Util.CRandF(noise), p*0.25+Util.CRandF(noise), p*0.75+Util.CRandF(noise));
		GL11.glRotated(-rotation-170, 1, 0, 1);
		

		GL11.glRotated(-rotation-231, 1, 1, 0);
		this.drawCube(p*-0.75+Util.CRandF(noise), p*-1.25+Util.CRandF(noise), p*-0.75+Util.CRandF(noise), p*0.75+Util.CRandF(noise), p*0.25+Util.CRandF(noise), p*0.75+Util.CRandF(noise));
		GL11.glRotated(rotation+231, 1, 1, 0);
		
		GL11.glRotated(-rotation-267, 0, 1, 1);
		this.drawCube(p*-0.75+Util.CRandF(noise), p*-1.25+Util.CRandF(noise), p*-0.75+Util.CRandF(noise), p*0.75+Util.CRandF(noise), p*0.25+Util.CRandF(noise), p*0.75+Util.CRandF(noise));
		GL11.glRotated(rotation+267, 0, 1, 1);
		
		GL11.glRotated(-rotation-192, 1, 0, 1);
		this.drawCube(p*-0.75+Util.CRandF(noise), p*-1.25+Util.CRandF(noise), p*-0.75+Util.CRandF(noise), p*0.75+Util.CRandF(noise), p*0.25+Util.CRandF(noise), p*0.75+Util.CRandF(noise));
		GL11.glRotated(rotation+192, 1, 0, 1);
		
		GL11.glPopMatrix();
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11U.EndOpaqueRendering();
		
		this.drawCube(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
		
		
		
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	
	public void drawCube(double minx,double miny,double minz,double maxx,double maxy,double maxz){
			
			buf.addVertexWithUV(minx, maxy, minz, 0, 0);
			buf.addVertexWithUV(minx, miny, minz, 0, 1);
			buf.addVertexWithUV(minx, miny, maxz, 1, 1);
			buf.addVertexWithUV(minx, maxy, maxz, 1, 0);
			
			buf.addVertexWithUV(maxx, maxy, maxz,  1, 1);
			buf.addVertexWithUV(maxx, miny,  maxz, 1, 0);
			buf.addVertexWithUV(maxx, miny,  minz, 0, 0);
			buf.addVertexWithUV(maxx, maxy, minz,  0, 1);
			
			buf.addVertexWithUV(minx, maxy, maxz,  0, 1);
			buf.addVertexWithUV(minx, miny , maxz, 0, 0);
			buf.addVertexWithUV(maxx, miny, maxz,  1, 0);
			buf.addVertexWithUV(maxx, maxy, maxz,  1, 1);
			
			buf.addVertexWithUV(maxx, maxy, minz,  1, 1);
			buf.addVertexWithUV(maxx, miny, minz,  1, 0);
			buf.addVertexWithUV(minx, miny , minz, 0, 0);
			buf.addVertexWithUV(minx, maxy, minz,  0, 1);
			
			buf.addVertexWithUV(maxx, maxy, maxz, 1, 1);
			buf.addVertexWithUV(maxx, maxy, minz, 1, 0);
			buf.addVertexWithUV(minx, maxy, minz, 0, 0);
			buf.addVertexWithUV(minx, maxy, maxz, 0, 1);
			
			buf.addVertexWithUV(minx, miny, maxz, 0, 1);
			buf.addVertexWithUV(minx, miny, minz, 0, 0);
			buf.addVertexWithUV(maxx, miny, minz, 1, 0);
			buf.addVertexWithUV(maxx, miny, maxz, 1, 1);
			buf.draw();
	}
	
}
