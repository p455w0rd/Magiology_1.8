package com.magiology.forgepowered.event;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.magiology.api.power.PowerCore;
import com.magiology.api.power.PowerUpgrades;
import com.magiology.util.utilclasses.Helper;

public class GameLoopEvents{
	
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
		TileEntity tile=world.getTileEntity(pos);
		if(player.capabilities.isCreativeMode){
			
		}else{
			dropContainerOnDerstroy(world, pos);
		}
	}
	public static EntityItem dropContainerOnDerstroy(World world,BlockPos pos){
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof PowerUpgrades){
			PowerUpgrades Tile=(PowerUpgrades)tile;
			if(Tile.getcontainerItems()!=null)for(ItemStack a:Tile.getcontainerItems())if(a!=null)return Helper.dropBlockAsItem(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,a);
		}
		return null;
	}
}
