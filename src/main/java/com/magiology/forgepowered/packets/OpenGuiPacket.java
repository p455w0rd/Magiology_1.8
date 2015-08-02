package com.magiology.forgepowered.packets;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.magiology.core.Magiology;

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
