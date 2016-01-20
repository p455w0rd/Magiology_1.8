package com.magiology.util.utilobjects.m_extension;

import java.util.*;

import net.minecraft.client.resources.model.*;
import net.minecraft.item.*;
import net.minecraftforge.client.model.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.core.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.UtilM.U;

public class ItemM extends Item{

	// #fucka youuu json models!
	public static Map<Item, ModelResourceLocation> modelsInit=new HashMap<Item, ModelResourceLocation>();

	public ItemM setTextureName(String name){
		modelsInit.put(this, new ModelResourceLocation(name, "inventory"));
		return this;
	}

	public ItemM setBlockTextureName(){
		return setTextureName(MReference.MODID+":"+getUnlocalizedName().substring(5));
	}

	public static void registerItemModels(){
		for(Item item:modelsInit.keySet()){
			try{
				Get.Render.RI().getItemModelMesher().register(item, 0, modelsInit.get(item));
				((ItemM)item).initModel();
			}catch(Exception e){
				PrintUtil.println("failed!", item, Get.Render.RI());
				e.printStackTrace();
				U.exit(404);
			}
		}
		modelsInit.clear();
	}

	@SideOnly(Side.CLIENT)
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(MReference.MODID+":simpletextureditem", "inventory"));
	}
}
