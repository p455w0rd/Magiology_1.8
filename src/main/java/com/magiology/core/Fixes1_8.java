package com.magiology.core;

import java.lang.reflect.Constructor;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.magiology.util.utilclasses.Get;

public class Fixes1_8{
	//--------------------------------------------------------------------------------------------------------------------------------//
	@SideOnly(value=Side.CLIENT)
	public static void injectStandardJsonModel(Item item, int meta, ModelResourceLocation location){
		Get.Render.RI().getItemModelMesher().register(item, meta, location);
	}
	private static Constructor<TextureAtlasSprite> constr;
	public static TextureAtlasSprite TextureAtlasSprite(ResourceLocation location){
		try{
			if(constr==null){
				constr=TextureAtlasSprite.class.getDeclaredConstructor(String.class);
				constr.setAccessible(true);
			}
			return constr.newInstance(location.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------//
	
}
