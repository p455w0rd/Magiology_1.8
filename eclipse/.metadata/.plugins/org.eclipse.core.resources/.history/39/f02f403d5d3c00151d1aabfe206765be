package com.magiology.objhelper.getters;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;

import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.renderers.NoramlisedVertixBuffer;
import com.magiology.objhelper.helpers.renderers.TessHelper;

public class RenderGet{
	public static ItemRenderer IR(){
		return Minecraft.getMinecraft().getItemRenderer();
	}
	public static RenderItem RI(){
		return Minecraft.getMinecraft().getRenderItem();
	}
	public static NoramlisedVertixBuffer NVB(){
		return TessHelper.getNVB();
	}
	public static WorldRenderer WR(){
		return TessHelper.getWR();
	}
	public static FontRenderer FR(){
		return Helper.getFontRenderer();
	}
}
