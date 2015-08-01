package com.magiology.core.init;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.magiology.core.Magiology;
import com.magiology.mcobjects.entitys.EntityBallOfEnergy;
import com.magiology.mcobjects.entitys.EntitySubatomicWorldDeconstructor;
import com.magiology.mcobjects.entitys.ModedEntityFallingBlock;
import com.magiology.render.entityrender.BallOfEnergyRenderer;
import com.magiology.render.entityrender.EntitySubatomicWorldDeconstructorRenderer;
import com.magiology.render.entityrender.RenderModedFallingBlock;
import com.magiology.render.models.entitys.BallOfEnergyModel;

public class MEntitys{

	private static int id=1;

	public static void init(){
		reg(EntityBallOfEnergy.class, 100, 1, true,new BallOfEnergyRenderer(new BallOfEnergyModel(),1));
		reg(ModedEntityFallingBlock.class, 100, 1, true,new RenderModedFallingBlock());
		reg(EntitySubatomicWorldDeconstructor.class, 100, 1, true,new EntitySubatomicWorldDeconstructorRenderer(10));
	}

	private static void reg(Class<? extends Entity> entityClass, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates,Render model){
		EntityRegistry.registerModEntity(entityClass, entityClass.getClass().getCanonicalName(), id, Magiology.getMagiology(),  trackingRange, updateFrequency, sendsVelocityUpdates);
		RenderingRegistry.registerEntityRenderingHandler(entityClass, model);
		id++;
	}

	private static void reg(Class<? extends Entity> entityClass, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates){
		EntityRegistry.registerModEntity(entityClass, entityClass.getClass().getCanonicalName(), id, Magiology.getMagiology(),  trackingRange, updateFrequency, sendsVelocityUpdates);
		id++;
	}

}
