package com.magiology.render.tilerender.hologram;

import java.awt.event.KeyEvent;
import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.magiology.core.Magiology;
import com.magiology.forgepowered.packets.packets.RenderObjectUploadPacket;
import com.magiology.gui.GuiUpdater.Updateable;
import com.magiology.mcobjects.tileentityes.hologram.RenderObject;
import com.magiology.mcobjects.tileentityes.hologram.StringContainer;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.AdvancedPhysicsFloat;

public class GuiObjectCustomize extends GuiContainer implements Updateable{
	
	EntityPlayer player;
	TileEntityHologramProjector hologramProjector;
	GuiTextField txt,red,green,blue,alpha;
	RenderObject ro;
	boolean suportsText,textLimitedToObj;
	StringContainer sc;
	AdvancedPhysicsFloat redF,greenF,blueF,alphaF;
	float resireScale;
	
	public GuiObjectCustomize(EntityPlayer player,TileEntityHologramProjector hologramProjector,RenderObject ro){
		super(new GuiObjectCustomizeContainer(player,hologramProjector));
		this.player=player;
		this.hologramProjector=hologramProjector;
		this.ro=ro;
		suportsText=ro instanceof StringContainer;
		if(suportsText){
			sc=(StringContainer)ro;
			textLimitedToObj=sc.isTextLimitedToObj();
		}
		this.xSize=300;
		this.ySize=14;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partTick, int x, int y){
		GL11.glPushMatrix();
		txt.drawTextBox();
		red.drawTextBox();
		green.drawTextBox();
		blue.drawTextBox();
		alpha.drawTextBox();
		
		GL11U.SetUpOpaqueRendering(1);
		GL11U.texture(false);
		
		ColorF mainColor=new ColorF(hologramProjector.mainColor.x, hologramProjector.mainColor.y, hologramProjector.mainColor.z,0.2);
		mainColor.bind();
		int xs=(int)hologramProjector.size.x*20, ys=(int)hologramProjector.size.y*20;
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef(guiLeft, 10, 0);
		drawTexturedModalRect(0,0, 0, 0, xs,ys);
		
		GL11.glTranslatef(-(int)(ro.offset.x*20),-(int)(ro.offset.y*20), 0);
		drawTexturedModalRect(0,0, 0, 0, (int)(ro.size.x*20),(int)(ro.size.y*20));
		
		GL11.glPopMatrix();
		
		new ColorF(redF.getPoint(),greenF.getPoint(),blueF.getPoint(),alphaF.getPoint()).bind();
		drawTexturedModalRect(guiLeft+49, guiTop-28, 0, 0, 202, 5);
		try{
			int start=guiLeft+xSize-50;
			new ColorF(0,0,0,1).set(redF.getPoint(), 'r').bind();
			drawTexturedModalRect(start-50*1,   guiTop-23, 0, 0, 51, 5);
			new ColorF(0,0,0,1).set(greenF.getPoint(), 'g').bind();
			drawTexturedModalRect(start-50*2,   guiTop-23, 0, 0, 50, 5);
			new ColorF(0,0,0,1).set(blueF.getPoint(), 'b').bind();
			drawTexturedModalRect(start-50*3,   guiTop-23, 0, 0, 50, 5);
			new ColorF(1,1,1,0).set(alphaF.getPoint(), 'a').bind();
			drawTexturedModalRect(start-50*4-1, guiTop-23, 0, 0, 51, 5);
		}catch(Exception e){
			e.printStackTrace();
		}
		GL11U.texture(true);
		GL11U.EndOpaqueRendering();
		
		GL11.glPopMatrix();
		
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_){
	}
	@Override
	public void initGui(){
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		txt=new GuiTextField(0, fontRendererObj,  guiLeft,  guiTop, 300, 14);
		if(suportsText){
			txt.setText(sc.getString().equals("   ")?"":sc.getString());
			txt.setMaxStringLength(Integer.MAX_VALUE);
		}
		txt.setFocused(suportsText);
		txt.setEnabled(suportsText);
		
		int start=guiLeft+xSize-50;
		red=new GuiTextField(0,fontRendererObj, start-50*1, guiTop-17, 50, 14);
		green=new GuiTextField(0,fontRendererObj, start-50*2, guiTop-17, 50, 14);
		blue=new GuiTextField(0,fontRendererObj, start-50*3, guiTop-17, 50, 14);
		alpha=new GuiTextField(0,fontRendererObj, start-50*4, guiTop-17, 50, 14);
		red.setText(ro.setColor.r+"");
		green.setText(ro.setColor.g+"");
		blue.setText(ro.setColor.b+"");
		alpha.setText(ro.setColor.a+"");
		
		red.setMaxStringLength(7);
		green.setMaxStringLength(7);
		blue.setMaxStringLength(7);
		alpha.setMaxStringLength(7);
		redF  =new AdvancedPhysicsFloat(ro.setColor.r, 0.1F,true);
		greenF=new AdvancedPhysicsFloat(ro.setColor.g, 0.1F,true);
		blueF =new AdvancedPhysicsFloat(ro.setColor.b, 0.1F,true);
		alphaF=new AdvancedPhysicsFloat(ro.setColor.a, 0.1F,true);
		buttonList.add(new GuiButton(0, guiLeft+xSize/2-15, guiTop+15, 30, 18, "move"));
	}
	@Override
	protected void keyTyped(char Char, int id) throws IOException{
		//Enter simulates the esc button
		if(Char==13){
			Magiology.ROBOT.clickKeyKeyboard(KeyEvent.VK_ESCAPE);
			return;
		}
		int textLenght=txt.getText().length(),pos=txt.getCursorPosition();
		if(!txt.textboxKeyTyped(Char, id))
			if(!red.textboxKeyTyped(Char, id))
				if(!green.textboxKeyTyped(Char, id))
					if(!blue.textboxKeyTyped(Char, id))
						if(!alpha.textboxKeyTyped(Char, id))
							super.keyTyped(Char, id);
		if(textLenght!=txt.getText().length()){
			handleSpaces();
			txt.setText(Util.getStringForSize(txt.getText(),textLimitedToObj?ro.size.x/Util.p:hologramProjector.size.x/Util.p));
			txt.setCursorPosition(pos-textLenght+txt.getText().length());
		}
		ro.setColor=new ColorF(
				Float.parseFloat(red.getText()+"0"),
				Float.parseFloat(green.getText()+"0"),
				Float.parseFloat(blue.getText()+"0"),
				Float.parseFloat(alpha.getText()+"0"));
		if(suportsText&&!sc.isTextLimitedToObj()){
			sc.setString(Util.getStringForSize(txt.getText(),textLimitedToObj?ro.size.x/Util.p:hologramProjector.size.x/Util.p).trim());
			if(textLenght!=txt.getText().length())txt.setCursorPosition(pos-textLenght+txt.getText().length());
		}
	}
	@Override
	public void onGuiClosed(){
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		txt.setText(Util.getStringForSize(txt.getText(),textLimitedToObj?ro.size.x/Util.p:hologramProjector.size.x/Util.p).trim());
		if(suportsText){
			sc.setString(txt.getText());
			if(sc.getString().isEmpty())sc.setString("   ");
		}
		ro.setColor=new ColorF(
				Float.parseFloat(red.getText()+"0"),
				Float.parseFloat(green.getText()+"0"),
				Float.parseFloat(blue.getText()+"0"),
				Float.parseFloat(alpha.getText()+"0"));
		Util.sendMessage(new RenderObjectUploadPacket(ro));
	}
	@Override
	protected void mouseClicked(int x, int y, int id) throws IOException{
		txt.mouseClicked(x, y, id);
		red.mouseClicked(x, y, id);
		green.mouseClicked(x, y, id);
		blue.mouseClicked(x, y, id);
		alpha.mouseClicked(x, y, id);
		super.mouseClicked(x,y,id);
	}
	@Override
	protected void actionPerformed(GuiButton g){
		int id=g.id;
		switch(id){
		case 0:{
			ro.moveMode=true;
			Magiology.ROBOT.clickKeyKeyboard(KeyEvent.VK_ESCAPE);
		}break;
		default:{
			
		}break;
		}
	}
	private void handleSpaces(){
		int pointerPos=txt.getCursorPosition();
		if(!txt.getText().isEmpty()&&txt.getText().length()>2){
			if(txt.getText().charAt(0)==' '&&txt.getText().charAt(1)==' ')txt.setText(txt.getText().substring(1));
			if(txt.getText().charAt(txt.getText().length()-1)==' '&&txt.getText().charAt(txt.getText().length()-2)==' ')txt.setText(txt.getText().substring(0,txt.getText().length()-1));
		}
		txt.setCursorPosition(pointerPos);
	}
	@Override
	public void update(){
		redF.wantedPoint=Float.parseFloat(red.getText()+"0");
		redF.update();
		greenF.wantedPoint=Float.parseFloat(green.getText()+"0");
		greenF.update();
		blueF.wantedPoint=Float.parseFloat(blue.getText()+"0");
		blueF.update();
		alphaF.wantedPoint=Float.parseFloat(alpha.getText()+"0");
		alphaF.update();
	}
}
