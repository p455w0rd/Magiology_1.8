package com.magiology.util.utilobjects.m_extension;

import java.util.HashMap;
import java.util.Map;

import com.magiology.core.MReference;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class BlockM extends Block{

	public BlockM(Material material){
		super(material);
	}
	
	//#fucka youuu json models!
	public static Map<Block, ResourceLocation> modelsInit=new HashMap<Block, ResourceLocation>();
	
	
	public BlockM setBlockTextureName(String name){
		modelsInit.put(this, new ResourceLocation(name));
		return this;
	}
	public BlockM setBlockTextureName(){
		return setBlockTextureName(MReference.MODID+":"+getUnlocalizedName().substring(5));
	}
	
	public static void registerBlockModels(){
		for(Block block:modelsInit.keySet()){ 
			Item itemBlock=Item.getItemFromBlock(block);
			try{
				Get.Render.RI().getItemModelMesher().register(itemBlock, 0, new ModelResourceLocation(modelsInit.get(block), "inventory"));
			} catch (Exception e){
				PrintUtil.println("failed!",block,itemBlock,Get.Render.RI());
				e.printStackTrace();
				UtilM.exit(404);
			}
		}
//		modelsInit.clear();
	}
	
	@Override
	public BlockM setCreativeTab(CreativeTabs tab){
		return (BlockM)super.setCreativeTab(tab);
	}
}
