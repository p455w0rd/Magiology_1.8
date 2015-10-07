package com.magiology.client.gui.guiutil.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.input.Mouse;

import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.tessellatorscripts.Drawer;
import com.magiology.util.utilclasses.Get.Render.Font;
import com.magiology.util.utilclasses.Util.U;
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
        this.getHoverState(this.hovered);
        GL11U.SetUpOpaqueRendering(1);
        ColorF color=U.calculateRenderColor(prevColor, this.color).mul(highlight.getPoint()+1);
        if(!enabled)color=color.mix(color.blackNWhite(),1,2);
        GL11U.color(color);
        GL11U.texture(false);
        this.drawTexturedModalRect(xPosition, yPosition, 0, 0, width, height);
        GL11U.color(color.mix(new ColorF(0,0,0,color.a), 1,1.2F));
        Drawer.startDrawingLines();
        
        if(enabledOutline[0]){
        	Drawer.addVertex(xPosition,       yPosition, 0);
        	Drawer.addVertex(xPosition+width, yPosition, 0);
        }
        if(enabledOutline[1]){
        	Drawer.addVertex(xPosition, yPosition,        0);
        	Drawer.addVertex(xPosition, yPosition+height, 0);
        }
        if(enabledOutline[2]){
        	Drawer.addVertex(xPosition+width, yPosition,        0);
        	Drawer.addVertex(xPosition+width, yPosition+height, 0);
        }
        if(enabledOutline[3]){
        	Drawer.addVertex(xPosition,       yPosition+height, 0);
        	Drawer.addVertex(xPosition+width, yPosition+height, 0);
        }
        
        Drawer.draw();
        
        GL11U.texture(true);
        
        this.drawCenteredString(Font.FR(), this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, color.mix(ColorF.WHITE,1F,1.5F).toCode());
        GL11U.EndOpaqueRendering();
    }
	
	@Override
	public void update(){
		if(enabled){
			if(hovered){
				if(Mouse.isButtonDown(0))highlight.wantedPoint=0.5F;
				else highlight.wantedPoint=0.2F;
			}else highlight.wantedPoint=0F;
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