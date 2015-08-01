package com.magiology.render.entityrender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.magiology.mcobjects.entitys.ModedEntityFallingBlock;
import com.magiology.objhelper.helpers.renderers.TessHelper;

public class RenderModedFallingBlock extends Render{
	
	RenderBlocks renderBlocks=new RenderBlocks();
	
	@Override
	public void doRender(Entity en, double var1, double var2, double var3, float var4, float partialTicks){
		ModedEntityFallingBlock entity=(ModedEntityFallingBlock)en;
		try{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			float playerOffsetX=-(float)(player.lastTickPosX+(player.posX-player.lastTickPosX)*partialTicks),playerOffsetY=-(float)(player.lastTickPosY+(player.posY-player.lastTickPosY)*partialTicks),playerOffsetZ=-(float)(player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*partialTicks);
			float entityOffsetX=(float)(entity.lastTickPosX+(entity.posX-entity.lastTickPosX)*partialTicks),entityOffsetY=(float)(entity.lastTickPosY+(entity.posY-entity.lastTickPosY)*partialTicks),entityOffsetZ=(float)(entity.lastTickPosZ+(entity.posZ-entity.lastTickPosZ)*partialTicks);
			GL11.glPushMatrix();
			GL11.glTranslatef(entityOffsetX, entityOffsetY, entityOffsetZ);
			GL11.glTranslatef(playerOffsetX, playerOffsetY, playerOffsetZ);
			GL11.glDisable(GL11.GL_CULL_FACE);
			TessHelper.drawCircleRes180(0.1, 1, 0.1, 1, 1, 0, 91);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glPopMatrix();
			
		}catch(Exception e){e.printStackTrace();}
        
    }
	
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		// TODO Auto-generated method stub
		return null;
	}
}
