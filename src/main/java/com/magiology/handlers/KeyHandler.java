package com.magiology.handlers;

import static com.magiology.handlers.KeyHandler.Keys.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.settings.*;
import net.minecraftforge.fml.client.*;
import net.minecraftforge.fml.client.registry.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;

import org.lwjgl.input.*;

import com.magiology.client.gui.custom.hud.*;
import com.magiology.core.*;
import com.magiology.core.init.*;
import com.magiology.forgepowered.packets.packets.*;
import com.magiology.forgepowered.packets.packets.generic.*;
import com.magiology.mcobjects.entitys.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.*;

public class KeyHandler{
	Minecraft mc=U.getMC();
	static SimpleCounter counter=new SimpleCounter();
	public static enum Keys{
		StatsGui("key.stats",Keyboard.KEY_B),
		ArmorGui("key.armor",Keyboard.KEY_V),
  		BusGui("key.bus",Keyboard.KEY_I),
		HandMode("key.bus",Keyboard.KEY_F),
//		test("key.bus",Keyboard.KEY_L)
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
	private KeyBinding[] keys;
	
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
		for(int i=0;i<keyValues.length;i++)keys[i]=regKey(new KeyBinding(keyDesc[i], keyValues[i], MReference.MODID+".key_bindings"));
	}
	private KeyBinding regKey(KeyBinding key){
		ClientRegistry.registerKeyBinding(key);
		return key;
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
//				IResourceManager IRM=H.getMC().getResourceManager();
//				Map domainResourceManagers=DataStalker.getVariable(SimpleReloadableResourceManager.class, "domainResourceManagers",IRM);
//				IResourceManager iresourcemanager = (IResourceManager)domainResourceManagers.get(Textures.BedrockBreakerBase.getResourceDomain());
//				Helper.println(iresourcemanager.getResource(Textures.BedrockBreakerBase));
//			}
//		} catch (Exception e){e.printStackTrace();}
		GameSettings gs=U.getMC().gameSettings;
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
				UtilM.sendMessage(new GenericServerStringPacket(0,x+","+z));
			}
		}
		if(rawKey==gs.keyBindForward.getKeyCode()||rawKey==gs.keyBindBack.getKeyCode()||rawKey==gs.keyBindJump.getKeyCode()||rawKey==gs.keyBindSneak.getKeyCode()||rawKey==gs.keyBindRight.getKeyCode()||rawKey==gs.keyBindLeft.getKeyCode()){
			ExtendedPlayerData data=ExtendedPlayerData.get(UtilM.getThePlayer());
			if(data!=null){
				data.keys=keys.clone();
				UtilM.sendMessage(new UploadPlayerDataPacket(UtilM.getThePlayer()));
			}
		}
	}
	// 3.
	public void keyInput(int keyId){
		if(StatsGui.check(keyId)){
			StatsDisplayHUD.instance.isStatsShowed=!StatsDisplayHUD.instance.isStatsShowed;
		}
		else if(ArmorGui.check(keyId)){
			UtilM.sendMessage(new OpenGuiPacket(MGui.GuiArmor));
		}
		else if(BusGui.check(keyId)){
			
		}
		else if(HandMode.check(keyId)){
			if(HandModeChangerHUD.instance.handAlpha>0.9){
				UtilM.sendMessage(new GenericServerVoidPacket(0));
			}
		}
//		else if(test.check(keyId)){
//			FMLClientHandler.instance().showGuiScreen(new GuiContainer(new ContainerEmpty()){
//				
//				@Override
//				protected void drawGuiContainerBackgroundLayer(float partialTicks,
//						int mouseX, int mouseY) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//		}
		
	}
}
