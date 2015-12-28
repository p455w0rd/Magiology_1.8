package com.magiology.util.renderers;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.vectors.*;


/**
 * GL11 helper! :D
 * @author LapisSea
 */
public class GL11U{
	/**@param ID 1-2*/
	public static void blendFunc(int ID){switch(ID){
		case 1:glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);return;
		case 2:glBlendFunc(GL_SRC_ALPHA, GL_ONE);return;
		case 3:glBlendFunc(GL_ONE, GL_ONE);return;
		default:UtilM.println(">>>WARNING!!<<<\nGL11Helper failed to get glBlendFunc from chosen ID!\n--------------------------\n");return;
	}}
	/**Is blend enabled?
	 * @param enabled*/
	public static void glRotate(double xAngle,double yAngle,double zAngle,double xOffset,double yOffset,double zOffset,boolean... willTranslateBack){
		boolean WTB=true;
		if(willTranslateBack.length==1)WTB=willTranslateBack[0];
		GL11.glTranslated(xOffset, yOffset, zOffset);
		glRotate(xAngle, yAngle, zAngle);
		if(WTB)GL11.glTranslated(-xOffset, -yOffset, -zOffset);
	}
	public static void glRotate(double x,double y,double z){
		glRotated(x,1,0,0);
		glRotated(y,0,1,0);
		glRotated(z,0,0,1);
	}
	public static void glRotate(float[] float3){
		glRotate(float3[0], float3[1], float3[2]);
	}
	public static void glRotate(double[] double3){
		glRotate(double3[0], double3[1], double3[2]);
	}
	public static void glRotate(Vec3M vec){
		glRotate(vec.x, vec.y, vec.z);
	}
	public static void glRotate(Vector3f vec){
		glRotate(vec.x, vec.y, vec.z);
	}
	public static void glBlend(boolean enabled){
		if(enabled)glEnable(GL_BLEND);
		else glDisable(GL_BLEND);
	}
	/**Is all opaque texture accepted to be rendered.
	 * true  = everything
	 * false = only 100% opaque texture rendered
	 * @param enabled*/
	public static void allOpacityIs(boolean enabled){
		if(enabled)glAlphaFunc(GL_GREATER, 0);
		else glAlphaFunc(GL_GREATER, 0.9999F);
	}
	/**Sets the texture opaque minimal limit to default MC setting.*/
	public static void resetOpacity(){glAlphaFunc(GL_GREATER, 0.1F);}
	/**Sets the rendering mode to render opaque texture.*/
	public static void setUpOpaqueRendering(int ID){
		glDepthMask(false);
		glBlend(true);
		blendFunc(ID);
		allOpacityIs(true);
		glDisable(GL_ALPHA_TEST);
	}
	/**Sets the rendering mode to render 100% opaque texture only.*/
	public static void endOpaqueRendering(){
		glBlend(false);
		glEnable(GL_ALPHA_TEST);
		glDepthMask(true);
		blendFunc(1);
		resetOpacity();
	}
	public static void glScale(double scale){
		glScaled(scale, scale, scale);
	}
	public static void glScale(float scale){
		glScalef(scale, scale, scale);
	}
	public static void glTranslate(double[] arrayOf3D){
		if(arrayOf3D.length!=3)return;
		GL11.glTranslated(arrayOf3D[0],arrayOf3D[1],arrayOf3D[2]);
	}
	public static void glTranslate(float[] arrayOf3F){
		if(arrayOf3F.length!=3)return;
		GL11.glTranslated(arrayOf3F[0],arrayOf3F[1],arrayOf3F[2]);
	}
	public static void glTranslate(Vec3M vec){
		GL11.glTranslated(vec.x,vec.y,vec.z);
	}
	public static void texture(boolean enabled){
		if(enabled)glEnable(GL_TEXTURE_2D);
		else glDisable(GL_TEXTURE_2D);
	}
	public static void glLighting(boolean enabled){
		if(enabled)glEnable(GL_LIGHTING);
		else glDisable(GL_LIGHTING);
	}
	public static void glCulFace(boolean enabled){
		if(enabled)glEnable(GL_CULL_FACE);
		else glDisable(GL_CULL_FACE);
	}
	public static void glDepth(boolean enabled){
		if(enabled)glEnable(GL_DEPTH_TEST);
		else glDisable(GL_DEPTH_TEST);
	}
	public static Matrix4f createMatrix(Vec3M translation,float rotationX,float rotationY,float rotationZ, float scale){
		return createMatrix(new Vector3f((float)translation.x, (float)translation.y, (float)translation.z), rotationX, rotationY, rotationZ, scale);
	}
	public static Matrix4f createMatrix(Vector3f translation,float rotationX,float rotationY,float rotationZ, float scale){
		Matrix4f result=new Matrix4f();
		Matrix4f.translate(translation, result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationX), new Vector3f(1, 0, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationY), new Vector3f(0, 1, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationZ), new Vector3f(0, 0, 1), result, result);
		Matrix4f.scale(new Vector3f(scale, scale, scale), result, result);
		return result;
	}
	public static Vec3M transformVector(Vec3M vectorForTransformation,Vector3f translation,double rotationX,double rotationY,double rotationZ, double scale){
		if(vectorForTransformation==null)vectorForTransformation=new Vec3M();
		Vector3f vec=transformVector(new Vector3f((float)vectorForTransformation.x, (float)vectorForTransformation.y, (float)vectorForTransformation.z), translation, rotationX, rotationY, rotationZ, scale);
		vectorForTransformation.x=vec.x;
		vectorForTransformation.y=vec.y;
		vectorForTransformation.z=vec.z;
		return vectorForTransformation;
	}
	public static Vector3f transformVector(Vector3f vectorForTransformation,Vector3f translation,double rotationX,double rotationY,double rotationZ, double scale){
		if(vectorForTransformation==null)vectorForTransformation=new Vector3f();
		Matrix4f transform=GL11U.createMatrix(translation,(float)rotationX,(float)rotationY,(float)rotationZ,(float)scale);
		return transformVector(vectorForTransformation, transform);
	}
	public static Vec3 transformVector(Vec3 vectorForTransformation,Matrix4f transformation){
		return transformVector(Vec3M.conv(vectorForTransformation), transformation).conv();
	}
	public static Vec3M transformVector(Vec3M vectorForTransformation,Matrix4f transformation){
		Vector3f result=transformVector(new Vector3f((float)vectorForTransformation.x, (float)vectorForTransformation.y, (float)vectorForTransformation.z), transformation);
		return new Vec3M(result.x, result.y, result.z);
	}
	public static Vector3f transformVector(Vector3f vectorForTransformation,Matrix4f transformation){
		if(vectorForTransformation==null)vectorForTransformation=new Vector3f();
		Vector4f vec4=Matrix4f.transform(transformation, new Vector4f(vectorForTransformation.x,vectorForTransformation.y,vectorForTransformation.z,1), null);
		vectorForTransformation.x=vec4.x;
		vectorForTransformation.y=vec4.y;
		vectorForTransformation.z=vec4.z;
		return vectorForTransformation;
	}
	public static void glTranslatep(BlockPos pos){
		glTranslate(new float[]{pos.getX(),pos.getY(),pos.getZ()});
	}
	public static void protect(){
		glColor4f(1, 1, 1, 1);
		glLighting(true);
		resetOpacity();
		glDepthMask(true);
		glPushMatrix();
	}
	public static void endProtection(){
		GL11.glColor4f(1, 1, 1, 1);
		glPopMatrix();
	}
	public static void glColor(ColorF color){
		GL11.glColor4f(color.r, color.g, color.b, color.a);
	}
	public static void glColor(int color){
		GL11.glColor4f((color>>16&255)/255.0F, (color>>8&255)/255.0F, (color & 255)/255.0F, (color>>24&255)/255.0F);
	}
	@Deprecated
	public static int textureId;
	@Deprecated
	public static void captureTexture(){
		try{
			Integer id=(Integer)DataStalker.getVariable(GlStateManager.class, "activeTextureUnit").get(null);
			Object texture=((Object[])DataStalker.getVariable(GlStateManager.class, "textureState").get(null))[id];
			textureId=(Integer)DataStalker.getVariable(texture.getClass(), "textureName", texture);
		}catch(Exception e){}
	}
	@Deprecated
	public static void setTexture(){
		GlStateManager.bindTexture(textureId);
	}
}
