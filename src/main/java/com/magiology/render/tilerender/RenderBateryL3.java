package com.magiology.render.tilerender;

import com.magiology.objhelper.helpers.renderers.ShadedQuad;
import com.magiology.render.Textures;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

public class RenderBateryL3 extends TileEntitySpecialRenderer {

	private final float p=1F/16F;
	private final float tW=1F/64F;
	private final float tH=1F/64F;
	private final float tWC=1F/112F;
	private final float tHC=1F/16F;
	
	public ForgeDirection[] connections = new ForgeDirection[6];
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		GL11.glTranslated(x, y, z);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		this.renderSides();
		this.renderConections(ForgeDirection.DOWN);
		this.renderConections(ForgeDirection.EAST);
		this.renderConections(ForgeDirection.NORTH);
		this.renderConections(ForgeDirection.SOUTH);
		this.renderConections(ForgeDirection.UP);
		this.renderConections(ForgeDirection.WEST);
		
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
	}
	
	public void renderSides() {

		Tessellator tess = Tessellator.instance;
		this.bindTexture(Textures.BateryL3Core);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*15,   p*1,  tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*1,    p*1,  tW*48, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*1,    p*15, tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*15,   p*15, tW*17, tH*1);

			ShadedQuad.addVertexWithUVWRender(p*15,  p*15,   p*15, tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*15,  p*1,    p*15, tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*15,  p*1,    p*1,  tW*48, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*15,  p*15,   p*1,  tW*48, tH*1);
		
			ShadedQuad.addVertexWithUVWRender(p*15, p*1,    p*15,  tW*48, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*15, p*15,   p*15,  tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*15,   p*15,  tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*1,    p*15,  tW*17, tH*4);

			ShadedQuad.addVertexWithUVWRender(p*1,  p*1,    p*1,  tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*15,   p*1,  tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*15, p*15,   p*1,  tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*15, p*1,    p*1,  tW*48, tH*4);
			
			ShadedQuad.addVertexWithUVWRender(p*1,  p*15,    p*1,  tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*15,   p*15,  tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*15, p*15,   p*15,  tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*15, p*15,    p*1,  tW*48, tH*4);
			
			ShadedQuad.addVertexWithUVWRender(p*15, p*1,    p*1,  tW*48, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*15, p*1,   p*15,  tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*1,   p*15,  tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*1,    p*1,  tW*17, tH*4);	
			
		
	}
	public void renderConections(ForgeDirection dir)
	{
		Tessellator tess = Tessellator.instance;
		this.bindTexture(Textures.BateryL3Core);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			if(dir.equals(ForgeDirection.WEST)){
			}
			else if (dir.equals(ForgeDirection.UP)){
				GL11.glRotatef(-90, 0, 0, 1);
			}
			else if (dir.equals(ForgeDirection.DOWN)){
				GL11.glRotatef(90, 0, 0, 1);
			}
			else if (dir.equals(ForgeDirection.SOUTH)){
				GL11.glRotatef(90, 0, 1, 0);
			}
			else if (dir.equals(ForgeDirection.EAST)){
				GL11.glRotatef(-180, 0, 1, 0);
			}
			else if (dir.equals(ForgeDirection.NORTH)){
				GL11.glRotatef(-90, 0, 1, 0);
			}
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			
			ShadedQuad.addVertexWithUVWRender(p*0,  p*9.5, p*6.5,  tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*0,  p*6.5, p*6.5,  tW*48, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*0,  p*6.5, p*9.5, tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*0,  p*9.5, p*9.5, tW*17, tH*1);
			
			ShadedQuad.addVertexWithUVWRender(p*0, p*9.5, p*9.5, tWC*24.5, tHC*16);
			ShadedQuad.addVertexWithUVWRender(p*0, p*6.5, p*9.5, tWC*24.5, tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*1, p*6.5, p*9.5, tWC*0,    tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*1, p*9.5, p*9.5, tWC*0,    tHC*16);
			
			ShadedQuad.addVertexWithUVWRender(p*1, p*9.5, p*6.5, tWC*0,    tHC*16);
			ShadedQuad.addVertexWithUVWRender(p*1, p*6.5, p*6.5, tWC*0,    tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*0, p*6.5, p*6.5, tWC*24.5, tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*0, p*9.5, p*6.5, tWC*24.5, tHC*16);
			
			ShadedQuad.addVertexWithUVWRender(p*1, p*9.5, p*9.5, tWC*0,    tHC*16);
			ShadedQuad.addVertexWithUVWRender(p*1, p*9.5, p*6.5, tWC*0,    tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*0, p*9.5, p*6.5, tWC*24.5, tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*0, p*9.5, p*9.5, tWC*24.5, tHC*16);
			
			ShadedQuad.addVertexWithUVWRender(p*0, p*6.5, p*9.5, tWC*24.5, tHC*16);
			ShadedQuad.addVertexWithUVWRender(p*0, p*6.5, p*6.5, tWC*24.5, tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*1, p*6.5, p*6.5, tWC*0,    tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*1, p*6.5, p*9.5, tWC*0,    tHC*16);
			
			this.bindTexture(Textures.BateryL3Core);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(90, 0, 0, 1);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			
			ShadedQuad.addVertexWithUVWRender(p*0.5, p*15.5, p*0.5,  tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*0.5, p*15.5, p*15.5, tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*1,   p*15.5, p*15.5, tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*1,   p*15.5, p*0.5,  tW*48, tH*4);
			
			ShadedQuad.addVertexWithUVWRender(p*15,  p*15.5, p*0.5,  tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*15,  p*15.5, p*15.5, tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*15.5,p*15.5, p*15.5, tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*15.5,p*15.5, p*0.5,  tW*48, tH*4);
			
			ShadedQuad.addVertexWithUVWRender(p*1,   p*15.5, p*1,   tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*15,  p*15.5, p*1,   tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*15,  p*15.5, p*0.5, tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*1,   p*15.5, p*0.5, tW*48, tH*4);
			
			ShadedQuad.addVertexWithUVWRender(p*1,   p*15.5, p*15.5, tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*15,  p*15.5, p*15.5, tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*15,  p*15.5, p*15,   tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*1,   p*15.5, p*15,   tW*48, tH*4);
			
			
			ShadedQuad.addVertexWithUVWRender(p*1,  p*15.5, p*1,  tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*15.5, p*15, tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*15,   p*15, tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*15,   p*1,  tW*48, tH*4);
			
			ShadedQuad.addVertexWithUVWRender(p*15, p*15, p*1,    tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*15, p*15, p*15,   tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*15, p*15.5, p*15, tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*15, p*15.5, p*1,  tW*48, tH*4);
			
			ShadedQuad.addVertexWithUVWRender(p*1,  p*15,   p*1, tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*15, p*15,   p*1, tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*15, p*15.5, p*1, tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*15.5, p*1, tW*48, tH*4);
			
			ShadedQuad.addVertexWithUVWRender(p*1,  p*15.5, p*15, tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(p*15, p*15.5, p*15, tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*15, p*15,   p*15, tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(p*1,  p*15,   p*15, tW*48, tH*4);
		
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		GL11.glRotatef(-90, 0, 0, 1);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		if(dir.equals(ForgeDirection.WEST)){
			GL11.glRotatef(0, 0, 1, 0);
		}
		else if (dir.equals(ForgeDirection.SOUTH)){
			GL11.glRotatef(-90, 0, 1, 0);
		}else if (dir.equals(ForgeDirection.EAST)){
			GL11.glRotatef(180, 0, 1, 0);
		}else if (dir.equals(ForgeDirection.UP)){
			GL11.glRotatef(90, 0, 0, 1);
		}
		else if (dir.equals(ForgeDirection.DOWN)){
			GL11.glRotatef(-90, 0, 0, 1);
		}
		else if (dir.equals(ForgeDirection.NORTH)){
			GL11.glRotatef(90, 0, 1, 0);
		}
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	}
	
}