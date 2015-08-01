package com.magiology.core.init;

import com.magiology.forgepowered.event.RenderLoopEvents;
import com.magiology.gui.fpgui.HandModeChangerGui;
import com.magiology.gui.fpgui.SoulFlameDisplayGui;
import com.magiology.gui.fpgui.StatsDisplayGui;
import com.magiology.gui.fpgui.WingModeChangerGui;
import com.magiology.gui.guiContainer.GuiArmorContainer;
import com.magiology.gui.guiContainer.GuiControlBockContainer;
import com.magiology.gui.guiContainer.GuiISidedPowerInstructorContainer;
import com.magiology.gui.guiContainer.GuiSCContainer;
import com.magiology.gui.guiContainer.GuiUpdater;
import com.magiology.gui.guiContainer.GuiUpdater.GuiProp;
import com.magiology.gui.guiContainer.GuiUpgradeContainer;
import com.magiology.render.tilerender.hologram.GuiObjectCustomizeContainer;

public class MGui{
	public static final byte
		GuiControlBock=0,
		GuiArmor=1,
		GuiUpgrade=2,
		GuiStats=3,
		GuiSC=4,
		GuiISidedPowerInstructor=5,
		HologramProjectorGui=6,
		HologramProjectorObjectCustomGui=7;
	
	public static void preInit(){
		new GuiUpdater();
		reg(
			GuiUpdater.newGuiProp(GuiArmorContainer.class),
			GuiUpdater.newGuiProp(GuiUpgradeContainer.class),
			GuiUpdater.newGuiProp(GuiSCContainer.class),
			GuiUpdater.newGuiProp(GuiISidedPowerInstructorContainer.class),
			GuiUpdater.newGuiProp(GuiControlBockContainer.class),
			GuiUpdater.newGuiProp(GuiObjectCustomizeContainer.class)
			);
	}

	public static void registerGuis(){
		RenderLoopEvents.registerFirstPersonGui(SoulFlameDisplayGui.instance);
		RenderLoopEvents.registerFirstPersonGui(WingModeChangerGui.instance);
		RenderLoopEvents.registerFirstPersonGui(StatsDisplayGui.instance);
		RenderLoopEvents.registerFirstPersonGui(HandModeChangerGui.instance);
	}
	static void reg(GuiProp...guiProp){
		for(Object a:guiProp){
			GuiUpdater.RegisterGUI((GuiProp) a,true);
		}
	}
}
