package com.magiology.core;

import static com.magiology.core.MReference.*;
import static com.magiology.util.utilclasses.UtilM.*;

import java.awt.*;
import java.io.*;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.api.lang.program.*;
import com.magiology.client.gui.gui.*;
import com.magiology.client.render.*;
import com.magiology.core.init.*;
import com.magiology.forgepowered.proxy.*;
import com.magiology.handlers.*;
import com.magiology.handlers.web.*;
import com.magiology.io.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.m_extension.*;
import com.magiology.windowsgui.*;

@Mod(modid=MODID,version=VERSION,name=NAME,acceptedMinecraftVersions=ACCEPTED_MC_VERSION)
public class Magiology{
	
	/***//**variables*//***/
	
	
	public static SimpleNetworkWrapper NETWORK_CHANNEL;
	
	//TODO: change this when you compile the mod! (you dumbass)
	public static final boolean IS_DEV=true;
	
//	@Instance(value=MODID)
	private static Magiology instance;
	
	public static IOReadableMap infoFile;
	
	@SideOnly(Side.CLIENT)
	public static ModInfoGUI modInfGUI;
	
	@SidedProxy(clientSide=MReference.ClIENT_PROXY_LOCATION, serverSide=MReference.SERVER_PROXY_LOCATION)
	public static CommonProxy proxy;
	
	public static EnhancedRobot ROBOT;
	
	
	/**_xXx__init, stuff and things_xXx_**/
	
	
	public Magiology(){
		testOnStartup();
		instance=this;
		infoFile=new IOReadableMap(INFO_FILE_NAME);
		
		EnhancedRobot robotH=null;
		try{
			robotH=new EnhancedRobot();
		}catch(Exception e){
			throw new NullPointerException((UtilM.RB(0.1)?"CRAP! :(":"")+" "+NAME+" has encountered with a problem while trying to initialize the input robot! This might be the result of incompatible hardware or java version.");
		}
		ROBOT=robotH;
	}
	public void loadFiles(){
		new File(MODS_SUBFOLDER_WIN_GUI).mkdir();
		if(!new File(MODS_SUBFOLDER_WIN_GUI+"/MagiZip.zip").exists())DownloadingHandler.downladAssets();
		infoFile.readFromFile();
		UtilM.println(infoFile.getB("GUIOpen", true));
		if(infoFile.getB("GUIOpen", true)){
			Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
			modInfGUI=new ModInfoGUI((int)screenSize.getWidth(),(int)screenSize.getHeight(),-680,0);
			modInfGUI.downloadData(infoFile);
		}
	}
	public void preInit(FMLPreInitializationEvent event){
		MUpdater.init();
//		//TODO
//		Config.setShadersEnabled(false);
		
		MCreativeTabs.preInit();
		MGui.preInit();
		MBlocks.preInit();
		MItems.preInit();
		MPackets.preInit();
	}
	public void init(FMLInitializationEvent event){
		MRecepies.init();
		MTileEntitys.init();
		proxy.registerProxy();
		MEntitys.init();
		MEvents.init();
		MInterfaces.init();
	}
	public void postInit(FMLPostInitializationEvent event){
		if(modInfGUI!=null)modInfGUI.modStat=true;
		Textures.postInit();
		
		ProgramDataBase.loadClass();
		GuiProgramContainerEditor.loadClass();
	}
	public void exit(){
		if(modInfGUI!=null)SoundPlayer.playSound(MODS_SUBFOLDER_WIN_GUI+"/Close.wav");
		Magiology.infoFile.writeToFile();
	}
	
	public static Magiology getMagiology(){
		return instance;
	}
	public boolean isWindowOpen(){
		return modInfGUI!=null&&!modInfGUI.isExited;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
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
			"Is FNAF 50 the sequel of a prequel of a generic prequel out yet? Oh... And is COD advanced zombie warfare DLC edition extra for iPhone 8s-a out? No? Maybe Portal 3? NO!? Darn it!",
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
//		format.calc(5F,new float[]{1.7F,1.7F,1.7F},new long[]{1,2,3},1);
//		long calcTime1=0,calcTime2=0;
//		for(int i=0;i<1000000;i++){
//			{
//				long start=System.nanoTime();
//				format.calc(5F,new float[]{1.7F,1.7F,1.7F},new long[]{1,2,3},1);
//				calcTime1+=System.nanoTime()-start;
//			}{
//				long start=System.nanoTime();
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
//				calcTime2+=System.nanoTime()-start;
//			}
//		}
//		Util.printlnInln(calcTime1,calcTime2,(double)calcTime1/(double)calcTime2);
//	    try{
//	    	long timeStart=System.currentTimeMillis();
//	    	ScriptEngine engine=new ScriptEngineManager(null).getEngineByName("nashorn");
//	    	for(int i=0;i<10;i++)Util.printInln(engine.eval("function sum() { return Math.random(); }sum();"));
//	    	Util.printInln(System.currentTimeMillis()-timeStart);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		try{
//		       new ScriptEngineManager(null).getEngineByName("nashorn").eval("var EmptyClass = Java.type('"+EmptyClass.class.getName()+"');\nnew EmptyClass().lol();");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		Util.exit(404);
	}
	
	
	@EventHandler
    public void preInitStarter(FMLPreInitializationEvent event){
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
    			+ " The paradox will effectively crash the Genetic Lifeform and Disk Operating System. Do not listen to it! It may quote Moby Dick! #ReferenceInception"));
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
