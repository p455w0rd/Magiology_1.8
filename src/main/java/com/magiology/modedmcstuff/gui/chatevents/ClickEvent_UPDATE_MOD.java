package com.magiology.modedmcstuff.gui.chatevents;

import static com.magiology.core.MReference.*;

import java.awt.Desktop;
import java.io.File;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;

import com.magiology.handelers.web.DownloadingHandeler;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;

public class ClickEvent_UPDATE_MOD extends AbstractCustomClickEvent{
	
	public static boolean IS_UPDATE_PROCESS_ACTIVATED=false,IS_DOWNLOADING_UPDATER=false,IS_DOWNLOADING_UPDATER_PREV=false;
	
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
			File updater=new File(UPDATER_DIR);
			if(updater.exists()){
				Desktop.getDesktop().open(updater);
			}else{
				DownloadingHandeler.downloadUpdater();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		Helper.exitSoft();
	}
	
	public static void update(){
		if(IS_UPDATE_PROCESS_ACTIVATED&&H.getMC().currentScreen instanceof GuiMainMenu)terminateAndOpenUpdater();
	}
}
