package com.magiology.registry.events;

import static net.minecraftforge.fml.common.eventhandler.Event.Result.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.*;

import com.magiology.registry.*;

public class PlayerWrenchEvent extends Event{
	
	public final EntityPlayer entityPlayer;
	public final Action action;
	public final World world;
	public final BlockPos pos;
	public final EnumFacing face;
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
