package com.magiology.render.tilerender.isbhrrender;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import org.lwjgl.opengl.GL11;

import com.magiology.objhelper.helpers.renderers.ShadedQuad;

public abstract class ISBRH implements ISimpleBlockRenderingHandler,IItemRenderer{
	@Override public boolean handleRenderType(ItemStack item, ItemRenderType type){return true;}
	@Override public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper){return true;}
	@Override public void renderInventoryBlock(Block block, int metadata, int modelId,RenderBlocks renderer){
		
	}
	@Override public boolean shouldRender3DInInventory(int modelId){return false;}
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data){
		ShadedQuad.isInISBRHMode=false;
		GL11.glPushMatrix();
		renderItemInW(type, item, data);
		GL11.glPopMatrix();
	}
	@Override
	public boolean renderWorldBlock(IBlockAccess world, BlockPos pos,Block block, int modelId, RenderBlocks renderer){
		ShadedQuad.isInISBRHMode=true;
		GL11.glPushMatrix();
		boolean result=renderBlockInW(world, pos, block, modelId, renderer);
		GL11.glPopMatrix();
		ShadedQuad.isInISBRHMode=false;
		return result;
	}
	public static void registerBlockRender(Block block,ISBRH renderer){
		RenderingRegistry.registerBlockHandler(renderer);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block),renderer);
	}
	
	@Override
	public abstract int getRenderId();
	public abstract boolean renderBlockInW(IBlockAccess world, BlockPos pos,Block block, int modelId, RenderBlocks renderer);
	public abstract void renderItemInW(ItemRenderType type, ItemStack item, Object... data);
}
