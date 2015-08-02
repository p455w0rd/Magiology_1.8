package com.magiology.forgepowered.event;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import org.lwjgl.input.Keyboard;

import com.magiology.core.Magiology;
import com.magiology.core.init.MEvents;
import com.magiology.forgepowered.packets.RightClickBlockPacket;
import com.magiology.forgepowered.packets.UploadPlayerDataPacket;
import com.magiology.gui.fpgui.FirstPersonGui;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;
import com.magiology.render.aftereffect.LongAfterRenderRenderer;

public class TickEvents{
	Minecraft mc=Minecraft.getMinecraft();
	public static TickEvents instance=new TickEvents();
	public static CientPlayerBufferedGui bufferedGui=instance.new CientPlayerBufferedGui(0, 0, 0);
	boolean bufferedGuiFirst=true;
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event){
		if(event.phase!=Phase.START)return;
		if(H.getTheWorld()==null){
			MEvents.RenderLoopInstance.universalLongRender.clear();
		}
		if(mc.isGamePaused())return;
		if(Helper.getThePlayer()==null)return;
		/**RenderLoopEvents sync**/{
			RenderLoopEvents renderE=MEvents.RenderLoopInstance;
			List<LongAfterRenderRenderer> render=RenderLoopEvents.universalLongRender;
			if(!render.isEmpty())for(LongAfterRenderRenderer a:render)if(a!=null&&!a.isDead())a.update();
		}
		if(RenderLoopEvents.disabledEquippItemAnimationTime>0)RenderLoopEvents.disabledEquippItemAnimationTime--;
		else if(RenderLoopEvents.disabledEquippItemAnimationTime<0)RenderLoopEvents.disabledEquippItemAnimationTime=0;

		GameSettings gs=Minecraft.getMinecraft().gameSettings;
		boolean[] keys={Keyboard.isKeyDown(gs.keyBindForward.getKeyCode()),Keyboard.isKeyDown(gs.keyBindBack.getKeyCode()),Keyboard.isKeyDown(gs.keyBindJump.getKeyCode()),Keyboard.isKeyDown(gs.keyBindSneak.getKeyCode()),Keyboard.isKeyDown(gs.keyBindRight.getKeyCode()),Keyboard.isKeyDown(gs.keyBindLeft.getKeyCode())};
		ExtendedPlayerData data=ExtendedPlayerData.get(Helper.getThePlayer());
		if(data!=null){
			for(int a=0;a<keys.length;a++)if(keys[a]!=data.keys[a]){
				Helper.sendMessage(new UploadPlayerDataPacket(Helper.getThePlayer()));
				continue;
			}
			data.keys=keys.clone();
			if(Helper.getTheWorld().getTotalWorldTime()%12==0){
				Helper.sendMessage(new UploadPlayerDataPacket(Helper.getThePlayer()));
				data.keys=keys.clone();
			}
		}
		for(int a=0;a<RenderLoopEvents.FPGui.size();a++){
			FirstPersonGui gui=RenderLoopEvents.FPGui.get(a);
			gui.update();
		}
//		if(Helper.getFPS()<30||true){
//			if(Helper.getTheWorld().getTotalWorldTime()%100==0){
//				try{
//					List[] fxLayers=DataStalker.getVariable(EffectRenderer.class, "fxLayers", Minecraft.getMinecraft().effectRenderer);
//					Integer[] dead=new Integer[fxLayers.length];
//					for(int i=0;i<fxLayers.length;i++){
//						dead[i]=new Integer(0);
//						for(int j=0;j<fxLayers[i].size();j++){
//							EntityFX part=(EntityFX)fxLayers[i].get(j);
//							if(part!=null&&part.isDead)dead[i]++;
//						}
//					}
//					Helper.printInln(dead);
//				}catch(Exception e){
//				}
//			}
//		}
	}
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event){
		
	}
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event){
		RenderLoopEvents.partialTicks=event.renderTickTime;
		if(bufferedGuiFirst){
			bufferedGuiFirst=false;
			bufferedGui.isDone=true;
		}
		if(mc.thePlayer!=null&&mc.thePlayer.openContainer.getClass().equals(ContainerPlayer.class)&&!bufferedGui.isDone){
			bufferedGui.isDone=true;
			Helper.sendMessage(new RightClickBlockPacket(bufferedGui.x, bufferedGui.y, bufferedGui.z, (byte) 0));
		}
		
		if(Magiology.modInfGUI!=null&&!Magiology.modInfGUI.isExited)Magiology.modInfGUI=null;
		else if(Magiology.getMagiology().modWindowOpen()){
			if(Magiology.modInfGUI.exitOn==3) Magiology.modInfGUI.exit();
			else if(Magiology.modInfGUI.exitOn==2&&Minecraft.getMinecraft().theWorld!=null) Magiology.modInfGUI.exit();
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
		if(Helper.isNull(world))return;//------------------|
		long wTime=world.getWorldTime();//-----------------|
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
		public int x,y,z;
		public boolean isDone=false;
		public CientPlayerBufferedGui(int x,int y,int z){
			this.x=x;
			this.y=y;
			this.z=z;
		}
	}
}
