package com.magiology.client.render.itemrender;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.magiology.handlers.animationhandlers.TheHandHandler;
import com.magiology.handlers.animationhandlers.TheHandHandler.HandData;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.MultiTransfromModel;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MatrixUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.IndexedModel;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemRendererTheHand{
	private final float p=1F/16F;
	public ResourceLocation[] blank1={new ResourceLocation("noTexture")};
	
	public MultiTransfromModel handModel;
	
	private Matrix4f 
		base,
		thumb1,thumb2,thumb3,
		finger1,finger2,finger3,
		finger4,finger5,finger6,
		finger7,finger8,finger9,
		finger10,finger11,finger12;

	
	public void renderItem(ItemStack item, EntityPlayer player){
		secure();
		try{
			HandData data=TheHandHandler.getRenderHandData();
			
			
			List<Matrix4f> transformations=new ArrayList<>();
			
			addFingerRotations(transformations, new Matrix4f[]{thumb1, thumb2, thumb3},
				new Vec3M(data.thumb[0], data.thumb[1], data.thumb[2]),
				new Vector2f(data.thumb[3], data.thumb[4])
			);
			addFingerRotations(transformations, new Matrix4f[]{finger1, finger2, finger3},
				new Vec3M(data.fingers[0][0], data.fingers[0][1], 0),
				new Vector2f(data.fingers[0][2], data.fingers[0][3])
			);
			addFingerRotations(transformations, new Matrix4f[]{finger4, finger5, finger6},
				new Vec3M(data.fingers[1][0], data.fingers[1][1], 0),
				new Vector2f(data.fingers[1][2], data.fingers[1][3])
			);
			addFingerRotations(transformations, new Matrix4f[]{finger7, finger8, finger9},
				new Vec3M(data.fingers[2][0], data.fingers[2][1], 0),
				new Vector2f(data.fingers[2][2], data.fingers[2][3])
			);
			addFingerRotations(transformations, new Matrix4f[]{finger10, finger11, finger12},
				new Vec3M(data.fingers[3][0], data.fingers[3][1], 0),
				new Vector2f(data.fingers[3][2], data.fingers[3][3])
			);
			
			new ColorF(1, 1, 1, 0.2).bind();
			OpenGLM.disableTexture2D();
			if(handModel==null||true){
				IndexedModel model=new IndexedModel();
				model.addCube(new CubeModel(0, 0, 0, p*8, p*2, p*10));
				
				handModel=new MultiTransfromModel(model);
				addFinger(new Vector2f(p*2, p*2), new Vec3M(p*3.5, p*3.4,p*2.4));
				addFinger(new Vector2f(p*1.7F, p*1.7F), new Vec3M(p*3.2, p*2.4, p*2.3));
				addFinger(new Vector2f(p*1.8F, p*1.8F), new Vec3M(p*3.6, p*3  , p*2.5));
				addFinger(new Vector2f(p*1.8F, p*1.8F), new Vec3M(p*3.2, p*2.7, p*2.9));
				addFinger(new Vector2f(p*1.6F, p*1.6F), new Vec3M(p*2.1, p*2,   p*2.1));
			}
			OpenGLM.pushMatrix();
			OpenGLM.translate(p*13, p*3, 0);
			OpenGLM.translate(data.base[0], data.base[1], data.base[2]);
			GL11U.glRotate(10+data.base[3], data.base[4], 15+data.base[5]);
			GL11U.glRotate(0, 130, 0);
			try{
				handModel.draw(transformations);
			}catch(Exception e){
				e.printStackTrace();
			}
			OpenGLM.popMatrix();
			OpenGLM.enableTexture2D();
		}catch(Exception e){
//			e.printStackTrace();
		}
	}
	
	private void init(){
		base=MatrixUtil.createMatrix(-60, 20, 20);
		thumb1=MatrixUtil.createMatrix(new Vector3f(p*7F,0,p*1.7F));
		thumb2=MatrixUtil.createMatrix(new Vector3f(0,0,p*3.5F));
		thumb3=MatrixUtil.createMatrix(new Vector3f(0,0,p*3.4F));
		
		finger1=MatrixUtil.createMatrix(new Vector3f(p*7F,p,p*10F)).rotate((float)Math.toRadians(-90), new Vector3f(0, 0, 1));
		finger2=MatrixUtil.createMatrix(new Vector3f(0,0,p*3.3F));
		finger3=MatrixUtil.createMatrix(new Vector3f(0,0,p*2.5F));
				
		finger4=MatrixUtil.createMatrix(new Vector3f(p*5F,p,p*10F)).rotate((float)Math.toRadians(-90), new Vector3f(0, 0, 1));
		finger5=MatrixUtil.createMatrix(new Vector3f(0,0,p*3.7F));
		finger6=MatrixUtil.createMatrix(new Vector3f(0,0,p*3.1F));
				
		finger7=MatrixUtil.createMatrix(new Vector3f(p*3F,p,p*10F)).rotate((float)Math.toRadians(-90), new Vector3f(0, 0, 1));
		finger8=MatrixUtil.createMatrix(new Vector3f(0,0,p*3.3F));
		finger9=MatrixUtil.createMatrix(new Vector3f(0,0,p*2.8F));
		
		finger10=MatrixUtil.createMatrix(new Vector3f(p*1F,p,p*10F)).rotate((float)Math.toRadians(-90), new Vector3f(0, 0, 1));
		finger11=MatrixUtil.createMatrix(new Vector3f(0,0,p*2.2F));
		finger12=MatrixUtil.createMatrix(new Vector3f(0,0,p*2.1F));
	}
	
	
	private void addFingerRotations(List<Matrix4f> transformations, Matrix4f[] mats, Vec3M rotBase, Vector2f rot_2_3){
		Matrix4f mat1=Matrix4f.load(mats[0], null);
		Matrix4f mat2=Matrix4f.load(mats[1], null);
		Matrix4f mat3=Matrix4f.load(mats[2], null);
		
		Matrix4f.mul(mat1, MatrixUtil.createMatrixZYX(rotBase.getZ(),rotBase.getY(),rotBase.getX()), mat1); 
		Matrix4f.mul(mat2, MatrixUtil.createMatrixY(rot_2_3.x), mat2);
		Matrix4f.mul(mat3, MatrixUtil.createMatrixY(rot_2_3.y), mat3);
		
		Matrix4f.mul(mat1, mat2, mat2);
		Matrix4f.mul(mat2, mat3, mat3);
		
		transformations.add(mat1);
		transformations.add(mat2);
		transformations.add(mat3);
	}
	
	private void addFinger(Vector2f wh, Vec3M lenghts){
		IndexedModel model=handModel.getChild();
		
		int start=model.getVertices().size();
		float w=wh.x,h=wh.y;
		CubeModel 
			th1=new CubeModel(-w/2, -h/2, 0, w/2, h/2, lenghts.getX()),
			th2=new CubeModel(-w/2, -h/2, 0, w/2, h/2, lenghts.getY()),
			th3=new CubeModel(-w/2, -h/2, 0, w/2, h/2, lenghts.getZ());
		th1.willSideRender[5]=
		th2.willSideRender[4]=
		th2.willSideRender[5]=
		th3.willSideRender[4]=false;

		model.addCube(th1);
		model.addCube(th2);
		model.addCube(th3);
		
		int[] partFront={2,3,6,7},partBack={1,0,5,4};
		for(int i=0;i<4;i++)partBack[i]+=8;
		
		int[] inds={
			partBack[0],
			partFront[0],
			partFront[2],
			partBack[2],
			
			partFront[1],
			partBack[1],
			partBack[3],
			partFront[3],
			
			partBack[1],
			partFront[1],
			partFront[0],
			partBack[0],
			
			partFront[3],
			partBack[3],
			partBack[2],
			partFront[2]
		};
		
		model.addIndices(start,inds);
		model.addIndices(start+8F,inds);
		
		handModel.addMatrix(UtilM.countedArray(start,start+8));
		handModel.addMatrix(UtilM.countedArray(start+8,start+16));
		handModel.addMatrix(UtilM.countedArray(start+16,start+24));
	}
	public void secure(){
		if(base==null)init();
	}
}