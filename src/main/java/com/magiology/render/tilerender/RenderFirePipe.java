package com.magiology.render.tilerender;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;

import com.magiology.core.init.MItems;
import com.magiology.forgepowered.event.RenderLoopEvents;
import com.magiology.mcobjects.tileentityes.TileEntityFirePipe;
import com.magiology.objhelper.getters.RenderGet;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.renderers.NoramlisedVertixBuffer;
import com.magiology.objhelper.helpers.renderers.NoramlisedVertixBufferModel;
import com.magiology.objhelper.helpers.renderers.TessHelper;
import com.magiology.render.Textures;
import com.magiology.render.aftereffect.LongAfterRenderRenderer;
import com.magiology.render.aftereffect.RenderFirePipeGlow;
import com.magiology.render.aftereffect.RenderFirePipePriorityCube;

public class RenderFirePipe extends TileEntitySpecialRenderer {
	private final float p= 1F/16F;
	private final float tW=1F/131F;
	private final float tH=1F/16F;
	private final float tWC=1F/4F;
	private final float tHC=1F/40F;
	private final float tWFSL=1F/62F;
	private final float tHFSL=1F/38F;
	private final float tWS=1F/16F;
	private final float tHS=1F/32F;
	NoramlisedVertixBuffer buf=TessHelper.getNVB();
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f,int pass){
		TileEntityFirePipe pipe= (TileEntityFirePipe) tileentity;
		if(Helper.isItemInStack(MItems.FireHammer, Helper.getThePlayer().getCurrentEquippedItem())){
			if(pipe.hasPriorityUpg){
				boolean var1=true;
				for(int a=0;a<RenderLoopEvents.universalLongRender.size();a++){
					LongAfterRenderRenderer ab=RenderLoopEvents.universalLongRender.get(a);
					if(ab instanceof RenderFirePipePriorityCube&&!((RenderFirePipePriorityCube)ab).isDead())if(((RenderFirePipePriorityCube)ab).pipe==pipe)var1=false;
				}
				if(var1&&pipe.FirstSide>=0){
					int a=0;
					switch (pipe.FirstSide){
					case 0:a=4;break;
					case 1:a=1;break;
					case 2:a=2;break;
					case 3:a=3;break;
					case 4:a=5;break;
					case 5:a=0;break;
					}
					AxisAlignedBB b=pipe.collisionBoxes[a];
					RenderLoopEvents.spawnLARR(new RenderFirePipePriorityCube(pipe, pipe.getPos(), b));
				}
			}
			boolean var2=true;
			for(int a=0;a<RenderLoopEvents.universalLongRender.size();a++){
				LongAfterRenderRenderer ab=RenderLoopEvents.universalLongRender.get(a);
				if(ab instanceof RenderFirePipeGlow&&!((RenderFirePipeGlow)ab).isDead()&&((RenderFirePipeGlow)ab).pipe==pipe)var2=false;
			}
			if(var2)RenderLoopEvents.spawnLARR(new RenderFirePipeGlow(pipe));
		}
		RenderGet.WR().setTranslation(pipe.getPos().getX(), pipe.getPos().getY(), pipe.getPos().getZ());
		
		
		if(pipe.DCFFL!=null)drawConectorFFL();
		for(int i=0; i< pipe.connectionsToObjInMe.length; i++){
			if(pipe.shouldConnectionsBeRendered[i]){
				if(pipe.connectionsToObjInMe[i]!= null)drawConectionToObj(pipe.connectionsToObjInMe[i],"outOfMe");
				else if(pipe.connectionsToObjOut[i]!= null)drawConectionToObj(pipe.connectionsToObjOut[i],"inMe");
			}
		}
		
		if(pipe.strateConnection[0]==null&&pipe.strateConnection[1]==null&&pipe.strateConnection[2]==null){
			drawCore(pipe.texAnim);
			for(int i=0; i< pipe.connections.length; i++)if(pipe.connections[i]!=null&&pipe.shouldConnectionsBeRendered[i])drawConector(pipe.connections[i]);
			if(pipe.isSolidDown==true)drawStand(EnumFacing.DOWN);
			else if(pipe.isSolidUp==true)drawStand(EnumFacing.UP);
		}
		else for(int a=0;a<pipe.strateConnection.length;a++)if(pipe.strateConnection[a]!=null)drawStrateCore(pipe.strateConnection[a]);
		
		RenderGet.WR().setTranslation(0,0,0);
	}
	
	
	private NoramlisedVertixBufferModel modelStand;
	private void generateModelStand(){
		buf.cleanUp();
		buf.addVertexWithUV(p*7.5,  p*6, p*8.5,  tWS*0, tHS*0);
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*0, tHS*24);
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*8, tHS*24);
		buf.addVertexWithUV(p*8.5,  p*6, p*8.5,  tWS*8, tHS*0);

		buf.addVertexWithUV(p*8.5,  p*6, p*7.5,  tWS*0, tHS*0);
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*0, tHS*24);
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*8, tHS*24);
		buf.addVertexWithUV(p*7.5,  p*6, p*7.5,  tWS*8, tHS*0);

		buf.addVertexWithUV(p*7.5,  p*6, p*7.5,  tWS*0, tHS*0);
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*0, tHS*24);
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*8, tHS*24);
		buf.addVertexWithUV(p*7.5,  p*6, p*8.5,  tWS*8, tHS*0);
		
		buf.addVertexWithUV(p*8.5,  p*6, p*8.5,  tWS*0, tHS*0);
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*0, tHS*24);
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*8, tHS*24);
		buf.addVertexWithUV(p*8.5,  p*6, p*7.5,  tWS*8, tHS*0);
		
		
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*8,  tHS*0);
		buf.addVertexWithUV(p*7.5,  p*0, p*10.5, tWS*8,  tHS*32);
		buf.addVertexWithUV(p*8.5,  p*0, p*10.5, tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*16, tHS*0);

		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*8,  tHS*0);
		buf.addVertexWithUV(p*8.5,  p*0, p*9.5,  tWS*8,  tHS*32);
		buf.addVertexWithUV(p*7.5,  p*0, p*9.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*16, tHS*0);

		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*8,  tHS*0);
		buf.addVertexWithUV(p*8.5,  p*0, p*10.5, tWS*8,  tHS*32);
		buf.addVertexWithUV(p*8.5,  p*0, p*9.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*16, tHS*0);
	 	
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*7.5,  p*0, p*9.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*0, p*10.5, tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*16, tHS*0);
		
		
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*7.5,  p*0, p*6.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*0, p*6.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*16, tHS*0);
		
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*8.5,  p*0, p*5.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*0, p*5.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*16, tHS*0);
		
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*8.5,  p*0, p*6.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*0, p*5.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*16, tHS*0);
		
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*7.5,  p*0, p*5.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*0, p*6.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*16, tHS*0);
		
		
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*6.5,  p*0, p*8.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*6.5,  p*0, p*7.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*16, tHS*0);

		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*5.5,  p*0, p*7.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*5.5,  p*0, p*8.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*16, tHS*0);

		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*5.5,  p*0, p*8.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*6.5,  p*0, p*8.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*16, tHS*0);

		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*6.5,  p*0, p*7.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*5.5,  p*0, p*7.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*16, tHS*0);
		
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*10.5, p*0, p*8.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*10.5, p*0, p*7.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*16, tHS*0);
		
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*9.5,  p*0, p*7.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*9.5,  p*0, p*8.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*16, tHS*0);
		
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*9.5,  p*0, p*8.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*10.5, p*0, p*8.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*16, tHS*0);
		
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*10.5, p*0, p*7.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*9.5,  p*0, p*7.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*16, tHS*0);
		modelStand=buf.exportToNoramlisedVertixBufferModel();
	}
	private void drawStand(EnumFacing dir){
		if(modelStand==null)generateModelStand();
		modelStand.push();
		if(dir.equals(EnumFacing.UP))modelStand.rotateAt(0.5, 0.5, 0.5, 0, 0, 180);
		bindTexture(Textures.FirePipeConectionFF);
		modelStand.draw();
		modelStand.pop();
	}
	
	
	
	private NoramlisedVertixBufferModel[] conectionToObjModel=new NoramlisedVertixBufferModel[2];
	private void generateModelConectionToObj(){
		buf.cleanUp();
		buf.addVertexWithUV(p*0,   p*10, p*10,1, 0);
		buf.addVertexWithUV(p*0,   p*6,  p*10,1, 1);
		buf.addVertexWithUV(p*1.5, p*6,  p*10,0, 1);
		buf.addVertexWithUV(p*1.5, p*10, p*10,0, 0);

		buf.addVertexWithUV(p*1.5, p*10, p*6,0, 0);
		buf.addVertexWithUV(p*1.5, p*6,  p*6,0, 1);
		buf.addVertexWithUV(p*0,   p*6,  p*6,1, 1);
		buf.addVertexWithUV(p*0,   p*10, p*6,1, 0);
			
		buf.addVertexWithUV(p*0,   p*10, p*6,1, 0);
		buf.addVertexWithUV(p*0,   p*10, p*10,1, 1);
		buf.addVertexWithUV(p*1.5, p*10, p*10,0, 1);
		buf.addVertexWithUV(p*1.5, p*10, p*6,0, 0);
			
		buf.addVertexWithUV(p*1.5, p*6,  p*6,0, 0);
		buf.addVertexWithUV(p*1.5, p*6,  p*10,0, 1);
		buf.addVertexWithUV(p*0,   p*6,  p*10,1, 1);
		buf.addVertexWithUV(p*0,   p*6,  p*6,1, 0);
		conectionToObjModel[0]=buf.exportToNoramlisedVertixBufferModel();
		
		buf.addVertexWithUV(p*1.5, p*10, p*10, 1, 1);
		buf.addVertexWithUV(p*1.5, p*6,  p*10, 1, 0);
		buf.addVertexWithUV(p*1.5, p*6,  p*6,  0, 0);
		buf.addVertexWithUV(p*1.5, p*10, p*6,  0, 1);
		conectionToObjModel[1]=buf.exportToNoramlisedVertixBufferModel();
	}
	private void drawConectionToObj(EnumFacing dir, String type){
		if(conectionToObjModel[0]==null)generateModelConectionToObj();
		for(int i=0;i<2;i++){
			conectionToObjModel[i].push();
			     if(dir.equals(EnumFacing.UP))conectionToObjModel[i].rotateAt(0.5, 0.5, 0.5, 0, 0, -90);
			else if(dir.equals(EnumFacing.DOWN))conectionToObjModel[i].rotateAt(0.5, 0.5, 0.5, 0, 0, 90);
			else if(dir.equals(EnumFacing.SOUTH))conectionToObjModel[i].rotateAt(0.5, 0.5, 0.5, 0, 90, 0);
			else if(dir.equals(EnumFacing.EAST))conectionToObjModel[i].rotateAt(0.5, 0.5, 0.5, 0, -180, 0);
			else if(dir.equals(EnumFacing.NORTH))conectionToObjModel[i].rotateAt(0.5, 0.5, 0.5, 0, -90, 0);
		}
		
		if(type=="inMe")bindTexture(Textures.FirePipeConecterInMe);
		else if(type=="outOfMe")bindTexture(Textures.FirePipeConecterOutOfMe);
		conectionToObjModel[0].draw();
		bindTexture(Textures.FirePipeConecterBase);
		conectionToObjModel[1].draw();
		
		conectionToObjModel[0].pop();
		conectionToObjModel[1].pop();
	}
	private NoramlisedVertixBufferModel strateCoreModel;
	private void generateModelStrateCore(){
		buf.cleanUp();
		buf.addVertexWithUV(0, p*9.5, p*9.5,tW*103, tH*0);
		buf.addVertexWithUV(0, p*6.5, p*9.5,tW*103, tH*16);
		buf.addVertexWithUV(1, p*6.5, p*9.5,tW*28, tH*16);
		buf.addVertexWithUV(1, p*9.5, p*9.5,tW*28, tH*0);

		buf.addVertexWithUV(1, p*9.5, p*6.5,tW*28, tH*0);
		buf.addVertexWithUV(1, p*6.5, p*6.5,tW*28, tH*16);
		buf.addVertexWithUV(0, p*6.5, p*6.5,tW*103, tH*16);
		buf.addVertexWithUV(0, p*9.5, p*6.5,tW*103, tH*0);

		buf.addVertexWithUV(0, p*9.5, p*6.5,tW*103, tH*0);
		buf.addVertexWithUV(0, p*9.5, p*9.5,tW*103, tH*16);
		buf.addVertexWithUV(1, p*9.5, p*9.5,tW*28, tH*16);
		buf.addVertexWithUV(1, p*9.5, p*6.5,tW*28, tH*0);

		buf.addVertexWithUV(1, p*6.5, p*6.5,tW*28, tH*0);
		buf.addVertexWithUV(1, p*6.5, p*9.5,tW*28, tH*16);
		buf.addVertexWithUV(0, p*6.5, p*9.5,tW*103, tH*16);
		buf.addVertexWithUV(0, p*6.5, p*6.5,tW*103, tH*0);
		strateCoreModel=buf.exportToNoramlisedVertixBufferModel();
	}
	private void drawStrateCore(EnumFacing dir){
		if(strateCoreModel==null)generateModelStrateCore();
		strateCoreModel.push();
		if(dir.equals(EnumFacing.UP))strateCoreModel.rotateAt(0.5, 0.5, 0.5, 0, 0, -90);
		else if(dir.equals(EnumFacing.SOUTH))strateCoreModel.rotateAt(0.5, 0.5, 0.5, 0, 90, 0);
		bindTexture(Textures.FirePipeConection);
		strateCoreModel.draw();
		strateCoreModel.pop();
	}
	
	public void drawCore(int txAnim){
		bindTexture(Textures.firePipeCore);
		float teHC=tHC*txAnim*4;
		buf.push();
		buf.cleanUp();
		buf.addVertexWithUV(p*6, p*10, p*6, tWC*4, tHC*4-teHC);
		buf.addVertexWithUV(p*6, p*6,  p*6, tWC*4, tHC*0-teHC);
		buf.addVertexWithUV(p*6, p*6,  p*10,tWC*0, tHC*0-teHC);
		buf.addVertexWithUV(p*6, p*10, p*10,tWC*0, tHC*4-teHC);
		
		buf.addVertexWithUV(p*10, p*10, p*10, tWC*4, tHC*4-teHC);
		buf.addVertexWithUV(p*10, p*6,  p*10, tWC*4, tHC*0-teHC);
		buf.addVertexWithUV(p*10, p*6,  p*6, tWC*0, tHC*0-teHC);
		buf.addVertexWithUV(p*10, p*10, p*6, tWC*0, tHC*4-teHC);
		
		buf.addVertexWithUV(p*6, p*10, p*10, tWC*0, tHC*4-teHC);
		buf.addVertexWithUV(p*6, p*6 , p*10, tWC*0, tHC*0-teHC);
		buf.addVertexWithUV(p*10, p*6, p*10, tWC*4, tHC*0-teHC);
		buf.addVertexWithUV(p*10, p*10, p*10, tWC*4, tHC*4-teHC);
		
		buf.addVertexWithUV(p*10, p*10, p*6, tWC*4, tHC*4-teHC);
		buf.addVertexWithUV(p*10, p*6, p*6, tWC*4, tHC*0-teHC);
		buf.addVertexWithUV(p*6, p*6 , p*6, tWC*0, tHC*0-teHC);
		buf.addVertexWithUV(p*6, p*10, p*6, tWC*0, tHC*4-teHC);
		
		buf.addVertexWithUV(p*10, p*10, p*10, tWC*4, tHC*4-teHC);
		buf.addVertexWithUV(p*10, p*10, p*6, tWC*4, tHC*0-teHC);
		buf.addVertexWithUV(p*6, p*10, p*6, tWC*0, tHC*0-teHC);
		buf.addVertexWithUV(p*6, p*10, p*10, tWC*0, tHC*4-teHC);
		
		buf.addVertexWithUV(p*6, p*6, p*10, tWC*0, tHC*4-teHC);
		buf.addVertexWithUV(p*6, p*6, p*6, tWC*0, tHC*0-teHC);
		buf.addVertexWithUV(p*10, p*6, p*6, tWC*4, tHC*0-teHC);
		buf.addVertexWithUV(p*10, p*6, p*10, tWC*4, tHC*4-teHC);
		buf.draw();
		buf.pop();
	}
	public void drawConector(EnumFacing dir){
		buf.push();
		bindTexture(Textures.FirePipeConection);
		float rX=0,rY=0,rZ=0;
		int t=-1;
		double tw1=-1,tw2=-1,th1=-1,th2=-1;
		boolean[] flipTH=new boolean[5];
		
		if(dir.equals(EnumFacing.WEST)){t=1;}
		else if (dir.equals(EnumFacing.UP)){rZ=-90;t=1;}
		else if (dir.equals(EnumFacing.DOWN)){rZ=90;t=2;}//texture2
		else if (dir.equals(EnumFacing.SOUTH)){rY=90;t=1;}
		else if (dir.equals(EnumFacing.EAST)){rY=-180;t=3;}//texture2
		else if (dir.equals(EnumFacing.NORTH)){rY=-90;t=3;}//texture2
		
		switch(t){
		case(1):{th1=tH*16;th2=0;tw1=0;tw2=tW*28;}break;
		case(2):{th1=tH*16;th2=0;tw1=1;tw2=tW*103;flipTH[1]=true;flipTH[2]=true;}break;
		case(3):{th1=tH*16;th2=0;tw1=1;tw2=tW*103;flipTH[3]=true;flipTH[4]=true;}break;
		default:{th1=1;th2=0;tw1=1;tw2=0;}break;}
		buf.cleanUp();
		buf.addVertexWithUV(p*0, p*9.5, p*9.5,tw2,flipTH[1]?th1:th2);
		buf.addVertexWithUV(p*0, p*6.5, p*9.5,tw2,flipTH[1]?th2:th1);
		buf.addVertexWithUV(p*6, p*6.5, p*9.5,tw1,flipTH[1]?th2:th1);
		buf.addVertexWithUV(p*6, p*9.5, p*9.5,tw1,flipTH[1]?th1:th2);

		buf.addVertexWithUV(p*6, p*9.5, p*6.5,tw1,flipTH[2]?th1:th2);
		buf.addVertexWithUV(p*6, p*6.5, p*6.5,tw1,flipTH[2]?th2:th1);
		buf.addVertexWithUV(p*0, p*6.5, p*6.5,tw2,flipTH[2]?th2:th1);
		buf.addVertexWithUV(p*0, p*9.5, p*6.5,tw2,flipTH[2]?th1:th2);
		
		buf.addVertexWithUV(p*6, p*9.5, p*9.5,tw1,flipTH[3]?th2:th1);
		buf.addVertexWithUV(p*6, p*9.5, p*6.5,tw1,flipTH[3]?th1:th2);
		buf.addVertexWithUV(p*0, p*9.5, p*6.5,tw2,flipTH[3]?th1:th2);
		buf.addVertexWithUV(p*0, p*9.5, p*9.5,tw2,flipTH[3]?th2:th1);

		buf.addVertexWithUV(p*0, p*6.5, p*9.5,tw2,flipTH[4]?th2:th1);
		buf.addVertexWithUV(p*0, p*6.5, p*6.5,tw2,flipTH[4]?th1:th2);
		buf.addVertexWithUV(p*6, p*6.5, p*6.5,tw1,flipTH[4]?th1:th2);
		buf.addVertexWithUV(p*6, p*6.5, p*9.5,tw1,flipTH[4]?th2:th1);
		
		buf.rotateAt(0.5F,0.5F,0.5F,rX, rY, rZ);
		buf.draw();
		buf.pop();
	}
	private NoramlisedVertixBufferModel conectorFFLModel;
	private void generateModelConectorFFL(){
		buf.cleanUp();
		buf.addVertexWithUV(p*6.5,  p*6,     p*9.5,tWFSL*0, 0);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*9.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*9.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*9.5,  p*6,     p*9.5,tWFSL*8, 0);

		buf.addVertexWithUV(p*9.5,  p*6,     p*6.5,tWFSL*0, 0);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*6.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*6.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*6.5,  p*6,     p*6.5,tWFSL*8, 0);

		buf.addVertexWithUV(p*6.5,  p*6,     p*6.5,tWFSL*0, 0);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*6.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*9.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*6.5,  p*6,     p*9.5,tWFSL*8, 0);

		buf.addVertexWithUV(p*9.5,  p*6,     p*9.5,tWFSL*8, 0);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*9.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*6.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*9.5,  p*6,     p*6.5,tWFSL*0, 0);

		buf.addVertexWithUV(p*9.5,  p*6,     p*9.5,tWFSL*8, 0);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*9.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*9.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*6.5,  p*6,     p*9.5,tWFSL*0, 0);

		buf.addVertexWithUV(p*6.5,  p*6,     p*6.5,tWFSL*8, 0);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*6.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*6.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*9.5,  p*6,     p*6.5,tWFSL*0, 0);

		buf.addVertexWithUV(p*6.5,  p*6,     p*9.5,tWFSL*8, 0);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*9.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*6.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*6.5,  p*6,     p*6.5,tWFSL*0, 0);

		buf.addVertexWithUV(p*9.5,  p*6,     p*6.5,tWFSL*0, 0);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*6.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*9.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*9.5,  p*6,     p*9.5,tWFSL*8, 0);
		
		buf.addVertexWithUV(p*4.5, -p*2.78,  p*11.5,tWFSL*9, tHFSL*0);
		buf.addVertexWithUV(p*4.5, -p*4.78,  p*11.5,tWFSL*9, tHFSL*8);
		buf.addVertexWithUV(p*11.5,-p*4.78,  p*11.5,tWFSL*37, tHFSL*8);
		buf.addVertexWithUV(p*11.5,-p*2.78,  p*11.5,tWFSL*37, tHFSL*0);

		buf.addVertexWithUV(p*11.5,-p*2.78,  p*4.5,tWFSL*9, tHFSL*0);
		buf.addVertexWithUV(p*11.5,-p*4.78,  p*4.5,tWFSL*9, tHFSL*8);
		buf.addVertexWithUV(p*4.5, -p*4.78,  p*4.5,tWFSL*37, tHFSL*8);
		buf.addVertexWithUV(p*4.5, -p*2.78,  p*4.5,tWFSL*37, tHFSL*0);

		buf.addVertexWithUV(p*4.5, -p*2.78, p*4.5,tWFSL*9, tHFSL*0);
		buf.addVertexWithUV(p*4.5, -p*4.78, p*4.5,tWFSL*9, tHFSL*8);
		buf.addVertexWithUV(p*4.5, -p*4.78, p*11.5,tWFSL*37, tHFSL*8);
		buf.addVertexWithUV(p*4.5, -p*2.78, p*11.5,tWFSL*37, tHFSL*0);

		buf.addVertexWithUV(p*11.5,-p*2.78, p*11.5,tWFSL*9, tHFSL*0);
		buf.addVertexWithUV(p*11.5,-p*4.78, p*11.5,tWFSL*9, tHFSL*8);
		buf.addVertexWithUV(p*11.5,-p*4.78, p*4.5,tWFSL*37, tHFSL*8);
		buf.addVertexWithUV(p*11.5,-p*2.78, p*4.5,tWFSL*37, tHFSL*0);
		
		buf.addVertexWithUV(p*4.5, -p*2.78, p*4.5,tWFSL*38, tHFSL*24);
		buf.addVertexWithUV(p*4.5, -p*2.78, p*11.5,tWFSL*38, tHFSL*0);
		buf.addVertexWithUV(p*11.5,-p*2.78, p*11.5,tWFSL*62, tHFSL*0);
		buf.addVertexWithUV(p*11.5,-p*2.78, p*4.5,tWFSL*62, tHFSL*24);
		
		buf.addVertexWithUV(p*5.5, -p*0.78, p*10.5,tWFSL*9, tHFSL*9);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*10.5,tWFSL*9, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*10.5,tWFSL*29, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*0.78, p*10.5,tWFSL*29, tHFSL*9);

		buf.addVertexWithUV(p*10.5,-p*0.78, p*5.5,tWFSL*9, tHFSL*9);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*5.5,tWFSL*9, tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*5.5,tWFSL*29, tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*0.78, p*5.5,tWFSL*29, tHFSL*9);

		buf.addVertexWithUV(p*5.5, -p*0.78, p*5.5,tWFSL*9, tHFSL*9);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*5.5,tWFSL*9, tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*10.5,tWFSL*29, tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*0.78, p*10.5,tWFSL*29, tHFSL*9);

		buf.addVertexWithUV(p*10.5,-p*0.78, p*10.5,tWFSL*9, tHFSL*9);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*10.5,tWFSL*9, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*5.5,tWFSL*29, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*0.78, p*5.5,tWFSL*29, tHFSL*9);
		
		buf.addVertexWithUV(p*5.5, -p*0.78,  p*5.5,tWFSL*9, tHFSL*18);
		buf.addVertexWithUV(p*5.5, -p*0.78,  p*10.5,tWFSL*9, tHFSL*38);
		buf.addVertexWithUV(p*10.5, -p*0.78, p*10.5,tWFSL*29, tHFSL*38);
		buf.addVertexWithUV(p*10.5, -p*0.78, p*5.5,tWFSL*29, tHFSL*18);
		
		buf.addVertexWithUV(p*10.5,-p*0.78, p*10.5,tWFSL*29, tHFSL*9);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*10.5,tWFSL*29, tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*10.5,tWFSL*9, tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*0.78, p*10.5,tWFSL*9, tHFSL*9);

		buf.addVertexWithUV(p*5.5, -p*0.78, p*5.5,tWFSL*29, tHFSL*9);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*5.5,tWFSL*29, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*5.5,tWFSL*9, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*0.78, p*5.5,tWFSL*9, tHFSL*9);

		buf.addVertexWithUV(p*5.5, -p*0.78, p*10.5,tWFSL*29,tHFSL*9);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*10.5,tWFSL*29,tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*5.5,tWFSL*9,  tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*0.78, p*5.5,tWFSL*9,  tHFSL*9);

		buf.addVertexWithUV(p*10.5,-p*0.78, p*5.5,tWFSL*29, tHFSL*9);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*5.5,tWFSL*29, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*10.5,tWFSL*9, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*0.78, p*10.5,tWFSL*9, tHFSL*9);

		buf.addVertexWithUV(p*10.5, -p*0.78, p*5.5,tWFSL*29, tHFSL*18);
		buf.addVertexWithUV(p*10.5, -p*0.78, p*10.5,tWFSL*29, tHFSL*38);
		buf.addVertexWithUV(p*5.5, -p*0.78,  p*10.5,tWFSL*9, tHFSL*38);
		buf.addVertexWithUV(p*5.5, -p*0.78,  p*5.5,tWFSL*9, tHFSL*18);
		conectorFFLModel=buf.exportToNoramlisedVertixBufferModel();
	}
	public void drawConectorFFL(){
		bindTexture(Textures.FirePipeConectionFSL);
		if(conectorFFLModel==null)generateModelConectorFFL();
		else conectorFFLModel.draw();
	}
}
