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
	
	private static Map<String, PlayerPremiumData> data=new HashMap<String, PlayerPremiumData>();
	
	
	
	public static boolean isPremium(EntityPlayer player){
		return get(player).isPremium;
	}
	
	public static boolean hasInternetConnection(EntityPlayer player){
		return get(player).canAccesInternet;
	}
	
	public static boolean hasOfflineUUID(EntityPlayer player){
		return get(player).isOfflineUUID;
	}
	
	public static String toString(EntityPlayer player){
		return get(player).toString();
	}
	
	
	
	private static PlayerPremiumData get(EntityPlayer player){
		check(player);
		return data.get(name(player));
	}
	
	private static void check(EntityPlayer player){
		if(!data.containsKey(name(player)))fill(player);
	}
	
	
	private static void fill(EntityPlayer player){
		data.put(name(player), generateData(player));
	}
	
	private static PlayerPremiumData generateData(EntityPlayer player){
		
		boolean isOfflineUUID=player.getGameProfile().getId().equals(player.getOfflineUUID(player.getGameProfile().getName())),canAccesInternet=false;
		try{
			if(InetAddress.getByName("minecraft.net")!=null)canAccesInternet=true;
        }catch(Exception e){}
		
		boolean isPremium=canAccesInternet?!isOfflineUUID:false;
		PlayerPremiumData result=new PlayerPremiumData(isOfflineUUID,canAccesInternet,isPremium);
		UtilM.println("Player:",name(player),"has been checked for premium!\nThe results are:\n",result);
		return result;
	}
	
	private static String name(EntityPlayer player){
		return player.getGameProfile().getName();
	}
	
	private static class PlayerPremiumData{
		private boolean isOfflineUUID,canAccesInternet,isPremium;
		
		public PlayerPremiumData(boolean isOfflineUUID, boolean canAccesInternet, boolean isPremium){
			this.isOfflineUUID=isOfflineUUID;
			this.canAccesInternet=canAccesInternet;
			this.isPremium=isPremium;
		}
		@Override
		public String toString(){
			StringBuilder result=new StringBuilder();
			result.append("PlayerPremiumData(").append("\n");
			result.append("\t").append("Is unique id generated from username: ").append(isOfflineUUID).append("\n");
			result.append("\t").append("Can access internet: ").append(canAccesInternet).append("\n");
			result.append("\t").append("Is premium: ").append(isPremium).append("\n");
			result.append(")");
			return result.toString();
		}
	}
}
