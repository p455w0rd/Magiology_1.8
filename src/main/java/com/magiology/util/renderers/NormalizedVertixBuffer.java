package com.magiology.util.renderers;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.magiology.util.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilclasses.Helper.H;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class NormalizedVertixBuffer{
	
	protected List<ShadedTriangle> shadedTriangles=new ArrayList<ShadedTriangle>();
	protected List<PositionTextureVertex> unfinishedTriangle=new ArrayList<PositionTextureVertex>();
	protected WorldRenderer renderer;
	protected boolean instantNormalCalculation=true,willDrawAsAWireFrame=false,willClearAfterDraw=true;
	protected List<NormalizedVertixBuffer> subBuffers=new ArrayList<NormalizedVertixBuffer>();
	
	protected Matrix4f transformation=new Matrix4f();
	protected Vector3f rotation=new Vector3f();
	
	protected List<Matrix4f> transformationStacks=new ArrayList<Matrix4f>();
	protected List<Vector3f> rotationStacks=new ArrayList<Vector3f>();
	
	public NormalizedVertixBuffer(){
		this.renderer=Render.WR();
	}
	
	public NormalizedVertixBuffer createNewSubBuffer(){
		NormalizedVertixBuffer result=new NormalizedVertixBuffer();
		subBuffers.add(result);
		result.instantNormalCalculation=instantNormalCalculation;
		result.willDrawAsAWireFrame=willDrawAsAWireFrame;
		result.willClearAfterDraw=willClearAfterDraw;
		return result;
	}
	
	public void addVertexWithUV(double x,double y,double z,double u,double v){
		addVertexWithUV((float)x, (float)y, (float)z, (float)u, (float)v);
	}
	private void addVertexWithUV(float x,float y,float z,float u,float v){
		unfinishedTriangle.add(new PositionTextureVertex(x,y,z, u, v));
		if(unfinishedTriangle.size()==4)process();
	}
	public void addVertexWithUV(Vec3M vec3m, double u, double v){
		addVertexWithUV((float)vec3m.x, (float)vec3m.y, (float)vec3m.z, (float)u, (float)v);
	}
	protected final static float NULL_UV_ID=123456789;
	public void addVertex(double x,double y,double z){
		addVertexWithUV(x,y,z, NULL_UV_ID, NULL_UV_ID);
	}
	
	private void process(){
		shadedTriangles.add(generateShadedTriangle(unfinishedTriangle.get(0), unfinishedTriangle.get(1), unfinishedTriangle.get(2),true));
		shadedTriangles.add(generateShadedTriangle(unfinishedTriangle.get(2), unfinishedTriangle.get(3), unfinishedTriangle.get(0),true));
		unfinishedTriangle.clear();
	}
	private ShadedTriangle generateShadedTriangle(PositionTextureVertex pos0,PositionTextureVertex pos1,PositionTextureVertex pos2,boolean hasNormal){
		Vec3M normal;
		if(instantNormalCalculation){
			Vec3 vec3  = pos1.vector3D.subtractReverse(pos0.vector3D);
	        Vec3 vec31 = pos1.vector3D.subtractReverse(pos2.vector3D);
	        normal = Vec3M.conv(vec31.crossProduct(vec3).normalize());
		}else normal=new Vec3M(0, 1, 0);
		return new ShadedTriangle(pos0,pos1,pos2, normal);
	}
	
	public void cleanUp(){
		shadedTriangles.clear();
		unfinishedTriangle.clear();
	}
	
	protected void startRecordingTesselator(int type){
		renderer.startDrawing(type);
	}
	
	public void pasteToTesselator(boolean type){
		for(NormalizedVertixBuffer a:subBuffers){
			for(ShadedTriangle b:a.shadedTriangles){
				GL11H.transformVector(b.normal, new Vector3f(0,0,0),a.rotation.x,a.rotation.y,a.rotation.z,1);
				for(int b1=0;b1<2;b1++)b.pos3[b1].vector3D=GL11H.transformVector(b.pos3[b1].vector3D, a.transformation);
			}
			shadedTriangles.addAll(a.shadedTriangles);
		}
		subBuffers.clear();
		try {
			for(int s=0;s<shadedTriangles.size();s++){
				ShadedTriangle a=shadedTriangles.get(s);
				if(type){
					Vec3M finalNormal=GL11H.transformVector(a.normal.addVector(0,0,0), new Vector3f(0,0,0),rotation.x,rotation.y,rotation.z,1);
					renderer.setNormal((float)finalNormal.x, (float)finalNormal.y, (float)finalNormal.z);
					for(int b=0;b<a.pos3.length;b++){
						Vec3M finalVec=GL11H.transformVector(new Vec3M(a.pos3[b].vector3D.xCoord, a.pos3[b].vector3D.yCoord, a.pos3[b].vector3D.zCoord), transformation);
						if(NULL_UV_ID==a.pos3[b].texturePositionX&&NULL_UV_ID==a.pos3[b].texturePositionY)renderer.addVertex(finalVec.x, finalVec.y, finalVec.z);
						else renderer.addVertexWithUV(finalVec.x, finalVec.y, finalVec.z, a.pos3[b].texturePositionX, a.pos3[b].texturePositionY);
					}
				}else{
					Vec3M finalVects[]={
							GL11H.transformVector(Vec3M.conv(a.pos3[0].vector3D), transformation),
							GL11H.transformVector(Vec3M.conv(a.pos3[1].vector3D), transformation),
							GL11H.transformVector(Vec3M.conv(a.pos3[2].vector3D), transformation)
						};
					renderer.addVertexWithUV(finalVects[0].x, finalVects[0].y, finalVects[0].z, a.pos3[0].texturePositionX, a.pos3[0].texturePositionY);
					renderer.addVertexWithUV(finalVects[1].x, finalVects[1].y, finalVects[1].z, a.pos3[1].texturePositionX, a.pos3[1].texturePositionY);
					
					renderer.addVertexWithUV(finalVects[1].x, finalVects[1].y, finalVects[1].z, a.pos3[1].texturePositionX, a.pos3[1].texturePositionY);
					renderer.addVertexWithUV(finalVects[2].x, finalVects[2].y, finalVects[2].z, a.pos3[2].texturePositionX, a.pos3[2].texturePositionY);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public NormalizedVertixBufferModel exportToNoramlisedVertixBufferModel(){
		NormalizedVertixBufferModel model=NormalizedVertixBufferModel.create();
		model.init(shadedTriangles);
		cleanUp();
		return model;
	}
	public void renderToScreen(){
		TessHelper.draw();
		if(willClearAfterDraw)cleanUp();
	}
	
	public void draw(){
		if(willDrawAsAWireFrame){
			GL11H.texture(false);
			startRecordingTesselator(GL11.GL_LINES);
			pasteToTesselator(false);
			renderToScreen();
			GL11H.texture(true);
		}else{
			startRecordingTesselator(GL11.GL_TRIANGLES);
			pasteToTesselator(true);
			renderToScreen();
		}
	}
	
	public static class ShadedTriangle{
		public PositionTextureVertex[] pos3=null;
		public Vec3M normal=null;
		public ShadedTriangle(PositionTextureVertex pos1,PositionTextureVertex pos2,PositionTextureVertex pos3, Vec3M normal) {
			this.pos3=new PositionTextureVertex[]{pos1,pos2,pos3};
			this.normal=normal;
		}
		public void recalculateNormal(){
			Vec3 vec3  = this.pos3[1].vector3D.subtractReverse(this.pos3[0].vector3D);
	        Vec3 vec31 = this.pos3[1].vector3D.subtractReverse(this.pos3[2].vector3D);
	        normal = Vec3M.conv(vec31.crossProduct(vec3).normalize());
		}
	}
	public void recalculateNormals(){
		for(int a=0;a<shadedTriangles.size();a++){
			shadedTriangles.get(a).recalculateNormal();
		}
	}
	public void translate(double x,double y,double z){
		if(x==0&&y==0&&z==0)return;
		transformation.translate(new Vector3f((float)x, (float)y, (float)z));
	}
	public void rotate(double x,double y,double z){
		if(x==0&&y==0&&z==0)return;
		Vector3f.add(rotation, new Vector3f((float)x, (float)y, (float)z), rotation);
		if(x!=0)Matrix4f.rotate((float)Math.toRadians(x), new Vector3f(1, 0, 0), transformation, transformation);
		if(y!=0)Matrix4f.rotate((float)Math.toRadians(y), new Vector3f(0, 1, 0), transformation, transformation);
		if(z!=0)Matrix4f.rotate((float)Math.toRadians(z), new Vector3f(0, 0, 1), transformation, transformation);
	}
	public void rotateAt(double x,double y,double z,double rotX,double rotY,double rotZ){
		if(rotX==0&&rotY==0&&rotZ==0)return;
		translate(x,y,z);
		rotate(rotX, rotY, rotZ);
		translate(-x,-y,-z);
	}
	public void scale(double scale){
		if(scale==1)return;
		transformation.scale(new Vector3f((float)scale, (float)scale, (float)scale));
	}
	public void transform(double x,double y,double z,double rotX,double rotY,double rotZ, double scale){
		Vector3f.add(rotation, new Vector3f((float)rotX, (float)rotY, (float)rotZ), rotation);
		Matrix4f.mul(transformation, GL11H.createMatrix(new Vector3f((float)x, (float)y, (float)z), (float)rotX, (float)rotY, (float)rotZ, (float)scale), transformation);
	}
	
	public void useInstantNormalCalculation(){
		instantNormalCalculation=true;
	}
	public void disableInstantNormalCalculation(){
		instantNormalCalculation=false;
	}
	public void setDrawModeToWireFrame(){
		willDrawAsAWireFrame=true;
	}
	public void setDrawModeToQuadPlate(){
		willDrawAsAWireFrame=false;
	}
	public void enableClearing(){
		willClearAfterDraw=true;
	}
	public void disableClearing(){
		willClearAfterDraw=false;
	}
	public void pushMatrix(){
		transformationStacks.add(Matrix4f.add(transformation, Matrix4f.setZero(new Matrix4f()), null));
		rotationStacks.add(new Vector3f(rotation.x, rotation.y, rotation.z));
	}
	public void popMatrix(){
		if(transformationStacks.isEmpty()){
			H.printInln("Buffer is out of stacks to pop! You need to push before popping!\nFunction aborted.",H.getStackTrace());
			return;
		}
		int pos=transformationStacks.size()-1;
		transformation=transformationStacks.get(pos);
		rotation=rotationStacks.get(pos);
		transformationStacks.remove(pos);
		rotationStacks.remove(pos);
	}
	
	public void importComplexCube(ComplexCubeModel... cubeModels){
		for(ComplexCubeModel a:cubeModels)importComplexCube(a);
	}
	public void importComplexCube(ComplexCubeModel cubeModel){
		try{
			if(cubeModel.willSideRender[0])try{
				addVertexWithUV(cubeModel.points[0].x,cubeModel.points[0].y,cubeModel.points[0].z,cubeModel.UVs[0].x2,cubeModel.UVs[0].y2);
				addVertexWithUV(cubeModel.points[1].x,cubeModel.points[1].y,cubeModel.points[1].z,cubeModel.UVs[0].x1,cubeModel.UVs[0].y1);
				addVertexWithUV(cubeModel.points[2].x,cubeModel.points[2].y,cubeModel.points[2].z,cubeModel.UVs[0].x4,cubeModel.UVs[0].y4);
				addVertexWithUV(cubeModel.points[3].x,cubeModel.points[3].y,cubeModel.points[3].z,cubeModel.UVs[0].x3,cubeModel.UVs[0].y3);
			}catch(Exception exception){
				addVertexWithUV(cubeModel.points[0].x,cubeModel.points[0].y,cubeModel.points[0].z,0,0);
				addVertexWithUV(cubeModel.points[1].x,cubeModel.points[1].y,cubeModel.points[1].z,0,0);
				addVertexWithUV(cubeModel.points[2].x,cubeModel.points[2].y,cubeModel.points[2].z,0,0);
				addVertexWithUV(cubeModel.points[3].x,cubeModel.points[3].y,cubeModel.points[3].z,0,0);
			}
			if(cubeModel.willSideRender[1])try{
				addVertexWithUV(cubeModel.points[7].x,cubeModel.points[7].y,cubeModel.points[7].z,cubeModel.UVs[1].x2,cubeModel.UVs[1].y2);
				addVertexWithUV(cubeModel.points[6].x,cubeModel.points[6].y,cubeModel.points[6].z,cubeModel.UVs[1].x1,cubeModel.UVs[1].y1);
				addVertexWithUV(cubeModel.points[5].x,cubeModel.points[5].y,cubeModel.points[5].z,cubeModel.UVs[1].x4,cubeModel.UVs[1].y4);
				addVertexWithUV(cubeModel.points[4].x,cubeModel.points[4].y,cubeModel.points[4].z,cubeModel.UVs[1].x3,cubeModel.UVs[1].y3);
			}catch(Exception exception){
				addVertexWithUV(cubeModel.points[7].x,cubeModel.points[7].y,cubeModel.points[7].z,0,0);
				addVertexWithUV(cubeModel.points[6].x,cubeModel.points[6].y,cubeModel.points[6].z,0,0);
				addVertexWithUV(cubeModel.points[5].x,cubeModel.points[5].y,cubeModel.points[5].z,0,0);
				addVertexWithUV(cubeModel.points[4].x,cubeModel.points[4].y,cubeModel.points[4].z,0,0);
			}
			if(cubeModel.willSideRender[2])try{
				addVertexWithUV(cubeModel.points[2].x,cubeModel.points[2].y,cubeModel.points[2].z,cubeModel.UVs[2].x2,cubeModel.UVs[2].y2);
				addVertexWithUV(cubeModel.points[1].x,cubeModel.points[1].y,cubeModel.points[1].z,cubeModel.UVs[2].x1,cubeModel.UVs[2].y1);
				addVertexWithUV(cubeModel.points[5].x,cubeModel.points[5].y,cubeModel.points[5].z,cubeModel.UVs[2].x4,cubeModel.UVs[2].y4);
				addVertexWithUV(cubeModel.points[6].x,cubeModel.points[6].y,cubeModel.points[6].z,cubeModel.UVs[2].x3,cubeModel.UVs[2].y3);
			}catch(Exception exception){
				addVertexWithUV(cubeModel.points[2].x,cubeModel.points[2].y,cubeModel.points[2].z,0,0);
				addVertexWithUV(cubeModel.points[1].x,cubeModel.points[1].y,cubeModel.points[1].z,0,0);
				addVertexWithUV(cubeModel.points[5].x,cubeModel.points[5].y,cubeModel.points[5].z,0,0);
				addVertexWithUV(cubeModel.points[6].x,cubeModel.points[6].y,cubeModel.points[6].z,0,0);
			}
			if(cubeModel.willSideRender[3])try{
				addVertexWithUV(cubeModel.points[7].x,cubeModel.points[7].y,cubeModel.points[7].z,cubeModel.UVs[3].x2,cubeModel.UVs[3].y2);
				addVertexWithUV(cubeModel.points[4].x,cubeModel.points[4].y,cubeModel.points[4].z,cubeModel.UVs[3].x1,cubeModel.UVs[3].y1);
				addVertexWithUV(cubeModel.points[0].x,cubeModel.points[0].y,cubeModel.points[0].z,cubeModel.UVs[3].x4,cubeModel.UVs[3].y4);
				addVertexWithUV(cubeModel.points[3].x,cubeModel.points[3].y,cubeModel.points[3].z,cubeModel.UVs[3].x3,cubeModel.UVs[3].y3);
			}catch(Exception exception){
				addVertexWithUV(cubeModel.points[7].x,cubeModel.points[7].y,cubeModel.points[7].z,0,0);
				addVertexWithUV(cubeModel.points[4].x,cubeModel.points[4].y,cubeModel.points[4].z,0,0);
				addVertexWithUV(cubeModel.points[0].x,cubeModel.points[0].y,cubeModel.points[0].z,0,0);
				addVertexWithUV(cubeModel.points[3].x,cubeModel.points[3].y,cubeModel.points[3].z,0,0);
			}
			if(cubeModel.willSideRender[4])try{
				addVertexWithUV(cubeModel.points[4].x,cubeModel.points[4].y,cubeModel.points[4].z,cubeModel.UVs[4].x2,cubeModel.UVs[4].y2);
				addVertexWithUV(cubeModel.points[5].x,cubeModel.points[5].y,cubeModel.points[5].z,cubeModel.UVs[4].x1,cubeModel.UVs[4].y1);
				addVertexWithUV(cubeModel.points[1].x,cubeModel.points[1].y,cubeModel.points[1].z,cubeModel.UVs[4].x4,cubeModel.UVs[4].y4);
				addVertexWithUV(cubeModel.points[0].x,cubeModel.points[0].y,cubeModel.points[0].z,cubeModel.UVs[4].x3,cubeModel.UVs[4].y3);
			}catch(Exception exception){
				addVertexWithUV(cubeModel.points[4].x,cubeModel.points[4].y,cubeModel.points[4].z,0,0);
				addVertexWithUV(cubeModel.points[5].x,cubeModel.points[5].y,cubeModel.points[5].z,0,0);
				addVertexWithUV(cubeModel.points[1].x,cubeModel.points[1].y,cubeModel.points[1].z,0,0);
				addVertexWithUV(cubeModel.points[0].x,cubeModel.points[0].y,cubeModel.points[0].z,0,0);
			}
			if(cubeModel.willSideRender[5])try{
				addVertexWithUV(cubeModel.points[3].x,cubeModel.points[3].y,cubeModel.points[3].z,cubeModel.UVs[5].x2,cubeModel.UVs[5].y2);
				addVertexWithUV(cubeModel.points[2].x,cubeModel.points[2].y,cubeModel.points[2].z,cubeModel.UVs[5].x1,cubeModel.UVs[5].y1);
				addVertexWithUV(cubeModel.points[6].x,cubeModel.points[6].y,cubeModel.points[6].z,cubeModel.UVs[5].x4,cubeModel.UVs[5].y4);
				addVertexWithUV(cubeModel.points[7].x,cubeModel.points[7].y,cubeModel.points[7].z,cubeModel.UVs[5].x3,cubeModel.UVs[5].y3);
			}catch(Exception exception){
				addVertexWithUV(cubeModel.points[3].x,cubeModel.points[3].y,cubeModel.points[3].z,0,0);
				addVertexWithUV(cubeModel.points[2].x,cubeModel.points[2].y,cubeModel.points[2].z,0,0);
				addVertexWithUV(cubeModel.points[6].x,cubeModel.points[6].y,cubeModel.points[6].z,0,0);
				addVertexWithUV(cubeModel.points[7].x,cubeModel.points[7].y,cubeModel.points[7].z,0,0);
			}
			
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
}
