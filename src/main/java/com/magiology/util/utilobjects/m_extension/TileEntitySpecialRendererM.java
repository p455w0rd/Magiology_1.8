package com.magiology.util.utilobjects.m_extension;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntitySpecialRendererM extends TileEntitySpecialRenderer{
	public static final float p=1F/16F;
	@Override
	public final void renderTileEntityAt(TileEntity p_180535_1_, double posX, double posY, double posZ, float partialTicks, int p_180535_9_){
		renderTileEntityAt(p_180535_1_, posX, posY, posZ, partialTicks);
	}

	public abstract void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float partialTicks);

}