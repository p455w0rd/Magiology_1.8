package com.magiology.gui.guiutil.gui.buttons;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.magiology.gui.GuiUpdater.Updateable;
import com.magiology.util.renderers.GL11H;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.Helper.H;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.AdvancedPhysicsFloat;

public class CleanButton extends GuiButton implements Updateable{
	
	public ColorF color,prevColor;
	public AdvancedPhysicsFloat highlight=new AdvancedPhysicsFloat(0, 0.1F,true);
	public boolean[] enabledOutline={true,true,true,true};
	
	public CleanButton(int buttonId, int x, int y, String buttonText, ColorF color){
		super(buttonId, x, y, buttonText);
		this.color=prevColor=color;
	}
	
	public CleanButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, ColorF color){
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		this.color=prevColor=color;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY){
        if(!this.visible)return;
        this.mouseDragged(mc, mouseX, mouseY);
        this.hovered=mouseX>=this.xPosition&&mouseY>=this.yPosition&&mouseX<this.xPosition+this.width&&mouseY<this.yPosition+this.height;
        int selected=this.getHoverState(this.hovered);
        GL11H.blend(true);
        GL11H.blendFunc(1);
            ColorF color=H.calculateRenderColor(prevColor, this.color).mul(highlight.getPoint()+1);
            GL11H.color(color);
            GL11.glPushMatrix();
            GL11H.texture(false);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0, this.width, this.height);
            GL11H.texture(true);
            
            this.drawCenteredString(Get.Render.FR(), this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, color.mix(Color.WHITE).toCode());
            GL11.glPopMatrix();
        
    }
	
	@Override
	public void update(){
		if(hovered){
			if(Mouse.isButtonDown(0))highlight.wantedPoint=0.5F;
			else highlight.wantedPoint=0.2F;
		}else highlight.wantedPoint=0F;
		highlight.update();
	}
	
	public CleanButton setOutline(int side, boolean enabled){
		enabledOutline[side]=enabled;
		return this;
	}
	public CleanButton setOutline(boolean[] enabled){
		enabledOutline=enabled;
		return this;
	}
}
