package com.magiology.core;

import static com.magiology.core.MReference.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.magiology.core.init.Init;
import com.magiology.core.init.MBlocks;
import com.magiology.core.init.MCreativeTabs;
import com.magiology.core.init.MEntitys;
import com.magiology.core.init.MEvents;
import com.magiology.core.init.MGui;
import com.magiology.core.init.MInterfaces;
import com.magiology.core.init.MItems;
import com.magiology.core.init.MPackets;
import com.magiology.core.init.MRecepies;
import com.magiology.core.init.MTileEntitys;
import com.magiology.forgepowered.proxy.CommonProxy;
import com.magiology.handelers.EnhancedRobot;
import com.magiology.handelers.GuiHandelerM;
import com.magiology.handelers.web.DownloadingHandeler;
import com.magiology.io.IOReadableMap;
import com.magiology.render.Textures;
import com.magiology.util.utilclasses.Util;
import com.magiology.windowsgui.ModInfoGUI;
import com.magiology.windowsgui.SoundPlayer;

@Mod(modid=MODID,version=VERSION,name=NAME,acceptedMinecraftVersions=ACCEPTED_MC_VERSION)
public class Magiology{
    public static SimpleNetworkWrapper NETWORK_CHANNEL;
    public static final EnhancedRobot ROBOT;
    //TODO: change this when you compile the mod! (you dumbass)
    public static final boolean IS_DEV=true;
    @Instance(MODID)
    private static Magiology instance;
    public static Magiology getMagiology(){return instance;}
    static{
    	EnhancedRobot robotH=null;
    	try{robotH=new EnhancedRobot();}catch(Exception e){
    		throw new NullPointerException((Util.RB(0.1)?"CRAP! :(":"")+" "+"Magiology"+" has encountered with a problem while trying to initialize the input robot! This might be the result of incompatible hardware or java version.");
		}
    	ROBOT=robotH;
    }
    
    public static IOReadableMap infoFile=new IOReadableMap(INFO_FILE_NAME);
    @SideOnly(Side.CLIENT)
    public static ModInfoGUI modInfGUI;
    
    
    //proxy cluster
    @SidedProxy(clientSide=MReference.ClIENT_PROXY_LOCATION, serverSide=MReference.COMMON_PROXY_LOCATION)
	public static CommonProxy proxy;
    public boolean modWindowOpen(){return modInfGUI!=null&&!modInfGUI.isExited;}
    
    public void loadFiles(){
    	
//    	CalculationFormat format=CalculationFormat.format("%f-%f[]+%l[]+%i");
//    	
//		format.calc(5F,new float[]{1.7F,1.7F,1.7F},new long[]{1,2,3},1);
//    	
//    	long calcTime1=0,calcTime2=0;
//    	
//    	for(int i=0;i<1000000;i++){
//    		{
//    			long start=System.nanoTime();
//    			
//    			format.calc(5F,new float[]{1.7F,1.7F,1.7F},new long[]{1,2,3},1);
//    			
//    			calcTime1+=System.nanoTime()-start;
//    		}{
//    			long start=System.nanoTime();
//    			
//    			float[] a=new float[]{1.7F,1.7F,1.7F};
//    			long[] b=new long[]{1,2,3};
//    			for(int j=0;j<a.length;j++){
//    				a[j]-=5;
//    			}
//    			for(int j=0;j<a.length;j++){
//    				a[j]+=b[j];
//    			}
//    			for(int j=0;j<a.length;j++){
//    				a[j]+=1;
//    			}
//    			
//    			calcTime2+=System.nanoTime()-start;
//    		}
//    	}
//    	Helper.printInln(calcTime1,calcTime2,(double)calcTime1/(double)calcTime2);
//    	Helper.printInln(275F/50F);
//    	H.exit(404);
    	
    	new File(MODS_SUBFOLDER_WIN_GUI).mkdir();
    	if(!new File(MODS_SUBFOLDER_WIN_GUI+"/MagiZip.zip").exists())DownloadingHandeler.downladAssets();
		infoFile.readFromFile();
    	
		if(infoFile.data.get("GUIOpen")==null||infoFile.getB("GUIOpen")){
	    	Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
	    	new ModInfoGUI((int)screenSize.getWidth(),(int)screenSize.getHeight(),-680,0);
	    	modInfGUI.downloadData(infoFile);
		}
    }
	public void preInit(FMLPreInitializationEvent event){
		VersionChecker.init();
		//TODO
		Config.setShadersEnabled(false);
		
		MCreativeTabs.preInit();
		MGui.preInit();
		MTileEntitys.setCustomRenderers();
		MBlocks.preInit();
		MItems.preInit();
		MPackets.preInit();
    }
    public void init(FMLInitializationEvent event){
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandelerM());
    	MRecepies.init();
    	MTileEntitys.init();
    	proxy.registerProxies();
    	MEntitys.init();
    	MEvents.init();
    	MInterfaces.init();
    }
    public void postInit(FMLPostInitializationEvent event){
    	if(modInfGUI!=null)modInfGUI.modStat=true;
		Textures.postInit();
    }
    public void exit(){
    	if(modInfGUI!=null)SoundPlayer.playSound(MODS_SUBFOLDER_WIN_GUI+"/Close.wav");
		Magiology.infoFile.writeToFile();
    }
    
    
    
    
    
    
    //You can't see this!
    public Magiology(){instance=this;}
    @EventHandler
    public void preInitStarter(FMLPreInitializationEvent event){
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			@Override
			public void run(){
				exit();
			}
		}));
    	loadFiles();
    	Init.Message(-1);
    	preInit(event);
    	Init.Message(1);
    }
    @EventHandler
    public void initStarter(FMLInitializationEvent event){
    	Init.Message(-2);
    	init(event);
    	Init.Message(2);
    }
    @EventHandler
    public void postInitStarter(FMLPostInitializationEvent event){
    	Init.Message(-3);
    	postInit(event);
    	Init.Message(3);
    	Init.Message(4);
    }
}
