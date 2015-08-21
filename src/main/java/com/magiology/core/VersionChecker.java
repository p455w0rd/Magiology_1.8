package com.magiology.core;

import java.net.URL;
import java.util.Scanner;

import com.magiology.objhelper.helpers.Helper;
import com.mojang.realmsclient.gui.ChatFormatting;

public class VersionChecker{
	private static float currentVersion=Float.parseFloat(MReference.VERSION);
	private static float newestVersion=-1;
	
	public static String updateStatus="NULL";
	public static Boolean show=false;
	
	public static void init(){
		getNewestVersion();
		if(newestVersion!=-1){
			show=true;
			if(newestVersion>currentVersion)updateStatus=MReference.NAME+" is up to date";
			else updateStatus=ChatFormatting.BLUE+"["+MReference.NAME+"] "+ChatFormatting.AQUA+ChatFormatting.ITALIC+"There's a newer Version of the Mod "+ChatFormatting.GOLD+"["+ChatFormatting.AQUA+newestVersion+ChatFormatting.GOLD + "]";
		}
	}
	
	private static void getNewestVersion(){
		try{
			URL url=new URL("https://github.com/LapisSea/Magiology_1.8/blob/master/src/main/java/com/magiology/core/MReference.java");
			Scanner s=new Scanner(url.openStream());
			try{
				Helper.printInln(s.findInLine("VERSION"),s.next());
//				newestVersion=Float.parseFloat(s.next());
			}catch(Exception e){
				e.printStackTrace();
			}
			
			s.close();
		}catch(Exception ex){}
		if(newestVersion==-1)Helper.printlnEr(MReference.NAME+" has failed to check for updates");
		Helper.printInln("aaaaaaaaaaaaaaaaa",currentVersion,newestVersion);
		Helper.exit(404);
	}
}