package com.magiology.util.utilobjects.m_extension;

import com.magiology.util.utilclasses.*;

import net.minecraft.entity.player.*;
import net.minecraftforge.fml.common.network.NetworkRegistry.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class SimpleNetworkWrapperM extends SimpleNetworkWrapper{
	
	private boolean willPrint=false;
	
	public SimpleNetworkWrapperM willPrint(boolean willPrint){
		this.willPrint=willPrint;
		return this;
	}
	
	public SimpleNetworkWrapperM(String channelName){
		super(channelName);
	}
	
	@Override
	public void sendTo(IMessage message, EntityPlayerMP player){
		onMessage(message);
		super.sendTo(message, player);
	}
	@Override
	public void sendToAll(IMessage message){
		onMessage(message);
		super.sendToAll(message);
	}
	@Override
	public void sendToAllAround(IMessage message, TargetPoint point){
		onMessage(message);
		super.sendToAllAround(message, point);
	}
	@Override
	public void sendToDimension(IMessage message, int dimensionId){
		onMessage(message);
		super.sendToDimension(message, dimensionId);
	}
	@Override
	public void sendToServer(IMessage message){
		onMessage(message);
		super.sendToServer(message);
	}
	
	public void onMessage(IMessage message){
		if(!willPrint)return;
		PrintUtil.println(message);
		
		
	}
}
