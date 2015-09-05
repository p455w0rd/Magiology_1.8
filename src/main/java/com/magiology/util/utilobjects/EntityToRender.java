package com.magiology.util.utilobjects;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

public class EntityToRender{
	
	public Entity entity;
	public double var1, var2, var3;
	public float var4, partialTicks;
	public Render render;
	
	public EntityToRender(Entity entity, double var1,double var2, double var3, float var4,float partialTicks, Render render){
		if(render instanceof EntityToRenderInt);else return;
		this.render=render;
		this.entity=entity;
		this.var1=var1;
		this.var2=var2;
		this.var3=var3;
		this.var4=var4;
		this.partialTicks=partialTicks;
	}
	
	public interface EntityToRenderInt{
		public void doRenderInPlace(Entity en, double var1,double var2, double var3, float var4,float partialTicks);
	}
}
