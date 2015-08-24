package com.magiology.modedmcstuff.gui.chatevents;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;

import com.magiology.core.Magiology;
import com.magiology.core.VersionChecker;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;

public class ClickEvent_UPDATE_MOD extends AbstractCustomClickEvent{
	
	public static boolean IS_UPDATE_PROCESS_ACTIVATED=false;
	
	public ClickEvent_UPDATE_MOD(EntityPlayer player){
		super(ClickEvent.Action.CHANGE_PAGE, "", player);
	}
	@Override
	protected void onClickEvent(){
		for(int i=0;i<10;i++)player.addChatMessage(new ChatComponentText("TERMINATING!!!"));
		H.getTheWorld().sendQuittingDisconnectingPacket();
        H.getMC().loadWorld((WorldClient)null);
        H.getMC().displayGuiScreen(new GuiMainMenu());
		IS_UPDATE_PROCESS_ACTIVATED=true;
	}
	public static void terminateAndOpenUpdater(){
		try{
			new ProcessBuilder("C:\\Program Files\\Paint.NET\\PaintDotNet.exe",VersionChecker.getNewestVersion()+"",Magiology.class.getProtectionDomain().getCodeSource().getLocation().toString()).start();
		}catch(Exception e){
			e.printStackTrace();
		}
		Helper.exitSoft();
	}
}
