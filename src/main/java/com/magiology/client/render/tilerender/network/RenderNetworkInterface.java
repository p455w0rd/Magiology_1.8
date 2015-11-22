package com.magiology.client.render.tilerender.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

public class RenderNetworkInterface extends TileEntitySpecialRendererM{

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float pt){
		GL11U.protect();
		GL11.glTranslated(x,y,z);
		new ColorF(0.2,0.6,0.2,0.5).bind();
		GL11U.texture(false);
		AxisAlignedBB[] cubes=((MultiColisionProvider)tile).getActiveBoxes();
		if(((ISidedNetworkComponent)tile).getBrain()==null)GL11U.setUpOpaqueRendering(1);
		else GL11U.endOpaqueRendering();
		for(AxisAlignedBB a:cubes){
			TessUtil.drawCube(a);
		}
		GL11U.endOpaqueRendering();
		GL11U.texture(true);
		GL11U.endProtection();
	}

}
