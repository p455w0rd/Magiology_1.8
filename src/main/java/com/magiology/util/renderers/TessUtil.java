package com.magiology.util.renderers;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;

import java.lang.reflect.*;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

import com.magiology.client.render.*;
import com.magiology.client.render.font.*;
import com.magiology.core.*;
import com.magiology.handlers.obj.handler.revived.yayformc1_8.*;
import com.magiology.mcobjects.effect.*;
import com.magiology.util.renderers.tessellatorscripts.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilclasses.math.*;
import com.magiology.util.utilobjects.vectors.*;


/**
 * Tessellator Helper
 * @author LapisSea
 */
public class TessUtil{
	
	private static VertixBuffer buf=new VertixBuffer();
	private static FontRendererMBase fontRendererMBase=new FontRendererMBase(new ResourceLocation("textures/font/ascii.png"));
	public static VertixBuffer getVB(){return buf;}
	public static WorldRenderer getWR(){return Tessellator.getInstance().getWorldRenderer();}
	public static RenderManager getRM(){return UtilM.getMC().getRenderManager();}
	public static Tessellator getT(){return Tessellator.getInstance();}
	
	
	public static void bindTexture(ResourceLocation texture){U.getMC().getTextureManager().bindTexture(texture);}
	public static void drawTri(double[] X, double[] Y,double[] Z, double[] U, double[] V){
		int hi=max(max(max(max(X.length,Y.length),Z.length),U.length),V.length);
		if(hi%3!=0||X.length<hi||Y.length<hi||Z.length<hi||U.length<hi||V.length<hi)return;
		buf.cleanUp();
		for (int i = 0; i < X.length; i++)buf.addVertexWithUV(X[i], Y[i], Z[i], U[i], V[i]);
		buf.draw();
	}
	public static void drawQuad(double[] X, double[] Y,double[] Z, double[] U, double[] V){
		int hi=max(max(max(max(X.length,Y.length),Z.length),U.length),V.length);
		if(hi%4!=0||X.length<hi||Y.length<hi||Z.length<hi||U.length<hi||V.length<hi)return;
		buf.cleanUp();
		for (int i = 0; i < X.length; i++)buf.addVertexWithUV(X[i], Y[i], Z[i], U[i], V[i]);
		buf.draw();
	}
	public static void drawPolygon(double[] X, double[] Y,double[] Z, double[] U, double[] V){
		int hi=max(max(max(max(X.length,Y.length),Z.length),U.length),V.length);
		if(X.length<hi||Y.length<hi||Z.length<hi||U.length<hi||V.length<hi)return;
		buf.cleanUp();
		for (int i = 0; i < X.length; i++)buf.addVertexWithUV(X[i], Y[i], Z[i], U[i], V[i]);
		buf.draw();
	}
	public static void drawBlurredCube(int x,int y,int z,double minX,double minY,double minZ,double maxX,double maxy,double maxZ,int blurQuality,double resolution,double r,double g,double b,double alpha){
		drawBlurredCube(x,y,z, new AxisAlignedBB(minX, minY, minZ, maxX, maxy, maxZ), blurQuality, resolution,r,g,b, alpha);
	}
	public static void drawBlurredCube(int x,int y,int z,AxisAlignedBB cube,int blurQuality,double resolution,double r,double g,double b,double alpha){
		if(blurQuality<1||cube==null)return;
		alpha=alpha/blurQuality;
		GL11.glPushMatrix();
		
		GL11.glColor4d(r, g, b, alpha);
		for(int size=0;size<blurQuality;size++){
			GL11.glTranslated((cube.maxX-cube.minX)/2+cube.minX, (cube.maxY-cube.minY)/2+cube.minY, (cube.maxZ-cube.minZ)/2+cube.minZ);
			GL11.glScaled(-resolution+1, -resolution+1, -resolution+1);
			GL11.glTranslated(-((cube.maxX-cube.minX)/2+cube.minX), -((cube.maxY-cube.minY)/2+cube.minY), -((cube.maxZ-cube.minZ)/2+cube.minZ));
			drawCube(cube);
		}
		GL11.glColor4d(1,1,1,1);
		GL11.glPopMatrix();
	}
	public static void drawCube(AxisAlignedBB a){drawCube(a.minX,a.minY,a.minZ,a.maxX,a.maxY,a.maxZ);}
	public static void drawCube(double minX,double minY,double minZ,double maxX,double maxy,double maxZ){
		buf.cleanUp();
		buf.addVertexWithUV(minX, minY, maxZ,0,0);
		buf.addVertexWithUV(minX, minY, minZ,0,0);
		buf.addVertexWithUV(maxX, minY, minZ,0,0);
		buf.addVertexWithUV(maxX, minY, maxZ,0,0);
		buf.addVertexWithUV(maxX, maxy, maxZ,0,0);
		buf.addVertexWithUV(maxX, maxy, minZ,0,0);
		buf.addVertexWithUV(minX, maxy, minZ,0,0);
		buf.addVertexWithUV(minX, maxy, maxZ,0,0);
		buf.addVertexWithUV(maxX, maxy, minZ,0,0);
		buf.addVertexWithUV(maxX, minY, minZ,0,0);
		buf.addVertexWithUV(minX, minY, minZ,0,0);
		buf.addVertexWithUV(minX, maxy, minZ,0,0);
		buf.addVertexWithUV(minX, maxy, maxZ,0,0);
		buf.addVertexWithUV(minX, minY, maxZ,0,0);
		buf.addVertexWithUV(maxX, minY, maxZ,0,0);
		buf.addVertexWithUV(maxX, maxy, maxZ,0,0);
		buf.addVertexWithUV(minX, maxy, minZ,0,0);
		buf.addVertexWithUV(minX, minY, minZ,0,0);
		buf.addVertexWithUV(minX, minY, maxZ,0,0);
		buf.addVertexWithUV(minX, maxy, maxZ,0,0);
		buf.addVertexWithUV(maxX, maxy, maxZ,0,0);
		buf.addVertexWithUV(maxX, minY, maxZ,0,0);
		buf.addVertexWithUV(maxX, minY, minZ,0,0);
		buf.addVertexWithUV(maxX, maxy, minZ,0,0);
		buf.draw();
	}
	public static void drawFullCircleRes45(double r,double g,double b,double alpha,double scale){drawCircleRes45(r, g, b, alpha, scale, 0, 359);}
	public static void drawFullCircleRes90(double r,double g,double b,double alpha,double scale){drawCircleRes90(r, g, b, alpha, scale, 0, 360);}
	public static void drawCircleRes45(double r,double g,double b,double alpha,double scale,int startAngle,int endAngle){
		endAngle++;
		if(startAngle%45!=0)startAngle=startAngle%45;
		if(endAngle%45!=0)endAngle=endAngle%45;
		startAngle*=8;
		endAngle*=8;
		double[][] xy=new double[2][Math.abs(endAngle-startAngle)];
		for(int xory=0;xory<xy.length;xory++){
			for(int index=0;index<xy[xory].length;index++){
				xy[xory][index]=xory==0?CricleUtil.sin(index+startAngle):CricleUtil.cos(index+startAngle);
				xy[xory][index]*=scale;
			}
		}
		GL11.glColor4d(r, g, b, alpha);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		{
			VertixBuffer r1=TessUtil.getVB();
			r1.cleanUp();
			int i=0;
			while(i<xy[0].length-1){
				r1.addVertexWithUV(0, 0, -scale,0,0);
				r1.addVertexWithUV(0, 0, -scale,0,0);
				r1.addVertexWithUV(xy[0][i], xy[1][i], 0,0,0);
				i++;
				r1.addVertexWithUV(xy[0][i], xy[1][i], 0,0,0);
			}
			r1.draw();
		}
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	public static void drawCircleRes90(double r,double g,double b,double alpha,double scale,int startAngle,int endAngle){
		if(startAngle%90!=0)startAngle=startAngle%90;
		if(endAngle%90!=0)endAngle=endAngle%90;
		startAngle*=4;
		endAngle*=4;
		double[][] xy=new double[2][Math.abs(endAngle-startAngle)];
		for(int xory=0;xory<xy.length;xory++){
			for(int index=0;index<xy[xory].length;index++){
				xy[xory][index]=xory==0?CricleUtil.sin(index+startAngle):CricleUtil.cos(index+startAngle);
				xy[xory][index]*=scale;
			}
		}
		GL11.glColor4d(r, g, b, alpha);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		{
			VertixBuffer r1=TessUtil.getVB();
			r1.cleanUp();
			int i=0;
			while(i<xy[0].length-1){
				r1.addVertexWithUV(0, 0, 0,0,0);
				r1.addVertexWithUV(0, 0, 0,0,0);
				r1.addVertexWithUV(xy[0][i], xy[1][i], 0,0,0);
				i++;
				r1.addVertexWithUV(xy[0][i], xy[1][i], 0,0,0);
			}
			r1.draw();
		}
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	public static void drawCircleRes180(double r,double g,double b,double alpha,double scale,int startAngle,int endAngle){
		if(startAngle%180!=0)startAngle=startAngle%180;
		if(endAngle%180!=0)endAngle=endAngle%180;
		startAngle*=2;
		endAngle*=2;
		double[][] xy=new double[2][Math.abs(endAngle-startAngle)];
		for(int xory=0;xory<xy.length;xory++){
			for(int index=0;index<xy[xory].length;index++){
				xy[xory][index]=xory==0?CricleUtil.sin(index+startAngle):CricleUtil.cos(index+startAngle);
				xy[xory][index]*=scale;
			}
		}
		GL11.glColor4d(r, g, b, alpha);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		Renderer.begin(GL11.GL_TRIANGLES);
		{
			int i=0;
			while(i<xy[0].length-1){
				Renderer.addPos(0, 0, 0).endVertex();
				Renderer.addPos(xy[0][i], xy[1][i], 0).endVertex();
				if(i<xy[0].length)i++;
				Renderer.addPos(xy[0][i], xy[1][i], 0).endVertex();
			}
		}
		TessUtil.draw();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	private static IModelCustom arrowModel;
	public static void drawArrow(){
		if(arrowModel==null)arrowModel=AdvancedModelLoader.loadModel(new ResourceLocation(MReference.MODID,"/models/arrow.obj"));
		else{ 
			GL11.glPushMatrix();
			GL11.glTranslatef(0.6F, -0.03F, 0.52F);
			GL11U.glRotate(0, 45, 0);
			arrowModel.renderAll();
			GL11.glPopMatrix();
		}
	}
	private static IModelCustom ballModel;
	public static void drawBall(){
		if(ballModel==null)ballModel=AdvancedModelLoader.loadModel(new ResourceLocation(MReference.MODID,"/models/ball.obj"));
		else{
			ballModel.renderAll();
		}
	}
	private static IModelCustom SV98;
	public static void drawSV98(){
		if(SV98==null)SV98=AdvancedModelLoader.loadModel(new ResourceLocation(MReference.MODID,"/models/SV98.obj"));
		else{
			GL11U.texture(false);
			SV98.renderAll();
			GL11U.texture(true);
		}
	}
	public static void drawPlayerIntoGUI(int x, int y, int scale, float mouseX, float mouseY, EntityLivingBase player,boolean... WillRotate){
		boolean willRotate=false;
		if(WillRotate.length!=0){
			if(WillRotate.length!=1)return;
			willRotate=WillRotate[0];
		}
		glPushMatrix();
        glEnable(GL_COLOR_MATERIAL);
        glTranslatef(x, y, 50.0F);
        glScalef((-scale), scale, scale);
        glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = player.renderYawOffset;
        float f3 = player.rotationYaw;
        float f4 = player.rotationPitch;
        float f5 = player.prevRotationYawHead;
        float f6 = player.rotationYawHead;
        glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        glRotatef(-((float)Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
        if(!willRotate){
            player.renderYawOffset = (float)Math.atan(mouseX / 40.0F) * 20.0F;
            player.rotationYaw = (float)Math.atan(mouseX / 40.0F) * 40.0F;
            player.rotationPitch = -((float)Math.atan(mouseY / 40.0F)) * 20.0F;
            player.rotationYawHead = player.rotationYaw;
            player.prevRotationYawHead = player.rotationYaw;
        }
        glTranslatef(0.0F, (float)player.getYOffset(), 0.0F);
        getRM().playerViewY = 180.0F;
        getRM().renderEntityWithPosYaw(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        player.renderYawOffset = f2;
        player.rotationYaw = f3;
        player.rotationPitch = f4;
        player.prevRotationYawHead = f5;
        player.rotationYawHead = f6;
        RenderHelper.disableStandardItemLighting();
        glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        glDisable(GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        glPopMatrix();
    }
	static final ResourceLocation inventoryPict = new ResourceLocation("textures/gui/container/inventory.png");
	public static void drawSlotLightMapWcustomSizes(GuiContainer gui,int xPos,int yPos,int xSize,int ySize,boolean useDynamicShadow,boolean invertDepth){
		if(xSize<2||ySize<2)return;
		bindTexture(inventoryPict);
		GL11U.setUpOpaqueRendering(1);
//		int var2=Math.min(xSize, ySize),var1=(var2-var2%2)/2;
		int var1=1;
		//x
		GL11.glPushMatrix();
		for(int xProgress=0;xProgress<xSize;xProgress++){
			gui.drawTexturedModalRect(xPos, yPos, 10, invertDepth?119-var1:101, 1, var1);
			GL11.glTranslatef(1, 0, 0);
		}
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		for(int xProgress=0;xProgress<xSize;xProgress++){
			gui.drawTexturedModalRect(xPos, yPos+ySize-var1, 10, invertDepth?7:119-var1, 1, var1);
			GL11.glTranslatef(1, 0, 0);
		}
		GL11.glPopMatrix();
		//y
		GL11.glPushMatrix();
		for(int yProgress=0;yProgress<ySize-var1;yProgress++){
			if(yProgress==0)GL11.glTranslatef(0, 1, 0);
			gui.drawTexturedModalRect(xPos, yPos, invertDepth?25-var1:7, 102, var1, 1);
			GL11.glTranslatef(0, 1, 0);
		}
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		for(int yProgress=0;yProgress<ySize-var1;yProgress++){
			if(yProgress==0)GL11.glTranslatef(0, 1, 0);
			gui.drawTexturedModalRect(xPos+xSize-var1, yPos, invertDepth?7:25-var1, 102, var1, 1);
			GL11.glTranslatef(0, 1, 0);
		}
		GL11.glPopMatrix();
		gui.drawTexturedModalRect(xPos+xSize-1, yPos, 42, 101, 1, 1);
		gui.drawTexturedModalRect(xPos, yPos+ySize-1, 42, 101, 1, 1);
		GL11.glPushMatrix();
		if(useDynamicShadow)GL11.glColor4d(0, 0, 0, 77F/255F);
		for(int xProgress=0;xProgress<xSize-2;xProgress++){
			GL11.glTranslatef(1, 0, 0);
			GL11.glPushMatrix();
			for(int yProgress=0;yProgress<ySize-2;yProgress++){
				GL11.glTranslatef(0, 1, 0);
				gui.drawTexturedModalRect(xPos, yPos, invertDepth?4:26, invertDepth?10:102, 1, 1);
			}
			GL11.glPopMatrix();
		}
		if(useDynamicShadow)GL11.glColor4d(1,1,1,1);
		GL11.glPopMatrix();
		GL11U.endOpaqueRendering();
	}
	static Field equippedProgress,prevEquippedProgress;
	public static void setItemRendererEquippProgress(float From0To1,boolean isSmooth){
		ItemRenderer IR=U.getMC().entityRenderer.itemRenderer;
		if(IR!=null)try{
			if(!isSmooth){
				if(prevEquippedProgress==null)prevEquippedProgress = ItemRenderer.class.getDeclaredField("prevEquippedProgress");
				prevEquippedProgress.setAccessible(true);
				prevEquippedProgress.setFloat(IR,From0To1);
			}
			if(equippedProgress==null)equippedProgress = ItemRenderer.class.getDeclaredField("equippedProgress");
		    equippedProgress.setAccessible(true);
		    equippedProgress.setFloat(IR,From0To1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void drawLine(double x1,double y1,double z1,double x2,double y2,double z2,float width,boolean hasNormal, VertixBuffer nvb,double textueOffset,double textueScale){
		boolean newBuff;
		if(newBuff=(nvb==null)){
			nvb=new VertixBuffer();
		}
		double lenght=new Vec3M(x1-x2, y1-y2, z1-z2).lengthVector();
		
		QuadUV uv=new QuadUV(
			(float)(lenght*textueScale+textueOffset),1,
			(float)(lenght*textueScale+textueOffset),0,
			(float)textueOffset,0,
			(float)textueOffset,1
		);

		Vec3M[] points={
			new Vec3M(0,        -width/2, 0     ),
			new Vec3M(0,         width/2, 0     ),
			new Vec3M(0,         width/2, lenght),
			new Vec3M(0,        -width/2, lenght),
			new Vec3M(-width/2,        0, 0     ),
			new Vec3M( width/2,        0, 0     ),
			new Vec3M( width/2,        0, lenght),
			new Vec3M(-width/2,        0, lenght)
		};
		float
			ditanceX=(float)-(x1-x2),
			ditanceY=(float)-(y1-y2),
			ditanceZ=(float)-(z1-z2),
			rotationX=(float)-Math.toDegrees(Math.atan2(ditanceY,new Vec3M(x1-x2, 0, z1-z2).lengthVector())),
			rotationY=(float)-Math.toDegrees(Math.atan2(ditanceX, -ditanceZ));
		
		for(int i=0;i<points.length;i++){
			points[i]=GL11U.transformVector(points[i], new Vector3f(), rotationX, 0, 0, 1);
			points[i]=GL11U.transformVector(points[i], new Vector3f(), 0, rotationY+180, 0, 1);
			points[i]=GL11U.transformVector(points[i], new Vector3f((float)x1, (float)y1, (float)z1), 0, 0, 0, 1);
		}
		
		for(int i=0;i<points.length;i++)nvb.addVertexWithUV(points[i].x, points[i].y, points[i].z,uv.getUV(i%4).x,uv.getUV(i%4).y);
		for(int i=points.length-1;i>=0;i--)nvb.addVertexWithUV(points[i].x, points[i].y, points[i].z,uv.getUV(i%4).x,uv.getUV(i%4).y);
		if(newBuff)nvb.draw();
	}
	public static void renderParticle(){
		boolean isFP=U.getMC().gameSettings.thirdPersonView==2;
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		if(isFP)GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
		
		EntityFXM.renderBufferedParticle();
		
		
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glDisable(GL11.GL_BLEND);
		if(isFP)GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(true);
		
	}
	public static void draw(){
		Tessellator.getInstance().draw();
	}
	
	public static FontRendererMBase getCustomFontRednerer(){
		return fontRendererMBase;
	}
	public static FontRenderer getFontRenderer(){
		return UtilM.getMC().fontRendererObj;
	}
	public static void translateByEntityPos(Entity entity){
		GL11U.glTranslate(TessUtil.calculateRenderPos(entity));
	}
	public static float calculateRenderPos(Entity entity, char xyz){
		if((""+xyz).toLowerCase().equals("x")){
			return UtilM.calculatePos(entity.lastTickPosX,entity.posX);
		}
		if((""+xyz).toLowerCase().equals("y")){
			return UtilM.calculatePos(entity.lastTickPosY,entity.posY);
		}
		if((""+xyz).toLowerCase().equals("z")){
			return UtilM.calculatePos(entity.lastTickPosZ,entity.posZ);
		}
		PrintUtil.println(xyz,"is not a valid key! Use x or y or z.");
		return -1;
	}
	public static Vec3M calculateRenderPosV(Entity entity){
		return new Vec3M(
				calculateRenderPos(entity,'x'),
				calculateRenderPos(entity,'y'),
				calculateRenderPos(entity,'z'));
	}
	public static float[] calculateRenderPos(Entity entity){
		return new float[]{
				calculateRenderPos(entity,'x'),
				calculateRenderPos(entity,'y'),
				calculateRenderPos(entity,'z')};
	}
	
	private static CubeModel brainC1,brainC2;
	
	static{
		QuadUV uv=new QuadUV(0, 0, 1, 0, 1, 1, 0, 1);
		brainC1=new CubeModel(-UtilM.p, -UtilM.p, -UtilM.p, 0, 0, 0,new QuadUV[]{uv},new ResourceLocation[]{Textures.Brain});
		brainC2=new CubeModel(0, 0, 0 , UtilM.p, UtilM.p, UtilM.p,new QuadUV[]{uv},new ResourceLocation[]{Textures.Brain});
	}
	
	public static VertixBuffer drawBrain(Vec3M pos,double scale1,double anim){
		anim=Math.toRadians(anim);
		
		TessUtil.bindTexture(Textures.Brain);
		
		VertixBuffer wrapper=new VertixBuffer();
		VertixBuffer buff=TessUtil.getVB();
		
		
		buff.setInstantNormalCalculation(false);
		buff.pushMatrix();
		buff.translate(pos.x, pos.y, pos.z);
		buff.scale(scale1);
		{
			buff.pushMatrix();
			buff.importComplexCube(brainC1,brainC2);
			
			double scale=Math.sin(anim*10);
			scale=scale*scale;
			scale*=0.5;
			scale+=0.5;
			
			buff.scale(scale);
			buff.rotate(Math.sin(anim*4)*180,Math.sin(anim*8+235)*90,Math.sin(anim*2+859)*360);
			
			buff.transformAndSaveTo(wrapper);
			buff.popMatrix();
		}{
			buff.pushMatrix();
			buff.importComplexCube(brainC1,brainC2);
			
			double scale=Math.sin(anim*4);
			scale=scale*scale;
			scale*=0.5;
			scale+=0.5;
			
			buff.scale(scale);
			buff.rotate(Math.sin(anim*8)*90,Math.sin(anim*8+140)*90,-Math.sin(anim*2+90)*360);
			
			buff.transformAndSaveTo(wrapper);
			buff.popMatrix();
		}
		buff.popMatrix();
		
		buff.setInstantNormalCalculation(true);
		
		wrapper.recalculateNormals();
		return wrapper;
	}
}
