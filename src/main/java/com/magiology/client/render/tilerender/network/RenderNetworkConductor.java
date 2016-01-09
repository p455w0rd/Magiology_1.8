package com.magiology.client.render.tilerender.network;

import net.minecraft.tileentity.*;
import net.minecraft.util.*;

import org.lwjgl.opengl.*;

import com.magiology.api.connection.*;
import com.magiology.api.network.*;
import com.magiology.api.network.skeleton.*;
import com.magiology.core.*;
import com.magiology.mcobjects.tileentityes.network.*;
import com.magiology.util.renderers.*;
import com.magiology.util.renderers.glstates.*;
import com.magiology.util.renderers.tessellatorscripts.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.m_extension.*;
import com.magiology.util.utilobjects.vectors.*;

public class RenderNetworkConductor extends TileEntitySpecialRendererM{
	
	protected static SidedModel connections,strateConnections;
	protected static VertexModel body1,body2[]=new VertexModel[8];
	
	public RenderNetworkConductor(){
		initModels();
	}
	
	protected void initModels(){
		VertixBuffer buff=TessUtil.getVB();
		
		QuadUV all=QuadUV.all().rotate1();
		ComplexCubeModel[] sideThingys={
				new ComplexCubeModel(p*6.5F, p*6.5F, p*9F, p*7F, p*7F, 1, new QuadUV[]{all.rotate1(),all.rotate1(),all,all,all,all},null),
				new ComplexCubeModel(p*6.5F, p*9F, p*9F, p*7F, p*9.5F, 1, new QuadUV[]{all.rotate1(),all.rotate1(),all,all,all,all},null)
		};
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].willSideRender[4]=sideThingys[i].willSideRender[5]=false;
		buff.importComplexCube(sideThingys);
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].translate(p*2.5F, 0, 0);
		buff.importComplexCube(sideThingys);
		
		VertexModel sideThingysModel1=buff.exportToNoramlisedVertixBufferModel();
		sideThingysModel1.glStateCell=new GlStateCell(
			new GlState(new int[]{GL11.GL_TEXTURE_2D}, new int[]{}, ()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/iron_strip.png"));
			})
		, null);
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].translate(-p*2.5F, 0, 0);
		
		for(int i=0;i<sideThingys.length;i++){
			sideThingys[i].UVs[2]=sideThingys[i].UVs[2].rotate1().rotate1().mirror1();
			sideThingys[i].UVs[3]=sideThingys[i].UVs[3].rotate1().rotate1().mirror1();
		}
		
		buff.importComplexCube(sideThingys);
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].translate(p*2.5F, 0, 0);
		buff.importComplexCube(sideThingys);
		
		VertexModel sideThingysModel2=buff.exportToNoramlisedVertixBufferModel();
		sideThingysModel2.glStateCell=new GlStateCell(
				new GlState(new int[]{GL11.GL_TEXTURE_2D}, new int[]{}, ()->{
					TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/iron_strip.png"));
				})
				, null);
		
		
		float w1=1F/216F;
		QuadUV coreUV=new QuadUV(
				w1*24,1,
				w1*24,0,
				w1*108,0,
				w1*108,1
			).rotate1();
		ComplexCubeModel core=new ComplexCubeModel(p*7F, p*7F, p*9F, p*9F, p*9F, 1,new QuadUV[]{
			coreUV.rotate1(),coreUV.mirror1().rotate1(),coreUV,coreUV,QuadUV.all(),coreUV
		},null);
		core.willSideRender[5]=core.willSideRender[4]=false;
		
		buff.importComplexCube(core);
		
		VertexModel horisontalCore1=buff.exportToNoramlisedVertixBufferModel();
		horisontalCore1.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe.png"));
			})
		, null);

		coreUV=new QuadUV(
			w1*132,1,
			w1*132,0,
			1,0,
			1,1
		).rotate1().mirror1();
		core=new ComplexCubeModel(p*7F, p*7F, p*9F, p*9F, p*9F, 1,new QuadUV[]{
			coreUV.rotate1(),coreUV.mirror1().rotate1(),coreUV,coreUV,QuadUV.all(),coreUV
		},null);
		core.willSideRender[5]=core.willSideRender[4]=false;
		buff.importComplexCube(core);
		
		VertexModel horisontalCore2=buff.exportToNoramlisedVertixBufferModel();
		horisontalCore2.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe.png"));
			})
		, null);
		connections=new SidedModel(
			new DoubleObject<VertexModel[],int[]>(
				new VertexModel[]{
						sideThingysModel1,
						horisontalCore1
				},
				new int[]{
					1,
					3,
					5
				}
			),
			new DoubleObject<VertexModel[],int[]>(
				new VertexModel[]{
						sideThingysModel2,
						horisontalCore2
				},
				new int[]{
					0,
					2,
					4
				}
			)
		);
		
		

		coreUV=new QuadUV(
			w1*24,1,
			w1*24,0,
			1,0,
			1,1
		).rotate1();
		core=new ComplexCubeModel(p*7F, p*7F, 0, p*9F, p*9F, 1,new QuadUV[]{
			coreUV.rotate1(),coreUV.mirror1().rotate1(),coreUV,coreUV,QuadUV.all(),coreUV
		},null);
		core.willSideRender[5]=core.willSideRender[4]=false;
		buff.importComplexCube(core);
		
		VertexModel coreLong=buff.exportToNoramlisedVertixBufferModel();
		coreLong.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe.png"));
			})
		, null);
		
		
		ComplexCubeModel[] sideThingys1={
				new ComplexCubeModel(p*6.5F, p*6.5F, 0, p*7F, p*7F,   1, new QuadUV[]{all.rotate1(),all.rotate1(),all,all,all,all},null),
				new ComplexCubeModel(p*6.5F, p*9F,   0, p*7F, p*9.5F, 1, new QuadUV[]{all.rotate1(),all.rotate1(),all,all,all,all},null)
		};
		for(int i=0;i<sideThingys1.length;i++)sideThingys1[i].willSideRender[4]=sideThingys1[i].willSideRender[5]=false;
		buff.importComplexCube(sideThingys1);
		for(int i=0;i<sideThingys1.length;i++)sideThingys1[i].translate(p*2.5F, 0, 0);
		buff.importComplexCube(sideThingys1);
		
		VertexModel sideThingysModelLong=buff.exportToNoramlisedVertixBufferModel();
		sideThingysModelLong.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/iron_strip.png"));
			})
		, null);
		
		
		strateConnections=new SidedModel(
				new DoubleObject<VertexModel[],int[]>(
					new VertexModel[]{
							sideThingysModelLong,
							coreLong
					},
					new int[]{
						1,
						3,
						5
					}
				)
			);
		
		
		
		
		ComplexCubeModel cube=new ComplexCubeModel(p*7F, p*7F, p*7F, p*9F, p*9F, p*9F,new QuadUV[]{new QuadUV(
				w1*24,1,
				w1*24,0,
				0,0,
				0,1
			)},null);
		buff.importComplexCube(cube);

		body1=buff.exportToNoramlisedVertixBufferModel();
		body1.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe.png"));
			})
		, null);
		
		int i=0;
		ComplexCubeModel cube1=new ComplexCubeModel(p*8.99F, p*8.99F, p*8.99F, p*9.51F, p*9.51F, p*9.51F,new QuadUV[]{QuadUV.all()},null);
		
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNoramlisedVertixBufferModel();
		cube1.translate(-p*2.5F, 0, 0);
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNoramlisedVertixBufferModel();
		cube1.translate(0, -p*2.5F, 0);
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNoramlisedVertixBufferModel();
		cube1.translate(p*2.5F, 0, 0);
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNoramlisedVertixBufferModel();
		cube1.translate(0, 0, -p*2.5F);
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNoramlisedVertixBufferModel();
		cube1.translate(-p*2.5F, 0, 0);
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNoramlisedVertixBufferModel();
		cube1.translate(0, p*2.5F, 0);
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNoramlisedVertixBufferModel();
		cube1.translate(p*2.5F, 0, 0);
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNoramlisedVertixBufferModel();
		
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks){
		rednerNetworkPipe((TileEntityNetworkConductor)tile, x, y, z);
	}
	protected <NetworkComponent extends IConnectionProvider&NetworkBaseComponent> void rednerNetworkPipe(NetworkComponent networkComponent, double x, double y, double z){
		GL11U.protect();
		GL11U.texture(true);
		GL11.glTranslated(x,y,z);
		
		boolean[] strateSides=new boolean[6];
		strateSides[1]=networkComponent.isStrate(EnumFacing.UP);
		strateSides[3]=networkComponent.isStrate(EnumFacing.SOUTH);
		strateSides[5]=networkComponent.isStrate(EnumFacing.WEST);
		
		if(networkComponent.getBrain()==null)GL11U.glColor(ColorF.GRAY);
		
		if(strateSides[1]||strateSides[3]||strateSides[5])strateConnections.draw(strateSides);
		else{
			boolean[] sides=new boolean[6];
			for(int i=0;i<6;i++)sides[i]=networkComponent.getConnections()[i].getMain();
			
			connections.draw(sides);
			body1.draw();
			
			TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/iron_strip_end.png"));
			for(int i=0;i<body2.length;i++)switch(i){
			case 0:if(          sides[1]          ||sides[3]          ||sides[5])body2[i].draw();break;
			case 1:if(          sides[1]          ||sides[3]||sides[4]          )body2[i].draw();break;
			case 2:if(sides[0]                    ||sides[3]||sides[4]          )body2[i].draw();break;
			case 3:if(sides[0]                    ||sides[3]          ||sides[5])body2[i].draw();break;
			case 4:if(sides[0]          ||sides[2]                    ||sides[5])body2[i].draw();break;
			case 5:if(sides[0]          ||sides[2]          ||sides[4]          )body2[i].draw();break;
			case 6:if(          sides[1]||sides[2]          ||sides[4]          )body2[i].draw();break;
			case 7:if(          sides[1]||sides[2]                    ||sides[5])body2[i].draw();break;
			}
		}
		
		GL11U.endProtection();
	}
}
