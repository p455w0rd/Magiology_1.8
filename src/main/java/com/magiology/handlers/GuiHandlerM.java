package com.magiology.handlers;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.internal.*;

import com.magiology.client.gui.container.*;
import com.magiology.client.gui.gui.*;
import com.magiology.core.*;
import com.magiology.core.init.*;
import com.magiology.mcobjects.tileentityes.*;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.*;
import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.mcobjects.tileentityes.network.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.vectors.*;

public class GuiHandlerM implements IGuiHandler{
	
	public Container GetServerGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){
		TileEntity tile=world.getTileEntity(new Pos(x, y, z));
		MovingObjectPosition hit=UtilM.rayTrace(player,4, 1);
		int side=hit!=null?hit.sideHit.getIndex():-1;
		
		switch (ID){
		case MGui.GuiUpgrade:              if(tile instanceof TileEntityPow)         
			return new UpgradeContainer(player.inventory, (TileEntityPow)tile);
		case MGui.GuiControlBock:          if(tile instanceof TileEntityControlBlock)
			return new ControlBockContainer(player.inventory, (TileEntityControlBlock)tile);
		case MGui.GuiArmor:                                                          
			return new ArmorContainer(player, player.inventory.armorInventory);
		case MGui.GuiSC:                   if(tile instanceof TileEntitySmartCrafter)
			return new SmartCrafterContainer(player, (TileEntitySmartCrafter)tile,side);
		case MGui.GuiISidedPowerInstructor:                                          
			return new ISidedPowerInstructorContainer(player, tile);
		case MGui.HologramProjectorObjectCustomGui:
			if(tile instanceof TileEntityHologramProjector&&((TileEntityHologramProjector)tile).lastPartClicked!=null)
			return new ContainerEmpty();
		case MGui.HologramProjectorMainGui:
			if(tile instanceof TileEntityHologramProjector)
				return new ContainerEmpty();
		case MGui.CommandCenterGui:
			if(tile instanceof TileEntityNetworkProgramHolder)
				return new CommandCenterContainer(player, (TileEntityNetworkProgramHolder)tile);
		case MGui.CommandContainerEditor:
			return new ContainerEmpty();
		}
		UtilM.println("[WARNING] Gui on "+(world.isRemote?"client":"server")+"\tat X= "+x+"\tY= "+y+"\tZ= "+z+"\t has failed to open!");
		return null;
	}

	public GuiContainer GetClientGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){
		TileEntity tile=world.getTileEntity(new Pos(x,y,z));
		MovingObjectPosition hit=UtilM.rayTrace(player,4, 1);
		int side=hit!=null?hit.sideHit.getIndex():-1;
		
		switch (ID){
		case MGui.GuiUpgrade:
			if(tile instanceof TileEntityPow)         
				return new GuiUpgrade(player.inventory, (TileEntityPow)tile);
		case MGui.GuiControlBock:
			if(tile instanceof TileEntityControlBlock)
				return new GuiControlBock(player.inventory, (TileEntityControlBlock)tile);
		case MGui.GuiArmor:
			return new GuiArmor(player, player.inventory.armorInventory);
		case MGui.GuiSC:
			if(tile instanceof TileEntitySmartCrafter)
				return new GuiSC(player, (TileEntitySmartCrafter)tile,side);
		case MGui.GuiISidedPowerInstructor:                                          
			return new GuiISidedPowerInstructor(player, tile);
		case MGui.HologramProjectorObjectCustomGui:
			if(tile instanceof TileEntityHologramProjector&&((TileEntityHologramProjector)tile).lastPartClicked!=null)
				return new GuiHoloObjectEditor(player, (TileEntityHologramProjector)tile,((TileEntityHologramProjector)tile).lastPartClicked);
		case MGui.HologramProjectorMainGui:
			if(tile instanceof TileEntityHologramProjector)
				return new GuiHologramProjectorMain(player, (TileEntityHologramProjector)tile);
		case MGui.CommandCenterGui:
			if(tile instanceof TileEntityNetworkProgramHolder)
				return new GuiCenterContainer(player, (TileEntityNetworkProgramHolder)tile);
		case MGui.CommandContainerEditor:
			return new GuiProgramContainerEditor(player);
		}
		UtilM.println("[WARNING] Gui on "+(world.isRemote?"client":"server")+"\tat X= "+x+"\tY= "+y+"\tZ= "+z+"\t has failed to open!");
		return null;
	}
	
	public static void openGui(EntityPlayer player, Object mainModClassInstance, int modGuiId, int x,int y,int z){
		if(U.isRemote(player))return;
		FMLNetworkHandler.openGui(player, mainModClassInstance, modGuiId, player.getEntityWorld(), x,y,z);
	}
	
	public static void openGui(EntityPlayer player, Object mainModClassInstance, int modGuiId, BlockPos pos){
		GuiHandlerM.openGui(player, mainModClassInstance, modGuiId, pos.getX(),pos.getY(),pos.getZ());
	}
	
	public static void openGui(EntityPlayer player, int modGuiId, BlockPos pos){
		GuiHandlerM.openGui(player, Magiology.getMagiology(), modGuiId, pos);
	}
	public static void openGui(EntityPlayer player, int modGuiId, int x, int y, int z){
		openGui(player, Magiology.getMagiology(), modGuiId, x,y,z);
	}
	@Override public Object getServerGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){return GetServerGuiElement(ID, player, world, x,y,z);}
	@Override public Object getClientGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){return GetClientGuiElement(ID, player, world, x,y,z);}
}
