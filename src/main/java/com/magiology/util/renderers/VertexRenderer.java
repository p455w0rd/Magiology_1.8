package com.magiology.util.renderers;


import com.magiology.util.renderers.Renderer.RendererBase;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.vectors.Vec3M;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class VertexRenderer{
	
	protected List<ShadedTriangle> shadedTriangles=new ArrayList<>();
	protected List<PositionTextureVertex> unfinishedTriangle=new ArrayList<>();
	protected boolean instantNormalCalculation=true,willDrawAsAWireFrame=false,willClear=true;
	protected List<VertexRenderer> subBuffers=new ArrayList<>();
	
	private RendererBase renderer;
	
	protected VertexRenderer transformedBuffer;
	
	protected Matrix4f transformation=new Matrix4f();
	protected Vector3f rotation=new Vector3f();
	
	protected List<Matrix4f> transformationStacks=new ArrayList<>();
	protected List<Vector3f> rotationStacks=new ArrayList<>();
	
	public VertexRenderer(){
		this(true);
	}
	public VertexRenderer(boolean hasTransformedBuffer){
		if(hasTransformedBuffer)transformedBuffer=new VertexRenderer(false);
	}
	
	public VertexRenderer createNewSubBuffer(){
		VertexRenderer result=new VertexRenderer();
		subBuffers.add(result);
		result.instantNormalCalculation=instantNormalCalculation;
		result.willDrawAsAWireFrame=willDrawAsAWireFrame;
		result.willClear=willClear;
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
		if(hasNormal&&instantNormalCalculation){
			Vec3 vec3  = pos1.vector3D.subtractReverse(pos0.vector3D);
	        Vec3 vec31 = pos1.vector3D.subtractReverse(pos2.vector3D);
	        normal = Vec3M.conv(vec31.crossProduct(vec3).normalize());
		}else normal=new Vec3M(0, 1, 0);
		return new ShadedTriangle(pos0,pos1,pos2, normal);
	}
	
	public void cleanUp(){
		shadedTriangles.clear();
		unfinishedTriangle.clear();
		if(transformedBuffer!=null)transformedBuffer.cleanUp();
	}
	
	public void pasteToTesselator(boolean type){

		for(VertexRenderer a:subBuffers){
			for(ShadedTriangle b:a.shadedTriangles){
				GL11U.transformVector(b.normal, new Vector3f(),a.rotation.x,a.rotation.y,a.rotation.z,1);
				for(int b1=0;b1<2;b1++)b.pos3[b1].vector3D=GL11U.transformVector(b.pos3[b1].vector3D, a.transformation);
			}
			shadedTriangles.addAll(a.shadedTriangles);
		}
		subBuffers.clear();
		if(transformedBuffer!=null)transformedBuffer.pasteToTesselator(type);
		try {
			for(ShadedTriangle a:shadedTriangles){
				if(type){
					triangleToTesselatorQuads(a);
				}else{
					triangleToTesselatorLines(a);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	protected void triangleToTesselatorQuads(ShadedTriangle triangle){

		Vec3M finalNormal=GL11U.transformVector(triangle.normal.addVector(0,0,0), new Vector3f(),rotation.x,rotation.y,rotation.z,1).normalize();
		for(int b=0;b<2;b++){
			Vec3M finalVec=GL11U.transformVector(new Vec3M(triangle.pos3[b].vector3D.xCoord, triangle.pos3[b].vector3D.yCoord, triangle.pos3[b].vector3D.zCoord), transformation);
			Renderer.POS_UV_NORMAL.addVertex(finalVec, triangle.pos3[b].texturePositionX, triangle.pos3[b].texturePositionY,finalNormal);
		}

	}

	protected void triangleToTesselatorLines(ShadedTriangle triangle){
		Vec3M finalVectors[]={
				GL11U.transformVector(Vec3M.conv(triangle.pos3[0].vector3D), transformation),
				GL11U.transformVector(Vec3M.conv(triangle.pos3[1].vector3D), transformation),
				GL11U.transformVector(Vec3M.conv(triangle.pos3[2].vector3D), transformation)
		};
		Renderer.POS.addVertex(finalVectors[0].x, finalVectors[0].y, finalVectors[0].z);
		Renderer.POS.addVertex(finalVectors[1].x, finalVectors[1].y, finalVectors[1].z);

		Renderer.POS.addVertex(finalVectors[1].x, finalVectors[1].y, finalVectors[1].z);
		Renderer.POS.addVertex(finalVectors[2].x, finalVectors[2].y, finalVectors[2].z);
	}

	public VertexModel exportToNormalisedVertexBufferModel(){
		VertexModel model=VertexModel.create();
		model.init(shadedTriangles);
		cleanUp();
		return model;
	}
	public void renderToScreen(){
		Renderer.POS.draw();
		if(willClear)cleanUp();
	}
	
	public void draw(){
		if(getRenderer()==null){
			if(willDrawAsAWireFrame){
				GL11U.texture(false);
				Renderer.POS.begin(GL11.GL_LINES);
				pasteToTesselator(false);
				renderToScreen();
				GL11U.texture(true);
			}else{
				Renderer.POS_UV_NORMAL.beginQuads();
				pasteToTesselator(true);
				renderToScreen();
			}
		}else{
			getRenderer().beginQuads();
			pasteToTesselator(true);
			renderToScreen();
		}
	}
	
	public ByteBuffer toByteBuffer(){
		WorldRenderer wr=TessUtil.getWR();
		wr.begin(7, Renderer.POS_UV_NORMAL.getVertexFormat());
		pasteToTesselator(!willDrawAsAWireFrame);
		wr.finishDrawing();
		wr.reset();
		return wr.getByteBuffer();
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
		shadedTriangles.forEach(ShadedTriangle::recalculateNormal);
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
		Matrix4f.mul(transformation, GL11U.createMatrix(new Vector3f((float)x, (float)y, (float)z), (float)rotX, (float)rotY, (float)rotZ, (float)scale), transformation);
	}
	
	public void setInstantNormalCalculation(boolean enabled){
		instantNormalCalculation=enabled;
		if(transformedBuffer!=null)transformedBuffer.setInstantNormalCalculation(enabled);
	}
	public void setDrawAsWire(boolean wireMode){
		willDrawAsAWireFrame=wireMode;
		if(transformedBuffer!=null)transformedBuffer.setDrawAsWire(wireMode);
	}
	public boolean getDrawAsWire(){
		return willDrawAsAWireFrame;
	}
	public void setClearing(boolean enabled){
		willClear=enabled;
		if(transformedBuffer!=null)transformedBuffer.setClearing(enabled);
	}
	public void pushMatrix(){
		transformationStacks.add(Matrix4f.add(transformation, Matrix4f.setZero(new Matrix4f()), null));
		rotationStacks.add(new Vector3f(rotation.x, rotation.y, rotation.z));
	}
	public void popMatrix(){
		if(transformationStacks.isEmpty()){
			PrintUtil.println("Buffer is out of stacks to pop! You need to push before popping!\nFunction aborted.",U.getStackTrace());
			return;
		}
		int pos=transformationStacks.size()-1;
		transformation=transformationStacks.get(pos);
		rotation=rotationStacks.get(pos);
		transformationStacks.remove(pos);
		rotationStacks.remove(pos);
	}
	public void resetTransformation(){
		transformation=new Matrix4f();
		rotation=new Vector3f();
		
		transformationStacks=new ArrayList<>();
		rotationStacks=new ArrayList<>();
	}
	public void transformAndSaveTo(VertexRenderer buff){
		try{
			boolean isItself=buff==null||buff==this;
			List<ShadedTriangle> buffer=isItself?new ArrayList<>():buff.shadedTriangles;
			for(ShadedTriangle a:shadedTriangles){
				Vec3M finalNormal=GL11U.transformVector(a.normal.addVector(0, 0, 0), new Vector3f(0, 0, 0), rotation.x, rotation.y, rotation.z, 1);
				buffer.add(new ShadedTriangle(
						new PositionTextureVertex(GL11U.transformVector(new Vec3(a.pos3[0].vector3D.xCoord, a.pos3[0].vector3D.yCoord, a.pos3[0].vector3D.zCoord), transformation), a.pos3[0].texturePositionX, a.pos3[0].texturePositionY),
						new PositionTextureVertex(GL11U.transformVector(new Vec3(a.pos3[1].vector3D.xCoord, a.pos3[1].vector3D.yCoord, a.pos3[1].vector3D.zCoord), transformation), a.pos3[1].texturePositionX, a.pos3[1].texturePositionY),
						new PositionTextureVertex(GL11U.transformVector(new Vec3(a.pos3[2].vector3D.xCoord, a.pos3[2].vector3D.yCoord, a.pos3[2].vector3D.zCoord), transformation), a.pos3[2].texturePositionX, a.pos3[2].texturePositionY),
						finalNormal
				));
			}
			if(isItself){
				shadedTriangles=buffer;
			}else{
				if(willClear)shadedTriangles.clear();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public Matrix4f getTransformation(){
		return Matrix4f.add(transformation, Matrix4f.setZero(new Matrix4f()), null);
	}
	public Vector3f getRotation(){
		return Vector3f.add(rotation, new Vector3f(), null);
	}
	
	public ShadedTriangle getTriangle(int id){
		return shadedTriangles.get(id);
	}
	public List<ShadedTriangle> getTriangles(){
		return shadedTriangles;
	}
	public void unbindOverrideFormat(){
		renderer=null;
	}
	public void setRenderer(RendererBase format){
		renderer=format;
	}
	
	public void importComplexCube(CubeModel... cubeModels){
		for(CubeModel a:cubeModels)importComplexCube(a);
	}
	public void importComplexCube(CubeModel cubeModel){
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
	public RendererBase getRenderer(){
		return renderer;
	}
}
