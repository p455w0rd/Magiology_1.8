package com.magiology.client.render.tilerender;

import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import com.magiology.client.render.Textures;
import com.magiology.mcobjects.tileentityes.TileEntityOreStructureCore;
import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

public class RenderOreStructureCore extends TileEntitySpecialRendererM{
	private final float p= 1F/16F;
	private final float tW=1F/96F;
	private final float tH=1F/80F;
	NormalizedVertixBuffer buf=Render.NVB();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f){
		TileEntityOreStructureCore core= (TileEntityOreStructureCore) tileentity;
		GL11.glTranslated(x,y,z);
		GL11.glTranslated(0.5, 1, 0.5);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		if(core.updateStructureHelper==true)renderTop();
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-0.5, -1, -0.5);
		GL11.glTranslated(-x, -y, -z);
	}

	public void renderTop(){
		GL11.glEnable(GL11.GL_LIGHTING);
		this.bindTexture(Textures.OreStructureCore);
			buf.addVertexWithUV(-p*10, p*0, -p*10,        tW*88, tH*0);
			buf.addVertexWithUV(-p*10, p*0,  p*10,        tW*88, tH*80);
			buf.addVertexWithUV(-p*10, p*2,  p*10,        tW*96, tH*80);
			buf.addVertexWithUV(-p*10, p*2, -p*10,        tW*96, tH*0);
			
			buf.addVertexWithUV(-p*8, p*2, -p*8,          tW*80, tH*0);
			buf.addVertexWithUV(-p*8, p*2,  p*8,          tW*80, tH*72);
			buf.addVertexWithUV(-p*8, p*0,  p*8,          tW*88, tH*72);
			buf.addVertexWithUV(-p*8, p*0, -p*8,          tW*88, tH*0);
			

			buf.addVertexWithUV(p*10, p*2, -p*10,         tW*88, tH*0);
			buf.addVertexWithUV(p*10, p*2,  p*10,         tW*88, tH*80);
			buf.addVertexWithUV(p*10, p*0,  p*10,         tW*96, tH*80);
			buf.addVertexWithUV(p*10, p*0, -p*10,         tW*96, tH*0);

			buf.addVertexWithUV(p*8, p*0, -p*8,           tW*80, tH*0);
			buf.addVertexWithUV(p*8, p*0,  p*8,           tW*80, tH*72);
			buf.addVertexWithUV(p*8, p*2,  p*8,           tW*88, tH*72);
			buf.addVertexWithUV(p*8, p*2, -p*8,           tW*88, tH*0);
			
			
			buf.addVertexWithUV( p*10, p*2, p*10,         tW*88, tH*0);
			buf.addVertexWithUV(-p*10, p*2, p*10,         tW*88, tH*80);
			buf.addVertexWithUV(-p*10, p*0, p*10,         tW*96, tH*80);
			buf.addVertexWithUV( p*10, p*0, p*10,         tW*96, tH*0);
			
			buf.addVertexWithUV( p*8, p*0, p*8,           tW*80, tH*0);
			buf.addVertexWithUV(-p*8, p*0, p*8,           tW*80, tH*72);
			buf.addVertexWithUV(-p*8, p*2, p*8,           tW*88, tH*72);
			buf.addVertexWithUV( p*8, p*2, p*8,           tW*88, tH*0);
			

			buf.addVertexWithUV( p*10, p*0, -p*10,        tW*88, tH*0);
			buf.addVertexWithUV(-p*10, p*0, -p*10,        tW*88, tH*80);
			buf.addVertexWithUV(-p*10, p*2, -p*10,        tW*96, tH*80);
			buf.addVertexWithUV( p*10, p*2, -p*10,        tW*96, tH*0);

			buf.addVertexWithUV( p*8, p*2, -p*8,          tW*80, tH*0);
			buf.addVertexWithUV(-p*8, p*2, -p*8,          tW*80, tH*72);
			buf.addVertexWithUV(-p*8, p*0, -p*8,          tW*88, tH*72);
			buf.addVertexWithUV( p*8, p*0, -p*8,          tW*88, tH*0);
			
			
			
			buf.addVertexWithUV(-p*10, p*2, -p*10,        tW*0,  tH*0);
			buf.addVertexWithUV(-p*10, p*2,  p*10,        tW*80, tH*0);
			buf.addVertexWithUV(p*10,  p*2,  p*10,        tW*80, tH*80);
			buf.addVertexWithUV(p*10,  p*2, -p*10,        tW*0,  tH*80);
			buf.draw();
		GL11.glEnable(GL11.GL_LIGHTING);
	}
}