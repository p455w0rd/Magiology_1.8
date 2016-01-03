package com.magiology.forgepowered.events;

import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.event.world.*;
import net.minecraftforge.fml.common.eventhandler.*;

import com.magiology.api.power.*;
import com.magiology.util.utilclasses.*;

public class GameEvents{
	
	@SubscribeEvent
	public void onBlockPlace(BlockEvent.PlaceEvent e){
		BlockPos pos=e.pos;
		EntityPlayer player=e.player;
		World world=e.world;
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof PowerCore)PowerCore.SavePowerToItemEvents.onPowerCorePlaced(pos, player, world, tile);
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent e){
		BlockPos pos=e.pos;
		EntityPlayer player=e.getPlayer();
		World world=e.world;
		world.getTileEntity(pos);
		if(player.capabilities.isCreativeMode){
			
		}else{
			dropContainerOnDerstroy(world, pos);
		}
	}
	public static EntityItem dropContainerOnDerstroy(World world,BlockPos pos){
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof PowerUpgrades){
			PowerUpgrades Tile=(PowerUpgrades)tile;
			if(Tile.getcontainerItems()!=null)for(ItemStack a:Tile.getcontainerItems())if(a!=null)return UtilM.dropBlockAsItem(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,a);
		}
		return null;
	}
	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save e){
		
	}
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e){
		
	}
}
