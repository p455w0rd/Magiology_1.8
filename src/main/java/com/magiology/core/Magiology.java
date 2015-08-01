package com.magiology.core;

import static com.magiology.core.MReference.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.Scanner;

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

import com.magiology.clientdata.InfoFile;
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
import com.magiology.handelers.GuiHandeler;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.render.Textures;
import com.magiology.windowsgui.ModInfoGUI;
import com.magiology.windowsgui.SoundPlayer;
import com.magiology.windowsgui.ZipManager;

@Mod(modid=MODID,version=VERSION,name=NAME)
public class Magiology{
    public static SimpleNetworkWrapper NETWORK_CHANNEL;
    public static String[] InfoFileContent=null;
    public static final EnhancedRobot ROBOT;
    @SideOnly(Side.CLIENT)
    public static String InfoFileName="File for mod - "+MODID;
    @Instance(MODID)
    private static Magiology instance;
    public static Magiology getMagiology(){return instance;}
    static{
    	EnhancedRobot robotH=null;
    	try{robotH=new EnhancedRobot();}catch(Exception e){
    		throw new NullPointerException((Helper.RB(0.1)?"CRAP! :(":"")+" "+"Magiology"+" has encountered with a problem while trying to initialize the input robot! This might be the result of incompatible hardware or java version.");
		}
    	ROBOT=robotH;
    }
    
    public static InfoFile infoFile=new InfoFile(InfoFileName, "McModClientSave");
    @SideOnly(Side.CLIENT)
    public static ModInfoGUI modInfGUI;
    
    
    //proxy cluster
    @SidedProxy(clientSide=ClIENT_PROXY_LOCATION, serverSide=COMMON_PROXY_LOCATION)
	public static CommonProxy proxy;
    public boolean modWindowOpen(){return modInfGUI!=null&&!modInfGUI.isExited;}
    
    public void loadFiles(){
    	String dir="mods\\1.7.10\\"+MODID;
    	new File(dir).mkdir();
    	ZipManager.extractFileFromZip("mods\\Magiology-0.026A.jar", dir,"MagiZip.zip","OpenUp.wav","Close.wav","Loaded.wav");
    	
    	boolean willOpen=true;
    	try{
    		Scanner file=new Scanner(new File(InfoFileName+".McModClientSave"));
    		file.findWithinHorizon("1->", 9999999);
    		willOpen=file.next().equals("true");
    		file.close();
		}catch(Exception e){e.printStackTrace();}
		if(willOpen){
	    	Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
	    	new ModInfoGUI((int)screenSize.getWidth(),(int)screenSize.getHeight(),-680,0);
		}
		infoFile.setUpInfoFile();
		if(willOpen)modInfGUI.downloadData(infoFile);
    }
	public void preInit(FMLPreInitializationEvent event){
		//TODO
		Config.shadersEnabled=false;
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){@Override
		public void run(){
			if(modInfGUI!=null)SoundPlayer.playSound("mods\\1.7.10\\"+MODID+"\\Close.wav");
		}}, "Shutdown-thread"));
		
		
		MCreativeTabs.preInit();
		MGui.preInit();
		MTileEntitys.setCustomRenderers();
		MBlocks.preInit();
		MItems.preInit();
		MPackets.preInit();
    }
    public void init(FMLInitializationEvent event){
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandeler());
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
    
    
    
    
    
    
    //You can't see this!
    public Magiology(){instance=this;}
    @EventHandler public void preInitStarter(FMLPreInitializationEvent event){loadFiles();Init.Message(-1);preInit(event);Init.Message(1);}
    @EventHandler public void initStarter(FMLInitializationEvent event){Init.Message(-2);init(event);Init.Message(2);}
    @EventHandler public void postInitStarter(FMLPostInitializationEvent event){Init.Message(-3);postInit(event);Init.Message(3);Init.Message(4);}
}
