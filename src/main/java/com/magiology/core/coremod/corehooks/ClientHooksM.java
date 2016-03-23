package com.magiology.core.coremod.corehooks;

import com.magiology.forgepowered.events.client.CustomRenderedItem;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHooksM extends CommonHooksM{
	
	public static boolean renderItem(ItemStack stack, EntityLivingBase entity, ItemCameraTransforms.TransformType transform){
		if(stack!=null){
			Item item=stack.getItem();
			if(item instanceof CustomRenderedItem){
				CustomRenderedItem renderer=(CustomRenderedItem)item;
				if(renderer.shouldRender(stack, entity, transform)){
					renderer.renderItem(stack, entity, transform);
					return false;
				}
			}
		}
		return true;
	}
}
