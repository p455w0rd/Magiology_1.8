package com.magiology.client.render.tilerender;

import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import com.magiology.client.render.Textures;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

public class RenderEnergizedLapisOre extends TileEntitySpecialRendererM {

	NormalizedVertixBuffer buf=Render.NVB();
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f){
		GL11.glPushMatrix();
		this.bindTexture(Textures.EnergizedLapisOre);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11U.SetUpOpaqueRendering(2);
		GL11.glTranslated(x,y,z);
		for(int a=0;a<6;a++){
			int b=0;int c=0;
			
			if(a==1)b=90;
			else if(a==2)b=-90;
			else if(a==3)c=-90;
			else if(a==4)c=90;
			else if(a==5)c=180;
			
			
			GL11.glTranslated(0.5, 0.5, 0.5);
			GL11.glRotatef(b, 1,0,0);GL11.glRotatef(c, 0,0,1);
			GL11.glTranslated(-0.5, -0.5, -0.5);
			
				buf.addVertexWithUV(0,     1.001, 0,     0,1);
				buf.addVertexWithUV(0,     1.001, 1.001, 0,0);
				buf.addVertexWithUV(1.001, 1.001, 1.001, 1,0);
				buf.addVertexWithUV(1.001, 1.001, 0,     1,1);
				buf.draw();
				
			GL11.glTranslated(0.5, 0.5, 0.5);
			GL11.glRotatef(-b, 1,0,0);GL11.glRotatef(-c, 0,0,1);
			GL11.glTranslated(-0.5, -0.5, -0.5);
		}

		GL11U.EndOpaqueRendering();
		
//		GL11.glDisable(GL11.GL_TEXTURE_2D);
//		GL11.glTranslated(0.5, 1.5, 0.5);
//		new PlateModel(1, 1F, 1, true);
//		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
	
	
}