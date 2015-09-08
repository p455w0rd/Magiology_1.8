package com.magiology.gui.custom.hud;

import static com.magiology.core.MReference.NAME;
import static com.magiology.core.MReference.UPDATER_DIR;

import java.awt.Desktop;
import java.awt.Rectangle;
import java.io.File;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.magiology.core.MReference;
import com.magiology.core.Magiology;
import com.magiology.gui.custom.DownloadingIcon;
import com.magiology.handelers.web.DownloadingHandeler;
import com.magiology.io.IOReadableMap;
import com.magiology.util.renderers.GL11H;
import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.renderers.TessHelper;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.Helper;
import com.magiology.util.utilclasses.Helper.H;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.vectors.AdvancedPhysicsFloat;
import com.magiology.util.utilobjects.vectors.AdvancedPhysicsVec3F;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class MainMenuUpdateNotificationHUD{
	
	public static AdvancedPhysicsVec3F rotation=new AdvancedPhysicsVec3F(new Vec3M(), new Vec3M(0.2,0.2,0.2));
	public static AdvancedPhysicsFloat 
		backgroundBlue=new AdvancedPhysicsFloat(0,0.01F),
		textTransition=new AdvancedPhysicsFloat(0,0.3F,true),
		popup=new AdvancedPhysicsFloat(-0.5F,0.05F),
		down=new AdvancedPhysicsFloat(0,0.05F),
		button=new AdvancedPhysicsFloat(0,0.3F,true);
	private static NormalizedVertixBuffer buff=TessHelper.getNVB();
	private static DoubleObject<Integer, Boolean> mousePrev;
	public static boolean isClicked,selected,isDownloading=false,downloadingInvoked=false;
	public static ColorF backgroundColor=new ColorF(0, 0, 0, 0.2F);
	public static int timeout,lenghts[],biggest=-1;
	private static String[] text={
		"New update for: "+MReference.NAME,
		"[click for more]",
		"Click [HERE] to update the mod!",
		"WARNING: The message above will:",
		"1. turn off minecraft!",
		"2. open an automatic updater program",
		"The aplication will:",
		"1. remove the old mod file",
		"2. download a new one",
		"3. place it in your mods folder"
	};
	static{
		popup.friction=0.95F;
		popup.addWall("1", 0.9999F, true);
		popup.bounciness=0.4F;
		popup.wantedPoint=1;
		
		down.friction=0.95F;
		down.addWall("1", 0.9999F, true);
		down.bounciness=0.4F;
		down.wantedPoint=1;
		
		backgroundBlue.friction=0.95F;
		backgroundBlue.addWall("1", 1, true);
		backgroundBlue.addWall("0", 0, false);
		backgroundBlue.bounciness=0.4F;
		for(String a:text){
			int lenght=Get.Render.FR().getStringWidth(a);
			biggest=Math.max(biggest, lenght);
			lenghts=ArrayUtils.add(lenghts, lenght);
		}
	}
	
	public static void update(){
		popup.update();
		rotation.update();
		backgroundBlue.update();
		textTransition.update();
		button.update();
		down.update();
		
		if(H.RB(0.1)){
			rotation.x.wantedPoint=H.CRandF(20);
			rotation.y.wantedPoint=H.CRandF(20);
			rotation.z.wantedPoint=H.CRandF(1);
		}
		reimplementMouseEvents();
		if(selected){
			timeout++;
			rotation.x.wantedPoint=rotation.y.wantedPoint=rotation.z.wantedPoint=0;
			rotation.x.friction=rotation.y.friction=rotation.z.friction=0.3F;
		}else{
			rotation.x.friction=rotation.y.friction=rotation.z.friction=0.9F;
		}
		button.wantedPoint=H.booleanToInt(isClicked&&getBoundingBoxClick(false).contains(Mouse.getX(),Display.getHeight()-Mouse.getY()));
		
		
		if(timeout>0)timeout--;
		else isClicked=false;
		down.wantedPoint=isDownloading?1:-1;
		textTransition.wantedPoint=H.booleanToInt(selected);
		backgroundBlue.wantedPoint=H.booleanToInt(timeout>0);
		if(isClicked&&Math.abs(rotation.getPoint().x)<0.055){
			rotation.x.point=0;
			rotation.y.point=0;
			rotation.z.point=0;
		}
		if(downloadingInvoked&&!isDownloading){
			downloadingInvoked=false;
			terminateAndOpenUpdater();
		}
		
	}
	
	private static void onClick(int x,int y){
		if(isClicked&&getBoundingBoxClick(false).contains(x, y)){
			terminateAndOpenUpdater();
		}
		isClicked=true;
		timeout=40;
	}
	
	public static void terminateAndOpenUpdater(){
		if(!isDownloading)try{
			File updater=new File(UPDATER_DIR);
			if(updater.exists()){
				String updaterFolder=UPDATER_DIR.substring(0, UPDATER_DIR.lastIndexOf("/"))+"/";
				//bridge between the updater app and the mod
				IOReadableMap config=new IOReadableMap(updaterFolder+"/updaterConfig");
				config.set("modPos", "mods/"+Magiology.infoFile.getS("modPos",NAME+".jar"));
				config.set("url", DownloadingHandeler.findValue("NEWEST_VERSION_URL"));
				config.set("appPos", updaterFolder);
				config.set("isDev", Magiology.IS_DEV);
				config.writeToFile();
				Desktop.getDesktop().open(updater);
				Helper.exitSoft();
			}
			else DownloadingHandeler.downloadUpdater();
		}catch(Exception e){e.printStackTrace();}
	}
	
	public static void render(int width,int height){
		float 
			x=rotation.x.getPoint(), 
			y=rotation.y.getPoint(), 
			z=rotation.z.getPoint(),
			txtTrans=textTransition.getPoint()*0.25F;
		ColorF col=new ColorF(0.75+x/80+txtTrans, 0.75+y/80+txtTrans, 0.75+z/4+txtTrans, 1);
		int FH=Get.Render.FR().FONT_HEIGHT;
		Rectangle boundingBox=getBoundingBox(true);
		
		GL11H.protect();
		GL11H.rotateXYZAt(popup.getPoint()*90-90, -popup.getPoint()*30+30, popup.getPoint()*40-40, biggest/2, FH, 0);
		GL11H.scaled(popup.getPoint());
		GL11H.SetUpOpaqueRendering(1);
		GL11H.lighting(false);
		GL11H.depth(false);
		GL11H.texture(false);
		
		GL11H.rotateXYZAt(x,y,z, biggest/2, FH, 0);
		if(button.getPoint()>0){
			Rectangle boundingBoxClick=getBoundingBoxClick(true);
			GL11.glColor4f(1, 0F, 0F, button.getPoint());
			buff.addVertexWithUV(boundingBoxClick.getMinX(), boundingBoxClick.getMinY(), 0, 0, 0);
			buff.addVertexWithUV(boundingBoxClick.getMinX(), boundingBoxClick.getMaxY(), 0, 0, 0);
			buff.addVertexWithUV(boundingBoxClick.getMaxX(), boundingBoxClick.getMaxY(), 0, 0, 0);
			buff.addVertexWithUV(boundingBoxClick.getMaxX(), boundingBoxClick.getMinY(), 0, 0, 0);
			buff.draw();
		}
		GL11.glTranslatef(4, 4, 0);
		backgroundColor.a=0.2F+0.3F*backgroundBlue.getPoint();
		backgroundColor.b=backgroundBlue.getPoint();
		GL11H.color(backgroundColor);
		//input vertices
		buff.addVertexWithUV(0,                 0,                  0, 0, 0);
		buff.addVertexWithUV(0,                 boundingBox.height, 0, 0, 0);
		buff.addVertexWithUV(boundingBox.width, boundingBox.height, 0, 0, 0);
		buff.addVertexWithUV(boundingBox.width, 0,                  0, 0, 0);
		//do not kill them
		buff.disableClearing();
		//draw the quad
		buff.draw();
		//k now you can kill them and before you do so turn them into a wire
		buff.enableClearing();
		buff.setDrawModeToWireFrame();
		//and color them
		GL11.glColor4f(0, 0, 0, 0.4F+backgroundBlue.getPoint()*0.6F);
		//JUST DO IT!!
		buff.draw();
		//dam that was messed up
		buff.setDrawModeToQuadPlate();
		GL11.glColor4f(1,1,1,1);
		//reset stuff for font rendering
		GL11H.texture(true);
		//move text get out of the way. Get out of the way text get out of the way.
		
		GL11.glTranslatef(2, 2, 0);
		
		//draw text
		for(int i=0;i<text.length;i++){
			if(boundingBox.contains(boundingBox.x+boundingBox.width/2, boundingBox.y+FH*i+4)){
				double maxX=boundingBox.getMaxY();
				Get.Render.FR().drawStringWithShadow(text[i], (biggest-lenghts[i])/2, FH*i, col.set((float)((maxX-(boundingBox.y+FH*(i)+4))/FH), 'a').toCode());
			}
		}
		if(down.point>-0.1){
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glTranslatef(0, 110, 0);
			GL11H.scaled(0.2);
			GL11H.rotateXYZAt(-down.getPoint()*90+90, down.getPoint()*50-50, down.getPoint()*40-40, 85, 200, 0);
			GL11H.scaled(down.getPoint());
			DownloadingIcon.draw(col);
		}
		
		
		GL11H.EndOpaqueRendering();
		
		//protect vanilla rendering for my stuff
		GL11H.depth(true);
		GL11H.endProtection();
	}
	private static Rectangle getBoundingBox(boolean hasGL11Transformation){
		float scale=0;
		if(hasGL11Transformation)scale=Helper.getGuiScale();
		else scale=Helper.getGuiScaleRaw();
		int FH=Get.Render.FR().FONT_HEIGHT;
		return new Rectangle((int) ((4)*scale),(int) ((4)*scale),(int) ((biggest+4)*scale),(int) ((FH*2+FH*(text.length-2)*backgroundBlue.getPoint()+4)*scale));
	}
	private static Rectangle getBoundingBoxClick(boolean hasGL11Transformation){
		float scale=0;
		if(hasGL11Transformation)scale=Helper.getGuiScale();
		else scale=Helper.getGuiScaleRaw();
		int FH=Get.Render.FR().FONT_HEIGHT;
		return new Rectangle(
				(int) ((4+(biggest-lenghts[2])/2+Get.Render.FR().getStringWidth("Click "))*scale),
				(int) ((4+FH*2)*scale),
				(int) ((Get.Render.FR().getStringWidth("[HERE]")+3)*scale),
				(int) ((FH+2)*scale));
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static void reimplementMouseEvents(){
		Rectangle boundingBox=getBoundingBox(false);
		//simulate mouse event because forge mouse event is not active in main menu :(
		int x1=Mouse.getX(),y1=Display.getHeight()-Mouse.getY();
		selected=boundingBox.contains(x1,y1);
		if(mousePrev!=null){
			if(Mouse.getEventButton()==0&&Mouse.getEventButtonState()&&!mousePrev.obj2){
				if(boundingBox.contains(x1, y1)){
					onClick(x1,y1);
				}
			}
		}
		mousePrev=new DoubleObject<Integer, Boolean>(Mouse.getEventButton(),Mouse.getEventButtonState());
	}
}