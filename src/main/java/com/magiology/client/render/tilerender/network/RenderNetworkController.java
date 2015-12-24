package com.magiology.client.render.tilerender.network;

import net.minecraft.client.gui.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.*;

import org.lwjgl.opengl.*;

import com.magiology.client.render.*;
import com.magiology.client.render.shaders.*;
import com.magiology.core.*;
import com.magiology.core.init.*;
import com.magiology.mcobjects.tileentityes.network.*;
import com.magiology.util.renderers.*;
import com.magiology.util.renderers.VertixBuffer.ShadedTriangle;
import com.magiology.util.renderers.glstates.*;
import com.magiology.util.renderers.tessellatorscripts.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.m_extension.*;
import com.magiology.util.utilobjects.vectors.*;

public class RenderNetworkController extends TileEntitySpecialRendererM{
	
	private static SidedModel connections;
	private static VertixModel body1,body2;
	private static ColorF glassColor=new ColorF(0.6,0,0,0.1);
	
	public RenderNetworkController(){
		initModels();
	}
	
	private static void initModels(){
		VertixBuffer buff=TessUtil.getVB();
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		QuadUV all=QuadUV.all().rotate();
		ComplexCubeModel[] sideThingys={
				new ComplexCubeModel(p*6.5F, p*6.5F, p*11F, p*7F, p*7F, p*13F, new QuadUV[]{all.rotate(),all.rotate(),all,all,all,all},null),
				new ComplexCubeModel(p*6.5F, p*6.5F, p*13F, p*7F, p*7F, 1, new QuadUV[]{all.rotate(),all.rotate(),all,all,all,all},null),
				new ComplexCubeModel(p*6.5F, p*9F, p*9.5F, p*7F, p*9.5F, p*13F, new QuadUV[]{all.rotate(),all.rotate(),all,all,all,all},null),
				new ComplexCubeModel(p*6.5F, p*9F, p*13F, p*7F, p*9.5F, 1, new QuadUV[]{all.rotate(),all.rotate(),all,all,all,all},null)
		};
		sideThingys[0].points[0].y-=p*1.5;
		sideThingys[0].points[1].y-=p*1.5;
		sideThingys[0].points[4].y-=p*1.5;
		sideThingys[0].points[5].y-=p*1.5;
		
		sideThingys[2].points[0].y-=p*3;sideThingys[2].points[0].z+=p*0.5;
		sideThingys[2].points[1].y-=p*3.5;
		sideThingys[2].points[4].y-=p*3;sideThingys[2].points[4].z+=p*0.5;
		sideThingys[2].points[5].y-=p*3.5;
		
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].willSideRender[4]=sideThingys[i].willSideRender[5]=false;

		buff.importComplexCube(sideThingys);
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].translate(p*2.5F, 0, 0);
		buff.importComplexCube(sideThingys);
		
		VertixModel sideThingysModel=buff.exportToNoramlisedVertixBufferModel();
		sideThingysModel.glStateCell=new GlStateCell(
			new GlState(new int[]{GL11.GL_TEXTURE_2D}, new int[]{}, ()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/iron_strip.png"));
			})
			, null);
		float height=1/12F;
		QuadUV 
			sideCon1=new QuadUV(
				1,height*4,
				1,0,
				0,height*2,
				0,height*4
			).rotate(),
			sideCon2=new QuadUV(
				1,height*4,
				1,0,
				0,height*2,
				0,height*4
			).mirror().rotate(),
			front=new QuadUV(
				0.5F,height*8,
				0.5F,1,
				0,1,
				0,height*8
			),
			sideCon3=new QuadUV(
				1,height*8,
				1,height*4,
				0,height*4,
				0,height*8
			).rotate().mirror();
		
		ComplexCubeModel[] cores={
			new ComplexCubeModel(p*7.75F, p*7.75F, p*12F, p*8.25F, p*8.25F, p*13, new QuadUV[]{
					sideCon3.rotate(),sideCon3.mirror().rotate(),sideCon3,sideCon3,front,all},null),
					
			new ComplexCubeModel(p*7.75F, p*8.75F, p*12F, p*8.25F, p*9.25F, p*13, new QuadUV[]{
					sideCon1.mirror().rotate(),sideCon1.rotate(),sideCon3,sideCon3,front,all},null),
			new ComplexCubeModel(p*7.75F, p*6.75F, p*12F, p*8.25F, p*7.25F, p*13, new QuadUV[]{
					sideCon2.mirror().rotate(),sideCon2.rotate(),sideCon3,sideCon3,front,all},null),
					
			new ComplexCubeModel(p*8.75F, p*7.75F, p*12F, p*9.25F, p*8.25F, p*13, new QuadUV[]{
					sideCon3.rotate(),sideCon3.mirror().rotate(),sideCon2.mirror(),sideCon1.mirror(),front,all},null),
			new ComplexCubeModel(p*6.75F, p*7.75F, p*12F, p*7.25F, p*8.25F, p*13, new QuadUV[]{
					sideCon3.rotate(),sideCon3.mirror().rotate(),sideCon1.mirror(),sideCon2.mirror(),front,all},null)
		};
		for(int i=0;i<cores.length;i++)cores[i].willSideRender[5]=false;
		
		
		cores[1].points[2].y-=p*0.25;
		cores[1].points[6].y-=p*0.25;
		
		cores[2].points[3].y+=p*0.25;
		cores[2].points[7].y+=p*0.25;
		
		cores[3].points[3].x-=p*0.25;
		cores[3].points[2].x-=p*0.25;
		
		cores[4].points[6].x+=p*0.25;
		cores[4].points[7].x+=p*0.25;
		
		buff.importComplexCube(cores);
		
		VertixModel horisontalConnectionToBrain=buff.exportToNoramlisedVertixBufferModel();
		horisontalConnectionToBrain.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe_brain_con.png"));
			})
			, null);
		float w1=1F/216F;
		QuadUV coreUV=new QuadUV(
				w1*56F,1,
				w1*56F,0,
				w1*24,0,
				w1*24,1
			).rotate().mirror();
		ComplexCubeModel core=new ComplexCubeModel(p*7F, p*7F, p*13F, p*9F, p*9F, 1,new QuadUV[]{
			coreUV.rotate(),coreUV.mirror().rotate(),coreUV,coreUV,QuadUV.all(),coreUV
		},null);
		core.willSideRender[5]=core.willSideRender[4]=false;
		
		buff.importComplexCube(core);
		
		VertixModel horisontalCore1=buff.exportToNoramlisedVertixBufferModel();
		horisontalCore1.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe.png"));
			})
		, null);
		for(int i=0;i<core.willSideRender.length;i++)core.willSideRender[i]=i==4;
		buff.importComplexCube(core);
		VertixModel horisontalCore2=buff.exportToNoramlisedVertixBufferModel();
		horisontalCore2.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe_end.png"));
			})
		, null);
		ComplexCubeModel[] upConsCubes={
				
		};
		buff.importComplexCube(upConsCubes);
		VertixModel upCons=buff.exportToNoramlisedVertixBufferModel();
		horisontalCore2.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/iron_strip.png"));
			})
		, null);
		
		sideThingys[0].points[0].y+=p*1.5;sideThingys[0].points[1].y+=p*1.5;sideThingys[0].points[4].y+=p*1.5;sideThingys[0].points[5].y+=p*1.5;
		sideThingys[2].points[0].y+=p*3;sideThingys[2].points[0].z-=p*0.5;sideThingys[2].points[1].y+=p*3.5;sideThingys[2].points[4].y+=p*3;sideThingys[2].points[4].z-=p*0.5;sideThingys[2].points[5].y+=p*3.5;
		
		sideThingys[0].points[0].z-=p/2;sideThingys[0].points[1].z-=p/2;sideThingys[0].points[4].z-=p/2;sideThingys[0].points[5].z-=p/2;
		sideThingys[2].points[0].z+=p;sideThingys[2].points[1].z+=p;sideThingys[2].points[4].z+=p;sideThingys[2].points[5].z+=p;
		
		sideThingys[0].points[0].y-=p*0.5;
		sideThingys[0].points[1].y-=p*0.5;
		sideThingys[0].points[4].y-=p*0.5;
		sideThingys[0].points[5].y-=p*0.5;
		sideThingys[0].points[0].x+=p*0.5;
		sideThingys[0].points[1].x+=p*0.5;
		sideThingys[0].points[4].x+=p*0.5;
		sideThingys[0].points[5].x+=p*0.5;
		
		
		sideThingys[2].points[0].y+=p*0.5;
		sideThingys[2].points[1].y+=p*0.5;
		sideThingys[2].points[4].y+=p*0.5;
		sideThingys[2].points[5].y+=p*0.5;
		sideThingys[2].points[0].x+=p*0.5;
		sideThingys[2].points[1].x+=p*0.5;
		sideThingys[2].points[4].x+=p*0.5;
		sideThingys[2].points[5].x+=p*0.5;
		
		
		buff.importComplexCube(sideThingys);
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].translate(-p*2.5F, 0, 0);
		sideThingys[0].points[0].x-=p;
		sideThingys[0].points[1].x-=p;
		sideThingys[0].points[4].x-=p;
		sideThingys[0].points[5].x-=p;
		sideThingys[2].points[0].x-=p;
		sideThingys[2].points[1].x-=p;
		sideThingys[2].points[4].x-=p;
		sideThingys[2].points[5].x-=p;
		buff.importComplexCube(sideThingys);
		VertixModel sideThingysModel2=buff.exportToNoramlisedVertixBufferModel();
		sideThingysModel2.glStateCell=new GlStateCell(
			new GlState(new int[]{GL11.GL_TEXTURE_2D}, new int[]{}, ()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/iron_strip.png"));
			})
			, null);
		
		
		
		
		ComplexCubeModel core1=new ComplexCubeModel(p*7F, p*7F, p*13F, p*9F, p*9F, 1,new QuadUV[]{
			coreUV.rotate(),coreUV.mirror().rotate(),coreUV,coreUV,QuadUV.all(),coreUV
		},null);
		
		core1.willSideRender[5]=core1.willSideRender[4]=false;
		buff.importComplexCube(core1);
		VertixModel upModelCore1=buff.exportToNoramlisedVertixBufferModel();
		upModelCore1.glStateCell=new GlStateCell(new GlState(()->{TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe.png"));}), null);
		
		for(int i=0;i<core1.willSideRender.length;i++)core1.willSideRender[i]=i==4;
		buff.importComplexCube(core1);
		VertixModel upModelCore2=buff.exportToNoramlisedVertixBufferModel();
		upModelCore2.glStateCell=new GlStateCell(new GlState(()->{TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe_end.png"));}), null);
		
		
		
		buff.importComplexCube(new ComplexCubeModel(p*7F, p*7F, p*13F, p*9F, p*9F, 1));
		VertixModel downModel=buff.exportToNoramlisedVertixBufferModel();
		
		
		connections=new SidedModel(
			new DoubleObject<VertixModel[],int[]>(
				new VertixModel[]{
					horisontalConnectionToBrain,
					sideThingysModel,
					horisontalCore1,
					horisontalCore2
				},
				new int[]{
					2,
					3,
					4,
					5
				}
			),
			new DoubleObject<VertixModel[],int[]>(
				new VertixModel[]{
						upModelCore1,
						upModelCore2,
						sideThingysModel2,
						horisontalConnectionToBrain
				},
				new int[]{
					0,1
				}
			)
//			,
//			new DoubleObject<NormalizedVertixBufferModel[],int[]>(
//				new NormalizedVertixBufferModel[]{
//					upModelCore1,
//					upModelCore2,
//					sideThingysModel2,
//					horisontalConnectionToBrain
//				},
//				new int[]{
//					1
//				})
			);
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		TessUtil.drawLine(p*5.05, p*6,p*5.05, p*6.05, p*10, p*6.05, p/4, false, buff, 0, 10);
		TessUtil.drawLine(p*10.95,p*6,p*5.05, p*9.95, p*10, p*6.05, p/4, false, buff, 0, 10);
		
		TessUtil.drawLine(p*5.05, p*6,p*10.95, p*6.05, p*10, p*9.95, p/4, false, buff, 0, 10);
		TessUtil.drawLine(p*10.95,p*6,p*10.95, p*9.95, p*10, p*9.95, p/4, false, buff, 0, 10);
		
		buff.importComplexCube(new ComplexCubeModel(p*5, p*5, p*5, p*11, p*6, p*11),new ComplexCubeModel(p*6, p*10, p*6, p*10, p*10.5F, p*10));
		
		body1=buff.exportToNoramlisedVertixBufferModel();
		body1.glStateCell=new GlStateCell(new GlState(()->{
			GL11.glColor3b((byte)0, (byte)12, (byte)26);
			GL11U.texture(false);
		}), new GlState(()->{
			GL11U.texture(true);
			ColorF.WHITE.bind();
		}));
		
		ComplexCubeModel glass=new ComplexCubeModel(p*6.5F, p*6, p*6.5F, p*9.5F, p*10, p*9.5F);
		glass.willSideRender[2]=false;
		glass.willSideRender[3]=false;
		for(int i=0;i<5;i++){
			glass.expand(-0.004F,0,0.004F);
			buff.importComplexCube(glass);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		body2=buff.exportToNoramlisedVertixBufferModel();
		body2.glStateCell=new GlStateCell(new GlState(()->{
			GL11U.setUpOpaqueRendering(1);
			glassColor.bind();
		}), new GlState(()->{
			ColorF.WHITE.bind();
			GL11U.endOpaqueRendering();
			GL11U.texture(true);
		}));
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile0, double x, double y, double z, float pt){
//		initModels();
		GL11U.protect();
		GL11.glTranslated(x,y,z);
		
		
		boolean[] sides=new boolean[6];
		for(int i=0;i<6;i++)sides[i]=((TileEntityNetworkController)tile0).connections[i].getMain();
		connections.draw(sides);
		body1.draw();
		animate(tile0, x, y, z);
		body2.draw();
		GL11U.endProtection();
	}
	
	private void animate(TileEntity tile0, double x, double y, double z){
		TileEntityNetworkController tile=(TileEntityNetworkController)tile0;
		double tim=tile.getWorld().getTotalWorldTime()+(x+y+z)*30;
		float anim=(UtilM.calculatePos(tim-1, tim)/3)%360;
		
		double upDown=Math.sin(Math.toRadians(anim)*4)*UtilM.p/2;
		
		VertixBuffer brainModel=TessUtil.drawBrain(new Vec3M(0.5, 0.5+upDown, 0.5), 1, anim);
		
		Vec3 middle=brainModel.getTriangle(0).pos3[2].vector3D, minY=middle,maxY=middle;
		
		
		for(ShadedTriangle tri:brainModel.getTriangles())for(int i=0;i<3;i++){
			Vec3 newPos=tri.pos3[i].vector3D;
			if(newPos.yCoord>maxY.yCoord)maxY=newPos;
			if(newPos.yCoord<minY.yCoord)minY=newPos;
		}
		brainModel.draw();
		
		VertixBuffer buff=TessUtil.getVB();
		GL11U.texture(false);
		TessUtil.drawLine(minY.xCoord+UtilM.CRandF(minY.yCoord-p*6)/2, p*6,   minY.zCoord+UtilM.CRandF(minY.yCoord-p*6)/2, minY.xCoord, minY.yCoord+0.01, minY.zCoord, p/8, true, buff, 0, 10);
		TessUtil.drawLine(maxY.xCoord+UtilM.CRandF(p*10-maxY.yCoord)/2, p*10,  maxY.zCoord+UtilM.CRandF(p*10-maxY.yCoord)/2, maxY.xCoord, maxY.yCoord-0.01, maxY.zCoord, p/8, true, buff, 0, 10);
		GL11.glColor3b((byte)0, (byte)12, (byte)26);
		buff.draw();
	}
}
