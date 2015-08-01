package com.magiology.handelers;

import static com.magiology.handelers.KeyHandler.Keys.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.input.Keyboard;

import com.magiology.core.MReference;
import com.magiology.core.init.MGui;
import com.magiology.forgepowered.packets.OpenGuiPacket;
import com.magiology.forgepowered.packets.UploadPlayerDataPacket;
import com.magiology.forgepowered.packets.generic.GenericServerStringPacket;
import com.magiology.forgepowered.packets.generic.GenericServerVoidPacket;
import com.magiology.gui.fpgui.HandModeChangerGui;
import com.magiology.gui.fpgui.StatsDisplayGui;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;
import com.magiology.objhelper.SimpleCounter;
import com.magiology.objhelper.helpers.Helper;

public class KeyHandler{
	Minecraft mc=Minecraft.getMinecraft();
	static SimpleCounter counter=new SimpleCounter();
	public static enum Keys{
		StatsGui("key.stats",Keyboard.KEY_B),
		ArmorGui("key.armor",Keyboard.KEY_V),
  		BusGui("key.bus",Keyboard.KEY_I),
		HandMode("key.bus",Keyboard.KEY_F)
		;
		public final String  keyDesc;
		public final int keyValue,id;
		private Keys(String desc,int value){
			keyDesc=MReference.MODID+"."+desc;
			keyValue=value;
			id=counter.getAndAdd();
		}
		public boolean check(int pressedId){
			return pressedId==id;
		}
	}
	private static String[] keyDesc;
	private static int[] keyValues;
	private final KeyBinding[] keys;
	
	public KeyHandler(){
		int lenght=Keys.values().length;
		keyDesc=new String[lenght];
		keyValues=new int[lenght];
		keys=new KeyBinding[lenght];
		
		for(int i=0;i<keyDesc.length;i++){
			Keys a=Keys.values()[i];
			keyDesc[i]=a.keyDesc;
			keyValues[i]=a.keyValue;
		}
		for(int i=0;i<keyValues.length;i++)RegKey(keys[i],new KeyBinding(keyDesc[i], keyValues[i], MReference.MODID+".key_bindings"));
	}
	private void RegKey(KeyBinding saver,KeyBinding New){
		saver=New;ClientRegistry.registerKeyBinding(saver);
	}
	// 1.
	@SubscribeEvent
	public void startKeyInput(InputEvent.KeyInputEvent event){
		if(FMLClientHandler.instance().isGUIOpen(GuiChat.class))return;
		decodeKeyInput(Keyboard.getEventKey());
	}
	// 2.
	public void decodeKeyInput(int rawKey){
//		try {
//			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
//				IResourceManager IRM=Minecraft.getMinecraft().getResourceManager();
//				Map domainResourceManagers=DataStalker.getVariable(SimpleReloadableResourceManager.class, "domainResourceManagers",IRM);
//				IResourceManager iresourcemanager = (IResourceManager)domainResourceManagers.get(Textures.BedrockBreakerBase.getResourceDomain());
//				Helper.println(iresourcemanager.getResource(Textures.BedrockBreakerBase));
//			}
//		} catch (Exception e){e.printStackTrace();}
		GameSettings gs=Minecraft.getMinecraft().gameSettings;
		boolean[] keys={Keyboard.isKeyDown(gs.keyBindForward.getKeyCode()),Keyboard.isKeyDown(gs.keyBindBack.getKeyCode()),Keyboard.isKeyDown(gs.keyBindJump.getKeyCode()),Keyboard.isKeyDown(gs.keyBindSneak.getKeyCode()),Keyboard.isKeyDown(gs.keyBindRight.getKeyCode()),Keyboard.isKeyDown(gs.keyBindLeft.getKeyCode())};
		if(Keyboard.getEventKeyState()){
			for(Keys i:Keys.values()){
				if(rawKey==keyValues[i.id]){
					keyInput(i.id);
					return;
				}
			}
			if(rawKey==gs.keyBindJump.getKeyCode()){
				int x=0,z=0;
				if(keys[0])x=1;
				if(keys[1])x+=2;
				if(keys[4])z=1;
				if(keys[5])z+=2;
				Helper.sendMessage(new GenericServerStringPacket(0,x+","+z));
			}
		}
		if(rawKey==gs.keyBindForward.getKeyCode()||rawKey==gs.keyBindBack.getKeyCode()||rawKey==gs.keyBindJump.getKeyCode()||rawKey==gs.keyBindSneak.getKeyCode()||rawKey==gs.keyBindRight.getKeyCode()||rawKey==gs.keyBindLeft.getKeyCode()){
			ExtendedPlayerData data=ExtendedPlayerData.get(Helper.getThePlayer());
			Helper.sendMessage(new UploadPlayerDataPacket(Helper.getThePlayer()));
			data.keys=keys.clone();
		}
	}
	// 3.
	public void keyInput(int keyId){
		if(StatsGui.check(keyId)){
			StatsDisplayGui.instance.isStatsShowed=!StatsDisplayGui.instance.isStatsShowed;
		}
		else if(ArmorGui.check(keyId)){
			Helper.sendMessage(new OpenGuiPacket(MGui.GuiArmor));
		}
		else if(BusGui.check(keyId)){
			
		}
		else if(HandMode.check(keyId)){
			if(HandModeChangerGui.instance.handAlpha>0.9){
				Helper.sendMessage(new GenericServerVoidPacket(0));
			}
		}
		
	}
}
