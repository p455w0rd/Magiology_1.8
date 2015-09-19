package com.magiology.gui.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import com.magiology.core.MReference;
import com.magiology.gui.GuiUpdater.Updateable;
import com.magiology.gui.container.CommandCenterContainer;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkCommandCenter;
import com.magiology.util.renderers.TessUtil;

public class GuiCenterContainer extends GuiContainer implements Updateable{
	
	ResourceLocation main= new ResourceLocation(MReference.MODID,"/textures/gui/GuiControlBock.png");
	ResourceLocation EnergyBar=new ResourceLocation(MReference.MODID,"/textures/models/PowerCounter/EnergyBar.png");
	
	private TileEntityNetworkCommandCenter tile;
	
	public GuiCenterContainer(EntityPlayer player,TileEntityNetworkCommandCenter tile){
		super(new CommandCenterContainer(player,tile));
		this.tile=tile;
		this.xSize=176;
		this.ySize=166;
		
	}
	@Override
	public void drawGuiContainerForegroundLayer(int a,int b){
		
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float v1, int v2, int v3){
		TessUtil.bindTexture(main);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
	}
	
	@Override
	public void initGui(){
		super.initGui();
	}
	 
	 @Override
	 protected void actionPerformed(GuiButton b){
		 
		 switch (b.id){
		 case 1:{
			 
		 }break;
		 case 2:{
			 
		 }break;
		 
		 }
	 }
	 
	 @Override
	 public void update(){
		 
		 
		 
	 }
}