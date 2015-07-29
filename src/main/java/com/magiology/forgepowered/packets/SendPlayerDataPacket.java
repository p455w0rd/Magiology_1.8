package com.magiology.forgepowered.packets;

import java.io.IOException;

import com.magiology.mcobjects.entitys.ExtendedPlayerData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.relauncher.Side;

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
	public void process(EntityPlayer player, Side side){
		ExtendedPlayerData data=ExtendedPlayerData.get(player);
		if(data==null)return;
		data.soulFlame=now;
		data.maxSoulFlame=max;
		data.setJupmCount(count);
		data.setReducedFallDamage(reducedFallDamage);
		
	}

}
