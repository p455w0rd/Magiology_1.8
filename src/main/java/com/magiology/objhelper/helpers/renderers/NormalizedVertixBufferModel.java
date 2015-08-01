package com.magiology.objhelper.helpers.renderers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.renderers.NoramlisedVertixBuffer.ShadedTriangle;
import com.magiology.objhelper.vectors.Vec3M;

public class NormalizedVertixBufferModel{
	private List<ShadedTriangle> shadedTriangles=new ArrayList<ShadedTriangle>();
	private Tessellator tessellator;
	private boolean isInit=false;
	private boolean willDrawAsAWireFrame=false,willClearAfterDraw=true;
	private final static float NULL_UV_ID=123456789;
	private Matrix4f transformation=new Matrix4f(),backupTransformation=new Matrix4f();
	private Vector3f rotation=new Vector3f(),backupRotation=new Vector3f();
	
	
	protected static NormalizedVertixBufferModel create(Tessellator tessellator){
		return new NormalizedVertixBufferModel(tessellator);
	}
	private NormalizedVertixBufferModel(Tessellator tessellator){
		this.tessellator=tessellator;
		
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
	protected void init(List<ShadedTriangle> shadedTriangles){
		if(isInit)return;isInit=true;
		this.shadedTriangles=new ArrayList<ShadedTriangle>();
		this.shadedTriangles.addAll(shadedTriangles);
	}
	private void startRecordingTesselator(int type){
		tessellator.startDrawing(type);
	}
	
	public void pasteToTesselator(boolean type){
		for(ShadedTriangle a:shadedTriangles){
			if(type){
				for(int b=0;b<a.pos3.length;b++){
					Vec3M finalNormal=GL11H.transformVector(Helper.Vec3M(a.normal.xCoord, a.normal.yCoord, a.normal.zCoord), new Vector3f(),rotation.x,rotation.y,rotation.z,1);
					tessellator.setNormal((float)finalNormal.xCoord, (float)finalNormal.yCoord, (float)finalNormal.zCoord);
					
					
					Vec3M finalVec=GL11H.transformVector(Helper.Vec3M(a.pos3[b].vector3D.xCoord, a.pos3[b].vector3D.yCoord, a.pos3[b].vector3D.zCoord), transformation);
					if(NULL_UV_ID==a.pos3[b].texturePositionX&&NULL_UV_ID==a.pos3[b].texturePositionY){
						tessellator.addVertex(finalVec.xCoord, finalVec.yCoord, finalVec.zCoord);
					}
					tessellator.addVertexWithUV(finalVec.xCoord, finalVec.yCoord, finalVec.zCoord, a.pos3[b].texturePositionX, a.pos3[b].texturePositionY);
				}
			}else{
				tessellator.addVertexWithUV(a.pos3[0].vector3D.xCoord, a.pos3[0].vector3D.yCoord, a.pos3[0].vector3D.zCoord, a.pos3[0].texturePositionX, a.pos3[0].texturePositionY);
				tessellator.addVertexWithUV(a.pos3[1].vector3D.xCoord, a.pos3[1].vector3D.yCoord, a.pos3[1].vector3D.zCoord, a.pos3[1].texturePositionX, a.pos3[1].texturePositionY);
				
				tessellator.addVertexWithUV(a.pos3[1].vector3D.xCoord, a.pos3[1].vector3D.yCoord, a.pos3[1].vector3D.zCoord, a.pos3[1].texturePositionX, a.pos3[1].texturePositionY);
				tessellator.addVertexWithUV(a.pos3[2].vector3D.xCoord, a.pos3[2].vector3D.yCoord, a.pos3[2].vector3D.zCoord, a.pos3[2].texturePositionX, a.pos3[2].texturePositionY);
				
//				tessellator.addVertexWithUV(a.pos3[2].vector3D.xCoord, a.pos3[2].vector3D.yCoord, a.pos3[2].vector3D.zCoord, a.pos3[2].texturePositionX, a.pos3[2].texturePositionY);
//				tessellator.addVertexWithUV(a.pos3[0].vector3D.xCoord, a.pos3[0].vector3D.yCoord, a.pos3[0].vector3D.zCoord, a.pos3[0].texturePositionX, a.pos3[0].texturePositionY);
			}
			
		}
	}
	
	public void renderToScreen(){
		tessellator.draw();
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
		translate( pos);
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
	
	public void push(){
		backupTransformation=Matrix4f.add(transformation, Matrix4f.setZero(new Matrix4f()), null);
		backupRotation=new Vector3f(rotation.x, rotation.y, rotation.z);
	}
	public void pop(){
		transformation=Matrix4f.add(backupTransformation, Matrix4f.setZero(new Matrix4f()), null);
		rotation=new Vector3f(backupRotation.x, backupRotation.y, backupRotation.z);
	}
}