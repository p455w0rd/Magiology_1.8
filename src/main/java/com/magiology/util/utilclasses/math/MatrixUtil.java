package com.magiology.util.utilclasses.math;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.util.Vec3;

public class MatrixUtil{
	
	public static Matrix4f createMatrix(Vec3M translation,float rotationX,float rotationY,float rotationZ, float scale){
		return createMatrix(new Vector3f((float)translation.x, (float)translation.y, (float)translation.z), rotationX, rotationY, rotationZ, scale);
	}
	
	public static Matrix4f rotateAt(Vec3M rotationOffset, Vec3M rot, Matrix4f mat){
		return rotateAt(rotationOffset.toLWJGLVec(), rot.toLWJGLVec(), mat);
	}
	public static Matrix4f rotateAt(Vector3f rotationOffset, Vector3f rot, Matrix4f mat){
		mat.translate(rotationOffset);
		rotate(rot, mat);
		mat.translate(rotationOffset.negate(null));
		return mat;
	}
	public static Matrix4f rotate(Vector3f rot, Matrix4f mat){
		Matrix4f.rotate((float)Math.toRadians(rot.x), new Vector3f(1, 0, 0), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.y), new Vector3f(0, 1, 0), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.z), new Vector3f(0, 0, 1), mat, mat);
		return mat;
	}
	public static Matrix4f rotate(Vec3M rot, Matrix4f mat){
		Matrix4f.rotate((float)Math.toRadians(rot.x), new Vector3f(1, 0, 0), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.y), new Vector3f(0, 1, 0), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.z), new Vector3f(0, 0, 1), mat, mat);
		return mat;
	}
	public static Matrix4f rotateZYX(Vec3M rot, Matrix4f mat){
		Matrix4f.rotate((float)Math.toRadians(rot.z), new Vector3f(0, 0, 1), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.y), new Vector3f(0, 1, 0), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.x), new Vector3f(1, 0, 0), mat, mat);
		return mat;
	}
	public static Matrix4f rotateZYX(Vector3f rot, Matrix4f mat){
		Matrix4f.rotate((float)Math.toRadians(rot.z), new Vector3f(0, 0, 1), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.y), new Vector3f(0, 1, 0), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.x), new Vector3f(1, 0, 0), mat, mat);
		return mat;
	}
	
	public static Matrix4f copy(Matrix4f mat){
		return mat.transpose(null);
	}
	
	public static Matrix4f createMatrix(Vector3f translation){
		return new Matrix4f().translate(translation);
	}
	
	public static Matrix4f createMatrixZYX(float rotationZ,float rotationY,float rotationX){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationZ), new Vector3f(0, 0, 1), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationY), new Vector3f(0, 1, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationX), new Vector3f(1, 0, 0), result, result);
		return result;
	}
	
	public static Matrix4f createMatrixX(float rotationX){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationX), new Vector3f(1, 0, 0), result, result);
		return result;
	}
	public static Matrix4f createMatrixY(float rotationY){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationY), new Vector3f(0, 1, 0), result, result);
		return result;
	}
	public static Matrix4f createMatrixZ(float rotationZ){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationZ), new Vector3f(0, 0, 1), result, result);
		return result;
	}
	public static Matrix4f createMatrixXY(float rotationX,float rotationY){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationX), new Vector3f(1, 0, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationY), new Vector3f(0, 1, 0), result, result);
		return result;
	}
	public static Matrix4f createMatrixYZ(float rotationY,float rotationZ){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationY), new Vector3f(0, 1, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationZ), new Vector3f(0, 0, 1), result, result);
		return result;
	}
	public static Matrix4f createMatrixXZ(float rotationX,float rotationZ){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationX), new Vector3f(1, 0, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationZ), new Vector3f(0, 0, 1), result, result);
		return result;
	}
	public static Matrix4f createMatrix(float rotationX,float rotationY,float rotationZ){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationX), new Vector3f(1, 0, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationY), new Vector3f(0, 1, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationZ), new Vector3f(0, 0, 1), result, result);
		return result;
	}
	
	public static Matrix4f createMatrix(float rotationX,float rotationY,float rotationZ, float scale){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationX), new Vector3f(1, 0, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationY), new Vector3f(0, 1, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationZ), new Vector3f(0, 0, 1), result, result);
		Matrix4f.scale(new Vector3f(scale, scale, scale), result, result);
		return result;
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
		Matrix4f transform=createMatrix(translation,(float)rotationX,(float)rotationY,(float)rotationZ,(float)scale);
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
	
}
