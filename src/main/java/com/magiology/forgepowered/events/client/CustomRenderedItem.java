package com.magiology.forgepowered.events.client;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface CustomRenderedItem{
	public void renderItem(ItemStack stack, EntityLivingBase player);
}
