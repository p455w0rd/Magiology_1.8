package com.magiology.forgepowered.events.client;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface CustomRenderedItem{
	public void renderItem(ItemStack stack, EntityLivingBase entity, TransformType transform);
	public boolean shouldRender(ItemStack stack, EntityLivingBase entity, TransformType transform);
}
