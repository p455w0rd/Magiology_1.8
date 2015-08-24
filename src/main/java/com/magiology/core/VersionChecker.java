package com.magiology.core;

import static com.mojang.realmsclient.gui.ChatFormatting.*;

import com.magiology.handelers.DownloadingHandeler;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;

public class VersionChecker{
	private static float currentVersion=Float.parseFloat(MReference.VERSION),newestVersion=-1;
	private static String extraData;
	private static boolean show=false,foundNew;
	
	public static void init(){
		getVersion();
		if(newestVersion!=-1){
			show=true;
			foundNew=newestVersion>currentVersion;
			extraData=H.signature(AQUA)+"Latest version is: "+GOLD+newestVersion+AQUA+", and you are using version: "+GOLD+currentVersion;
		}
	}
	private static void getVersion(){
		newestVersion=Float.parseFloat(DownloadingHandeler.findValue("VERSION"));
		if(newestVersion==-1)Helper.printlnEr(MReference.NAME+" has failed to check for updates");
	}
	public static float getCurrentVersion(){return currentVersion;}
	public static float getNewestVersion(){return newestVersion;}
	public static String getUpdateStatus(){
		if(!foundNew)return H.signature(AQUA,ITALIC)+"Found a newer version! "+RESET+GOLD+"["+BLUE+"More info"+GOLD+"]";
		else return H.signature(AQUA,ITALIC)+"I am up to date! "+BLUE+";)";
	}
	public static String getExtraData(){return extraData;}
	public static boolean getFoundNew(){return foundNew;}
	public static boolean getShow(){return show;}
}