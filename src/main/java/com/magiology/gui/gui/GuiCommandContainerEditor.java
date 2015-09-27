package com.magiology.gui.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import com.magiology.gui.container.CommandCenterContainer;
import com.magiology.gui.container.ContainerEmpty;
import com.magiology.gui.guiutil.gui.GuiTextArea;
import com.magiology.mcobjects.items.CommandContainer;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.m_extension.GuiContainerM;
import com.magiology.util.utilobjects.vectors.Vec2i;

public class GuiCommandContainerEditor extends GuiContainerM{
	
	private ItemStack stack;
	private GuiTextArea text=new GuiTextArea(new Vec2i(0, 50),new Vec2i(300, 600));
	
	public GuiCommandContainerEditor(EntityPlayer player){
		super(new ContainerEmpty());
		
		if(player.openContainer instanceof CommandCenterContainer){
			CommandCenterContainer container=((CommandCenterContainer)player.openContainer);
			stack=((Slot)container.inventorySlots.get(container.selectedSlotId+36)).getStack();
		}else stack=player.getCurrentEquippedItem();
		xSize=200;
		ySize=166;
		
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		text.size=new Vec2i(xSize+guiLeft*2-20, ySize+guiTop*2-20);
		text.pos=new Vec2i(10, 10);
		try{
			text.render(0, 0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}	
	@Override
	public void initGui(){
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		text.setText(CommandContainer.getCode(stack));
	}
	
	@Override
	protected void actionPerformed(GuiButton b)throws IOException{
		
		
		
	}
	@Override
	public void onGuiClosed(){
		Keyboard.enableRepeatEvents(false);
		CommandContainer.setCode(stack, text.getSelectedText());
		CommandContainer.setCode(stack, text.getText());
		super.onGuiClosed();
	}
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException{
		try{
			if(!text.keyTyped(keyCode, typedChar))super.keyTyped(typedChar, keyCode);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		try{
			text.mouseClickMove(mouseX, mouseY);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		try{
			text.mouseClicked(mouseX, mouseY, mouseButton);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
