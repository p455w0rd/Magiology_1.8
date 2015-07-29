package com.magiology.forgepowered.event;

import com.magiology.api.power.PowerUpgrades;
import com.magiology.objhelper.helpers.Helper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

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
		TileEntity tile=world.getTileEntity(x, y, z);

//    	ForcePipeUpdate_SubEvent.updatein3by3(world,x,y,z);
    	
		if(player.capabilities.isCreativeMode){
			
		}else{
			dropContainerOnDerstroy(world, x, y, z);
		}
	}
	public static void dropContainerOnDerstroy(World world,int x,int y,int z){
		TileEntity tile=world.getTileEntity(x, y, z);
		if(tile instanceof PowerUpgrades){
			PowerUpgrades Tile=(PowerUpgrades)tile;
			if(Tile.getcontainerItems()!=null)for(ItemStack a:Tile.getcontainerItems())if(a!=null)Helper.dropBlockAsItem(world,x+0.5,y+0.5,z+0.5,a);
			return;
		}
	}
}
