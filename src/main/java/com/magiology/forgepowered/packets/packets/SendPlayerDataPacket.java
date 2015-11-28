package com.magiology.forgepowered.packets.packets;

import java.io.*;

import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.forgepowered.packets.core.*;
import com.magiology.mcobjects.entitys.*;

public class SendPlayerDataPacket extends AbstractToClientMessage{
	private int now,max,count;
	private float reducedFallDamage;
	public SendPlayerDataPacket(){}
	public SendPlayerDataPacket(EntityPlayer target){
		super(new SendingTarget(target.worldObj, target));
		ExtendedPlayerData data=ExtendedPlayerData.get(target);
		now=data.soulFlame;
		max=data.maxSoulFlame;
		count=data.getJupmCount();
		reducedFallDamage=data.getReducedFallDamage();
	}
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		buffer.writeInt(now);
		buffer.writeInt(max);
		buffer.writeInt(count);
		buffer.writeFloat(reducedFallDamage);
	}

	@Override
	public void read(PacketBuffer buffer)throws IOException{
		now=buffer.readInt();
		max=buffer.readInt();
		count=buffer.readInt();
		reducedFallDamage=buffer.readFloat();
	}

	@Override
	public IMessage process(EntityPlayer player, Side side){
		ExtendedPlayerData data=ExtendedPlayerData.get(player);
		if(data==null)return null;
		data.soulFlame=now;
		data.maxSoulFlame=max;
		data.setJupmCount(count);
		data.setReducedFallDamage(reducedFallDamage);
		return null;
		
	}

}
