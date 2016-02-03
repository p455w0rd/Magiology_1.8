package com.magiology.handlers;

import com.magiology.util.utilclasses.UtilM;

import net.minecraft.entity.EntityLivingBase;

public class CameraHandeler{
	
	private static EntityLivingBase camera;
	
	public static void bind(EntityLivingBase camera){
		if(camera==null)UtilM.getMC().setRenderViewEntity(UtilM.getThePlayer());
		else UtilM.getMC().setRenderViewEntity(camera);
	}
}
