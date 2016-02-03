package com.magiology.core;

import com.magiology.client.render.models.ModelWingsFromTheBlackFire;

public class Config{
	private static boolean shadersEnabled=true;
	public static boolean isShadersEnabled(){return shadersEnabled;}
	public static void setShadersEnabled(boolean shadersEnabled){Config.shadersEnabled = shadersEnabled;}

	private static float PARTICLE_AMOUNT=1;
	public static float getParticleAmount(){return PARTICLE_AMOUNT;}
	public static void setParticleAmount(float particleAmount){PARTICLE_AMOUNT=particleAmount;}

	private static boolean wingsThick=true;
	public static boolean isWingsThick(){return wingsThick;}
	public static void setWingsThick(boolean wingsThick){ModelWingsFromTheBlackFire.wings3D(Config.wingsThick=wingsThick);}
}
