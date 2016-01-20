package com.magiology.mcobjects.effect;

import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import org.lwjgl.opengl.*;

import com.magiology.core.*;
import com.magiology.util.renderers.*;
import com.magiology.util.renderers.tessellatorscripts.*;
import com.magiology.util.utilclasses.UtilM.U;

public class EntityBaseFX extends EntityFX{
	
	double gravity,friction,r_e,g_e,b_e,opacity_e;
	
	private static final ResourceLocation texture1 = new ResourceLocation(MReference.MODID+":/textures/particle/SmoothBuble1.png");
	
	public EntityBaseFX(World w, double xp, double yp, double zp, double xs, double ys, double zs, int siz, int lengt,double gravit, double Ra,double Ga,double Ba,double opacita,double frictio){
//		delete double Ra,double Ga,double Ba, and set r_e,g_e,b_e to 1 if you want to have a multicolored texture
        super(w, xp, yp, zp, xs, ys, zs);
        this.motionX =xs;
        this.motionY =ys;
        this.motionZ =zs;
        this.friction=frictio;
        this.particleMaxAge=lengt;
        this.particleScale=siz/10;
        this.gravity=gravit*0.001;
        this.r_e=Ra;
        this.g_e=Ga;
        this.b_e=Ba;
        this.opacity_e=opacita;
    }
	public EntityBaseFX(World w, double xp, double yp, double zp, double xs, double ys, double zs, int siz, int lengt){
//		use this if you want to use less customization
		super(w, xp, yp, zp, xs, ys, zs);
		this.motionX =xs;
		this.motionY =ys;
		this.motionZ =zs;
		this.particleMaxAge=lengt;
		this.particleScale=siz/10;
		
		this.friction=0.98;
		this.gravity=0;
		this.r_e=1;
		this.g_e=1;
		this.b_e=1;
		this.opacity_e=1;
	}
	
	public EntityBaseFX(World w, double xp, double yp, double zp){
		super(w, xp, yp, zp);
	}
	@Override
	public void renderParticle(WorldRenderer renderer, Entity entitiy, float par2, float par3, float par4, float par5, float par6, float par7){
		
	}
	public void renderParticle(float par2, float par3, float par4, float par5, float par6, float par7){
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
    	float PScale = 0.01F*this.particleScale;
    	float x=(float)(this.prevPosX+(this.posX-this.prevPosX)*par2-interpPosX);
    	float y=(float)(this.prevPosY+(this.posY-this.prevPosY)*par2-interpPosY);
    	float z=(float)(this.prevPosZ+(this.posZ-this.prevPosZ)*par2-interpPosZ);
        U.getMC().renderEngine.bindTexture(texture1);
        Renderer.beginQuads();
        GL11.glColor4f((float)this.r_e, (float)this.g_e, (float)this.b_e, (float)this.opacity_e);
        Renderer.addVertexData(x-par3*PScale-par6*PScale, y-par4*PScale, z-par5*PScale-par7*PScale, 0, 0).endVertex();
        Renderer.addVertexData(x-par3*PScale+par6*PScale, y+par4*PScale, z-par5*PScale+par7*PScale, 1, 0).endVertex();
        Renderer.addVertexData(x+par3*PScale+par6*PScale, y+par4*PScale, z+par5*PScale+par7*PScale, 1, 1).endVertex();
        Renderer.addVertexData(x+par3*PScale-par6*PScale, y-par4*PScale, z+par5*PScale-par7*PScale, 0, 1).endVertex();
		TessUtil.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	@Override
	public int getFXLayer(){return 3;}
	
	@Override
	public void onUpdate(){
		if(particleAge>particleMaxAge)this.setDead();
		if(U.getMC().gameSettings.particleSetting==2)this.setDead();
		
		if(worldObj.isRemote)this.motionHandler();
		
		this.particleScale-=particleMaxAge/10.0;
		
		if(this.isDead){
			
			
			
		}
		
		
		this.particleAge++;
	}
	
	public void motionHandler(){
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionX*=friction;
		this.motionY*=friction;
		this.motionZ*=friction;
		
		
		
		
		
		this.motionY +=this.gravity;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		
	}
	
}
