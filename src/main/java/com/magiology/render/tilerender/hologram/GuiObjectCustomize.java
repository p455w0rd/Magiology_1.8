package com.magiology.render.tilerender.hologram;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.magiology.api.updateable.Updater;
import com.magiology.core.Magiology;
import com.magiology.forgepowered.packets.packets.RenderObjectUploadPacket;
import com.magiology.gui.GuiUpdater.Updateable;
import com.magiology.gui.guiutil.gui.buttons.CleanButton;
import com.magiology.mcobjects.tileentityes.hologram.HoloObject;
import com.magiology.mcobjects.tileentityes.hologram.StringContainer;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.AdvancedPhysicsFloat;

public class GuiObjectCustomize extends GuiContainerM implements Updateable{
	
	EntityPlayer player;
	TileEntityHologramProjector hologramProjector;
	GuiTextField txt,red,green,blue,alpha,scale,size;
	HoloObject ro;
	boolean suportsText,textLimitedToObj,deleteStarted=false;
	StringContainer sc;
	AdvancedPhysicsFloat redF,greenF,blueF,alphaF;
	float resireScale;
	
	public GuiObjectCustomize(EntityPlayer player,TileEntityHologramProjector hologramProjector,HoloObject ro){
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
		
		
		GL11U.SetUpOpaqueRendering(1);
		GL11.glPushMatrix();
		GL11.glTranslatef(100, 100, 0);
		GL11U.scaled(-20);
		ro.render(new ColorF(hologramProjector.mainColor.x, hologramProjector.mainColor.y, hologramProjector.mainColor.z,0.2));
		
		GL11.glPopMatrix();
		
		
		super.drawGuiContainerBackgroundLayer(partTick, x, y);
		
		GL11U.SetUpOpaqueRendering(1);
		GL11U.texture(false);
		
		new ColorF(redF.getPoint(),greenF.getPoint(),blueF.getPoint(),alphaF.getPoint()).bind();
		drawTexturedModalRect(guiLeft+49, guiTop-28, 0, 0, 202, 5);
		int start=guiLeft+xSize-50;
		try{
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
		
		String s1="Scale",s2="RGBA",s3="Size: x, y",s4="Press <Enter> to confirm or click <Esc> to cancel.";
		Get.Render.FR().drawStringWithShadow(s1, guiLeft+1, guiTop+33, Color.WHITE.hashCode());
		Get.Render.FR().drawStringWithShadow(s2, start-50*4-Get.Render.FR().getStringWidth(s2)-2, guiTop-13, Color.WHITE.hashCode());
		Get.Render.FR().drawStringWithShadow(s2, start+3, guiTop-13, Color.WHITE.hashCode());
		Get.Render.FR().drawStringWithShadow(s3, guiLeft+xSize-Get.Render.FR().getStringWidth(s3)-1, guiTop+33, Color.WHITE.hashCode());
		if(deleteStarted){
			GL11.glPushMatrix();
			GL11.glTranslated(0, Math.sin(ro.host.getWorld().getTotalWorldTime()/8F)*4, 0);
			Get.Render.FR().drawStringWithShadow(s4, guiLeft+(xSize-Get.Render.FR().getStringWidth(s4))/2, guiTop+58, Color.RED.hashCode());
			GL11.glPopMatrix();
		}
		
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
		scale=new GuiTextField(0,fontRendererObj, start-50*5, guiTop+17, 100, 14);
		size=new GuiTextField(0,fontRendererObj, start-50*1, guiTop+17, 100, 14);
		red.setText(ro.setColor.r+"");
		green.setText(ro.setColor.g+"");
		blue.setText(ro.setColor.b+"");
		alpha.setText(ro.setColor.a+"");
		scale.setText(ro.scale+"");
		size.setText(U.round(ro.originalSize.x, 3)+", "+U.round(ro.originalSize.y, 3));
		if(suportsText)size.setEnabled(((StringContainer)ro).isTextLimitedToObj());
		red.setMaxStringLength(7);
		green.setMaxStringLength(7);
		blue.setMaxStringLength(7);
		alpha.setMaxStringLength(7);
		scale.setMaxStringLength(7);
		redF  =new AdvancedPhysicsFloat(ro.setColor.r, 0.1F,true);
		greenF=new AdvancedPhysicsFloat(ro.setColor.g, 0.1F,true);
		blueF =new AdvancedPhysicsFloat(ro.setColor.b, 0.1F,true);
		alphaF=new AdvancedPhysicsFloat(ro.setColor.a, 0.1F,true);
		
		buttonList.add(new CleanButton(0, guiLeft+xSize/2+5, guiTop+35, 30, 18, "Move", new ColorF(0.1, 0.2, 1, 0.6)));
		buttonList.add(new CleanButton(1, guiLeft+xSize/2-35, guiTop+35, 40, 18, "Delete", new ColorF(1, 0.1, 0.2, 0.6)));
		CleanButton 
			sx=new CleanButton(2, guiLeft+xSize/2-44, guiTop+15, 44, 18, "Snap X", new ColorF(0, 0.7, 0.1, 0.4)),
			sy=new CleanButton(3, guiLeft+xSize/2, guiTop+15, 44, 18, "Snap Y", new ColorF(0, 0.7, 0.1, 0.4));
		sx.enabled=sy.enabled=suportsText?((StringContainer)ro).isTextLimitedToObj():false;
		buttonList.add(sx);
		buttonList.add(sy);
		textFieldList.add(txt);
		textFieldList.add(red);
		textFieldList.add(green);
		textFieldList.add(blue);
		textFieldList.add(alpha);
		textFieldList.add(scale);
		textFieldList.add(size);
	}
	@Override
	protected void keyTyped(char Char, int id) throws IOException{
		int textLenght=txt.getText().length(),pos=txt.getCursorPosition();
		
		if(deleteStarted){
			if(Char==13||Char==27){
				deleteStarted=false;
				if(Char==13){
					ro.kill();
					Magiology.ROBOT.clickKeyKeyboard(KeyEvent.VK_ESCAPE);
				}
			}
			return;
		}
		if(Char==13){
			Magiology.ROBOT.clickKeyKeyboard(KeyEvent.VK_ESCAPE);
			return;
		}
		super.keyTyped(Char, id);
		
		if(textLenght!=txt.getText().length()){
			handleSpaces();
			txt.setText(Util.getStringForSize(txt.getText(),textLimitedToObj?ro.originalSize.x/Util.p:hologramProjector.size.x/Util.p));
			txt.setCursorPosition(pos-textLenght+txt.getText().length());
		}
		try{
			ro.setColor=new ColorF(
				Float.parseFloat(red.getText()+"0"),
				Float.parseFloat(green.getText()+"0"),
				Float.parseFloat(blue.getText()+"0"),
				Float.parseFloat(alpha.getText()+"0"));
		}catch(Exception e){}
		try{
			float scal=Float.parseFloat(scale.getText());
			if(scal<0.01)scal=1;
			if(ro.originalSize.x*scal>hologramProjector.size.x||ro.originalSize.y*scal>hologramProjector.size.y)throw new IllegalStateException("To big scale!");
			ro.scale=scal;
			scale.setTextColor(Color.WHITE.hashCode());
		}catch(Exception e){
			scale.setTextColor(Color.RED.hashCode());
		}
		try{
			String[] xy=size.getText().split(",");
			if(xy.length!=2)throw new IllegalStateException("Wrong string!");
			float x=Float.parseFloat(xy[0]+"0"),y=Float.parseFloat(xy[1]+"0");
			ro.originalSize.x=x;
			ro.originalSize.y=y;
			size.setTextColor(Color.WHITE.hashCode());
		}catch(Exception e){
			size.setTextColor(Color.RED.hashCode());
		}
		if(suportsText&&!sc.isTextLimitedToObj()){
			sc.setString(Util.getStringForSize(txt.getText(),textLimitedToObj?ro.originalSize.x/Util.p:hologramProjector.size.x/Util.p).trim());
			if(textLenght!=txt.getText().length())txt.setCursorPosition(pos-textLenght+txt.getText().length());
		}
	}
	@Override
	public void onGuiClosed(){
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		txt.setText(Util.getStringForSize(txt.getText(),textLimitedToObj?ro.originalSize.x/Util.p:hologramProjector.size.x/Util.p).trim());
		if(suportsText){
			sc.setString(txt.getText());
			if(sc.getString().isEmpty())sc.setString("   ");
		}
		try{
			ro.setColor=new ColorF(
				Float.parseFloat(red.getText()+"0"),
				Float.parseFloat(green.getText()+"0"),
				Float.parseFloat(blue.getText()+"0"),
				Float.parseFloat(alpha.getText()+"0"));
		}catch(Exception e){}
		try{
			float scal=Float.parseFloat(scale.getText());
			if(scal<0.01)scal=1;
			if(ro.originalSize.x*scal>hologramProjector.size.x||ro.originalSize.y*scal>hologramProjector.size.y)throw new IllegalStateException("To big scale!");
			ro.scale=scal;
		}catch(Exception e){}
		try{
			String[] xy=size.getText().split(",");
			if(xy.length!=2)throw new IllegalStateException("Wrong string!");
			float x=Float.parseFloat(xy[0]+"0"),y=Float.parseFloat(xy[1]+"0");
			ro.originalSize.x=x;
			ro.originalSize.y=y;
		}catch(Exception e){}
		Util.sendMessage(new RenderObjectUploadPacket(ro));
		textFieldList.clear();
	}
	@Override
	protected void mouseClicked(int x, int y, int id) throws IOException{
		if(deleteStarted)return;
		super.mouseClicked(x,y,id);
	}
	@Override
	protected void actionPerformed(GuiButton g){
		if(deleteStarted)return;
		int id=g.id;
		switch(id){
		case 0:{
			new Thread(new Runnable(){@Override public void run(){
				try{
					Thread.sleep(100);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				ro.moveMode=true;
				Magiology.ROBOT.clickKeyKeyboard(KeyEvent.VK_ESCAPE);
			}}).start();
			
		}break;
		case 1:deleteStarted=true;break;
		case 2:{
			try{
				String[] xy=size.getText().replaceAll("\\s","").split(",");
				if(xy.length!=2)throw new IllegalStateException("Wrong string!");
				xy[0]=U.round((Get.Render.FR().getStringWidth(sc.getString())+2)*U.p, 3)+"";
				size.setText(xy[0]+", "+xy[1]);
				float x=Float.parseFloat(xy[0]+"0"),y=Float.parseFloat(xy[1]+"0");
				ro.originalSize.x=x;
				ro.originalSize.y=y;
			}catch(Exception e){}
		}break;
		case 3:{
			try{
				String[] xy=size.getText().replaceAll("\\s","").split(",");
				if(xy.length!=2)throw new IllegalStateException("Wrong string!");
				xy[1]=U.round((Get.Render.FR().FONT_HEIGHT+2)*U.p, 3)+"";
				size.setText(xy[0]+", "+xy[1]);
				float x=Float.parseFloat(xy[0]+"0"),y=Float.parseFloat(xy[1]+"0");
				ro.originalSize.x=x;
				ro.originalSize.y=y;
			}catch(Exception e){}
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
		Updater.update(buttonList);
		try{
			redF.wantedPoint=Float.parseFloat(red.getText()+"0");
			red.setTextColor(Color.WHITE.hashCode());
			redF.update();
		}catch(Exception e){
			red.setTextColor(Color.RED.hashCode());
		}
		try{
			greenF.wantedPoint=Float.parseFloat(green.getText()+"0");
			green.setTextColor(Color.WHITE.hashCode());
			greenF.update();
		}catch(Exception e){
			green.setTextColor(Color.RED.hashCode());
		}
		try{
			blueF.wantedPoint=Float.parseFloat(blue.getText()+"0");
			blue.setTextColor(Color.WHITE.hashCode());
			blueF.update();
		}catch(Exception e){
			blue.setTextColor(Color.RED.hashCode());
		}
		try{
			alphaF.wantedPoint=Float.parseFloat(alpha.getText()+"0");
			alpha.setTextColor(Color.WHITE.hashCode());
			alphaF.update();
		}catch(Exception e){
			alpha.setTextColor(Color.RED.hashCode());
		}
	}
}
