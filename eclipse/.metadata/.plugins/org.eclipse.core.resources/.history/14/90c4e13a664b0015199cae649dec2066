package com.magiology.render.tilerender;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import org.lwjgl.opengl.GL11;

import com.magiology.forgepowered.event.RenderLoopEvents;
import com.magiology.mcobjects.tileentityes.TileEntityFireMatrixReceaver;
import com.magiology.objhelper.Get.Render;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.PowerHelper;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.NoramlisedVertixBuffer;
import com.magiology.objhelper.vectors.TwoDots;
import com.magiology.render.Textures;
import com.magiology.render.aftereffect.LongAfterRenderRenderer;
import com.magiology.render.aftereffect.TwoDotsLineRender;

public class RenderFireMatrixReceaver extends TileEntitySpecialRenderer {

	private final float p=1F/16F;
	private final float tW=1F/64F;
	private final float tH=1F/64F;
	
	
	public EnumFacing[] connections = new EnumFacing[6];
	NoramlisedVertixBuffer buf=Render.NVB();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f,int pass) {
		TileEntityFireMatrixReceaver tile=(TileEntityFireMatrixReceaver) tileentity;
		
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
		
		float rotation=Helper.calculateRenderPos(tile.prevRotation,tile.rotation);
		
		GL11.glTranslated(x,y,z);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		this.bindTexture(Textures.FireMatrixReceaverBase);
		

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11H.SetUpOpaqueRendering(1);
		GL11.glColor4d(1, 0.01, 0.01, 0.2);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		GL11.glRotated(rotation, 0, 1, 0);
		GL11.glRotated(rotation+20, 0, 0, 1);
		
		float noise=PowerHelper.getPowerPrecentage(tile)/50.0F;
		this.drawCube(p*-0.75+Helper.CRandF(noise), p*-1.25+Helper.CRandF(noise), p*-0.75+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise), p*0.25+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise));
		
		GL11.glRotated(rotation+90, 0, -2, 0);
		this.drawCube(p*-0.75+Helper.CRandF(noise), p*-1.25+Helper.CRandF(noise), p*-0.75+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise), p*0.25+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise));
		GL11.glRotated(-rotation-90, 0, -2, 0);
		
		GL11.glRotated(rotation+72, 1, 1, 0);
		this.drawCube(p*-0.75+Helper.CRandF(noise), p*-1.25+Helper.CRandF(noise), p*-0.75+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise), p*0.25+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise));
		GL11.glRotated(-rotation-72, 1, 1, 0);
		
		GL11.glRotated(rotation+64, 0, 1, 1);
		this.drawCube(p*-0.75+Helper.CRandF(noise), p*-1.25+Helper.CRandF(noise), p*-0.75+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise), p*0.25+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise));
		GL11.glRotated(-rotation-64, 0, 1, 1);
		
		GL11.glRotated(rotation+170, 1, 0, 1);
		this.drawCube(p*-0.75+Helper.CRandF(noise), p*-1.25+Helper.CRandF(noise), p*-0.75+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise), p*0.25+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise));
		GL11.glRotated(-rotation-170, 1, 0, 1);
		

		GL11.glRotated(-rotation-231, 1, 1, 0);
		this.drawCube(p*-0.75+Helper.CRandF(noise), p*-1.25+Helper.CRandF(noise), p*-0.75+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise), p*0.25+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise));
		GL11.glRotated(rotation+231, 1, 1, 0);
		
		GL11.glRotated(-rotation-267, 0, 1, 1);
		this.drawCube(p*-0.75+Helper.CRandF(noise), p*-1.25+Helper.CRandF(noise), p*-0.75+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise), p*0.25+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise));
		GL11.glRotated(rotation+267, 0, 1, 1);
		
		GL11.glRotated(-rotation-192, 1, 0, 1);
		this.drawCube(p*-0.75+Helper.CRandF(noise), p*-1.25+Helper.CRandF(noise), p*-0.75+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise), p*0.25+Helper.CRandF(noise), p*0.75+Helper.CRandF(noise));
		GL11.glRotated(rotation+192, 1, 0, 1);
		
		GL11.glPopMatrix();
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11H.EndOpaqueRendering();
		
		this.drawCube(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
		
		
		
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
	}

	
	private void drawBase(){
		double minx=p*8;double miny=p*4;double minz=p*2;
		double maxx=p*10;double maxy=p*10;double maxz=p*14;

		this.bindTexture(Textures.FireMatrixReceaverBase);
		
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
