package com.magiology.client.render.tilerender.network;

import net.minecraft.tileentity.*;
import net.minecraft.util.*;

import org.lwjgl.opengl.*;

import com.magiology.api.network.*;
import com.magiology.mcobjects.tileentityes.corecomponents.*;
import com.magiology.util.renderers.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.m_extension.*;

public class RenderNetworkConductor extends TileEntitySpecialRendererM{

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float pt){
		GL11U.protect();
		GL11.glTranslated(x,y,z);
		new ColorF(0.6,0.6,0.6,0.5).bind();
		GL11U.texture(false);
		AxisAlignedBB[] cubes=((MultiColisionProvider)tile).getActiveBoxes();
		if(tile instanceof ISidedNetworkComponent&&((ISidedNetworkComponent)tile).getBrain()==null)GL11U.setUpOpaqueRendering(1);
		else GL11U.endOpaqueRendering();
		for(AxisAlignedBB a:cubes){
			TessUtil.drawCube(a);
		}
		GL11U.endOpaqueRendering();
		GL11U.texture(true);
		GL11U.endProtection();
	}

}
