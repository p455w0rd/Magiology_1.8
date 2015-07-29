package com.magiology.mcobjects.effect;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

import com.magiology.objhelper.helpers.renderers.NoramlisedVertixBuffer;

public abstract class EntityFXM extends EntityFX{
	public static final float p=1F/16F;
	protected static Tessellator tess=Tessellator.instance;
	public EntityFXM(World world, double x, double y, double z, double xSpeed, double ySpeed,double zSpeed){
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
	}
	
	public void renderParticle(NoramlisedVertixBuffer buf, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_)
    {
        float f6 = (float)particleTextureIndexX / 16.0F;
        float f7 = f6 + 0.0624375F;
        float f8 = (float)particleTextureIndexY / 16.0F;
        float f9 = f8 + 0.0624375F;
        float f10 = 0.1F * particleScale;

        if (particleIcon != null)
        {
            f6 = particleIcon.getMinU();
            f7 = particleIcon.getMaxU();
            f8 = particleIcon.getMinV();
            f9 = particleIcon.getMaxV();
        }

        float f11 = (float)(prevPosX + (posX - prevPosX) * (double)p_70539_2_ - interpPosX);
        float f12 = (float)(prevPosY + (posY - prevPosY) * (double)p_70539_2_ - interpPosY);
        float f13 = (float)(prevPosZ + (posZ - prevPosZ) * (double)p_70539_2_ - interpPosZ);
        tess.setColorRGBA_F(particleRed, particleGreen, particleBlue, particleAlpha);
        buf.addVertexWithUV((double)(f11 - p_70539_3_ * f10 - p_70539_6_ * f10), (double)(f12 - p_70539_4_ * f10), (double)(f13 - p_70539_5_ * f10 - p_70539_7_ * f10), (double)f7, (double)f9);
        buf.addVertexWithUV((double)(f11 - p_70539_3_ * f10 + p_70539_6_ * f10), (double)(f12 + p_70539_4_ * f10), (double)(f13 - p_70539_5_ * f10 + p_70539_7_ * f10), (double)f7, (double)f8);
        buf.addVertexWithUV((double)(f11 + p_70539_3_ * f10 + p_70539_6_ * f10), (double)(f12 + p_70539_4_ * f10), (double)(f13 + p_70539_5_ * f10 + p_70539_7_ * f10), (double)f6, (double)f8);
        buf.addVertexWithUV((double)(f11 + p_70539_3_ * f10 - p_70539_6_ * f10), (double)(f12 - p_70539_4_ * f10), (double)(f13 + p_70539_5_ * f10 - p_70539_7_ * f10), (double)f6, (double)f9);
    }
	public abstract int getRenderPass();
}
