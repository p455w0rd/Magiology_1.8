package com.magiology.core;

import java.util.Map;

import scala.annotation.meta.getter;

import com.google.common.collect.Maps;
import com.magiology.objhelper.DataStalker;
import com.magiology.objhelper.Get;

import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Fixes1_8{
	//--------------------------------------------------------------------------------------------------------------------------------//
	private static Map simpleShapes,simpleShapesCache;
	
	private static int getIndex(Item item, int meta){
        return Item.getIdFromItem(item) << 16 | meta;
    }
	@SideOnly(value=Side.CLIENT)
	public static void injectStandardJsonModel_Block(Item item, int meta, ModelResourceLocation location){
		if(simpleShapes==null){
			simpleShapes=DataStalker.getVariable(ItemModelMesher.class, "simpleShapes", Get.Render.RI().getItemModelMesher());
			simpleShapesCache=DataStalker.getVariable(ItemModelMesher.class, "simpleShapesCache", Get.Render.RI().getItemModelMesher());
		}
		simpleShapes.put(Integer.valueOf(getIndex(item, meta)), location);
        simpleShapesCache.put(Integer.valueOf(getIndex(item, meta)), Get.Render.RI().getItemModelMesher().getModelManager().getModel(location));
	}
	//--------------------------------------------------------------------------------------------------------------------------------//
	
}
