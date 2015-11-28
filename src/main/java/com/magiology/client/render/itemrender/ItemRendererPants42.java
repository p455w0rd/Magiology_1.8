package com.magiology.client.render.itemrender;

import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraftforge.client.*;

import org.lwjgl.opengl.*;

import com.magiology.client.render.models.*;

public class ItemRendererPants42 implements IItemRenderer {
	ModelPants42 model=new ModelPants42();

	public ItemRendererPants42(){}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper) {
		return true;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack is, Object... data){
		double xoffset=0,yoffset=0,zoffset=0,xRotation=0,yRotation=0,zRotation=0;
		
		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON){
			xoffset=0.5;
			yoffset=0.8;
			zoffset=0.5;
			yRotation=110;
		}
		yoffset+=0.7;
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glTranslated( xoffset,  yoffset,  zoffset);
		GL11.glRotated(180, 1, 0, 0);
		double scale=0.7;
		GL11.glScaled(scale,scale,scale);
//		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON||type==ItemRenderType.INVENTORY)model.shouldFollowThePlayer=false;
//		else model.shouldFollowThePlayer=true;
		
		GL11.glRotated(xRotation,1,0,0);
		GL11.glRotated(yRotation,0,1,0);
		GL11.glRotated(zRotation,0,0,1);
		GL11.glPushMatrix();
		model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
		GL11.glRotated(zRotation,0,0,1);
		GL11.glRotated(yRotation,0,1,0);
		GL11.glRotated(xRotation,1,0,0);
		
		
		GL11.glScaled(1/scale,1/scale,1/scale);
		GL11.glRotated(-180, 1, 0, 0);
		GL11.glTranslated(-xoffset, -yoffset, -zoffset);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	

}




