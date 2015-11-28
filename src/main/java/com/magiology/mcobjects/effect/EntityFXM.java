package com.magiology.mcobjects.effect;

import java.util.*;

import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;

import com.magiology.util.renderers.*;
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
	public void func_180434_a(WorldRenderer world, Entity ent, float pa2, float pa3, float pa4, float pa5, float pa6, float pa7){
		par2=pa2;par3=pa3;par4=pa4;par5=pa5;par6=pa6;par7=pa7;
		queuedRenders.add(this);
	}
	public static void renderBufferedParticle(WorldRenderer tess){
		for(EntityFXM particle:queuedRenders)particle.render(tess);
		queuedRenders.clear();
	}
	public void render(WorldRenderer tess){
		renderStandardParticle(tess,par2,par3,par4,par5,par6,par7,0.01F*this.particleScale,true);
	}
	public void renderStandardParticle(WorldRenderer tess, float par2, float par3, float par4, float par5, float par6, float par7, float Scale,boolean draw){
		
    	float PScale = 0.01F*this.particleScale;
    	float x=(float)(this.prevPosX+(this.posX-this.prevPosX)*par2-interpPosX);
    	float y=(float)(this.prevPosY+(this.posY-this.prevPosY)*par2-interpPosY);
    	float z=(float)(this.prevPosZ+(this.posZ-this.prevPosZ)*par2-interpPosZ);
    	if(draw)tess.startDrawingQuads();
    	tess.addVertexWithUV((x-par3*PScale-par6*PScale), (y-par4*PScale), (z-par5*PScale-par7*PScale), 0, 0);
    	tess.addVertexWithUV((x-par3*PScale+par6*PScale), (y+par4*PScale), (z-par5*PScale+par7*PScale), 1, 0);
    	tess.addVertexWithUV((x+par3*PScale+par6*PScale), (y+par4*PScale), (z+par5*PScale+par7*PScale), 1, 1);
    	tess.addVertexWithUV((x+par3*PScale-par6*PScale), (y-par4*PScale), (z+par5*PScale-par7*PScale), 0, 1);
    	if(draw)TessUtil.draw();
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
