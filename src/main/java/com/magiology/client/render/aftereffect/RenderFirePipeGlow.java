package com.magiology.client.render.aftereffect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

import org.lwjgl.opengl.GL11;

import com.magiology.core.init.MItems;
import com.magiology.mcobjects.tileentityes.TileEntityFirePipe;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.renderers.NormalizedVertixBufferModel;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;

public class RenderFirePipeGlow extends LongAfterRenderRendererBase{
	
	private final float p= 1F/16F;
	NormalizedVertixBuffer buf=TessUtil.getNVB();
	public TileEntityFirePipe pipe;
	public  double alpha=0,prevAlpha;
	private static EntityPlayer player=U.getMC().thePlayer;
	
	public RenderFirePipeGlow(TileEntityFirePipe pipe){
		this.pipe=pipe;
		alpha=0.1;
	}

	@Override
	public void render(){
		float fc=Util.keepValueInBounds(PowerUtil.getPowerPrecentage(pipe), 0, 1);
		if(fc>0.01){
			GL11.glPushMatrix();
			GL11.glTranslated(pipe.x(), pipe.y(), pipe.z());
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11U.SetUpOpaqueRendering(1);
			
			double var1=Util.fluctuator(20,(pipe.x()+pipe.y()+pipe.z())*4),
				   var2=Util.fluctuator(47,(pipe.x()+pipe.y()+pipe.z())*4);
			
			GL11.glColor4d(0.9, 0.1*var1, 0.15*var2, 0.6*fc*Util.calculateRenderPos(prevAlpha, alpha));
			GL11.glDepthMask(true);
			if(!pipe.isStrate(null)){
				for(int i=0; i< pipe.connections.length; i++)if(pipe.connections[i].getMain()&&pipe.connections[i].willRender())drawConnectorGlow(pipe.connections[i].getFaceEF());
				drawCoreGlow();
			}else for(int a=0;a<3;a++){
				int b=a;
				if(a==0)b=3;
				else if(a==1)b=4;
				else if(a==2)b=1;
				if(pipe.isStrate(EnumFacing.getFront(b)))drawStrateCoreGlow(EnumFacing.getFront(b));
			}
			
			GL11U.EndOpaqueRendering();
			GL11.glColor4d(1,1,1,1);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();
		}
	}
	

	private static NormalizedVertixBufferModel strateCoreModel;
	private void generateModelStrateCoreGlow(){
		buf.cleanUp();
		ComplexCubeModel
		c5=new ComplexCubeModel(0,   p*9.5F,  p*9.5F, 1,   p*10F,  p*10F),
		c6=new ComplexCubeModel(0,   p*9.5F,  p*6F,   1,   p*10,   p*6.5F),
		c7=new ComplexCubeModel(0,   p*6,     p*9.5F, 1,   p*6.5F,  p*10F),
		c8=new ComplexCubeModel(0,   p*6,     p*6F,   1,   p*6.5F,   p*6.5F);
		c5.willSideRender[0]=c5.willSideRender[1]=
		c6.willSideRender[0]=c6.willSideRender[1]=
		c7.willSideRender[0]=c7.willSideRender[1]=
		c8.willSideRender[0]=c8.willSideRender[1]=
		false;
		buf.importComplexCube(c5,c6,c7,c8);
		strateCoreModel=buf.exportToNoramlisedVertixBufferModel();
	}
	private void drawStrateCoreGlow(EnumFacing dir){
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		if (dir.equals(EnumFacing.UP))GL11.glRotatef(-90, 0, 0, 1);
		else if (dir.equals(EnumFacing.SOUTH))GL11.glRotatef(90, 0, 1, 0);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		if(strateCoreModel==null)generateModelStrateCoreGlow();
		else strateCoreModel.draw();
		GL11.glPopMatrix();
	}
	
	
	private static NormalizedVertixBufferModel connectorGlowModel;
	private void generateModelConnectorGlow(){
		buf.cleanUp();
		ComplexCubeModel 
		c1=new ComplexCubeModel(p*3, p*10.5F, p*10.5F,p*5.5F,p*10,   p*10),
		c2=new ComplexCubeModel(p*3, p*10.5F, p*6F,   p*5.5F,p*10,   p*5.5F),
		c3=new ComplexCubeModel(p*3, p*6F,    p*10.5F,p*5.5F,p*5.5F, p*10),
		c4=new ComplexCubeModel(p*3, p*6F,    p*6F,   p*5.5F,p*5.5F, p*5.5F),
		c5=new ComplexCubeModel(0,   p*9.5F,  p*9.5F, p*3,   p*10F,  p*10F),
		c6=new ComplexCubeModel(0,   p*9.5F,  p*6F,   p*3,   p*10,   p*6.5F),
		c7=new ComplexCubeModel(0,   p*6,  p*9.5F, p*3,   p*6.5F,  p*10F),
		c8=new ComplexCubeModel(0,   p*6,  p*6F,   p*3,   p*6.5F,   p*6.5F);
		c1.willSideRender[0]=c1.willSideRender[1]=
		c2.willSideRender[0]=c2.willSideRender[1]=
		c3.willSideRender[0]=c3.willSideRender[1]=
		c4.willSideRender[0]=c4.willSideRender[1]=
		c5.willSideRender[0]=c5.willSideRender[1]=
		c6.willSideRender[0]=c6.willSideRender[1]=
		c7.willSideRender[0]=c7.willSideRender[1]=
		c8.willSideRender[0]=c8.willSideRender[1]=false;
		c1.points[4].y-=p*0.5;c1.points[5].y-=p*0.5;c1.points[6].y-=p*0.5;c1.points[7].y-=p*0.5;c1.points[4].z-=p*0.5;c1.points[5].z-=p*0.5;c1.points[6].z-=p*0.5;c1.points[7].z-=p*0.5;
		c2.points[4].y-=p*0.5;c2.points[5].y-=p*0.5;c2.points[6].y-=p*0.5;c2.points[7].y-=p*0.5;c2.points[4].z+=p*0.5;c2.points[5].z+=p*0.5;c2.points[6].z+=p*0.5;c2.points[7].z+=p*0.5;
		c3.points[4].y+=p*0.5;c3.points[5].y+=p*0.5;c3.points[6].y+=p*0.5;c3.points[7].y+=p*0.5;c3.points[4].z-=p*0.5;c3.points[5].z-=p*0.5;c3.points[6].z-=p*0.5;c3.points[7].z-=p*0.5;
		c4.points[4].y+=p*0.5;c4.points[5].y+=p*0.5;c4.points[6].y+=p*0.5;c4.points[7].y+=p*0.5;c4.points[4].z+=p*0.5;c4.points[5].z+=p*0.5;c4.points[6].z+=p*0.5;c4.points[7].z+=p*0.5;
		buf.importComplexCube(c1,c2,c3,c4,c5,c6,c7,c8);
		connectorGlowModel=buf.exportToNoramlisedVertixBufferModel();
	}
	private void drawConnectorGlow(EnumFacing dir){
		if(connectorGlowModel==null)generateModelConnectorGlow();
		else{
			float rX=0,rY=0,rZ=0;
			if(dir.equals(EnumFacing.WEST)){}
			else if (dir.equals(EnumFacing.UP)){rZ=-90;}
			else if (dir.equals(EnumFacing.DOWN)){rZ=90;}
			else if (dir.equals(EnumFacing.SOUTH)){rY=90;}
			else if (dir.equals(EnumFacing.EAST)){rY=-180;}
			else if (dir.equals(EnumFacing.NORTH)){rY=-90;}
			
			GL11.glPushMatrix();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11U.rotateXYZ(rX, rY, rZ);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			connectorGlowModel.draw();
			GL11.glPopMatrix();
		}
	}
	
	private static NormalizedVertixBufferModel coreGlowModel;
	private void generateModelCoreGlow(){
		buf.cleanUp();
		ComplexCubeModel 
		c1=new ComplexCubeModel(p*10,   p*10,   p*6,p*10.5F, p*10.5F, p*10),
		c2=new ComplexCubeModel(p*10,   p*5.5F, p*6,p*10.5F, p*6F,    p*10),
		c3=new ComplexCubeModel(p*5.5F, p*10,   p*6,p*6,     p*10.5F, p*10),
		c4=new ComplexCubeModel(p*5.5F, p*5.5F, p*6,p*6,     p*6F,    p*10),
		
		c5=new ComplexCubeModel(p*6F, p*10F,  p*5.5F, p*10F, p*10.5F, p*6F),
		c6=new ComplexCubeModel(p*6F, p*5.5F, p*5.5F, p*10F, p*6F,    p*6F),
		c7=new ComplexCubeModel(p*6F, p*10F,  p*10F,  p*10F, p*10.5F, p*10.5F),
		c8=new ComplexCubeModel(p*6F, p*5.5F, p*10F,  p*10F, p*6F,    p*10.5F),
		
		c9 =new ComplexCubeModel(p*10.5F,p*10F, p*10F, p*10F,  p*6F, p*10.5F),
		c10=new ComplexCubeModel(p*6F,   p*10F, p*10F, p*5.5F, p*6F, p*10.5F),
		c11=new ComplexCubeModel(p*10.5F,p*10F, p*5.5F, p*10F,  p*6F, p*6),
		c12=new ComplexCubeModel(p*6F,   p*10F, p*5.5F, p*5.5F, p*6F, p*6),
		
		c13=new ComplexCubeModel(p*10F,  p*10, p*10,   p*10.5F,p*10.5F, p*10.5F),
		c14=new ComplexCubeModel(p*5.5F, p*10, p*10,   p*6,    p*10.5F, p*10.5F),
		c15=new ComplexCubeModel(p*5.5F, p*10, p*5.5F, p*6,    p*10.5F, p*6),
		c16=new ComplexCubeModel(p*10,   p*10, p*5.5F, p*10.5F,p*10.5F, p*6),
		
		c17=new ComplexCubeModel(p*10F,  p*5.5F, p*10,   p*10.5F,p*6, p*10.5F),
		c18=new ComplexCubeModel(p*5.5F, p*5.5F, p*10,   p*6,    p*6, p*10.5F),
		c19=new ComplexCubeModel(p*5.5F, p*5.5F, p*5.5F, p*6,    p*6, p*6),
		c20=new ComplexCubeModel(p*10,   p*5.5F, p*5.5F, p*10.5F,p*6, p*6);
		c1.willSideRender[4]=c1.willSideRender[5]=
		c2.willSideRender[4]=c2.willSideRender[5]=
		c3.willSideRender[4]=c3.willSideRender[5]=
		c4.willSideRender[4]=c4.willSideRender[5]=
		
		c5.willSideRender[0]=c5.willSideRender[1]=
		c6.willSideRender[0]=c6.willSideRender[1]=
		c7.willSideRender[0]=c7.willSideRender[1]=
		c8.willSideRender[0]=c8.willSideRender[1]=
		
		 c9.willSideRender[2]= c9.willSideRender[3]=
		c10.willSideRender[2]=c10.willSideRender[3]=
		c11.willSideRender[2]=c11.willSideRender[3]=
		c12.willSideRender[2]=c12.willSideRender[3]=
		
		c13.willSideRender[1]=c13.willSideRender[3]=c13.willSideRender[4]=
		c14.willSideRender[0]=c14.willSideRender[3]=c14.willSideRender[4]=
		c15.willSideRender[0]=c15.willSideRender[3]=c15.willSideRender[5]=
		c16.willSideRender[1]=c16.willSideRender[3]=c16.willSideRender[5]=
		c17.willSideRender[1]=c17.willSideRender[2]=c17.willSideRender[4]=
		c18.willSideRender[2]=c18.willSideRender[0]=c18.willSideRender[4]=
		c19.willSideRender[2]=c19.willSideRender[0]=c19.willSideRender[5]=
		c20.willSideRender[2]=c20.willSideRender[1]=c20.willSideRender[5]=
		false;
		buf.importComplexCube(c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,c19,c20);
		coreGlowModel=buf.exportToNoramlisedVertixBufferModel();
	}
	private void drawCoreGlow(){
		if(coreGlowModel==null)generateModelCoreGlow();
		else coreGlowModel.draw();
	}
	
	
	@Override
	public void update(){
		player=Util.getThePlayer();
		if(player==null)return;
		
		prevAlpha=alpha;
		
		alpha+=0.4*(Util.isItemInStack(MItems.FireHammer, player.getCurrentEquippedItem())?1:-1);
		
		if(!(player.worldObj.getTileEntity(pipe.getPos())instanceof TileEntityFirePipe))kill();
		
		alpha=Util.keepValueInBounds(alpha, 0, 1);
		if(alpha<0.05)kill();
	}
}