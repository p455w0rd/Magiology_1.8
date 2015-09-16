package com.magiology.core.init;

import com.magiology.forgepowered.event.client.RenderLoopEvents;
import com.magiology.gui.GuiUpdater;
import com.magiology.gui.custom.hud.HandModeChangerHUD;
import com.magiology.gui.custom.hud.SoulFlameHUD;
import com.magiology.gui.custom.hud.StatsDisplayHUD;
import com.magiology.gui.custom.hud.WingModeChangerHUD;
import com.magiology.util.utilclasses.Util.U;

public class MGui{
	public static final byte
		GuiControlBock=0,
		GuiArmor=1,
		GuiUpgrade=2,
		GuiStats=3,
		GuiSC=4,
		GuiISidedPowerInstructor=5,
		HologramProjectorObjectCustomGui=6,
		HologramProjectorMainGui=7;
	
	public static void preInit(){
		new GuiUpdater();
	}

	public static void registerGuis(){
		try{
			RenderLoopEvents.registerFirstPersonGui(SoulFlameHUD.instance);
			RenderLoopEvents.registerFirstPersonGui(WingModeChangerHUD.instance);
			RenderLoopEvents.registerFirstPersonGui(StatsDisplayHUD.instance);
			RenderLoopEvents.registerFirstPersonGui(HandModeChangerHUD.instance);
		}catch(Exception e){
			e.printStackTrace();
			U.exit(404);
		}
	}
}
