package com.magiology.gui.custom.hud;

import com.magiology.util.utilclasses.Helper;

public class StatsDisplayHUD extends HUD{
	public static StatsDisplayHUD instance=new StatsDisplayHUD();
	public boolean isStatsShowed=false;
	public float statsAlpha=0,prevStatsAlpha=0,statsWantedAlpha=0;
	private StatsDisplayHUD(){}
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