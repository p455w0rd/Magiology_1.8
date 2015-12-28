package com.magiology.client.render.tilerender.network;

import net.minecraft.tileentity.*;
import net.minecraft.util.*;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

import com.magiology.api.connection.*;
import com.magiology.api.network.*;
import com.magiology.forgepowered.events.client.*;
import com.magiology.mcobjects.tileentityes.corecomponents.*;
import com.magiology.mcobjects.tileentityes.network.*;
import com.magiology.util.renderers.*;
import com.magiology.util.renderers.glstates.*;
import com.magiology.util.renderers.tessellatorscripts.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.math.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.m_extension.*;import com.magiology.util.utilobjects.vectors.*;


public class RenderNetworkInterface extends RenderNetworkConductor{
	
	protected SidedModel interfacePlate;
	private TileEntityNetworkInterface curentTile;
	
	public RenderNetworkInterface(){
		super();
	}
	@Override
	protected void initModels(){
		super.initModels();
		VertixBuffer buff=TessUtil.getVB();
		
		buff.importComplexCube(new ComplexCubeModel(p*6,p*6,0,p*10,p*10,p*2));
		VertixModel centerCube=buff.exportToNoramlisedVertixBufferModel();
		centerCube.glStateCell=new GlStateCell(new GlState(new int[]{}, new int[]{GL11.GL_TEXTURE_2D},()->{GL11.glColor3f(0.1F, 0.1F, 0.1F);}), null);
		
		
		for(int i=0;i<6;i++){
			int angle=i*60;
			double 
				sin1=Math.sin(Math.toRadians(angle)),
				sin2=Math.sin(Math.toRadians(angle+60)),
				cos1=Math.cos(Math.toRadians(angle)),
				cos2=Math.cos(Math.toRadians(angle+60)),
				
				xIn1=sin1*p*5+0.5,
				xOut1=sin1*p*6+0.5,
				xIn2=sin2*p*5+0.5,
				xOut2=sin2*p*6+0.5,
				
				yIn1=cos1*p*5+0.5,
				yOut1=cos1*p*6+0.5,
				yIn2=cos2*p*5+0.5,
				yOut2=cos2*p*6+0.5;
			
			buff.addVertex(xIn1,  yIn1,  0);
			buff.addVertex(xOut1, yOut1, 0);
			buff.addVertex(xOut2, yOut2, 0);
			buff.addVertex(xIn2,  yIn2,  0);
			
			buff.addVertex(xOut1, yOut1, p/2);
			buff.addVertex(xIn1,  yIn1,  p/2);
			buff.addVertex(xIn2,  yIn2,  p/2);
			buff.addVertex(xOut2, yOut2, p/2);
			
			buff.addVertex(xOut2, yOut2, 0);
			buff.addVertex(xOut1, yOut1, 0);
			buff.addVertex(xOut1, yOut1, p/2);
			buff.addVertex(xOut2, yOut2, p/2);

			buff.addVertex(xIn1,  yIn1,  0);
			buff.addVertex(xIn2,  yIn2,  0);
			buff.addVertex(xIn2,  yIn2,  p/2);
			buff.addVertex(xIn1,  yIn1,  p/2);
		};
		VertixModel plate=buff.exportToNoramlisedVertixBufferModel();
		plate.glStateCell=new GlStateCell(new GlState(()->{

			long offset=curentTile.x()*7-curentTile.y()*15+curentTile.z()*9;
			GL11.glColor3f(UtilM.fluctuatorSmooth(80, offset), UtilM.fluctuatorSmooth(134, 40+offset), UtilM.fluctuatorSmooth(156, 56+offset));
			
			if(curentTile.getBrain()!=null){
				VertixModel model=interfacePlate.compiledModels[interfacePlate.curentSide][1];
				GL11.glPushMatrix();
				Vec3M rotation=null,translation=null;
				
				switch (interfacePlate.curentSide){
				case 0:rotation=new Vec3M( 90,   0, 0);translation=new Vec3M( 0.5,  0.5, 0.5);break;
				case 1:rotation=new Vec3M(-90,   0, 0);translation=new Vec3M( 0.5, -0.5, 0.5);break;
				case 2:rotation=new Vec3M(  0, 180, 0);translation=new Vec3M(-0.5,  0.5, 0.5);break;
				case 3:rotation=new Vec3M(  0,   0, 0);translation=new Vec3M( 0.5,  0.5, 0.5);break;
				case 4:rotation=new Vec3M(  0, -90, 0);translation=new Vec3M( 0.5,  0.5, 0.5);break;
				case 5:rotation=new Vec3M(  0,  90, 0);translation=new Vec3M(-0.5,  0.5, 0.5);break;
				}
				
				GL11U.glRotate(rotation);
				GL11.glTranslated(translation.x,translation.y,translation.z);
				GL11U.glRotate(0,0,UtilM.getTheWorld().getTotalWorldTime()+RenderLoopEvents.partialTicks);
				GL11.glTranslated(-translation.x,-translation.y,-translation.z);
				GL11.glTranslatef(0, 0, (float) (Math.sin((UtilM.getTheWorld().getTotalWorldTime()+RenderLoopEvents.partialTicks)/10+offset)*p/2+p/2));
				GL11U.glRotate((Vec3M)rotation.negate());
			}
			
		}), new GlState(new int[]{GL11.GL_TEXTURE_2D}, new int[]{},()->{
			if(curentTile.getBrain()!=null)GL11.glPopMatrix();
		}));
		
		interfacePlate=new SidedModel(new DoubleObject<VertixModel[], int[]>(
			new VertixModel[]{
					centerCube,
					plate
			},
			new int[]{
				0,
				1,
				2,
				3,
				4,
				5
			}
		));
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float pt){
		curentTile=(TileEntityNetworkInterface)tile;
//		initModels();
		rednerNetworkPipe(curentTile, x, y, z);
		
	}
	@Override
	protected <NetworkComponent extends IConnectionProvider & NetworkBaseComponent> void rednerNetworkPipe(NetworkComponent networkComponent, double x, double y, double z) {
		super.rednerNetworkPipe(networkComponent, x, y, z);
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		int side=((TileEntityNetworkInterface)networkComponent).getOrientation();
		boolean[] sides=new boolean[6];
		for(int i=0;i<6;i++)sides[i]=side==i;
		interfacePlate.draw(sides);
		GL11.glPopMatrix();
		GL11U.glColor(ColorF.WHITE);
	}
}
