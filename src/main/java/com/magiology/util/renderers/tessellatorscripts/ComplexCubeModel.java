package com.magiology.util.renderers.tessellatorscripts;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilobjects.vectors.Vec3M;
import com.magiology.util.utilobjects.vectors.Vec8F;

public class ComplexCubeModel{
	private final float minX,minY,minZ,maxX,maxY,maxZ;
	public Vec3M[] points=new Vec3M[8];
	public Vec8F[] UVs=new Vec8F[6];
	public Vec8F[] UVs2=new Vec8F[6];
	ResourceLocation[] st=new ResourceLocation[6];
	NormalizedVertixBuffer buf=Render.NVB();
	public boolean[] willSideRender={true,true,true,true,true,true};
	public ComplexCubeModel(float minX,float minY,float minZ,float maxX,float maxY,float maxZ){
		this.minX=minX;this.minY=minY;this.minZ=minZ;
		this.maxX=maxX;this.maxY=maxY;this.maxZ=maxZ;
		points=genPoints();
	}
	public ComplexCubeModel(float minX,float minY,float minZ,float maxX,float maxY,float maxZ,Vec8F[] quadUVs,ResourceLocation[] sidedtextures){
		if(quadUVs!=null){
			if(quadUVs.length==6)UVs=quadUVs;
			else if(quadUVs.length==1)UVs=new Vec8F[]{quadUVs[0],quadUVs[0],quadUVs[0],quadUVs[0],quadUVs[0],quadUVs[0]};
		}
		if(sidedtextures!=null){
			if(sidedtextures.length==6)st=sidedtextures;
			else if(sidedtextures.length==1)st=new ResourceLocation[]{sidedtextures[0],sidedtextures[0],sidedtextures[0],sidedtextures[0],sidedtextures[0],sidedtextures[0]};
		}
		if(quadUVs==null||sidedtextures==null){
			quadUVs=null;
			sidedtextures=null;
		}
		this.minX=minX;this.minY=minY;this.minZ=minZ;
		this.maxX=maxX;this.maxY=maxY;this.maxZ=maxZ;
		points=genPoints();
	}
	public ComplexCubeModel(float minX,float minY,float minZ,float maxX,float maxY,float maxZ,ResourceLocation[] sidedtextures){
		if(sidedtextures!=null){
			if(sidedtextures.length==6)st=sidedtextures;
			else if(sidedtextures.length==1)st=new ResourceLocation[]{sidedtextures[0],sidedtextures[0],sidedtextures[0],sidedtextures[0],sidedtextures[0],sidedtextures[0]};
		}
		this.minX=minX;this.minY=minY;this.minZ=minZ;
		this.maxX=maxX;this.maxY=maxY;this.maxZ=maxZ;
		points=genPoints();
		{
			float a=1-maxY;
			minY+=a;
			maxY+=a;
		}
		UVs[0]=new Vec8F(maxZ, minY, maxZ, maxY, minZ, maxY, minZ, minY);
		UVs[1]=new Vec8F(maxZ, minY, maxZ, maxY, minZ, maxY, minZ, minY);
		UVs[2]=new Vec8F(maxX, maxZ, maxX, minZ, minX, minZ, minX, maxZ);
		UVs[3]=new Vec8F(maxX, minZ, maxX, maxZ, minX, maxZ, minX, minZ);
		UVs[4]=new Vec8F(maxX, minY, maxX, maxY, minX, maxY, minX, minY);
		UVs[5]=new Vec8F(maxX, minY, maxX, maxY, minX, maxY, minX, minY);
		UVs2=UVs.clone();
	}
	private Vec3M[] genPoints(){
		//2=all max, 4=all min
		Vec3M[] result=new Vec3M[8];
		result[0]=new Vec3M(maxX, minY, minZ);
		result[1]=new Vec3M(maxX, maxY, minZ);
		result[2]=new Vec3M(maxX, maxY, maxZ);
		result[3]=new Vec3M(maxX, minY, maxZ);
		result[4]=new Vec3M(minX, minY, minZ);
		result[5]=new Vec3M(minX, maxY, minZ);
		result[6]=new Vec3M(minX, maxY, maxZ);
		result[7]=new Vec3M(minX, minY, maxZ);
		return result;
	}
	
	public void flipUVs(){
		Vec8F[] h=UVs2;
		UVs2=UVs;
		UVs=h;
	}
	
	public void draw(){
		try{
			if(willSideRender[0])try{
				if(st[0]!=null)TessUtil.bindTexture(st[0]);
				buf.addVertexWithUV(points[0],UVs[0].x2,UVs[0].y2);
				buf.addVertexWithUV(points[1],UVs[0].x1,UVs[0].y1);
				buf.addVertexWithUV(points[2],UVs[0].x4,UVs[0].y4);
				buf.addVertexWithUV(points[3],UVs[0].x3,UVs[0].y3);
				buf.draw();
			}catch(Exception exception){
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				buf.addVertexWithUV(points[0],0,0);
				buf.addVertexWithUV(points[1],0,0);
				buf.addVertexWithUV(points[2],0,0);
				buf.addVertexWithUV(points[3],0,0);
				buf.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			if(willSideRender[1])try{
				if(st[1]!=null)TessUtil.bindTexture(st[1]);
				buf.addVertexWithUV(points[7],UVs[1].x2,UVs[1].y2);
				buf.addVertexWithUV(points[6],UVs[1].x1,UVs[1].y1);
				buf.addVertexWithUV(points[5],UVs[1].x4,UVs[1].y4);
				buf.addVertexWithUV(points[4],UVs[1].x3,UVs[1].y3);
				buf.draw();
			}catch(Exception exception){
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				buf.addVertexWithUV(points[7],0,0);
				buf.addVertexWithUV(points[6],0,0);
				buf.addVertexWithUV(points[5],0,0);
				buf.addVertexWithUV(points[4],0,0);
				buf.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			if(willSideRender[2])try{
				if(st[2]!=null)TessUtil.bindTexture(st[2]);
				buf.addVertexWithUV(points[2],UVs[2].x2,UVs[2].y2);
				buf.addVertexWithUV(points[1],UVs[2].x1,UVs[2].y1);
				buf.addVertexWithUV(points[5],UVs[2].x4,UVs[2].y4);
				buf.addVertexWithUV(points[6],UVs[2].x3,UVs[2].y3);
				buf.draw();
			}catch(Exception exception){
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				buf.addVertexWithUV(points[2],0,0);
				buf.addVertexWithUV(points[1],0,0);
				buf.addVertexWithUV(points[5],0,0);
				buf.addVertexWithUV(points[6],0,0);
				buf.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			if(willSideRender[3])try{
				if(st[3]!=null)TessUtil.bindTexture(st[3]);
				buf.addVertexWithUV(points[7],UVs[3].x2,UVs[3].y2);
				buf.addVertexWithUV(points[4],UVs[3].x1,UVs[3].y1);
				buf.addVertexWithUV(points[0],UVs[3].x4,UVs[3].y4);
				buf.addVertexWithUV(points[3],UVs[3].x3,UVs[3].y3);
				buf.draw();
			}catch(Exception exception){
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				buf.addVertexWithUV(points[7],0,0);
				buf.addVertexWithUV(points[4],0,0);
				buf.addVertexWithUV(points[0],0,0);
				buf.addVertexWithUV(points[3],0,0);
				buf.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			if(willSideRender[4])try{
				if(st[4]!=null)TessUtil.bindTexture(st[4]);
				buf.addVertexWithUV(points[4],UVs[4].x2,UVs[4].y2);
				buf.addVertexWithUV(points[5],UVs[4].x1,UVs[4].y1);
				buf.addVertexWithUV(points[1],UVs[4].x4,UVs[4].y4);
				buf.addVertexWithUV(points[0],UVs[4].x3,UVs[4].y3);
				buf.draw();
			}catch(Exception exception){
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				buf.addVertexWithUV(points[4],0,0);
				buf.addVertexWithUV(points[5],0,0);
				buf.addVertexWithUV(points[1],0,0);
				buf.addVertexWithUV(points[0],0,0);
				buf.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			if(willSideRender[5])try{
				if(st[5]!=null)TessUtil.bindTexture(st[5]);
				buf.addVertexWithUV(points[3],UVs[5].x2,UVs[5].y2);
				buf.addVertexWithUV(points[2],UVs[5].x1,UVs[5].y1);
				buf.addVertexWithUV(points[6],UVs[5].x4,UVs[5].y4);
				buf.addVertexWithUV(points[7],UVs[5].x3,UVs[5].y3);
				buf.draw();
			}catch(Exception exception){
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				buf.addVertexWithUV(points[3],0,0);
				buf.addVertexWithUV(points[2],0,0);
				buf.addVertexWithUV(points[6],0,0);
				buf.addVertexWithUV(points[7],0,0);
				buf.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	/**true = max, false = min*/
	public Vec3M getPoint(boolean x,boolean y,boolean z){
		     if( x&&!y&&!z)return points[0];
		else if( x&& y&&!z)return points[1];
		else if( x&& y&& z)return points[2];
		else if( x&&!y&& z)return points[3];
		else if(!x&&!y&&!z)return points[4];
		else if(!x&& y&&!z)return points[5];
		else if(!x&& y&& z)return points[6];
		else if(!x&&!y&& z)return points[7];
		return null;
	}
	
	public ComplexCubeModel expand(float var){
		return expand(var, var, var);
	}
	public ComplexCubeModel expand(float x,float y,float z){
		points[0]=points[0].addVector(-x,y,-z);
		points[1]=points[1].addVector(-x,-y,-z);
		points[2]=points[2].addVector(-x,-y,z);
		points[3]=points[3].addVector(-x,y,z);
		points[4]=points[4].addVector(x,y,-z);
		points[5]=points[5].addVector(x,-y,-z);
		points[6]=points[6].addVector(x,-y,z);
		points[7]=points[7].addVector(x,y,z);
		
		return this;
	}
	
	
	
	
}
