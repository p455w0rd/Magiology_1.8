package com.magiology.handlers;

import net.minecraft.entity.EntityLivingBase;

import com.magiology.mcobjects.entitys.ClientFakePlayer;
import com.magiology.util.utilclasses.UtilM;

public class CameraHandeler{
	
	private static EntityLivingBase camera;
	
	public static void bind(EntityLivingBase camera){
		if(camera==null)UtilM.getMC().setRenderViewEntity(UtilM.getThePlayer());
		else UtilM.getMC().setRenderViewEntity(camera);
	}
}
