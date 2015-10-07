package com.magiology.client.render.itemrender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.magiology.client.render.Textures;
import com.magiology.core.MReference;
import com.magiology.mcobjects.items.GenericItemUpgrade;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilclasses.Util.U;


public class ItemRendererGenericUpgrade implements IItemRenderer {
	Minecraft mc=U.getMC();
	ResourceLocation texture=null;
	ItemRenderer IR=new ItemRenderer(mc);
	
	public ItemRendererGenericUpgrade(){}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type!=ItemRenderType.INVENTORY;
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper){
		return true;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack is, Object... data){
		texture=Textures.getResource(MReference.MODID,"/textures/items/"+((GenericItemUpgrade)is.getItem()).UT.toString()+"Upgrades.png");
		U.getMC().renderEngine.bindTexture(texture);
		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON||type==ItemRenderType.EQUIPPED){
			double x=0,y=0,z=0,xr=0,yr=0,zr=0;
			if(type==ItemRenderType.EQUIPPED){
//				double angle=((float)H.getMC().theWorld.getTotalWorldTime()%180)*2;
				xr=90;
				yr=45;
				zr=90;
				x=0.3;
				y=-0.9;
				z=0.4;
			}else if(type==ItemRenderType.EQUIPPED_FIRST_PERSON){
				x=0;
				y=0.5;
				z=0.6;
			}
			GL11.glRotated(xr, 1, 0, 0);
			GL11.glRotated(yr, 0, 1, 0);
			GL11.glRotated(zr, 0, 0, 1);
			GL11.glTranslated(x,y,z);
			double time=U.getMC().theWorld.getTotalWorldTime()%180,angle=((time)*2-2)+(2)*Render.partialTicks;
			GL11.glTranslated(0.5, 0, 0);
			GL11.glRotated(angle, 0, 1, 0);
			GL11.glTranslated(-0.5, 0, 0);
			render(is);
			GL11.glTranslated(0.5, 0, 0);
			GL11.glRotated(-angle, 0, 1, 0);
			GL11.glTranslated(-0.5, 0, 0);
			GL11.glTranslated(x, -y, z);
			GL11.glRotated(zr, 0, 0, 1);
			GL11.glRotated(yr, 0, 1, 0);
			GL11.glRotated(xr, 1, 0, 0);
		}else if(type==ItemRenderType.ENTITY){
			double p=1.0/16.0;
			double transx=p*8,transy=0.2,transz=-p/2;
			
			GL11.glTranslated(-transx, -transy, -transz);
			render(is);
			GL11.glTranslated(transx, transy, transz);
		}
	}
	public void render(ItemStack is){
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.99F);
		float width=0.0625F;
		GL11.glPushMatrix();
		if(((GenericItemUpgrade)is.getItem()).UT.toString().equals("Speed")){
			width*=0.5;
			GL11.glTranslated(0, 0, -width/2);
		}
		Render.RI().renderItemModel(is);
		GL11.glPopMatrix();
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.99F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glColor4d(1, 1, 1, 0.2);
		Render.RI().renderItemModel(is);
		GL11.glColor4d(1, 1, 1, 1);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(true);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
	}
	
	
}



