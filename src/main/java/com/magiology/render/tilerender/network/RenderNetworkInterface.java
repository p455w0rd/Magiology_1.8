package com.magiology.render.tilerender.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider;
import com.magiology.util.renderers.GL11H;
import com.magiology.util.renderers.TessHelper;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

public class RenderNetworkInterface extends TileEntitySpecialRendererM{

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float pt){
		GL11.glPushMatrix();
		GL11.glTranslated(x,y,z);
		new ColorF(0.2,0.6,0.2,0.5).bind();
		GL11H.texture(false);
		AxisAlignedBB[] cubes=((MultiColisionProvider)tile).getActiveBoxes();
		if(((ISidedNetworkComponent)tile).getBrain()==null)GL11H.SetUpOpaqueRendering(1);
		else GL11H.EndOpaqueRendering();
		for(AxisAlignedBB a:cubes){
			TessHelper.drawCube(a);
		}
		GL11H.EndOpaqueRendering();
		GL11H.texture(true);
		GL11.glPopMatrix();
	}

}
