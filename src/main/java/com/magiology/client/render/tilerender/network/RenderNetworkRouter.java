package com.magiology.client.render.tilerender.network;

import net.minecraft.tileentity.*;
import net.minecraft.util.*;




import org.apache.commons.lang3.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;




import com.magiology.api.network.*;
import com.magiology.core.*;
import com.magiology.forgepowered.events.client.*;
import com.magiology.mcobjects.effect.*;
import com.magiology.mcobjects.tileentityes.corecomponents.*;
import com.magiology.mcobjects.tileentityes.network.*;
import com.magiology.util.renderers.*;
import com.magiology.util.renderers.glstates.*;
import com.magiology.util.renderers.tessellatorscripts.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.m_extension.*;
import com.magiology.util.utilobjects.vectors.*;

public class RenderNetworkRouter extends TileEntitySpecialRendererM{
	
	protected static SidedModel connections,core;
	protected static OpenGlModel lever,usb;
	protected static TileEntityNetworkRouter curentTile;
	protected static LinearAnimation insertionAnimation=new LinearAnimation(
		new DoubleObject<Vec3M[], Float>(new Vec3M[]{new Vec3M(-8,1,0)},0F),
		new DoubleObject<Vec3M[], Float>(new Vec3M[]{new Vec3M( 0,1,0)},0.4F),
		new DoubleObject<Vec3M[], Float>(new Vec3M[]{new Vec3M( 0,0,0)},1F)
	);
	
	
	public RenderNetworkRouter(){
		initModels();
	}
	
	protected void initModels(){
		VertixBuffer buff=TessUtil.getVB();
		
		QuadUV all=QuadUV.all().rotate();
		{
			ComplexCubeModel[] sideThingys={
					new ComplexCubeModel(p*6.5F, p*6.5F, p*11F, p*7F, p*7F, 1, new QuadUV[]{all.rotate(),all.rotate(),all,all,all,all},null),
					new ComplexCubeModel(p*6.5F, p*9F, p*11F, p*7F, p*9.5F, 1, new QuadUV[]{all.rotate(),all.rotate(),all,all,all,all},null)
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
				sideThingys[i].UVs[2]=sideThingys[i].UVs[2].rotate().rotate().mirror();
				sideThingys[i].UVs[3]=sideThingys[i].UVs[3].rotate().rotate().mirror();
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
			
			
			float w1=1F/216F,offset=24;
			QuadUV coreUV=new QuadUV(
					w1*24,1,
					w1*24,0,
					w1*84,0,
					w1*84,1
				).rotate();
			ComplexCubeModel core=new ComplexCubeModel(p*7F, p*7F, p*11F, p*9F, p*9F, 1,new QuadUV[]{
				coreUV.rotate(),coreUV.mirror().rotate(),coreUV,coreUV,QuadUV.all(),coreUV
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
				w1*156,1,
				w1*156,0,
				1,0,
				1,1
			).rotate().mirror();
			core.UVs=new QuadUV[]{
				coreUV.rotate(),coreUV.mirror().rotate(),coreUV,coreUV,QuadUV.all(),coreUV
			};
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
		}
		ComplexCubeModel 
			cubes[]={
					new ComplexCubeModel(p*5.5F, p*5.5F, p*10F, p*10.5F, p*10.5F, p*10.5F),
					new ComplexCubeModel(p*5.5F, p*5.5F, p*10.5F, p*10.5F, p*10.5F, p*11F)
			},
			leverBoxes[]={
					new ComplexCubeModel(p*9.75F, p*9.25F, p*6F, p*10.25F, p*9.5F, p*6.75F),
					new ComplexCubeModel(p*9.875F, p*9F, p*6.25F, p*10.125F, p*9.25F, p*8.5F),
					new ComplexCubeModel(p*9.875F, p*9F, p*8.5F, p*10.125F, p*9.25F, p*10F)
			},
			plug=new ComplexCubeModel(p*9.75F, p*9.75F, p*9F, p*10.25F, p*10.25F, p*10F);
		
		cubes[1].points[2]=cubes[1].points[2].addVector(-p, -p, 0);
		cubes[1].points[3]=cubes[1].points[3].addVector(-p, p, 0);
		cubes[1].points[6]=cubes[1].points[6].addVector(p, -p, 0);
		cubes[1].points[7]=cubes[1].points[7].addVector(p, p, 0);
		buff.importComplexCube(cubes);
		
		leverBoxes[1].willSideRender[5]=
		leverBoxes[2].willSideRender[5]=
		leverBoxes[2].willSideRender[4]=
		plug.willSideRender[5]=false;
		int id=7;
		leverBoxes[2].points[2]=leverBoxes[2].points[2].addVector(0,p/2,0);
		leverBoxes[2].points[3]=leverBoxes[2].points[3].addVector(0,p/2,0);
		leverBoxes[2].points[6]=leverBoxes[2].points[6].addVector(0,p/2,0);
		leverBoxes[2].points[id]=leverBoxes[2].points[id].addVector(0,p/2,0);
		
		for(int i=0;i<3;i++)for(int j=0;j<3;j++){
			plug.translate(-i*p*2, -j*p*2, 0);
			buff.importComplexCube(plug);
			plug.translate(i*p*2, j*p*2, 0);
		}
		
		VertexModel core=buff.exportToNoramlisedVertixBufferModel();
		core.glStateCell=new GlStateCell(new GlState(new int[]{}, new int[]{GL11.GL_TEXTURE_2D},()->{
			
			for(int i=0;i<3;i++)for(int j=0;j<3;j++){
				Vec3M rots=insertionAnimation.get(curentTile.animationos[j+i*3].get())[0];
				GL11.glPushMatrix();
				Vec3M rot=this.core.rotations[this.core.getCurentSide()];
				GL11U.glRotate(rot,0.5F,0.5F,0.5F);
				GL11.glTranslatef(-i*p*2, -j*p*2, 0);
				
				GL11.glPushMatrix();
				GL11U.glRotate(0, rots.y*90, 0);
				GL11.glColor3f(0, 0.125F, 0.125F);
				usb.draw();
				GL11.glColor3f(0, 0, 0.125F);
				
				GL11.glPopMatrix();
				
				GL11U.glRotate(rots.x,0,0, p*10.125F, p*9.75F, p*10F);
				lever.draw();
				GL11.glPopMatrix();
			}
			GL11.glColor3f(0.125F, 0.125F, 0.125F);
		}),new GlState(new int[]{GL11.GL_TEXTURE_2D},new int[]{},()->{GL11.glColor3f(1,1,1);}));
		
		buff.importComplexCube(leverBoxes);
		lever=new OpenGlModel(buff.exportToNoramlisedVertixBufferModel());
		
		ComplexCubeModel[] usbModel={
				new ComplexCubeModel(p*9.5F, p*9.5F, p*4.8F, p*10.5F, p*10.5F, p*9)
		};
		
		buff.importComplexCube(usbModel);
		
		usb=new OpenGlModel(buff.exportToNoramlisedVertixBufferModel());
		
		
		
		this.core=new SidedModel(
			new DoubleObject<VertexModel[],int[]>(
				new VertexModel[]{
					core
				},
				new int[]{
					1,
					3,
					5,
					0,
					2,
					4
				}
			)
		);
	}
	
	
	
	@Override
	public void renderTileEntityAt(TileEntity tile0, double x, double y, double z, float pt){
		initModels();
		
		
		TileEntityNetworkRouter tile=curentTile=(TileEntityNetworkRouter)tile0;
		boolean[] connectionsSides=new boolean[6],coreSides=new boolean[6];
		int or=SideUtil.getOppositeSide(tile.getOrientation());
		for(int i=0;i<6;i++)connectionsSides[i]=(coreSides[i]=i==or)&&tile.getConnections()[i].getMain();
		
		
		GL11.glPushMatrix();
		GL11.glTranslated(x,y,z);
		
		
		GL11U.texture(false);
		
		connections.draw(connectionsSides);
		core.draw(coreSides);
		
		GL11U.texture(true);
		
		GL11.glPopMatrix();
	}

}
