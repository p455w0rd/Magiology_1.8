package com.magiology.gui.custom.hud;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;

import com.magiology.util.renderers.TessHelper;

public abstract class HUD{
	public EntityPlayer player;
	public abstract void render(int xScreen,int yScreen,float partialTicks);
	public static void drawRect(float xPngSize,float yPngSize,double xPos, double yPos, double xTextureOffset, double yTextureOffset, double xSize, double ySize){
        WorldRenderer renderer = TessHelper.getWR();
        renderer.startDrawingQuads();
        renderer.addVertexWithUV(xPos + 0, yPos + ySize, 0, (float)(xTextureOffset + 0) * xPngSize, (float)(yTextureOffset + ySize) * yPngSize);
        renderer.addVertexWithUV(xPos + xSize, yPos + ySize, 0, (float)(xTextureOffset + xSize) * xPngSize, (float)(yTextureOffset + ySize) * yPngSize);
        renderer.addVertexWithUV(xPos + xSize, yPos + 0, 0, (float)(xTextureOffset + xSize) * xPngSize, (float)(yTextureOffset + 0) * yPngSize);
        renderer.addVertexWithUV(xPos + 0, yPos + 0, 0, (float)(xTextureOffset + 0) * xPngSize, (float)(yTextureOffset + 0) * yPngSize);
        TessHelper.draw();
	}
	public void update(){}
}
