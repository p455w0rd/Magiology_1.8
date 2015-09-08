package com.magiology.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import com.magiology.util.utilclasses.Helper.H;

public class GuiUpdater{
	static Minecraft mc=H.getMC();
	private static GuiUpdater instance;
	public static GuiUpdater GetInstace(){return instance;}
	public GuiUpdater(){instance=this;}
	
	public static void tryToUpdate(EntityPlayer player){
		if(player==null)return;
		try{
			if(player.openContainer instanceof Updateable)((Updateable)player.openContainer).update();
			if(H.isRemote(player)&&H.getMC().currentScreen instanceof Updateable)((Updateable)H.getMC().currentScreen).update();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static interface Updateable{
		public void update();
	}
}
