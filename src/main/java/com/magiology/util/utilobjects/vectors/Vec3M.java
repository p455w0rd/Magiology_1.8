package com.magiology.util.utilobjects.vectors;

import java.io.Serializable;
import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.ReadableVector;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.WritableVector3f;

import com.magiology.util.renderers.GL11U;
import com.magiology.util.utilclasses.UtilM;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *	Copy of mc Vec3M because mc didn't heard or a word called convenient 
 */
public class Vec3M extends Vector implements Serializable, ReadableVector, ReadableVector3f, WritableVector3f{
	public double x,y,z;

	public Vec3M(){
		this(0, 0, 0);
	}
	public Vec3M(double x, double y, double z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public Vec3M subtractReverse(Vec3M vec){
		return new Vec3M(vec.x-this.x,vec.x-this.y,vec.x-this.z);
	}
	public Vec3M subtractReverse(double x,double y,double z){
		return new Vec3M(x-this.x,x-this.y,x-this.z);
	}
	public Vec3M normalize(){
		double d0 = MathHelper.sqrt_double(this.x * this.x + this.y * this.y + this.z * this.z);
		return d0 < 1.0E-4D ? new Vec3M(0.0D, 0.0D, 0.0D) : new Vec3M(this.x / d0, this.y / d0, this.z / d0);
	}
	public double dotProduct(Vec3M vec){
		return this.x * vec.x + this.y * vec.x + this.z * vec.x;
	}
	public Vec3M crossProduct(Vec3M vec){
		return new Vec3M(this.y * vec.x - this.z * vec.x, this.z * vec.x - this.x * vec.x, this.x * vec.x - this.y * vec.x);
	}

	public Vec3M subtract(Vec3M vec){
		return this.subtract(vec.x, vec.y, vec.z);
	}

	public Vec3M subtract(double x, double y, double z){
		return this.addVector(-x, -y, -z);
	}

	public Vec3M addVector(Vec3M vec){
		return this.addVector(vec.x, vec.y, vec.z);
	}
	public Vec3M addVector(double x, double y, double z){
		return new Vec3M(this.x + x, this.y + y, this.z + z);
	}
	public double distanceTo(Vec3M vec){
		double d0 = vec.x - this.x;
		double d1 = vec.y - this.y;
		double d2 = vec.z - this.z;
		return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
	}
	public double distanceTo(double x, double y, double z){
		double d0 = x - this.x;
		double d1 = y - this.y;
		double d2 = z - this.z;
		return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
	}
	public double distanceTo(Vec3i pos){
		double d0 = pos.getX() - this.x;
		double d1 = pos.getY() - this.y;
		double d2 = pos.getZ() - this.z;
		return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
	}
	public double squareDistanceTo(Vec3M vec){
		double d0 = vec.x - this.x;
		double d1 = vec.x - this.y;
		double d2 = vec.x - this.z;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}
	public double lengthVector(){
		return MathHelper.sqrt_double(this.x * this.x + this.y * this.y + this.z * this.z);
	}
	public Vec3M getIntermediateWithXValue(Vec3M vec, double x){
		double d1 = vec.x - this.x;
		double d2 = vec.x - this.y;
		double d3 = vec.x - this.z;

		if (d1 * d1 < 1.0000000116860974E-7D)
		{
			return null;
		}
		else
		{
			double d4 = (x - this.x) / d1;
			return d4 >= 0.0D && d4 <= 1.0D ? new Vec3M(this.x + d1 * d4, this.y + d2 * d4, this.z + d3 * d4) : null;
		}
	}
	public Vec3M getIntermediateWithYValue(Vec3M vec, double y){
		double d1 = vec.x - this.x;
		double d2 = vec.x - this.y;
		double d3 = vec.x - this.z;

		if (d2 * d2 < 1.0000000116860974E-7D)
		{
			return null;
		}
		else
		{
			double d4 = (y - this.y) / d2;
			return d4 >= 0.0D && d4 <= 1.0D ? new Vec3M(this.x + d1 * d4, this.y + d2 * d4, this.z + d3 * d4) : null;
		}
	}
	public Vec3M getIntermediateWithZValue(Vec3M vec, double z){
		double d1 = vec.x - this.x;
		double d2 = vec.x - this.y;
		double d3 = vec.x - this.z;

		if (d3 * d3 < 1.0000000116860974E-7D)
		{
			return null;
		}
		else
		{
			double d4 = (z - this.z) / d3;
			return d4 >= 0.0D && d4 <= 1.0D ? new Vec3M(this.x + d1 * d4, this.y + d2 * d4, this.z + d3 * d4) : null;
		}
	}

	@Override
	public String toString(){
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}

	public Vec3M rotatePitch(float pitch){
		float f1 = MathHelper.cos(pitch);
		float f2 = MathHelper.sin(pitch);
		double d0 = this.x;
		double d1 = this.y * f1 + this.z * f2;
		double d2 = this.z * f1 - this.y * f2;
		return new Vec3M(d0, d1, d2);
	}

	public Vec3M rotateYaw(float yaw){
		float f1 = MathHelper.cos(yaw);
		float f2 = MathHelper.sin(yaw);
		double d0 = this.x * f1 + this.z * f2;
		double d1 = this.y;
		double d2 = this.z * f1 - this.x * f2;
		return new Vec3M(d0, d1, d2);
	}
	@Override
	public float lengthSquared(){
		return (float)(x*x+y*y+z*z);
	}
	@Override
	public Vector load(FloatBuffer buf){
		x = buf.get();
		y = buf.get();
		z = buf.get();
		return this;
	}
	@Override
	public Vector negate(){
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	@Override
	public Vector store(FloatBuffer buf){
		buf.put((float) x);
		buf.put((float) y);
		buf.put((float) z);
		return this;
	}
	@Override
	public Vector scale(float scale){
		x *= scale;
		y *= scale;
		z *= scale;
		return this;
	}
	@Override
	public float getX(){
		return (float) x;
	}
	@Override
	public float getY() {
		return (float) y;
	}
	@Override
	public void setX(float x) {
		this.x=x;
	}
	@Override
	public void setY(float y) {
		this.y=y;
	}
	@Override
	public void set(float x, float y) {
		setX(x);
		setY(y);
	}
	@Override
	public void setZ(float z) {
		this.z=z;
	}
	@Override
	public void set(float x, float y, float z) {
		set(x, y);
		setZ(z);
	}
	@Override
	public float getZ() {
		return (float) z;
	}
	public static Vec3M conv(Vec3 look){
		return new Vec3M(look.xCoord,look.yCoord,look.zCoord);
	}
	public Vec3 conv(){
		return new Vec3(getX(), getY(), getZ());
	}
	public void rotateAroundX(float par1)
	{
		float f1 = MathHelper.cos(par1);
		float f2 = MathHelper.sin(par1);
		double d0 = this.x;
		double d1 = this.y * f1 + this.z * f2;
		double d2 = this.z * f1 - this.y * f2;
		this.x = d0;
		this.y = d1;
		this.z = d2;
	}

	/**
	 * Rotates the vector around the y axis by the specified angle.
	 */
	public void rotateAroundY(float par1)
	{
		float f1 = MathHelper.cos(par1);
		float f2 = MathHelper.sin(par1);
		double d0 = this.x * f1 + this.z * f2;
		double d1 = this.y;
		double d2 = this.z * f1 - this.x * f2;
		this.x = d0;
		this.y = d1;
		this.z = d2;
	}

	@SideOnly(Side.CLIENT)

	/**
	 * Rotates the vector around the z axis by the specified angle.
	 */
	public void rotateAroundZ(float par1)
	{
		float f1 = MathHelper.cos(par1);
		float f2 = MathHelper.sin(par1);
		double d0 = this.x * f1 + this.y * f2;
		double d1 = this.y * f1 - this.x * f2;
		double d2 = this.z;
		this.x = d0;
		this.y = d1;
		this.z = d2;
	}
	public Vec3M add(BlockPos pos){
		return new Vec3M(getX()+pos.getX(), getY()+pos.getY(), getZ()+pos.getZ());
	}
	public Vec3M mul(double value){
		return new Vec3M(getX()*value, getY()*value, getZ()*value);
	}
	public Vec3M mul(double x, double y, double z){
		return new Vec3M(getX()*x, getY()*y, getZ()*z);
	}
	public Vec3M mul(Vec3M vec){
		return new Vec3M(getX()*vec.getX(), getY()*vec.getY(), getZ()*vec.getZ());
	}

	public Vec3M reflect(Vec3M normal){
		Vec3M norm = normal.normalize();
		Vec3M base=normalize();
		Vec3M difference=base.subtract(norm);

		Matrix4f rot=new Matrix4f();
		rot.rotate((float)Math.PI,norm.toLWJGLVec());
		difference=GL11U.transformVector(difference,rot);
		return norm.addVector(difference);
	}

	private Vector3f toLWJGLVec(){
		return  new Vector3f(getX(),getY(),getZ());
	}
	public double lightProduct(Vec3M vec){
		return 1-UtilM.snap(normalize().distanceTo(vec.normalize()), 0, 1);
	}
	public Vec3M copy(){
		return new Vec3M(x,y,z);
	}
	public Vec3M offset(EnumFacing facing){
		return offset(facing,1);
	}
	public Vec3M offset(EnumFacing facing, float mul){
		return new Vec3M(x+facing.getFrontOffsetX()*mul, y+facing.getFrontOffsetY()*mul, z+facing.getFrontOffsetZ()*mul);
	}
	public float distanceTo(Vec3 vec){
		double d0 = vec.xCoord - this.x;
		double d1 = vec.yCoord - this.y;
		double d2 = vec.zCoord - this.z;
		return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
	}
	public Vec3M abs(){
		return new Vec3M(Math.abs(x), Math.abs(y), Math.abs(z));
	}
}