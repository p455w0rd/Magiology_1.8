package com.magiology.mcobjects.effect;

import java.util.*;

import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;

import com.magiology.util.renderers.*;
import com.magiology.util.renderers.tessellatorscripts.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.*;

public class EntityFXM extends EntityFX{
	
	public double gravity,friction;
	public int state;
	public ColorF color;
	public static Queue<EntityFXM> queuedRenders = new ArrayDeque();
	public float par2,par3,par4,par5,par6,par7;
	public EntityFXM(World w, double xp, double yp, double zp, double xs, double ys, double zs){
		super(w, xp, yp, zp, xs, ys, zs);
		this.motionX =xs;
		this.motionY =ys;
		this.motionZ =zs;
    }
	public EntityFXM(World w, double xp, double yp, double zp){
		super(w, xp, yp, zp);
	}
	@Override
	public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float pa2, float pa3, float pa4, float pa5, float pa6, float pa7){
		par2=pa2;par3=pa3;par4=pa4;par5=pa5;par6=pa6;par7=pa7;
		queuedRenders.add(this);

//        Renderer.begin(7,DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
//        Renderer.addVertexData((double)(f5 - p_180434_4_ * f4 - p_180434_7_ * f4), (double)(f6 - p_180434_5_ * f4), (double)(f7 - p_180434_6_ * f4 - p_180434_8_ * f4), (double)f1, (double)f3).setColor(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//        Renderer.addVertexData((double)(f5 - p_180434_4_ * f4 + p_180434_7_ * f4), (double)(f6 + p_180434_5_ * f4), (double)(f7 - p_180434_6_ * f4 + p_180434_8_ * f4), (double)f1, (double)f2).setColor(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//        Renderer.addVertexData((double)(f5 + p_180434_4_ * f4 + p_180434_7_ * f4), (double)(f6 + p_180434_5_ * f4), (double)(f7 + p_180434_6_ * f4 + p_180434_8_ * f4), (double)f, (double)f2).setColor(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//        Renderer.addVertexData((double)(f5 + p_180434_4_ * f4 - p_180434_7_ * f4), (double)(f6 - p_180434_5_ * f4), (double)(f7 + p_180434_6_ * f4 - p_180434_8_ * f4), (double)f, (double)f3).setColor(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//        Renderer.draw();
	}
	public static void renderBufferedParticle(){
		for(EntityFXM particle:queuedRenders)particle.render();
		queuedRenders.clear();
	}
	public void render(){
		renderStandardParticle(par2,par3,par4,par5,par6,par7,0.01F*this.particleScale,true);
	}
	public void renderStandardParticle(float par2, float par3, float par4, float par5, float par6, float par7, float Scale,boolean draw){
		
    	float PScale = 0.01F*this.particleScale;
    	float x=(float)(this.prevPosX+(this.posX-this.prevPosX)*par2-interpPosX);
    	float y=(float)(this.prevPosY+(this.posY-this.prevPosY)*par2-interpPosY);
    	float z=(float)(this.prevPosZ+(this.posZ-this.prevPosZ)*par2-interpPosZ);
        int i = this.getBrightnessForRender(par2);
        int j = i >> 16 & 65535;
        int k = i & 65535;
    	if(draw)Renderer.begin(7,DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
    	Renderer.addVertexData((x-par3*PScale-par6*PScale), (y-par4*PScale), (z-par5*PScale-par7*PScale), 0, 0).setColor(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
    	Renderer.addVertexData((x-par3*PScale+par6*PScale), (y+par4*PScale), (z-par5*PScale+par7*PScale), 1, 0).setColor(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
    	Renderer.addVertexData((x+par3*PScale+par6*PScale), (y+par4*PScale), (z+par5*PScale+par7*PScale), 1, 1).setColor(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
    	Renderer.addVertexData((x+par3*PScale-par6*PScale), (y-par4*PScale), (z+par5*PScale-par7*PScale), 0, 1).setColor(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
    	if(draw)Renderer.draw();
	}
	@Override
	public int getFXLayer(){return 3;}
	@Override
	public void onUpdate(){
		this.prevPosX=this.posX;
		this.prevPosY=this.posY;
		this.prevPosZ=this.posZ;
		this.particleAge++;
		if(Integer.valueOf(Get.Render.ER().getStatistics())>2500&&worldObj.rand.nextBoolean())this.setDead();
		if(U.getMC().gameSettings.particleSetting==2)this.setDead();
		this.motionHandler();
	}
	
	public void motionHandler(){
		this.motionX*=friction;
		this.motionY*=friction;
		this.motionZ*=friction;
		this.motionY +=this.gravity;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}
}
