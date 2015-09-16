package com.magiology.mcobjects.items;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.magiology.handelers.animationhandelers.TheHandHandeler;
import com.magiology.handelers.animationhandelers.TheHandHandeler.HandComonPositions;
import com.magiology.util.utilobjects.NBTUtil;

public class TheHand extends Item{
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player){
	    NBTUtil.createNBT(itemStack);
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,List list, boolean par4){
//		if(itemStack.getTagCompound() != null){
//				list.add(EnumChatFormatting.BLUE+" X="+EnumChatFormatting.AQUA+Integer.toString(itemStack.getTagCompound().getInteger("xC"))+
//					 EnumChatFormatting.BLUE+" Y="+EnumChatFormatting.AQUA+Integer.toString(itemStack.getTagCompound().getInteger("yC"))+
//					 EnumChatFormatting.BLUE+" Z="+EnumChatFormatting.AQUA+Integer.toString(itemStack.getTagCompound().getInteger("zC"))
//					 );
//		}
//		list.add("hold ");
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity){
		if(TheHandHandeler.getActivePosition(player)==HandComonPositions.ClosedFist){
			
			player.worldObj.createExplosion(player, entity.posX, entity.posY-0.0005, entity.posZ, 0.01F, false);
			return true;
		}
        return false;
    }
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int timeHeld){
		timeHeld-=100000;
		timeHeld*=-1;
		if(timeHeld<10)return;
		if(!world.isRemote){
			HandComonPositions ap=TheHandHandeler.getActivePosition(player);
			if(ap==HandComonPositions.ReadyForAction)TheHandHandeler.addANewEvent(player, player.worldObj.getTotalWorldTime()+5, "spawnProjectile", timeHeld);
			else if(ap==HandComonPositions.WeaponHolder)TheHandHandeler.addANewEvent(player, player.worldObj.getTotalWorldTime()+5, "spawnEntitySubatomicWorldDeconstructor", timeHeld);
		}
		else TheHandHandeler.actionAnimation(player);
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player){
		TheHandHandeler.handUseAnimation(player);
		HandComonPositions ap=TheHandHandeler.getActivePosition(player);
		if(ap==HandComonPositions.ReadyForAction||ap==HandComonPositions.WeaponHolder){
			player.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		}else if(ap==HandComonPositions.NaturalPosition){
			
			
			
		}
		return itemstack;
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		TheHandHandeler.handUseAnimation(player);
		if(TheHandHandeler.getActivePosition(player)==HandComonPositions.ReadyForAction){
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
		}else if(TheHandHandeler.getActivePosition(player)==HandComonPositions.NaturalPosition){
//			Helper.spawnEntity(new ModedEntityFallingBlock(world, x+0.5, y+0.501, z+0.5, Helper.getBlock(world, pos), world.getBlockMetadata(pos),player));
			world.setBlockToAir(pos);
			
		}
        return false;
    }
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5){
		NBTUtil.createNBT(itemstack);
		if(entity instanceof EntityPlayer){
//			((EntityPlayer)entity).setItemInUse(itemstack, 100);
		}
	}


	@Override
	public int getMaxItemUseDuration(ItemStack p_77626_1_){return 100000;}
}
