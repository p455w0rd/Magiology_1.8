package com.magiology.forgepowered.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.magiology.core.init.MItems;
import com.magiology.handlers.animationhandlers.WingsFromTheBlackFireHandler;
import com.magiology.handlers.animationhandlers.WingsFromTheBlackFireHandler.Positions;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;
import com.magiology.mcobjects.items.armor.Pants_42;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.m_extension.effect.EntitySmokeFXM;


public class SpecialMovmentEvents{
	public static final SpecialMovmentEvents instance=new SpecialMovmentEvents();
	public void doubleJumpEvent(EntityPlayer player,int x,int y){
		World world=player.worldObj;
		boolean isRemote=world.isRemote;
		ExtendedPlayerData playerData=ExtendedPlayerData.get(player);
		ItemStack pantsSlot=player.inventory.armorInventory[1];
		if(!Util.isItemInStack(MItems.pants_42I, pantsSlot)||pantsSlot==null)return;
		else if(((Pants_42)pantsSlot.getItem()).hasUpgrade(pantsSlot, MItems.flightUpgrades)==-1)return;
		if(!player.isCollidedVertically&&!player.capabilities.isFlying){
			if(playerData.soulFlame<200)return;
			if(playerData.getJupmCount()>1)return;
			playerData.onJump();
			double xChange=0,yChange=0,zChange=0;
			yChange+=0.7;
			double[] a;
			if(x!=0||y!=0){
				int xRot=0,yRot=0,rot=0;
				if(x==-1)xRot=-180;
				yRot=90*y;
				if(xRot!=0&&yRot!=0)rot=(xRot);
				else{
					if(xRot!=0)rot=xRot;
					if(yRot!=0)rot=yRot;
				}
				a=Util.cricleXZ(player.rotationYaw+rot);
//				Helper.printInln(rot,xRot,yRot);
				xChange+=-a[0]*0.5;
				zChange+=a[1]*0.5;
			}else a=Util.cricleXZ(player.rotationYaw);
			if(isRemote){
				for(int a1=0;a1<15;a1++){
					float rand=Util.CRandF(0.45);
					double[] a2=Util.cricleXZ(player.rotationYaw+90);
					double xPos=player.posX-a2[0]*rand+a[0]*0.2,yPos=player.posY-0.9,zPos=player.posZ+a2[1]*rand-a[1]*0.2;
					boolean rb=Util.RInt(20)!=0;
					EntitySmoothBubleFX particle=new EntitySmoothBubleFX(world, xPos, yPos, zPos, Util.CRandF(0.1)-xChange/10, Util.CRandF(0.1)-yChange/10, Util.CRandF(0.1)-zChange/10,300, 1, rb?50:0, rb?1:2, 1, 0.2+Util.RF()*0.5, 0.2+Util.RF()*0.2, 0.8);
					particle.noClip=false;
					Util.spawnEntityFX(particle);
					Util.spawnEntityFX(new EntitySmokeFXM(world, xPos, yPos, zPos, Util.CRandF(0.1)-xChange, Util.CRandF(0.1)-yChange, Util.CRandF(0.1)-zChange));
					Util.spawnEntityFX(new EntitySmokeFXM(world, xPos, yPos, zPos, Util.CRandF(0.1)-xChange, Util.CRandF(0.1)-yChange, Util.CRandF(0.1)-zChange));
				}
			}
			player.motionX+=xChange;
			player.motionY+=yChange;
			player.motionZ+=zChange;
			if(playerData.getJupmCount()>1){
				if(!player.capabilities.isCreativeMode){
					playerData.setReducedFallDamage(playerData.getReducedFallDamage()+4);
				}
				playerData.soulFlame-=200;
			}
			playerData.sendData();
		}
	}
	public void handleWingPhysics(EntityPlayer player){
		Positions position=WingsFromTheBlackFireHandler.getPos(player);
		if(!Util.isItemInStack(MItems.WingsFTBFI, player.getCurrentArmor(2)))return;
		double[] a=Util.cricleXZ(player.rotationYaw);
		if(position==Positions.HoverPos||position==Positions.FlyBackvardPos||position==Positions.FlyStationarPos||position==Positions.FlyForvardPos){
			player.motionX*=0.9;
			player.motionY*=0.8;
			player.motionZ*=0.9;
			player.fallDistance=Math.abs((float)(player.motionY*5));
		}else if(position==Positions.HighSpeedPos){
			if(!player.capabilities.isFlying){
				ExtendedPlayerData extendedData=ExtendedPlayerData.get(player);
				if(extendedData!=null){
					if(extendedData.soulFlame<3)return;
					extendedData.soulFlame-=3;
				}
				double multi=Math.abs(player.motionY/2);
				player.motionY*=0.7;
				player.motionX-=a[0]*multi;
				player.motionZ+=a[1]*multi;
			}
		}
	}
	public void onFlap(EntityPlayer player,int x,int y,int z){
		Positions position=WingsFromTheBlackFireHandler.getPos(player);
		if(player.capabilities.isFlying)return;
		ExtendedPlayerData extendedData=ExtendedPlayerData.get(player);
		if(extendedData!=null){
			if(extendedData.soulFlame<13)return;
			extendedData.soulFlame-=13;
		}
		double[] a=Util.cricleXZ(player.rotationYaw);
		a[0]*=0.15;a[1]*=0.15;
		double y1=0.16;
		if(y==1&&extendedData.soulFlame>4){
			extendedData.soulFlame-=4;
			y1=0.3;
		}
		else if(y==-1){
			extendedData.soulFlame+=5;
			y1=-0.05;
		}
		if(position==Positions.FlyBackvardPos){
			player.motionY+=y1;
			player.motionX+=a[0];
			player.motionZ+=-a[1];
		}else if(position==Positions.FlyStationarPos){
			player.motionY+=y1;
		}else if(position==Positions.FlyForvardPos){
			player.motionY+=y1;
			player.motionX-=a[0];
			player.motionZ-=-a[1];
		}
	}
}
