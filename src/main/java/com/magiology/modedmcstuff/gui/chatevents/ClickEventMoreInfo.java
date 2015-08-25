package com.magiology.modedmcstuff.gui.chatevents;

import static com.mojang.realmsclient.gui.ChatFormatting.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;

import com.magiology.core.VersionChecker;

public class ClickEventMoreInfo extends AbstractCustomClickEvent{
	public ClickEventMoreInfo(EntityPlayer player){
		super(ClickEvent.Action.CHANGE_PAGE, "", player);
	}
	@Override
	protected void onClickEvent(){
		if(VersionChecker.getFoundNew()){
			player.addChatMessage(new ChatComponentText(GREEN+"Click "+GOLD+"["+AQUA+"HERE"+GOLD+"]"+GREEN+" to update the mod!").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent_UPDATE_MOD(player))));
			player.addChatMessage(new ChatComponentText(""+RED+BOLD+"WARNING: "+RESET+YELLOW+"The message above will:"));
			player.addChatMessage(new ChatComponentText(""+AQUA+BOLD+"1. "+RED+"turn off"+YELLOW+" minecraft!"));
			player.addChatMessage(new ChatComponentText(""+AQUA+BOLD+"2. "+YELLOW+"open an automatic updater aplication"));
			player.addChatMessage(new ChatComponentText(""+RESET+YELLOW+"The aplication will:"));
			player.addChatMessage(new ChatComponentText(""+AQUA+BOLD+"1. "+YELLOW+"remove the old mod file"));
			player.addChatMessage(new ChatComponentText(""+AQUA+BOLD+"2. "+YELLOW+"downlad a new one"));
			player.addChatMessage(new ChatComponentText(""+AQUA+BOLD+"3. "+YELLOW+"place it in your mods folder"));
		}
	}
}
