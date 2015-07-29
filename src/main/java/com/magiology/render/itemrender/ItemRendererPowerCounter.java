package com.magiology.render.itemrender;

import com.magiology.forgepowered.event.RenderLoopEvents;
import com.magiology.objhelper.helpers.renderers.ShadedQuad;
import com.magiology.render.Textures;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class ItemRendererPowerCounter implements IItemRenderer {
	Tessellator tess=Tessellator.instance;
	FontRenderer fr=Minecraft.getMinecraft().fontRenderer;
	private final float p= 1F/16F;
	private final float tWC=1F/64F;
	private final float tHC=1F/64F;
	double anim,powerBar;
	int maxPB,currentP;
	String block;
	
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type){return true;}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper){return true;}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if(item.stackTagCompound!=null){
			NBTTagCompound PC= item.stackTagCompound;
			anim=PC.getDouble("pAnim")+(PC.getDouble("pAnim")-PC.getDouble("anim"))*RenderLoopEvents.partialTicks;
			powerBar=PC.getDouble("powerBar");
			maxPB=PC.getInteger("maxEn");
			currentP=PC.getInteger("currentEn");
			block=PC.getString("block");
//			System.out.println((PC.getDouble("pAnim")-PC.getDouble("anim")));
		}
		
		
		float x=0;
		float y=0;
		float z=0;
		float xr=0;
		float yr=0;
		float zr=0;
		float scale=1;
		
		if(ItemRenderType.ENTITY == type){
//			xr=90;
			z=-0.4F;
//			y=-1.05F;
			x=-0.4F;
			scale=0.9F;
		}
		else if(ItemRenderType.EQUIPPED_FIRST_PERSON == type){
			x=-0.8F;
			z=0.2F;
			xr=-5;
			zr=-5;
			yr=-45;
			y=0.8F+(float)anim/2.7F;
			xr+=5*(float)anim;
			zr+=5*(float)anim;
			x+=-1.07F*(float)anim;
		}
		else if(ItemRenderType.EQUIPPED == type){
			scale=1.2F;
			x=1F;
			y=-0.2F;
			z=1F;
			xr=60;
			yr=30;
			zr=-60;
		}
		else if(ItemRenderType.INVENTORY == type){
			y=-1.2F;
			yr=180;
			y+=(float)anim*0.6F;
			yr+=50*(float)anim;
			z+=1*(float)anim;
			scale=1.4F;
		}
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
	    GL11.glTranslatef(x, y, z);
	    GL11.glRotatef(-xr, 1, 0, 0);GL11.glRotatef(-yr, 0, 1, 0);GL11.glRotatef(-zr, 0, 0, 1);
	    GL11.glScalef(scale, scale, scale);
	    
		drawCore();
		GL11.glEnable(GL11.GL_LIGHTING);
		drawText();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
	public void drawText(){
		float x=p*4-0.0001F;
		float y=p*9;
		float z=p*3;
		float xr=90;
		float yr=-90;
		float zr=90;
		float scale=0.0049F;
		
		GL11.glTranslatef(x, y, z);
	    GL11.glRotatef(-xr, 1, 0, 0);GL11.glRotatef(-yr, 0, 1, 0);GL11.glRotatef(-zr, 0, 0, 1);
	    GL11.glScalef(scale, scale, scale);
		
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        String pauwa=Integer.toString(maxPB)+"/"+Integer.toString(currentP);
        double Precent=currentP!=maxPB?(powerBar*100>0?powerBar*100:0):100;
        DecimalFormat df = new DecimalFormat("###.##");
        String PrecentS=Precent<=0?(currentP>0?"Almost empty":"Empty"):(df.format(Precent)+"%");
        
        fr.drawString(pauwa, 0, 0, 11111);
        fr.drawString(PrecentS, 0, 10, 11111);
        fr.drawString(block, 0, 20, 11111);
        
        GL11.glRotatef(xr, 1, 0, 0);GL11.glRotatef(yr, 0, 1, 0);GL11.glRotatef(zr, 0, 0, 1);
	    GL11.glTranslatef(-x, -y, -z);
	}

	public void drawCore(){
		Minecraft.getMinecraft().renderEngine.bindTexture(Textures.PowerCounterEnergyBar);
		double var1=powerBar;
		double var2=p*5+p*4*var1;
		double var3=1-var1;
		ShadedQuad.addVertexWithUVWRender(p*4-0.0001, var2, p*11, 0, var3);
		ShadedQuad.addVertexWithUVWRender(p*4-0.0001, p*5,  p*11, 0, 1);
		ShadedQuad.addVertexWithUVWRender(p*4-0.0001, p*5,  p*13, 1, 1);
		ShadedQuad.addVertexWithUVWRender(p*4-0.0001, var2, p*13, 1, var3);
		
		double minx=p*4;double miny=p*4;double minz=p*2;
		double maxx=p*10;double maxy=p*10;double maxz=p*14;
		
		
		Minecraft.getMinecraft().renderEngine.bindTexture(Textures.PowerCounterFront);
		ShadedQuad.addVertexWithUVWRender(minx, maxy, minz, 0, 0);
		ShadedQuad.addVertexWithUVWRender(minx, miny, minz, 0, 1);
		ShadedQuad.addVertexWithUVWRender(minx, miny, maxz, 1, 1);
		ShadedQuad.addVertexWithUVWRender(minx, maxy, maxz, 1, 0);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(Textures.PowerCounterSide1);
		ShadedQuad.addVertexWithUVWRender(maxx, maxy, maxz, 1, 1);
		ShadedQuad.addVertexWithUVWRender(maxx, miny,  maxz, 1, 0);
		ShadedQuad.addVertexWithUVWRender(maxx, miny,  minz, 0, 0);
		ShadedQuad.addVertexWithUVWRender(maxx, maxy, minz, 0, 1);
		
		ShadedQuad.addVertexWithUVWRender(minx, maxy, maxz, 0, 1);
		ShadedQuad.addVertexWithUVWRender(minx, miny , maxz, 0, 0);
		ShadedQuad.addVertexWithUVWRender(maxx, miny, maxz, 1, 0);
		ShadedQuad.addVertexWithUVWRender(maxx, maxy, maxz, 1, 1);
		
		ShadedQuad.addVertexWithUVWRender(maxx, maxy, minz, 1, 1);
		ShadedQuad.addVertexWithUVWRender(maxx, miny, minz, 1, 0);
		ShadedQuad.addVertexWithUVWRender(minx, miny , minz, 0, 0);
		ShadedQuad.addVertexWithUVWRender(minx, maxy, minz, 0, 1);
		
		ShadedQuad.addVertexWithUVWRender(maxx, maxy, maxz, 1, 1);
		ShadedQuad.addVertexWithUVWRender(maxx, maxy, minz, 1, 0);
		ShadedQuad.addVertexWithUVWRender(minx, maxy, minz, 0, 0);
		ShadedQuad.addVertexWithUVWRender(minx, maxy, maxz, 0, 1);
		
		ShadedQuad.addVertexWithUVWRender(minx, miny, maxz, 0, 1);
		ShadedQuad.addVertexWithUVWRender(minx, miny, minz, 0, 0);
		ShadedQuad.addVertexWithUVWRender(maxx, miny, minz, 1, 0);
		ShadedQuad.addVertexWithUVWRender(maxx, miny, maxz, 1, 1);
	}
	
}
