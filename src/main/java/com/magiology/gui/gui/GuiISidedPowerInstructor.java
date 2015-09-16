package com.magiology.gui.gui;

import java.awt.Rectangle;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import org.lwjgl.opengl.GL11;

import com.magiology.api.power.ISidedPower;
import com.magiology.forgepowered.event.client.RenderLoopEvents;
import com.magiology.forgepowered.packets.packets.generic.GenericServerIntPacket;
import com.magiology.gui.GuiUpdater.Updateable;
import com.magiology.gui.container.ISidedPowerInstructorContainer;
import com.magiology.gui.guiutil.gui.buttons.ColoredGuiButton;
import com.magiology.gui.guiutil.gui.buttons.TexturedColoredButton;
import com.magiology.render.Textures;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.SimpleCounter;
import com.magiology.util.utilobjects.vectors.AdvancedPhysicsFloat;

public class GuiISidedPowerInstructor extends GuiContainer implements Updateable{
	
	
	int mouseX,mouseY,buttonId=0;
	AdvancedPhysicsFloat xRotation,yRotation,zRotation;
	
	private final TileEntity tile;
	TileEntitySpecialRenderer renderer;
	
	private ComplexCubeModel cube;
	
	public GuiISidedPowerInstructor(EntityPlayer player,TileEntity tile){
		super(new ISidedPowerInstructorContainer(player, tile));
		this.tile=tile;
		this.xSize=176;
		this.ySize=166;
		renderer=TileEntityRendererDispatcher.instance.getSpecialRenderer(tile);
		
		xRotation=new AdvancedPhysicsFloat(Util.CRandI(100)-player.rotationPitch,2);
		yRotation=new AdvancedPhysicsFloat(Util.CRandI(100)-player.rotationYaw,2);
		zRotation=new AdvancedPhysicsFloat(Util.CRandI(100),2);
		xRotation.friction=yRotation.friction=zRotation.friction=0.9F;
		xRotation.wantedPoint=-player.rotationPitch;
		yRotation.wantedPoint=-player.rotationYaw;
		zRotation.wantedPoint=0;
		
		Block aa=U.getBlock(player.worldObj,tile.getPos());
		cube=new ComplexCubeModel((float)aa.getBlockBoundsMinX(), (float)aa.getBlockBoundsMinY(), (float)aa.getBlockBoundsMinZ(), (float)aa.getBlockBoundsMaxX(), (float)aa.getBlockBoundsMaxY(), (float)aa.getBlockBoundsMaxZ());
	}
	@Override
	public void drawGuiContainerForegroundLayer(int x,int y){
		
	}
	
	
	int mouseStartX,mouseStartY;
	@Override
	public void update(){
		
		double xDifference=xRotation.difference()+Math.abs(xRotation.speed/4),
			   yDifference=yRotation.difference()+Math.abs(yRotation.speed/4),
			   zDifference=zRotation.difference()+Math.abs(zRotation.speed/4);
		
		xRotation.friction=yRotation.friction=zRotation.friction=0.9F;
		
		if(xDifference<5)xRotation.friction=0.9F;
		if(xDifference<3)xRotation.friction=0.8F;
		if(xDifference<2)xRotation.friction=0.7F;
		if(xDifference<1)xRotation.friction=0.1F;

		if(yDifference<5)yRotation.friction=0.9F;
		if(yDifference<3)yRotation.friction=0.8F;
		if(yDifference<2)yRotation.friction=0.7F;
		if(yDifference<1)yRotation.friction=0.1F;
		
		if(zDifference<5)zRotation.friction=0.9F;
		if(zDifference<3)zRotation.friction=0.8F;
		if(zDifference<2)zRotation.friction=0.7F;
		if(zDifference<1)zRotation.friction=0.1F;
		
		xRotation.update();
		yRotation.update();
		zRotation.update();
//		((ColoredGuiButton)buttonList.get(7)).update();
//		((ColoredGuiButton)buttonList.get(8)).update();
		for(Object b:buttonList){
			if(b instanceof TexturedColoredButton)((TexturedColoredButton)b).update();
//			if(b instanceof ColoredGuiButton)((ColoredGuiButton)b).update();
		}
		int side=convert(buttonId);
		boolean 
		allowedReceive=((ISidedPower)tile).getAllowedReceaver(side),
		allowedSend   =((ISidedPower)tile).getAllowedSender  (side),
		receive=((ISidedPower)tile).getIn(side),
		send   =((ISidedPower)tile).getOut   (side);
		
		((GuiButton)buttonList.get(7)).enabled=allowedSend;
		((GuiButton)buttonList.get(8)).enabled=allowedReceive;
		((ColoredGuiButton)buttonList.get(7)).wantedR=send?0.1F:1;
		((ColoredGuiButton)buttonList.get(7)).wantedG=send?1:0.1F;
		((ColoredGuiButton)buttonList.get(7)).wantedB=0.1F;
		((ColoredGuiButton)buttonList.get(8)).wantedR=receive?0.1F:1;
		((ColoredGuiButton)buttonList.get(8)).wantedG=receive?1:0.1F;
		((ColoredGuiButton)buttonList.get(8)).wantedB=0.1F;
	}
	@Override
	protected void mouseClicked(int x, int y, int buttonClicked)throws IOException{
		super.mouseClicked(x, y, buttonClicked);
		mouseStartX=x;
		mouseStartY=y;
	}
	@Override
	protected void mouseClickMove(int x, int y, int lastButtonClicked, long totalMoved){
		super.mouseClickMove(x, y, lastButtonClicked, totalMoved);
		if(new Rectangle(guiLeft+14,guiTop+12,46,46).contains(x, y)){
			yRotation.wantedPoint-=(x-mouseStartX)*2;
			if(x<guiLeft+20)zRotation.wantedPoint-=(y-mouseStartY)*2;
			else if(x>guiLeft+52)zRotation.wantedPoint-=(y-mouseStartY)*2;
			else xRotation.wantedPoint-=(y-mouseStartY)*2;
		}
    }
	
	@Override
	protected void actionPerformed(GuiButton b){
		int id=b.id;
		if(id<6){
			buttonId=id;
		}
		int side=convert(buttonId);
		if(id==6){
			Util.sendMessage(new GenericServerIntPacket(7, side));
		}else if(id==7){
			Util.sendMessage(new GenericServerIntPacket(8, side));
		}else if(id==8){
			Util.sendMessage(new GenericServerIntPacket(9, side));
		}
		if(b instanceof ColoredGuiButton)((ColoredGuiButton)b).blink(1);
	}
	private int convert(int id){
		switch (id){
		case 4:return 5;
		case 3:return 4;
		case 5:return 3;
		case 0:return 1;
		case 1:return 0;
		case 2:return 2;
		}
		return -1;
	}
	@Override
	public void initGui(){
		super.initGui();
		SimpleCounter sc=new SimpleCounter();
		for(int a=0;a<6;a++){
			buttonList.add(new TexturedColoredButton(a,100+guiLeft,10+guiTop+a*22, 40, 20, EnumFacing.getFront(a).toString()));
			sc.add();
		}
		buttonList.add(new TexturedColoredButton(sc.getAndAdd(),15+guiLeft, 80+guiTop, 72, 20, "Cricle"));
		buttonList.add(new TexturedColoredButton(sc.getAndAdd(),10+guiLeft,102+guiTop, 40, 20, "Output"));
		buttonList.add(new TexturedColoredButton(sc.getAndAdd(),52+guiLeft,102+guiTop, 40, 20, "Input"));
		actionPerformed((GuiButton)buttonList.get(0));
	}
	@Override
	public void drawScreen(int x, int y, float partialTicks){
		float xRot=xRotation.getPoint()+180, 
			  yRot=yRotation.getPoint(),
			  zRot=zRotation.getPoint();
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		super.drawScreen(x, y, partialTicks);
		RenderHelper.enableGUIStandardItemLighting();
		
		
		try{
			if(tile==null)return;
			mouseX=x;
			mouseY=y;
			
			
			//TODO: BlockRendererDispatcher#renderBlock may be able to do what you want. Look at RenderChunk#rebuildChunk to see how it's used.
//			Helper.getRenderBlocks().blockAccess=tile.getWorld();
			
			
			
			GL11.glPushMatrix();
			GL11.glTranslated(guiLeft+37, guiTop+35, 120);
			GL11U.scaled(35);
			GL11U.rotateXYZ(xRot,yRot,zRot);
			GL11.glScalef((-1), 1, 1);
			if(renderer!=null){
		        GL11.glCullFace(GL11.GL_FRONT);
				renderer.renderTileEntityAt(tile, -0.5, -0.5, -0.5, RenderLoopEvents.partialTicks,0);
				GL11.glCullFace(GL11.GL_BACK);
				renderer.renderTileEntityAt(tile, -0.5, -0.5, -0.5, RenderLoopEvents.partialTicks,0);
			}
			Block block=U.getBlock(tile.getWorld(), tile.getPos());
			if(block!=null){
				block.getRenderType();
				GL11.glPushMatrix();
				try{
					TessUtil.bindTexture(TextureMap.locationBlocksTexture);
					Get.Render.WR().startDrawingQuads();
					//TODO: BlockRendererDispatcher#renderBlock may be able to do what you want. Look at RenderChunk#rebuildChunk to see how it's used.
//					Helper.getRenderBlocks().renderBlockByRenderType(block, tile.getPos());
					GL11.glTranslated(-tile.getPos().getX()-0.5, -tile.getPos().getY()-0.5, -tile.getPos().getZ()-0.5);
					TessUtil.draw();
				}catch(Exception e){e.printStackTrace();}
				GL11.glPopMatrix();
			}
			int id=-1;
			for(int a=0;a<cube.willSideRender.length;a++){
				GuiButton b=(GuiButton)buttonList.get(a);
				if(x>=b.xPosition &&y>=b.yPosition&&x<b.xPosition+b.width&&y<b.yPosition+b.height){
					id=a;
					continue;
				}
			}
			if(id!=-1){
				switch (id){
				case 0:id=3;break;
				case 1:id=2;break;
				case 2:id=4;break;
				case 3:id=5;break;
				case 4:id=1;break;
				case 5:id=0;break;
				}
				for(int a=0;a<cube.willSideRender.length;a++)cube.willSideRender[a]=a==id;
				
				
				
				GL11.glPushMatrix();
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11U.SetUpOpaqueRendering(1);
				GL11U.scaled(1.001);
				GL11.glTranslated(-0.5, -0.5, -0.5);
				double 
				r=Util.fluctuator(9, 0)*0.4,
				g=0.5-Util.fluctuator(17, 0)*0.3,
				b=1-Util.fluctuator(27, 0)*0.2;
				GL11.glColor4d(r,g,b, 0.4);
				GL11.glDisable(GL11.GL_LIGHTING);
				cube.draw();
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glColor4d(r,g,b, 0.2);
				cube.draw();
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11U.EndOpaqueRendering();
				GL11.glEnable(GL11.GL_CULL_FACE);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glPopMatrix();
			}
			GL11.glPopMatrix();
		}catch(Exception e){e.printStackTrace();}
		GL11.glDisable(GL11.GL_LIGHTING);
		
		TessUtil.bindTexture(Textures.ISidedIns);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		GL11.glPushMatrix();
		GL11.glTranslated(guiLeft+82, guiTop+60, 120);
		GL11U.SetUpOpaqueRendering(1);
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
		
		GL11.glColor4d(0.65F+Util.fluctuator(41, 0)*0.1,0.65+Util.fluctuator(25, 0)*0.05,0.65+Util.fluctuator(73, 0)*0.15,1);
		GL11U.scaled(12.9);
		GL11U.rotateXYZ(Util.fluctuator(164, 0)*180+xRot/4, Util.fluctuator(84, 0)*60+yRot/4, Util.fluctuator(508, 0)*360+zRot/4);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		TessUtil.drawBall();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		
		GL11.glPopMatrix();
		
		
		GL11.glPushMatrix();
		GL11U.rotateXYZ(xRotation.wantedPoint+180, yRotation.wantedPoint, zRotation.wantedPoint);
		GL11.glColor4f(1, 0, 0, 1F);
		GL11.glLineWidth(2.5F);
		Get.Render.WR().startDrawing(GL11.GL_LINES);
		Get.Render.WR().addVertex(0,0,0);
		Get.Render.WR().addVertex(13,0,0);
		TessUtil.draw();
		GL11.glColor4f(1, 0, 0, 0.3F);
		GL11.glLineWidth(5F);
		Get.Render.WR().startDrawing(GL11.GL_LINES);
		Get.Render.WR().addVertex(0,0,0);
		Get.Render.WR().addVertex(13,0,0);
		TessUtil.draw();
		GL11.glPopMatrix();
		
		
		GL11.glPushMatrix();
		GL11U.rotateXYZ(xRot,yRot,zRot);
		GL11.glColor4f(0, 1, 0, 1F);
		GL11.glLineWidth(2.5F);
		Get.Render.WR().startDrawing(GL11.GL_LINES);
		Get.Render.WR().addVertex(0,0,0);
		Get.Render.WR().addVertex(13,0,0);
		TessUtil.draw();
		GL11.glColor4f(0, 1, 0, 0.3F);
		GL11.glLineWidth(5F);
		Get.Render.WR().startDrawing(GL11.GL_LINES);
		Get.Render.WR().addVertex(0,0,0);
		Get.Render.WR().addVertex(13,0,0);
		TessUtil.draw();
		GL11.glPopMatrix();
		
		
		GL11U.EndOpaqueRendering();
		GL11.glPopMatrix();

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPushMatrix();
		GL11.glTranslated(guiLeft+82, guiTop+35, 120);
		GL11U.scaled(11);
		GL11U.rotateXYZ(xRot,yRot,zRot);
		GL11.glTranslated(0.55,-0.55,-0.55);
		
		
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glScalef((-1), 1, 1);
		GL11.glColor4f(0.2F, 0.1F, 1, 1);
		TessUtil.drawArrow();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslated(guiLeft+152, guiTop+19+buttonId*22, 120);
		GL11U.scaled(12);
		GL11U.rotateXYZ(0, 30, 90);
		GL11U.rotateXYZ(0, Util.fluctuator(1600, 0)*3600,0);
		GL11.glTranslated(0.375,-0.375,-0.375);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glScalef(-0.75F, 1, 0.75F);
		GL11.glColor4f(0.5F, 0.4F, 1, 1);
		TessUtil.drawArrow();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		
		GL11.glPopMatrix();
		
		GL11.glDisable(GL11.GL_LIGHTING);
		
		for(int k=0;k<buttonList.size();++k){((GuiButton)this.buttonList.get(k)).drawButton(mc, x, y);}

		GL11.glPopMatrix();
    }
	@Override
	protected void drawGuiContainerBackgroundLayer(float v1, int x, int y){
		GL11.glDisable(GL11.GL_LIGHTING);
		TessUtil.bindTexture(Textures.ISidedIns);
		GL11U.SetUpOpaqueRendering(2);
		GL11.glColor4f(1, 1, 1, 0.9F);
		GL11.glTranslatef(0, 0, 0);
		this.drawTexturedModalRect(guiLeft+14, guiTop+12, xSize, 0, 46, 46);
		GL11U.EndOpaqueRendering();
		if(isShiftKeyDown()){
			GL11.glPushMatrix();
			GL11.glTranslated(guiLeft, guiTop+12, 0);
			GL11.glColor4f(1, 0.6F, 0.6F, 1F);
			GL11.glLineWidth(1F);
			Get.Render.WR().startDrawing(GL11.GL_LINES);
			Get.Render.WR().addVertex(20,0,0);
			Get.Render.WR().addVertex(20,46,0);
			Get.Render.WR().addVertex(52,0,0);
			Get.Render.WR().addVertex(52,46,0);
			TessUtil.draw();
			GL11.glPopMatrix();
		}
	}
}
