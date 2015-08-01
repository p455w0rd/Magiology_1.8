package com.magiology.forgepowered.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.magiology.api.power.PowerUpgrades;
import com.magiology.objhelper.helpers.Helper;

public class GameLoopEvents{
	
	@SubscribeEvent
	public void onBlockPlace(BlockEvent.PlaceEvent e){
//    	ForcePipeUpdate_SubEvent.updatein3by3(e.world,e.x,e.y,e.z);
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent e){
		int x=e.x,y=e.y,z=e.z;
		EntityPlayer player=e.getPlayer();
		World world=e.world;
		TileEntity tile=world.getTileEntity(pos);

//    	ForcePipeUpdate_SubEvent.updatein3by3(world,x,y,z);
    	
		if(player.capabilities.isCreativeMode){
			
		}else{
			dropContainerOnDerstroy(world, pos);
		}
	}
	public static void dropContainerOnDerstroy(World world,BlockPos pos){
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof PowerUpgrades){
			PowerUpgrades Tile=(PowerUpgrades)tile;
			if(Tile.getcontainerItems()!=null)for(ItemStack a:Tile.getcontainerItems())if(a!=null)Helper.dropBlockAsItem(world,x+0.5,y+0.5,z+0.5,a);
			return;
		}
	}
}
