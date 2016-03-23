package com.magiology.core.coremod.transformers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.magiology.core.coremod.ClassTransformerBase;
import com.magiology.util.utilclasses.PrintUtil;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class NativeItemRednerHook extends ClassTransformerBase{
	
	@Override
	public String[] getTransformingClasses(){
		return new String[]{
				"net.minecraft.client.renderer.ItemRenderer"
		};
	}

	@Override
	public void transform(ClassNode itemRendererClass){
		MethodNode renderItemMethod=findMethod(itemRendererClass, renderItemFunc);
		
		
		
	}
	
	public static boolean renderItem(EntityLivingBase entityIn, ItemStack heldStack, TransformType transform){
		PrintUtil.println(heldStack,"is rendered via asm!!!");
		return false;
	}
}
