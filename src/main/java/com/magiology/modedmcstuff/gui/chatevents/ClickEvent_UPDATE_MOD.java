package com.magiology.modedmcstuff.gui.chatevents;

import java.io.File;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;

import com.magiology.core.MReference;
import com.magiology.core.Magiology;
import com.magiology.handelers.DownloadingHandeler;
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
		IS_UPDATE_PROCESS_ACTIVATED=false;
		try{
			if(new File(MReference.UPDATER_DIR).exists()){
				new ProcessBuilder(MReference.UPDATER_DIR,DownloadingHandeler.findValue("NEWEST_VERSION_URL"),Magiology.class.getProtectionDomain().getCodeSource().getLocation().toString()).start();
			}else DownloadingHandeler.downloadUpdater();
		}catch(Exception e){
			e.printStackTrace();
		}
		Helper.exitSoft();
	}
}
