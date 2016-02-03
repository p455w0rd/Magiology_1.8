package com.magiology.util.utilobjects.m_extension;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntitySpecialRendererM extends TileEntitySpecialRenderer{
	public static final float p=1F/16F;
	@Override
	public final void renderTileEntityAt(TileEntity tile, double posX, double posY, double posZ, float partialTicks, int IDuannoMaybeRenderPass){
		renderTileEntityAt(tile, posX, posY, posZ, partialTicks);
	}

	public abstract void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float partialTicks);

}
