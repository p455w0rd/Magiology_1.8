package com.magiology.gui.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.magiology.core.MReference;
import com.magiology.core.init.MItems;
import com.magiology.forgepowered.packets.packets.ClickCommandContainerInGui;
import com.magiology.gui.GuiUpdater.Updateable;
import com.magiology.gui.container.CommandCenterContainer;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkCommandHolder;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.NBTUtil;

public class GuiCenterContainer extends GuiContainer implements Updateable{
	
	ResourceLocation main= new ResourceLocation(MReference.MODID,"/textures/gui/GuiCenterContainer.png");
	protected static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
	
	public GuiCenterContainer(EntityPlayer player,TileEntityNetworkCommandHolder tile){
		super(new CommandCenterContainer(player,tile));
		this.xSize=176;
		this.ySize=166;
		
	}
	@Override
	public void drawGuiContainerForegroundLayer(int a,int b){
		if(getSelectedSlotId()>=0){
			TessUtil.bindTexture(widgetsTexPath);
			drawTexturedModalRect(getSelectedSlotId()%4*18+49, getSelectedSlotId()/4*18+3, 0, 22, 24, 24);
		}
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float v1, int v2, int v3){
		TessUtil.bindTexture(main);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		((GuiButton)buttonList.get(0)).enabled=getSelectedSlotId()>=0;
	}
	
	@Override
	public void initGui(){
		super.initGui();
		buttonList.add(new GuiButton(0, guiLeft+11, guiTop+32, 34, 20, "Open"));
	}
	
	@Override
	protected void actionPerformed(GuiButton b){
		switch(b.id){
		case 0:{
			EntityPlayer player=Util.getThePlayer();
			if(player.openContainer instanceof CommandCenterContainer){
				CommandCenterContainer container=((CommandCenterContainer)player.openContainer);
				int id=container.selectedSlotId+36;
				ItemStack stack=((Slot)container.inventorySlots.get(id)).getStack();
				if(stack!=null){
					Util.sendMessage(new ClickCommandContainerInGui(id));
					NBTUtil.createNBT(stack);
				}
			}
		}break;
		}
	}
	
	@Override
	public void update(){
		
		
		
	}
	
	private int getSelectedSlotId(){
		EntityPlayer player=Util.getThePlayer();
		if(player.openContainer instanceof CommandCenterContainer){
			CommandCenterContainer container=((CommandCenterContainer)player.openContainer);
			int id=container.selectedSlotId;
			if(!Util.isItemInStack(MItems.commandContainer, ((Slot)container.inventorySlots.get(id+36)).getStack()))return -1;
			return id;
		}
		return -1;
	}
}