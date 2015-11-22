package com.magiology.client.gui;

import static com.magiology.util.utilclasses.UtilM.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import com.magiology.api.updateable.Updater;

public class GuiUpdater{
	private static GuiUpdater instance;
	public static GuiUpdater GetInstace(){return instance;}
	public GuiUpdater(){instance=this;}
	
	public static void tryToUpdate(EntityPlayer player){
		if(player==null)return;
		List objects=new ArrayList();
		objects.add(player.openContainer);
		if(isRemote(player))objects.add(getMC().currentScreen);
		Updater.update(objects);
	}
	public static interface Updateable{
		public void update();
	}
}
