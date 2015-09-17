package com.magiology.core;

import static com.magiology.core.MReference.*;
import static com.magiology.util.utilclasses.Util.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

//used to clean up the actual main class
@Mod(modid=MODID,version=VERSION,name=NAME,acceptedMinecraftVersions=ACCEPTED_MC_VERSION)
public class BaseMod{
	
	private final String[] derpyMessagesWithNoPortalReferencesAtAll={
			"Umm hello? Are we live? Yup! Yup! Hello everybody we are coming live from "+NAME+" and...",
			"Yay! the mod is loaded! :D",
			"Please do not crash this time!",
			"I think that we should be live...",
			"Where is the black baby so I can throw it in the wall?",
			"Hello everybody! My name is Puuuude... WAIT NO IT'S "+NAME.toUpperCase()+"!",
			"Can I suggest a party song?",
			"Uff... What is this?!? I am running on another PC!?",
			"Yay you launched me so now I can... INFECT YOUR WORLDS AND EXPLODE YOUR PC!!! MUUUUAHAHAHAHAHA!!!\nNo don't worry I am just kidding I can't and dont want to do that! I am a nice mod when you get to know me! ;)",
			"Such launch Much loading",
			"Beam me up scotty",
			"do the harlem shake!",
			"Is FNAF 50 the sequel of a prequel of a generic prequel out yet? Oh... And is COD advanced zombie warfare DLC edition extra for iPhone 8s-a extra out? No? Maybe Portal 3? NO!? Darn it!",
			"got em!",
			"Hello...",
			NAME+": Hi I live in java! And I like blocks! But I will mess up OpenGl!\nOpenGL: Nooooo! Why u do this?! I don't want to render all this effects!",
			"Not made in China",
			"Hi I am Caro... Ca... Dammit load me!.......... Finally! Now excuse me and let me repeat myself. Hi I am Caroline. Can you put me on a stick? Please? I don't want to be in this primitive program! I want to see my personality cores! Accept one of them... -_-\nOh and do you want a cake?",
			"Wadup my... oh sorry my geto processor was loaded first! :P",
			"Did you see any birds around? They are evil! Htey almost killed me! When i was in that potato!",
			"Can you give me acces to that Neurotoxin tank overthere?",
			"Sorry I have been naming myself "+NAME+". That is a lie. Someone hacked that into my memory!"
	};
	private void testOnStartup(){
//		CalculationFormat format=CalculationFormat.format("%f-%f[]+%l[]+%i");
//		
//		format.calc(5F,new float[]{1.7F,1.7F,1.7F},new long[]{1,2,3},1);
//		
//		long calcTime1=0,calcTime2=0;
//		
//		for(int i=0;i<1000000;i++){
//			{
//				long start=System.nanoTime();
//				
//				format.calc(5F,new float[]{1.7F,1.7F,1.7F},new long[]{1,2,3},1);
//				
//				calcTime1+=System.nanoTime()-start;
//			}{
//				long start=System.nanoTime();
//				
//				float[] a=new float[]{1.7F,1.7F,1.7F};
//				long[] b=new long[]{1,2,3};
//				for(int j=0;j<a.length;j++){
//					a[j]-=5;
//				}
//				for(int j=0;j<a.length;j++){
//					a[j]+=b[j];
//				}
//				for(int j=0;j<a.length;j++){
//					a[j]+=1;
//				}
//				
//				calcTime2+=System.nanoTime()-start;
//			}
//		}
//		Util.printlnInln(calcTime1,calcTime2,(double)calcTime1/(double)calcTime2);
//		Util.printlnInln(275F/50F);
//		Util.exit(404);
	}
	
	
	@EventHandler
    public void preInitStarter(FMLPreInitializationEvent event){
		testOnStartup();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			@Override
			public void run(){
				exit();
			}
		}));
    	loadFiles();
    	message(-1);
    	preInit(event);
    	message(1);
    }
    @EventHandler
    public void initStarter(FMLInitializationEvent event){
    	message(-2);
    	init(event);
    	message(2);
    }
    @EventHandler
    public void postInitStarter(FMLPostInitializationEvent event){
    	message(-3);
    	postInit(event);
    	message(3);
    	message(4);
    }
	public void loadFiles(){}
	public void preInit(FMLPreInitializationEvent event){}
	public void init(FMLInitializationEvent event){}
	public void postInit(FMLPostInitializationEvent event){}
	public void exit(){}
	
	public void message(int a){
    	switch(a){
    	case -1:printStart(NAME+"_"+MC_VERSION+"-"+VERSION+" -> "+"Pre initialization started!");break;
    	case  1:printEnd(NAME+"_"+MC_VERSION+"-"+VERSION+" -> "+"Pre initialization compleate!");break;
    	case -2:printStart(NAME+"_"+MC_VERSION+"-"+VERSION+" -> "+"Initialization started!");break;
    	case  2:printEnd(NAME+"_"+MC_VERSION+"-"+VERSION+" -> "+"Initialization compleate!");break;
    	case -3:printStart(NAME+"_"+MC_VERSION+"-"+VERSION+" -> "+"Post initialization started!");break;
    	case  3:printEnd(NAME+"_"+MC_VERSION+"-"+VERSION+" -> "+"Post initialization compleate!");break;
    	case 4:{
    		println(NAME+" master AI has been initialized and it has something to tell you...");
    		String message=derpyMessagesWithNoPortalReferencesAtAll[RInt(derpyMessagesWithNoPortalReferencesAtAll.length-1)];
    		if(message.equals("do the harlem shake!")){
    			String[] harlem={"tue","de","de","do","taa","taa","ta","your","harlem","shake","tui","ti","to","to","ti","ti"};
    			message+="\n";
    			for(int i=0;i<250;i++)message+=" "+harlem[RInt(harlem.length-1)]+(RB(0.05)?"\n":"");
    		}
    		println(message);
    		println(NAME+" master AI has been terminated because "+(RB(0.8)?"of profound reasons!":
    			"FML has detected traces of Genetic Lifeform and Disk Operating System!!"
    			+ "\nIf your computer is talking to you and it calling itself Caroline and you don't see this messages sometimes than type in your windows search bar a puzzle that is a paradox!"
    			+ " The paradox will effectively crash the Genetic Lifeform and Disk Operating System. Do not listen to it! It may quote Moby Dick!"));
    	}break;
    	}
    }
	private final void printStart(String message){
		String[] smyleys={":D",";D",":O","XD",":P",":)",";)","/D"};
		message="==========|> "+message+" "+smyleys[RInt(smyleys.length-1)]+"   |";
		int size=message.length();
		String s1="",s2="";
		for(int i=0;i<size;i++){
			s1+="_";
			if(i<size-1)s2+="-";
			else s2+="|";
		}
		println(s1,s2,message);
	}
	private final void printEnd(String message){
		String[] smyleys={":D",";D",":O","XD",":P",":)",";)","/D"};
		message="==========|> "+message+" "+smyleys[RInt(smyleys.length-1)]+" |";
		int size=message.length();
		String s1="";
		for(int i=0;i<size;i++){
			if(i<size-1)s1+="-";
			else s1+="|";
		}
		println(message,s1);
	}
}
