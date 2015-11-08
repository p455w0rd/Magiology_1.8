package com.magiology.forgepowered.events;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.magiology.client.gui.custom.DownloadingIcon;
import com.magiology.client.gui.custom.hud.FakeMessageHUD;
import com.magiology.client.gui.custom.hud.HUD;
import com.magiology.client.gui.custom.hud.MainMenuUpdateNotificationHUD;
import com.magiology.client.render.aftereffect.LongAfterRenderRenderer;
import com.magiology.core.MUpdater;
import com.magiology.core.Magiology;
import com.magiology.forgepowered.events.client.RenderLoopEvents;
import com.magiology.forgepowered.packets.packets.RightClickBlockPacket;
import com.magiology.forgepowered.packets.packets.UploadPlayerDataPacket;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.vectors.Pos;

public class TickEvents{
	Minecraft mc=U.getMC();
	public static TickEvents instance=new TickEvents();
	public static CientPlayerBufferedGui bufferedGui=instance.new CientPlayerBufferedGui(new Pos());
	boolean bufferedGuiFirst=true;
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event){
		if(event.phase!=Phase.START)return;
		
		if(MUpdater.getFoundNew()&&U.getMC().currentScreen instanceof GuiMainMenu)MainMenuUpdateNotificationHUD.instance.update();
		DownloadingIcon.update();
		if(U.getTheWorld()==null)RenderLoopEvents.universalLongRender.clear();
		
		if(mc.isGamePaused())return;
		if(Util.getThePlayer()==null)return;
		
		FakeMessageHUD.get().update();
		
		/**RenderLoopEvents sync**/{
			List<LongAfterRenderRenderer> render=RenderLoopEvents.universalLongRender;
			if(!render.isEmpty())for(LongAfterRenderRenderer a:render)if(a!=null&&!a.isDead())a.update();
		}
		if(RenderLoopEvents.disabledEquippItemAnimationTime>0)RenderLoopEvents.disabledEquippItemAnimationTime--;
		else if(RenderLoopEvents.disabledEquippItemAnimationTime<0)RenderLoopEvents.disabledEquippItemAnimationTime=0;

		GameSettings gs=U.getMC().gameSettings;
		boolean[] keys={Keyboard.isKeyDown(gs.keyBindForward.getKeyCode()),Keyboard.isKeyDown(gs.keyBindBack.getKeyCode()),Keyboard.isKeyDown(gs.keyBindJump.getKeyCode()),Keyboard.isKeyDown(gs.keyBindSneak.getKeyCode()),Keyboard.isKeyDown(gs.keyBindRight.getKeyCode()),Keyboard.isKeyDown(gs.keyBindLeft.getKeyCode())};
		ExtendedPlayerData data=ExtendedPlayerData.get(Util.getThePlayer());
		if(data!=null){
			for(int a=0;a<keys.length;a++)if(keys[a]!=data.keys[a]){
				Util.sendMessage(new UploadPlayerDataPacket(Util.getThePlayer()));
				continue;
			}
			data.keys=keys.clone();
			if(Util.getTheWorld().getTotalWorldTime()%12==0){
				Util.sendMessage(new UploadPlayerDataPacket(Util.getThePlayer()));
				data.keys=keys.clone();
			}
		}
		for(int a=0;a<RenderLoopEvents.FPGui.size();a++){
			HUD gui=RenderLoopEvents.FPGui.get(a);
			gui.update();
		}
	}
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event){
		
	}
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event){
		Render.partialTicks=event.renderTickTime;
		if(event.phase!=Phase.END)return;
		if(MUpdater.getFoundNew()&&U.getMC().currentScreen instanceof GuiMainMenu)MainMenuUpdateNotificationHUD.instance.render(Display.getWidth(), Display.getHeight(),event.renderTickTime);
		if(bufferedGuiFirst){
			bufferedGuiFirst=false;
			bufferedGui.isDone=true;
		}
		if(mc.thePlayer!=null&&mc.thePlayer.openContainer.getClass().equals(ContainerPlayer.class)&&!bufferedGui.isDone){
			bufferedGui.isDone=true;
			Util.sendMessage(new RightClickBlockPacket(bufferedGui.pos, (byte) 0));
		}
		
		if(Magiology.modInfGUI!=null&&!Magiology.modInfGUI.isExited)Magiology.modInfGUI=null;
		else if(Magiology.getMagiology().isWindowOpen()){
			if(Magiology.modInfGUI.exitOn==3) Magiology.modInfGUI.exit();
			else if(Magiology.modInfGUI.exitOn==2&&U.getMC().theWorld!=null) Magiology.modInfGUI.exit();
			if(!Magiology.modInfGUI.MCStat){
				Magiology.modInfGUI.MCStat=true;
				Magiology.modInfGUI.Update();
			}
		}
	}
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event){
		//checking if the tick should be activated: start--|
		World world=event.world;//-------------------------|
		if(Util.isNull(world))return;//------------------|
		if(event.phase==Phase.END)return;
//		if(event.type!=Type.WORLD)return;
		//checking if the tick should be activated: end----|
//		Helper.println((wTime==lastTick)+"\t"+event.type+" "+event.side+" "+event.phase);
		/**Caling for my gui registry for updating**/
//		lastTick=wTime;
	}
	@SubscribeEvent
	public void clientTickEnd(ClientTickEvent event){
		
	}
	public class CientPlayerBufferedGui{
		public BlockPos pos;
		public boolean isDone=false;
		public CientPlayerBufferedGui(BlockPos pos){
			this.pos=pos;
		}
	}
}