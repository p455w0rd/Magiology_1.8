package com.magiology.mcobjects.effect;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.magiology.client.render.Textures;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class EntityFacedFX extends EntityFXM{
	
	double length,gravity,friction;
	int type;
	int state;
	boolean active;
	double r_e,g_e,b_e,opacity_e;
	double[] sideOpacity=new double[8],sideOpacityMultiplayer=new double[8];
	int[] sideOpacityChange=new int[8];
	String texture;
	public Vec3M rotation=new Vec3M();
	
	public EntityFacedFX(World w,double xp, double yp, double zp, double xs, double ys, double zs, int siz, double lengt,double gravit, boolean activ,int typ,String textur ,double Ra,double Ga,double Ba,double opacita,double frictio){
        super(w, xp, yp, zp, xs, ys, zs);
        motionX =xs;
        motionY =ys;
        motionZ =zs;
        friction=frictio;
        if(texture=="tx1"){
        	for(int a=0;a<sideOpacity.length;a++){
				sideOpacityChange[a]=worldObj.rand.nextInt(3)-1;
				sideOpacity[a]=worldObj.rand.nextDouble();
			}
        }
        type=typ;
        if(type==1){particleMaxAge=siz;particleScale=siz/10;}
        if(type==2){particleMaxAge=siz;particleScale=0;state=1;}
        else if(type>=3&&type<=10){particleMaxAge=siz;particleScale=siz/10;}
        gravity=gravit*0.001;
        length=lengt;
        active=activ;
        r_e=Ra;
        g_e=Ga;
        b_e=Ba;
        opacity_e=opacita;
        texture=textur;
    }
	public EntityFacedFX(World w,double xp, double yp, double zp, double xs, double ys, double zs, int siz, double lengt,double gravit,int typ ,double Ra,double Ga,double Ba,double opacita,double frictio){
		this(w,xp, yp, zp, xs, ys, zs, siz, lengt, gravit, false, typ, "tx1", Ra, Ga, Ba, opacita, frictio);
	}
	public EntityFacedFX(World w,double xp, double yp, double zp, double xs, double ys, double zs, int siz, double lengt,double gravit,int typ ,double Ra,double Ga,double Ba,double opacita){
		this(w,xp, yp, zp, xs, ys, zs, siz, lengt, gravit, typ, Ra, Ga, Ba, opacita, 0.99);
	}
	public EntityFacedFX(World w,double xp, double yp, double zp, double xs, double ys, double zs, int siz, double lengt,double gravit ,double Ra,double Ga,double Ba,double opacita){
		this(w,xp, yp, zp, xs, ys, zs, siz, lengt, gravit, 1, Ra, Ga, Ba, opacita);
	}
	
	@Override
	public void render(WorldRenderer tess){
		GL11.glDisable(GL11.GL_FOG);
		GL11U.setUpOpaqueRendering(2);
        
		
    	float PScale = 0.01F*particleScale;
    	float x=(float)(prevPosX+(posX-prevPosX)*par2-interpPosX);
    	float y=(float)(prevPosY+(posY-prevPosY)*par2-interpPosY);
    	float z=(float)(prevPosZ+(posZ-prevPosZ)*par2-interpPosZ);
    	
    	
    	if(texture=="tx1")     U.getMC().renderEngine.bindTexture(Textures.SmoothBuble1);
    	else if(texture=="tx2")U.getMC().renderEngine.bindTexture(Textures.SmoothBuble2);
    	else if(texture=="tx3")U.getMC().renderEngine.bindTexture(Textures.SmoothBuble3);
    	GL11.glPushMatrix();
    	GL11.glTranslated(x,y,z);
    	GL11.glTranslated(0, -0.095, 0);
    	GL11U.glRotate(rotation.x, rotation.y, rotation.z);
//    	GL11.glScaled(1, 1.3, 1);
    	GL11U.glCulFace(false);
    	tess.startDrawingQuads();
    	tess.setColorRGBA_F((float)r_e, (float)g_e, (float)b_e, (float)opacity_e);
    	tess.setBrightness(240);
    	tess.addVertexWithUV( PScale, PScale,0, 0, 0);
    	tess.addVertexWithUV(-PScale, PScale,0, 1, 0);
    	tess.addVertexWithUV(-PScale,-PScale,0, 1, 1);
    	tess.addVertexWithUV( PScale,-PScale,0, 0, 1);
    	TessUtil.draw();
    	GL11U.glCulFace(true);
    	
		
    	GL11.glPopMatrix();

		GL11U.endOpaqueRendering();
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		
		float random=worldObj.rand.nextFloat()*4;
		
		if(texture=="tx1")opacityHandler();
		if(type==1){
			particleScale-=0.001*particleMaxAge/(length/10);
			if(particleScale<2)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<4)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<6)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<8)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<0){
				setDead();
			}
		}
		else if(type==2){
			if(particleAge<particleMaxAge/8.5/2){
				particleScale+=0.004*particleMaxAge;
				double x1=(0.025-0.05*worldObj.rand.nextFloat())*particleScale/10;
				double y1=(0.025-0.05*worldObj.rand.nextFloat())*particleScale/10;
				double z1=(0.025-0.05*worldObj.rand.nextFloat())*particleScale/10;
				if(U.getMC().gameSettings.particleSetting==0&&worldObj.rand.nextBoolean())UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX+x1*4, posY+y1*4, posZ+z1*4, -x1/3+motionX,-y1/3+motionY, -z1/3+motionZ, particleMaxAge/2, 3,0, false,1,"tx1",r_e,g_e,b_e, 1.0, 0.99));
			}
			if(particleAge>particleMaxAge/8.5/2){
				particleScale-=0.004*particleMaxAge;
				if(active==true&&U.getMC().gameSettings.particleSetting==0&&worldObj.rand.nextBoolean())UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ, 0.025-0.05*worldObj.rand.nextFloat()+motionX,0.025-0.05*worldObj.rand.nextFloat()+motionY, 0.025-0.05*worldObj.rand.nextFloat()+motionZ, particleMaxAge/2, 3,-10, false,1,"tx1",r_e,g_e,b_e, 1.0, 0.99));
			}
			if(particleAge>particleMaxAge/8.5){
				setDead();
				if(active==true){
					for(int t=0;t<2;t++)UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,0.025-0.05*worldObj.rand.nextFloat()+motionX,0.025-0.05*worldObj.rand.nextFloat()+motionY, 0.025-0.05*worldObj.rand.nextFloat()+motionZ, particleMaxAge/2, 4,-1, false,1,"tx1",r_e,g_e,b_e, opacity_e, 0.99));
				}
			}
		}
		else if(type==3){
			particleScale-=0.001*particleMaxAge/(length/10);
			if(particleScale<2)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<4)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<6)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<8)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<0){
				setDead();
				for(int a=0;a<40;a++){
					UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,(0.25-0.5*worldObj.rand.nextFloat())*random,(0.25-0.5*worldObj.rand.nextFloat())*random, (0.25-0.5*worldObj.rand.nextFloat())*random,1500, 5,-5, false,1,"tx1",worldObj.rand.nextFloat(),worldObj.rand.nextFloat(),worldObj.rand.nextFloat(), 1, 0.99));
					}
			}
		}
		else if(type==4){
			particleScale-=0.001*particleMaxAge/(length/10);
			if(particleScale<2)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<4)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<6)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<8)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<0){
				setDead();
				for(int a=0;a<20;a++){
					UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX+(5-10*worldObj.rand.nextFloat())*random, posY+(5-10*worldObj.rand.nextFloat())*random, posZ+(5-10*worldObj.rand.nextFloat())*random,(0.025-0.05*worldObj.rand.nextFloat())*random,(0.025-0.05*worldObj.rand.nextFloat())*random, (0.025-0.05*worldObj.rand.nextFloat())*random,1000, 5,-5, false,2,"tx1",worldObj.rand.nextFloat(),worldObj.rand.nextFloat(),worldObj.rand.nextFloat(), 1, 0.99));
				}
			}
		}
		else if(type==5){
			if(particleAge<particleMaxAge/8.5/3){
				particleScale+=0.004*particleMaxAge;
				}
			else{
				setDead();
					for(int t=0;t<40;t++)UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,1-2*worldObj.rand.nextFloat()+motionX,3-4*worldObj.rand.nextFloat()+motionY*2, 1-2*worldObj.rand.nextFloat()+motionZ, particleMaxAge*4, 6,-1, false,1,"tx1",worldObj.rand.nextFloat(),worldObj.rand.nextFloat(),worldObj.rand.nextFloat(), 1, 0.99));
			}
		}
		else if(type==6){
//			motionY-=0.001;
			if(particleAge<particleMaxAge/8.5/3){
				particleScale+=0.006*particleMaxAge;
			}
			else{
				if(motionY<0.01){
					setDead();
					for(int t=0;t<8;t++){
						for(int t1=0;t1<5;t1++){
							if(worldObj.rand.nextInt(3)==0){
								UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,
										1.5-3*worldObj.rand.nextFloat()+motionX,2.5-3*worldObj.rand.nextFloat()+motionY*2, 1.5-3*worldObj.rand.nextFloat()+motionZ, 
										800*5, 6,-1, false,2,
												"tx1",worldObj.rand.nextFloat(),worldObj.rand.nextFloat(),worldObj.rand.nextFloat(), 1, 0.99));
							}else{
								UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,
										1.5-3*worldObj.rand.nextFloat()+motionX,2.5-3*worldObj.rand.nextFloat()+motionY*2, 1.5-3*worldObj.rand.nextFloat()+motionZ,  
										800*20, 40,-1, false,1,
												"tx1",worldObj.rand.nextFloat(),worldObj.rand.nextFloat(),worldObj.rand.nextFloat(), 1, 0.99));
							}
						}
						UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,
								(2-4*worldObj.rand.nextFloat()+motionX)/2, 1+(0.5-worldObj.rand.nextFloat()+motionX)/2, (2-4*worldObj.rand.nextFloat()+motionZ)/2, 
								800, 400,-5, false,8,
										"tx1",worldObj.rand.nextFloat(),worldObj.rand.nextFloat(),worldObj.rand.nextFloat(), 1, 0.99));
						
					}
				}
				else UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,1-2*worldObj.rand.nextFloat()+motionX,1-2*worldObj.rand.nextFloat()+motionY, 1-2*worldObj.rand.nextFloat()+motionZ, particleMaxAge*2, 4,-20, false,1,"tx1",r_e,g_e,b_e, opacity_e, 0.99));
			}
		}
		else if(type==7){
			if(particleAge>particleMaxAge/8.5){
				setDead();
			}
			else {
				for(int e=0;e<3;e++){
					double[] AB=UtilM.cricleXZ((particleAge/4.0)+(e>=1?180*e:0));
					
					
					UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,
					AB[0]*3-UtilM.RF()/2,3-UtilM.RF(), AB[1]*3-UtilM.RF()/2,
					(int)(particleMaxAge*2.5), 10,-100, false,1,"tx1",worldObj.rand.nextFloat(),worldObj.rand.nextFloat(),worldObj.rand.nextFloat(), 1, 0.99));
				}
				
			}
		}
		else if(type==8){
			if(isCollidedVertically)particleScale-=0.001*particleMaxAge/(length/10)*100;
			if(particleScale<=0){
				setDead();
				
				for(int a=0;a<10;a++){

					float xrand=0.5F-worldObj.rand.nextFloat();
					float yrand=0.5F-(worldObj.rand.nextFloat()*0.5F);
					float zrand=0.5F-worldObj.rand.nextFloat();
					
					UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX+xrand, posY+yrand, posZ+zrand,
							-xrand/21, -yrand/21, -zrand/21,200,4,0,true,1,"tx1", r_e+(0.5-worldObj.rand.nextFloat())/10, g_e+(0.5-worldObj.rand.nextFloat())/10, b_e+(0.5-worldObj.rand.nextFloat())/10,1, 0.99));
				}
			}
		}
		else if(type==9){
			if(isCollidedVertically)particleScale-=0.001*particleMaxAge/(length/10)*100;
			if(particleScale<=0){
				setDead();
				
				for(int a=0;a<10;a++){
					
					float xrand=0.5F-worldObj.rand.nextFloat();
					float yrand=0.5F-(worldObj.rand.nextFloat()*0.5F);
					float zrand=0.5F-worldObj.rand.nextFloat();
					
					UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX+xrand, posY+yrand, posZ+zrand,
							-xrand/21, -yrand/21, -zrand/21,200,4,0,true,1,"tx1", r_e+(0.5-worldObj.rand.nextFloat())/10, g_e+(0.5-worldObj.rand.nextFloat())/10, b_e+(0.5-worldObj.rand.nextFloat())/10,1, 0.99));
				}
			}
		}
		else type=1;
	}
	
	@Override
	public void motionHandler(){
		motionX*=friction;
		motionY*=friction;
		motionZ*=friction;
		motionY +=gravity;
		moveEntity(motionX, motionY, motionZ);
		
	}
	public void opacityHandler(){
		
		if(particleAge%12==0){
			for(int a=0;a<sideOpacity.length;a++){
				sideOpacityChange[a]=worldObj.rand.nextInt(5)-2;
			}
			for(int a=0;a<sideOpacity.length;a++){
				if(sideOpacityMultiplayer[a]>0.99)sideOpacityChange[a]=worldObj.rand.nextInt(3)-2;
				else if(sideOpacityMultiplayer[a]<0.01)sideOpacityChange[a]=worldObj.rand.nextInt(3);
			}
		}
		for(int a=0;a<sideOpacityMultiplayer.length;a++){
			sideOpacityMultiplayer[a]+=sideOpacityChange[a]/30.0;
			sideOpacity[a]=sideOpacityMultiplayer[a]*opacity_e;
		}
		
	}
}
