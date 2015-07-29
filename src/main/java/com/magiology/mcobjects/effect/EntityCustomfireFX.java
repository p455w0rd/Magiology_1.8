package com.magiology.mcobjects.effect;

import org.lwjgl.opengl.GL11;

import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.NoramlisedVertixBuffer;
import com.magiology.objhelper.helpers.renderers.TessHelper;
import com.magiology.render.Textures;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityCloudFX;
import net.minecraft.client.particle.EntityLavaFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityCustomfireFX extends EntityMagiologyBaseFX{
	public boolean active=false;
	int optimizer;
	boolean random;
	float[] roration=new float[3],prevRoration=new float[3];
	float[] rorationSize=new float[3];
	
	public EntityCustomfireFX(World w, double xp, double yp, double zp, double xs, double ys, double zs, boolean l, float sc){
        super(w, xp, yp, zp, xs, ys, zs);
        for(int i=0;i<roration.length;i++){
        	roration[i]=worldObj.rand.nextInt(360);
        }
        this.motionX =xs;
        this.motionY =ys;
        this.motionZ =zs;
        this.particleMaxAge=1;
        this.active=l;
        this.particleScale=sc;
        this.particleAge=0;
    }
	
	
	@Override
	public void render(Tessellator tess){
		GL11.glDisable(GL11.GL_LIGHTING);
//		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
//		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
		float p=1F/16F;
		double ps=this.particleScale*1.5;
		float x=(float)(prevPosX + (posX-prevPosX)* par2 - interpPosX);
		float y=(float)(prevPosY + (posY-prevPosY)* par2 - interpPosY);
		float z=(float)(prevPosZ + (posZ-prevPosZ)* par2 - interpPosZ);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(Textures.SmoothBuble1);
		
		GL11H.SetUpOpaqueRendering(2);
		tess.startDrawingQuads();
		tess.setColorRGBA_F(1, 0, 0, 0.5F);
		tess.addVertexWithUV((x-par3*ps/1.5-par6*ps/1.5), (y-par4*ps/1.5), (z-par5*ps/1.5-par7*ps/1.5), 0, 0);
		tess.addVertexWithUV((x-par3*ps/1.5+par6*ps/1.5), (y+par4*ps/1.5), (z-par5*ps/1.5+par7*ps/1.5), 1, 0);
		tess.addVertexWithUV((x+par3*ps/1.5+par6*ps/1.5), (y+par4*ps/1.5), (z+par5*ps/1.5+par7*ps/1.5), 1, 1);
		tess.addVertexWithUV((x+par3*ps/1.5-par6*ps/1.5), (y-par4*ps/1.5), (z+par5*ps/1.5-par7*ps/1.5), 0, 1);
		tess.draw();
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		
		TessHelper.bindTexture(Textures.FireHD);
		
//		GL11.glPushMatrix();
//		GL11.glEnable(GL11.GL_LIGHTING);
//		GL11.glTranslated(x, y, z);
//		GL11.glRotated(roration[0], 1, 0, 0);
//		GL11.glRotated(roration[1], 0, 1, 0);
//		GL11.glRotated(roration[2], 0, 0, 1);
//
//		cube.render(this, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
//		GL11.glPopMatrix();

		tess.setBrightness(240);
		
        RenderHelper.enableStandardItemLighting();
		
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11H.rotateXYZ(Helper.calculateRenderPosArray(prevRoration, roration));
		GL11.glTranslated(-x, -y, -z);
		
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		double lol=2.3;
		
		
        
		GL11H.SetUpOpaqueRendering(1);
		if(particleScale>0.8)TessHelper.drawBlurredCube(0, 0, 0, -p*ps*lol, -p*ps*lol, -p*ps*lol, p*ps*lol, p*ps*lol, p*ps*lol, 20, 0.07,  1, 1, 1, 0.7);
		GL11H.EndOpaqueRendering();
		GL11.glPopMatrix();
		
		NoramlisedVertixBuffer a=TessHelper.getNVB();
		
		
		a.addVertexWithUV(x-p*ps, y+p*ps, z+p*ps, 0, 1);
		a.addVertexWithUV(x+p*ps, y+p*ps, z+p*ps, 1, 1);
		a.addVertexWithUV(x+p*ps, y+p*ps, z-p*ps, 1, 0);
		a.addVertexWithUV(x-p*ps, y+p*ps, z-p*ps, 0, 0);
		
		a.addVertexWithUV(x-p*ps, y-p*ps, z-p*ps, 0, 0);
		a.addVertexWithUV(x+p*ps, y-p*ps, z-p*ps, 1, 0);
		a.addVertexWithUV(x+p*ps, y-p*ps, z+p*ps, 1, 1);
		a.addVertexWithUV(x-p*ps, y-p*ps, z+p*ps, 0, 1);
		
		a.addVertexWithUV(x-p*ps, y+p*ps, z-p*ps, 0, 1);
		a.addVertexWithUV(x+p*ps, y+p*ps, z-p*ps, 1, 1);
		a.addVertexWithUV(x+p*ps, y-p*ps, z-p*ps, 1, 0);
		a.addVertexWithUV(x-p*ps, y-p*ps, z-p*ps, 0, 0);

		a.addVertexWithUV(x-p*ps, y-p*ps, z+p*ps, 0, 0);
		a.addVertexWithUV(x+p*ps, y-p*ps, z+p*ps, 1, 0);
		a.addVertexWithUV(x+p*ps, y+p*ps, z+p*ps, 1, 1);
		a.addVertexWithUV(x-p*ps, y+p*ps, z+p*ps, 0, 1);
		
		a.addVertexWithUV(x+p*ps, y+p*ps, z+p*ps, 0, 1);
		a.addVertexWithUV(x+p*ps, y-p*ps, z+p*ps, 1, 1);
		a.addVertexWithUV(x+p*ps, y-p*ps, z-p*ps, 1, 0);
		a.addVertexWithUV(x+p*ps, y+p*ps, z-p*ps, 0, 0);

		a.addVertexWithUV(x-p*ps, y+p*ps, z-p*ps, 0, 0);
		a.addVertexWithUV(x-p*ps, y-p*ps, z-p*ps, 1, 0);
		a.addVertexWithUV(x-p*ps, y-p*ps, z+p*ps, 1, 1);
		a.addVertexWithUV(x-p*ps, y+p*ps, z+p*ps, 0, 1);
		
		a.draw();
		GL11.glPopMatrix();
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
//		GL11.glEnable(GL11.GL_LIGHTING);
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	@Override
	public void renderParticle(Tessellator tess, float pa2, float pa3, float pa4, float pa5, float pa6, float pa7){
		par2=pa2;par3=pa3;par4=pa4;par5=pa5;par6=pa6;par7=pa7;
		queuedRenders.add(this);
	}
	public static void RenderQueuedParticle(Tessellator tess){
		for(EntityMagiologyBaseFX particle:queuedRenders)particle.render(tess);
		queuedRenders.clear();
	}
	
	@Override
	public int getFXLayer(){return 3;}
	
	@Override
	public void onUpdate(){
		if(Integer.valueOf(Minecraft.getMinecraft().effectRenderer.getStatistics())>2500)if(Helper.RInt(10)==0)this.setDead();
		if(Minecraft.getMinecraft().gameSettings.particleSetting==2)this.setDead();
		prevRoration=roration.clone();
		this.particleWithSize15andSpeed35();
		this.spawningParticleHandeler();
	    this.onCollided();
		this.motionHandeler();
	}
	
	@Override
	public void motionHandeler(){
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.isInWeb)
		{
			this.motionY +=rand.nextFloat();
			this.motionX+=0.05-0.1*rand.nextFloat();
			this.motionZ+=0.05-0.1*rand.nextFloat();
			this.active=true;
    		this.particleMaxAge=100;
			if(this.particleAge++>=this.particleMaxAge){
				this.setDead();
				if(Minecraft.getMinecraft().gameSettings.particleSetting==0)for(int gol=0;gol<200;gol++)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, this.posX, this.posY, this.posZ, 0.25-0.5*worldObj.rand.nextFloat(),-0.25+0.25-0.5*worldObj.rand.nextFloat(), 0.25-0.5*worldObj.rand.nextFloat()));
				Helper.spawnEntityFX(new EntityLavaFX(worldObj, this.posX, this.posY, this.posZ));
			}
		}
		
		for(int i=0;i<roration.length;i++){
			rorationSize[i]+=Helper.CRandD(10);
			rorationSize[i]*=0.9;
			roration[i]+=rorationSize[i];
			if(roration[i]<0){
				if(roration[i]<360)roration[i]+=360;
			}else{
				if(roration[i]>360)roration[i]-=360;
			}
		}
		this.motionY +=-0.009;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		
	}
	
	public void particleWithSize15andSpeed35(){
        if(this.motionY<-3.5&&this.particleScale>=15){
        	if(worldObj.rand.nextInt(2)==0)Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, this.motionX/2+0.05-0.1*worldObj.rand.nextFloat(),this.motionY/2, this.motionX/2+0.05-0.1*worldObj.rand.nextFloat(), worldObj.rand.nextBoolean(),1+worldObj.rand.nextInt(3)));
        	for(int grde=0;grde<10;grde++){
        		Helper.spawnEntityFX(new EntitySmokeFX(worldObj, this.posX+worldObj.rand.nextFloat(), this.posY+worldObj.rand.nextFloat(), this.posZ+worldObj.rand.nextFloat(), this.motionX/2+0.25-0.5*worldObj.rand.nextFloat(),-this.motionY/3, this.motionX/2+0.25-0.5*worldObj.rand.nextFloat()));
        		Helper.spawnEntityFX(new EntitySmokeFX(worldObj, this.posX+worldObj.rand.nextFloat(), this.posY+worldObj.rand.nextFloat(), this.posZ+worldObj.rand.nextFloat(), this.motionX/2+0.25-0.5*worldObj.rand.nextFloat(),-this.motionY/3, this.motionX/2+0.25-0.5*worldObj.rand.nextFloat()));
        		Helper.spawnEntityFX(new EntitySmokeFX(worldObj, this.posX+worldObj.rand.nextFloat(), this.posY+worldObj.rand.nextFloat(), this.posZ+worldObj.rand.nextFloat(), this.motionX/2+0.25-0.5*worldObj.rand.nextFloat(),-this.motionY/3, this.motionX/2+0.25-0.5*worldObj.rand.nextFloat()));
        		Helper.spawnEntityFX(new EntityCloudFX(worldObj, this.posX+worldObj.rand.nextFloat(), this.posY+worldObj.rand.nextFloat(), this.posZ+worldObj.rand.nextFloat(), this.motionX/2+0.25-0.5*worldObj.rand.nextFloat(),-this.motionY/3, this.motionX/2+0.25-0.5*worldObj.rand.nextFloat()));
        		Helper.spawnEntityFX(new EntityLavaFX( worldObj, this.posX+worldObj.rand.nextFloat(), this.posY+worldObj.rand.nextFloat(), this.posZ+worldObj.rand.nextFloat()));
        		Helper.spawnEntityFX(new EntityLavaFX( worldObj, this.posX+worldObj.rand.nextFloat(), this.posY+worldObj.rand.nextFloat(), this.posZ+worldObj.rand.nextFloat()));
        	}
        }
	}
	
	public void onCollided(){
		if(this.isCollided){
			//-----------------------------------------
			
			for(int i=0;i<roration.length;i++){
				roration[i]=0;
			}
			
	    	if(this.particleScale>=15){
	    		this.particleMaxAge=10;
	    		if(this.particleAge++>=this.particleMaxAge){
	    			this.setDead();
    				

	    		}
	    		if(Minecraft.getMinecraft().gameSettings.particleSetting>=1){
//	    			for(int i=0;i<10;i++)
	    				Helper.spawnEntityFX(new EntityLavaFX(worldObj, this.posX+worldObj.rand.nextFloat()*10, this.posY+1+worldObj.rand.nextFloat()*10, this.posZ+worldObj.rand.nextFloat()*10));
	    		}
	    	}
	    	else if(this.active==true){
	    		this.particleMaxAge=10;
	    		if(this.particleAge==0)random=rand.nextBoolean();
	    		if(random){
	    			
	    			this.motionX*=0.96;
	    			this.motionY*=0.96;
	    			this.motionZ*=0.96;
	    			
		    		if(this.particleAge++>=this.particleMaxAge){
		    			this.setDead();
		    			this.spda();
		    		}
	    		}
	    		else{
	    			this.setDead();
	    			this.spda();
	    		}
	    	}
	    	else this.setDead();
			//-----------------------------------------
		}
	}
	
	public void spawningParticleHandeler(){
        if(this.active==true){
        	if(optimizer++>=1&&Minecraft.getMinecraft().gameSettings.particleSetting==0){
        		optimizer=0;
        		EntitySmoothBubleFX sb=new EntitySmoothBubleFX(worldObj,this.posX, this.posY, this.posZ,this.motionX/2+Helper.CRandF(0.1)*particleScale, this.motionY/2+Helper.CRandF(0.1)*particleScale, this.motionZ/2+Helper.CRandF(0.1)*particleScale,(int) (600*particleScale), 1.5,this.motionY/2, false,1,"tx1",1,0,0, 0.3, 0.96);
            	Helper.spawnEntityFX(sb);
            	sb.noClip=true;
            	if(particleScale>0.8)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, this.posX, this.posY, this.posZ, Helper.CRandF(0.1), Helper.CRandF(0.1), Helper.CRandF(0.1)));
        		
        		
        	}
        }
		if(particleScale<0.8)return;
        
        if(worldObj.rand.nextInt(500)<=1){
        	worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0, 0, 0);
        	worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0, 0, 0);
        	if(worldObj.rand.nextBoolean())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, 0.15-0.3*worldObj.rand.nextFloat(), 0.1+this.motionY*2, 0.15-0.3*worldObj.rand.nextFloat(), true,particleScale));
        }
	}
	
	public void spda(){
		if(particleScale<0.8)return;
		worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0, 0, 0);
		if(worldObj.rand.nextInt(40)==1){
			if(Minecraft.getMinecraft().gameSettings.particleSetting==0)worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0, 0.1, 0);
			if(worldObj.rand.nextBoolean())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, 0+this.motionX/2,    -this.motionY, 0+this.motionZ/2, true,particleScale));
    		if(worldObj.rand.nextBoolean())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, 0.1+this.motionX/2,  -this.motionY, 0+this.motionZ/2, true,particleScale));
    		if(worldObj.rand.nextBoolean())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, 0.1+this.motionX/2,  -this.motionY, 0.1+this.motionZ/2, true,particleScale));
    		if(worldObj.rand.nextBoolean())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, -0.1+this.motionX/2, -this.motionY, 0+this.motionZ/2, true,particleScale));
    		if(worldObj.rand.nextBoolean())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, -0.1+this.motionX/2, -this.motionY, 0.1+this.motionZ/2, true,particleScale));
    		if(worldObj.rand.nextBoolean())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, -0.1+this.motionX/2, -this.motionY, -0.1+this.motionZ/2, true,particleScale));
   			if(worldObj.rand.nextBoolean())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, 0+this.motionX/2,    -this.motionY, 0.1+this.motionZ/2, true,particleScale));
   			if(worldObj.rand.nextBoolean())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, 0+this.motionX/2,    -this.motionY, -0.1+this.motionZ/2, true,particleScale));
   			if(worldObj.rand.nextBoolean())Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY+1, this.posZ, 0.1+this.motionX/2, -this.motionY, -0.1+this.motionZ/2, true,particleScale));
   			if(worldObj.rand.nextInt(80)==0)Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, 356, this.posZ, 0.25-0.5*worldObj.rand.nextFloat(), -3.5, 0.25-0.5*worldObj.rand.nextFloat(), true,15));
    	}
    	else if(worldObj.rand.nextInt(2)==0){
    		Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, 0.05-0.1*worldObj.rand.nextFloat()+this.motionX/2, worldObj.rand.nextFloat()*0.3-this.motionY/3, 0.05-0.1*worldObj.rand.nextFloat()+this.motionZ/2, true,1));
    		if(Minecraft.getMinecraft().gameSettings.particleSetting==0){
    			for(int i=0;i<10;i++)Helper.spawnEntityFX(new EntitySmokeFX(worldObj, this.posX, this.posY, this.posZ, 0.05-0.1*worldObj.rand.nextFloat(),0.05-0.1*worldObj.rand.nextFloat(),0.05-0.1*worldObj.rand.nextFloat()));
    		}
    	}
    	else if(Minecraft.getMinecraft().gameSettings.particleSetting==0){
    		for(int i=0;i<2;i++)Helper.spawnEntityFX(new EntityLavaFX(worldObj, this.posX, this.posY, this.posZ));
    		if(H.getFPS()>20)for(int i=0;i<2+H.RInt(10);i++)Helper.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, H.CRandF(0.1)+motionX,    0.15+H.CRandF(0.1), H.CRandF(0.1)+motionZ, true,0.2F));
    		worldObj.spawnParticle("largesmoke", this.posX, this.posY, this.posZ, 0, 0, 0);
    		Helper.spawnEntityFX(new EntityCloudFX(worldObj, this.posX, this.posY, this.posZ, 0,0.1,0));
    	}
	}
	
	
	
}
