package com.magiology.render.entityrender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.magiology.mcobjects.entitys.EntitySubatomicWorldDeconstructor;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.TessHelper;

public class EntitySubatomicWorldDeconstructorRenderer extends Render{
	private float scale;
	public EntitySubatomicWorldDeconstructorRenderer(float scale){
		super(TessHelper.getRM());
		this.scale=scale;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return null;
	}

	@Override
	public void doRender(Entity en, double var1, double var2, double var3, float var4,float partialTicks){
		try{
			EntitySubatomicWorldDeconstructor entity=(EntitySubatomicWorldDeconstructor)en;
			EntityPlayer player=Minecraft.getMinecraft().thePlayer;
			int time360=entity.age%90*4,offs=25,many=1;
			GL11.glPushMatrix();
			GL11.glTranslatef(-(float)(player.lastTickPosX+(player.posX-player.lastTickPosX)*partialTicks),
					-(float)(player.lastTickPosY+(player.posY-player.lastTickPosY)*partialTicks), 
					-(float)(player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*partialTicks));
//			GL11.glTranslated(entity.posX, entity.posY, entity.posZ);
			GL11.glTranslatef((float)(entity.lastTickPosX+(entity.posX-entity.lastTickPosX)*partialTicks),
					(float)(entity.lastTickPosY+(entity.posY-entity.lastTickPosY)*partialTicks), 
					(float)(entity.lastTickPosZ+(entity.posZ-entity.lastTickPosZ)*partialTicks));
			float smoothRotation=(time360-4)+4*partialTicks,size=0.1F;
			AxisAlignedBB cube=new AxisAlignedBB(-size,-size,-size,size,size,size+1);
//			GL11H.SetUpOpaqueRendering(1);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor4d(0.5, 0.5,1,1);
			
//			if(entity.targetHit){
//				for(int a=0;a<15;a++){
//					double[] a1=Helper.createBallXYZ(10, false);
//					TessHelper.drawLine(0, 0, 0, a1[0], a1[1], a1[2], 1, true, null, 0, 1);
//				}
//			}
			
//			GL11H.scaled(scale);
			GL11H.rotateXYZ(Math.toDegrees(Math.atan2(entity.motionX+entity.motionZ, entity.motionY)), Math.toDegrees(Math.atan2(entity.motionX,entity.motionZ)), 0);
			TessHelper.drawCube(cube);
			
//			GL11H.EndOpaqueRendering();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			
			GL11.glPopMatrix();
		}catch(Exception e){
			
		}
		
	
	
	}
}