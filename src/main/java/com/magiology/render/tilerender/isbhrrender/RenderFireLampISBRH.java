package com.magiology.render.tilerender.isbhrrender;

import com.magiology.mcobjects.blocks.FireLamp;
import com.magiology.objhelper.helpers.Helper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public class RenderFireLampISBRH extends ISBRH{
	public static int renderId;
	@Override
	public int getRenderId(){return renderId;}
	public RenderFireLampISBRH(int renderId){RenderFireLampISBRH.renderId=renderId;}
	
	@Override
	public boolean renderBlockInW(IBlockAccess world, int x, int y, int z,Block bleck, int modelId, RenderBlocks renderer){
		FireLamp block=(FireLamp)bleck;
        Tessellator tessellator=Tessellator.instance;
        
        tessellator.setColorOpaque_F(1F, 1F, 1F);
        tessellator.
        setBrightness(
        		block.
        		getMixedBrightnessForBlock(
        				world, x, y, z));
        
        renderer.renderFromInside=true;
        renderer.renderStandardBlock(block, x, y, z);
        
        renderer.renderFromInside=false;
        renderer.renderStandardBlock(block, x, y, z);
        return false;
	}
	@Override
	public void renderItemInW(ItemRenderType type, ItemStack item,Object... data){
		Helper.println("renderItemInW in RenderFireLampISBRH");
	}



}
