package com.magiology.gui.gui;

import java.awt.event.KeyEvent;
import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.updateable.Updater;
import com.magiology.core.Magiology;
import com.magiology.forgepowered.packets.packets.RenderObjectUploadPacket;
import com.magiology.gui.GuiUpdater.Updateable;
import com.magiology.gui.container.ContainerEmpty;
import com.magiology.gui.guiutil.gui.buttons.CleanButton;
import com.magiology.mcobjects.tileentityes.hologram.Button;
import com.magiology.mcobjects.tileentityes.hologram.TextBox;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.ColorF;

public class GuiHologramProjectorMain extends GuiContainer implements Updateable{
	
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
		buttonList.add(new CleanButton(0, guiLeft, guiTop,      xSize, ys, "Text Box", new ColorF(0.8, 0.2, 0.2, 0.8)));
		buttonList.add(new CleanButton(1, guiLeft, guiTop+ys+1,   xSize, ys, "Button", new ColorF(0.2, 0.8, 0.2, 0.8)));
		buttonList.add(new CleanButton(2, guiLeft, guiTop+ys*2+2, xSize, ys, "something else", new ColorF(0.2, 0.2, 0.8, 0.8)));
	}


	@Override
	public void update(){
		Updater.update(buttonList);
	}
	@Override
	protected void actionPerformed(GuiButton button)throws IOException{
		switch(button.id){
		case 0:{
			U.sendMessage(new RenderObjectUploadPacket(new TextBox(tile, "<empty>")));
		}break;
		case 1:{
			U.sendMessage(new RenderObjectUploadPacket(new Button(tile, new Vector2f(3, 1))));
		}break;
		case 2:{
			
		}break;
		}
		Magiology.ROBOT.clickKeyKeyboard(KeyEvent.VK_ESCAPE);
	}
}
