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
	
	private static SidedModel connections;
	private NormalizedVertixBufferModel opaqueNoTexture,noTexture,texture;
	private static void initModels(){
		NormalizedVertixBuffer buff=TessUtil.getNVB();
		
		ComplexCubeModel[] sideThingys={
				new ComplexCubeModel(p*6.5F, p*6.5F, p*11F, p*7F, p*7F, p*13F),
				new ComplexCubeModel(p*6.5F, p*6.5F, p*13F, p*7F, p*7F, 1),
				new ComplexCubeModel(p*6.5F, p*9F, p*9.5F, p*7F, p*9.5F, p*13F),
				new ComplexCubeModel(p*6.5F, p*9F, p*13F, p*7F, p*9.5F, 1),
				new ComplexCubeModel(p*9F, p*6.5F, p*11F, p*9.5F, p*7F, p*13F),
				new ComplexCubeModel(p*9F, p*6.5F, p*13F, p*9.5F, p*7F, 1),
				new ComplexCubeModel(p*9F, p*9F, p*9.5F, p*9.5F, p*9.5F, p*13F),
				new ComplexCubeModel(p*9F, p*9F, p*13F, p*9.5F, p*9.5F, 1)
		};
		sideThingys[0].points[0].y-=p*1.5;
		sideThingys[0].points[1].y-=p*1.5;
		sideThingys[0].points[4].y-=p*1.5;
		sideThingys[0].points[5].y-=p*1.5;
		
		sideThingys[2].points[0].y-=p*3;sideThingys[2].points[0].z+=p*0.5;
		sideThingys[2].points[1].y-=p*3.5;
		sideThingys[2].points[4].y-=p*3;sideThingys[2].points[4].z+=p*0.5;
		sideThingys[2].points[5].y-=p*3.5;
		
		sideThingys[4].points[0].y-=p*1.5;
		sideThingys[4].points[1].y-=p*1.5;
		sideThingys[4].points[4].y-=p*1.5;
		sideThingys[4].points[5].y-=p*1.5;
		
		sideThingys[6].points[0].y-=p*3;sideThingys[6].points[0].z+=p*0.5;
		sideThingys[6].points[1].y-=p*3.5;
		sideThingys[6].points[4].y-=p*3;sideThingys[6].points[4].z+=p*0.5;
		sideThingys[6].points[5].y-=p*3.5;
		
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].willSideRender[4]=sideThingys[i].willSideRender[5]=false;
		
		buff.importComplexCube(sideThingys);
		
		ComplexCubeModel[] cores={
			new ComplexCubeModel(p*7F, p*7F, p*13F, p*9F, p*9F, 1),
			new ComplexCubeModel(p*7.75F, p*7.75F, p*12F, p*8.25F, p*8.25F, p*13),
			new ComplexCubeModel(p*7.75F, p*8.75F, p*12F, p*8.25F, p*9.25F, p*13),
			new ComplexCubeModel(p*7.75F, p*6.75F, p*12F, p*8.25F, p*7.25F, p*13),
			new ComplexCubeModel(p*8.75F, p*7.75F, p*12F, p*9.25F, p*8.25F, p*13),
			new ComplexCubeModel(p*6.75F, p*7.75F, p*12F, p*7.25F, p*8.25F, p*13)
		};
		for(int i=0;i<cores.length;i++)cores[i].willSideRender[5]=false;
		cores[2].points[2].y-=p*0.25;
		cores[2].points[6].y-=p*0.25;
		
		cores[3].points[3].y+=p*0.25;
		cores[3].points[7].y+=p*0.25;
		
		cores[4].points[3].x-=p*0.25;
		cores[4].points[2].x-=p*0.25;
		
		cores[5].points[6].x+=p*0.25;
		cores[5].points[7].x+=p*0.25;
		
		buff.importComplexCube(cores);
		
		NormalizedVertixBufferModel horisontalModels=buff.exportToNoramlisedVertixBufferModel();
		
		
		
		buff.importComplexCube(new ComplexCubeModel(p*7F, p*7F, p*13F, p*9F, p*9F, 1));
		NormalizedVertixBufferModel upModel=buff.exportToNoramlisedVertixBufferModel();
		
		
		
		buff.importComplexCube(new ComplexCubeModel(p*7F, p*7F, p*13F, p*9F, p*9F, 1));
		NormalizedVertixBufferModel downModel=buff.exportToNoramlisedVertixBufferModel();
		
		
		connections=new SidedModel(new DoubleObject<NormalizedVertixBufferModel,int[]>(horisontalModels,new int[]{2,3,4,5}),new DoubleObject<NormalizedVertixBufferModel,int[]>(upModel,new int[]{0}),new DoubleObject<NormalizedVertixBufferModel,int[]>(downModel,new int[]{1}));
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile0, double x, double y, double z, float pt){
		initModels();
		TileEntityNetworkController tile=(TileEntityNetworkController)tile0;
		NormalizedVertixBuffer buff=TessUtil.getNVB();
		GL11U.protect();
		GL11.glTranslated(x,y,z);
		
		double tim=tile.getWorld().getTotalWorldTime()+(x+y+z)*30;
		float anim=(UtilM.calculatePos(tim-1, tim)/3)%360;
		
		double upDown=Math.sin(Math.toRadians(anim)*4)*UtilM.p/2;
		
		NormalizedVertixBuffer brainModel=TessUtil.drawBrain(new Vec3M(0.5, 0.5+upDown, 0.5), 1, anim);
		
		Vec3 middle=brainModel.getTriangle(0).pos3[2].vector3D, minY=middle,maxY=middle;
		
		
		for(ShadedTriangle tri:brainModel.getTriangles())for(int i=0;i<3;i++){
			Vec3 newPos=tri.pos3[i].vector3D;
			if(newPos.yCoord>maxY.yCoord)maxY=newPos;
			if(newPos.yCoord<minY.yCoord)minY=newPos;
		}
		
		
		brainModel.draw();

		GL11.glColor3b((byte)0, (byte)12, (byte)26);
		
		GL11U.texture(false);
		
		boolean[] sides=new boolean[6];
		for(int i=0;i<6;i++)sides[i]=tile.connections[i].getMain();
		
		connections.draw(sides);
		TessUtil.drawLine(minY.xCoord+UtilM.CRandF(minY.yCoord-p*6)/2, p*6,   minY.zCoord+UtilM.CRandF(minY.yCoord-p*6)/2, minY.xCoord, minY.yCoord+0.01, minY.zCoord, p/8, true, buff, 0, 10);
		TessUtil.drawLine(maxY.xCoord+UtilM.CRandF(p*10-maxY.yCoord)/2, p*10,  maxY.zCoord+UtilM.CRandF(p*10-maxY.yCoord)/2, maxY.xCoord, maxY.yCoord-0.01, maxY.zCoord, p/8, true, buff, 0, 10);
		

		TessUtil.drawLine(p*5.05, p*6,p*5.05, p*6.05, p*10, p*6.05, p/4, false, buff, 0, 10);
		TessUtil.drawLine(p*10.95,p*6,p*5.05, p*9.95, p*10, p*6.05, p/4, false, buff, 0, 10);
		
		TessUtil.drawLine(p*5.05, p*6,p*10.95, p*6.05, p*10, p*9.95, p/4, false, buff, 0, 10);
		TessUtil.drawLine(p*10.95,p*6,p*10.95, p*9.95, p*10, p*9.95, p/4, false, buff, 0, 10);
		
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
