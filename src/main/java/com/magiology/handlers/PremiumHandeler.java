package com.magiology.handlers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import scala.collection.mutable.StringBuilder;

import com.magiology.util.utilclasses.UtilM;

import net.minecraft.entity.player.EntityPlayer;

public class PremiumHandeler{
	
	private static Map<EntityPlayer, boolean[]> data=new HashMap<EntityPlayer, boolean[]>();
	
	public static boolean isPremium(EntityPlayer player){
		player=check(player);
		return data.get(player)[2];
	}
	
	public static boolean hasInternetConnection(EntityPlayer player){
		player=check(player);
		return data.get(player)[1];
	}
	
	public static boolean hasOfflineUUID(EntityPlayer player){
		player=check(player);
		return data.get(player)[0];
	}
	
	public static String toString(EntityPlayer player){
		player=check(player);
		StringBuilder result=new StringBuilder();
		result.append("Is unique id generated from username: ").append(hasOfflineUUID(player)+"\n");
		result.append("Can access internet: ").append(hasInternetConnection(player)+"\n");
		result.append("Is premium: ").append(isPremium(player)+"\n");
		return result.toString();
	}
	
	private static EntityPlayer check(EntityPlayer player){
		EntityPlayer player0=find(player);
		if(player0==null){
			fill(player);
			return player;
		}
		return player0;
	}
	
	private static EntityPlayer find(EntityPlayer player){
		if(data.containsKey(player))return player;
		for(Entry<EntityPlayer, boolean[]> data:data.entrySet())if(player.getGameProfile().getName()==data.getKey().getGameProfile().getName())return data.getKey();
		return null;
	}
	
	private static void fill(EntityPlayer player){
		data.put(player, generateData(player));
	}
	
	private static boolean[] generateData(EntityPlayer player){
		
		boolean isOfflineUUID=player.getGameProfile().getId().equals(player.getOfflineUUID(player.getGameProfile().getName())),canAccesInternet=false;
//		UtilM.println("",isOfflineUUID+", :",canAccesInternet+", :",isPremium);
		try{
			if(InetAddress.getByName("minecraft.net")!=null)canAccesInternet=true;
        }catch(Exception e){}
		
		boolean isPremium=canAccesInternet?!isOfflineUUID:false;
		
		return new boolean[]{
			isOfflineUUID,
			canAccesInternet,
			isPremium
		};
	}
	
}
