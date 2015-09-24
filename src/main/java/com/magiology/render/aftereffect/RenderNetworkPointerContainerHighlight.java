package com.magiology.render.aftereffect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider.MultiColisionProviderRayTracer;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkPointerContainer;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilclasses.math.CricleUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.AdvancedPhysicsFloat;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class RenderNetworkPointerContainerHighlight extends LongAfterRenderRendererBase{
	private static EntityPlayer player=U.getMC().thePlayer;
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
		highlightedBoxId=MultiColisionProviderRayTracer.getPointedId(tile);
	}

	@Override
	public void render(){
		if(progress.getPoint()<0.01)return;
		//setup openGl
		GL11.glPushMatrix();
		GL11U.lighting(false);
		//move to block
		GL11.glTranslated(tile.x()+0.5, tile.y()+0.5, tile.z()+0.5);
		//move to side
		
		Vec3M off=getOffset();
		
		GL11U.translate(off);
		//set up openGl opaque
		GL11U.SetUpOpaqueRendering(3);
		GL11U.scaled(-U.p/4);
		
		//calculation of rotation
		float
			point=progress.getPoint(),
			point2=Util.keepValueInBounds(point, 0, 1),
			playerX=Util.calculateRenderPos(player, 'x'),
			playerY=Util.calculateRenderPos(player, 'y')+player.getEyeHeight(),
			playerZ=Util.calculateRenderPos(player, 'z'),
			txtX=tile.x()+0.5F+off.getX(),
			txtY=tile.y()+0.5F+off.getY(),
			txtZ=tile.z()+0.5F+off.getZ(),
			difX=playerX-txtX,
			difY=playerY-txtY,
			difZ=playerZ-txtZ,
			camPich=(float)Math.toDegrees(Math.atan2(difY,Math.sqrt(difX*difX+difZ*difZ))),
			camYaw=(float)Math.toDegrees(Math.atan2(difZ,difX))+90;
		int time360=(int)((tile.getWorld().getTotalWorldTime()+highlightedBoxId*20)%360);
		double time=Util.calculateRenderPos(time360, time360+1);
		
		//rotation
		GL11U.rotateXYZ(0                     , -camYaw-80+80*point2,                   0);
		GL11U.rotateXYZ(camPich+120-120*point2, 0                   , Math.sin(time/20)*2);
		GL11U.scaled(point2);
		//center string
		GL11.glTranslatef(-Util.getFontRenderer().getStringWidth(text)/2, -Util.getFontRenderer().FONT_HEIGHT/2, 0);
		
		float r=0.8F,g=Util.fluctuatorSmooth(20, 0)*0.15F+0.15F,b=0.1F;
		//draw in front
		Util.getFontRenderer().drawString(text, 0, 0, new ColorF(r,g,b,(point-0.4)).toCode());
		//draw behind block
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		Util.getFontRenderer().drawString(text, 0, 0, new ColorF(r,g,b,(point-0.4)*0.3).toCode());
		NormalizedVertixBuffer buff=TessUtil.getNVB();
		float expand=-1;
		buff.cleanUp();
		for(int i=0;i<5;i++){
			buff.addVertexWithUV(-expand, -expand, -0.001, 0, 0);
			buff.addVertexWithUV(-expand, expand+Util.getFontRenderer().FONT_HEIGHT, -0.001, 0, 0);
			buff.addVertexWithUV(expand+Util.getFontRenderer().getStringWidth(text), expand+Util.getFontRenderer().FONT_HEIGHT, -0.001, 0, 0);
			buff.addVertexWithUV(expand+Util.getFontRenderer().getStringWidth(text), -expand, -0.001, 0, 0);
			expand++;
		}
		GL11.glColor4f(0, 0, 0, 0.06F);
		GL11U.texture(false);
		GL11U.blendFunc(1);
		buff.setClearing(false);
		buff.setDrawAsWire(true);
		buff.draw();
		buff.setDrawAsWire(false);
		GL11.glColor4f(0, 0.4F+U.fluctuatorSmooth(50, 0)*0.6F, 0.6F+U.fluctuatorSmooth(97, 61)*0.4F, 0.01F);
		buff.draw();
		buff.setClearing(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		buff.draw();
		GL11U.texture(true);
		//reset openGl
		GL11U.EndOpaqueRendering();
		GL11U.lighting(true);
		GL11.glPopMatrix();
	}
	
	@Override
	public void update(){
		player=Util.getThePlayer();
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
		if(tile.getWorld().getTileEntity(tile.getPos())!=tile){
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
		
		Vec3M off=getOffset();
		float
			point=progress.getPoint(),
			playerX=(float)player.posX,
			playerZ=(float)player.posZ,
			txtX=tile.x()+0.5F+off.getX(),
			txtZ=tile.z()+0.5F+off.getZ(),
			difX=playerX-txtX,
			difZ=playerZ-txtZ,
			camYaw=(float)Math.toDegrees(Math.atan2(difZ,difX))+90,
			width=Util.getFontRenderer().getStringWidth(text)*U.p/4;
		Util.getFontRenderer();
		float leftX=CricleUtil.sin(-camYaw+90)*width/2, leftZ=CricleUtil.cos(-camYaw+90)*width/2, rand=Util.CRandF(2);
		
		
		float r=0.8F,g=Util.fluctuator(20, 0)*0.15F+0.15F,b=0.1F;
		Util.spawnEntityFX(new EntitySmoothBubleFX(player.worldObj,
				tile.x()+0.5+off.x+leftX*rand+Util.CRandF(0.1), tile.y()+0.5+off.y+Util.CRandF(0.1), tile.z()+0.5+off.z+leftZ*rand+Util.CRandF(0.1),
				Util.CRandF(0.01), 0.005+Util.CRandF(0.01), Util.CRandF(0.01), 200, 5, U.RB()?-0.5:0.1, r,g,b,0.1*point));
	}
	private Vec3M getOffset(){
		float x=0,y=0,z=0;
		int id=highlightedBoxId-7,idX=id/3,idY=id%3,side=tile.getOrientation(),multi=(side%2==0?-1:1);
		switch(side/2){
		case 0:{
			y=4*p*multi;
			x+=p*2;
			z+=p*2;
			x-=p*2*idX;
			z-=p*2*idY;
		}break;
		case 1:{
			z=4*p*multi;
			x+=p*2;
			y+=p*2;
			x-=p*2*idX;
			y-=p*2*idY;
		}break;
		case 2:{
			x=4*p*multi;
			z+=p*2;
			y+=p*2;
			z-=p*2*idX;
			y-=p*2*idY;
		}break;
		}
		return new Vec3M(x, y, z);
	}
}
