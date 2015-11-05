package com.magiology.client.gui.gui;

import static com.magiology.io.WorldData.WorkingProtocol.*;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.magiology.api.lang.ProgramHolder;
import com.magiology.api.updateable.Updater;
import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.client.gui.container.CommandCenterContainer;
import com.magiology.client.gui.container.ContainerEmpty;
import com.magiology.client.gui.custom.OnOffGuiButton;
import com.magiology.client.gui.guiutil.gui.GuiTextEditor;
import com.magiology.client.render.Textures;
import com.magiology.client.render.font.FontRendererMBase;
import com.magiology.core.Magiology;
import com.magiology.forgepowered.packets.packets.OpenProgramContainerInGui;
import com.magiology.io.WorldData;
import com.magiology.mcobjects.items.ProgramContainer;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.m_extension.GuiContainerM;
import com.magiology.util.utilobjects.vectors.AdvancedPhysicsFloat;
import com.magiology.util.utilobjects.vectors.Vec2i;

public class GuiProgramContainerEditor extends GuiContainerM implements Updateable{
	
	private static WorldData<String,String> settingsData=new WorldData<String,String>("jsEditorSettings","dat","jsEdit",FROM_CLIENT);
	public static void loadClass(){}
	
	private ItemStack stack;
	private int slotId;
	private BlockPos tilePos;
	private GuiTextEditor 
		programSrc=new GuiTextEditor(new Vec2i(0, 50),new Vec2i(300, 600)),
		programLog=new GuiTextEditor(new Vec2i(0, 50),new Vec2i(300, 600));
	private boolean firstInit=true,logActive=false;
	private AdvancedPhysicsFloat 
		compile=new AdvancedPhysicsFloat(0.3F, 0.3F, true),
		settings=new AdvancedPhysicsFloat(0.3F, 0.3F, true),
		log=new AdvancedPhysicsFloat(0.3F, 0.3F, true),
		buttonY=new AdvancedPhysicsFloat(0, 0.3F, true),
		overallAlpha=new AdvancedPhysicsFloat(0, 0.2F, true);
	private OnOffGuiButton saveOnExit;
	
	public GuiProgramContainerEditor(EntityPlayer player){
		super(new ContainerEmpty());
		
		if(player.openContainer instanceof CommandCenterContainer){
			CommandCenterContainer container=((CommandCenterContainer)player.openContainer);
			stack=((Slot)container.inventorySlots.get(slotId=container.selectedSlotId+36)).getStack();
			tilePos=container.tile.getPos();
		}else{
			stack=player.inventory.mainInventory[slotId=player.inventory.currentItem];
		}
		xSize=200;
		ySize=166;
		programLog.viewOnly=true;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		getEditor().size=new Vec2i(xSize+guiLeft*2-20, ySize+guiTop*2-20);
		getEditor().pos=new Vec2i(10, 10);
		
		getEditor().render(mouseX,mouseY);
		
		Vec2i pos=getEditor().pos.add(0,getEditor().size.y-16-(int)(buttonY.getPoint()*10));
		
		compile .wantedPoint=new Rectangle(pos.x+ 0,pos.y,16,16).contains(mouseX,mouseY)?1:0.3F;
		settings.wantedPoint=new Rectangle(pos.x+16,pos.y,16,16).contains(mouseX,mouseY)?1:0.3F;
		log     .wantedPoint=new Rectangle(pos.x+32,pos.y,16,16).contains(mouseX,mouseY)?1:0.3F;
		
		GL11U.SetUpOpaqueRendering(1);
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		GL11U.scaled(0.5F);
		
		ColorF background=ColorF.convert(new Color(16,16,32,220));
		background.a*=overallAlpha.getPoint();
		GL11U.color(background);
		GL11U.texture(false);
		drawTexturedModalRect(0, 0, 0, 0, 96, 32);
		GL11U.texture(true);
		GL11U.blendFunc(2);
		ColorF color=new ColorF();
		
		String message="";
		if(compile.wantedPoint!=0.3F){
			color.a=compile.getPoint()*overallAlpha.getPoint();
			message="Save/Compile";
		}
		if(settings.wantedPoint!=0.3F){
			color.a=settings.getPoint()*overallAlpha.getPoint();
			message="Settings";
		}
		if(log.wantedPoint!=0.3F){
			color.a=log.getPoint()*overallAlpha.getPoint();
			message=logActive?"Switch to code":"Switch to program log";
		}
		FontRendererMBase fr=Get.Render.Font.FRB();
		if(!message.isEmpty()){
			background.a=color.a*overallAlpha.getPoint();
			GL11U.color(background);
			GL11U.blendFunc(1);
			GL11U.scaled(2);
			GL11U.texture(false);
			drawTexturedModalRect(0, -9, 0, 0, fr.getStringWidth(message)+1, 10);
			GL11U.texture(true);
			fr.drawString(message, 0, -8, color.toCode());
			GL11U.scaled(0.5F);
		}
		TessUtil.bindTexture(Textures.ProgramGui);
		color.a=compile.getPoint()*overallAlpha.getPoint();
		color.bind();
		drawModalRectWithCustomSizedTexture(0,0, 0, 0, 32, 32, 64, 64);
		color.a=settings.getPoint()*overallAlpha.getPoint();
		color.bind();
		drawModalRectWithCustomSizedTexture(32,0, 32, 0, 32, 32, 64, 64);
		color.a=log.getPoint()*overallAlpha.getPoint();
		color.bind();
		drawModalRectWithCustomSizedTexture(64,0, 0, 32, 32, 32, 64, 64);
		
		GL11.glPopMatrix();
		GL11U.EndOpaqueRendering();
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}	
	@Override
	public void initGui(){
		xSize=getEditor().size.x;
		ySize=getEditor().size.y;
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		if(firstInit)programSrc.setText(ProgramHolder.code_get(ProgramContainer.getId(stack)));
		firstInit=false;
		try{
			saveOnExit=new OnOffGuiButton(0, 40, 20, 20, 10, new ColorF(0.8, 0.2, 0.2, 0.8));
			saveOnExit.forceIsOn(Boolean.parseBoolean(settingsData.getFileContent("saveOnExit").text.toString()));
		}catch(Exception e){}
		buttonList.add(saveOnExit);
	}
	
	@Override
	protected void actionPerformed(GuiButton b)throws IOException{
		
		if(b.id==saveOnExit.id){
			settingsData.addFile("saveOnExit", saveOnExit.isOn()+"");
			settingsData.saveFromWorld(Util.getTheWorld());
		}
		
	}
	@Override
	public void onGuiClosed(){
		Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException{
		try{
			boolean active=getEditor().active;
			if(!getEditor().keyTyped(keyCode, typedChar))super.keyTyped(typedChar, keyCode);
			if(active&&!getEditor().active)Magiology.ROBOT.clickKeyKeyboard(KeyEvent.VK_ESCAPE);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		try{
			getEditor().mouseClickMove(mouseX, mouseY);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)throws IOException{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		Vec2i pos=getEditor().pos.add(0,getEditor().size.y-16);
		if(compile.wantedPoint==1){
			Util.sendMessage(new OpenProgramContainerInGui.ExitGui(slotId, programSrc.getText(),tilePos));
		}else if(settings.wantedPoint==1){
			
		}else if(log.wantedPoint==1){
			logActive=!logActive;
			
			List<CharSequence> logData=ProgramHolder.log_get(ProgramContainer.getId(stack));
			List<StringBuilder> logDataNew=new ArrayList<StringBuilder>();
			for(int i=0;i<logData.size();i++){
				String line=logData.get(i).toString();
				if(line.startsWith(ProgramHolder.err))logDataNew.add(new StringBuilder(line.substring(ProgramHolder.err.length())));
				else logDataNew.add(new StringBuilder(line));
			}
			programLog.setText(Util.join("\n",logDataNew.toArray()));
			programLog.sliderY=1;
		}else try{
			getEditor().mouseClicked(mouseX, mouseY, mouseButton);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void update(){
		getEditor().update();
		
		compile.update();
		log.update();
		settings.update();
		
		buttonY.wantedPoint=getEditor().maxWidth>getEditor().size.x?1:0;
		
		overallAlpha.wantedPoint=compile.wantedPoint!=0.3F||settings.wantedPoint!=0.3F||log.wantedPoint!=0.3F?1:0.4F;
		
		buttonY.update();
		overallAlpha.update();
		Updater.update(buttonList);
	}
	private GuiTextEditor getEditor(){
		return logActive?programLog:programSrc;
	}
}
