package com.magiology.core;

import static com.mojang.realmsclient.gui.ChatFormatting.*;

import java.net.URL;
import java.util.Scanner;

import com.magiology.objhelper.helpers.Helper;

public class VersionChecker{
	private static float currentVersion=Float.parseFloat(MReference.VERSION);
	private static float newestVersion=-1;
	
	public static String updateStatus="NULL";
	public static boolean show=false,foundNew;
	
	public static void init(){
		getNewestVersion();
		if(newestVersion!=-1){
			show=true;
			foundNew=newestVersion>currentVersion;
			if(foundNew)updateStatus=BLUE+"["+MReference.NAME+"] "+AQUA+ITALIC+"There's a newer Version of the Mod "+GOLD+"["+AQUA+newestVersion+GOLD + "]";
			else updateStatus=MReference.NAME+" is up to date.";
			updateStatus+=AQUA+" Latest version is: "+GOLD+newestVersion+AQUA+", and you are using version: "+GOLD+currentVersion;
		}
	}
	private static void getNewestVersion(){
		try{
			URL url=new URL("https://raw.githubusercontent.com/LapisSea/Magiology_1.8/master/src/main/java/com/magiology/core/MReference.java");
			Scanner s=new Scanner(url.openStream());
			try{
				s.findWithinHorizon("VERSION=", 9999);
				String version=s.next();
				version=version.substring(1, version.length()-2);
				newestVersion=Float.parseFloat(version);
			}catch(Exception e){}
			s.close();
		}catch(Exception ex){}
		if(newestVersion==-1)Helper.printlnEr(MReference.NAME+" has failed to check for updates");
	}
}