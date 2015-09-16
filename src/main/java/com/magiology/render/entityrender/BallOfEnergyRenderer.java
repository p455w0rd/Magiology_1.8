package com.magiology.render.entityrender;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.magiology.mcobjects.entitys.EntityBallOfEnergy;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.Util.U;

public class BallOfEnergyRenderer extends Render{

	ModelBase model;
	public BallOfEnergyRenderer(ModelBase model, float scale){
		super(TessUtil.getRM());
		this.model=model;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return null;
	}

	@Override
	public void doRender(Entity en, double var1, double var2, double var3, float var4,float partialTicks){
		EntityBallOfEnergy entity=(EntityBallOfEnergy)en;
		EntityPlayer player=U.getMC().thePlayer;
		int time360=entity.age%90*4,offs=25,many=1;
		GL11.glPushMatrix();
		GL11.glTranslatef(-(float)(player.lastTickPosX+(player.posX-player.lastTickPosX)*partialTicks),
				-(float)(player.lastTickPosY+(player.posY-player.lastTickPosY)*partialTicks), 
				-(float)(player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*partialTicks));
//		GL11.glTranslated(entity.posX, entity.posY, entity.posZ);
		GL11.glTranslatef((float)(entity.lastTickPosX+(entity.posX-entity.lastTickPosX)*partialTicks),
				(float)(entity.lastTickPosY+(entity.posY-entity.lastTickPosY)*partialTicks), 
				(float)(entity.lastTickPosZ+(entity.posZ-entity.lastTickPosZ)*partialTicks));
		float smoothRotation=(time360-4)+4*partialTicks,size=-(float)(0.1-entity.time/100F);
		AxisAlignedBB cube=new AxisAlignedBB(-size,-size,-size,size,size,size);
		GL11U.SetUpOpaqueRendering(1);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4d(1, 0.1, 0.1, 0.2);
		GL11U.rotateXYZ(smoothRotation, 0, 0);
		TessUtil.drawCube(cube);
		GL11U.rotateXYZ(0, -smoothRotation+offs*many++, 0);
		TessUtil.drawCube(cube);
		GL11U.rotateXYZ(0, 0, -smoothRotation+offs*many++);
		TessUtil.drawCube(cube);
		GL11U.rotateXYZ(0, smoothRotation+offs*many++, 0);
		TessUtil.drawCube(cube);
		GL11U.rotateXYZ(smoothRotation+offs*many++, 0, 0);
		TessUtil.drawCube(cube);
		GL11U.rotateXYZ(0, 0, smoothRotation+offs*many++);
		TessUtil.drawCube(cube);
		GL11U.rotateXYZ(-smoothRotation+offs*many++, 0, smoothRotation+offs*many++);
		TessUtil.drawCube(cube);
		GL11U.rotateXYZ(0, -smoothRotation+offs*many++, -smoothRotation+offs*many++);
		TessUtil.drawCube(cube);
		GL11U.rotateXYZ(smoothRotation+offs*many++, -smoothRotation+offs*many++, 0);
		TessUtil.drawCube(cube);
		GL11U.EndOpaqueRendering();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glPopMatrix();
	
	
	}
}
