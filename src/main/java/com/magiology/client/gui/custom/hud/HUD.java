package com.magiology.client.gui.custom.hud;

import net.minecraft.entity.player.*;

import com.magiology.util.renderers.*;
import com.magiology.util.renderers.tessellatorscripts.*;

public abstract class HUD{
	public EntityPlayer player;
	public abstract void render(int xScreen,int yScreen,float partialTicks);
	public static void drawRect(float xPngSize,float yPngSize,double xPos, double yPos, double xTextureOffset, double yTextureOffset, double xSize, double ySize){
        Renderer.beginQuads();
        Renderer.addVertexData(xPos + 0, yPos + ySize, 0, (float)(xTextureOffset + 0) * xPngSize, (float)(yTextureOffset + ySize) * yPngSize).endVertex();
        Renderer.addVertexData(xPos + xSize, yPos + ySize, 0, (float)(xTextureOffset + xSize) * xPngSize, (float)(yTextureOffset + ySize) * yPngSize).endVertex();
        Renderer.addVertexData(xPos + xSize, yPos + 0, 0, (float)(xTextureOffset + xSize) * xPngSize, (float)(yTextureOffset + 0) * yPngSize).endVertex();
        Renderer.addVertexData(xPos + 0, yPos + 0, 0, (float)(xTextureOffset + 0) * xPngSize, (float)(yTextureOffset + 0) * yPngSize).endVertex();
        Renderer.draw();
	}
	public void update(){}
}
