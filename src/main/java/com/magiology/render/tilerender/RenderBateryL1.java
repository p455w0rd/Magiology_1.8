package com.magiology.render.tilerender;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import org.lwjgl.opengl.GL11;

import com.magiology.objhelper.Get.Render;
import com.magiology.objhelper.helpers.renderers.NoramlisedVertixBuffer;
import com.magiology.objhelper.helpers.renderers.TessHelper;
import com.magiology.render.Textures;

public class RenderBateryL1 extends TileEntitySpecialRenderer {

	private final float p=1F/16F;
	private final float tW=1F/64F;
	private final float tH=1F/64F;
	private final float tWC=1F/112F;
	private final float tHC=1F/16F;
	NoramlisedVertixBuffer buf=Render.NVB();
	
	public EnumFacing[] connections = new EnumFacing[6];
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f,int pass) {
		GL11.glTranslated(x,y,z);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		this.renderSides();
		renderConections(EnumFacing.DOWN);
		renderConections(EnumFacing.EAST);
		renderConections(EnumFacing.NORTH);
		renderConections(EnumFacing.SOUTH);
		renderConections(EnumFacing.UP);
		renderConections(EnumFacing.WEST);
		
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
	}
	
	public void renderSides() {

		WorldRenderer tess=TessHelper.getWR();
		this.bindTexture(Textures.BateryL1Core);
		TessHelper.drawQuad(new double[]{p*6, p*6,p*6, p*6, p*10,p*10,p*10,p*10},
				            new double[]{p*10,p*6,p*6, p*10,p*10,p*6, p*6, p*10}, 
				            new double[]{p*6, p*6,p*10,p*10,p*10,p*10,p*6, p*6},  
				            new double[]{tW*48,tW*48,tW*17,tW*17,tW*17,tW*17,tW*48,tW*48},  
				            new double[]{tH*1, tH*4, tH*4, tH*1, tH*1, tH*4, tH*4, tH*1});
		
			buf.addVertexWithUV(p*10, p*6,    p*10,  tW*48, tH*4);
			buf.addVertexWithUV(p*10, p*10,   p*10,  tW*48, tH*1);
			buf.addVertexWithUV(p*6,  p*10,   p*10,  tW*17, tH*1);
			buf.addVertexWithUV(p*6,  p*6,    p*10,  tW*17, tH*4);

			buf.addVertexWithUV(p*6,  p*6,    p*6,  tW*17, tH*4);
			buf.addVertexWithUV(p*6,  p*10,   p*6,  tW*17, tH*1);
			buf.addVertexWithUV(p*10, p*10,   p*6,  tW*48, tH*1);
			buf.addVertexWithUV(p*10, p*6,    p*6,  tW*48, tH*4);
			
			buf.addVertexWithUV(p*6,  p*10,    p*6,  tW*17, tH*4);
			buf.addVertexWithUV(p*6,  p*10,   p*10,  tW*17, tH*1);
			buf.addVertexWithUV(p*10, p*10,   p*10,  tW*48, tH*1);
			buf.addVertexWithUV(p*10, p*10,    p*6,  tW*48, tH*4);

			buf.addVertexWithUV(p*10, p*6,    p*6,  tW*48, tH*4);
			buf.addVertexWithUV(p*10, p*6,   p*10,  tW*48, tH*1);
			buf.addVertexWithUV(p*6,  p*6,   p*10,  tW*17, tH*1);
			buf.addVertexWithUV(p*6,  p*6,    p*6,  tW*17, tH*4);
			buf.draw();
	}
	public void renderConections(EnumFacing dir)
	{
		WorldRenderer tess=TessHelper.getWR();
		this.bindTexture(Textures.BateryL1Core);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			if(dir.equals(EnumFacing.WEST)){
			}
			else if (dir.equals(EnumFacing.UP)){
				GL11.glRotatef(-90, 0, 0, 1);
			}
			else if (dir.equals(EnumFacing.DOWN)){
				GL11.glRotatef(90, 0, 0, 1);
			}
			else if (dir.equals(EnumFacing.SOUTH)){
				GL11.glRotatef(90, 0, 1, 0);
			}
			else if (dir.equals(EnumFacing.EAST)){
				GL11.glRotatef(-180, 0, 1, 0);
			}
			else if (dir.equals(EnumFacing.NORTH)){
				GL11.glRotatef(-90, 0, 1, 0);
			}
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			
			buf.addVertexWithUV(p*1, p*8.5, p*8.5, tWC*24.5, tHC*16);
			buf.addVertexWithUV(p*1, p*7.5, p*8.5, tWC*24.5, tHC*0);
			buf.addVertexWithUV(p*6, p*7.5, p*8.5, tWC*0,    tHC*0);
			buf.addVertexWithUV(p*6, p*8.5, p*8.5, tWC*0,    tHC*16);

			buf.addVertexWithUV(p*6, p*8.5, p*7.5, tWC*0,    tHC*16);
			buf.addVertexWithUV(p*6, p*7.5, p*7.5, tWC*0,    tHC*0);
			buf.addVertexWithUV(p*1, p*7.5, p*7.5, tWC*24.5, tHC*0);
			buf.addVertexWithUV(p*1, p*8.5, p*7.5, tWC*24.5, tHC*16);
			
			buf.addVertexWithUV(p*6, p*8.5, p*8.5, tWC*0,     tHC*16);
			buf.addVertexWithUV(p*6, p*8.5, p*7.5, tWC*0,     tHC*0);
			buf.addVertexWithUV(p*1, p*8.5, p*7.5, tWC*24.5,  tHC*0);
			buf.addVertexWithUV(p*1, p*8.5, p*8.5, tWC*24.5,  tHC*16);

			buf.addVertexWithUV(p*1, p*7.5, p*8.5, tWC*24.5,  tHC*16);
			buf.addVertexWithUV(p*1, p*7.5, p*7.5, tWC*24.5,  tHC*0);
			buf.addVertexWithUV(p*6, p*7.5, p*7.5, tWC*0,     tHC*0);
			buf.addVertexWithUV(p*6, p*7.5, p*8.5, tWC*0,     tHC*16);
			
			
			buf.addVertexWithUV(p*0,  p*9.5, p*6.5,  tW*48, tH*1);
			buf.addVertexWithUV(p*0,  p*6.5, p*6.5,  tW*48, tH*4);
			buf.addVertexWithUV(p*0,  p*6.5, p*9.5, tW*17, tH*4);
			buf.addVertexWithUV(p*0,  p*9.5, p*9.5, tW*17, tH*1);

			buf.addVertexWithUV(p*1,  p*9.5, p*9.5, tW*17, tH*1);
			buf.addVertexWithUV(p*1,  p*6.5, p*9.5, tW*17, tH*4);
			buf.addVertexWithUV(p*1,  p*6.5, p*6.5,  tW*48, tH*4);
			buf.addVertexWithUV(p*1,  p*9.5, p*6.5,  tW*48, tH*1);		
			
			buf.addVertexWithUV(p*0, p*9.5, p*9.5, tWC*24.5, tHC*16);
			buf.addVertexWithUV(p*0, p*6.5, p*9.5, tWC*24.5, tHC*0);
			buf.addVertexWithUV(p*1, p*6.5, p*9.5, tWC*0,    tHC*0);
			buf.addVertexWithUV(p*1, p*9.5, p*9.5, tWC*0,    tHC*16);
			
			buf.addVertexWithUV(p*1, p*9.5, p*6.5, tWC*0,    tHC*16);
			buf.addVertexWithUV(p*1, p*6.5, p*6.5, tWC*0,    tHC*0);
			buf.addVertexWithUV(p*0, p*6.5, p*6.5, tWC*24.5, tHC*0);
			buf.addVertexWithUV(p*0, p*9.5, p*6.5, tWC*24.5, tHC*16);
			
			buf.addVertexWithUV(p*1, p*9.5, p*9.5, tWC*0,     tHC*16);
			buf.addVertexWithUV(p*1, p*9.5, p*6.5, tWC*0,     tHC*0);
			buf.addVertexWithUV(p*0, p*9.5, p*6.5, tWC*24.5,  tHC*0);
			buf.addVertexWithUV(p*0, p*9.5, p*9.5, tWC*24.5,  tHC*16);
			
			buf.addVertexWithUV(p*0, p*6.5, p*9.5, tWC*24.5,  tHC*16);
			buf.addVertexWithUV(p*0, p*6.5, p*6.5, tWC*24.5,  tHC*0);
			buf.addVertexWithUV(p*1, p*6.5, p*6.5, tWC*0,     tHC*0);
			buf.addVertexWithUV(p*1, p*6.5, p*9.5, tWC*0,     tHC*16);
			buf.draw();

		
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		if(dir.equals(EnumFacing.WEST)){
			GL11.glRotatef(0, 0, 1, 0);
		}
		else if (dir.equals(EnumFacing.SOUTH)){
			GL11.glRotatef(-90, 0, 1, 0);
		}else if (dir.equals(EnumFacing.EAST)){
			GL11.glRotatef(180, 0, 1, 0);
		}else if (dir.equals(EnumFacing.UP)){
			GL11.glRotatef(90, 0, 0, 1);
		}
		else if (dir.equals(EnumFacing.DOWN)){
			GL11.glRotatef(-90, 0, 0, 1);
		}
		else if (dir.equals(EnumFacing.NORTH)){
			GL11.glRotatef(90, 0, 1, 0);
		}
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	}
	
}