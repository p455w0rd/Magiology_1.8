package com.magiology.client.render.tilerender.network;

import net.minecraft.tileentity.*;
import net.minecraft.util.*;

import org.lwjgl.opengl.*;

import com.magiology.mcobjects.tileentityes.network.*;
import com.magiology.util.renderers.*;
import com.magiology.util.renderers.NormalizedVertixBuffer.ShadedTriangle;
import com.magiology.util.renderers.tessellatorscripts.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.m_extension.*;
import com.magiology.util.utilobjects.vectors.*;

public class RenderNetworkController extends TileEntitySpecialRendererM{

	@Override
	public void renderTileEntityAt(TileEntity tile0, double x, double y, double z, float pt){
		TileEntityNetworkController tile=(TileEntityNetworkController)tile0;
		NormalizedVertixBuffer buff=TessUtil.getNVB();
		GL11U.protect();
		GL11.glTranslated(x,y,z);
		
		double tim=tile.getWorld().getTotalWorldTime()+(x+y+z)*30;
		float anim=(UtilM.calculatePos(tim-1, tim)/3)%360;
		
		double upDown=Math.sin(Math.toRadians(anim)*4)*UtilM.p/2;
		
		NormalizedVertixBuffer brainModel=TessUtil.drawBrain(new Vec3M(0.5, 0.5+upDown, 0.5), 1, anim);
		
		Vec3 middle=brainModel.getTriangle(0).pos3[2].vector3D, minY=middle,maxY=middle;
		
		ShadedTriangle tri;
		
		boolean noError=false;
		int index=0;
		while(!noError){
			try{
				tri=brainModel.getTriangle(index++);
				for(int i=0;i<3;i++){
					Vec3 newPos=tri.pos3[i].vector3D;
					if(newPos.yCoord>maxY.yCoord)maxY=newPos;
					if(newPos.yCoord<minY.yCoord)minY=newPos;
				}
			}catch(Exception e){
				noError=true;
			}
		}
		
		brainModel.draw();

		GL11.glColor3b((byte)0, (byte)12, (byte)26);
		
		GL11U.texture(false);
		GL11U.glCulFace(false);
		TessUtil.drawLine(minY.xCoord+UtilM.CRandF(minY.yCoord-p*6)/2, p*6,   minY.zCoord+UtilM.CRandF(minY.yCoord-p*6)/2, minY.xCoord, minY.yCoord+0.01, minY.zCoord, p/8, true, buff, 0, 10);
		TessUtil.drawLine(maxY.xCoord+UtilM.CRandF(p*10-maxY.yCoord)/2, p*10,  maxY.zCoord+UtilM.CRandF(p*10-maxY.yCoord)/2, maxY.xCoord, maxY.yCoord-0.01, maxY.zCoord, p/8, true, buff, 0, 10);
		
		TessUtil.drawLine(p*5.05, p*6,p*5.05, p*6.05, p*10, p*6.05, p/4, false, buff, 0, 10);
		TessUtil.drawLine(p*10.95,p*6,p*5.05, p*9.95, p*10, p*6.05, p/4, false, buff, 0, 10);
		
		TessUtil.drawLine(p*5.05, p*6,p*10.95, p*6.05, p*10, p*9.95, p/4, false, buff, 0, 10);
		TessUtil.drawLine(p*10.95,p*6,p*10.95, p*9.95, p*10, p*9.95, p/4, false, buff, 0, 10);
		
		
		buff.draw();
		
		GL11U.glCulFace(true);
		
		buff.importComplexCube(new ComplexCubeModel(p*5, p*5, p*5, p*11, p*6, p*11),new ComplexCubeModel(p*6, p*10, p*6, p*10, p*10.5F, p*10));
		
		buff.draw();
		
		GL11U.setUpOpaqueRendering(1);
		new ColorF(0.6,0,0,0.1).bind();
		ComplexCubeModel glass=new ComplexCubeModel(p*6.5F, p*6, p*6.5F, p*9.5F, p*10, p*9.5F);
		glass.willSideRender[2]=false;
		glass.willSideRender[3]=false;
		for(int i=0;i<5;i++){
			glass.expand(-0.004F,0,0.004F);
			buff.importComplexCube(glass);
		}
		buff.draw();
		GL11U.endOpaqueRendering();
		
		
		GL11U.texture(true);
		
		GL11U.endProtection();
	}

}
