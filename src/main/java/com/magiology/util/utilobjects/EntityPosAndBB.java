package com.magiology.util.utilobjects;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import com.magiology.util.utilclasses.Helper;

public class EntityPosAndBB{
	public double[] BB;
	public Entity entity;
	private boolean isDead,isRendering;
	
	public EntityPosAndBB(Entity entity){
		this.entity=entity;
		this.BB=new double[]{entity.getBoundingBox().minX,entity.getBoundingBox().minY,entity.getBoundingBox().minZ,entity.getBoundingBox().maxX,entity.getBoundingBox().maxY,entity.getBoundingBox().maxZ};
	}
	
	public double[] getRandDotInBB(double randomness){
		double[] result={0,0,0};
		result[0]=entity.posX+Helper.CRandF(randomness);
		result[1]=entity.posY+Helper.CRandF(randomness);
		result[2]=entity.posZ+Helper.CRandF(randomness);
		
		return result;
	}
	public double[] getMiddleOfBB(){
		double[] result={0,0,0};
		result[0]=/*(BB[3]-BB[0])**/0;
		result[1]=(BB[4]-BB[1])/(entity instanceof EntityPlayer?-2:2);
		result[2]=/*(BB[5]-BB[2])**/0;
		
		return result;
	}
	
	public boolean isDead(){
		isDead=entity.isDead;
		return isDead;
		}
	public boolean isRendering(){
		EntityPlayer p=Minecraft.getMinecraft().thePlayer;
		isRendering=entity.isInRangeToRender3d(p.posX, p.posY, p.posZ);
		return isRendering;
		}
	
//	public void update(Entity entity){
//		EntityClientPlayerMP p=Minecraft.getMinecraft().thePlayer;
//		isRendering=entity.isInRangeToRender3d(p.posX, p.posY, p.posZ);
//		isDead=entity.isDead;
//	}
	
}
