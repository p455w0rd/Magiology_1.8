package com.magiology.client.render.aftereffect;

import net.minecraft.entity.player.*;
import net.minecraft.util.*;

import org.lwjgl.opengl.*;

import com.magiology.core.init.*;
import com.magiology.mcobjects.tileentityes.*;
import com.magiology.util.renderers.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.UtilM.U;

public class RenderFirePipePriorityCube implements LongAfterRenderRenderer{
	
	private boolean isDead=false;
	public  double alpha=0;
	private final double speed;
	private final AxisAlignedBB box;
	private final BlockPos pos;
	public final TileEntityFirePipe pipe;
	private static EntityPlayer player=U.getMC().thePlayer;
	
	public RenderFirePipePriorityCube(TileEntityFirePipe pipe,BlockPos pos, AxisAlignedBB b){
		this.box=b;
		this.pipe=pipe;
		this.speed=0.06;
		alpha=speed;
		this.pos=pos;
	}
	
	@Override
	public void render(){
		GL11.glPushMatrix();
		GL11U.setUpOpaqueRendering(2);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
//		GL11.glDisable(GL11.GL_CULL_FACE);
//		GL11.glDisable(GL11.GL_FOG);
		GL11U.glTranslatep(pos);
		GL11.glDepthMask(false);
		GL11.glColor4d(1, 0.1, 0.1, alpha/5);
		if(box!=null)for(int a=0;a<5;a++)drawCube(box.minX-a*0.01+UtilM.CRandF(0.005), box.minY-a*0.01+UtilM.CRandF(0.005), box.minZ-a*0.01+UtilM.CRandF(0.005), box.maxX+a*0.01+UtilM.CRandF(0.005), box.maxY+a*0.01+UtilM.CRandF(0.005), box.maxZ+a*0.01+UtilM.CRandF(0.005));
		GL11.glColor4d(0.1, 0.1, 1, alpha*2/5);
		for(int a=0;a<5;a++)drawCube(pipe.collisionBoxes[6].minX-0.001-a*0.01+(a==0?0:UtilM.CRandF(0.005)), pipe.collisionBoxes[6].minY-0.001-a*0.01+(a==0?0:UtilM.CRandF(0.005)), pipe.collisionBoxes[6].minZ-0.001-a*0.01+(a==0?0:UtilM.CRandF(0.005)),pipe.collisionBoxes[6].maxX+0.001+a*0.01+(a==0?0:UtilM.CRandF(0.005)), pipe.collisionBoxes[6].maxY+0.001+a*0.01+(a==0?0:UtilM.CRandF(0.005)), pipe.collisionBoxes[6].maxZ+0.001+a*0.01+(a==0?0:UtilM.CRandF(0.005)));
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
//		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11U.endOpaqueRendering();
		GL11.glColor4d(1,1,1,1);
	}
	
	@Override
	public void update(){
		if(UtilM.isItemInStack(MItems.FireHammer, player.inventory.mainInventory[player.inventory.currentItem]))
			 alpha+=speed;
		else alpha-=speed;
		double maxAlpha=0.3;
		if(alpha>maxAlpha)alpha=maxAlpha;
		else if(alpha<=0){kill();return;}
	}
	
	@Override
	public boolean isDead(){return isDead;}
	@Override
	public void kill(){isDead=true;}
	public void drawCube(double minx,double miny,double minz,double maxx,double maxy,double maxz){
		Renderer.POS.beginQuads();
		Renderer.POS.addVertex(minx, miny, maxz);
		Renderer.POS.addVertex(minx, miny, minz);
		Renderer.POS.addVertex(maxx, miny, minz);
		Renderer.POS.addVertex(maxx, miny, maxz);

		Renderer.POS.addVertex(maxx, maxy, maxz);
		Renderer.POS.addVertex(maxx, maxy, minz);
		Renderer.POS.addVertex(minx, maxy, minz);
		Renderer.POS.addVertex(minx, maxy, maxz);

		Renderer.POS.addVertex(maxx, maxy, minz);
		Renderer.POS.addVertex(maxx, miny, minz);
		Renderer.POS.addVertex(minx, miny , minz);
		Renderer.POS.addVertex(minx, maxy, minz);

		Renderer.POS.addVertex(minx, maxy, maxz);
		Renderer.POS.addVertex(minx, miny , maxz);
		Renderer.POS.addVertex(maxx, miny, maxz);
		Renderer.POS.addVertex(maxx, maxy, maxz);

		Renderer.POS.addVertex(minx, maxy, minz);
		Renderer.POS.addVertex(minx, miny, minz);
		Renderer.POS.addVertex(minx, miny, maxz);
		Renderer.POS.addVertex(minx, maxy, maxz);

		Renderer.POS.addVertex(maxx, maxy, maxz);
		Renderer.POS.addVertex(maxx, miny,  maxz);
		Renderer.POS.addVertex(maxx, miny,  minz);
		Renderer.POS.addVertex(maxx, maxy, minz);
		Renderer.POS.draw();
	}
}
