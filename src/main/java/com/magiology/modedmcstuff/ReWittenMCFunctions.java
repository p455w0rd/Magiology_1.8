package com.magiology.modedmcstuff;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

@Deprecated
public class ReWittenMCFunctions{
	static FontRenderer fr=Minecraft.getMinecraft().fontRenderer;

	@Deprecated
	public static int drawString(String text, int x, int y, int color){
		return drawString(text, x, y, color, false);
	}
	@Deprecated
	public static int drawStringWithAlpha(String text, int x, int y, int color){
		return drawString(text, x, y, color, false);
	}
	@Deprecated
	public static int drawString(String text, int x, int y, int color, boolean shade){
		try{
	        resetStyles();
	        int l;
	        if(shade){
	            l=renderString(text, x + 1, y + 1, color, true);
	            l = Math.max(l, renderString(text, x, y, color, false));
	        }else l = renderString(text, x, y, color, false);
	        return l;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
    }
	static Method renderS,renderStringAtPos;
	static Field posX,posY;
	@Deprecated
	private static int renderString(String text, int x, int y, int color,boolean shade)throws Exception{
		if(renderS==null){
			posX=FontRenderer.class.getDeclaredField("posX");
			posY=FontRenderer.class.getDeclaredField("posY");
			Method[] s=FontRenderer.class.getDeclaredMethods();
			for(Method s1:s){
				if(s1.getName().equals("renderString")){
					renderS=s1;
				}else if(s1.getName().equals("renderStringAtPos")){
					renderStringAtPos=s1;
				}
			}
		}
		posX.setAccessible(true);
		posY.setAccessible(true);
		posX.set(fr, x);
		posY.set(fr, y);
		renderStringAtPos.setAccessible(true);
		renderStringAtPos.invoke(fr,text,shade);
		return 0;
	}
	static Method resetS;
	@Deprecated
	private static void resetStyles()throws Exception{
		if(resetS==null)resetS=FontRenderer.class.getDeclaredMethod("resetStyles");
		resetS.setAccessible(true);
		resetS.invoke(fr);
	}
}
