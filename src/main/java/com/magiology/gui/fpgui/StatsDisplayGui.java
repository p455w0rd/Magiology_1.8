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
		
		float renderAlpha=Helper.calculateRenderPos(prevStatsAlpha, statsAlpha);
		if(renderAlpha>0.3){
			GL11H.SetUpOpaqueRendering(1);
			TessHelper.bindTexture(Textures.StatsGui1);
			GL11.glTranslatef(-20+renderAlpha*20, -10+renderAlpha*10, 0);
			GL11.glScalef(1F/3F-0.5F+renderAlpha/2,1F/3F-0.5F+renderAlpha/2,1F/3F-0.5F+renderAlpha/2);
			GL11.glColor4d(1, 1, 1, Helper.keepAValueInBounds(renderAlpha+0.2, 0, 1));
			drawRect(1F/239, 1F/283F, 0, 0, 0, 0, 239, 283F);
			GL11H.EndOpaqueRendering();
		}
		GL11.glColor4d(1, 1, 1, 1);
	}
	@Override
	public void update(){
		prevStatsAlpha=statsAlpha;
		statsAlpha=(float)Helper.slowlyEqalize(statsAlpha, statsWantedAlpha, 0.1);
		statsWantedAlpha=isStatsShowed?1:0.2F;
	}
}
