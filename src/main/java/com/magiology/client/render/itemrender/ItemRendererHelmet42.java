package com.magiology.client.render.itemrender;

import net.minecraft.entity.*;
import net.minecraft.item.*;

import org.lwjgl.opengl.*;

import com.magiology.client.render.models.*;

//public class ItemRendererHelmet42 implements IItemRenderer{
//	public ModelHelmet42 model=new ModelHelmet42();
//
//	public ItemRendererHelmet42(){}
//	
//	@Override
//	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
//		return true;
//	}
//
//	@Override
//	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper) {
//		return helper!=ItemRendererHelper.ENTITY_ROTATION;
//	}
//	
//	@Override
//	public void renderItem(ItemRenderType type, ItemStack is, Object... data){
//		double xoffset=0,yoffset=0,zoffset=0,xRotation=0,yRotation=0,zRotation=0;
//		
//		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON){
//			xoffset=0.5;
//			yoffset=0.8;
//			zoffset=0.5;
//			xRotation=-25;
//			yRotation=-130;
//			zRotation=-12.5;
//		}else if (type==ItemRenderType.INVENTORY){
//			yRotation=80;
//		}
//		GL11.glPushMatrix();
//		GL11.glTranslated( xoffset,  yoffset,  zoffset);
//		GL11.glRotated(180, 1, 0, 0);
//		double scale=0.7;
//		GL11.glScaled(scale,scale,scale);
//		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON||type==ItemRenderType.INVENTORY)model.shouldFollowThePlayer=false;
//		else model.shouldFollowThePlayer=true;
//		
//		GL11.glRotated(xRotation,1,0,0);
//		GL11.glRotated(yRotation,0,1,0);
//		GL11.glRotated(zRotation,0,0,1);
//		model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
//		GL11.glPopMatrix();
//	}
//	
//	
//
//}




