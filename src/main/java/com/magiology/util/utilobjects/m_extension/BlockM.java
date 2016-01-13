package com.magiology.util.utilobjects.m_extension;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import com.magiology.core.*;
import com.magiology.util.utilclasses.*;

public class BlockM extends Block{

	public BlockM(Material material){
		super(material);
	}
	
	//#fucka youuu json models!
	public static Map<Block, ResourceLocation> modelsInit=new HashMap<Block, ResourceLocation>();
	public static IBakedModel[] models,invmodels;
	
	
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
