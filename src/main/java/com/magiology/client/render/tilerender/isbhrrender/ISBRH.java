package com.magiology.client.render.tilerender.isbhrrender;


//public abstract class ISBRH implements ISimpleBlockRenderingHandler,IItemRenderer{
//	@Override public boolean handleRenderType(ItemStack item, ItemRenderType type){return true;}
//	@Override public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper){return true;}
//	@Override public void renderInventoryBlock(Block block, int metadata, int modelId,RenderBlocks renderer){
//		
//	}
//	@Override public boolean shouldRender3DInInventory(int modelId){return false;}
//	@Override
//	public void renderItem(ItemRenderType type, ItemStack item, Object... data){
//		ItemRenderer
//		GL11.glPushMatrix();
//		renderItemInW(type, item, data);
//		GL11.glPopMatrix();
//	}
//	@Override
//	public boolean renderWorldBlock(IBlockAccess world, BlockPos pos,Block block, int modelId, RenderBlocks renderer){
//		GL11.glPushMatrix();
//		boolean result=renderBlockInW(world, pos, block, modelId, renderer);
//		GL11.glPopMatrix();
//		return result;
//	}
//	public static void registerBlockRender(Block block,ISBRH renderer){
//		RenderingRegistry.registerBlockHandler(renderer);
//		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block),renderer);
//	}
//	
//	@Override
//	public abstract int getRenderId();
//	public abstract boolean renderBlockInW(IBlockAccess world, BlockPos pos,Block block, int modelId, RenderBlocks renderer);
//	public abstract void renderItemInW(ItemRenderType type, ItemStack item, Object... data);
//}