package com.magiology.handelers;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.magiology.core.init.MGui;
import com.magiology.gui.gui.GuiArmor;
import com.magiology.gui.gui.GuiControlBock;
import com.magiology.gui.gui.GuiISidedPowerInstructor;
import com.magiology.gui.gui.GuiSC;
import com.magiology.gui.gui.GuiUpgrade;
import com.magiology.gui.guiContainer.GuiArmorContainer;
import com.magiology.gui.guiContainer.GuiControlBockContainer;
import com.magiology.gui.guiContainer.GuiISidedPowerInstructorContainer;
import com.magiology.gui.guiContainer.GuiSCContainer;
import com.magiology.gui.guiContainer.GuiUpgradeContainer;
import com.magiology.mcobjects.tileentityes.TileEntityControlBlock;
import com.magiology.mcobjects.tileentityes.TileEntitySmartCrafter;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.render.tilerender.hologram.GuiObjectCustomize;
import com.magiology.render.tilerender.hologram.GuiObjectCustomizeContainer;
import com.magiology.util.utilclasses.Helper;
import com.magiology.util.utilobjects.vectors.Pos;

public class GuiHandeler implements IGuiHandler{
	
	public Container GetServerGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){
		TileEntity tile=world.getTileEntity(new Pos(x, y, z));
		MovingObjectPosition hit=Helper.rayTrace(player,4, 1);
		int side=hit!=null?hit.sideHit.getIndex():-1;
		
		switch (ID){
		case MGui.GuiUpgrade:              if(tile instanceof TileEntityPow)         
			return new GuiUpgradeContainer(player.inventory, (TileEntityPow)tile);
		case MGui.GuiControlBock:          if(tile instanceof TileEntityControlBlock)
			return new GuiControlBockContainer(player.inventory, (TileEntityControlBlock)tile);
		case MGui.GuiArmor:                                                          
			return new GuiArmorContainer(player, player.inventory.armorInventory);
		case MGui.GuiSC:                   if(tile instanceof TileEntitySmartCrafter)
			return new GuiSCContainer(player, (TileEntitySmartCrafter)tile,side);
		case MGui.GuiISidedPowerInstructor:                                          
			return new GuiISidedPowerInstructorContainer(player, tile);
		case MGui.HologramProjectorObjectCustomGui:
			if(tile instanceof TileEntityHologramProjector&&((TileEntityHologramProjector)tile).lastPartClicked!=null)
			return new GuiObjectCustomizeContainer(player, (TileEntityHologramProjector) tile);
		}
		Helper.println("[WARNING] Gui on "+(world.isRemote?"client":"server")+"\tat X= "+x+"\tY= "+y+"\tZ= "+z+"\t has failed to open!");
		return null;
	}

	public GuiContainer GetClientGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){
		TileEntity tile=world.getTileEntity(new Pos(x,y,z));
		MovingObjectPosition hit=Helper.rayTrace(player,4, 1);
		int side=hit!=null?hit.sideHit.getIndex():-1;
		
		switch (ID){
		case MGui.GuiUpgrade:              if(tile instanceof TileEntityPow)         
			return new GuiUpgrade(player.inventory, (TileEntityPow)tile);
		case MGui.GuiControlBock:          if(tile instanceof TileEntityControlBlock)
			return new GuiControlBock(player.inventory, (TileEntityControlBlock)tile);
		case MGui.GuiArmor:                                                          
			return new GuiArmor(player, player.inventory.armorInventory);
		case MGui.GuiSC:                   if(tile instanceof TileEntitySmartCrafter)
			return new GuiSC(player, (TileEntitySmartCrafter)tile,side);
		case MGui.GuiISidedPowerInstructor:                                          
			return new GuiISidedPowerInstructor(player, tile);
		case MGui.HologramProjectorObjectCustomGui:
			if(tile instanceof TileEntityHologramProjector&&((TileEntityHologramProjector)tile).lastPartClicked!=null)
			return new GuiObjectCustomize(player, (TileEntityHologramProjector)tile,((TileEntityHologramProjector)tile).lastPartClicked);
		}
		Helper.println("[WARNING] Gui on "+(world.isRemote?"client":"server")+"\tat X= "+x+"\tY= "+y+"\tZ= "+z+"\t has failed to open!");
		return null;
	}
	@Override public Object getServerGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){return GetServerGuiElement(ID, player, world, x,y,z);}
	@Override public Object getClientGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){return GetClientGuiElement(ID, player, world, x,y,z);}
}
