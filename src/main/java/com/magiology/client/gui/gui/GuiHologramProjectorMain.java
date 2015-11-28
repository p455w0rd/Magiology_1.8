package com.magiology.client.gui.gui;

import java.awt.event.*;
import java.io.*;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.entity.player.*;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

import com.magiology.api.updateable.*;
import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.client.gui.container.*;
import com.magiology.client.gui.custom.*;
import com.magiology.client.gui.guiutil.gui.buttons.*;
import com.magiology.core.*;
import com.magiology.forgepowered.packets.packets.*;
import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.util.renderers.*;
import com.magiology.util.utilclasses.Get.Render.Font;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.vectors.*;

public class GuiHologramProjectorMain extends GuiContainer implements Updateable{
	
	EntityPlayer player;
	TileEntityHologramProjector tile;
	AdvancedPhysicsFloat helpAlpha=new AdvancedPhysicsFloat(0, 0.1F, true);
	static NormalizedVertixBufferModel arrowsModel;
	
	public GuiHologramProjectorMain(EntityPlayer player,TileEntityHologramProjector tile){
		super(new ContainerEmpty());
		this.player=player;
		this.tile=tile;
		this.xSize=100;
		this.ySize=67;
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		float helpAlpha=this.helpAlpha.getPoint();
		if(helpAlpha<0.97){
			String s="Hold <Ctrl> to show help";
			GL11.glPushMatrix();
			GL11.glTranslatef(0, -5, 0);
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			GL11.glTranslatef((-Font.FR().getStringWidth(s)/4+xSize)/2, 0, 0);
			Font.FR().drawStringWithShadow(s, 0, 0, new ColorF(1,1,1,0.5F*(1-helpAlpha)).toCode());
			GL11.glPopMatrix();
		}
		if(helpAlpha>0.03){
			int color=new ColorF(1, 1, 1, helpAlpha).toCode();
			String ish="Is highlight™ going to be be applied";
			Font.FR().drawStringWithShadow(ish, 150-Font.FR().getStringWidth(ish), ySize+2, color);
			Font.FR().drawStringWithShadow("Spawn new component", -10, -9, color);
			
			if(arrowsModel==null)
				initModels();
			GL11.glLineWidth(1);
			arrowsModel.draw();
			GL11.glColor4f(1, 1, 1, 1);
		}
	}
	@Override
	public void initGui(){
		super.initGui();
		
		int ys=ySize/4;
		buttonList.add(new CleanButton(0, guiLeft, guiTop,      xSize, ys, "Text Box", new ColorF(0.8, 0.2, 0.2, 0.8)));
		buttonList.add(new CleanButton(1, guiLeft, guiTop+ys+1,   xSize, ys, "Button", new ColorF(0.2, 0.8, 0.2, 0.8)));
		buttonList.add(new CleanButton(2, guiLeft, guiTop+ys*2+2, xSize, ys, "Field", new ColorF(0.2, 0.2, 0.8, 0.8)));
		buttonList.add(new CleanButton(3, guiLeft, guiTop+ys*3+3, xSize, ys, "Slider", new ColorF(0.2, 0.8, 0.8, 0.8)));
		
		OnOffGuiButton 
			b1=new OnOffGuiButton(0, guiLeft+xSize+1, guiTop+2, 24, 12, new ColorF(0.8, 0.2, 0.2, 0.8)),
			b2=new OnOffGuiButton(1, guiLeft+xSize+1, guiTop+ys+3, 24, 12, new ColorF(0.2, 0.8, 0.2, 0.8)),
			b3=new OnOffGuiButton(2, guiLeft+xSize+1, guiTop+ys*2+4, 24, 12, new ColorF(0.2, 0.2, 0.8, 0.8)),
			b4=new OnOffGuiButton(3, guiLeft+xSize+1, guiTop+ys*3+5, 24, 12, new ColorF(0.2, 0.8, 0.8, 0.8));
		b1.forceIsOn(tile.highlighs[0]);
		b2.forceIsOn(tile.highlighs[1]);
		b3.forceIsOn(tile.highlighs[2]);
		b4.forceIsOn(tile.highlighs[3]);
		buttonList.add(b1);
		buttonList.add(b2);
		buttonList.add(b3);
		buttonList.add(b4);
	}


	@Override
	public void update(){
		Updater.update(buttonList);
		helpAlpha.wantedPoint=isCtrlKeyDown()?1:0;
		helpAlpha.point+=0.03;
		helpAlpha.update();
	}
	@Override
	protected void actionPerformed(GuiButton button)throws IOException{
		if(button instanceof OnOffGuiButton){
			tile.highlighs[button.id]=((OnOffGuiButton)button).isOn();
			UtilM.sendMessage(new HologramProjectorUpload(tile));
		}else{
			switch(button.id){
			case 0:{
				U.sendMessage(new RenderObjectUploadPacket(new TextBox(tile, "<empty>")));
				Magiology.ROBOT.clickKeyKeyboard(KeyEvent.VK_ESCAPE);
			}break;
			case 1:{
				U.sendMessage(new RenderObjectUploadPacket(new Button(tile, new Vector2f(3, 1))));
				Magiology.ROBOT.clickKeyKeyboard(KeyEvent.VK_ESCAPE);
			}break;
			case 2:{
				U.sendMessage(new RenderObjectUploadPacket(new Field(tile, new Vector2f(1, 1))));
				Magiology.ROBOT.clickKeyKeyboard(KeyEvent.VK_ESCAPE);
			}break;
			case 3:{
				U.sendMessage(new RenderObjectUploadPacket(new Slider(tile, new Vector2f(UtilM.p*6, 1))));
				Magiology.ROBOT.clickKeyKeyboard(KeyEvent.VK_ESCAPE);
			}break;
			}
		}
	}
	private void initModels(){
		//generate curve vertices
		AdvancedPhysicsFloat arrowX=new AdvancedPhysicsFloat(0.001F, 0.1F);
		arrowX.wantedPoint=-0.05F;
		arrowX.speed=2;
		int arrowY=0;
		NormalizedVertixBuffer buff=TessUtil.getNVB();
		buff.setDrawAsWire(true);
		while(arrowX.point>1||arrowX.speed>0){
			arrowX.update();
			buff.addVertex(arrowX.point, arrowY, 0);
			buff.addVertex(arrowX.prevPoint, arrowY+1, 0);
			arrowY--;
		}
		//add arrow end
		buff.addVertex(arrowX.point, arrowY+1, 0);
		buff.addVertex(5, arrowY, 0);
		buff.addVertex(arrowX.point, arrowY+1, 0);
		buff.addVertex(arrowX.point, arrowY+1, 0);
		
		buff.addVertex(arrowX.point, arrowY+1, 0);
		buff.addVertex(2.2, arrowY+6, 0);
		
		//Set up rendering for reusable line rendering
		buff.setClearing(false);
		buff.setInstantNormalCalculation(false);
		NormalizedVertixBuffer buffer=new NormalizedVertixBuffer();
		//draw & transform
		buff.pushMatrix();
		buff.translate(140, ySize+1, 0);
		
		buff.rotate(0, 0, -13);
		buff.scale(1.6);
		buff.transformAndSaveTo(buffer);
		
		buff.rotate(0, 0, -5);
		buff.scale(0.75);
		buff.transformAndSaveTo(buffer);
		
		buff.rotate(0, 0, -10);
		buff.scale(0.65);
		buff.transformAndSaveTo(buffer);
		
		buff.rotate(0, 0, -17);
		buff.scale(0.65);
		buff.transformAndSaveTo(buffer);
		
		buff.popMatrix();
		
		buff.pushMatrix();
		buff.translate(-11, -5, 0);
		
		buff.rotate(0, 0, 145);
		buff.scale(0.45);
		buff.transformAndSaveTo(buffer);
		
		buff.rotate(0, 0, 16);
		buff.scale(1.8);
		buff.transformAndSaveTo(buffer);
		
		buff.rotate(0, 0, 7);
		buff.scale(1.55);
		buff.transformAndSaveTo(buffer);
		
		buff.rotate(0, 0, 3);
		buff.scale(1.35);
		buff.transformAndSaveTo(buffer);
		
		buff.popMatrix();
		
		//reset rendering
		buff.setClearing(true);
		buff.setDrawAsWire(false);
		buff.setInstantNormalCalculation(true);
		
		//clear curve vertices
		buff.cleanUp();
		//export to model
		arrowsModel=buffer.exportToNoramlisedVertixBufferModel();
		arrowsModel.setDrawAsWire(true);
		UtilM.println("Info: "+getClass().getSimpleName()+" has initialized a model. This may cause a FPS dorp for a second.");
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){}
}
