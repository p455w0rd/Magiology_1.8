package com.magiology.mcobjects.blocks;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.magiology.core.MReference;
import com.magiology.objhelper.Get;
import com.magiology.objhelper.helpers.Helper;

public class BlockM extends Block{

	public BlockM(Material material){
		super(material);
	}
	
	//#fucka youuu json models!
	public static Map<Block, ModelResourceLocation> modelsInit=new HashMap<Block, ModelResourceLocation>();
	public BlockM setBlockTextureName(String name){
		modelsInit.put(this, new ModelResourceLocation(name,"inventory"));
		return this;
	}
	public BlockM setBlockTextureName(){
		return setBlockTextureName(MReference.MODID+":"+getUnlocalizedName().substring(5));
	}
	
	public static void registerBlockModels(){
		for(Block block:modelsInit.keySet()){ 
			Item itemBlock=Item.getItemFromBlock(block);
			try{
				Get.Render.RI().getItemModelMesher().register(itemBlock, 0, modelsInit.get(block));
			} catch (Exception e){
				Helper.printInln("failed!",block,itemBlock,Get.Render.RI());
				throw e;
			}
		}
		modelsInit.clear();
	}
	
	@Override
	public BlockM setCreativeTab(CreativeTabs tab){
		return (BlockM)super.setCreativeTab(tab);
	}
}
