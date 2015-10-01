package com.magiology.handlers.obj.handler.revived.yayformc1_8.techne;

import net.minecraft.util.ResourceLocation;

import com.magiology.handlers.obj.handler.revived.yayformc1_8.IModelCustom;
import com.magiology.handlers.obj.handler.revived.yayformc1_8.IModelCustomLoader;
import com.magiology.handlers.obj.handler.revived.yayformc1_8.ModelFormatException;

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