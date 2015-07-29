package com.magiology.render.aftereffect;

import net.minecraft.client.renderer.Tessellator;

public interface AfterRenderRenderer{
	public static final Tessellator tess=Tessellator.instance;
	public void render();
}
