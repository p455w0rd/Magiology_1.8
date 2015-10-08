package com.magiology.client.gui.guiutil.gui.buttons;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.magiology.util.utilclasses.Util;

@SideOnly(Side.CLIENT)
public class ColoredGuiButton extends GuiButton{
	
	public float 
	r=1,g=1,b=1,alpha=1,
	prevR=1,prevG=1,prevB=1,prevAlpha=1,
	wantedR=1,wantedG=1,wantedB=1,wantedAlpha=1;
	
	public void update(){
		r=Util.snap(r, 0, 1);
		g=Util.snap(g, 0, 1);
		b=Util.snap(b, 0, 1);
		prevR=r;
		prevG=g;
		prevB=b;
		prevAlpha=alpha;
		
		r=(float)Util.slowlyEqalize(r, wantedR, 0.1);
		g=(float)Util.slowlyEqalize(g, wantedG, 0.1);
		b=(float)Util.slowlyEqalize(b, wantedB, 0.1);
		alpha=(float)Util.slowlyEqalize(prevAlpha, wantedAlpha, 0.2);
	}
	
	public ColoredGuiButton(int id, int x, int y,int width, int height, String txt){
		super(id, x, y, width, height, txt);
	}
	public void blink(float f){
		r+=f;
		g+=f;
		b+=f;
		r=Util.snap(r, 0, 1);
		g=Util.snap(g, 0, 1);
		b=Util.snap(b, 0, 1);
	}
	@Override
	public void drawButton(Minecraft v1, int v2, int v3){
		if (this.visible)
        {
			float r=Util.calculateRenderPos(prevR, this.r),g=Util.calculateRenderPos(prevG, this.g),b=Util.calculateRenderPos(prevB, this.b),alpha=Util.calculateRenderPos(prevAlpha, this.alpha);
            FontRenderer fontrenderer = v1.fontRendererObj;
            v1.getTextureManager().bindTexture(buttonTextures);
            GL11.glColor4f(r,g,b,alpha);
            this.hovered = v2 >= this.xPosition && v3 >= this.yPosition && v2 < this.xPosition + this.width && v3 < this.yPosition + this.height;
            int k = this.getHoverState(this.hovered);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
            this.mouseDragged(v1, v2, v3);
            int l = 14737632;

            if (packedFGColour != 0)
            {
                l = packedFGColour;
            }
            else if (!this.enabled)
            {
                l = 10526880;
            }
            else if (this.hovered)
            {
                l = 16777120;
            }
            
            float[] rgb=Util.codeToRGBABPrecentage(l);
            
            rgb[0]=(rgb[0]+r)/2F;
            rgb[1]=(rgb[1]+g)/2F;
            rgb[2]=(rgb[2]+b)/2F;
            
            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, new Color(r,g,b,alpha).hashCode());
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
	}

}