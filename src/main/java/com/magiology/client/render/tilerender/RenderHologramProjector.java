package com.magiology.client.render.tilerender;

import java.util.*;

import net.minecraft.tileentity.*;

import org.lwjgl.opengl.*;

import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.util.renderers.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.m_extension.*;

public class RenderHologramProjector extends TileEntitySpecialRendererM{
	
	private TileEntityHologramProjector tile;
	
	@Override
	public void renderTileEntityAt(TileEntity t, double x, double y, double z, float partialTicks){
		tile=(TileEntityHologramProjector)t;
//		tile.arraySize.updateValue(tile.holoObjects);
		
		GL11.glPushMatrix();
		GL11.glTranslated(x,y,z);
		GL11U.texture(false);
		GL11U.glLighting(true);
		TessUtil.drawCube(t.getBlockType().getBlockBoundsMinX(),t.getBlockType().getBlockBoundsMinY(),t.getBlockType().getBlockBoundsMinZ(),t.getBlockType().getBlockBoundsMaxX(),t.getBlockType().getBlockBoundsMaxY(),t.getBlockType().getBlockBoundsMaxZ());
		GL11U.setUpOpaqueRendering(1);
		GL11U.glScale(0.99999);
		ColorF color=new ColorF(UtilM.fluctuatorSmooth(10, 0)*0.2+tile.mainColor.x,UtilM.fluctuatorSmooth(35, 0)*0.2+tile.mainColor.y,UtilM.fluctuatorSmooth(16, 0)*0.2+tile.mainColor.z,0.2);
		color.bind();
		GL11.glTranslatef(tile.offset.x, tile.offset.y-UtilM.p*1.45F, 0.5F);
		tile.main.draw();
		GL11.glTranslatef(tile.size.x, tile.size.y, 0);
		boolean selected=false;
		tile.selectedObj=null;
		Iterator<HoloObject> holos=tile.holoObjects.iterator();
		while(holos.hasNext()){
			HoloObject ro=holos.next();
			if(ro.host==null)ro.host=tile;
			GL11.glPushMatrix();
			GL11.glTranslatef(ro.position.x, ro.position.y, 0);
			ro.render(color);
			GL11.glPopMatrix();
			if((
				(ro.getClass()==TextBox.class&&tile.highlighs[0])||
				(ro.getClass()==Button.class&&tile.highlighs[1])||
				(ro.getClass()==Slider.class&&tile.highlighs[3])||
				(ro.getClass()==Field.class&&tile.highlighs[2])
				)&&!selected&&(ro.isHighlighted||ro.moveMode)){
				selected=true;
				GL11.glColor4f(0,0,0,0.4F);
	            GL11.glLineWidth(1);
	            GL11U.texture(false);
	            ro.drawHighlight();
	            GL11U.texture(true);
			}
		}
		GL11U.glColor(ColorF.WHITE);
		GL11U.glCulFace(true);
		
		GL11U.endOpaqueRendering();
		GL11.glPopMatrix();
		
//		tile.arraySize.updateValue(tile.holoObjects);
	}
	
}
