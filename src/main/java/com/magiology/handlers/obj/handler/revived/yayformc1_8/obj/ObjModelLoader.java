package com.magiology.handlers.obj.handler.revived.yayformc1_8.obj;

import net.minecraft.util.*;

import com.magiology.handlers.obj.handler.revived.yayformc1_8.*;

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