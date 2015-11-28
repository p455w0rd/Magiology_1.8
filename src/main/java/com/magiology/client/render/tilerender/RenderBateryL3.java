package com.magiology.client.render.tilerender;

import net.minecraft.tileentity.*;
import net.minecraft.util.*;

import org.lwjgl.opengl.*;

import com.magiology.client.render.*;
import com.magiology.util.renderers.*;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilobjects.m_extension.*;

public class RenderBateryL3 extends TileEntitySpecialRendererM {

	private final float p=1F/16F;
	private final float tW=1F/64F;
	private final float tH=1F/64F;
	private final float tWC=1F/112F;
	private final float tHC=1F/16F;
	
	public EnumFacing[] connections = new EnumFacing[6];
	
	NormalizedVertixBuffer buf=Render.NVB();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		GL11.glTranslated(x,y,z);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		this.renderSides();
		this.renderConections(EnumFacing.DOWN);
		this.renderConections(EnumFacing.EAST);
		this.renderConections(EnumFacing.NORTH);
		this.renderConections(EnumFacing.SOUTH);
		this.renderConections(EnumFacing.UP);
		this.renderConections(EnumFacing.WEST);
		
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
	}
	
	public void renderSides() {

		this.bindTexture(Textures.BateryL3Core);
			buf.addVertexWithUV(p*1,  p*15,   p*1,  tW*48, tH*1);
			buf.addVertexWithUV(p*1,  p*1,    p*1,  tW*48, tH*4);
			buf.addVertexWithUV(p*1,  p*1,    p*15, tW*17, tH*4);
			buf.addVertexWithUV(p*1,  p*15,   p*15, tW*17, tH*1);

			buf.addVertexWithUV(p*15,  p*15,   p*15, tW*17, tH*1);
			buf.addVertexWithUV(p*15,  p*1,    p*15, tW*17, tH*4);
			buf.addVertexWithUV(p*15,  p*1,    p*1,  tW*48, tH*4);
			buf.addVertexWithUV(p*15,  p*15,   p*1,  tW*48, tH*1);
		
			buf.addVertexWithUV(p*15, p*1,    p*15,  tW*48, tH*4);
			buf.addVertexWithUV(p*15, p*15,   p*15,  tW*48, tH*1);
			buf.addVertexWithUV(p*1,  p*15,   p*15,  tW*17, tH*1);
			buf.addVertexWithUV(p*1,  p*1,    p*15,  tW*17, tH*4);

			buf.addVertexWithUV(p*1,  p*1,    p*1,  tW*17, tH*4);
			buf.addVertexWithUV(p*1,  p*15,   p*1,  tW*17, tH*1);
			buf.addVertexWithUV(p*15, p*15,   p*1,  tW*48, tH*1);
			buf.addVertexWithUV(p*15, p*1,    p*1,  tW*48, tH*4);
			
			buf.addVertexWithUV(p*1,  p*15,    p*1,  tW*17, tH*4);
			buf.addVertexWithUV(p*1,  p*15,   p*15,  tW*17, tH*1);
			buf.addVertexWithUV(p*15, p*15,   p*15,  tW*48, tH*1);
			buf.addVertexWithUV(p*15, p*15,    p*1,  tW*48, tH*4);
			
			buf.addVertexWithUV(p*15, p*1,    p*1,  tW*48, tH*4);
			buf.addVertexWithUV(p*15, p*1,   p*15,  tW*48, tH*1);
			buf.addVertexWithUV(p*1,  p*1,   p*15,  tW*17, tH*1);
			buf.addVertexWithUV(p*1,  p*1,    p*1,  tW*17, tH*4);	
			buf.draw();
		
	}
	public void renderConections(EnumFacing dir)
	{
		this.bindTexture(Textures.BateryL3Core);
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
			
			buf.addVertexWithUV(p*0,  p*9.5, p*6.5,  tW*48, tH*1);
			buf.addVertexWithUV(p*0,  p*6.5, p*6.5,  tW*48, tH*4);
			buf.addVertexWithUV(p*0,  p*6.5, p*9.5, tW*17, tH*4);
			buf.addVertexWithUV(p*0,  p*9.5, p*9.5, tW*17, tH*1);
			
			buf.addVertexWithUV(p*0, p*9.5, p*9.5, tWC*24.5, tHC*16);
			buf.addVertexWithUV(p*0, p*6.5, p*9.5, tWC*24.5, tHC*0);
			buf.addVertexWithUV(p*1, p*6.5, p*9.5, tWC*0,    tHC*0);
			buf.addVertexWithUV(p*1, p*9.5, p*9.5, tWC*0,    tHC*16);
			
			buf.addVertexWithUV(p*1, p*9.5, p*6.5, tWC*0,    tHC*16);
			buf.addVertexWithUV(p*1, p*6.5, p*6.5, tWC*0,    tHC*0);
			buf.addVertexWithUV(p*0, p*6.5, p*6.5, tWC*24.5, tHC*0);
			buf.addVertexWithUV(p*0, p*9.5, p*6.5, tWC*24.5, tHC*16);
			
			buf.addVertexWithUV(p*1, p*9.5, p*9.5, tWC*0,    tHC*16);
			buf.addVertexWithUV(p*1, p*9.5, p*6.5, tWC*0,    tHC*0);
			buf.addVertexWithUV(p*0, p*9.5, p*6.5, tWC*24.5, tHC*0);
			buf.addVertexWithUV(p*0, p*9.5, p*9.5, tWC*24.5, tHC*16);
			
			buf.addVertexWithUV(p*0, p*6.5, p*9.5, tWC*24.5, tHC*16);
			buf.addVertexWithUV(p*0, p*6.5, p*6.5, tWC*24.5, tHC*0);
			buf.addVertexWithUV(p*1, p*6.5, p*6.5, tWC*0,    tHC*0);
			buf.addVertexWithUV(p*1, p*6.5, p*9.5, tWC*0,    tHC*16);
			buf.draw();
			this.bindTexture(Textures.BateryL3Core);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(90, 0, 0, 1);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			
			buf.addVertexWithUV(p*0.5, p*15.5, p*0.5,  tW*17, tH*4);
			buf.addVertexWithUV(p*0.5, p*15.5, p*15.5, tW*17, tH*1);
			buf.addVertexWithUV(p*1,   p*15.5, p*15.5, tW*48, tH*1);
			buf.addVertexWithUV(p*1,   p*15.5, p*0.5,  tW*48, tH*4);
			
			buf.addVertexWithUV(p*15,  p*15.5, p*0.5,  tW*17, tH*4);
			buf.addVertexWithUV(p*15,  p*15.5, p*15.5, tW*17, tH*1);
			buf.addVertexWithUV(p*15.5,p*15.5, p*15.5, tW*48, tH*1);
			buf.addVertexWithUV(p*15.5,p*15.5, p*0.5,  tW*48, tH*4);
			
			buf.addVertexWithUV(p*1,   p*15.5, p*1,   tW*17, tH*4);
			buf.addVertexWithUV(p*15,  p*15.5, p*1,   tW*17, tH*1);
			buf.addVertexWithUV(p*15,  p*15.5, p*0.5, tW*48, tH*1);
			buf.addVertexWithUV(p*1,   p*15.5, p*0.5, tW*48, tH*4);
			
			buf.addVertexWithUV(p*1,   p*15.5, p*15.5, tW*17, tH*4);
			buf.addVertexWithUV(p*15,  p*15.5, p*15.5, tW*17, tH*1);
			buf.addVertexWithUV(p*15,  p*15.5, p*15,   tW*48, tH*1);
			buf.addVertexWithUV(p*1,   p*15.5, p*15,   tW*48, tH*4);
			
			
			buf.addVertexWithUV(p*1,  p*15.5, p*1,  tW*17, tH*4);
			buf.addVertexWithUV(p*1,  p*15.5, p*15, tW*17, tH*1);
			buf.addVertexWithUV(p*1,  p*15,   p*15, tW*48, tH*1);
			buf.addVertexWithUV(p*1,  p*15,   p*1,  tW*48, tH*4);
			
			buf.addVertexWithUV(p*15, p*15, p*1,    tW*17, tH*4);
			buf.addVertexWithUV(p*15, p*15, p*15,   tW*17, tH*1);
			buf.addVertexWithUV(p*15, p*15.5, p*15, tW*48, tH*1);
			buf.addVertexWithUV(p*15, p*15.5, p*1,  tW*48, tH*4);
			
			buf.addVertexWithUV(p*1,  p*15,   p*1, tW*17, tH*4);
			buf.addVertexWithUV(p*15, p*15,   p*1, tW*17, tH*1);
			buf.addVertexWithUV(p*15, p*15.5, p*1, tW*48, tH*1);
			buf.addVertexWithUV(p*1,  p*15.5, p*1, tW*48, tH*4);
			
			buf.addVertexWithUV(p*1,  p*15.5, p*15, tW*17, tH*4);
			buf.addVertexWithUV(p*15, p*15.5, p*15, tW*17, tH*1);
			buf.addVertexWithUV(p*15, p*15,   p*15, tW*48, tH*1);
			buf.addVertexWithUV(p*1,  p*15,   p*15, tW*48, tH*4);
		buf.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		GL11.glRotatef(-90, 0, 0, 1);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		
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