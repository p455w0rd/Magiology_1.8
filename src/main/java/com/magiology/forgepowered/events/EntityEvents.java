package com.magiology.forgepowered.events;

import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.*;

import com.magiology.api.power.*;
import com.magiology.client.gui.*;
import com.magiology.core.*;
import com.magiology.core.init.*;
import com.magiology.handlers.*;
import com.magiology.handlers.animationhandlers.*;
import com.magiology.mcobjects.effect.*;
import com.magiology.mcobjects.entitys.*;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.*;
import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.registry.events.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.*;

public class EntityEvents{
//	ResourceLocation lol = new ResourceLocation(Magiology.MODID+":"+"/textures/blocks/background orginal.png");
//	WorldRenderer tess=TessHelper.getWR();
	boolean isFP;
	SpecialPlayerParicleHandler spph=new SpecialPlayerParicleHandler();
	SlowdownUtil slowdown2=new SlowdownUtil(20);
	@SubscribeEvent
	public void onLivingUpdateEvent(LivingUpdateEvent event){
		World world=event.entity.worldObj;
		double x=event.entity.posX,y=event.entity.posY,z=event.entity.posZ,xv=event.entity.motionX,yv=event.entity.motionY,zv=event.entity.motionZ;
		
		{
			Entity entity=event.entity;
//			if(RenderLoopEvents.entitys.size()>50)RenderLoopEvents.entitys.remove(Helper.RInt(RenderLoopEvents.entitys.size()));
			if(world.isRemote)if(entity instanceof EntityLivingBase){
				EntityPlayer player=UtilM.getThePlayer();
				if(
				   !entity.isDead&&
				   entity.isInRangeToRender3d(player.posX+UtilM.CRandI(50), player.posY+UtilM.CRandI(50), player.posZ+UtilM.CRandI(50))&&
				   ((EntityLivingBase)entity).getCreatureAttribute()!=EnumCreatureAttribute.UNDEAD&&
				   entity.isInRangeToRenderDist(entity.renderDistanceWeight)&&
				   entity.getBoundingBox()!=null
				   ){
					EntityPosAndBB EPABB=new EntityPosAndBB(entity);
//					if(!contains&&RenderLoopEvents.entitys.size()<100)RenderLoopEvents.entitys.add(EPABB);
					
					
					if((entity instanceof EntityPlayer?true:(entity.worldObj.getTotalWorldTime()%6==0))&&(isFP?true:entity!=player)&&UtilM.RB()&&(UtilM.isItemInStack(MItems.pants_42I, player.inventory.armorInventory[1]))){
						double[] l=EPABB.getRandDotInBB(1.5),k=EPABB.getMiddleOfBB();
						EntityFollowingBubleFX particle=new EntityFollowingBubleFX(world,l[0]+k[0], l[1]+k[1], l[2]+k[2], xv*1.2, yv*1.2, zv*1.2,entity,0, k[0]+UtilM.CRandF(0.4), k[1]+UtilM.CRandF(0.4), k[2]+UtilM.CRandF(0.4), 600, 4, 1,0.2+UtilM.RF()*0.5,0.2+UtilM.RF()*0.5,0.5/(entity==player?8:1));
						particle.isGL_DEPTHDisabled=true;
						particle.noClip=true;
						UtilM.spawnEntityFX(particle);
					}
				}
			}
		}
		
		if(event.entity instanceof EntityPlayer){
			EntityPlayer player=(EntityPlayer) event.entity;
			
//			if(player.getCurrentEquippedItem()!=null&&player.getCurrentEquippedItem().getTagCompound()!=null){
//				NBTTagCompound nbt=player.getCurrentEquippedItem().getTagCompound();
//				for(Object i:nbt.getKeySet()){
//					Helper.print(i);
//					Helper.println(nbt.getInteger(i.toString()));
//				}
//				Helper.println("----------");
//			}
			
			
			WingsFromTheBlackFireHandler.updateModel(player);
			GuiUpdater.tryToUpdate(player);
			if(world.isRemote)if(ComplexPlayerRenderingData.get(player)==null)ComplexPlayerRenderingData.registerEntityPlayerRenderer(player);
			ExtendedPlayerData playerData=ExtendedPlayerData.get(player);
			if(playerData==null){
				ExtendedPlayerData.register(player);
				ExtendedPlayerData.get(player).sendData();
			}else{
				if(playerData.soulFlame+5<=playerData.maxSoulFlame)playerData.soulFlame+=5;
				if(world.getTotalWorldTime()%40==0)playerData.sendData();
				
				if(playerData.getReducedFallDamage()>0.01&&player.fallDistance>2){
					player.fallDistance-=playerData.getReducedFallDamage();
					playerData.setReducedFallDamage(0);
					if(player.fallDistance<0){
						playerData.setReducedFallDamage(-player.fallDistance);
						player.fallDistance=0.001F;
					}
				}
				if(playerData.isFlappingDown)SpecialMovmentEvents.instance.onFlap(player, playerData.getKeysX(), playerData.getKeysY(), playerData.getKeysZ());
			}
			if(WingsFromTheBlackFireHandler.getIsActive(player))SpecialMovmentEvents.instance.handleWingPhysics(player);
			if(TheHandHandler.isActive(player))TheHandHandler.update(player);
			{
				InventoryPlayer inv=player.inventory;
				
				for(ItemStack a:inv.mainInventory){
					if(UtilM.isItemInStack(MItems.PowerCounter, a)){
						NBTUtil.createNBT(a);
						boolean state=a.getTagCompound().getBoolean("state");
						double animation=a.getTagCompound().getDouble("anim"),prevAnimation=a.getTagCompound().getDouble("pAnim");
						prevAnimation=animation;
						for(int i=0;i<5;i++)
							if(state==true){if(animation<1)animation+=0.01;}
							else{if(animation>0)animation-=0.01;}
						
						if(!player.isSneaking()){
							state=false;
						}
						
						a.getTagCompound().setDouble("anim", animation);
						a.getTagCompound().setDouble("pAnim", prevAnimation);
						a.getTagCompound().setBoolean("state", state);
					
					}
				}
				
			}
			if(SpecialPlayerUtil.getPlayerRank(player)!=-1){
				isFP=U.getMC().gameSettings.thirdPersonView!=0;
				if(player.isCollidedVertically)yv=0;
				
				if(isFP)spph.onUpdate(world, player, x,y,z,xv,yv,zv);
			}
			//---------------------
		}
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event){
		World world=event.world;
		EntityPlayer player=event.entityPlayer;
		BlockPos pos=event.pos;
		ItemStack equippedItemStack=player.getCurrentEquippedItem();
		if(TileEntityHologramProjector.invokeRayTrace(event, player))return;
		PlayerWrenchEvent.create(player, event.action, pos, event.face);
		if((equippedItemStack==null?true:equippedItemStack.getItem()!=MItems.FireHammer)&&event.action==Action.RIGHT_CLICK_BLOCK&&player.isSneaking()){
			TileEntity tile=world.getTileEntity(pos);
			if(tile instanceof TileEntityPow){
				if(!world.isRemote){
					GuiHandlerM.openGui(player, Magiology.getMagiology(), MGui.GuiUpgrade, pos);
					event.setCanceled(true);
				}
			}
		}
	}
	@SubscribeEvent
	public void onPlayerWrenchEvent(PlayerWrenchEvent event){
		World world=event.world;
		EntityPlayer player=event.entityPlayer;
		BlockPos pos=event.pos;
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof PowerCore){
			PowerCore.SavePowerToItemEvents.onPowerCoreWrenched(pos, player, world, tile);
		}
	}
//	@SubscribeEvent
//	public void onEntityConstructing(EntityConstructing event){
//		World world=event.entity.worldObj;
//		Entity entity=event.entity;
//		if(entity instanceof EntityItem);else{
//			if(entity instanceof EntitySubatomicWorldDeconstructor){
//				EntitySubatomicWorldDeconstructor Entity=(EntitySubatomicWorldDeconstructor)entity;
//				Helper.printInln(world.isRemote,Entity.shootingEntity);
//			}
//		}
//		if(entity instanceof EntityPlayer){
//			EntityPlayer player=(EntityPlayer)entity;
//			
//		}
//		
//	}
//	@SubscribeEvent
//	public void onEntityJoinWorld(EntityJoinWorldEvent event){
//		World world=event.world;
//		double x=event.entity.posX,y=event.entity.posY,z=event.entity.posZ,xv=event.entity.motionX,yv=event.entity.motionY,zv=event.entity.motionZ;
//		Entity entity=event.entity;
//		
//		
//		if(entity instanceof EntityPlayer){
//			EntityPlayer player=(EntityPlayer)entity;
//			
//			if(GetSpecialPlayer.getPlayerRank(player)==1){
//				double R,G,B;
//				for (int i=0;i<25;i++){
//					double xr=x+Helper.CRandF(0.8);
//					double yr=y+Helper.CRandF(1.8)-0.6;
//					double zr=z+Helper.CRandF(0.8);
//					R=world.rand.nextFloat();G=world.rand.nextFloat();B=world.rand.nextFloat();
//					Helper.spawnEnitiyFX(new EntitySmoothBubleFX(world,xr,yr,zr, 0,-0.05,0, 400, 6+world.rand.nextInt(2), 8,true,2,"tx1", R,G,B, 1, 0.95));
//				}
//			}
//		}
//	}
	
	@SubscribeEvent
	public void onLivingJumpEvent(LivingJumpEvent event){
		World world=event.entity.worldObj;
		double x=event.entity.posX,y=event.entity.posY,z=event.entity.posZ;
		
		
		if(event.entity instanceof EntityPlayer){
			EntityPlayer player=(EntityPlayer)event.entity;
			
			if(SpecialPlayerUtil.getPlayerRank(player)!=-1){
				isFP=U.getMC().gameSettings.thirdPersonView!=0;
				if(isFP)spph.onJump(world, player, x,y,z);
			}
			//---------------------
			{
				
				
				
			}
		}
	}
	@SubscribeEvent
	public void onLivingFallEvent(LivingFallEvent event){
		World world=event.entity.worldObj;double x=event.entity.posX,y=event.entity.posY,z=event.entity.posZ;
		if(event.entity instanceof EntityPlayer){
			EntityPlayer player=(EntityPlayer)event.entity;
			if(SpecialPlayerUtil.getPlayerRank(player)!=-1){
				isFP=U.getMC().gameSettings.thirdPersonView!=0;
				if(isFP)spph.onHitTheGround(world, x,y,z, (EntityPlayer)event.entity,event.distance);
			}
			ExtendedPlayerData.get(player).onLand(true);
			//---------------------  
			{
				
				
				
			}
		}
	}
	@SubscribeEvent
	public void onPlayerFlyableFallEvent(PlayerFlyableFallEvent event){
		World world=event.entity.worldObj;double x=event.entity.posX,y=event.entity.posY,z=event.entity.posZ;
		if(event.entity instanceof EntityPlayer){
			ExtendedPlayerData.get((EntityPlayer)event.entity).onLand(false);
			if(SpecialPlayerUtil.getPlayerRank((EntityPlayer) event.entity)!=-1){
				isFP=U.getMC().gameSettings.thirdPersonView!=0;
				if(isFP)spph.onHitTheGround(world, x,y,z, (EntityPlayer)event.entity,event.distance);
			}
			//---------------------
			{
				
				
				
			}
		}
	}
}
