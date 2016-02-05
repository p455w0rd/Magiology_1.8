package com.magiology.forgepowered.events.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import com.magiology.util.utilclasses.DataStalker;
import com.magiology.util.utilclasses.UtilM;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class FirstPersonItemRederer{
	
	private static Minecraft mc=UtilM.getMC();
	private static Method getFOVModifier;
	
	
	public static boolean render(RenderHandEvent e){
		try{
			if(getFOVModifier==null){
				getFOVModifier=ReflectionHelper.findMethod(EntityRenderer.class, mc.entityRenderer, new String[]{"getFOVModifier"}, float.class,boolean.class);
				getFOVModifier.setAccessible(true);
			}
//			renderHand(e.partialTicks, e.renderPass);
		} catch (Exception e2){
			e2.printStackTrace();
		}
		return false;
	}
	
	private void renderSpecialItem(){
	}
	
	private static void renderHand(float partialTicks, int xOffset){
		
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		float f = 0.07F;

		if (mc.gameSettings.anaglyph) {
			GlStateManager.translate((float) (-(xOffset * 2 - 1)) * f, 0.0F, 0.0F);
		}

		try{
			Project.gluPerspective((float) getFOVModifier.invoke(mc.entityRenderer, partialTicks, false),(float) mc.displayWidth / (float) mc.displayHeight, 0.05F, (float)(mc.gameSettings.renderDistanceChunks * 16) * 2.0F);
		}catch(Exception e){
			e.printStackTrace();
		}
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();

		if (mc.gameSettings.anaglyph) {
			GlStateManager.translate((float) (xOffset * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}

		GlStateManager.pushMatrix();
		hurtCameraEffect(partialTicks);

		if (mc.gameSettings.viewBobbing) {
			setupViewBobbing(partialTicks);
		}

		boolean flag = mc.getRenderViewEntity() instanceof EntityLivingBase
				&& ((EntityLivingBase) mc.getRenderViewEntity()).isPlayerSleeping();

		if (mc.gameSettings.thirdPersonView == 0 && !flag && !mc.gameSettings.hideGUI
				&& !mc.playerController.isSpectator()) {
			mc.entityRenderer.enableLightmap();
			mc.getItemRenderer().renderItemInFirstPerson(partialTicks);
			mc.entityRenderer.disableLightmap();
		}

		GlStateManager.popMatrix();

		if (mc.gameSettings.thirdPersonView == 0 && !flag) {
			mc.getItemRenderer().renderOverlays(partialTicks);
			hurtCameraEffect(partialTicks);
		}

		if (mc.gameSettings.viewBobbing) {
			setupViewBobbing(partialTicks);
		}
	}
	
	private static void hurtCameraEffect(float partialTicks){
		if(mc.getRenderViewEntity() instanceof EntityLivingBase){
			EntityLivingBase entitylivingbase=(EntityLivingBase)mc.getRenderViewEntity();
			float f=(float) entitylivingbase.hurtTime - partialTicks;

			if (entitylivingbase.getHealth() <= 0.0F) {
				float f1 = (float) entitylivingbase.deathTime + partialTicks;
				GlStateManager.rotate(40.0F - 8000.0F / (f1 + 200.0F), 0.0F, 0.0F, 1.0F);
			}

			if (f < 0.0F) {
				return;
			}

			f = f / (float) entitylivingbase.maxHurtTime;
			f = MathHelper.sin(f * f * f * f * (float) Math.PI);
			float f2 = entitylivingbase.attackedAtYaw;
			GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-f * 14.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
		}
	}

	private static void setupViewBobbing(float partialTicks) {
		if (mc.getRenderViewEntity() instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) mc.getRenderViewEntity();
			float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
			float f1 = -(entityplayer.distanceWalkedModified + f * partialTicks);
			float f2 = entityplayer.prevCameraYaw
					+ (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
			float f3 = entityplayer.prevCameraPitch
					+ (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
			GlStateManager.translate(MathHelper.sin(f1 * (float) Math.PI) * f2 * 0.5F,-Math.abs(MathHelper.cos(f1 * (float) Math.PI) * f2), 0.0F);
			GlStateManager.rotate(MathHelper.sin(f1 * (float) Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * (float) Math.PI - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(f3, 1.0F, 0.0F, 0.0F);
		}
	}
}
