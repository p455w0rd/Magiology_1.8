package com.magiology.gui.guiutil.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.magiology.util.renderers.TessHelper;
import com.magiology.util.utilclasses.Helper;
import com.magiology.util.utilclasses.Helper.H;

@SideOnly(Side.CLIENT)
public class CustomButton extends GuiButton{

	public ResourceLocation buttonTexture = new ResourceLocation("textures/gui/widgets.png");
	public double r=1,g=1,b=1,rGoal=0.9,gGoal=0.9,bGoal=0.9;
	public double[] one2={1,0},one2Goal={1,0};
	public int state=0;
	WorldRenderer tess=TessHelper.getWR();
	
    
    public CustomButton(int id, int x, int y,int width, int height, String text,String resouce){
    	super(id, x, y, width, height, text);
    	rGoal=Helper.RD()/1.3;
    	gGoal=Helper.RD()/1.3;
    	bGoal=Helper.RD()/1.3;
    	if(resouce!=null)buttonTexture=new ResourceLocation(resouce);
    }
    
    public void setC(int R,int G,int B){
    	rGoal=R;gGoal=G;bGoal=B;
    	r=rGoal;g=gGoal;b=bGoal;
    }
    
    public void update(int x, int y){
    	state=this.getHoverState(this.hovered);
    	if(state==1){
    		one2Goal[0]=1;
    		one2Goal[1]=0;
    	}else if(state==2){
    		one2Goal[0]=0;
    		one2Goal[1]=1;
    	}
    	xPosition=x;
    	yPosition=y;
    	if(r>1)r=1;
    	else if(r<0)r=0;
    	if(g>1)g=1;
    	else if(g<0)g=0;
    	if(b>1)b=1;
    	else if(b<0)b=0;
    	double Cspeed=0.04;
    	r=Helper.slowlyEqalize(r, rGoal, Cspeed+Helper.CRandD(Cspeed/2));
    	g=Helper.slowlyEqalize(g, gGoal, Cspeed+Helper.CRandD(Cspeed/2));
    	b=Helper.slowlyEqalize(b, bGoal, Cspeed+Helper.CRandD(Cspeed/2));
    	for(int a=0;a<one2.length;a++)if(Math.abs(one2[a]-one2Goal[a])>0.1)one2[a]=Helper.slowlyEqalize(one2[a], one2Goal[a], Cspeed*3+Helper.CRandD(Cspeed));
    }
    
	@Override
	public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_){
        if (this.visible){
            FontRenderer fontrenderer = H.getFontRenderer();
            p_146112_1_.getTextureManager().bindTexture(buttonTexture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            this.getHoverState(this.hovered);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glTranslated(0, 0, 1);
            
            	this.drawTexturedModalRect(xPosition,yPosition,0,46,width/2,height);
            	this.drawTexturedModalRect(xPosition+width/2,yPosition,200-width/2,46,width/2,height);
            	
            	GL11.glColor4d(one2[0], one2[0], one2[0], 0.3);
            	GL11.glTranslated(0, 0, 1);
            		this.drawTexturedModalRect(xPosition,yPosition,0,46+20,width/2,height);
            		this.drawTexturedModalRect(xPosition+width/2,yPosition,200-width/2,46+20,width/2,height);
            		
            		GL11.glColor4d(one2[1], one2[1], one2[1], 0.3);
            		this.drawTexturedModalRect(xPosition,yPosition,0,46+40,width/2,height);
            		this.drawTexturedModalRect(xPosition+width/2,yPosition,200-width/2,46+40,width/2,height);
            GL11.glTranslated(0, 0, -2);
            
    		GL11.glDepthMask(false);
    		
    		GL11.glColor4d(0, 0, 0, 0.2);
            GL11.glTranslated(0, 0, -1);
            this.drawTexturedModalRect(xPosition,yPosition,0,46+60,width/2,height);
            this.drawTexturedModalRect(xPosition+width/2,yPosition,200-width/2,46+60,width/2,height);
            GL11.glTranslated(0, 0, 1);
            
    		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
    		GL11.glColor4d(r, g, b, 0.3);
            
    		this.drawTexturedModalRect(xPosition,yPosition,0,46+60,width/2,height);
            this.drawTexturedModalRect(xPosition+width/2,yPosition,200-width/2,46+60,width/2,height);
            
    		GL11.glDepthMask(true);
    		GL11.glDisable(GL11.GL_BLEND);
    		GL11.glColor4d(1, 1, 1, 1);
            
            this.mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);
            int l = 14737632;
            if (packedFGColour != 0)l = packedFGColour;
            else if (!this.enabled)l = 10526880;
            else if (this.hovered)l = 16777120;
            this.drawCenteredString(fontrenderer,this.displayString,this.xPosition+this.width/2,this.yPosition+(this.height-8)/2,l);
        }
    }
}
