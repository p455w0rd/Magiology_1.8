package com.magiology.client.render.tilerender;

import net.minecraft.tileentity.*;

import org.lwjgl.opengl.*;

import com.magiology.client.render.*;
import com.magiology.mcobjects.tileentityes.*;
import com.magiology.util.renderers.*;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilobjects.m_extension.*;

public class RenderBFCPowerOut extends TileEntitySpecialRendererM {
	
	public double TBM=0;
	public int TBMS=0;
	private final float p= 1F/16F;
	private final float tWC=1F/4F;
	private final float tHC=1F/40F;
	NormalizedVertixBuffer buf=Render.NVB();
	
@Override
public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
	TileEntityBFCPowerOut tile=(TileEntityBFCPowerOut) tileentity;
	int roation=0;
	int rotat;
		GL11.glTranslated(x,y,z);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		if(tile.CallDir[2]!=null)roation=-90;
		else if(tile.CallDir[3]!=null)roation=90;
		else if(tile.CallDir[1]!=null)roation=180;
		else if(tile.CallDir[0]!=null)roation=0;
		
		GL11.glTranslated(0.5, 0.5, 0.5);
		GL11.glRotatef(roation, 0, 1, 0);
		GL11.glTranslated(-0.5, -0.5, -0.5);
		
		renderTop();
		
		for(int r12=0;r12<4;r12++){
			
			if(r12==0)rotat=0;
			else if(r12==1)rotat=90;
			else if(r12==2)rotat=180;
			else rotat=270;
			
			GL11.glTranslated(0.5, 0.5, 0.5);
			GL11.glRotatef((rotat+tile.animationP1*400), 1, 0, 0);
			GL11.glTranslatef(tile.animationP1, 0, tile.animationP1/2);
			GL11.glTranslated(-0.5, -0.5, -0.5);
			
			
			
		renderHand();
		
		GL11.glTranslated(0.5, 0.5, 0.5);
		GL11.glTranslatef(0, -tile.animationP2, 0);
		GL11.glTranslated(-0.5, -0.5, -0.5);
		
		renderHand2();
		
		GL11.glTranslated(0.5, 0.5, 0.5);
		GL11.glTranslatef(0, tile.animationP2, 0);
		GL11.glTranslated(-0.5, -0.5, -0.5);

		GL11.glTranslated(0.5, 0.5, 0.5);
		GL11.glTranslatef(-tile.animationP1, 0, -tile.animationP1/2);
		GL11.glRotatef(-(rotat+tile.animationP1*400), 1, 0, 0);
		GL11.glTranslated(-0.5, -0.5, -0.5);
}
		
		GL11.glTranslated(0.5, 0.5, 0.5);
		GL11.glRotatef(-roation, 0, 1, 0);
		GL11.glTranslated(-0.5, -0.5, -0.5);
		
		GL11.glTranslated(-x, -y, -z);
	}

	public void renderTop(){
		this.bindTexture(Textures.BigFurnaceOutput);
			buf.addVertexWithUV(p*16+0.0002, p*10, p*6,     tWC*4, tHC*4);
			buf.addVertexWithUV(p*16+0.0002, p*10, p*10,    tWC*4, tHC*0);
			buf.addVertexWithUV(p*16+0.0002, p*6,  p*10,    tWC*0, tHC*0);
			buf.addVertexWithUV(p*16+0.0002, p*6,  p*6,     tWC*0, tHC*4);
			buf.draw();
			
	}
	public void renderHand(){
		this.bindTexture(Textures.BigFurnaceOutput);
		
				GL11.glTranslated(0, p*0.25, p*0.75);
				buf.addVertexWithUV(p*17.5, p*12, p*8,     tWC*4, tHC*4);
				buf.addVertexWithUV(p*16,   p*12, p*8,     tWC*4, tHC*0);
				buf.addVertexWithUV(p*16,   p*10.5, p*8,   tWC*0, tHC*0);
				buf.addVertexWithUV(p*17.5, p*10.5, p*8,   tWC*0, tHC*4);
	
				buf.addVertexWithUV(p*17.5, p*10.5, p*6.5, tWC*0, tHC*4);
				buf.addVertexWithUV(p*16,   p*10.5, p*6.5, tWC*0, tHC*0);
				buf.addVertexWithUV(p*16,   p*12,  p*6.5,  tWC*4, tHC*0);
				buf.addVertexWithUV(p*17.5, p*12,  p*6.5,  tWC*4, tHC*4);
				
				buf.addVertexWithUV(p*16,   p*12, p*6.5,   tWC*4, tHC*4);
				buf.addVertexWithUV(p*16,   p*12, p*8,     tWC*4, tHC*0);
				buf.addVertexWithUV(p*17.5, p*12, p*8,     tWC*0, tHC*0);
				buf.addVertexWithUV(p*17.5, p*12, p*6.5,   tWC*0, tHC*4);
	
				buf.addVertexWithUV(p*17.5, p*10.5, p*6.5, tWC*0, tHC*4);
				buf.addVertexWithUV(p*17.5, p*10.5, p*8,   tWC*0, tHC*0);
				buf.addVertexWithUV(p*16,   p*10.5, p*8,   tWC*4, tHC*0);
				buf.addVertexWithUV(p*16,   p*10.5, p*6.5, tWC*4, tHC*4);
				buf.draw();
			GL11.glTranslated(0, -p*0.25, -p*0.75);
		
		
		
	}
	
	public void renderHand2(){
		this.bindTexture(Textures.BigFurnaceOutput);
			buf.addVertexWithUV(p*19, p*13, p*6,    tWC*4, tHC*4);
			buf.addVertexWithUV(p*19, p*13, p*10,   tWC*4, tHC*0);
			buf.addVertexWithUV(p*19, p*10, p*10,   tWC*0, tHC*0);
			buf.addVertexWithUV(p*19, p*10, p*6,    tWC*0, tHC*4);
			
			buf.addVertexWithUV(p*17.5, p*10, p*6,    tWC*0, tHC*4);
			buf.addVertexWithUV(p*17.5, p*10, p*10,   tWC*0, tHC*0);
			buf.addVertexWithUV(p*17.5, p*13, p*10,   tWC*4, tHC*0);
			buf.addVertexWithUV(p*17.5, p*13, p*6,    tWC*4, tHC*4);
			
			buf.addVertexWithUV(p*19,   p*13, p*10, tWC*4, tHC*4);
			buf.addVertexWithUV(p*17.5, p*13, p*10, tWC*4, tHC*0);
			buf.addVertexWithUV(p*17.5, p*10, p*10, tWC*0, tHC*0);
			buf.addVertexWithUV(p*19,   p*10, p*10, tWC*0, tHC*4);
			
			buf.addVertexWithUV(p*19,   p*10, p*6,  tWC*0, tHC*4);
			buf.addVertexWithUV(p*17.5, p*10, p*6,  tWC*0, tHC*0);
			buf.addVertexWithUV(p*17.5, p*13, p*6,  tWC*4, tHC*0);
			buf.addVertexWithUV(p*19,   p*13, p*6,  tWC*4, tHC*4);
			
			buf.addVertexWithUV(p*17.5, p*13, p*6,    tWC*4, tHC*4);
			buf.addVertexWithUV(p*17.5, p*13, p*10,   tWC*4, tHC*0);
			buf.addVertexWithUV(p*19,   p*13, p*10,   tWC*0, tHC*0);
			buf.addVertexWithUV(p*19,   p*13, p*6,    tWC*0, tHC*4);
			
			buf.addVertexWithUV(p*19,   p*10, p*6,    tWC*0, tHC*4);
			buf.addVertexWithUV(p*19,   p*10, p*10,   tWC*0, tHC*0);
			buf.addVertexWithUV(p*17.5, p*10, p*10,   tWC*4, tHC*0);
			buf.addVertexWithUV(p*17.5, p*10, p*6,    tWC*4, tHC*4);
			buf.draw();
			
			
		
	}
}