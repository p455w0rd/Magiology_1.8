package com.magiology.registry.events;

import static net.minecraftforge.fml.common.eventhandler.Event.Result.*;

import com.magiology.registry.WrenchRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerWrenchEvent extends Event{
	
	public EntityPlayer entityPlayer;
	public Action action;
	public World world;
	public BlockPos pos;
	public EnumFacing face;
	public Result useBlock = DEFAULT,useItem = DEFAULT;
	public ItemStack wrenchItem;
	
	private PlayerWrenchEvent(EntityPlayer player, Action action, BlockPos pos, EnumFacing face, World world, ItemStack wrench){
		entityPlayer=player;
		this.action=action;
		this.pos=pos;
		this.face=face;
		if(face==null)useBlock=DENY;
		this.world=world;
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
	@Override
	public boolean isCancelable(){
		return false;
	}

	@Override
	public void setCanceled(boolean cancel)
	{
		super.setCanceled(cancel);
		useBlock = (cancel ? DENY : useBlock == DENY ? DEFAULT : useBlock);
		useItem = (cancel ? DENY : useItem == DENY ? DEFAULT : useItem);
	}
}
