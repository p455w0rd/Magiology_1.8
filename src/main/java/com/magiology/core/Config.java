package com.magiology.core;

public class Config{
	public static boolean shadersEnabled=true;
	private static float PARTICLE_AMOUNT=1;
	public static float getParticleAmount(){
		return PARTICLE_AMOUNT;
	}
	public static void setParticleAmount(float particleAmount){
		PARTICLE_AMOUNT=particleAmount;
	}
	
}
