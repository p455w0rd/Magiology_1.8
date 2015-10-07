package com.magiology.client.render.tilerender;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import org.lwjgl.opengl.GL11;

import com.magiology.client.render.Textures;
import com.magiology.mcobjects.tileentityes.TileEntityBedrockBreaker;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

public class RenderBedrockBreaker extends TileEntitySpecialRendererM {
//	ResourceLocation BBB = new ResourceLocation(Magiology.MODID+":"+"/textures/models/BedrockBreakerBase.png");
//	ResourceLocation BBLS = new ResourceLocation(Magiology.MODID+":"+"/textures/models/BedrockBreakerLegSide.png");
//	ResourceLocation BBLF = new ResourceLocation(Magiology.MODID+":"+"/textures/models/BedrockBreakerLegFront.png");
//	ResourceLocation BBLB = new ResourceLocation(Magiology.MODID+":"+"/textures/models/BedrockBreakerLegBack.png");
//	ResourceLocation BBLTB = new ResourceLocation(Magiology.MODID+":"+"/textures/models/BedrockBreakerLegTopBottom.png");
//	ResourceLocation BBL = new ResourceLocation(Magiology.MODID+":"+"/textures/models/BedrockBreakerLegLaser.png");
	
	private final float p= 1F/16F;
	
	private final float tW=1F/80F;
	private final float tH=1F/240F;
	
	private final float tW2=1F/32F;
	private final float tH2=1F/48F;
	
	private final float tW3=1F/16F;
	private final float tH3=1F/71F;
	
	private final float tW4=1F/16F;
	private final float tH4=1F/66F;
	
	private final float tW5=1F/16F;
	private final float tH5=1F/32F;
	int state=0;
	int StartStop=0;
	NormalizedVertixBuffer buf=Render.NVB();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		TileEntityBedrockBreaker entity= (TileEntityBedrockBreaker) tileentity;
		GL11.glPushMatrix();
		GL11.glTranslated(x,y,z);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		drawBase();
		drawLaser(entity.positionForLaser);
		if(entity.IRFA==true)drawLaserBeam();
		drawLeg(EnumFacing.EAST, entity.animation);
		drawLeg(EnumFacing.NORTH, entity.animation);
		drawLeg(EnumFacing.SOUTH, entity.animation);
		drawLeg(EnumFacing.WEST, entity.animation);
		GL11.glPopMatrix();
	}
	
	
	public void drawBase(){
		this.bindTexture(Textures.BedrockBreakerBase);
		
		{
		buf.addVertexWithUV(p*3, p*16, p*13, tW*0, tH*0);
		buf.addVertexWithUV(p*3, p*6,  p*13, tW*0, tH*80);
		buf.addVertexWithUV(p*13,p*6,  p*13, tW*80,tH*80);
		buf.addVertexWithUV(p*13,p*16, p*13, tW*80,tH*0);

		buf.addVertexWithUV(p*13,p*16, p*3,  tW*0, tH*0);
		buf.addVertexWithUV(p*13,p*6,  p*3,  tW*0, tH*80);
		buf.addVertexWithUV(p*3, p*6,  p*3,  tW*80,tH*80);
		buf.addVertexWithUV(p*3, p*16, p*3,  tW*80,tH*0);

		buf.addVertexWithUV(p*13,p*16, p*13, tW*0, tH*0);
		buf.addVertexWithUV(p*13,p*6,  p*13, tW*0, tH*80);
		buf.addVertexWithUV(p*13, p*6, p*3,  tW*80,tH*80);
		buf.addVertexWithUV(p*13, p*16,p*3,  tW*80,tH*0);

		buf.addVertexWithUV(p*3, p*16,p*3,   tW*0,  tH*0);
		buf.addVertexWithUV(p*3, p*6, p*3,   tW*0,  tH*80);
		buf.addVertexWithUV(p*3,p*6,  p*13,  tW*80, tH*80);
		buf.addVertexWithUV(p*3,p*16, p*13,  tW*80, tH*0);

		buf.addVertexWithUV(p*3, p*16,p*3,   tW*80, tH*80);
		buf.addVertexWithUV(p*3, p*16, p*13, tW*80, tH*160);
		buf.addVertexWithUV(p*13,p*16, p*13, tW*0,  tH*160);
		buf.addVertexWithUV(p*13,p*16,p*3,   tW*0,  tH*80);

		buf.addVertexWithUV(p*13,p*6,p*3,   tW*80,  tH*160);
		buf.addVertexWithUV(p*13,p*6, p*13, tW*80,  tH*240);
		buf.addVertexWithUV(p*3, p*6, p*13, tW*0,   tH*240);
		buf.addVertexWithUV(p*3, p*6,p*3,   tW*0,   tH*160);
		buf.draw();

		}
		
	}

	public void drawLeg(EnumFacing dir,double animation){
		//sides-----------------------------------------------------
		//----------------------------------------------------------
		
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		if(dir.equals(EnumFacing.WEST)){}
		else if (dir.equals(EnumFacing.SOUTH))GL11.glRotatef(90, 0, 1, 0);
		else if (dir.equals(EnumFacing.EAST))GL11.glRotatef(-180, 0, 1, 0);
		else if (dir.equals(EnumFacing.NORTH))GL11.glRotatef(-90, 0, 1, 0);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		GL11.glTranslated(p*3.8, p*6.5, p*3.8);
		GL11.glRotated(-45, 0, 1, 0);
		GL11.glRotated(animation, 0, 0, 1);
		{
			{
				this.bindTexture(Textures.BedrockBreakerLegSide);
				buf.addVertexWithUV(p*-2, p*0,  p*1,   tW2*25, tH2*0);
				buf.addVertexWithUV(p*-2, p*-4, p*1,   tW2*7, tH2*15);
				buf.addVertexWithUV(p*0,  p*-4, p*1,   tW2*14,tH2*23);
				buf.addVertexWithUV(p*0,  p*0,  p*1,   tW2*32,tH2*9);

				buf.addVertexWithUV(p*0,  p*0,  -p*1,  tW2*25,tH2*0);
				buf.addVertexWithUV(p*0,  p*-4, -p*1,  tW2*7,tH2*15);
				buf.addVertexWithUV(p*-2, p*-4, -p*1,  tW2*14, tH2*23);
				buf.addVertexWithUV(p*-2, p*0,  -p*1,  tW2*32, tH2*9);
				
				
				
				buf.addVertexWithUV(p*-2, p*-4,   p*1,   tW2*7, tH2*15);
				buf.addVertexWithUV(p*-1, p*-6,   p*1,   tW2*2, tH2*26);
				buf.addVertexWithUV(p*0.7,p*-5.3, p*1,   tW2*11,tH2*31);
				buf.addVertexWithUV(p*0,  p*-4,   p*1,   tW2*14,tH2*23);
	 
				buf.addVertexWithUV(p*0,   p*-4,  -p*1,  tW2*7, tH2*15);
				buf.addVertexWithUV(p*0.7, p*-5.3,-p*1,  tW2*2, tH2*26);
				buf.addVertexWithUV(p*-1,  p*-6,  -p*1,  tW2*11,tH2*31);
				buf.addVertexWithUV(p*-2,  p*-4,  -p*1,  tW2*14,tH2*23);
				
				
				
				buf.addVertexWithUV(p*-1,  p*-6,   p*1,  tW2*2, tH2*26);
				buf.addVertexWithUV(p*1.6, p*-8.5, p*1,  tW2*1, tH2*47);
				buf.addVertexWithUV(p*2.8, p*-7.2, p*1,  tW2*10,tH2*48);
				buf.addVertexWithUV(p*0.7, p*-5.3, p*1,  tW2*11,tH2*31);

				buf.addVertexWithUV(p*0.7, p*-5.3, -p*1, tW2*2, tH2*26);
				buf.addVertexWithUV(p*2.8, p*-7.2, -p*1, tW2*1, tH2*47);
				buf.addVertexWithUV(p*1.6, p*-8.5, -p*1, tW2*10,tH2*48);
				buf.addVertexWithUV(p*-1,  p*-6,   -p*1, tW2*11,tH2*31);
				buf.draw();
			}
		}
		
		//----------------------------------------------------------
		//----------------------------------------------------------
		

		//front-----------------------------------------------------
		//----------------------------------------------------------
		
		
		{
			{
				this.bindTexture(Textures.BedrockBreakerLegFront);
				buf.addVertexWithUV(-p*2, p*0,  -p*1,  tW3*0,  tH3*0);
				buf.addVertexWithUV(-p*2, p*-4, -p*1,  tW3*0,  tH3*32);
				buf.addVertexWithUV(-p*2, p*-4,  p*1,  tW3*16, tH3*32);
				buf.addVertexWithUV(-p*2, p*0,   p*1,  tW3*16, tH3*0);
				
				
				buf.addVertexWithUV(-p*2, p*-4,  -p*1,   tW3*0,  tH3*32);
				buf.addVertexWithUV(-p*1, p*-6,  -p*1,   tW3*0,  tH3*50);
				buf.addVertexWithUV(-p*1, p*-6,   p*1,   tW3*16, tH3*50);
				buf.addVertexWithUV(-p*2, p*-4,   p*1,   tW3*16, tH3*32);
				
				
				buf.addVertexWithUV(-p*1,  p*-6,  -p*1,  tW3*0,  tH3*50);
				buf.addVertexWithUV(p*1.6, p*-8.5,-p*1,  tW3*0,  tH3*71);
				buf.addVertexWithUV(p*1.6, p*-8.5, p*1,  tW3*16, tH3*71);
				buf.addVertexWithUV(-p*1,  p*-6,   p*1,  tW3*16, tH3*50);
				buf.draw();
			}
		}
		
		//----------------------------------------------------------
		//----------------------------------------------------------
		
		//back------------------------------------------------------
		//----------------------------------------------------------
		
		
		{
			{
				this.bindTexture(Textures.BedrockBreakerLegBack);
				buf.addVertexWithUV(0, p*0,   p*1,     tW4*0, tH4*0);
				buf.addVertexWithUV(0, p*-4,  p*1,     tW4*0, tH4*32);
				buf.addVertexWithUV(0, p*-4, -p*1,     tW4*16,tH4*32);
				buf.addVertexWithUV(0, p*0,  -p*1,     tW4*16,tH4*0);
				
				
				buf.addVertexWithUV(p*0,   p*-4,   p*1,  tW4*0, tH4*32);
				buf.addVertexWithUV(p*0.7, p*-5.3, p*1,  tW4*0, tH4*44);
				buf.addVertexWithUV(p*0.7, p*-5.3,-p*1,  tW4*16,tH4*44);
				buf.addVertexWithUV(p*0,   p*-4,  -p*1,  tW4*16,tH4*32);
				
				
				buf.addVertexWithUV(p*0.7,  p*-5.3, p*1, tW4*0, tH4*44);
				buf.addVertexWithUV(p*2.8,  p*-7.2, p*1, tW4*0, tH4*66);
				buf.addVertexWithUV(p*2.8,  p*-7.2,-p*1, tW4*16,tH4*66);
				buf.addVertexWithUV(p*0.7,  p*-5.3,-p*1, tW4*16,tH4*44);
				buf.draw();
			}
		}
		
		//----------------------------------------------------------
		//----------------------------------------------------------
		
		
		//top&bottom------------------------------------------------
		//----------------------------------------------------------
		
		{
			{
				this.bindTexture(Textures.BedrockBreakerLegTopBottom);
				buf.addVertexWithUV(p*0,  0,  p*1,     tW5*0,  tH5*0);
				buf.addVertexWithUV(p*0,  0, -p*1,     tW5*0,  tH5*16);
				buf.addVertexWithUV(p*-2, 0, -p*1,     tW5*16, tH5*16);
				buf.addVertexWithUV(p*-2, 0,  p*1,     tW5*16, tH5*0);
				
				
				buf.addVertexWithUV(p*2.8,  p*-7.2,-p*1, tW5*0,  tH5*16);
				buf.addVertexWithUV(p*2.8,  p*-7.2, p*1, tW5*0,  tH5*32);
				buf.addVertexWithUV(p*1.6, p*-8.5,  p*1, tW5*16, tH5*32);
				buf.addVertexWithUV(p*1.6, p*-8.5, -p*1, tW5*16, tH5*16);
				buf.draw();
			}
		}
		
		GL11.glRotated(animation, 0, 0, -1);
		GL11.glRotated(45, 0, 1, 0);
		GL11.glTranslated(-p*3.8, -p*6.5, -p*3.8);
		
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		if(dir.equals(EnumFacing.WEST))GL11.glRotatef(0, 0, 1, 0);
		else if (dir.equals(EnumFacing.SOUTH))GL11.glRotatef(-90, 0, 1, 0);
		else if (dir.equals(EnumFacing.EAST))GL11.glRotatef(180, 0, 1, 0);
		else if (dir.equals(EnumFacing.NORTH))GL11.glRotatef(90, 0, 1, 0);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		//----------------------------------------------------------
		//----------------------------------------------------------
	}
	
	public void drawLaser(double positionForLaser)
	{
		
		GL11.glTranslated(p*8, p*6+positionForLaser, p*8);
		GL11.glRotated(positionForLaser*1000, 0,1,0);
		{
				bindTexture(Textures.BedrockBreakerLegLaser);
				buf.addVertexWithUV(p*1,  p*-1.5,  p*1,  tW5*0,  tH5*12);
				buf.addVertexWithUV(p*1,  p*-1.5, -p*1,  tW5*16, tH5*12);
				buf.addVertexWithUV(p*1,  p*0,    -p*1,  tW5*16, tH5*0);
				buf.addVertexWithUV(p*1,  p*0,     p*1,  tW5*0,  tH5*0);

				buf.addVertexWithUV(p*-1,  p*0,    p*1,  tW5*0, tH5*12);
				buf.addVertexWithUV(p*-1,  p*0,   -p*1,  tW5*16, tH5*12);
				buf.addVertexWithUV(p*-1,  p*-1.5,-p*1,  tW5*16,  tH5*0);
				buf.addVertexWithUV(p*-1,  p*-1.5, p*1,  tW5*0,  tH5*0);
				
				buf.addVertexWithUV(p*1,  p*-1.5, -p*1,  tW5*0,  tH5*12);
				buf.addVertexWithUV(p*-1, p*-1.5, -p*1,  tW5*16,  tH5*12);
				buf.addVertexWithUV(p*-1, p*0,    -p*1,  tW5*16, tH5*0);
				buf.addVertexWithUV(p*1,  p*0,    -p*1,  tW5*0, tH5*0);

				buf.addVertexWithUV(p*1,  p*0,     p*1,  tW5*0, tH5*12);
				buf.addVertexWithUV(p*-1, p*0,     p*1,  tW5*16, tH5*12);
				buf.addVertexWithUV(p*-1, p*-1.5,  p*1,  tW5*16,  tH5*0);
				buf.addVertexWithUV(p*1,  p*-1.5,  p*1,  tW5*0,  tH5*0);
				
				buf.addVertexWithUV(p*1,  p*-1.5,  p*1,  tW5*0, tH5*12);
				buf.addVertexWithUV(p*-1, p*-1.5,  p*1,  tW5*16, tH5*12);
				buf.addVertexWithUV(p*-1, p*-1.5, -p*1,  tW5*16,  tH5*0);
				buf.addVertexWithUV(p*1,  p*-1.5, -p*1,  tW5*0,  tH5*0);
				
				buf.addVertexWithUV(p*0.5,  p*-4,    p*0.5,  tW5*0,  tH5*32);
				buf.addVertexWithUV(p*0.5,  p*-4,   -p*0.5,  tW5*8,  tH5*32);
				buf.addVertexWithUV(p*0.5,  p*-1.5, -p*0.5,  tW5*8,  tH5*12);
				buf.addVertexWithUV(p*0.5,  p*-1.5,  p*0.5,  tW5*0,  tH5*12);

				buf.addVertexWithUV(p*-0.5, p*-1.5,  p*0.5,  tW5*0,  tH5*32);
				buf.addVertexWithUV(p*-0.5, p*-1.5, -p*0.5,  tW5*8,  tH5*32);
				buf.addVertexWithUV(p*-0.5, p*-4,   -p*0.5,  tW5*8,  tH5*12);
				buf.addVertexWithUV(p*-0.5, p*-4,    p*0.5,  tW5*0,  tH5*12);
				
				buf.addVertexWithUV(p*0.5,  p*-4,   -p*0.5,  tW5*0,  tH5*32);
				buf.addVertexWithUV(p*-0.5, p*-4,   -p*0.5,  tW5*8,  tH5*32);
				buf.addVertexWithUV(p*-0.5, p*-1.5, -p*0.5,  tW5*8,  tH5*12);
				buf.addVertexWithUV(p*0.5,  p*-1.5, -p*0.5,  tW5*0,  tH5*12);

				buf.addVertexWithUV(p*0.5,  p*-1.5, p*0.5,  tW5*0,  tH5*32);
				buf.addVertexWithUV(p*-0.5, p*-1.5, p*0.5,  tW5*8,  tH5*32);
				buf.addVertexWithUV(p*-0.5, p*-4,   p*0.5,  tW5*8,  tH5*12);
				buf.addVertexWithUV(p*0.5,  p*-4,   p*0.5,  tW5*0,  tH5*12);
				
				buf.addVertexWithUV(p*0.5,  p*-4,   p*0.5,  tW5*0,  tH5*32);
				buf.addVertexWithUV(p*-0.5, p*-4,   p*0.5,  tW5*8,  tH5*32);
				buf.addVertexWithUV(p*-0.5, p*-4,  -p*0.5,  tW5*8,  tH5*12);
				buf.addVertexWithUV(p*0.5,  p*-4, - p*0.5,  tW5*0,  tH5*12);
				buf.draw();
		}
		
		GL11.glRotated(-positionForLaser*1000, 0,1,0);
		GL11.glTranslated(-p*8, -p*6-positionForLaser, -p*8);
	}
	
	public void drawLaserBeam(){
		
		GL11.glTranslated(p*8, p*6, p*8);
		GL11U.SetUpOpaqueRendering(1);
			this.bindTexture(Textures.BedrockBreakerLegLaser);
			buf.addVertexWithUV(p*0.25,  p*-6,   p*0.25,   tW5*12, tH5*12);
			buf.addVertexWithUV(p*0.25,  p*-6,  -p*0.25,   tW5*8,  tH5*12);
			buf.addVertexWithUV(p*0.25,  p*-4,  -p*0.25,   tW5*8,  tH5*28);
			buf.addVertexWithUV(p*0.25,  p*-4,   p*0.25,   tW5*12, tH5*28);
			
			buf.addVertexWithUV(p*-0.25,  p*-4,  p*0.25,   tW5*12, tH5*12);
			buf.addVertexWithUV(p*-0.25,  p*-4, -p*0.25,   tW5*8,  tH5*12);
			buf.addVertexWithUV(p*-0.25,  p*-6, -p*0.25,   tW5*8,  tH5*28);
			buf.addVertexWithUV(p*-0.25,  p*-6,  p*0.25,   tW5*12, tH5*28);
			
			buf.addVertexWithUV(p*0.25,  p*-6,  -p*0.25,   tW5*12, tH5*12);
			buf.addVertexWithUV(p*-0.25, p*-6,  -p*0.25,   tW5*8,  tH5*12);
			buf.addVertexWithUV(p*-0.25, p*-4,  -p*0.25,   tW5*8,  tH5*28);
			buf.addVertexWithUV(p*0.25,  p*-4,  -p*0.25,   tW5*12, tH5*28);
			
			buf.addVertexWithUV(p*0.25,  p*-4,  p*0.25,   tW5*12, tH5*12);
			buf.addVertexWithUV(p*-0.25, p*-4,  p*0.25,   tW5*8,  tH5*12);
			buf.addVertexWithUV(p*-0.25, p*-6,  p*0.25,   tW5*8,  tH5*28);
			buf.addVertexWithUV(p*0.25,  p*-6,  p*0.25,   tW5*12, tH5*28);
			buf.draw();
			GL11U.EndOpaqueRendering();
			GL11.glColor4f(1, 1, 1, 1);
		GL11.glTranslated(-p*8, -p*6, -p*8);
	
	}
}