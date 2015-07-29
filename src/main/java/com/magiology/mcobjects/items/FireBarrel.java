package com.magiology.mcobjects.items;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import com.magiology.modedmcstuff.items.UpgradeableItem;
import com.magiology.upgrades.RegisterUpgrades.Container;
public class FireBarrel extends UpgradeableItem{
	
	public FireBarrel(Container container){
		
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer player, World w, int x, int y, int z, int l, float f, float f1, float f2){
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
						PotionEffect potion=new PotionEffect(1, 2, 1000, false);
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
