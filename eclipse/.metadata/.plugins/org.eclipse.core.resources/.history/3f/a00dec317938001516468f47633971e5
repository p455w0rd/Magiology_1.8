package com.magiology.render.tilerender.hologram;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import com.magiology.mcobjects.tileentityes.hologram.RenderObject;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.modedmcstuff.ColorF;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.TessHelper;

public class RenderHologramProjector extends TileEntitySpecialRenderer{
	
	private TileEntityHologramProjector tile;
	
	@Override
	public void renderTileEntityAt(TileEntity t, double x, double y, double z, float partialTicks){
		tile=(TileEntityHologramProjector)t;
		GL11.glPushMatrix();
		GL11.glTranslated(pos);
		GL11H.texture(false);
		GL11H.lighting(true);
		TessHelper.drawCube(t.blockType.getBlockBoundsMinX(),t.blockType.getBlockBoundsMinY(),t.blockType.getBlockBoundsMinZ(),t.blockType.getBlockBoundsMaxX(),t.blockType.getBlockBoundsMaxY(),t.blockType.getBlockBoundsMaxZ());
		GL11H.SetUpOpaqueRendering(1);
		GL11H.scaled(0.99999);
		ColorF color=new ColorF(Helper.fluctuatorSmooth(10, 0)*0.2+tile.mainColor.xCoord,Helper.fluctuatorSmooth(35, 0)*0.2+tile.mainColor.yCoord,Helper.fluctuatorSmooth(16, 0)*0.2+tile.mainColor.zCoord,0.2);
		color.bind();
		GL11.glTranslatef(tile.offset.x, tile.offset.y-Helper.p*1.45F, 0.5F);
		tile.main.draw();
		GL11.glTranslatef(tile.size.x, tile.size.y, 0);
		
		for(RenderObject ro:tile.renderObjects){
			if(ro.host==null)ro.host=tile;
			GL11.glPushMatrix();
			GL11.glTranslatef(ro.offset.x, ro.offset.y, 0);
			ro.render(color);
			GL11.glPopMatrix();
		}
		GL11H.culFace(true);
		
		GL11H.EndOpaqueRendering();
		GL11H.texture(true);
		GL11.glPopMatrix();
	}
	
}
