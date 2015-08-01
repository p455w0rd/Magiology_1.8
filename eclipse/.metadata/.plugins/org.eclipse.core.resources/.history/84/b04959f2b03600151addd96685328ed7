package com.magiology.objhelper.helpers;

import java.awt.Color;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderWorldEvent;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.core.Config;
import com.magiology.core.MReference;
import com.magiology.core.Magiology;
import com.magiology.core.init.MEvents;
import com.magiology.forgepowered.event.RenderLoopEvents;
import com.magiology.forgepowered.packets.AbstractPacket;
import com.magiology.forgepowered.packets.AbstractToClientMessage;
import com.magiology.forgepowered.packets.AbstractToClientMessage.SendingTarget.TypeOfSending;
import com.magiology.forgepowered.packets.AbstractToServerMessage;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.modedmcstuff.ColorF;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.vectors.Plane;
import com.magiology.objhelper.vectors.Ray;
import com.magiology.objhelper.vectors.Vec3F;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;

public class Helper{
	public class H extends Helper{}
	private static boolean inited=false;
	public static void initHelper(){
		if(inited)return;
		inited=true;
//		MEvents.EventRegister(0, new Helper());
	}
	
	
	
	
	
	static Random rand=new Random();
	public  static final float p=1F/16F;
    private static final float SMALL_NUM = 0.00000001f;
	
	public static void spawnEntityFX(EntityFX particleFX){
		if(particleFX.worldObj.isRemote){
			Minecraft mc=Minecraft.getMinecraft();
			if(mc!=null&&mc.renderViewEntity!=null&&mc.effectRenderer!=null){
				int i = mc.gameSettings.particleSetting;
	            double d6=mc.renderViewEntity.posX-particleFX.posX,d7=mc.renderViewEntity.posY-particleFX.posY,d8=mc.renderViewEntity.posZ-particleFX.posZ,d9=Math.sqrt(mc.gameSettings.renderDistanceChunks)*45;
	            if(i>1);else{
	            	if (d6*d6+d7*d7+d8*d8>d9*d9);else if(RB(Config.getParticleAmount()))Minecraft.getMinecraft().effectRenderer.addEffect(particleFX);
	            }
			}
		}
	}
	public static void spawnEntityFX(EntityFX particleFX,int distance){
		if(particleFX.worldObj.isRemote){
			Minecraft mc=Minecraft.getMinecraft();
			if(mc!=null&&mc.renderViewEntity!=null&&mc.effectRenderer!=null){
				int i = mc.gameSettings.particleSetting;
				double d6=mc.renderViewEntity.posX-particleFX.posX,d7=mc.renderViewEntity.posY-particleFX.posY,d8=mc.renderViewEntity.posZ-particleFX.posZ;
				if(i>1);else{
					if (d6*d6+d7*d7+d8*d8>distance*distance);else if(RB(Config.getParticleAmount()))Minecraft.getMinecraft().effectRenderer.addEffect(particleFX);
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
	public static Minecraft getMC(){return Minecraft.getMinecraft();}
	public static World getTheWorld(){return Minecraft.getMinecraft().theWorld;}
	public static EntityPlayer getThePlayer(){return Minecraft.getMinecraft().thePlayer;}
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
		precentage=keepAValueInBounds(precentage, 0, 1);
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
    public static Vec3 Vec3(){return Vec3(0,0,0);}
	public static Vec3 Vec3(double x, double y, double z){
		return Vec3.createVectorHelper(x, y, z);
	}
	public static double[] cricleXZ(double angle){
		double[] result={0,0};
		int intAngle=(int)angle;
		result[0]=Cricle.sin(intAngle);//-X-
		result[1]=Cricle.cos(intAngle);//-Z-
		return result;
	}
	public static double[] cricleXZForce(double angle,double ofset){
		double[] result={0,0};
		angle+=ofset;
		int intAngle=(int)angle;
		result[0]=Cricle.sin(intAngle);//-X-
		result[1]=Cricle.cos(intAngle);//-Z-
		return result;
	}
	public static double[] cricleXZwSpeed(double angle,double ofset){
		double[] result={0,0,0,0};
		{
			int intAngle=(int)angle;
			result[0]=Cricle.sin(intAngle);//-X-
			result[1]=Cricle.cos(intAngle);//-Z-
			angle+=ofset;}{
			int intAngle=(int)angle;
			result[0]=Cricle.sin(intAngle);//-X-
			result[1]=Cricle.cos(intAngle);//-Z-
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
		boolean result=false;
		for(int a=0;a<objects.length;a++)if(objects[a]==null){result=true;continue;}
		return result;
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
		result[0]=Cricle.sin(xRot)*Cricle.cos(yRot);//-X-
		result[1]=Cricle.sin(yRot);//-Y-
		result[2]=Cricle.cos(xRot)*Cricle.cos(yRot);//-Z-
		if(hasSecondPos){
			xRot+=CRandI(50);
			yRot+=CRandI(50);
			result[3]=Cricle.sin(xRot)*Cricle.cos(yRot);//-X-
			result[4]=Cricle.sin(yRot);//-Y-
			result[5]=Cricle.cos(xRot)*Cricle.cos(yRot);//-Z-
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
	private static void println(Object obj){
		System.out.print(obj+"\n");
	}
	public static void println(Object... objs){
		for(Object a:objs)println(a+" ");
	}
	public static void printInln(Object... objs){
		for(Object a:objs){
			if(a instanceof boolean[]){
				boolean[] b=(boolean[])a;
				for(boolean c:b)System.out.print(c+" ");
			}
			else if(a instanceof float[]){
				float[] b=(float[])a;
				for(float c:b)System.out.print(c+" ");
			}
			else System.out.print(a+" ");
		}
		System.out.print("\n");
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
		return str.equals("true")||str.equals("false");
	}
	public static void throwAnException(Throwable object)throws Throwable{throw object;}
	public static void throwException(Object object){
		if(object==null)/*******************/return;
		if(object instanceof Throwable);else return;
		try{
			throwException(object);
		}catch(Throwable e){}
	}
	public static double keepAValueInBounds(double value,double min,double max){
		if(min>=max)return value;
		if(value<min)value=min;
		if(value>max)value=max;
		return value;
	}
	public static float keepAValueInBounds(float value,float min,float max){
		return (float)keepAValueInBounds((double)value, (double)min, (double)max);
	}
	public static MovingObjectPosition rayTrace(EntityLivingBase entity,float lenght, float var1){
		if(entity.worldObj.isRemote)return entity.rayTrace(lenght, var1);
		
		Vec3 vec3 =Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
		Vec3 vec31=entity.getLook(var1);
		Vec3 vec32=vec3.addVector(vec31.xCoord*var1, vec31.yCoord*var1, vec31.zCoord*var1);
		return entity.worldObj.func_147447_a(vec3, vec32, false, false, true);
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
				else if(msg.target.typeOfSending==TypeOfSending.ToDimension)Magiology.NETWORK_CHANNEL.sendToDimension(msg, msg.target.world.provider.dimensionId);
			}
		}
	}
	public static float[][] calculateDoubleRenderPosArray(float[][] prevPos,float[][] pos){
		if(pos.length!=prevPos.length)return null;
		float[][] result=null;
		for(int a=0;a<pos.length;a++)result=ArrayUtils.add(result, calculateRenderPosArray(prevPos[a], pos[a]));
		return result;
	}
	public static float[] calculateRenderPosArray(final float[] prevPos,final float[] pos){
		if(pos.length!=prevPos.length)return null;
		float[] result=null;
		for(int a=0;a<pos.length;a++)result=ArrayUtils.add(result, calculateRenderPos(prevPos[a], pos[a]));
		return result;
	}

	public static float calculateRenderPos(final double prevPos,final double pos){
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
		if(pos>wantedPos)speed-=acceleration;
		else speed+=acceleration;
		return speed;
	}
	public static void playSoundAtEntity(Object name,Entity entity,double volume,double pitch){
		if(entity.worldObj.isRemote)return;
        entity.worldObj.playSoundAtEntity(entity,(MReference.MODID+":"+name.toString()),(float)volume,(float)pitch);
	}
	private static int fps=0;
	private static long lastTime=0;
	private static void updateFps(){
		char[] debug=Minecraft.getMinecraft().debug.toCharArray();
		String number="";
		for(int a=0;a<debug.length;a++){
			char c=debug[a];
			if(c==' ')a=debug.length;
			else number+=c;
		}
		try{fps=Integer.parseInt(number);}
		catch(Exception e){fps=-1;}
	}
	public static int getFPS(){
		if(lastTime!=getTheWorld().getTotalWorldTime()){
			lastTime=getTheWorld().getTotalWorldTime();
			updateFps();
		}
		return fps;
	}
	public static boolean isRemote(Object object){
		if(object instanceof Entity)return((Entity)object).worldObj.isRemote;
		if(object instanceof World)return((World)object).isRemote;
		if(object instanceof TileEntity)return((TileEntity)object).getWorldObj().isRemote;
		println("Given object has no data reference to world!");
		return false;
	}
	public static boolean isAnArray(Object object){
		if(object instanceof Object[])return true;
		return false;
	}
	public static<T>boolean isInArray(T object,T[] array){
		return getPosInArray(object, array)>=0;
	}
	public static<T>int getPosInArray(T object,T[] array){
		if(isNull(object,array))return -1;
		if(array.length==0||isAnArray(object))return -1;
		int pos=-2;
		for(int a=0;a<array.length;a++)if(array[a]==object){
			pos=a;a=array.length;
		}
		return pos;
	}
	public static void exit(int Int){
		FMLCommonHandler.instance().exitJava(Int, false);
	}
	public static boolean isAny(Object tester,Object... objects){
		for(int a=0;a<objects.length;a++)if(tester==objects[a])return true;
		return false;
	}
	public static float fluctuatorSmooth(double speed,double offset){
		float
			fluctuator=fluctuator(speed, offset),
			prevFluctuator=fluctuator(speed, offset-1);
		return calculateRenderPos(prevFluctuator, fluctuator);
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
	public static void openGui(EntityPlayer player, int modGuiId, int x, int y, int z){
		openGui(player, Magiology.getMagiology(), modGuiId, x, y, z);
	}
	public static void openGui(EntityPlayer player, Object mainModClassInstance, int modGuiId, int x, int y, int z){
		if(isRemote(player))return;
		FMLNetworkHandler.openGui(player, mainModClassInstance, modGuiId, player.getEntityWorld(), x, y, z);
	}
	public static float[] calculateRenderPos(Entity entity){
		return new float[]{
				calculateRenderPos(entity,'x'),
				calculateRenderPos(entity,'y'),
				calculateRenderPos(entity,'z')};
	}
	public static float calculateRenderPos(Entity entity, char xyz){
		if((""+xyz).toLowerCase().equals("x")){
			return calculateRenderPos(entity.lastTickPosX,entity.posX);
		}
		if((""+xyz).toLowerCase().equals("y")){
			return calculateRenderPos(entity.lastTickPosY,entity.posY);
		}
		if((""+xyz).toLowerCase().equals("z")){
			return calculateRenderPos(entity.lastTickPosZ,entity.posZ);
		}
		printInln(xyz,"is not a valid key! Use x or y or z.");
		return -1;
	}
	public static void translateByEntityPos(Entity entity){
		GL11H.translate(calculateRenderPos(entity));
	}
	public static float[] multiplyArrayBy(float[] array,float multiplyer){
		float[] theReturnAndFinishTheFunction=array.clone();
		for(int a=0;a<theReturnAndFinishTheFunction.length;a++)theReturnAndFinishTheFunction[a]*=multiplyer;
		return theReturnAndFinishTheFunction;
	}
	public static <T> T getLastInColection(Collection<T> collection){
		T Return=null;
		Iterator<T> a=collection.iterator();
		while (a.hasNext())Return=a.next();
		return Return;
	}
	public static FontRenderer getFontRenderer(){
		return Minecraft.getMinecraft().fontRenderer;
	}
	
	public static boolean intersectLinePlane(Ray ray,Plane plane, Vec3 result){
		if(result==null){
			println("Result is null!\nResult can't be set if it is null!\nInitialize it!\n------------");
			return false;
		}
		
		boolean printProcess=false;
		
		
		boolean xz=
				plane.q.xCoord==plane.r.xCoord&&
				plane.r.xCoord==plane.s.xCoord&&
				plane.r.xCoord==plane.s.xCoord;
		if(xz){
			
		}else{
			
			if(ray.from.zCoord>ray.to.zCoord){
				Vec3 helper=ray.from;
				ray.from=ray.to;
				ray.to=helper;
			}
			double z=plane.q.zCoord;
			if(ray.from.zCoord>z){if(printProcess)printInln("target behind");return false;}
			if(ray.to.zCoord<z){if(printProcess)printInln("target to far");return false;}
			double
				distance=z-ray.from.zCoord,
				rayLenght=ray.from.distanceTo(ray.to),
				pecentage=distance/rayLenght;
			
			result.xCoord=ray.from.xCoord+(ray.to.xCoord-ray.from.xCoord)*pecentage;
			result.yCoord=ray.from.yCoord+(ray.to.yCoord-ray.from.yCoord)*pecentage;
			result.zCoord=ray.from.zCoord+(ray.to.zCoord-ray.from.zCoord)*pecentage;
			Vec3 norm=ray.from.subtract(ray.to).normalize();
			norm.xCoord*=0.5;
			norm.yCoord*=0.5;
			norm.zCoord*=0.5;
			
			
			while(result.zCoord-z>0.5){
				result.xCoord-=norm.xCoord;
				result.yCoord-=norm.yCoord;
				result.zCoord-=norm.zCoord;
			}
			while(result.zCoord-z<0.5){
				result.xCoord+=norm.xCoord;
				result.yCoord+=norm.yCoord;
				result.zCoord+=norm.zCoord;
			}
			norm.xCoord/=50;
			norm.yCoord/=50;
			norm.zCoord/=50;
			while(result.zCoord-z>0.01){
				result.xCoord-=norm.xCoord;
				result.yCoord-=norm.yCoord;
				result.zCoord-=norm.zCoord;
			}
			while(result.zCoord-z<0.01){
				result.xCoord+=norm.xCoord;
				result.yCoord+=norm.yCoord;
				result.zCoord+=norm.zCoord;
			}
//			result.zCoord=z;
			result.yCoord-=0.03;
			double
				minX=Math.min(plane.q.xCoord, Math.min(plane.r.xCoord, plane.s.xCoord)),
				maxX=Math.max(plane.q.xCoord, Math.max(plane.r.xCoord, plane.s.xCoord)),
				minY=Math.min(plane.q.yCoord, Math.min(plane.r.yCoord, plane.s.yCoord)),
				maxY=Math.max(plane.q.yCoord, Math.max(plane.r.yCoord, plane.s.yCoord));
			if(result.xCoord<minX){if(printProcess)printInln("target clipped out on min x");return false;}
			if(result.xCoord>maxX){if(printProcess)printInln("target clipped out on max x");return false;}
			if(result.yCoord<minY){if(printProcess)printInln("target clipped out on min y");return false;}
			if(result.yCoord>maxY){if(printProcess)printInln("target clipped out on max y");return false;}
			if(printProcess)printInln("Ray trace has resolwed a valid intersection point!");
			return true;
		}
		
		return false;
	}
	
	public static Object[][] rayTraceHolograms(EntityPlayer player,float lenght){
		Object[][] result={{},{}};
		try{
	        Vec3 vec3=getPosition(player,RenderLoopEvents.partialTicks);
	        Vec3 vec31=player.getLook(RenderLoopEvents.partialTicks);
	        Vec3 vec32=vec3.addVector(vec31.xCoord * lenght, vec31.yCoord * lenght, vec31.zCoord * lenght);
			
			Ray ray=new Ray(vec3, vec32);
			for(int a=0;a<TileEntityHologramProjector.hologramProjectors.size();a++){
				TileEntityHologramProjector tile=null;
				TileEntity test=player.worldObj.getTileEntity(TileEntityHologramProjector.hologramProjectors.get(a).xCoord, TileEntityHologramProjector.hologramProjectors.get(a).yCoord, TileEntityHologramProjector.hologramProjectors.get(a).zCoord);
				if(test instanceof TileEntityHologramProjector)tile=(TileEntityHologramProjector)test;
				if(tile!=null){
					Vec3 hit=Vec3.createVectorHelper(0, 0, 0);
					Vec3F
						min=tile.main.getPoint("min x,min y,min z"),
						max=tile.main.getPoint("max x,max y,max z");
					
					if(Helper.intersectLinePlane(ray, new Plane(
							Vec3.createVectorHelper(
									tile.xCoord+min.x+tile.offset.x,
									tile.yCoord+min.y+tile.offset.y,
									tile.zCoord+min.z+0.5),
							Vec3.createVectorHelper(
									tile.xCoord+max.x+tile.offset.x,
									tile.yCoord+min.y+tile.offset.y,
									tile.zCoord+min.z+0.5),
							Vec3.createVectorHelper(
									tile.xCoord+max.x+tile.offset.x,
									tile.yCoord+max.y+tile.offset.y,
									tile.zCoord+min.z+0.5)
							), hit)){
						result[0]=ArrayUtils.add(result[0], hit);
						result[1]=ArrayUtils.add(result[1], tile);
					}
				}else TileEntityHologramProjector.hologramProjectors.remove(a);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
    public static Vec3 getNormal(Vec3 point1,Vec3 point2,Vec3 point3){
    	Vec3 angle1=point2.subtract(point1);
		Vec3 angle2=point2.subtract(point3);
		return angle2.crossProduct(angle1).normalize();
    }
	public static ColorF calculateRenderColor(ColorF prevColor, ColorF color){
		return new ColorF(calculateRenderPos(prevColor.r, color.r),
						  calculateRenderPos(prevColor.g, color.g),
						  calculateRenderPos(prevColor.b, color.b),
						  calculateRenderPos(prevColor.a, color.a));
	}
	public static ColorF slowlyEqalizeColor(ColorF variable, ColorF goal, float speed){
		return new ColorF(slowlyEqalize(variable.r, goal.r, speed),
						  slowlyEqalize(variable.g, goal.g, speed),
						  slowlyEqalize(variable.b, goal.b, speed),
						  slowlyEqalize(variable.a, goal.a, speed));
	}
    public static Vec3 getPosition(EntityPlayer entity,float par1)
    {
        if (par1 == 1.0F)
        {
            return Vec3.createVectorHelper(entity.posX, entity.posY + (entity.getEyeHeight()), entity.posZ);
        }
        else
        {
            double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * par1;
            double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * par1 + (entity.getEyeHeight());
            double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * par1;
            return Vec3.createVectorHelper(d0, d1, d2);
        }
    }
	public static String getStringForSize(String text, float allowedWidth){
		if(text.isEmpty())return text;
		String Return=""+text;
		boolean cap=false;
		while(getFontRenderer().getStringWidth(Return)>allowedWidth){
			Return=Return.substring(0, Return.length()-1);
		}
		return Return;
	}
	public static void printStackTrace(){
		println(getStackTrace());
	}
	public static String getStackTrace(){
		String Return="";
		
		StackTraceElement[] a1=Thread.currentThread().getStackTrace();
		int lenght=0;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		Return+="Invoke time: "+dateFormat.format(cal.getTime())+"\n";
		for(int i=2;i<a1.length;i++){
			StackTraceElement a=a1[i];
			String s=a.toString();
			Return+=s+"\n";
			lenght=Math.max(s.length(),lenght);
		}
		for(int b=0;b<lenght;b++)Return+="-";
		Return+="\n";
		
		return Return;
	}
	public static AxisAlignedBB AxisAlignedBB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ){
		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
	public static void spawnEntityFXAt(TileEntity tileEntity, EntityFX entityFX){
		entityFX.posX+=tileEntity.xCoord;
		entityFX.posY+=tileEntity.yCoord;
		entityFX.posZ+=tileEntity.zCoord;
		spawnEntityFX(entityFX);
	}
	public static EntityFlameFX marker(double x,double y,double z,double xSpeed,double ySpeed,double zSpeed){
		if(getTheWorld()==null)return null;
		return new EntityFlameFX(getTheWorld(), x+0.5, y+0.5, z+0.5, xSpeed, ySpeed, zSpeed);
	}
	public static TargetPoint TargetPoint(TileEntity tile, int range){
		return new TargetPoint(tile.getWorldObj().provider.dimensionId, tile.xCoord, tile.yCoord, tile.zCoord, range);
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
	public static void breakBlock(World world,int x,int y,int z){
//		world.playSoundEffect(x+0.5,y+0.5,z+0.5, world.getBlock(x, y, z).sound, 1.0F, RF() * 0.4F + 0.8F);
		//TODO
	}
	public static EntityItem dropBlockAsItem(World world, double x, double y, double z, ItemStack stack){
	    if(!world.isRemote&&world.getGameRules().getGameRuleBooleanValue("doTileDrops")&&!world.restoringBlockSnapshots){
	        EntityItem entity=new EntityItem(world,x,y,z,stack);
	        entity.delayBeforeCanPickup=0;
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
	private static RenderBlocks renderBlocks=new RenderBlocks();
	public static RenderBlocks getRenderBlocks(){
		return renderBlocks;
	}
}
