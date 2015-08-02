package com.magiology.handelers.obj.handeler.revived.yayformc1_8.obj;

import net.minecraft.util.ResourceLocation;

import com.magiology.handelers.obj.handeler.revived.yayformc1_8.IModelCustom;
import com.magiology.handelers.obj.handeler.revived.yayformc1_8.IModelCustomLoader;
import com.magiology.handelers.obj.handeler.revived.yayformc1_8.ModelFormatException;

public class ObjModelLoader implements IModelCustomLoader
{

  @Override
  public String getType()
  {
    return "OBJ model";
  }

  private static final String[] types = { "obj" };
  @Override
  public String[] getSuffixes()
  {
    return types;
  }

  @Override
  public IModelCustom loadInstance(ResourceLocation resource) throws ModelFormatException
  {
    return new WavefrontObject(resource);
  }
}