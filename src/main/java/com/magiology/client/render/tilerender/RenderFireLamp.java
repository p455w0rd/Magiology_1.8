package com.magiology.client.render.tilerender;

import java.util.List;

import com.magiology.client.render.aftereffect.LongAfterRenderRenderer;
import com.magiology.client.render.aftereffect.TwoDotsLineRender;
import com.magiology.forgepowered.events.client.RenderEvents;
import com.magiology.mcobjects.tileentityes.TileEntityFireLamp;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;
import com.magiology.util.utilobjects.vectors.TwoDots;
import com.magiology.util.utilobjects.vectors.Vec2i;
import com.magiology.util.utilobjects.vectors.Vec3M;
import com.magiology.util.utilobjects.vectors.physics.AbstractRealPhysicsVec3F;

import net.minecraft.tileentity.TileEntity;

public class RenderFireLamp extends TileEntitySpecialRendererM{

	private final float p= 1F/16F;
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f){
		TileEntityFireLamp tileFL=(TileEntityFireLamp) tile;
		boolean var1=true;
		for(int a=0;a<RenderEvents.universalLongRender.size();a++){
			LongAfterRenderRenderer ab=RenderEvents.universalLongRender.get(a);
			if(ab instanceof TwoDotsLineRender&&!((TwoDotsLineRender)ab).isDead())if(((TwoDotsLineRender)ab).tile==tileFL)var1=false;
		}
		if(var1)RenderEvents.spawnLARR(new TwoDotsLineRender(new TwoDots(tileFL.x()+0.5, tileFL.y()+0.5, tileFL.z()+0.5, tileFL.control.getX()+0.5, tileFL.control.getY()+0.5, tileFL.control.getZ()+0.5),tileFL));
		float FP=PowerUtil.getFuelPrecentage(tileFL)+1,a=FP*10-1;
		if(a>1)a=1;
		else if(a<0)a=0;
		OpenGLM.pushMatrix();
		OpenGLM.translate(x,y,z);
		OpenGLM.disableTexture2D();

		OpenGLM.color(1,1,1,1);
		if(tileFL.cloth!=null){
			GL11U.glTranslate(new Vec3M().add(tile.getPos()).mul(-1));
			List<AbstractRealPhysicsVec3F> vertices=tileFL.cloth.getVertices();
			List<Vec2i> indices=tileFL.cloth.getIndices();

			for(Vec2i vec2i:indices){
				Vec3M vec1=vertices.get(vec2i.x).getPos(),vec2=vertices.get(vec2i.y).getPos();
//				PrintUtil.println(vec1,vec2);
				TessUtil.drawLine(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z, p, false, TessUtil.getVB(), 0, 0);
			}
			TessUtil.getVB().draw();
		}
		GL11U.setUpOpaqueRendering(1);
		
		TessUtil.drawBlurredCube((int)x, (int)y, (int)z, p*4.5,0.01,p*4.5,p*11.5,p*11*FP,p*11.5, 15, 0.03, 1,0.1,0.1, 0.5*a);
		OpenGLM.popMatrix();
		OpenGLM.depthMask(true);
		OpenGLM.enableTexture2D();
		GL11U.endOpaqueRendering();
		OpenGLM.color(1,1,1,1);
		OpenGLM.enableLighting();
		}
	
	
}