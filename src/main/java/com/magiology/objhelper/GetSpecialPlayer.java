package com.magiology.objhelper;

import net.minecraft.entity.player.EntityPlayer;

public final class GetSpecialPlayer{
	
	private static final String   owner="LapisSea";
	private static final String[] Youtubers={"BevoLJ","direwolf20","Etho","Docm","Pahimar","Nathaniel1985","Soaryn"},
								  friend={"WolfBurnsMC","OnajPederTamo","MartinKitten"};
	
	public static int getPlayerRank(EntityPlayer player){
		String plName=player.getDisplayName();
		
		if(plName.equals(owner))return 1;
		
		for(int i=0;i<Youtubers.length;i++)if(plName.equals(Youtubers[i]))return 2;
		
		for(int i=0;i<friend.length;i++)if(plName.equals(friend[i]))return 3;
		
		return -1;
	}
	
	
	
}
