package com.magiology.forgepowered.packets.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public abstract class AbstractToClientMessage extends AbstractPacket{
	public final SendingTarget target;
	public AbstractToClientMessage(){target=null;}
	public AbstractToClientMessage(SendingTarget target){
		this.target=target;
	}
	public static class SendingTarget{
		public TargetPoint point;
		public EntityPlayer player;
		public final World world;
		public final TypeOfSending typeOfSending;
		public SendingTarget(World world){
			typeOfSending=TypeOfSending.ToAll;
			this.world=world;
		}
		public SendingTarget(World world, TargetPoint point){
			this.point=point;
			typeOfSending=TypeOfSending.AroundPoint;
			this.world=world;
		}
		public SendingTarget(World world, EntityPlayer player){
			this.player=player;
			typeOfSending=TypeOfSending.ToPlayer;
			this.world=world;
		}
		public SendingTarget(World world,int dimensionId){
			typeOfSending=TypeOfSending.ToDimension;
			this.world=world;
		}
		public static enum TypeOfSending{
			ToAll,AroundPoint,ToPlayer,ToDimension;
		}
	}
}
