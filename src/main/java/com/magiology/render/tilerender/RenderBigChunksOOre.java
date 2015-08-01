package com.magiology.render.tilerender;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.magiology.core.MReference;
import com.magiology.mcobjects.tileentityes.TileEntityBigChunksOOre;
import com.magiology.objhelper.helpers.renderers.ShadedQuad;
import com.magiology.objhelper.helpers.renderers.TessHelper;

public class RenderBigChunksOOre extends TileEntitySpecialRenderer {
	ResourceLocation BateryL1Core = new ResourceLocation(MReference.MODID+":"+"/textures/blocks/BateryL1Core.png");
	ResourceLocation FirePipeConection = new ResourceLocation(MReference.MODID,"/textures/models/firepipe/FirePipeConection.png");

	private final float p=1F/16F;
	private final float tW=1F/64F;
	private final float tH=1F/64F;
	private final float tWC=1F/112F;
	private final float tHC=1F/16F;
	
	public EnumFacing[] connections = new EnumFacing[6];
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
	WorldRenderer tess=TessHelper.getWR();
		TileEntityBigChunksOOre tile=(TileEntityBigChunksOOre) tileentity;
		GL11.glTranslated(pos);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		if(tile.BlockType==1)renderBlock1(tileentity,x,y,z,f);
		else if(tile.BlockType==2)renderBlock2(tileentity,x,y,z,f);
		else if(tile.BlockType==3)renderBlock3(tileentity,x,y,z,f);
		else{
			this.bindTexture(FirePipeConection);
				ShadedQuad.addVertexWithUVWRender(0,  1, 0,  tW*48, tHC*1);
				ShadedQuad.addVertexWithUVWRender(0,  0, 0,  tW*48, tHC*4);
				ShadedQuad.addVertexWithUVWRender(0,  0, 1,  tW*17, tHC*4);
				ShadedQuad.addVertexWithUVWRender(0,  1, 1,  tW*17, tHC*1);

				ShadedQuad.addVertexWithUVWRender(1,  1, 1,  tW*17, tHC*1);
				ShadedQuad.addVertexWithUVWRender(1,  0, 1,  tW*17, tHC*4);
				ShadedQuad.addVertexWithUVWRender(1,  0, 0,  tW*48, tHC*4);
				ShadedQuad.addVertexWithUVWRender(1,  1, 0,  tW*48, tHC*1);
				
				ShadedQuad.addVertexWithUVWRender(1,  1, 0,  tW*17, tHC*1);
				ShadedQuad.addVertexWithUVWRender(1,  0, 0,  tW*17, tHC*4);
				ShadedQuad.addVertexWithUVWRender(0,  0, 0,  tW*48, tHC*4);
				ShadedQuad.addVertexWithUVWRender(0,  1, 0,  tW*48, tHC*1);
				
				ShadedQuad.addVertexWithUVWRender(0,  1, 1,  tW*48, tHC*1);
				ShadedQuad.addVertexWithUVWRender(0,  0, 1,  tW*48, tHC*4);
				ShadedQuad.addVertexWithUVWRender(1,  0, 1,  tW*17, tHC*4);
				ShadedQuad.addVertexWithUVWRender(1,  1, 1,  tW*17, tHC*1);
				
				ShadedQuad.addVertexWithUVWRender(0,  0, 1,  tW*48, tHC*1);
				ShadedQuad.addVertexWithUVWRender(0,  0, 0,  tW*48, tHC*4);
				ShadedQuad.addVertexWithUVWRender(1,  0, 0,  tW*17, tHC*4);
				ShadedQuad.addVertexWithUVWRender(1,  0, 1,  tW*17, tHC*1);
				
				ShadedQuad.addVertexWithUVWRender(1,  1, 1,  tW*17, tHC*1);
				ShadedQuad.addVertexWithUVWRender(1,  1, 0,  tW*17, tHC*4);
				ShadedQuad.addVertexWithUVWRender(0,  1, 0,  tW*48, tHC*4);
				ShadedQuad.addVertexWithUVWRender(0,  1, 1,  tW*48, tHC*1);
		}
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
	}
	
	public void renderBlock1(TileEntity tileentity, double x, double y, double z, float f){
		
	}
	
	public void renderBlock2(TileEntity tileentity, double x, double y, double z, float f){
		
		TileEntityBigChunksOOre tile=(TileEntityBigChunksOOre) tileentity;
		
		if(tile.rotation>=36000)tile.rotation=0;
		
		tile.rotation+=tile.animP1/100;
		
		for(int side=0;side<6;side++){
//			side=7;
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			if(side==0)GL11.glRotatef(0, 0, 1, 0);
			else if (side==1)GL11.glRotatef(90, 0, 1, 0);
			else if (side==2)GL11.glRotatef(-180, 0, 1, 0);
			else if (side==3)GL11.glRotatef(-90, 0, 0, 1);
			else if (side==4)GL11.glRotatef(90, 0, 0, 1);
			else if (side==5)GL11.glRotatef(-90, 0, 1, 0);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		renderArmour1();
		
		if(side==0||side==1||side==2||side==5)renderStand1();
		
		
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		if(side==0)GL11.glRotatef(0, 0, 1, 0);
		else if (side==1)GL11.glRotatef(-90, 0, 1, 0);
		else if (side==2)GL11.glRotatef(180, 0, 1, 0);
		else if (side==3)GL11.glRotatef(90, 0, 0, 1);
		else if (side==4)GL11.glRotatef(-90, 0, 0, 1);
		else if (side==5)GL11.glRotatef(90, 0, 1, 0);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		}
		
		for(float mu=1;mu<11;mu++){
			GL11.glTranslatef(0, -mu*p, 0);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(tile.rotation+mu*6, 0, 1, 0);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			renderTurbine1();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(-tile.rotation-mu*6, 0, 1, 0);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			GL11.glTranslatef(0, mu*p, 0);
		}
		for(float mu=1;mu<11;mu++){
			GL11.glTranslatef(0, -mu*p+p/2, 0);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(-tile.rotation-mu*6, 0, 1, 0);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			renderTurbine1();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(tile.rotation+mu*6, 0, 1, 0);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			GL11.glTranslatef(0, mu*p-p/2, 0);
		}
		
	}
	
	public void renderBlock3(TileEntity tileentity, double x, double y, double z, float f){
		
	}
	
	public void renderArmour1(){
		WorldRenderer tess=TessHelper.getWR();
		this.bindTexture(FirePipeConection);
			ShadedQuad.addVertexWithUVWRender(0,  p*16, 0,  tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(0,  p*14, 0,  tW*48, tH*4);
			ShadedQuad.addVertexWithUVWRender(0,  p*14, 1,  tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(0,  p*16, 1,  tW*17, tH*1);
			
			ShadedQuad.addVertexWithUVWRender(0,  p*2, 0,  tW*48, tH*1);
			ShadedQuad.addVertexWithUVWRender(0,  p*0, 0,  tW*48, tH*4);
			ShadedQuad.addVertexWithUVWRender(0,  p*0, 1,  tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(0,  p*2, 1,  tW*17, tH*1);
			
			ShadedQuad.addVertexWithUVWRender(0,  p*14, p*2,  tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(0,  p*14, p*0,  tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(0,  p*2,  p*0,  tW*48, tH*4);
			ShadedQuad.addVertexWithUVWRender(0,  p*2,  p*2,  tW*48, tH*1);
			
			ShadedQuad.addVertexWithUVWRender(0,  p*14, p*16,  tW*17, tH*1);
			ShadedQuad.addVertexWithUVWRender(0,  p*14, p*14,  tW*17, tH*4);
			ShadedQuad.addVertexWithUVWRender(0,  p*2,  p*14,  tW*48, tH*4);
			ShadedQuad.addVertexWithUVWRender(0,  p*2,  p*16,  tW*48, tH*1);
			
			ShadedQuad.addVertexWithUVWRender(p*0, p*14, p*2, tWC*24.5, tHC*16);
			ShadedQuad.addVertexWithUVWRender(p*0, p*2,  p*2, tWC*24.5, tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*2, p*2,  p*2, tWC*0,    tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*2, p*14, p*2, tWC*0,    tHC*16);

			ShadedQuad.addVertexWithUVWRender(p*2, p*14, p*14, tWC*0,    tHC*16);
			ShadedQuad.addVertexWithUVWRender(p*2, p*2,  p*14, tWC*0,    tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*0, p*2,  p*14, tWC*24.5, tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*0, p*14, p*14, tWC*24.5, tHC*16);
			
			ShadedQuad.addVertexWithUVWRender(p*2, p*2, p*14, tWC*0,     tHC*16);
			ShadedQuad.addVertexWithUVWRender(p*2, p*2, p*2, tWC*0,     tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*0, p*2, p*2, tWC*24.5,  tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*0, p*2, p*14, tWC*24.5,  tHC*16);
			
			ShadedQuad.addVertexWithUVWRender(p*0, p*14, p*14, tWC*24.5,  tHC*16);
			ShadedQuad.addVertexWithUVWRender(p*0, p*14, p*2, tWC*24.5,  tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*2, p*14, p*2, tWC*0,     tHC*0);
			ShadedQuad.addVertexWithUVWRender(p*2, p*14, p*14, tWC*0,     tHC*16);
		
	}
	public void renderStand1(){
		WorldRenderer tess=TessHelper.getWR();
		this.bindTexture(FirePipeConection);
		for(int a=0;a<2;a++){
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			if(a==0)GL11.glRotatef(180, 0, 0, 1);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				ShadedQuad.addVertexWithUVWRender(p*7.5,  p*15, p*2,   tW*48, tH*1);
				ShadedQuad.addVertexWithUVWRender(p*7.5,  p*14, p*2,   tW*48, tH*4);
				ShadedQuad.addVertexWithUVWRender(p*7.5,  p*13, p*7.5, tW*17, tH*4);
				ShadedQuad.addVertexWithUVWRender(p*7.5,  p*14, p*7.5, tW*17, tH*1);
	
				ShadedQuad.addVertexWithUVWRender(p*8.5,  p*14, p*7.5, tW*17, tH*1);
				ShadedQuad.addVertexWithUVWRender(p*8.5,  p*13, p*7.5, tW*17, tH*4);
				ShadedQuad.addVertexWithUVWRender(p*8.5,  p*14, p*2,   tW*48, tH*4);
				ShadedQuad.addVertexWithUVWRender(p*8.5,  p*15, p*2,   tW*48, tH*1);
				
				ShadedQuad.addVertexWithUVWRender(p*8.5,  p*13, p*7.5, tW*17, tH*1);
				ShadedQuad.addVertexWithUVWRender(p*7.5,  p*13, p*7.5, tW*17, tH*4);
				ShadedQuad.addVertexWithUVWRender(p*7.5,  p*14, p*2,   tW*48, tH*4);
				ShadedQuad.addVertexWithUVWRender(p*8.5,  p*14, p*2,   tW*48, tH*1);
	
				ShadedQuad.addVertexWithUVWRender(p*8.5,  p*15, p*2,   tW*48, tH*1);
				ShadedQuad.addVertexWithUVWRender(p*7.5,  p*15, p*2,   tW*48, tH*4);
				ShadedQuad.addVertexWithUVWRender(p*7.5,  p*14, p*7.5, tW*17, tH*4);
				ShadedQuad.addVertexWithUVWRender(p*8.5,  p*14, p*7.5, tW*17, tH*1);
				
				ShadedQuad.addVertexWithUVWRender(p*8.5,  p*14, p*7.5, tW*48, tH*1);
				ShadedQuad.addVertexWithUVWRender(p*8,    p*14, p*7.5, tW*48, tH*4);
				ShadedQuad.addVertexWithUVWRender(p*8,    p*14, p*8,   tW*17, tH*4);
				ShadedQuad.addVertexWithUVWRender(p*8.5,  p*14, p*8,   tW*17, tH*1);
	
				ShadedQuad.addVertexWithUVWRender(p*8.5,  p*13, p*8,   tW*17, tH*1);
				ShadedQuad.addVertexWithUVWRender(p*8,    p*13, p*8,   tW*17, tH*4);
				ShadedQuad.addVertexWithUVWRender(p*8,    p*13, p*7.5, tW*48, tH*4);
				ShadedQuad.addVertexWithUVWRender(p*8.5,  p*13, p*7.5, tW*48, tH*1);
				
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			if(a==0)GL11.glRotatef(-180, 0, 0, 1);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		}
	}
	public void renderTurbine1(){
		WorldRenderer tess=TessHelper.getWR();
		this.bindTexture(FirePipeConection);
		
		
		
		for(int rot=0;rot<360;rot+=45){
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(rot, 0, 1, 0);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			
			for(int m=0;m<6;m++){

				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				GL11.glRotatef(m*2, 0, 0, 1);
				GL11.glRotatef(m, 1, 0, 0);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				
					ShadedQuad.addVertexWithUVWRender(p*7.25,  p*13.5, p*(6-m), tWC*48, tHC*1);
					ShadedQuad.addVertexWithUVWRender(p*7.25,  p*13,   p*(6-m), tWC*48, tHC*4);
					ShadedQuad.addVertexWithUVWRender(p*7.25,  p*13,   p*(7-m), tWC*17, tHC*4);
					ShadedQuad.addVertexWithUVWRender(p*7.25,  p*13.5, p*(7-m), tWC*17, tHC*1);
		
					ShadedQuad.addVertexWithUVWRender(p*8.75,  p*13.5, p*(7-m), tWC*17, tHC*1);
					ShadedQuad.addVertexWithUVWRender(p*8.75,  p*13,   p*(7-m), tWC*17, tHC*4);
					ShadedQuad.addVertexWithUVWRender(p*8.75,  p*13,   p*(6-m), tWC*48, tHC*4);
					ShadedQuad.addVertexWithUVWRender(p*8.75,  p*13.5, p*(6-m), tWC*48, tHC*1);
					
					ShadedQuad.addVertexWithUVWRender(p*8.75,  p*13,   p*(7-m), tWC*17, tHC*1);
					ShadedQuad.addVertexWithUVWRender(p*7.25,  p*13,   p*(7-m), tWC*17, tHC*4);
					ShadedQuad.addVertexWithUVWRender(p*7.25,  p*13,   p*(6-m), tWC*48, tHC*4);
					ShadedQuad.addVertexWithUVWRender(p*8.75,  p*13,   p*(6-m), tWC*48, tHC*1);
		
					ShadedQuad.addVertexWithUVWRender(p*8.75,  p*13.5, p*(6-m), tWC*48, tHC*1);
					ShadedQuad.addVertexWithUVWRender(p*7.25,  p*13.5, p*(6-m), tWC*48, tHC*4);
					ShadedQuad.addVertexWithUVWRender(p*7.25,  p*13.5, p*(7-m), tWC*17, tHC*4);
					ShadedQuad.addVertexWithUVWRender(p*8.75,  p*13.5, p*(7-m), tWC*17, tHC*1);
					
					ShadedQuad.addVertexWithUVWRender(p*8.75,  p*13,   p*(6-m), tWC*48, tHC*1);
					ShadedQuad.addVertexWithUVWRender(p*7.25,  p*13,   p*(6-m), tWC*48, tHC*4);
					ShadedQuad.addVertexWithUVWRender(p*7.25,  p*13.5, p*(6-m), tWC*17, tHC*4);
					ShadedQuad.addVertexWithUVWRender(p*8.75,  p*13.5, p*(6-m), tWC*17, tHC*1);
					

		    	GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				GL11.glRotatef(-m, 1, 0, 0);
		    	GL11.glRotatef(-m*2, 0, 0, 1);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			}
			
	    	GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	    	GL11.glRotatef(-rot, 0, 1, 0);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		}
		
	}
	
	
}