package com.magiology.render.tilerender.network;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider;
import com.magiology.util.renderers.GL11H;
import com.magiology.util.renderers.TessHelper;
import com.magiology.util.utilobjects.ColorF;

public class RenderNetworkConductor extends TileEntitySpecialRenderer{

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float pt,int pass){
		GL11H.protect();
		GL11.glTranslated(x,y,z);
		new ColorF(0.6,0.6,0.6,0.5).bind();
		GL11H.texture(false);
		AxisAlignedBB[] cubes=((MultiColisionProvider)tile).getActiveBoxes();
		if(tile instanceof ISidedNetworkComponent&&((ISidedNetworkComponent)tile).getBrain()==null)GL11H.SetUpOpaqueRendering(1);
		else GL11H.EndOpaqueRendering();
		for(AxisAlignedBB a:cubes){
			TessHelper.drawCube(a);
		}
		GL11H.EndOpaqueRendering();
		GL11H.texture(true);
		GL11H.endProtection();
	}

}
