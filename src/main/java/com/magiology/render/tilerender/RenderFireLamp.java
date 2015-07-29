package com.magiology.render.tilerender;

import com.magiology.forgepowered.event.RenderLoopEvents;
import com.magiology.mcobjects.tileentityes.TileEntityFireLamp;
import com.magiology.objhelper.helpers.PowerHelper;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.TessHelper;
import com.magiology.objhelper.vectors.TwoDots;
import com.magiology.render.aftereffect.LongAfterRenderRenderer;
import com.magiology.render.aftereffect.TwoDotsLineRender;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class RenderFireLamp extends TileEntitySpecialRenderer {

	private final float p= 1F/16F;
	private final float tW=1F/64F;
	private final float tH=1F/64F;
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f){
		TileEntityFireLamp tileFL=(TileEntityFireLamp) tile;
		boolean var1=true;
		for(int a=0;a<RenderLoopEvents.universalLongRender.size();a++){
			LongAfterRenderRenderer ab=RenderLoopEvents.universalLongRender.get(a);
			if(ab instanceof TwoDotsLineRender&&!((TwoDotsLineRender)ab).isDead())if(((TwoDotsLineRender)ab).tile==tileFL)var1=false;
		}
		if(var1)RenderLoopEvents.spawnLARR(new TwoDotsLineRender(new TwoDots(tileFL.xCoord+0.5, tileFL.yCoord+0.5, tileFL.zCoord+0.5, tileFL.controlX+0.5, tileFL.controlY+0.5, tileFL.controlZ+0.5),tileFL));
		float FP=PowerHelper.getFuelPrecentage(tileFL),a=FP*10-1;
		if(a>1)a=1;
		else if(a<0)a=0;
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11H.SetUpOpaqueRendering(1);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(false);
		TessHelper.drawBlurredCube((int)x, (int)y, (int)z, p*4.5,0.01,p*4.5,p*11.5,p*11*FP,p*11.5, 15, 0.03, 1,0.1,0.1, 0.5*a);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11H.EndOpaqueRendering();
		GL11.glColor4d(1,1,1,1);
		GL11.glEnable(GL11.GL_LIGHTING);
		}
	
	
}