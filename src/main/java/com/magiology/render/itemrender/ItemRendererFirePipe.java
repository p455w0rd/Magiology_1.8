package com.magiology.render.itemrender;

import com.magiology.objhelper.helpers.renderers.ShadedQuad;
import com.magiology.render.Textures;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

public class ItemRendererFirePipe implements IItemRenderer {
	
	private final float p= 1F/16F;
	private final float tW=1F/112F;
	private final float tH=1F/16F;
	private final float tWC=1F/4F;
	private final float tHC=1F/40F;
	private final float tWFSL=1F/62F;
	private final float tHFSL=1F/38F;
	private final float tWS=1F/16F;
	private final float tHS=1F/32F;

	public ItemRendererFirePipe(){}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper) {
		return helper!=ItemRendererHelper.ENTITY_BOBBING&&helper!=ItemRendererHelper.ENTITY_ROTATION;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack is, Object... data){
		GL11.glPushMatrix();
		float x=0;
		float y=0;
		float z=0;
		float xr=0;
		float yr=0;
		float zr=0;
		float scale=1;
		
		if(ItemRenderType.ENTITY == type){
			xr=90;
			z=0.75F;
			y=-1.05F;
			x=-0.75F;
			scale=1.5F;
		}
		else if(ItemRenderType.EQUIPPED_FIRST_PERSON == type){
			x=-0.1F;
			z=-0.1F;
			xr=-5;
			zr=-5;
			y=0.5F;
		}
		else if(ItemRenderType.EQUIPPED == type){
			scale=1.2F;
			x=1F;
			y=-0.2F;
			z=1F;
			xr=60;
			yr=30;
			zr=-60;
		}
		else if(ItemRenderType.INVENTORY == type){
			y=-0.1F;
			scale=1.4F;
		}
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
	    GL11.glTranslatef(x, y, z);
	    GL11.glRotatef(-xr, 1, 0, 0);GL11.glRotatef(-yr, 0, 1, 0);GL11.glRotatef(-zr, 0, 0, 1);
	    GL11.glScalef(scale, scale, scale);
	    
		drawConector(ForgeDirection.DOWN);
		drawConector(ForgeDirection.UP);
		drawCore();
		
		GL11.glPopMatrix();
	}
	
	public void drawCore(){
		Tessellator tessellator = Tessellator.instance;
		Minecraft.getMinecraft().renderEngine.bindTexture(Textures.firePipeCore);
		ShadedQuad.addVertexWithUVWRender(p*6, p*10, p*6, tWC*4, tHC*4);
		ShadedQuad.addVertexWithUVWRender(p*6, p*6,  p*6, tWC*4, tHC*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*6,  p*10,tWC*0, tHC*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*10, p*10,tWC*0, tHC*4);
		
		ShadedQuad.addVertexWithUVWRender(p*10, p*10, p*10, tWC*4, tHC*4);
		ShadedQuad.addVertexWithUVWRender(p*10, p*6,  p*10, tWC*4, tHC*0);
		ShadedQuad.addVertexWithUVWRender(p*10, p*6,  p*6, tWC*0, tHC*0);
		ShadedQuad.addVertexWithUVWRender(p*10, p*10, p*6, tWC*0, tHC*4);
		
		ShadedQuad.addVertexWithUVWRender(p*6, p*10, p*10, tWC*0, tHC*4);
		ShadedQuad.addVertexWithUVWRender(p*6, p*6 , p*10, tWC*0, tHC*0);
		ShadedQuad.addVertexWithUVWRender(p*10, p*6, p*10, tWC*4, tHC*0);
		ShadedQuad.addVertexWithUVWRender(p*10, p*10, p*10, tWC*4, tHC*4);
		
		ShadedQuad.addVertexWithUVWRender(p*10, p*10, p*6, tWC*4, tHC*4);
		ShadedQuad.addVertexWithUVWRender(p*10, p*6, p*6, tWC*4, tHC*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*6 , p*6, tWC*0, tHC*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*10, p*6, tWC*0, tHC*4);
		
		ShadedQuad.addVertexWithUVWRender(p*10, p*10, p*10, tWC*4, tHC*4);
		ShadedQuad.addVertexWithUVWRender(p*10, p*10, p*6, tWC*4, tHC*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*10, p*6, tWC*0, tHC*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*10, p*10, tWC*0, tHC*4);
			
		ShadedQuad.addVertexWithUVWRender(p*6, p*6, p*10, tWC*0, tHC*4);
		ShadedQuad.addVertexWithUVWRender(p*6, p*6, p*6, tWC*0, tHC*0);
		ShadedQuad.addVertexWithUVWRender(p*10, p*6, p*6, tWC*4, tHC*0);
		ShadedQuad.addVertexWithUVWRender(p*10, p*6, p*10, tWC*4, tHC*4);
	}
	
	public void drawConector(ForgeDirection dir){
		GL11.glPushMatrix();
		Tessellator tessellator = Tessellator.instance;
		Minecraft.getMinecraft().renderEngine.bindTexture(Textures.FirePipeConection);
		
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		if(dir.equals(ForgeDirection.WEST)){}
		else if (dir.equals(ForgeDirection.UP)){GL11.glRotatef(-90, 0, 0, 1);}
		else if (dir.equals(ForgeDirection.DOWN)){GL11.glRotatef(90, 0, 0, 1);}
		else if (dir.equals(ForgeDirection.SOUTH)){GL11.glRotatef(90, 0, 1, 0);}
		else if (dir.equals(ForgeDirection.EAST)){GL11.glRotatef(-180, 0, 1, 0);}
		else if (dir.equals(ForgeDirection.NORTH)){GL11.glRotatef(-90, 0, 1, 0);}
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		ShadedQuad.addVertexWithUVWRender(p*0, p*9.5,  p*9.5,tW*24.5, tH*16);
		ShadedQuad.addVertexWithUVWRender(p*0, p*6.5,  p*9.5,tW*24.5, tH*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*6.5,  p*9.5,tW*0,    tH*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*9.5,  p*9.5,tW*0,    tH*16);

		ShadedQuad.addVertexWithUVWRender(p*6, p*9, p*9  ,tW*0,   tH*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*7, p*9 ,tW*0,   tH*16);
		ShadedQuad.addVertexWithUVWRender(p*0, p*7, p*9, tW*24.5,tH*16);
		ShadedQuad.addVertexWithUVWRender(p*0, p*9, p*9,  tW*24.5,tH*0);
		
		
		ShadedQuad.addVertexWithUVWRender(p*6, p*9.5, p*6.5  ,tW*0,   tH*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*6.5, p*6.5 ,tW*0,   tH*16);
		ShadedQuad.addVertexWithUVWRender(p*0, p*6.5, p*6.5, tW*24.5,tH*16);
		ShadedQuad.addVertexWithUVWRender(p*0, p*9.5, p*6.5,  tW*24.5,tH*0);
		
		ShadedQuad.addVertexWithUVWRender(p*0, p*9,  p*7,tW*24.5, tH*16);
		ShadedQuad.addVertexWithUVWRender(p*0, p*7,  p*7,tW*24.5, tH*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*7,  p*7,tW*0,    tH*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*9,  p*7,tW*0,    tH*16);
		
		
		ShadedQuad.addVertexWithUVWRender(p*6, p*9.5, p*9.5, tW*0,    tH*16);
		ShadedQuad.addVertexWithUVWRender(p*6, p*9.5, p*6.5, tW*0,    tH*0);
		ShadedQuad.addVertexWithUVWRender(p*0, p*9.5, p*6.5, tW*24.5, tH*0);
		ShadedQuad.addVertexWithUVWRender(p*0, p*9.5, p*9.5, tW*24.5, tH*16);

		ShadedQuad.addVertexWithUVWRender(p*0, p*9, p*9, tW*24.5, tH*16);
		ShadedQuad.addVertexWithUVWRender(p*0, p*9, p*7, tW*24.5, tH*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*9, p*7, tW*0,    tH*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*9, p*9, tW*0,    tH*16);
		

		ShadedQuad.addVertexWithUVWRender(p*0, p*6.5, p*9.5, tW*24.5, tH*16);
		ShadedQuad.addVertexWithUVWRender(p*0, p*6.5, p*6.5, tW*24.5, tH*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*6.5, p*6.5, tW*0,    tH*0);
		ShadedQuad.addVertexWithUVWRender(p*6, p*6.5, p*9.5, tW*0,    tH*16);

		ShadedQuad.addVertexWithUVWRender(p*6, p*7, p*9, tW*0,    tH*16);
		ShadedQuad.addVertexWithUVWRender(p*6, p*7, p*7, tW*0,    tH*0);
		ShadedQuad.addVertexWithUVWRender(p*0, p*7, p*7, tW*24.5, tH*0);
		ShadedQuad.addVertexWithUVWRender(p*0, p*7, p*9, tW*24.5, tH*16);
		
		
		Minecraft.getMinecraft().renderEngine.bindTexture(Textures.FirePipeConectionEnd);
		
		ShadedQuad.addVertexWithUVWRender(p*0, p*9.5, p*9.5, 1, 1);
		ShadedQuad.addVertexWithUVWRender(p*0, p*9.5, p*6.5, 1, 0);
		ShadedQuad.addVertexWithUVWRender(p*0, p*6.5, p*6.5, 0, 0);
		ShadedQuad.addVertexWithUVWRender(p*0, p*6.5, p*9.5, 0, 1);
		GL11.glPopMatrix();
	}

}




