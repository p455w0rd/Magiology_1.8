package com.magiology.mcobjects.items;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

import com.magiology.core.Fixes1_8;
import com.magiology.core.MReference;
import com.magiology.objhelper.Get;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;

public class ItemM extends Item{
		
		//#fucka youuu json models!
		public static Map<Item, ModelResourceLocation> modelsInit=new HashMap<Item, ModelResourceLocation>();
		public ItemM setTextureName(String name){
			modelsInit.put(this, new ModelResourceLocation(name,"inventory"));
			return this;
		}
		public ItemM setBlockTextureName(){
			return setTextureName(MReference.MODID+":"+getUnlocalizedName().substring(5));
		}
		
		public static void registerItemModels(){
			for(Item item:modelsInit.keySet()){
				try{
//					Get.Render.RI().getItemModelMesher().register(item, 0, modelsInit.get(item));
					Fixes1_8.injectStandardJsonModel(item, 0, modelsInit.get(item));
				} catch (Exception e){
					Helper.printInln("failed!",item,Get.Render.RI());
					e.printStackTrace();
					H.exit(404);
				}
			}
			modelsInit.clear();
		}
}
