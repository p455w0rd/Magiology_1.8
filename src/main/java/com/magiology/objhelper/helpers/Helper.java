package com.magiology.objhelper.helpers;

import java.awt.Color;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFlameFX;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.core.Config;
import com.magiology.core.MReference;
import com.magiology.core.Magiology;
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
import com.magiology.objhelper.vectors.Vec3M;

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
		if(isRemote(particleFX)){
			Minecraft mc=Minecraft.getMinecraft();
			Entity ent=mc.getRenderViewEntity();
			if(mc!=null&&ent!=null&&mc.effectRenderer!=null){
				int i = mc.gameSettings.particleSetting;
	            double d6=ent.posX-particleFX.posX,d7=ent.posY-particleFX.posY,d8=ent.posZ-particleFX.posZ,d9=Math.sqrt(mc.gameSettings.renderDistanceChunks)*45;
	            if(i>1);else{
	            	if (d6*d6+d7*d7+d8*d8>d9*d9);else if(RB(Config.getParticleAmount()))Minecraft.getMinecraft().effectRenderer.addEffect(particleFX);
	            }
			}
		}
	}
	public static void spawnEntityFX(EntityFX particleFX,int distance){
		if(particleFX.worldObj.isRemote){
			Minecraft mc=Minecraft.getMinecraft();
			Entity ent=mc.getRenderViewEntity();
			if(mc!=null&&ent!=null&&mc.effectRenderer!=null){
				int i = mc.gameSettings.particleSetting;
				double d6=ent.posX-particleFX.posX,d7=ent.posY-particleFX.posY,d8=ent.posZ-particleFX.posZ;
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
		
		Vec3M vec3 =new Vec3M(entity.posX, entity.posY, entity.posZ);
		Vec3M vec31=vec3.conv(entity.getLook(var1));
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
		if(object instanceof TileEntity)return((TileEntity)object).getWorld().isRemote;
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
	public static void openGui(EntityPlayer player, int modGuiId, BlockPos pos){
		openGui(player, Magiology.getMagiology(), modGuiId, pos);
	}
	public static void openGui(EntityPlayer player, Object mainModClassInstance, int modGuiId, BlockPos pos){
		openGui(player, mainModClassInstance, modGuiId, pos.getX(),pos.getY(),pos.getZ());
	}
	public static void openGui(EntityPlayer player, Object mainModClassInstance, int modGuiId, int x,int y,int z){
		if(isRemote(player))return;
		FMLNetworkHandler.openGui(player, mainModClassInstance, modGuiId, player.getEntityWorld(), x,y,z);
	}
	public static float[] calculateRenderPos(Entity entity){
		return new float[]{
				calculateRenderPos(entity,'x'),
				calculateRenderPos(entity,'y'),
				calculateRenderPos(entity,'z')};
	}
	public static Vec3M calculateRenderPosV(Entity entity){
		return new Vec3M(
				calculateRenderPos(entity,'x'),
				calculateRenderPos(entity,'y'),
				calculateRenderPos(entity,'z'));
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
		return Minecraft.getMinecraft().fontRendererObj;
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
			if(ray.from.z>z){if(printProcess)printInln("target behind");return false;}
			if(ray.to.z<z){if(printProcess)printInln("target to far");return false;}
			double
				distance=z-ray.from.z,
				rayLenght=ray.from.distanceTo(ray.to),
				pecentage=distance/rayLenght;
			AxisAlignedBB Plane=new AxisAlignedBB(plane.q.x, plane.q.y, plane.q.z, plane.s.x, plane.s.y, plane.s.z+0.01);
			MovingObjectPosition rayt=Plane.calculateIntercept(ray.from.addVector(0, 0.1, 0).conv(), ray.to.addVector(0, 0.1, 0).conv());
			if(rayt==null||rayt.hitVec==null){if(printProcess)printInln("target clipped out");return false;}
			result.x=rayt.hitVec.xCoord;
			result.y=rayt.hitVec.yCoord;
			result.z=rayt.hitVec.zCoord;
			
			if(printProcess)printInln("Ray trace has resolwed a valid intersection point!");
			return true;
		}
		
		return false;
	}
	
	public static Object[][] rayTraceHolograms(EntityPlayer player,float lenght){
		Object[][] result={{},{}};
		try{
	        Vec3M Vec3M=getPosition(player,RenderLoopEvents.partialTicks);
	        Vec3M vec31=Vec3M.conv(player.getLook(RenderLoopEvents.partialTicks));
	        Vec3M vec32=Vec3M.addVector(vec31.x * lenght, vec31.y * lenght, vec31.z * lenght);
			
			Ray ray=new Ray(Vec3M, vec32);
			for(int a=0;a<TileEntityHologramProjector.hologramProjectors.size();a++){
				TileEntityHologramProjector tile=null;
				TileEntity test=player.worldObj.getTileEntity(TileEntityHologramProjector.hologramProjectors.get(a).getPos());
				if(test instanceof TileEntityHologramProjector)tile=(TileEntityHologramProjector)test;
				if(tile!=null){
					Vec3M hit=new Vec3M(0, 0, 0);
					Vec3M
						min=tile.main.getPoint("min x,min y,min z"),
						max=tile.main.getPoint("max x,max y,max z");
					
					if(Helper.intersectLinePlane(ray, new Plane(
							new Vec3M(
									tile.x()+min.x+tile.offset.x,
									tile.y()+min.y+tile.offset.y,
									tile.z()+min.z+0.5),
							new Vec3M(
									tile.x()+max.x+tile.offset.x,
									tile.y()+min.y+tile.offset.y,
									tile.z()+min.z+0.5),
							new Vec3M(
									tile.x()+max.x+tile.offset.x,
									tile.y()+max.y+tile.offset.y,
									tile.z()+min.z+0.5)
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
	
    public static Vec3M getNormal(Vec3M point1,Vec3M point2,Vec3M point3){
    	Vec3M angle1=point2.subtract(point1);
		Vec3M angle2=point2.subtract(point3);
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
    public static Vec3M getPosition(EntityPlayer entity,float par1)
    {
        if (par1 == 1.0F)
        {
            return new Vec3M(entity.posX, entity.posY + (entity.getEyeHeight()), entity.posZ);
        }
        else
        {
            double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * par1;
            double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * par1 + (entity.getEyeHeight());
            double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * par1;
            return new Vec3M(d0, d1, d2);
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
//		world.playSoundEffect(x+0.5,y+0.5,z+0.5, Helper.getBlock(world, pos).sound, 1.0F, RF() * 0.4F + 0.8F);
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
//	private static RenderBlocks renderBlocks=new RenderBlocks();
//	public static RenderBlocks getRenderBlocks(){
//		return renderBlocks;
//	}
	//yay for 1.8 code changes
	/** thanks mc for this incredibly convenient code so i I need to make a helper for things that should not need one... */
	public static final PropertyInteger MetadataMarker = PropertyInteger.create("data", 0, 15);
	public static int getBlockMetadata(World world, BlockPos pos){
		return 0;
	}
	public static void setMetadata(World world, BlockPos pos,int meta){
		world.setBlockState(pos, world.getBlockState(pos).withProperty(MetadataMarker, meta), 3);
	}
	public static Block getBlock(World world, BlockPos pos){
		return world.getBlockState(pos).getBlock();
	}
	public static Block getBlock(World world, int x, int y, int z){
		return getBlock(world, new BlockPos(x, y, z));
	}
	public static void setBlock(World world, BlockPos pos,Block block){
		world.setBlockState(pos, block.getDefaultState(), 3);
	}
	public static void setBlock(World world, BlockPos pos, Block block, int meta){
		world.setBlockState(pos, block.getDefaultState().withProperty(MetadataMarker, meta), 3);
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
	public static boolean createNBT(Object a){
		if(a instanceof ItemStack){
			if(!((ItemStack)a).hasTagCompound())((ItemStack)a).setTagCompound(new NBTTagCompound());
			return ((ItemStack)a).hasTagCompound();
		}
		return false;
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
}
