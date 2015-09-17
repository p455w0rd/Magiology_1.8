package com.magiology.core;

import static com.magiology.core.MReference.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

public class Magiology extends BaseMod{
	
	/***//**variables*//***/
	
	
	public static SimpleNetworkWrapper NETWORK_CHANNEL;
	
	//TODO: change this when you compile the mod! (you dumbass)
	public static final boolean IS_DEV=true;
	
//	@Instance(value=MODID)
	private static Magiology instance;
	
	public static IOReadableMap infoFile;
	
	@SideOnly(Side.CLIENT)
	public static ModInfoGUI modInfGUI;
	
	@SidedProxy(clientSide=MReference.ClIENT_PROXY_LOCATION, serverSide=MReference.COMMON_PROXY_LOCATION)
	public static CommonProxy proxy;
	
	public static EnhancedRobot ROBOT;
	
	
	/***//**init, stuff and things*//***/
	
	
	public Magiology(){
		instance=this;
		infoFile=new IOReadableMap(INFO_FILE_NAME);
		
		EnhancedRobot robotH=null;
		try{
			robotH=new EnhancedRobot();
		}catch(Exception e){
			throw new NullPointerException((Util.RB(0.1)?"CRAP! :(":"")+" "+NAME+" has encountered with a problem while trying to initialize the input robot! This might be the result of incompatible hardware or java version.");
		}
		ROBOT=robotH;
	}
	@Override
	public void loadFiles(){
		new File(MODS_SUBFOLDER_WIN_GUI).mkdir();
		if(!new File(MODS_SUBFOLDER_WIN_GUI+"/MagiZip.zip").exists())DownloadingHandeler.downladAssets();
		infoFile.readFromFile();
		
		if(infoFile.data.get("GUIOpen")==null||infoFile.getB("GUIOpen")){
			Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
			modInfGUI=new ModInfoGUI((int)screenSize.getWidth(),(int)screenSize.getHeight(),-680,0);
			modInfGUI.downloadData(infoFile);
		}
	}
	@Override
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
	@Override
	public void init(FMLInitializationEvent event){
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandelerM());
		MRecepies.init();
		MTileEntitys.init();
		proxy.registerProxies();
		MEntitys.init();
		MEvents.init();
		MInterfaces.init();
	}
	@Override
	public void postInit(FMLPostInitializationEvent event){
		if(modInfGUI!=null)modInfGUI.modStat=true;
		Textures.postInit();
	}
	@Override
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
}
