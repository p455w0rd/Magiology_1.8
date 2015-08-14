package com.magiology.gui.fpgui;

import org.lwjgl.opengl.GL11;

import com.magiology.mcobjects.entitys.ExtendedPlayerData;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.TessHelper;
import com.magiology.render.Textures;

public class StatsDisplayGui extends FirstPersonGui{
	public static StatsDisplayGui instance=new StatsDisplayGui();
	private ExtendedPlayerData data;
	public boolean isStatsShowed=false;
	public float statsAlpha=0,prevStatsAlpha=0,statsWantedAlpha=0;
	@Override
	public void render(int xScreen, int yScreen, float partialTicks){
		
	}
	@Override
	public void update(){
		prevStatsAlpha=statsAlpha;
		statsAlpha=(float)Helper.slowlyEqalize(statsAlpha, statsWantedAlpha, 0.1);
		statsWantedAlpha=isStatsShowed?1:0.2F;
	}
}
