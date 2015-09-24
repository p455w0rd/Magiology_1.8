package com.magiology.gui.custom.hud;

import net.minecraft.entity.player.EntityPlayer;

import com.magiology.util.renderers.tessellatorscripts.Drawer;

public abstract class HUD{
	public EntityPlayer player;
	public abstract void render(int xScreen,int yScreen,float partialTicks);
	public static void drawRect(float xPngSize,float yPngSize,double xPos, double yPos, double xTextureOffset, double yTextureOffset, double xSize, double ySize){
        Drawer.startDrawingQuads();
        Drawer.addVertexWithUV(xPos + 0, yPos + ySize, 0, (float)(xTextureOffset + 0) * xPngSize, (float)(yTextureOffset + ySize) * yPngSize);
        Drawer.addVertexWithUV(xPos + xSize, yPos + ySize, 0, (float)(xTextureOffset + xSize) * xPngSize, (float)(yTextureOffset + ySize) * yPngSize);
        Drawer.addVertexWithUV(xPos + xSize, yPos + 0, 0, (float)(xTextureOffset + xSize) * xPngSize, (float)(yTextureOffset + 0) * yPngSize);
        Drawer.addVertexWithUV(xPos + 0, yPos + 0, 0, (float)(xTextureOffset + 0) * xPngSize, (float)(yTextureOffset + 0) * yPngSize);
        Drawer.draw();
	}
	public void update(){}
}
