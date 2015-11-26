package com.magiology.client.gui.gui;

import static com.magiology.io.WorldData.WorkingProtocol.*;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.magiology.api.lang.ProgramDataBase;
import com.magiology.api.updateable.Updater;
import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.client.gui.container.CommandCenterContainer;
import com.magiology.client.gui.container.ContainerEmpty;
import com.magiology.client.gui.custom.OnOffGuiButton;
import com.magiology.client.gui.guiutil.gui.GuiJavaScriptEditor;
import com.magiology.client.gui.guiutil.gui.GuiTextEditor;
import com.magiology.client.gui.guiutil.gui.buttons.CleanButton;
import com.magiology.client.render.Textures;
import com.magiology.client.render.font.FontRendererMBase;
import com.magiology.core.Magiology;
import com.magiology.forgepowered.packets.packets.OpenProgramContainerInGui;
import com.magiology.io.WorldData;
import com.magiology.io.WorldData.FileContent;
import com.magiology.mcobjects.items.ProgramContainer;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.tessellatorscripts.Drawer;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.m_extension.GuiContainerM;
import com.magiology.util.utilobjects.vectors.AdvancedPhysicsFloat;
import com.magiology.util.utilobjects.vectors.Vec2i;

public class GuiProgramContainerEditor extends GuiContainerM implements Updateable{
	
	private static WorldData<String,String> settingsData=new WorldData<String,String>("jsEditorSettings","dat","jsEdit",FROM_CLIENT);
	public static void loadClass(){settingsData.getFileContent("");}
	
	private ItemStack stack;
	private int slotId;
	private BlockPos tilePos;
	private GuiTextEditor 
		programSrc=new GuiJavaScriptEditor(new Vec2i(0, 50),new Vec2i(300, 600)),
		programLog=new GuiTextEditor(new Vec2i(0, 50),new Vec2i(300, 600));
	private boolean firstInit=true,logActive=false,settingsActive=false;
	private AdvancedPhysicsFloat 
		compile=new AdvancedPhysicsFloat(0.3F, 0.3F, true),
		settings=new AdvancedPhysicsFloat(0.3F, 0.3F, true),
		log=new AdvancedPhysicsFloat(0.3F, 0.3F, true),
		buttonY=new AdvancedPhysicsFloat(0, 0.3F, true),
		overallAlpha=new AdvancedPhysicsFloat(0, 0.2F, true);
	private OnOffGuiButton saveOnExit,printCompileError,useColors,recommendCode;
	private Integer txtId=null;
	GuiTextField compileAfter;
	
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
		
		getEditor().size=new Vec2i(width-20, height-20);
		getEditor().pos=new Vec2i(10, 10);
		((GuiJavaScriptEditor)programSrc).colors=useColors.isOn();
		getEditor().render(mouseX,mouseY);
		FontRendererMBase fr=Get.Render.Font.FRB();
		if(settingsActive){
			GL11.glPushMatrix();
			Vec2i middle=getEditor().pos.add(getEditor().size.x/2,getEditor().size.y/2);
			GL11.glTranslatef(middle.x, middle.y, 0);
			GL11U.texture(false);
			
			ColorF.convert(new Color(16,16,32,220)).mul(1.5).bind();
			Drawer.startDrawingQuads();
			Drawer.addVertex(-60, -50, 0);
			Drawer.addVertex(-60,  50, 0);
			Drawer.addVertex( 60,  50, 0);
			Drawer.addVertex( 60, -50, 0);
			Drawer.draw();
			
			ColorF.convert(new Color(16,16,32,220)).mul(3).bind();
			Drawer.startDrawingLines();
			Drawer.addVertex(-60, -50, 0);
			Drawer.addVertex(-60,  50, 0);
			
			Drawer.addVertex(-60,  50, 0);
			Drawer.addVertex( 60,  50, 0);
			
			Drawer.addVertex( 60,  50, 0);
			Drawer.addVertex( 60, -50, 0);
			
			Drawer.addVertex( 60, -50, 0);
			Drawer.addVertex(-60, -50, 0);
			Drawer.draw();
			
			GL11U.texture(true);
			int color=compileAfter.getText().length()==1&&compileAfter.getText().equals("0")?Color.YELLOW.hashCode():Color.WHITE.hashCode();
			try{Float.parseFloat(compileAfter.getText());}catch(Exception e){color=Color.RED.hashCode();}
			
			fr.drawString("Save on exit:", -57, -46, new ColorF(1-saveOnExit.pos.getPoint(),saveOnExit.pos.getPoint(),0,1).mix(ColorF.WHITE).toCode());
			fr.drawString("Compile after:", -57, -46+14, color);fr.drawString("s", 14+fr.getStringWidth(compileAfter.getText()), -46+14, color);
			fr.drawString("Print error:", -57, -46+26, new ColorF(1-printCompileError.pos.getPoint(),printCompileError.pos.getPoint(),0,1).mix(ColorF.WHITE).toCode());
			fr.drawString("Use colors:", -57, -46+40, new ColorF(1-useColors.pos.getPoint(),useColors.pos.getPoint(),0,1).mix(ColorF.WHITE).toCode());
			fr.drawString("Recommend code:", -57, -46+54, new ColorF(1-recommendCode.pos.getPoint(),recommendCode.pos.getPoint(),0,1).mix(ColorF.WHITE).toCode());
			fr.drawString("Command name", -57, -46+68, ColorF.WHITE.toCode());
			GL11.glPopMatrix();
		}
		
		Vec2i pos=getEditor().pos.add(0,getEditor().size.y-16-(int)(buttonY.getPoint()*10));
		
		compile .wantedPoint=new Rectangle(pos.x+ 0,pos.y,16,16).contains(mouseX,mouseY)?1:0.3F;
		settings.wantedPoint=new Rectangle(pos.x+16,pos.y,16,16).contains(mouseX,mouseY)?1:0.3F;
		log     .wantedPoint=new Rectangle(pos.x+32,pos.y,16,16).contains(mouseX,mouseY)?1:0.3F;
		
		GL11U.setUpOpaqueRendering(1);
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		GL11U.glScale(0.5F);
		
		ColorF background=ColorF.convert(new Color(16,16,32,220));
		background.a*=overallAlpha.getPoint();
		GL11U.glColor(background);
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
		if(!message.isEmpty()){
			background.a=color.a*overallAlpha.getPoint();
			GL11U.glColor(background);
			GL11U.blendFunc(1);
			GL11U.glScale(2);
			GL11U.texture(false);
			drawTexturedModalRect(0, -9, 0, 0, fr.getStringWidth(message)+1, 10);
			GL11U.texture(true);
			fr.drawString(message, 0, -8, color.toCode());
			GL11U.glScale(0.5F);
		}
		
		if(txtId==null){
			GL11U.captureTexture();
			txtId=GL11U.textureId;
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
		GlStateManager.bindTexture(txtId);
		
		GL11.glPopMatrix();
		GL11U.endOpaqueRendering();
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
	@Override
	public void initGui(){
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		if(firstInit)programSrc.setText(ProgramDataBase.code_get(ProgramContainer.getId(stack)));
		
		int top=width/2-60,left=height/2-50;
		
		saveOnExit=new OnOffGuiButton(0, top+98-3, left+3, 22, 11,           new ColorF(0.2, 0.2, 0.8, 0.8));
		printCompileError=new OnOffGuiButton(1, top+98-3, left+3+26, 22, 11, new ColorF(0.2, 0.8, 0.2, 0.8));
		useColors=new OnOffGuiButton(2, top+98-3, left+3+40, 22, 11,         new ColorF(0.2, 0.8, 0.8, 0.8));
		recommendCode=new OnOffGuiButton(3, top+98-3, left+3+54, 22, 11,     new ColorF(0.8, 0.2, 0.2, 0.8));
		try{saveOnExit.forceIsOn(Boolean.parseBoolean(settingsData.getFileContent("saveOnExit").text.toString().replaceAll("\n", "")));}catch(Exception e){}
		try{printCompileError.forceIsOn(Boolean.parseBoolean(settingsData.getFileContent("printCompileError").text.toString().replaceAll("\n", "")));}catch(Exception e){}
		try{useColors.forceIsOn(Boolean.parseBoolean(settingsData.getFileContent("useColors").text.toString().replaceAll("\n", "")));}catch(Exception e){}
		try{recommendCode.forceIsOn(Boolean.parseBoolean(settingsData.getFileContent("recommendCode").text.toString().replaceAll("\n", "")));}catch(Exception e){}
		
		buttonList.add(saveOnExit);
		saveOnExit.visible=settingsActive;
		buttonList.add(printCompileError);
		printCompileError.visible=settingsActive;
		buttonList.add(useColors);
		useColors.visible=settingsActive;
		buttonList.add(recommendCode);
		recommendCode.visible=settingsActive;
		buttonList.add(new CleanButton(4, top-3+95, left+69,26,12, "Save", new ColorF(0.6, 0.6, 0, 0.8)));
		
		compileAfter=new GuiTextField(0,fontRendererObj, top+73, left+18, 40, 11);
		compileAfter.setEnableBackgroundDrawing(false);
		compileAfter.setMaxStringLength(7);
		textFieldList.add(compileAfter);
		compileAfter.setVisible(settingsActive);
		FileContent<String> i=settingsData.getFileContent("compileAfter");
		String compAfter=i==null?"":i.text.toString().replaceAll("\n", "");
		compileAfter.setText(compAfter.isEmpty()?"0.5":compAfter);
		
		GuiTextField name=new GuiTextField(1,fontRendererObj, top+3, left+72+11, 120-6, 12);
		name.setText(ProgramContainer.getName(stack));
		textFieldList.add(name);
		((GuiButton)buttonList.get(4)).visible=settingsActive;
		textFieldList.get(1).setVisible(settingsActive);
		
		firstInit=false;
	}
	
	@Override
	protected void actionPerformed(GuiButton b){
		if(b.id==saveOnExit.id)settingsData.addFile("saveOnExit", saveOnExit.isOn()+"");
		else if(b.id==printCompileError.id)settingsData.addFile("printCompileError", printCompileError.isOn()+"");
		else if(b.id==useColors.id)settingsData.addFile("useColors", useColors.isOn()+"");
		else if(b.id==recommendCode.id)settingsData.addFile("recommendCode", useColors.isOn()+"");
		else if(b.id==4){
			UtilM.sendMessage(new OpenProgramContainerInGui.ExitGui(slotId, programSrc.getText(), textFieldList.get(1).getText(),tilePos));
			settingsData.saveFromWorld(UtilM.getTheWorld());
		}
	}
	@Override
	public void onGuiClosed(){
		Keyboard.enableRepeatEvents(false);
		if(saveOnExit.isOn())UtilM.sendMessage(new OpenProgramContainerInGui.ExitGui(slotId, programSrc.getText(), textFieldList.get(1).getText(),tilePos));
		super.onGuiClosed();
	}
	@Override
	protected void keyTyped(char typedChar, int keyCode){
		String compTxt=compileAfter.getText();
		try{
			boolean active=getEditor().active;
			if(settingsActive?true:!active)super.keyTyped(typedChar, keyCode);
			if(!settingsActive?true:!superClicked)if(!getEditor().keyTyped(keyCode, typedChar));
			if(active&&!getEditor().active)Magiology.ROBOT.clickKeyKeyboard(KeyEvent.VK_ESCAPE);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(compileAfter.getVisible()&&!compTxt.equals(compileAfter.getText())){
			compileAfter.setText(compileAfter.getText().replaceAll(" ", ""));
			try{
				float time=Float.parseFloat(compileAfter.getText());
				compileAfter.setTextColor(time==0?Color.YELLOW.hashCode():Color.WHITE.hashCode());
				settingsData.addFile("compileAfter", compileAfter.getText());
				settingsData.saveFromWorld(UtilM.getTheWorld());
			}catch(Exception e){
				compileAfter.setTextColor(Color.RED.hashCode());
			}
			
		}
	}
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		Vec2i middle=getEditor().pos.add(getEditor().size.x/2,getEditor().size.y/2);
		if(compile.wantedPoint==1){
			
		}else if(settings.wantedPoint==1){
			
		}else if(log.wantedPoint==1){
			
		}else if(settingsActive&&new Rectangle(middle.x-60, middle.y-50, 120, 100).contains(mouseX, mouseY)){
		
		}else 
		try{
			getEditor().mouseClickMove(mouseX, mouseY);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton){
		boolean compileAfterFocus=compileAfter.isFocused();
		try{super.mouseClicked(mouseX, mouseY, mouseButton);}catch(Exception e){e.printStackTrace();}
		Vec2i middle=getEditor().pos.add(getEditor().size.x/2,getEditor().size.y/2);
		boolean selected=false;
		for(GuiTextField textField:textFieldList)if(textField.isFocused()){
			selected=true;
			break;
		}
		if(compile.wantedPoint==1){
			UtilM.sendMessage(new OpenProgramContainerInGui.ExitGui(slotId, programSrc.getText(), textFieldList.get(1).getText(),tilePos));
			UtilM.getMC().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			
		}else if(settings.wantedPoint==1){
			settingsActive=!settingsActive;
			saveOnExit.visible=settingsActive;
			printCompileError.visible=settingsActive;
			compileAfter.setVisible(settingsActive);
			useColors.visible=settingsActive;
			recommendCode.visible=settingsActive;
			((GuiButton)buttonList.get(4)).visible=settingsActive;
			textFieldList.get(1).setVisible(settingsActive);
			
			UtilM.getMC().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			
		}else if(log.wantedPoint==1){
			logActive=!logActive;
			
			List<CharSequence> logData=ProgramDataBase.log_get(ProgramContainer.getId(stack));
			List<StringBuilder> logDataNew=new ArrayList<StringBuilder>();
			for(int i=0;i<logData.size();i++){
				String line=logData.get(i).toString();
				if(line.startsWith(ProgramDataBase.err))logDataNew.add(new StringBuilder(line.substring(ProgramDataBase.err.length())));
				else logDataNew.add(new StringBuilder(line));
			}
			programLog.setText(UtilM.join("\n",logDataNew.toArray()));
			programLog.sliderY=1;
			UtilM.getMC().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			
		}else if(selected){
			getEditor().active=false;
		}else if(settingsActive&&new Rectangle(middle.x-60, middle.y-50, 120, 100).contains(mouseX, mouseY)){
			
		}else try{
			getEditor().mouseClicked(mouseX, mouseY, mouseButton);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(compileAfter.getVisible()&&compileAfterFocus&&!compileAfter.isFocused()){
			compileAfter.setText(compileAfter.getText().replaceAll(" ", ""));
			if(compileAfter.getText().isEmpty())compileAfter.setText("0.5");
			try{
				float time=Float.parseFloat(compileAfter.getText());
				compileAfter.setTextColor(time==0?Color.YELLOW.hashCode():Color.WHITE.hashCode());
				compileAfter.setText((""+time).endsWith(".0")?(""+time).substring(0, (""+time).length()-2):""+time);
				settingsData.addFile("compileAfter", compileAfter.getText());
				settingsData.saveFromWorld(UtilM.getTheWorld());
			}catch(Exception e){
				compileAfter.setTextColor(Color.RED.hashCode());
			}
			
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
