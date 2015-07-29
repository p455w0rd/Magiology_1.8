package com.magiology.mcobjects.effect;

import java.util.ArrayDeque;
import java.util.Queue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityMagiologyBaseFX extends EntityFX{
	
	double gravity,friction;
	int state;
	double r_e,g_e,b_e;
	double opacity_e;
	public static Queue<EntityMagiologyBaseFX> queuedRenders = new ArrayDeque();
	public float par2,par3,par4,par5,par6,par7;
	public EntityMagiologyBaseFX(World w, double xp, double yp, double zp, double xs, double ys, double zs){
		super(w, xp, yp, zp, xs, ys, zs);
		this.motionX =xs;
		this.motionY =ys;
		this.motionZ =zs;
    }
	public EntityMagiologyBaseFX(World w, double xp, double yp, double zp){
		super(w, xp, yp, zp);
	}
	@Override
	public void renderParticle(Tessellator tess, float pa2, float pa3, float pa4, float pa5, float pa6, float pa7){
		par2=pa2;par3=pa3;par4=pa4;par5=pa5;par6=pa6;par7=pa7;
		queuedRenders.add(this);
	}
	public static void renderBufferedParticle(Tessellator tess){
		for(EntityMagiologyBaseFX particle:queuedRenders)particle.render(tess);
		queuedRenders.clear();
	}
	public void render(Tessellator tess){
		renderStandardParticle(tess,par2,par3,par4,par5,par6,par7,0.01F*this.particleScale,true);
	}
	public void renderStandardParticle(Tessellator tess, float par2, float par3, float par4, float par5, float par6, float par7, float Scale,boolean draw){
		
    	float PScale = 0.01F*this.particleScale;
    	float x=(float)(this.prevPosX+(this.posX-this.prevPosX)*par2-interpPosX);
    	float y=(float)(this.prevPosY+(this.posY-this.prevPosY)*par2-interpPosY);
    	float z=(float)(this.prevPosZ+(this.posZ-this.prevPosZ)*par2-interpPosZ);
    	if(draw)tess.startDrawingQuads();
    	tess.addVertexWithUV((x-par3*PScale-par6*PScale), (y-par4*PScale), (z-par5*PScale-par7*PScale), 0, 0);
    	tess.addVertexWithUV((x-par3*PScale+par6*PScale), (y+par4*PScale), (z-par5*PScale+par7*PScale), 1, 0);
    	tess.addVertexWithUV((x+par3*PScale+par6*PScale), (y+par4*PScale), (z+par5*PScale+par7*PScale), 1, 1);
    	tess.addVertexWithUV((x+par3*PScale-par6*PScale), (y-par4*PScale), (z+par5*PScale-par7*PScale), 0, 1);
    	if(draw)tess.draw();
	}
	@Override
	public int getFXLayer(){return 3;}
	@Override
	public void onUpdate(){
		this.prevPosX=this.posX;
		this.prevPosY=this.posY;
		this.prevPosZ=this.posZ;
		this.particleAge++;
		if(Integer.valueOf(Minecraft.getMinecraft().effectRenderer.getStatistics())>2500&&worldObj.rand.nextBoolean())this.setDead();
		if(Minecraft.getMinecraft().gameSettings.particleSetting==2)this.setDead();
		this.motionHandeler();
	}
	
	public void motionHandeler(){
		this.motionX*=friction;
		this.motionY*=friction;
		this.motionZ*=friction;
		this.motionY +=this.gravity;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}
}
