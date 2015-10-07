package com.magiology.client.gui.guiutil.gui.buttons;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import org.lwjgl.opengl.GL11;

import com.magiology.client.gui.custom.hud.HUD;
import com.magiology.client.render.Textures;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.Util;

public class TexturedColoredButton extends ColoredGuiButton{
	@Override
	public void update(){
		
		r=Util.keepValueInBounds(r, 0, 1);
		g=Util.keepValueInBounds(g, 0, 1);
		b=Util.keepValueInBounds(b, 0, 1);
		prevR=r;
		prevG=g;
		prevB=b;
		prevAlpha=alpha;
		
		r=(float)Util.slowlyEqalize(r, wantedR, 0.1);
		g=(float)Util.slowlyEqalize(g, wantedG, 0.1);
		b=(float)Util.slowlyEqalize(b, wantedB, 0.1);
		wantedAlpha=Util.booleanToInt(hovered);
		alpha+=0.03;
		alpha=(float)Util.slowlyEqalize(prevAlpha, wantedAlpha, 0.17);
		
		
	}
	
	public TexturedColoredButton(int id, int x, int y,int width, int height, String text) {
		super(id, x, y, width, height, text);
	}
	@Override
	public void drawButton(Minecraft p_146112_1_, int x, int y){
        if (this.visible){
			float r=Util.calculateRenderPos(prevR, this.r),g=Util.calculateRenderPos(prevG, this.g),b=Util.calculateRenderPos(prevB, this.b),alpha=Util.calculateRenderPos(prevAlpha, this.alpha);
            FontRenderer fr = TessUtil.getFontRenderer();
            p_146112_1_.getTextureManager().bindTexture(Textures.ISidedIns);
            this.hovered=x>=this.xPosition&&y>=this.yPosition&&x<this.xPosition+this.width&&y<this.yPosition+this.height;
            GL11U.SetUpOpaqueRendering(1);

            GL11.glColor4f(r,g,b,1);
            HUD.drawRect(1F/512F,1F/512F,this.xPosition, this.yPosition, 352, 72+20, this.width / 2, this.height);
	        HUD.drawRect(1F/512F,1F/512F,this.xPosition + this.width / 2, this.yPosition, 424-width/2, 72+20, this.width / 2, this.height);

            GL11.glColor4f(r,g,b,alpha);
	        HUD.drawRect(1F/512F,1F/512F,this.xPosition, this.yPosition, 352, 72 + 40, this.width / 2, this.height);
	        HUD.drawRect(1F/512F,1F/512F,this.xPosition + this.width / 2, this.yPosition, 424-width/2, 72+  40, this.width / 2, this.height);
            
            this.mouseDragged(p_146112_1_, x, y);
            int l = 14737632;
            if (packedFGColour != 0)l = packedFGColour;
            else if (!this.enabled)l = 10526880;
            else if (this.hovered)l = 16777120;
            
            float[] rgb=Util.codeToRGBABPrecentage(l);
            
            rgb[0]=(rgb[0]+r)/2F;
            rgb[1]=(rgb[1]+g)/2F;
            rgb[2]=(rgb[2]+b)/2F;
            
            this.drawCenteredString(fr, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, new Color(r,g,b,alpha).hashCode());
            GL11U.EndOpaqueRendering();
        }
    }
}