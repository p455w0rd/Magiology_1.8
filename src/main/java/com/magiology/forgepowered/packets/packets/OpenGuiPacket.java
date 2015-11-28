package com.magiology.forgepowered.packets.packets;

import java.io.*;

import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.network.internal.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.core.*;
import com.magiology.forgepowered.packets.core.*;

public class OpenGuiPacket extends AbstractToServerMessage{
	protected int id;
	public OpenGuiPacket(){}
	public OpenGuiPacket(int id){
		this.id = id;
	}
	@Override
	public void read(PacketBuffer buffer)throws IOException{
		this.id=buffer.readInt();
	}
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		buffer.writeInt(id);
	}
	@Override 
	public IMessage process(EntityPlayer player, Side side){
		FMLNetworkHandler.openGui(player, Magiology.getMagiology(), id, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
		return null;
	}
}
