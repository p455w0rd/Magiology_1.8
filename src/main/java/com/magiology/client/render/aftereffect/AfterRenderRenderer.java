package com.magiology.client.render.aftereffect;

import net.minecraft.client.renderer.WorldRenderer;

import com.magiology.util.renderers.TessUtil;

public interface AfterRenderRenderer{
	public static final WorldRenderer tess=TessUtil.getWR();
	public void render();
}
