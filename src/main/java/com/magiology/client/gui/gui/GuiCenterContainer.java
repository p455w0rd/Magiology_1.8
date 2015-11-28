package com.magiology.client.gui.gui;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.client.gui.container.*;
import com.magiology.core.*;
import com.magiology.core.init.*;
import com.magiology.forgepowered.packets.packets.*;
import com.magiology.mcobjects.tileentityes.network.*;
import com.magiology.util.renderers.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.*;

public class GuiCenterContainer extends GuiContainer implements Updateable{
	
	ResourceLocation main= new ResourceLocation(MReference.MODID,"/textures/gui/GuiCenterContainer.png");
	protected static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
	
	public GuiCenterContainer(EntityPlayer player,TileEntityNetworkProgramHolder tile){
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
			EntityPlayer player=UtilM.getThePlayer();
			if(player.openContainer instanceof CommandCenterContainer){
				CommandCenterContainer container=((CommandCenterContainer)player.openContainer);
				int id=container.selectedSlotId+36;
				ItemStack stack=((Slot)container.inventorySlots.get(id)).getStack();
				if(stack!=null){
					UtilM.sendMessage(new OpenProgramContainerInGui(id));
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
		EntityPlayer player=UtilM.getThePlayer();
		if(player.openContainer instanceof CommandCenterContainer){
			CommandCenterContainer container=((CommandCenterContainer)player.openContainer);
			int id=container.selectedSlotId;
			if(!UtilM.isItemInStack(MItems.commandContainer, ((Slot)container.inventorySlots.get(id+36)).getStack()))return -1;
			return id;
		}
		return -1;
	}
}