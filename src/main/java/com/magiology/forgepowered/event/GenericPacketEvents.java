package com.magiology.forgepowered.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.magiology.api.power.ISidedPower;
import com.magiology.client.gui.container.ISidedPowerInstructorContainer;
import com.magiology.client.gui.container.SmartCrafterContainer;
import com.magiology.handlers.GenericPacketEventHandler;
import com.magiology.handlers.GenericPacketEventHandler.IntegerPacketEvent;
import com.magiology.handlers.GenericPacketEventHandler.PacketEvent;
import com.magiology.handlers.GenericPacketEventHandler.StringPacketEvent;
import com.magiology.handlers.animationhandlers.TheHandHandler;
import com.magiology.handlers.animationhandlers.WingsFromTheBlackFireHandler;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.Util;

public class GenericPacketEvents{
	public static GenericPacketEventHandler callerInstance;
	public void intPacketEvent(IntegerPacketEvent event){
		EntityPlayer player=event.player;
		int integer=event.integer;
		try{switch (event.eventId){
		case 0:{
			if(!(player.openContainer instanceof SmartCrafterContainer))break;
			boolean doIt=true;
			int listOffset=((SmartCrafterContainer)player.openContainer).listOffset;
			if(integer<0){if(listOffset+integer<0)doIt=false;}
			else{if(listOffset+integer>((SmartCrafterContainer)player.openContainer).tileSC.wantedProducts.length-2)doIt=false;}
	        if(doIt)((SmartCrafterContainer)player.openContainer).listOffset+=integer;
		}break;
		case 1:{
			((SmartCrafterContainer)player.openContainer).tileSC.wantedProducts[integer].clear();
			((SmartCrafterContainer)player.openContainer).tileSC.wantedProducts[integer].ammountWanted=0;
		}break;
		case 2:{
			((SmartCrafterContainer)player.openContainer).tileSC.wantedProducts[((SmartCrafterContainer)player.openContainer).listOffset].ammountWanted=integer;
		}break;
		case 3:{
			((SmartCrafterContainer)player.openContainer).tileSC.wantedProducts[((SmartCrafterContainer)player.openContainer).listOffset+1].ammountWanted=integer;
		}break;
		case 5:{
			//wings has space update
			if(WingsFromTheBlackFireHandler.getIsActive(player))player.getCurrentArmor(2).getTagCompound().setBoolean("HS", integer==1);
		}break;
		case 6:{
			WingsFromTheBlackFireHandler.setPosId(player, integer);
		}break;
		case 7:{
			TileEntity tileEn=((ISidedPowerInstructorContainer)player.openContainer).tile;
			PowerUtil.cricleSideInteraction((ISidedPower)tileEn, integer);
			ForcePipeUpdate.updatein3by3(player.worldObj, tileEn.getPos());
		}break;
		case 8:{
			TileEntity tileEn=((ISidedPowerInstructorContainer)player.openContainer).tile;
			ISidedPower tile=(ISidedPower)tileEn;
			tile.setSendOnSide(integer, !tile.getOut(integer));
			ForcePipeUpdate.updatein3by3(player.worldObj, tileEn.getPos());
		}break;
		case 9:{
			TileEntity tileEn=((ISidedPowerInstructorContainer)player.openContainer).tile;
			ISidedPower tile=(ISidedPower)tileEn;
			tile.setReceaveOnSide(integer, !tile.getIn(integer));
			ForcePipeUpdate.updatein3by3(player.worldObj, tileEn.getPos());
		}break;
		default:{Util.println("ERROR! EVENT IntegerPacketEvent HAS BEEN RAN WITH A INVALID EVENT ID!","PLEASE ADD THE ID TO THE SWITCH IN THE EVENT HANDLER!");}break;
		}}catch(Exception e){e.printStackTrace();}
	}
	
	public void stringPacketEvent(StringPacketEvent event){
		EntityPlayer player=event.player;
		String string=event.string;
		try{switch (event.eventId){
		case 0:{
			int x=0,y=0,xTest,yTest;
			char[] chars=string.toCharArray();
			xTest=Integer.parseInt(chars[0]+"");
			yTest=Integer.parseInt(chars[2]+"");
			switch(xTest){case 0:x=0;break;case 1:x=1;break;case 2:x=-1;break;case 3:x=0;break;}
			switch(yTest){case 0:y=0;break;case 1:y=1;break;case 2:y=-1;break;case 3:y=0;break;}
			SpecialMovmentEvents.instance.doubleJumpEvent(player,x,y);
		}break;
		case 1:{
			
		}break;
		default:{Util.println("ERROR! EVENT StringPacketEvent HAS BEEN RAN WITH A INVALID EVENT ID!","PLEASE ADD THE ID TO THE SWITCH IN THE EVENT HANDLER!");}break;
		}}catch(Exception e){e.printStackTrace();}
	}
	public void voidPacketEvent(PacketEvent event){
		EntityPlayer player=event.player;
		try{switch (event.eventId){
		case 0:{
			TheHandHandler.nextPosition(player);
		}break;
		default:{Util.println("ERROR! EVENT voidPacketEvent HAS BEEN RAN WITH A INVALID EVENT ID!","PLEASE ADD THE ID TO THE SWITCH IN THE EVENT HANDLER!");}break;
		}}catch(Exception e){e.printStackTrace();}
	}
}
