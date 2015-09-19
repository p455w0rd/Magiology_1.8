package com.magiology.render.tilerender.hologram;

import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import com.magiology.mcobjects.tileentityes.hologram.HoloObject;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

public class RenderHologramProjector extends TileEntitySpecialRendererM{
	
	private TileEntityHologramProjector tile;
	
	@Override
	public void renderTileEntityAt(TileEntity t, double x, double y, double z, float partialTicks){
		tile=(TileEntityHologramProjector)t;
		GL11.glPushMatrix();
		GL11.glTranslated(x,y,z);
		GL11U.texture(false);
		GL11U.lighting(true);
		TessUtil.drawCube(t.getBlockType().getBlockBoundsMinX(),t.getBlockType().getBlockBoundsMinY(),t.getBlockType().getBlockBoundsMinZ(),t.getBlockType().getBlockBoundsMaxX(),t.getBlockType().getBlockBoundsMaxY(),t.getBlockType().getBlockBoundsMaxZ());
		GL11U.SetUpOpaqueRendering(1);
		GL11U.scaled(0.99999);
		ColorF color=new ColorF(Util.fluctuatorSmooth(10, 0)*0.2+tile.mainColor.x,Util.fluctuatorSmooth(35, 0)*0.2+tile.mainColor.y,Util.fluctuatorSmooth(16, 0)*0.2+tile.mainColor.z,0.2);
		color.bind();
		GL11.glTranslatef(tile.offset.x, tile.offset.y-Util.p*1.45F, 0.5F);
		tile.main.draw();
		GL11.glTranslatef(tile.size.x, tile.size.y, 0);
		boolean selected=false;
		NormalizedVertixBuffer buff=TessUtil.getNVB();
		for(HoloObject ro:tile.holoObjects){
			if(ro.host==null)ro.host=tile;
			GL11.glPushMatrix();
			GL11.glTranslatef(ro.offset.x, ro.offset.y, 0);
			ro.render(color);
			GL11.glPopMatrix();
			if(!selected&&(ro.isHighlighted||ro.moveMode)){
				selected=true;
				GL11.glColor4f(0,0,0,0.4F);
	            GL11.glLineWidth(1);
	            GL11U.texture(false);
				
				buff.pushMatrix();
				buff.translate(ro.offset.x, ro.offset.y, 0);
				buff.cleanUp();
				
				ComplexCubeModel selection=new ComplexCubeModel(0, 0, -Util.p/2, -ro.size.x, -ro.size.y, Util.p/2).expand(0.002F);
				buff.importComplexCube(selection);
				buff.setDrawModeToWireFrame();
				buff.draw();
				buff.setDrawModeToQuadPlate();
				buff.popMatrix();

	            GL11U.texture(true);
			}
		}
		GL11U.color(ColorF.WHITE);
		GL11U.culFace(true);
		
		GL11U.EndOpaqueRendering();
		GL11U.texture(true);
		GL11.glPopMatrix();
	}
	
}
