package com.magiology.mcobjects.items;

import java.util.*;

import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.world.*;

import com.magiology.forgepowered.events.client.*;
import com.magiology.util.utilobjects.m_extension.*;
public class PowerCounter extends ItemM{
	public PowerCounter(){
		   super();
	   }
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player){
		boolean state=itemstack.getTagCompound().getBoolean("state");
		boolean notDone=true;
		RenderLoopEvents.disabledEquippItemAnimationTime=2;
		if(player.isSneaking()&&world.isRemote){
			if(state==false&&notDone==true){
				state=true;
				notDone=false;
			}
			if(state==true&&notDone==true){
				state=false;
				notDone=false;
			}
		}
		itemstack.getTagCompound().setBoolean("state", state);
		return itemstack;
	}
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {}
	
	
	@Override
	public boolean isFull3D(){return true;}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean is){
	
		
	list.add("Use for checking how much fire units (FU) is in block.");
	
	}
	
}
