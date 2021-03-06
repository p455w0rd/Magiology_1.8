package com.magiology.util.utilclasses;

import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;

public class RayTracer{

	public static MovingObjectPosition rayTrace(EntityLivingBase entity,float lenght, float var1){
		if(entity.worldObj.isRemote)return entity.rayTrace(lenght, var1);
		
		Vec3M vec3 =new Vec3M(entity.posX, entity.posY, entity.posZ);
		Vec3M vec31=Vec3M.conv(entity.getLook(var1));
		Vec3M vec32=vec3.add(vec31.x*var1, vec31.y*var1, vec31.z*var1);
		return entity.worldObj.rayTraceBlocks(vec3.conv(), vec32.conv(), false, false, true);
	}

}
