package com.magiology.handlers.obj.handler.revived.yayformc1_8;

import net.minecraftforge.fml.relauncher.*;

public interface IModelCustom
{
  String getType();
  @SideOnly(Side.CLIENT)
  void renderAll();
  @SideOnly(Side.CLIENT)
  void renderOnly(String... groupNames);
  @SideOnly(Side.CLIENT)
  void renderPart(String partName);
  @SideOnly(Side.CLIENT)
  void renderAllExcept(String... excludedGroupNames);
}