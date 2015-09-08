package com.magiology.gui.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

import com.magiology.gui.container.ContainerEmpty;
import com.magiology.gui.guiutil.gui.buttons.CleanButton;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.util.utilobjects.ColorF;

public class GuiHologramProjectorMain extends GuiContainer{
	
	EntityPlayer player;
	TileEntityHologramProjector tile;
	
	public GuiHologramProjectorMain(EntityPlayer player,TileEntityHologramProjector tile){
		super(new ContainerEmpty());
		this.player=player;
		this.tile=tile;
		this.xSize=100;
		this.ySize=50;
	}
		

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		
	}
	@Override
	public void initGui(){
		super.initGui();
		int ys=ySize/3;
		buttonList.add(new CleanButton(0, guiLeft, guiTop,      xSize, ys, "lolz", new ColorF(0.8, 0.2, 0.2, 0.8)));
		buttonList.add(new CleanButton(0, guiLeft, guiTop+ys,   xSize, ys, "lolz", new ColorF(0.2, 0.8, 0.2, 0.8)));
		buttonList.add(new CleanButton(0, guiLeft, guiTop+ys*2, xSize, ys, "lolz", new ColorF(0.2, 0.2, 0.8, 0.8)));
	}
}
