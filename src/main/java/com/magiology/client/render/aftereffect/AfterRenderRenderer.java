package com.magiology.client.render.aftereffect;

import net.minecraft.client.renderer.*;

import com.magiology.util.renderers.*;

public interface AfterRenderRenderer{
	public static final WorldRenderer tess=TessUtil.getWR();
	public void render();
}
