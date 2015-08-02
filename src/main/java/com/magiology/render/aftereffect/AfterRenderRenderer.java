package com.magiology.render.aftereffect;

import net.minecraft.client.renderer.WorldRenderer;

import com.magiology.objhelper.helpers.renderers.TessHelper;

public interface AfterRenderRenderer{
	public static final WorldRenderer tess=TessHelper.getWR();
	public void render();
}
