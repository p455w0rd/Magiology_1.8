package com.magiology.client.render.tilerender;

import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import com.magiology.client.render.Textures;
import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

public class RenderFireExhaust extends TileEntitySpecialRendererM {
	
	private final float p= 1F/16F;
	private final float tW=1F/97F;
	private final float tH=1F/90F;
	double animation;
	NormalizedVertixBuffer buf=Render.NVB();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		GL11.glTranslated(x,y,z);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);

				drawFireExhaust();
				
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
	}
	
	public void drawFireExhaust(){
		this.bindTexture(Textures.FireExhaust);
			//gore
		{
			buf.addVertexWithUV(p*6.5,  p*16, p*9.5,  tW*72, tH*90);
			buf.addVertexWithUV(p*6.5,  p*7,  p*9.5,  tW*0,  tH*90);
			buf.addVertexWithUV(p*9.5,  p*7,  p*9.5,  tW*0,  tH*66);
			buf.addVertexWithUV(p*9.5,  p*16, p*9.5,  tW*72, tH*66);

			buf.addVertexWithUV(p*9.5,  p*16, p*6.5,  tW*72, tH*90);
			buf.addVertexWithUV(p*9.5,  p*7,  p*6.5,  tW*0,  tH*90);
			buf.addVertexWithUV(p*6.5,  p*7,  p*6.5,  tW*0,  tH*66);
			buf.addVertexWithUV(p*6.5,  p*16, p*6.5,  tW*72, tH*66);
			
			buf.addVertexWithUV(p*9.5,  p*16, p*9.5,  tW*72, tH*90);
			buf.addVertexWithUV(p*9.5,  p*7,  p*9.5,  tW*0,  tH*90);
			buf.addVertexWithUV(p*9.5,  p*7,  p*6.5,  tW*0,  tH*66);
			buf.addVertexWithUV(p*9.5,  p*16, p*6.5,  tW*72, tH*66);

			buf.addVertexWithUV(p*6.5,  p*16, p*6.5,  tW*72, tH*90);
			buf.addVertexWithUV(p*6.5,  p*7,  p*6.5,  tW*0,  tH*90);
			buf.addVertexWithUV(p*6.5,  p*7,  p*9.5,  tW*0,  tH*66);
			buf.addVertexWithUV(p*6.5,  p*16, p*9.5,  tW*72, tH*66);
		}
			
		{
			buf.addVertexWithUV(p*5.5,  p*6, p*10.5,  tW*57, tH*41);
			buf.addVertexWithUV(p*5.5,  p*7, p*10.5,  tW*57, tH*49);
			buf.addVertexWithUV(p*5.5,  p*7, p*5.5,   tW*97, tH*49);
			buf.addVertexWithUV(p*5.5,  p*6, p*5.5,   tW*97, tH*41);

			buf.addVertexWithUV(p*10.5,  p*6, p*5.5,   tW*57, tH*41);
			buf.addVertexWithUV(p*10.5,  p*7, p*5.5,   tW*57, tH*49);
			buf.addVertexWithUV(p*10.5,  p*7, p*10.5,  tW*97, tH*49);
			buf.addVertexWithUV(p*10.5,  p*6, p*10.5,  tW*97, tH*41);
			
			buf.addVertexWithUV(p*5.5,  p*6,  p*5.5,  tW*57, tH*41);
			buf.addVertexWithUV(p*5.5,  p*7,  p*5.5,  tW*57, tH*49);
			buf.addVertexWithUV(p*10.5, p*7,  p*5.5,  tW*97, tH*49);
			buf.addVertexWithUV(p*10.5, p*6,  p*5.5,  tW*97, tH*41);

			buf.addVertexWithUV(p*10.5, p*6,  p*10.5,  tW*57, tH*41);
			buf.addVertexWithUV(p*10.5, p*7,  p*10.5,  tW*57, tH*49);
			buf.addVertexWithUV(p*5.5,  p*7,  p*10.5,  tW*97, tH*49);
			buf.addVertexWithUV(p*5.5,  p*6,  p*10.5,  tW*97, tH*41);
			

			buf.addVertexWithUV(p*6.5,  p*6, p*6.5,  tW*57, tH*50);
			buf.addVertexWithUV(p*6.5,  p*7, p*6.5,  tW*57, tH*58);
			buf.addVertexWithUV(p*6.5,  p*7, p*9.5,  tW*79, tH*58);
			buf.addVertexWithUV(p*6.5,  p*6, p*9.5,  tW*79, tH*50);

			buf.addVertexWithUV(p*9.5,  p*6, p*9.5,  tW*57, tH*50);
			buf.addVertexWithUV(p*9.5,  p*7, p*9.5,  tW*57, tH*58);
			buf.addVertexWithUV(p*9.5,  p*7, p*6.5,  tW*79, tH*58);
			buf.addVertexWithUV(p*9.5,  p*6, p*6.5,  tW*79, tH*50);

			buf.addVertexWithUV(p*9.5, p*6,  p*6.5,  tW*57, tH*50);
			buf.addVertexWithUV(p*9.5, p*7,  p*6.5,  tW*57, tH*58);
			buf.addVertexWithUV(p*6.5, p*7,  p*6.5,  tW*79, tH*58);
			buf.addVertexWithUV(p*6.5, p*6,  p*6.5,  tW*79, tH*50);

			buf.addVertexWithUV(p*6.5, p*6,  p*9.5,  tW*57, tH*50);
			buf.addVertexWithUV(p*6.5, p*7,  p*9.5,  tW*57, tH*58);
			buf.addVertexWithUV(p*9.5, p*7,  p*9.5,  tW*79, tH*58);
			buf.addVertexWithUV(p*9.5, p*6,  p*9.5,  tW*79, tH*50);
			

			buf.addVertexWithUV(p*5.5,  p*5, p*5.5,   tW*57, tH*41);
			buf.addVertexWithUV(p*5.5,  p*6, p*5.5,   tW*57, tH*49);
			buf.addVertexWithUV(p*5.5,  p*6, p*10.5,  tW*97, tH*49);
			buf.addVertexWithUV(p*5.5,  p*5, p*10.5,  tW*97, tH*41);

			buf.addVertexWithUV(p*10.5,  p*5, p*10.5,  tW*97, tH*41);
			buf.addVertexWithUV(p*10.5,  p*6, p*10.5,  tW*97, tH*49);
			buf.addVertexWithUV(p*10.5,  p*6, p*5.5,   tW*57, tH*49);
			buf.addVertexWithUV(p*10.5,  p*5, p*5.5,   tW*57, tH*41);

			buf.addVertexWithUV(p*10.5, p*5,  p*5.5,  tW*97, tH*41);
			buf.addVertexWithUV(p*10.5, p*6,  p*5.5,  tW*97, tH*49);
			buf.addVertexWithUV(p*5.5,  p*6,  p*5.5,  tW*57, tH*49);
			buf.addVertexWithUV(p*5.5,  p*5,  p*5.5,  tW*57, tH*41);

			buf.addVertexWithUV(p*5.5,  p*5,  p*10.5,  tW*57, tH*41);
			buf.addVertexWithUV(p*5.5,  p*6,  p*10.5,  tW*57, tH*49);
			buf.addVertexWithUV(p*10.5, p*6,  p*10.5,  tW*97, tH*49);
			buf.addVertexWithUV(p*10.5, p*5,  p*10.5,  tW*97, tH*41);
			
			
			buf.addVertexWithUV(p*10.5, p*7,  p*10.5,  tW*57, tH*40);
			buf.addVertexWithUV(p*10.5, p*7,  p*5.5,   tW*57, tH*0);
			buf.addVertexWithUV(p*5.5,  p*7,  p*5.5,   tW*97, tH*0);
			buf.addVertexWithUV(p*5.5,  p*7,  p*10.5,  tW*97, tH*40);
			
			
			buf.addVertexWithUV(p*6.5, p*7,  p*9.5,  tW*73, tH*66);
			buf.addVertexWithUV(p*6.5, p*7,  p*6.5,  tW*73, tH*88);
			buf.addVertexWithUV(p*9.5, p*7,  p*6.5,  tW*95, tH*88);
			buf.addVertexWithUV(p*9.5, p*7,  p*9.5,  tW*95, tH*66);
			
			
			buf.addVertexWithUV(p*5.5,  p*6,  p*10.5,  tW*57, tH*0);
			buf.addVertexWithUV(p*5.5,  p*6,  p*5.5,   tW*57, tH*40);
			buf.addVertexWithUV(p*10.5, p*6,  p*5.5,   tW*97, tH*40);
			buf.addVertexWithUV(p*10.5, p*6,  p*10.5,  tW*97, tH*0);
			
			
			buf.addVertexWithUV(p*4.5,  p*5,  p*11.5,  tW*0, tH*0);
			buf.addVertexWithUV(p*4.5,  p*5,  p*4.5,   tW*0, tH*56);
			buf.addVertexWithUV(p*11.5, p*5,  p*4.5,   tW*56, tH*56);
			buf.addVertexWithUV(p*11.5, p*5,  p*11.5,  tW*56, tH*0);
		}
			
		{
			buf.addVertexWithUV(p*4.5,  p*5, p*11.5,  tW*0,  tH*56);
			buf.addVertexWithUV(p*4.5,  p*6, p*11.5,  tW*0,  tH*64);
			buf.addVertexWithUV(p*4.5,  p*6, p*4.5,   tW*56, tH*64);
			buf.addVertexWithUV(p*4.5,  p*5, p*4.5,   tW*56, tH*56);

			buf.addVertexWithUV(p*11.5, p*5, p*4.5,   tW*0,  tH*56);
			buf.addVertexWithUV(p*11.5, p*6, p*4.5,   tW*0,  tH*64);
			buf.addVertexWithUV(p*11.5, p*6, p*11.5,  tW*56, tH*64);
			buf.addVertexWithUV(p*11.5, p*5, p*11.5,  tW*56, tH*56);
				
			buf.addVertexWithUV(p*4.5,  p*5,  p*4.5,  tW*0,  tH*56);
			buf.addVertexWithUV(p*4.5,  p*6,  p*4.5,  tW*0,  tH*64);
			buf.addVertexWithUV(p*11.5, p*6,  p*4.5,  tW*56, tH*64);
			buf.addVertexWithUV(p*11.5, p*5,  p*4.5,  tW*56, tH*56);

			buf.addVertexWithUV(p*11.5, p*5,  p*11.5,  tW*0,  tH*56);
			buf.addVertexWithUV(p*11.5, p*6,  p*11.5,  tW*0,  tH*64);
			buf.addVertexWithUV(p*4.5,  p*6,  p*11.5,  tW*56, tH*64);
			buf.addVertexWithUV(p*4.5,  p*5,  p*11.5,  tW*56, tH*56);

			
			buf.addVertexWithUV(p*11.5, p*6,  p*11.5,  tW*0,  tH*56);
			buf.addVertexWithUV(p*11.5, p*6,  p*4.5,   tW*0,  tH*0);
			buf.addVertexWithUV(p*4.5,  p*6,  p*4.5,   tW*56, tH*0);
			buf.addVertexWithUV(p*4.5,  p*6,  p*11.5,  tW*56, tH*56);
		}
		buf.draw();
	}
}