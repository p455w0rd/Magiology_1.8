package com.magiology.registry.events;

import com.magiology.registry.WrenchRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class PlayerWrenchEvent extends PlayerInteractEvent{
	
	public ItemStack wrenchItem;
	
	private PlayerWrenchEvent(EntityPlayer player, Action action, BlockPos pos, EnumFacing face, World world, ItemStack wrench){
		super(player, action, pos, face, world);
		wrenchItem=wrench;
	}
	public static PlayerWrenchEvent create(EntityPlayer player, Action action, BlockPos pos, EnumFacing face){
		ItemStack wrench;
		if(action!=Action.RIGHT_CLICK_AIR){
			wrench=player.getCurrentEquippedItem();
			if(WrenchRegistry.isWrench(wrench)){
				PlayerWrenchEvent event=new PlayerWrenchEvent(player, action, pos, face, player.worldObj,wrench);
				MinecraftForge.EVENT_BUS.post(event);
				return event;
			}
		}
		return null;
	}
    public boolean isCancelable(){
        return false;
    }
}
