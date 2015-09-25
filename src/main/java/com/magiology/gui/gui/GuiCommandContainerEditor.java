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
import com.magiology.util.utilobjects.m_extension.GuiContainerM;

public class GuiCommandContainerEditor extends GuiContainerM{
	
	private ItemStack stack;
	private GuiTextField name,code;
	private GuiTextArea text=new GuiTextArea(10, 10);
	
	public GuiCommandContainerEditor(EntityPlayer player){
		super(new ContainerEmpty());
		
		if(player.openContainer instanceof CommandCenterContainer){
			CommandCenterContainer container=((CommandCenterContainer)player.openContainer);
			stack=((Slot)container.inventorySlots.get(container.selectedSlotId+36)).getStack();
		}else stack=player.getCurrentEquippedItem();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		text.render(300, 300);
	}
	@Override
	public void initGui(){
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		textFieldList.add(name=new GuiTextField(0, fontRendererObj, guiLeft, guiTop, 200, 14));
		textFieldList.add(code=new GuiTextField(0, fontRendererObj, guiLeft, guiTop+20, 200, 14));
		name.setText(CommandContainer.getName(stack));
		code.setText(CommandContainer.getCode(stack));
	}
	
	@Override
	protected void actionPerformed(GuiButton b)throws IOException{
		
		
		
	}
	@Override
	public void onGuiClosed(){
		Keyboard.enableRepeatEvents(false);
		CommandContainer.setCommandName(stack, name.getText());
		CommandContainer.setCode(stack, code.getText());
		super.onGuiClosed();
	}
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException{
		super.keyTyped(typedChar, keyCode);
		text.onKeyTyped(keyCode, typedChar);
	}
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		text.onMouseDragged(mouseX, mouseY);
	}
}
