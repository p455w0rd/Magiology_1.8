package com.magiology.handelers;

import net.minecraft.entity.player.EntityPlayer;

import com.magiology.forgepowered.event.GenericPacketEvents;

public class GenericPacketEventHandeler{
	public static GenericPacketEventHandeler instance=new GenericPacketEventHandeler();
	public static GenericPacketEvents handelerInstance=new GenericPacketEvents();
	static{GenericPacketEvents.callerInstance=instance;}

	public static void addNewIntegerPacketEvent(int eventId,int integer,EntityPlayer player,Side target){
		handelerInstance.intPacketEvent(instance.new IntegerPacketEvent(eventId, integer, player,target));
	}
	public static void addNewStringPacketEvent(int eventId, String string,EntityPlayer player, Side target){
		handelerInstance.stringPacketEvent(instance.new StringPacketEvent(eventId, string, player,target));
	}
	public static void addNewVoidPacketEvent(int eventId,EntityPlayer player, Side target){
		handelerInstance.voidPacketEvent(instance.new PacketEvent(eventId, player,target));
	}
	public class IntegerPacketEvent extends PacketEvent{
		public int integer;
		public IntegerPacketEvent(int eventId,int integer,EntityPlayer player, Side target){
			super(eventId,player,target);
			this.integer=integer;
		}
	}
	public class StringPacketEvent extends PacketEvent{
		public String string;
		public StringPacketEvent(int eventId,String string,EntityPlayer player, Side target){
			super(eventId,player,target);
			this.string=string;
		}
	}
	public class PacketEvent{
		public boolean isRemote;
		public int eventId;
		public EntityPlayer player;
		public Side target;
		public PacketEvent(int eventId,EntityPlayer player, Side target){
			this.player=player;
			this.eventId=eventId;
			this.target=target;
			isRemote=player.worldObj.isRemote;
		}
	}
}
