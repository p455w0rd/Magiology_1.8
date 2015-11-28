package com.magiology.mcobjects.items;

import java.util.*;

import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.Container;
import com.magiology.mcobjects.items.upgrades.skeleton.*;
public class FireBarrel extends UpgradeableItem{
	
	public FireBarrel(Container container){
		
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		boolean return1=false;
		if(player.isOnLadder()||player.isInWater())return1=true;
		
		
//		if(w.isRemote){
//			SixSidedBoolean test=SixSidedBoolean.create(Modifier.First6True,Modifier.Exclude,0,5,Modifier.Last6False,Modifier.Include,1,3);
//			if(test!=null){
//				for(int a=0;a<test.sides.length;a++){
//					Helper.println(test.sides[a]);
//					if(a==5)Helper.println("--");
//				}
//				Helper.println("-----");
//			}
//		}
		
		
		return return1;
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity){
		boolean result=false;
		
		if(player.getDisplayName().equals("direwolf20")){
			if(entity instanceof EntityPlayer){
				EntityPlayer clickedPlayer=(EntityPlayer)entity;
				if(clickedPlayer.getDisplayName().equals("Soaryn")){
					EntityPlayer Soaryn=clickedPlayer;
					
					Soaryn.rotationPitch=0;
					Soaryn.rotationYaw=0;
					Soaryn.rotationYawHead=0;
					
					if(player.worldObj.rand.nextBoolean())Soaryn.rotationPitch+=180;
					if(player.worldObj.rand.nextBoolean()){
						Soaryn.cameraPitch+=10;
						PotionEffect potion=new PotionEffect(1, 2, 1000);
						Soaryn.addPotionEffect(potion);
					}
				}
			}
		}
		
		
		
        return result;
    }
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean is){
		super.addInformation(itemStack, entityPlayer, list, is);
		if(entityPlayer.getDisplayName().equals("direwolf20"))list.add("Right click with this on Soaryn");
		
	}
}
