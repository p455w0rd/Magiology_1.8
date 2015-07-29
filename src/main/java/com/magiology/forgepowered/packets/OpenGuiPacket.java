package com.magiology.forgepowered.packets;

import java.io.IOException;

import com.magiology.core.Magiology;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;

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
	public void process(EntityPlayer player, Side side){
		FMLNetworkHandler.openGui(player, Magiology.getMagiology(), id, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
	}
}
