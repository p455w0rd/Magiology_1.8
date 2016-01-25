package com.magiology.client.gui.gui;

import net.minecraft.client.renderer.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import com.magiology.core.*;
import com.magiology.util.renderers.*;
import com.magiology.util.renderers.tessellatorscripts.*;

public class CraftingGridWOutput{
	public static ResourceLocation txt=new ResourceLocation(MReference.MODID,"/textures/gui/CraftingGridWproduct.png");
	public ItemStack[] product=new ItemStack[1];
	public ItemStack[] grid=new ItemStack[9];
	public int ammountWanted;
	public CraftingGridWOutput(ItemStack product,int ammountWanted,ItemStack[] grid){
		this.product=new ItemStack[]{product};
		if(grid!=null)this.grid=grid;
		this.ammountWanted=ammountWanted;
	}
	public void render(){
		TessUtil.bindTexture(txt);
		drawRect(0, 0, 0, 0, 93, 56);
		
	}
	protected void drawRect(float x, float y,float tx, float yt, float xp, float yp){
		 float f = 1F/102F;
	     float f1 = 1F/56F;
	     Renderer.POS_UV.beginQuads();
	     Renderer.POS_UV.addVertex((x+0),(y+yp),0,((tx+0)*f),((yt+yp)*f1));
	     Renderer.POS_UV.addVertex((x+xp),(y+yp),0,((tx+xp)*f),((yt+yp)*f1));
	     Renderer.POS_UV.addVertex((x+xp),(y+0),0,((tx+xp)*f),((yt+0)*f1));
	     Renderer.POS_UV.addVertex((x+0),(y+0),0,((tx+0)*f),((yt+0)*f1));
	     Renderer.POS_UV.draw();
	}
	public void clear(){
		product[0]=null;
		for(int a=0;a<9;a++)grid[a]=null;
	}
}
