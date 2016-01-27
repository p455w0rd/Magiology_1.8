package com.magiology.client.gui.guiutil.gui.buttons;

import java.awt.*;

import net.minecraft.client.*;
import net.minecraft.client.gui.*;

import org.lwjgl.opengl.*;

import com.magiology.client.gui.custom.hud.*;
import com.magiology.client.render.*;
import com.magiology.util.renderers.*;
import com.magiology.util.utilclasses.*;

public class TexturedColoredButton extends ColoredGuiButton{
	@Override
	public void update(){
		
		r=UtilM.snap(r, 0, 1);
		g=UtilM.snap(g, 0, 1);
		b=UtilM.snap(b, 0, 1);
		prevR=r;
		prevG=g;
		prevB=b;
		prevAlpha=alpha;
		
		r=(float)UtilM.slowlyEqualize(r, wantedR, 0.1);
		g=(float)UtilM.slowlyEqualize(g, wantedG, 0.1);
		b=(float)UtilM.slowlyEqualize(b, wantedB, 0.1);
		wantedAlpha=UtilM.booleanToInt(hovered);
		alpha+=0.03;
		alpha=(float)UtilM.slowlyEqualize(prevAlpha, wantedAlpha, 0.17);
		
		
	}
	
	public TexturedColoredButton(int id, int x, int y,int width, int height, String text) {
		super(id, x, y, width, height, text);
	}
	@Override
	public void drawButton(Minecraft p_146112_1_, int x, int y){
        if (this.visible){
			float r=UtilM.calculatePos(prevR, this.r),g=UtilM.calculatePos(prevG, this.g),b=UtilM.calculatePos(prevB, this.b),alpha=UtilM.calculatePos(prevAlpha, this.alpha);
            FontRenderer fr = TessUtil.getFontRenderer();
            p_146112_1_.getTextureManager().bindTexture(Textures.ISidedIns);
            this.hovered=x>=this.xPosition&&y>=this.yPosition&&x<this.xPosition+this.width&&y<this.yPosition+this.height;
            GL11U.setUpOpaqueRendering(1);

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
            
            float[] rgb=UtilM.codeToRGBABPercentage(l);
            
            rgb[0]=(rgb[0]+r)/2F;
            rgb[1]=(rgb[1]+g)/2F;
            rgb[2]=(rgb[2]+b)/2F;
            
            this.drawCenteredString(fr, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, new Color(r,g,b,alpha).hashCode());
            GL11U.endOpaqueRendering();
        }
    }
}
