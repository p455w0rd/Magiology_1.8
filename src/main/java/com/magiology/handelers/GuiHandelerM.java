package com.magiology.handelers;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import com.magiology.core.Magiology;
import com.magiology.core.init.MGui;
import com.magiology.gui.container.ArmorContainer;
import com.magiology.gui.container.CommandCenterContainer;
import com.magiology.gui.container.ContainerEmpty;
import com.magiology.gui.container.ControlBockContainer;
import com.magiology.gui.container.ISidedPowerInstructorContainer;
import com.magiology.gui.container.SmartCrafterContainer;
import com.magiology.gui.container.UpgradeContainer;
import com.magiology.gui.gui.GuiArmor;
import com.magiology.gui.gui.GuiCenterContainer;
import com.magiology.gui.gui.GuiCommandContainerEditor;
import com.magiology.gui.gui.GuiControlBock;
import com.magiology.gui.gui.GuiHologramProjectorMain;
import com.magiology.gui.gui.GuiISidedPowerInstructor;
import com.magiology.gui.gui.GuiObjectCustomize;
import com.magiology.gui.gui.GuiSC;
import com.magiology.gui.gui.GuiUpgrade;
import com.magiology.mcobjects.tileentityes.TileEntityControlBlock;
import com.magiology.mcobjects.tileentityes.TileEntitySmartCrafter;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkCommandHolder;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.vectors.Pos;

public class GuiHandelerM implements IGuiHandler{
	
	public Container GetServerGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){
		TileEntity tile=world.getTileEntity(new Pos(x, y, z));
		MovingObjectPosition hit=Util.rayTrace(player,4, 1);
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
			if(tile instanceof TileEntityNetworkCommandHolder)
				return new CommandCenterContainer(player, (TileEntityNetworkCommandHolder)tile);
		case MGui.CommandContainerEditor:
			return new ContainerEmpty();
		}
		Util.println("[WARNING] Gui on "+(world.isRemote?"client":"server")+"\tat X= "+x+"\tY= "+y+"\tZ= "+z+"\t has failed to open!");
		return null;
	}

	public GuiContainer GetClientGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){
		TileEntity tile=world.getTileEntity(new Pos(x,y,z));
		MovingObjectPosition hit=Util.rayTrace(player,4, 1);
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
				return new GuiObjectCustomize(player, (TileEntityHologramProjector)tile,((TileEntityHologramProjector)tile).lastPartClicked);
		case MGui.HologramProjectorMainGui:
			if(tile instanceof TileEntityHologramProjector)
				return new GuiHologramProjectorMain(player, (TileEntityHologramProjector)tile);
		case MGui.CommandCenterGui:
			if(tile instanceof TileEntityNetworkCommandHolder)
				return new GuiCenterContainer(player, (TileEntityNetworkCommandHolder)tile);
		case MGui.CommandContainerEditor:
			return new GuiCommandContainerEditor(player);
		}
		Util.println("[WARNING] Gui on "+(world.isRemote?"client":"server")+"\tat X= "+x+"\tY= "+y+"\tZ= "+z+"\t has failed to open!");
		return null;
	}
	
	public static void openGui(EntityPlayer player, Object mainModClassInstance, int modGuiId, int x,int y,int z){
		if(U.isRemote(player))return;
		FMLNetworkHandler.openGui(player, mainModClassInstance, modGuiId, player.getEntityWorld(), x,y,z);
	}
	
	public static void openGui(EntityPlayer player, Object mainModClassInstance, int modGuiId, BlockPos pos){
		GuiHandelerM.openGui(player, mainModClassInstance, modGuiId, pos.getX(),pos.getY(),pos.getZ());
	}
	
	public static void openGui(EntityPlayer player, int modGuiId, BlockPos pos){
		GuiHandelerM.openGui(player, Magiology.getMagiology(), modGuiId, pos);
	}
	public static void openGui(EntityPlayer player, int modGuiId, int x, int y, int z){
		openGui(player, Magiology.getMagiology(), modGuiId, x,y,z);
	}
	@Override public Object getServerGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){return GetServerGuiElement(ID, player, world, x,y,z);}
	@Override public Object getClientGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){return GetClientGuiElement(ID, player, world, x,y,z);}
}
