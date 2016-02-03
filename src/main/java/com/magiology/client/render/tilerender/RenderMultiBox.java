package com.magiology.client.render.tilerender;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class RenderMultiBox extends TileEntitySpecialRendererM{

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float pt){
		GL11U.protect();
		OpenGLM.translate(x,y,z);
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
