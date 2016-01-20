package com.magiology.handlers.obj.handler.revived.yayformc1_8.obj;

import java.util.*;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraftforge.fml.relauncher.*;


public class GroupObject
{
  public String name;
  public ArrayList<Face> faces = new ArrayList<Face>();
  public int glDrawingMode;

  public GroupObject()
  {
    this("");
  }

  public GroupObject(String name)
  {
    this(name, -1);
  }

  public GroupObject(String name, int glDrawingMode)
  {
    this.name = name;
    this.glDrawingMode = glDrawingMode;
  }

  @SideOnly(Side.CLIENT)
  public void render()
  {
    if (faces.size() > 0)
    {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.begin(glDrawingMode, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
      render(worldRenderer);
      tessellator.draw();
    }
  }

  @SideOnly(Side.CLIENT)
  public void render(WorldRenderer worldRenderer)
  {
    if (faces.size() > 0)
    {
      for (Face face : faces)
      {
        face.addFaceForRender(worldRenderer);
      }
    }
  }
}