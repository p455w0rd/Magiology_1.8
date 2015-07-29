package com.magiology.gui.fpgui;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;

public abstract class FirstPersonGui{
	public EntityPlayer player;
	public abstract void render(int xScreen,int yScreen,float partialTicks);
	public static void drawRect(float xPngSize,float yPngSize,double xPos, double yPos, double xTextureOffset, double yTextureOffset, double xSize, double ySize){
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(xPos + 0, yPos + ySize, 0, (float)(xTextureOffset + 0) * xPngSize, (float)(yTextureOffset + ySize) * yPngSize);
        tessellator.addVertexWithUV(xPos + xSize, yPos + ySize, 0, (float)(xTextureOffset + xSize) * xPngSize, (float)(yTextureOffset + ySize) * yPngSize);
        tessellator.addVertexWithUV(xPos + xSize, yPos + 0, 0, (float)(xTextureOffset + xSize) * xPngSize, (float)(yTextureOffset + 0) * yPngSize);
        tessellator.addVertexWithUV(xPos + 0, yPos + 0, 0, (float)(xTextureOffset + 0) * xPngSize, (float)(yTextureOffset + 0) * yPngSize);
        tessellator.draw();
	}
	public void update(){}
}
