package com.magiology.render.aftereffect;

import org.lwjgl.opengl.GL11;

import com.magiology.forgepowered.event.RenderLoopEvents;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider.MultiColisionProviderRayTracer;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkPointerContainer;
import com.magiology.modedmcstuff.AdvancedPhysicsFloat;
import com.magiology.modedmcstuff.ColorF;
import com.magiology.objhelper.helpers.Cricle;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.NoramlisedVertixBuffer;
import com.magiology.objhelper.helpers.renderers.TessHelper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class RenderNetworkPointerContainerHighlight extends LongAfterRenderRendererBase{
	private static EntityPlayer player=Minecraft.getMinecraft().thePlayer;
	private static final float p= 1F/16F;
	public TileEntityNetworkPointerContainer tile;
	public AdvancedPhysicsFloat progress;
	public int highlightedBoxId;
	public String text;
	
	public RenderNetworkPointerContainerHighlight(TileEntityNetworkPointerContainer tile){
		this.tile=tile;
		progress=new AdvancedPhysicsFloat(0.01F, 0.06F);
		progress.friction=0.9F;
		progress.addWall("zero", 0, false);
		progress.addWall("one", 1, true);
		progress.bounciness=0.6F;
		highlightedBoxId=MultiColisionProviderRayTracer.getPointedId(tile);
	}

	@Override
	public void render(){
		if(progress.getPoint()<0.01)return;
		//setup openGl
		GL11.glPushMatrix();
		GL11H.lighting(false);
		//move to block
		GL11.glTranslated(tile.xCoord+0.5, tile.yCoord+0.5, tile.zCoord+0.5);
		//move to side
		float x=0,y=0,z=0;
		int id=highlightedBoxId-7,idX=id/3,idY=id%3;
		switch(tile.getOrientation()){
		case 2:{
			z=-0.5F;
			x+=p*2;
			y+=p*2;
			x-=p*2*idX;
			y-=p*2*idY;
		}break;
		}
		GL11.glTranslatef(x, y, z);
		//set up openGl opaque
		GL11H.SetUpOpaqueRendering(3);
		GL11H.scaled(-H.p/4);
		
		//calculation of rotation
		float
			point=progress.getPoint(),
			point2=Helper.keepAValueInBounds(point, 0, 1),
			playerX=Helper.calculateRenderPos(player, 'x'),
			playerY=Helper.calculateRenderPos(player, 'y'),
			playerZ=Helper.calculateRenderPos(player, 'z'),
			txtX=tile.xCoord+0.5F+x,
			txtY=tile.yCoord+0.5F+y,
			txtZ=tile.zCoord+0.5F+z,
			difX=playerX-txtX,
			difY=playerY-txtY,
			difZ=playerZ-txtZ,
			camPich=(float)Math.toDegrees(Math.atan2(difY,Math.sqrt(difX*difX+difZ*difZ))),
			camYaw=(float)Math.toDegrees(Math.atan2(difZ,difX))+90;
		int time360=(int)((tile.getWorldObj().getTotalWorldTime()+highlightedBoxId*20)%360);
		double time=Helper.calculateRenderPos(time360, time360+1);
		
		//rotation
		GL11H.rotateXYZ(0                     , -camYaw-80+80*point2,                   0);
		GL11H.rotateXYZ(camPich+120-120*point2, 0                   , Math.sin(time/20)*2);
		GL11H.scaled(point2);
		//center string
		GL11.glTranslatef(-Helper.getFontRenderer().getStringWidth(text)/2, -Helper.getFontRenderer().FONT_HEIGHT/2, 0);
		
		float r=0.8F,g=Helper.fluctuatorSmooth(20, 0)*0.15F+0.15F,b=0.1F;
		//draw in front
		Helper.getFontRenderer().drawString(text, 0, 0, new ColorF(r,g,b,(point-0.4)).toCode());
		//draw behind block
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		Helper.getFontRenderer().drawString(text, 0, 0, new ColorF(r,g,b,(point-0.4)*0.3).toCode());
		NoramlisedVertixBuffer buff=TessHelper.getNVB();
		GL11.glColor4f(0, 0, 0, 0.03F);
		GL11H.texture(false);
		GL11H.GL11BlendFunc(1);
		float expand=-1;
		buff.cleanUp();
		for(int i=0;i<5;i++){
			buff.addVertexWithUV(-expand, -expand, -0.001, 0, 0);
			buff.addVertexWithUV(-expand, expand+Helper.getFontRenderer().FONT_HEIGHT, -0.001, 0, 0);
			buff.addVertexWithUV(expand+Helper.getFontRenderer().getStringWidth(text), expand+Helper.getFontRenderer().FONT_HEIGHT, -0.001, 0, 0);
			buff.addVertexWithUV(expand+Helper.getFontRenderer().getStringWidth(text), -expand, -0.001, 0, 0);
			expand++;
		}
		buff.disableClearing();
		buff.draw();
		buff.enableClearing();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		buff.draw();
		GL11H.texture(true);
		//reset openGl
		GL11H.EndOpaqueRendering();
		GL11H.lighting(true);
		GL11.glPopMatrix();
	}
	
	@Override
	public void update(){
		player=Helper.getThePlayer();
		if(player==null)return;
		progress.update();
		if(MultiColisionProviderRayTracer.getPointedId(tile)==highlightedBoxId)progress.wantedPoint=1;
		else{
			progress.wantedPoint=0F;
			progress.point-=progress.speed/1.5;
		}
		
		if(progress.wantedPoint==0&&progress.point<1F/256F){
			kill();
			return;
		}
		if(tile==null){
			kill();
			return;
		}
		if(tile.getWorldObj().getTileEntity(tile.xCoord, tile.yCoord, tile.zCoord)!=tile){
			kill();
			return;
		}
		
		if(tile.getStackInSlot(highlightedBoxId-7)!=null){
			NBTTagCompound nbt=tile.getStackInSlot(highlightedBoxId-7).getTagCompound();
			if(nbt==null)text="no bound position";
			else{
				int x=nbt.getInteger("xLink"),y=nbt.getInteger("yLink"),z=nbt.getInteger("zLink");
				text="target = ["+x+", "+y+", "+z+"]";
			}
		}else text="empty";
		

		float x=0,y=0,z=0;
		int id=highlightedBoxId-7,idX=id/3,idY=id%3;
		switch(tile.getOrientation()){
		case 2:{
			z=-0.5F;
			x+=p*2;
			y+=p*2;
			x-=p*2*idX;
			y-=p*2*idY;
		}break;
		}
		float
			point=progress.getPoint(),
			playerX=(float)player.posX,
			playerZ=(float)player.posZ,
			txtX=tile.xCoord+0.5F+x,
			txtZ=tile.zCoord+0.5F+z,
			difX=playerX-txtX,
			difZ=playerZ-txtZ,
			camYaw=(float)Math.toDegrees(Math.atan2(difZ,difX))+90,
			width=Helper.getFontRenderer().getStringWidth(text)*H.p/4,
			height=Helper.getFontRenderer().FONT_HEIGHT*H.p/4,
			leftX=(float)Cricle.sin(-camYaw+90)*width/2,
			leftZ=(float)Cricle.cos(-camYaw+90)*width/2,
			rand=Helper.CRandF(2);
		
		
		float r=0.8F,g=Helper.fluctuator(20, 0)*0.15F+0.15F,b=0.1F;
		Helper.spawnEntityFX(new EntitySmoothBubleFX(player.worldObj,
				tile.xCoord+0.5+x+leftX*rand+Helper.CRandF(0.1), tile.yCoord+0.5+y+Helper.CRandF(0.1), tile.zCoord+0.5+z+leftZ*rand+Helper.CRandF(0.1),
				Helper.CRandF(0.01), 0.005+Helper.CRandF(0.01), Helper.CRandF(0.01), 200, 5, H.RB()?-0.5:0.1, r,g,b,0.1*point));
	}
}
