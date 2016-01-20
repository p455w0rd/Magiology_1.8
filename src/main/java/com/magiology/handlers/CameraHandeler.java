package com.magiology.handlers;

import net.minecraft.entity.*;

import com.magiology.util.utilclasses.*;

public class CameraHandeler{
	
	private static EntityLivingBase camera;
	
	public static void bind(EntityLivingBase camera){
		if(camera==null)UtilM.getMC().setRenderViewEntity(UtilM.getThePlayer());
		else UtilM.getMC().setRenderViewEntity(camera);
	}
}
