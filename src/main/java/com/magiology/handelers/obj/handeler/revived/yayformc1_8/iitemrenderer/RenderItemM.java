package com.magiology.handelers.obj.handeler.revived.yayformc1_8.iitemrenderer;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.magiology.objhelper.DataStalker;
import com.magiology.objhelper.Get;
import com.magiology.objhelper.helpers.Helper.H;

public class RenderItemM extends RenderItem{
	TextureManager textureManagerM;
	public RenderItemM(TextureManager textureManager, ModelManager modelManager){
		super(textureManager, modelManager);
		textureManagerM=textureManager;
	}
	@Override
	protected void renderItemModelTransform(ItemStack stack, IBakedModel model, ItemCameraTransforms.TransformType cameraTransformType)
    {
		
        this.textureManagerM.bindTexture(TextureMap.locationBlocksTexture);
        this.textureManagerM.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        this.preTransform(stack);
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();

        model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, cameraTransformType);

        this.renderItem(stack, model);
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.textureManagerM.bindTexture(TextureMap.locationBlocksTexture);
        this.textureManagerM.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
    }
	private void preTransform(ItemStack stack)
    {
        IBakedModel ibakedmodel = this.getItemModelMesher().getItemModel(stack);
        Item item = stack.getItem();

        if (item != null)
        {
            boolean flag = ibakedmodel.isGui3d();

            if (!flag)
            {
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
	public static void castRenderItemToRenderItemM(){
//		Helper.printInln(Get.Render.RI());
		try {
			Field ir=Minecraft.class.getDeclaredField("renderItem");
			ir.setAccessible(true);
//			Helper.printInln("renderItem loaded!");
			TextureManager tm=DataStalker.getVariable(RenderItem.class, "textureManager", Get.Render.RI());
//			Helper.printInln("textureManager loaded!");
			ItemModelMesher imm=DataStalker.getVariable(RenderItem.class, "itemModelMesher", Get.Render.RI());
//			Helper.printInln("itemModelMesher loaded!");
			ir.set(H.getMC(), new RenderItemM(tm, imm.getModelManager()));
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Helper.printInln(Get.Render.RI());
//		Helper.exit(404);
	}
}
