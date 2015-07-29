package com.magiology.render.tilerender;

import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.ShadedQuad;
import com.magiology.render.Textures;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class RenderEnergizedLapisOre extends TileEntitySpecialRenderer {

	private final float p= 1F/16F;
	private final float tW=1F/64F;
	private final float tH=1F/64F;
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f){
		GL11.glPushMatrix();
		this.bindTexture(Textures.EnergizedLapisOre);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11H.SetUpOpaqueRendering(2);
		GL11.glTranslated(x, y, z);
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
			
				ShadedQuad.addVertexWithUVWRender(0,     1.001, 0,     0,1);
				ShadedQuad.addVertexWithUVWRender(0,     1.001, 1.001, 0,0);
				ShadedQuad.addVertexWithUVWRender(1.001, 1.001, 1.001, 1,0);
				ShadedQuad.addVertexWithUVWRender(1.001, 1.001, 0,     1,1);
				
			GL11.glTranslated(0.5, 0.5, 0.5);
			GL11.glRotatef(-b, 1,0,0);GL11.glRotatef(-c, 0,0,1);
			GL11.glTranslated(-0.5, -0.5, -0.5);
		}

		GL11H.EndOpaqueRendering();
		
//		GL11.glDisable(GL11.GL_TEXTURE_2D);
//		GL11.glTranslated(0.5, 1.5, 0.5);
//		new PlateModel(1, 1F, 1, true);
//		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
	
	
}