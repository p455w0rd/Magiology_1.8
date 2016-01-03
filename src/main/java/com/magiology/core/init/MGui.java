package com.magiology.core.init;

import net.minecraftforge.fml.common.network.*;

import com.magiology.client.gui.*;
import com.magiology.client.gui.custom.hud.*;
import com.magiology.core.*;
import com.magiology.forgepowered.events.client.*;
import com.magiology.handlers.*;
import com.magiology.util.utilclasses.UtilM.U;

public class MGui{
	public static final byte
		GuiControlBock=0,
		GuiArmor=1,
		GuiUpgrade=2,
		GuiStats=3,
		GuiSC=4,
		GuiISidedPowerInstructor=5,
		HologramProjectorObjectCustomGui=6,
		HologramProjectorMainGui=7,
		CommandCenterGui=8,
		CommandContainerEditor=9;
	
	public static void preInit(){
		new GuiUpdater();
	}

	public static void registerGuis(){
		NetworkRegistry.INSTANCE.registerGuiHandler(Magiology.getMagiology(), new GuiHandlerM());
		try{
			RenderEvents.registerFirstPersonGui(SoulFlameHUD.instance);
			RenderEvents.registerFirstPersonGui(WingModeChangerHUD.instance);
			RenderEvents.registerFirstPersonGui(StatsDisplayHUD.instance);
			RenderEvents.registerFirstPersonGui(HandModeChangerHUD.instance);
		}catch(Exception e){
			e.printStackTrace();
			U.exit(404);
		}
	}
}
