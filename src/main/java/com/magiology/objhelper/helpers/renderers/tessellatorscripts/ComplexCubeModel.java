package com.magiology.objhelper.helpers.renderers.tessellatorscripts;

import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.renderers.ShadedQuad;
import com.magiology.objhelper.helpers.renderers.TessHelper;
import com.magiology.objhelper.vectors.Vec3F;
import com.magiology.objhelper.vectors.Vec8F;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class ComplexCubeModel{
	private final float minX,minY,minZ,maxX,maxY,maxZ;
	public Vec3F[] points=new Vec3F[8];
	public Vec8F[] UVs=new Vec8F[6];
	ResourceLocation[] st=new ResourceLocation[6];
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

	}
	private Vec3F[] genPoints(){
		//2=all max, 4=all min
		Vec3F[] result=new Vec3F[8];
		result[0]=new Vec3F(maxX, minY, minZ);
		result[1]=new Vec3F(maxX, maxY, minZ);
		result[2]=new Vec3F(maxX, maxY, maxZ);
		result[3]=new Vec3F(maxX, minY, maxZ);
		result[4]=new Vec3F(minX, minY, minZ);
		result[5]=new Vec3F(minX, maxY, minZ);
		result[6]=new Vec3F(minX, maxY, maxZ);
		result[7]=new Vec3F(minX, minY, maxZ);
		return result;
	}
	public void draw(){
		try{
			if(willSideRender[0])try{
				if(st[0]!=null)TessHelper.bindTexture(st[0]);
				ShadedQuad.addVertexWithUV(points[0],UVs[0].x2,UVs[0].y2);
				ShadedQuad.addVertexWithUV(points[1],UVs[0].x1,UVs[0].y1);
				ShadedQuad.addVertexWithUV(points[2],UVs[0].x4,UVs[0].y4);
				ShadedQuad.addVertexWithUV(points[3],UVs[0].x3,UVs[0].y3);
				ShadedQuad.drawQuad();
			}catch(Exception exception){
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				ShadedQuad.addVertex(points[0]);
				ShadedQuad.addVertex(points[1]);
				ShadedQuad.addVertex(points[2]);
				ShadedQuad.addVertex(points[3]);ShadedQuad.drawQuad();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			if(willSideRender[1])try{
				if(st[1]!=null)TessHelper.bindTexture(st[1]);
				ShadedQuad.addVertexWithUV(points[7],UVs[1].x2,UVs[1].y2);
				ShadedQuad.addVertexWithUV(points[6],UVs[1].x1,UVs[1].y1);
				ShadedQuad.addVertexWithUV(points[5],UVs[1].x4,UVs[1].y4);
				ShadedQuad.addVertexWithUV(points[4],UVs[1].x3,UVs[1].y3);
				ShadedQuad.drawQuad();
			}catch(Exception exception){
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				ShadedQuad.addVertex(points[7]);
				ShadedQuad.addVertex(points[6]);
				ShadedQuad.addVertex(points[5]);
				ShadedQuad.addVertex(points[4]);ShadedQuad.drawQuad();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			if(willSideRender[2])try{
				if(st[2]!=null)TessHelper.bindTexture(st[2]);
				ShadedQuad.addVertexWithUV(points[2],UVs[2].x2,UVs[2].y2);
				ShadedQuad.addVertexWithUV(points[1],UVs[2].x1,UVs[2].y1);
				ShadedQuad.addVertexWithUV(points[5],UVs[2].x4,UVs[2].y4);
				ShadedQuad.addVertexWithUV(points[6],UVs[2].x3,UVs[2].y3);ShadedQuad.drawQuad();
			}catch(Exception exception){
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				ShadedQuad.addVertex(points[2]);
				ShadedQuad.addVertex(points[1]);
				ShadedQuad.addVertex(points[5]);
				ShadedQuad.addVertex(points[6]);ShadedQuad.drawQuad();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			if(willSideRender[3])try{
				if(st[3]!=null)TessHelper.bindTexture(st[3]);
				ShadedQuad.addVertexWithUV(points[7],UVs[3].x2,UVs[3].y2);
				ShadedQuad.addVertexWithUV(points[4],UVs[3].x1,UVs[3].y1);
				ShadedQuad.addVertexWithUV(points[0],UVs[3].x4,UVs[3].y4);
				ShadedQuad.addVertexWithUV(points[3],UVs[3].x3,UVs[3].y3);ShadedQuad.drawQuad();
			}catch(Exception exception){
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				ShadedQuad.addVertex(points[7]);
				ShadedQuad.addVertex(points[4]);
				ShadedQuad.addVertex(points[0]);
				ShadedQuad.addVertex(points[3]);ShadedQuad.drawQuad();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			if(willSideRender[4])try{
				if(st[4]!=null)TessHelper.bindTexture(st[4]);
				ShadedQuad.addVertexWithUV(points[4],UVs[4].x2,UVs[4].y2);
				ShadedQuad.addVertexWithUV(points[5],UVs[4].x1,UVs[4].y1);
				ShadedQuad.addVertexWithUV(points[1],UVs[4].x4,UVs[4].y4);
				ShadedQuad.addVertexWithUV(points[0],UVs[4].x3,UVs[4].y3);ShadedQuad.drawQuad();
			}catch(Exception exception){
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				ShadedQuad.addVertex(points[4]);
				ShadedQuad.addVertex(points[5]);
				ShadedQuad.addVertex(points[1]);
				ShadedQuad.addVertex(points[0]);ShadedQuad.drawQuad();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			if(willSideRender[5])try{
				if(st[5]!=null)TessHelper.bindTexture(st[5]);
				ShadedQuad.addVertexWithUV(points[3],UVs[5].x2,UVs[5].y2);
				ShadedQuad.addVertexWithUV(points[2],UVs[5].x1,UVs[5].y1);
				ShadedQuad.addVertexWithUV(points[6],UVs[5].x4,UVs[5].y4);
				ShadedQuad.addVertexWithUV(points[7],UVs[5].x3,UVs[5].y3);ShadedQuad.drawQuad();
			}catch(Exception exception){
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				ShadedQuad.addVertex(points[3]);
				ShadedQuad.addVertex(points[2]);
				ShadedQuad.addVertex(points[6]);
				ShadedQuad.addVertex(points[7]);ShadedQuad.drawQuad();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	
	public Vec3F getPoint(String string){
		String[] xyz=string.split(",");
		boolean x=false,y=false,z=false;
		for(int i=0;i<xyz.length;i++){
			String[] prop=xyz[i].split(" ");
			if(prop.length==2){
				if(!prop[0].equals("max")&&!prop[0].equals("min")){
					Helper.println("not min or max "+prop[0]);
					return null;
				}
				boolean minMax=prop[0].equals("max");
				
			    	 if(prop[1].equals("x"))x=minMax;
				else if(prop[1].equals("y"))y=minMax;
				else if(prop[1].equals("z"))z=minMax;
				else{
					Helper.println("invalid cordinate");
					return null;
				}
			    
			}else return null;
		}
		int id=-1;
		
		     if( x&&!y&&!z)id=0;
		else if( x&& y&&!z)id=1;
		else if( x&& y&& z)id=2;
		else if( x&&!y&& z)id=3;
		else if(!x&&!y&&!z)id=4;
		else if(!x&& y&&!z)id=5;
		else if(!x&& y&& z)id=6;
		else if(!x&&!y&& z)id=7;
		else return null;
//		Helper.printInln(id);
		return points[id];
	}
}
