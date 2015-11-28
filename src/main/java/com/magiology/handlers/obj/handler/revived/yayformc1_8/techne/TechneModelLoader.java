package com.magiology.handlers.obj.handler.revived.yayformc1_8.techne;

import net.minecraft.util.*;

import com.magiology.handlers.obj.handler.revived.yayformc1_8.*;

public class TechneModelLoader implements IModelCustomLoader {

  @Override
  public String getType()
  {
    return "Techne model";
  }

  private static final String[] types = { "tcn" };
  @Override
  public String[] getSuffixes()
  {
    return types;
  }

  @Override
  public IModelCustom loadInstance(ResourceLocation resource) throws ModelFormatException
  {
    return new TechneModel(resource);
  }

}