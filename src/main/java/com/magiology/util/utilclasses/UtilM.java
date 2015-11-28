package com.magiology.util.utilclasses;

import static com.mojang.realmsclient.gui.ChatFormatting.*;

import java.awt.*;
import java.io.*;
import java.lang.reflect.*;
import java.math.*;
import java.text.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.*;

import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.particle.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.event.world.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.*;

import org.apache.commons.lang3.*;

import com.google.common.collect.*;
import com.magiology.core.*;
import com.magiology.forgepowered.events.client.*;
import com.magiology.forgepowered.packets.core.*;
import com.magiology.forgepowered.packets.core.AbstractToClientMessage.SendingTarget.TypeOfSending;
import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.util.renderers.*;
import com.magiology.util.utilclasses.math.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.vectors.*;
import com.mojang.realmsclient.gui.*;

public class UtilM{
	public class U extends UtilM{}
	private static boolean inited=false;
	public static void initHelper(){
		if(inited)return;
		inited=true;
//		MEvents.EventRegister(0, new Helper());
	}
	
	
	
	
	
	static Random rand=new Random();
	public  static final float p=1F/16F;
	@SideOnly(value=Side.CLIENT)
    public static void spawnEntityFX(EntityFX particleFX){
		if(isRemote(particleFX)){
			Minecraft mc=U.getMC();
			Entity ent=mc.getRenderViewEntity();
			if(mc!=null&&ent!=null&&mc.effectRenderer!=null){
				int i=mc.gameSettings.particleSetting;
	            double d6=ent.posX-particleFX.posX,d7=ent.posY-particleFX.posY,d8=ent.posZ-particleFX.posZ,d9=Math.sqrt(mc.gameSettings.renderDistanceChunks)*45;
	            if(i>1);else{
	            	if (d6*d6+d7*d7+d8*d8>d9*d9);else if(RB(Config.getParticleAmount()))Get.Render.ER().addEffect(particleFX);
	            }
			}
		}
	}
	@SideOnly(value=Side.CLIENT)
	public static void spawnEntityFX(EntityFX particleFX,int distance){
		if(particleFX.worldObj.isRemote){
			Minecraft mc=U.getMC();
			Entity ent=mc.getRenderViewEntity();
			if(mc!=null&&ent!=null&&mc.effectRenderer!=null){
				int i=mc.gameSettings.particleSetting;
				double d6=ent.posX-particleFX.posX,d7=ent.posY-particleFX.posY,d8=ent.posZ-particleFX.posZ;
				if(i>1);else{
					if (d6*d6+d7*d7+d8*d8>distance*distance);else if(RB(Config.getParticleAmount()))Get.Render.ER().addEffect(particleFX);
				}
			}
		}
	}
	public static Entity spawnEntity(Entity entity){
		if(isRemote(entity))return null;
		entity.worldObj.spawnEntityInWorld(entity);
		entity.forceSpawn=true;
		return entity;
	}
	@SideOnly(value=Side.CLIENT)public static Minecraft getMC(){return Minecraft.getMinecraft();}
	@SideOnly(value=Side.CLIENT)public static World getTheWorld(){return U.getMC().theWorld;}
	@SideOnly(value=Side.CLIENT)public static EntityPlayer getThePlayer(){return U.getMC().thePlayer;}
	public static int booleanToInt(boolean bool){if(bool)return 1;return 0;}
	public static boolean intToBoolean(int i){return i==1;}
	public static float CRandF(double scale){return (float)((0.5-rand.nextFloat())*scale);}
	public static double CRandD(double scale){return (0.5-rand.nextDouble())*scale;}
	public static int CRandI(int scale){return (scale-rand.nextInt(scale*2));}
	public static int RInt(int scale){return rand.nextInt(scale);}
	public static float RF(){return rand.nextFloat();}
	public static float RF(double scale){return (float)(RF()*scale);}
	public static double RD(){return rand.nextDouble();}
	public static double RD(double scale){return RD()*scale;}
	public static boolean RB(){return rand.nextBoolean();}
	public static long RL(){return rand.nextLong();}
	/**
	 * this method returns a random boolean with a custom chance of getting true.
	 * The higher the number is the higher chance will be for getting a true return.
	 * Type in a number higher or equal to 0 and lower or equal to 1.
	 * @param precentage
	 * @return
	 */
	public static boolean RB(double precentage){
		precentage=snap(precentage, 0, 1);
		if(precentage==0)return false;
		if(precentage==1)return true;
		return RF()<precentage;
	}
	/**
	 * this method returns a random boolean with a custom chance of getting true.
	 * The higher the number is the lower chance will be for getting a true return.
	 * Type in a number higher or equal to 1.
	 * @param precentage
	 * @return
	 */
	public static boolean RB(int precentage){
		precentage=Math.max(precentage, 1);
		if(precentage==1)return true;
		return RInt(precentage)==0;
	}
	public static double[] cricleXZ(double angle){
		double[] result={0,0};
		int intAngle=(int)angle;
		result[0]=CricleUtil.sin(intAngle);//-X-
		result[1]=CricleUtil.cos(intAngle);//-Z-
		return result;
	}
	public static double[] cricleXZForce(double angle,double ofset){
		double[] result={0,0};
		angle+=ofset;
		int intAngle=(int)angle;
		result[0]=CricleUtil.sin(intAngle);//-X-
		result[1]=CricleUtil.cos(intAngle);//-Z-
		return result;
	}
	public static double[] cricleXZwSpeed(double angle,double ofset){
		double[] result={0,0,0,0};
		{
			int intAngle=(int)angle;
			result[0]=CricleUtil.sin(intAngle);//-X-
			result[1]=CricleUtil.cos(intAngle);//-Z-
			angle+=ofset;}{
			int intAngle=(int)angle;
			result[0]=CricleUtil.sin(intAngle);//-X-
			result[1]=CricleUtil.cos(intAngle);//-Z-
		}
		return result;
	}
	public static double slowlyEqalize(double variable,double goal,double speed){
		if(speed==0)return variable;
		speed=Math.abs(speed);
		if(variable+speed>goal&&(Math.abs((variable+speed)-goal)<speed*1.001))return goal;
		if(variable-speed<goal&&(Math.abs((variable-speed)-goal)<speed*1.001))return goal;
		
		if(variable<goal)variable+=speed;
		else if(variable>goal)variable-=speed;
		return variable;
	}
	public static boolean isEqualInBouds(double variable,double wantedVariable,double bounds){
		//10.02==10? 0.5
		//10.52>10 9.52<10
		if(variable+bounds>wantedVariable&&variable-bounds<wantedVariable)return true;
		return false;
	}
	/**
	 * Returns false if all objects are not null and it returns true if any of object/s are true
	 * Note: you'll might need to add "!" on using it
	 * @param objects
	 * @return
	 */
	public static boolean isNull(Object...objects){
		for(int a=0;a<objects.length;a++)if(objects[a]==null)return true;
		return false;
	}
	/**
	 * Returns if stack contains a specific item
	 * Note: no danger of null pointer exception!
	 * @param item
	 * @param stack
	 * @return
	 */
	public static boolean isItemInStack(Item item,ItemStack stack){
		if(stack!=null&&stack.getItem()==item)return true;
		return false;
	}
	/**
     * Creates a x,y,z offset coordinate of a ball. (can create 2 coordinates)
     * Args:x,y,z particle speed, size
	 * @param ballSize
	 * @param hasSecondPos
	 * @return
	 */
	public static double[] createBallXYZ(double ballSize, boolean hasSecondPos){
		int xRot=RInt(360),yRot=RInt(360);
		double[] result=new double[3*(hasSecondPos?2:1)];
		result[0]=CricleUtil.sin(xRot)*CricleUtil.cos(yRot);//-X-
		result[1]=CricleUtil.sin(yRot);//-Y-
		result[2]=CricleUtil.cos(xRot)*CricleUtil.cos(yRot);//-Z-
		if(hasSecondPos){
			xRot+=CRandI(50);
			yRot+=CRandI(50);
			result[3]=CricleUtil.sin(xRot)*CricleUtil.cos(yRot);//-X-
			result[4]=CricleUtil.sin(yRot);//-Y-
			result[5]=CricleUtil.cos(xRot)*CricleUtil.cos(yRot);//-Z-
		}
		for(int a=0;a<result.length;a++)result[a]*=ballSize;
    	return result;
    }
	
	public static ItemStack[] loadItemsFromNBT(NBTTagCompound NBTTC,String baseName,ItemStack[] stacks){
		int NumberOfSlots=stacks.length;
		NBTTagList list= NBTTC.getTagList(baseName+"Slots", 10);
		stacks=new ItemStack[NumberOfSlots];
    	for(int i=0;i<list.tagCount();i++){
    		NBTTagCompound item=list.getCompoundTagAt(i);
    		byte b=item.getByte(baseName);
    		if(b>=0&&b<stacks.length){
    			stacks[b]=ItemStack.loadItemStackFromNBT(item);
    		}
    	}
		return stacks;
	}
	public static void saveItemsToNBT(NBTTagCompound NBTTC,String baseName,ItemStack[] stacks){
		NBTTagList list= new NBTTagList();
		for(int i=0;i<stacks.length;i++){
			if(stacks[i]!=null){
				NBTTagCompound item=new NBTTagCompound();
				item.setByte(baseName, (byte)i);
				stacks[i].writeToNBT(item);
				list.appendTag(item);
			}
		}
		NBTTC.setTag(baseName+"Slots", list);
	}
	//Print cluster----------------------------------------
	public static void print(Object... objs){
		StringBuilder print=new StringBuilder();
		for(Object a:objs)print.append(a+" ");
		LogUtil.info(print.toString());
	}
	public static void printlnEr(Object... objs){
		StringBuilder print=new StringBuilder();
		for(Object a:objs)print.append(a+" \n");
		LogUtil.info(print.toString());
	}
	public static void println(Object... objs){
		StringBuilder print=new StringBuilder();
		for(Object a:objs){
			if(a!=null&&a.getClass().isArray()){
				if(a instanceof boolean[]){
					boolean[] b=(boolean[])a;
					for(boolean c:b)print.append(c+" ");
				}
				else if(a instanceof float[]){
					float[] b=(float[])a;
					for(float c:b)print.append(c+" ");
				}
				else if(a instanceof int[]){
					int[] b=(int[])a;
					for(int c:b)print.append(c+" ");
				}
				else if(a instanceof double[]){
					double[] b=(double[])a;
					for(double c:b)print.append(c+" ");
				}
				else if(a instanceof Object[]){
					Object[] b=(Object[])a;
					for(Object c:b)print.append(c+" ");
				}
			}
			else print.append(a+" ");
		}
		LogUtil.info(print.toString());
	}
	public static<T> T printlnAndReturn(T obj){
		println(obj);
		return obj;
	}
	public static<T> T printAndReturn(T obj){
		print(obj);
		return obj;
	}
	
	public static Object callMethod(Method method,Object objFromWhereFunctionIsCalled,Object...argsOfTheCalledFunction){
		try{
			return method.invoke(objFromWhereFunctionIsCalled, argsOfTheCalledFunction);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static boolean isInteger(String str){
		if(str==null)return false;
		int length=str.length();
		if(length==0)return false;
		int i=0;
		if(str.charAt(0)=='-'){
			if(length==1)return false;
			i=1;
		}for(;i<length;i++){
			char c=str.charAt(i);
			if(c<='/'||c>=':')return false;
		}return true;
	}
	public static boolean isBoolean(String str){
		if(str==null)return false;
		return str.equals("true")||str.equals("false");
	}
	public static double snap(double value,double min,double max){
		if(min>=max)return value;
		if(value<min)value=min;
		if(value>max)value=max;
		return value;
	}
	public static float snap(float value,float min,float max){
		return (float)snap((double)value, (double)min, (double)max);
	}
	public static int snap(int value,int min,int max){
		return (int)snap((double)value, (double)min, (double)max);
	}
	public static MovingObjectPosition rayTrace(EntityLivingBase entity,float lenght, float var1){
		if(entity.worldObj.isRemote)return entity.rayTrace(lenght, var1);
		
		Vec3M vec3 =new Vec3M(entity.posX, entity.posY, entity.posZ);
		Vec3M vec31=Vec3M.conv(entity.getLook(var1));
		Vec3M vec32=vec3.addVector(vec31.x*var1, vec31.y*var1, vec31.z*var1);
		return entity.worldObj.rayTraceBlocks(vec3.conv(), vec32.conv(), false, false, true);
	}
	public static void sendMessage(AbstractPacket message){
		if(isNull(message))return;
		if(message instanceof AbstractToServerMessage)Magiology.NETWORK_CHANNEL.sendToServer(message);
		else if(message instanceof AbstractToClientMessage){
			AbstractToClientMessage msg=(AbstractToClientMessage)message;
			if(msg.target!=null&&!msg.target.world.isRemote){
				     if(msg.target.typeOfSending==TypeOfSending.ToPlayer)Magiology.NETWORK_CHANNEL.sendTo(message,(EntityPlayerMP)msg.target.player);
				else if(msg.target.typeOfSending==TypeOfSending.ToAll)Magiology.NETWORK_CHANNEL.sendToAll(message);
				else if(msg.target.typeOfSending==TypeOfSending.AroundPoint)Magiology.NETWORK_CHANNEL.sendToAllAround(msg, msg.target.point);
				else if(msg.target.typeOfSending==TypeOfSending.ToDimension)Magiology.NETWORK_CHANNEL.sendToDimension(msg, msg.target.world.provider.getDimensionId());
			}
		}
	}
	public static float[][] calculateDoublePosArray(float[][] prevPos,float[][] pos){
		if(pos.length!=prevPos.length)return null;
		float[][] result=null;
		for(int a=0;a<pos.length;a++)result=ArrayUtils.add(result, calculatePosArray(prevPos[a], pos[a]));
		return result;
	}
	public static float[] calculatePosArray(final float[] prevPos,final float[] pos){
		if(pos.length!=prevPos.length)return null;
		float[] result=null;
		for(int a=0;a<pos.length;a++)result=ArrayUtils.add(result, calculatePos(prevPos[a], pos[a]));
		return result;
	}

	public static float calculatePos(final double prevPos,final double pos){
		return (float)(prevPos+(pos-prevPos)*RenderLoopEvents.partialTicks);
	}
	public static float[][] addToDoubleFloatArray(final float[][] array1,final float[][] array2){
		float[][] result=array1.clone();
		for(int a=0;a<result.length;a++)result[a]=addToFloatArray(result[a], array2[a]);
		return result;
	}
	public static float[] addToFloatArray(final float[] array1,final float[] array2){
		float[] result=array1.clone();
		for(int a=0;a<result.length;a++)result[a]+=array2[a];
		return result;
	}
	public static float handleSpeedFolower(float speed, float pos,float wantedPos,float acceleration){
	return (float)handleSpeedFolower((double)speed, (double)pos, (double)wantedPos, (double)acceleration);}
	public static double handleSpeedFolower(double speed, double pos,double wantedPos,double acceleration){
		if(pos==wantedPos)return speed;
		if(pos>wantedPos)speed-=acceleration;
		else speed+=acceleration;
		return speed;
	}
	public static void playSoundAtEntity(Object name,Entity entity,double volume,double pitch){
		if(entity.worldObj.isRemote)return;
        entity.worldObj.playSoundAtEntity(entity,(MReference.MODID+":"+name.toString()),(float)volume,(float)pitch);
	}
	@SideOnly(value=Side.CLIENT)
	public static int getFPS(){
		return Minecraft.getDebugFPS();
	}
	public static World getWorld(Object object){
		if(object instanceof Entity)return((Entity)object).worldObj;
		if(object instanceof World)return((World)object);
		if(object instanceof TileEntity)return((TileEntity)object).getWorld();
		if(object instanceof EntityEvent)return((EntityEvent)object).entity.worldObj;
		if(object instanceof BlockEvent)return((BlockEvent)object).world;
		if(object instanceof HoloObject)return ((HoloObject)object).host.getWorld();
		println("Given object has no data reference to world!");
		return null;
	}
	public static boolean isRemote(Object object){
		return getWorld(object).isRemote;
	}
	public static boolean isArray(Object object){
		if(object!=null){
			if(object instanceof Class)return((Class)object).isArray();
			return object.getClass().isArray();
		}
		return false;
	}
	public static<T>boolean isInArray(T object,T[] array){
		return getPosInArray(object, array)>=0;
	}
	public static<T>int getPosInArray(T object,T[] array){
		if(isNull(object,array))return -1;
		if(array.length==0||isArray(object))return -1;
		int pos=-2;
		for(int a=0;a<array.length;a++)if(array[a]==object){
			pos=a;a=array.length;
		}
		return pos;
	}
	public static void exit(int Int){
		FMLCommonHandler.instance().exitJava(Int, false);
	}
	@SideOnly(value=Side.CLIENT)
	public static void exitSoft(){
		getMC().shutdown();
	}
	public static boolean isAny(Object tester,Object... objects){
		for(int a=0;a<objects.length;a++)if(tester==objects[a])return true;
		return false;
	}
	public static float fluctuatorSmooth(double speed,double offset){
		float
			fluctuator=fluctuator(speed, offset),
			prevFluctuator=fluctuator(speed, offset-1);
		return calculatePos(prevFluctuator, fluctuator);
	}
	public static float fluctuator(double speed,double offset){
		long wtt=(long)(getTheWorld().getTotalWorldTime()+offset);
		double helper=(wtt%speed)/(speed/2F);
		return (float) (helper>1?2-helper:helper);
	}
	public static int colorToCode(Color color){
		return color.hashCode();
	}
	public static int rgbByteToCode(int r,int g,int b,int alpha){
		return colorToCode(new Color(r, g, b, alpha));
	}
	public static int rgbPrecentageToCode(double r,double g,double b,double alpha){
		int r1=(int)(255*r), g1=(int)(255*g), b1=(int)(255*b), alpha1=(int)(255*alpha);
		return rgbByteToCode(r1, g1, b1, alpha1);
	}
	public static int[] colorToRGBABByte(Color color){
		return new int[]{color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha()};
	}
	public static int[] codeToRGBABByte(int code){
		return colorToRGBABByte(new Color(code));
	}
	public static float[] colorToRGBABPrecentage(Color color){
		int[] data=colorToRGBABByte(color);
		return new float[]{(data[0])/255F,(data[1])/255F,(data[2])/255F,(data[3])/255F};
	}
	public static float[] codeToRGBABPrecentage(int code){
		return colorToRGBABPrecentage(new Color(code,true));
	}
	public static ColorF codeToColorF(int code){
		float[] data=codeToRGBABPrecentage(code);
		return new ColorF(data[0],data[1],data[2],data[3]);
	}
	public static boolean intersectLinePlane(Ray ray,Plane plane, Vec3M result){
		if(result==null){
			println("Result is null!\nResult can't be set if it is null!\nInitialize it!\n------------");
			return false;
		}
		
		boolean printProcess=false;
		
		
		boolean xz=
				plane.q.x==plane.r.x&&
				plane.r.x==plane.s.x&&
				plane.r.x==plane.s.x;
		if(xz){
			
		}else{
			
			if(ray.from.z>ray.to.z){
				Vec3M helper=ray.from;
				ray.from=ray.to;
				ray.to=helper;
			}
			double z=plane.q.z;
			if(ray.from.z>z){if(printProcess)println("target behind");return false;}
			if(ray.to.z<z){if(printProcess)println("target to far");return false;}
			AxisAlignedBB Plane=new AxisAlignedBB(plane.q.x, plane.q.y, plane.q.z, plane.s.x, plane.s.y, plane.s.z+0.01);
			MovingObjectPosition rayt=Plane.calculateIntercept(ray.from.addVector(0, 0.1, 0).conv(), ray.to.addVector(0, 0.1, 0).conv());
			if(rayt==null||rayt.hitVec==null){if(printProcess)println("target clipped out");return false;}
			result.x=rayt.hitVec.xCoord;
			result.y=rayt.hitVec.yCoord;
			result.z=rayt.hitVec.zCoord;
			
			if(printProcess)println("Ray trace has resolwed a valid intersection point!");
			return true;
		}
		
		return false;
	}
	
	public static Vec3M getNormal(Vec3M point1,Vec3M point2,Vec3M point3){
    	Vec3M angle1=point2.subtract(point1);
		Vec3M angle2=point2.subtract(point3);
		return angle2.crossProduct(angle1).normalize();
    }
	public static ColorF calculateRenderColor(ColorF prevColor, ColorF color){
		return new ColorF(calculatePos(prevColor.r, color.r),
						  calculatePos(prevColor.g, color.g),
						  calculatePos(prevColor.b, color.b),
						  calculatePos(prevColor.a, color.a));
	}
	public static ColorF slowlyEqalizeColor(ColorF variable, ColorF goal, float speed){
		return new ColorF(slowlyEqalize(variable.r, goal.r, speed),
						  slowlyEqalize(variable.g, goal.g, speed),
						  slowlyEqalize(variable.b, goal.b, speed),
						  slowlyEqalize(variable.a, goal.a, speed));
	}
    public static Vec3M getPosition(EntityPlayer entity,float par1)
    {
        if (par1 == 1.0F)
        {
            return new Vec3M(entity.posX, entity.posY + (entity.getEyeHeight()), entity.posZ);
        }
        else
        {
            double d0=entity.prevPosX + (entity.posX - entity.prevPosX) * par1;
            double d1=entity.prevPosY + (entity.posY - entity.prevPosY) * par1 + (entity.getEyeHeight());
            double d2=entity.prevPosZ + (entity.posZ - entity.prevPosZ) * par1;
            return new Vec3M(d0, d1, d2);
        }
    }
	public static String getStringForSize(String text, float allowedWidth){
		if(text.isEmpty())return text;
		String Return=""+text;
		while(TessUtil.getFontRenderer().getStringWidth(Return)>allowedWidth){
			Return=Return.substring(0, Return.length()-1);
		}
		return Return;
	}
	public static void printStackTrace(){
		print(getStackTrace());
	}
	public static String getStackTrace(){
		StringBuilder Return=new StringBuilder();
		
		StackTraceElement[] a1=Thread.currentThread().getStackTrace();
		int lenght=0;
		DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal=Calendar.getInstance();
		Return.append("Invoke time: "+dateFormat.format(cal.getTime())+"\n");
		for(int i=2;i<a1.length;i++){
			StackTraceElement a=a1[i];
			String s=a.toString();
			Return.append(s+"\n");
			lenght=Math.max(s.length(),lenght);
		}
		for(int b=0;b<lenght/4;b++)Return.append("_/\\_");
		
		return Return.toString();
	}
	public static void spawnEntityFXAt(TileEntity tileEntity, EntityFX entityFX){
		entityFX.posX+=x(tileEntity);
		entityFX.posY+=y(tileEntity);
		entityFX.posZ+=z(tileEntity);
		spawnEntityFX(entityFX);
	}
	public static EntityFlameFX marker(double x,double y,double z,double xSpeed,double ySpeed,double zSpeed){
		if(getTheWorld()==null)return null;
		return (EntityFlameFX)new EntityFlameFX.Factory().getEntityFX(0,getTheWorld(), x+0.5, y+0.5, z+0.5, xSpeed, ySpeed, zSpeed);
	}
	public static TargetPoint TargetPoint(TileEntity tile, int range){
		return new TargetPoint(tile.getWorld().provider.getDimensionId(), x(tile), y(tile), z(tile), range);
	}
	public static boolean Instanceof(Object tester,Object instace){
		return Instanceof(tester.getClass(), instace.getClass());
	}
	public static boolean Instanceof(Object tester,Class instace){
		return Instanceof(tester.getClass(), instace);
	}
	public static boolean Instanceof(Class tester,Class instace){
		try{
			tester.asSubclass(instace);
			return true;
		}catch(Exception e){}
		return false;
	}
	@Deprecated
	public static void breakBlock(World world,BlockPos pos){
//		world.playSoundEffect(pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5, Helper.getBlock(world, pos)., 1.0F, RF() * 0.4F + 0.8F);
		//TODO
	}
	public static EntityItem dropBlockAsItem(World world, double x, double y, double z, ItemStack stack){
	    if(!world.isRemote&&world.getGameRules().getGameRuleBooleanValue("doTileDrops")&&!world.restoringBlockSnapshots){
	        EntityItem entity=new EntityItem(world,x,y,z,stack);
	        entity.setPickupDelay(0);
	        world.spawnEntityInWorld(entity);
	        return entity;
	    }
		return null;
	}
	public static boolean AxisAlignedBBEqual(AxisAlignedBB box1, AxisAlignedBB box2){
		if(box1==box2)return true;
		if(isNull(box1,box2))return false;
		return box1.minX==box2.minX&&box1.minY==box2.minY&&box1.minZ==box2.minZ&&box1.maxX==box2.maxX&&box1.maxY==box2.maxY&&box1.maxZ==box2.maxZ;
	}
	//yay for 1.8 code changes
	/** thanks mc for this incredibly convenient code so i I need to make a helper for things that should not need one... */
	public static final PropertyInteger META=PropertyInteger.create("meta", 0, 15);
	public static int getBlockMetadata(World world, BlockPos pos){
		return hasMetaState(world, pos)?getBlock(world, pos).getMetaFromState(world.getBlockState(pos)):0;
	}
	public static void setMetadata(World world, BlockPos pos,int meta){
		if(hasMetaState(world, pos))world.setBlockState(pos, world.getBlockState(pos).withProperty(META, meta), 3);
	}
	
	public static boolean hasMetaState(World world, BlockPos pos){
		ImmutableMap i=world.getBlockState(pos).getProperties();
		return i.keySet().contains(META);
	}
	
	public static Block getBlock(IBlockAccess world, BlockPos pos){
		return world.getBlockState(pos).getBlock();
	}
	public static Block getBlock(World world, int x, int y, int z){
		return getBlock(world, new BlockPos(x, y, z));
	}
	public static void setBlock(World world, BlockPos pos,Block block){
		world.setBlockState(pos, block.getDefaultState(), 3);
	}
	public static void setBlock(World world, BlockPos pos, Block block, int meta){
		world.setBlockState(pos, block.getDefaultState().withProperty(META, meta), 3);
	}
	public static BlockPos BlockPos(int[] array3i){
		return new BlockPos(array3i[0], array3i[1], array3i[2]);
	}
	public static int x(TileEntity tile){
		return tile.getPos().getX();
	}
	public static int y(TileEntity tile){
		return tile.getPos().getZ();
	}
	public static int z(TileEntity tile){
		return tile.getPos().getY();
	}
	public static AxisAlignedBB setAABB(AxisAlignedBB box,String varName, double value){
		if(varName.length()!=4)throw new IllegalStateException("use minY, minZ, maxX, maxY, maxZ");
		if(varName.startsWith("min")){
			if(varName.endsWith("X"))return new AxisAlignedBB(value, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
			if(varName.endsWith("Y"))return new AxisAlignedBB(box.minX, value, box.minZ, box.maxX, box.maxY, box.maxZ);
			if(varName.endsWith("Z"))return new AxisAlignedBB(box.minX, box.minY, value, box.maxX, box.maxY, box.maxZ);
		}else if(varName.startsWith("max")){
			if(varName.endsWith("X"))return new AxisAlignedBB(box.minX, box.minY, box.minZ, value, box.maxY, box.maxZ);
			if(varName.endsWith("Y"))return new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, value, box.maxZ);
			if(varName.endsWith("z"))return new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, value);
		}
		return null;
	}
	public static BlockPos[] BlockPosArray(int[] pos1, int[] pos2, int[] pos3){
		BlockPos[] result=new BlockPos[0];
		for(int i=0;i<pos1.length;i++)result=ArrayUtils.add(result, new BlockPos(pos1[i],pos2[i],pos3[i]));
		return result;
	}
	public static void printIsRemote(Object worldContainer){
		println(isRemote(worldContainer));
	}
	public static boolean TRUE(){
		return true;
	}
	public static boolean FALSE(){
		return false;
	}
	public static String signature(){
		return signature(RESET);
	}
	public static String signature(ChatFormatting... colorAfter){
		String result=GOLD+"["+ChatFormatting.DARK_GREEN+MReference.NAME+GOLD+"] ";
		for(ChatFormatting a:colorAfter)result+=a;
		return result;
	}
	@SideOnly(value=Side.CLIENT)
	public static float getGuiScale(){
		return Math.max(getGuiScaleRaw()/4F,1);
	}
	@SideOnly(value=Side.CLIENT)
	public static int getGuiScaleRaw(){
		return new ScaledResolution(getMC(), getMC().displayWidth, getMC().displayHeight).getScaleFactor();
	}
	public static void sleep(int time){
		try{
			Thread.sleep(time);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static Vec3M getEntityPos(Entity entity){
		return new Vec3M(entity.posX, entity.posY, entity.posZ);
	}
	public static double getDistance(Entity entity,int x,int y, int z){
		Vec3M entityPos=new Vec3M(entity.posX, entity.posY, entity.posZ);
		Vec3M blockPos=new Vec3M(x+0.5, y+0.5, z+0.5);
		return entityPos.distanceTo(blockPos);
	}
	public static double getDistance(TileEntity tile,int x,int y, int z){
		Vec3M entityPos=new Vec3M(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getY());
		Vec3M blockPos=new Vec3M(x+0.5, y+0.5, z+0.5);
		return entityPos.distanceTo(blockPos);
	}
	public static float round(float d, int decimalPlace){
		return BigDecimal.valueOf(d).setScale(decimalPlace,BigDecimal.ROUND_HALF_UP).floatValue();
	}
	public static long getWorldTime(Object worldContainer){
		return getWorld(worldContainer).getTotalWorldTime();
	}
	public static String[] stringNewlineSplit(String toSplit){
		return toSplit.split("\\r\\n|\\n\\r|\\r|\\n");
	}
	//
	public static boolean isRemote(){
		return FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT;
	}
	public static String join(Object[] args){
		StringBuilder result=new StringBuilder();
		for(Object o:args)result.append(o);
		return result.toString();
	}
	public static String join(CharSequence splitter,Object[] args){
		StringBuilder result=new StringBuilder();
		for(Object o:args)result.append(o).append(splitter);
		return result.substring(0, result.length()-splitter.length()+1);
	}
	
	private static long startTime;
	public static void startTime(){
		startTime=System.currentTimeMillis();
	}
	public static long endTime(){
		return System.currentTimeMillis()-startTime;
	}
	public static void printTime(){
		println(endTime());
	}
	public static <T, E> T getMapKey(Map<T, E> map, E value){
	    for(Entry<T, E> entry : map.entrySet()){
	        if(Objects.equals(value, entry.getValue())){
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	public static <T, E> Set<T> getMapKeySet(Map<T, E> map, E value){
		return 
			 map.entrySet()
			.stream()
			.filter(entry->Objects.equals(entry.getValue(), value))
			.map(Map.Entry::getKey)
			.collect(Collectors.toSet());
	}
	
	public static Object fromString(String s)throws IOException,ClassNotFoundException{
		byte[] data=Base64.getDecoder().decode(s);
		ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(data));
		Object o=ois.readObject();
		ois.close();
		return o;
	}
	public static String toString(Serializable o)throws IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ObjectOutputStream oos=new ObjectOutputStream( baos );
		oos.writeObject(o);
		oos.close();
		return Base64.getEncoder().encodeToString(baos.toByteArray()); 
	}
}
