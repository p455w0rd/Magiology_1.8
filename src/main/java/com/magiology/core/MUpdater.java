package com.magiology.core;

import static com.mojang.realmsclient.gui.ChatFormatting.*;

import com.magiology.handelers.web.DownloadingHandeler;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;

public class MUpdater{
	private static float currentVersion=Float.parseFloat(MReference.VERSION),newestVersion=-1;
	private static String extraData;
	private static boolean show=false,foundNew;
	public static boolean debug=false;
	
	public static void init(){
		
//		debug=true;
		try{
			newestVersion=Float.parseFloat(DownloadingHandeler.findValue("VERSION"));
		}catch(Exception e){
			e.printStackTrace();
		}
		if(newestVersion==-1)Util.printlnEr(MReference.NAME+" has failed to check for updates");
		else{
			show=true;
			foundNew=newestVersion>currentVersion;
			extraData=U.signature(AQUA)+"Latest version is: "+GOLD+newestVersion+AQUA+", and you are using version: "+GOLD+currentVersion;
		}
	}
	public static float getCurrentVersion(){return currentVersion;}
	public static float getNewestVersion(){return newestVersion;}
	public static String getUpdateStatus(){
		if(!foundNew)return U.signature(AQUA,ITALIC)+"Found a newer version! "+RESET+GOLD+"["+BLUE+"More info"+GOLD+"]";
		else return U.signature(AQUA,ITALIC)+"I am up to date! "+BLUE+";)";
	}
	public static String getExtraData(){return extraData;}
	public static boolean getFoundNew(){return foundNew||debug;}
	public static boolean getShow(){return show;}
}