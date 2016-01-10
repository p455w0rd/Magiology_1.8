package com.magiology.handlers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import scala.collection.mutable.StringBuilder;

import com.magiology.util.utilclasses.UtilM;

import net.minecraft.entity.player.EntityPlayer;

public class PremiumHandeler{
	
	private static Map<String, PlayerPremiumData> data=new HashMap<String, PlayerPremiumData>();
	
	private static final boolean canAccesInternet=testInternet();
	
	public static final PremiumHandeler instance=new PremiumHandeler();
	private PremiumHandeler(){}
	
	
	
	public static boolean isPremium(EntityPlayer player){
		return get(player).isPremium;
	}
	
	public static boolean canAccesInternet(){
		return canAccesInternet;
	}
	
	public static boolean hasOfflineUUID(EntityPlayer player){
		return get(player).isOfflineUUID;
	}
	
	public static String toString(EntityPlayer player){
		return get(player).toString();
	}
	
	public static Map<String, PlayerPremiumData> getAll(){
		final Map<String, PlayerPremiumData> result=new HashMap<String, PlayerPremiumData>();
		data.entrySet().forEach(entry->result.put(entry.getKey(), entry.getValue().copy()));
		return result;
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
	
	private static boolean testInternet(){
		boolean result=false;
		try{
			if(InetAddress.getByName("minecraft.net")!=null)result=true;
		}catch(UnknownHostException e){}
		return result;
	}
	private static String name(EntityPlayer player){
		return player.getGameProfile().getName();
	}
	
	private static PlayerPremiumData generateData(EntityPlayer player){
		
		UUID
			premiumUUID=player.getGameProfile().getId(),
			offlineUUID=player.getOfflineUUID(player.getGameProfile().getName());
		
		boolean isOfflineUUID=premiumUUID.equals(offlineUUID);
		
		boolean isPremium=canAccesInternet?!isOfflineUUID:false;
		
		PlayerPremiumData result=instance.new PlayerPremiumData(isOfflineUUID,isPremium);
		
		UtilM.println("Player:",name(player),"has been checked for premium!\nThe results are:\n",result);
		return result;
	}
	
	
	
	
	public class PlayerPremiumData{
		private boolean isOfflineUUID,isPremium;
		
		private PlayerPremiumData(boolean isOfflineUUID, boolean isPremium){
			this.isOfflineUUID=isOfflineUUID;
			this.isPremium=isPremium;
		}
		
		private PlayerPremiumData copy(){
			return instance.new PlayerPremiumData(isOfflineUUID, isPremium);
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
