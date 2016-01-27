package com.magiology.util.utilclasses;

import com.google.common.collect.ImmutableMap;
import com.magiology.core.Config;
import com.magiology.core.MReference;
import com.magiology.core.Magiology;
import com.magiology.forgepowered.events.client.RenderEvents;
import com.magiology.forgepowered.packets.core.AbstractPacket;
import com.magiology.forgepowered.packets.core.AbstractToClientMessage;
import com.magiology.forgepowered.packets.core.AbstractToClientMessage.SendingTarget.TypeOfSending;
import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.mcobjects.tileentityes.hologram.HoloObject;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.Get.Render.Font;
import com.magiology.util.utilclasses.math.CricleUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.m_extension.effect.EntityFlameFXM;
import com.magiology.util.utilobjects.vectors.Plane;
import com.magiology.util.utilobjects.vectors.Ray;
import com.magiology.util.utilobjects.vectors.Vec2i;
import com.magiology.util.utilobjects.vectors.Vec3M;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.mojang.realmsclient.gui.ChatFormatting.GOLD;
import static com.mojang.realmsclient.gui.ChatFormatting.RESET;

public class UtilM{
	public class U extends UtilM{}

	
	
	static Random rand=new Random();
	public static final float p=1F/16F;
	@SideOnly(value=Side.CLIENT)
    public static void spawnEntityFX(EntityFX particleFX){
		
		if(isRemote(particleFX)){
			Minecraft mc=U.getMC();
			Entity ent=mc.getRenderViewEntity();
			if(ent!=null&&mc.effectRenderer!=null){
				int i=mc.gameSettings.particleSetting;
	            double d6=ent.posX-particleFX.posX,d7=ent.posY-particleFX.posY,d8=ent.posZ-particleFX.posZ,d9=Math.sqrt(mc.gameSettings.renderDistanceChunks)*45;
	            if(!(i>1)&&!(d6*d6+d7*d7+d8*d8>d9*d9)&&RB(Config.getParticleAmount()))Get.Render.ER().addEffect(particleFX);
			}
		}
	}
	@SideOnly(value=Side.CLIENT)
	public static void spawnEntityFX(EntityFX particleFX,int distance){
		if(particleFX.worldObj.isRemote){
			Minecraft mc=U.getMC();
			Entity ent=mc.getRenderViewEntity();
			if(ent!=null&&mc.effectRenderer!=null){
				int i=mc.gameSettings.particleSetting;
				double d6=ent.posX-particleFX.posX,d7=ent.posY-particleFX.posY,d8=ent.posZ-particleFX.posZ;
				if(!(i>1)&&!(d6*d6+d7*d7+d8*d8>distance*distance)&&RB(Config.getParticleAmount()))Get.Render.ER().addEffect(particleFX);
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
	 * @param percentage s
	 * @return s
	 */
	public static boolean RB(double percentage){
		percentage=snap(percentage, 0, 1);
		return percentage!=0&&(percentage==1||RF()<percentage);
	}
	/**
	 * this method returns a random boolean with a custom chance of getting true.
	 * The higher the number is the lower chance will be for getting a true return.
	 * Type in a number higher or equal to 1.
	 * @param percentage s
	 * @return
	 */
	public static boolean RB(int percentage){
		percentage=Math.max(percentage, 1);
		if(percentage==1)return true;
		return RInt(percentage)==0;
	}
	public static double[] circleXZ(double angle){
		double[] result={0,0};
		int intAngle=(int)angle;
		result[0]=CricleUtil.sin(intAngle);//-X-
		result[1]=CricleUtil.cos(intAngle);//-Z-
		return result;
	}
	public static double[] cricleXZForce(double angle,double offset){
		double[] result={0,0};
		angle+=offset;
		int intAngle=(int)angle;
		result[0]=CricleUtil.sin(intAngle);//-X-
		result[1]=CricleUtil.cos(intAngle);//-Z-
		return result;
	}
	public static double[] cricleXZwSpeed(double angle,double offset){
		double[] result={0,0,0,0};
		{
			int intAngle=(int)angle;
			result[0]=CricleUtil.sin(intAngle);//-X-
			result[1]=CricleUtil.cos(intAngle);//-Z-
			angle+=offset;}{
			int intAngle=(int)angle;
			result[0]=CricleUtil.sin(intAngle);//-X-
			result[1]=CricleUtil.cos(intAngle);//-Z-
		}
		return result;
	}
	public static double slowlyEqualize(double variable, double goal, double speed){
		return slowlyEqualize((float)variable, (float)goal, (float)speed);
	}
	public static float slowlyEqualize(float variable, float goal, float speed){
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
		return variable+bounds>wantedVariable&&variable-bounds<wantedVariable;
	}
	/**
	 * Returns false if all objects are not null and it returns true if any of object/s are true
	 * Note: you'll might need to add "!" on using it
	 * @param objects a
	 * @return a
	 */
	public static boolean isNull(Object...objects){
		for(Object object:objects)if(object==null)return true;
		return false;
	}
	/**
	 * Returns if stack contains a specific item
	 * Note: no danger of null pointer exception!
	 * @param item a
	 * @param stack a
	 * @return a
	 */
	public static boolean isItemInStack(Item item,ItemStack stack){
		return stack!=null&&stack.getItem()==item;
	}
	/**
     * Creates a x,y,z offset coordinate of a ball. (can create 2 coordinates)
     * Args:x,y,z particle speed, size
	 * @param ballSize a
	 * @param hasSecondPos a
	 * @return a
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
		return str!=null&&(str.equals("true")||str.equals("false"));
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
		return (float)(prevPos+(pos-prevPos)*RenderEvents.partialTicks);
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
		PrintUtil.println("Given object has no data reference to world!");
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
		for(Object object:objects)if(tester==object)return true;
		return false;
	}
	public static float fluctuateSmooth(double speed, double offset){
		float
			fluctuate=fluctuate(speed, offset),
			prevFluctuate=fluctuate(speed, offset-1);
		return calculatePos(prevFluctuate, fluctuate);
	}
	public static float fluctuate(double speed, double offset){
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
	public static int rgbPercentageToCode(double r, double g, double b, double alpha){
		int r1=(int)(255*r), g1=(int)(255*g), b1=(int)(255*b), alpha1=(int)(255*alpha);
		return rgbByteToCode(r1, g1, b1, alpha1);
	}
	public static int[] colorToRGBABByte(Color color){
		return new int[]{color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha()};
	}
	public static int[] codeToRGBABByte(int code){
		return colorToRGBABByte(new Color(code));
	}
	public static float[] colorToRGBABPercentage(Color color){
		int[] data=colorToRGBABByte(color);
		return new float[]{(data[0])/255F,(data[1])/255F,(data[2])/255F,(data[3])/255F};
	}
	public static float[] codeToRGBABPercentage(int code){
		return colorToRGBABPercentage(new Color(code,true));
	}
	public static ColorF codeToColorF(int code){
		float[] data=codeToRGBABPercentage(code);
		return new ColorF(data[0],data[1],data[2],data[3]);
	}
	public static boolean intersectLinePlane(Ray ray,Plane plane, Vec3M result){
		if(result==null){
			PrintUtil.println("Result is null!\nResult can't be set if it is null!\nInitialize it!\n------------");
			return false;
		}
		
		boolean printProcess=FALSE();
		
		
		boolean xz=
				plane.q.x==plane.r.x&&
				plane.r.x==plane.s.x&&
				plane.r.x==plane.s.x;
		if(!xz){
			
			if(ray.from.z>ray.to.z){
				Vec3M helper=ray.from;
				ray.from=ray.to;
				ray.to=helper;
			}
			double z=plane.q.z;
			if(ray.from.z>z){if(printProcess)PrintUtil.println("target behind");return false;}
			if(ray.to.z<z){if(printProcess)PrintUtil.println("target to far");return false;}
			AxisAlignedBB Plane=new AxisAlignedBB(plane.q.x, plane.q.y, plane.q.z, plane.s.x, plane.s.y, plane.s.z+0.01);
			MovingObjectPosition rayTrace=Plane.calculateIntercept(ray.from.addVector(0, 0.1, 0).conv(), ray.to.addVector(0, 0.1, 0).conv());
			if(rayTrace==null||rayTrace.hitVec==null){if(printProcess)PrintUtil.println("target clipped out");return false;}
			result.x=rayTrace.hitVec.xCoord;
			result.y=rayTrace.hitVec.yCoord;
			result.z=rayTrace.hitVec.zCoord;
			
			if(printProcess)PrintUtil.println("Ray trace has resolwed a valid intersection point!");
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
		return new ColorF(slowlyEqualize(variable.r, goal.r, speed),
						  slowlyEqualize(variable.g, goal.g, speed),
						  slowlyEqualize(variable.b, goal.b, speed),
						  slowlyEqualize(variable.a, goal.a, speed));
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
	public static String getStackTrace(){
		StringBuilder Return=new StringBuilder();
		
		StackTraceElement[] a1=Thread.currentThread().getStackTrace();
		int length=0;
		DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal=Calendar.getInstance();
		Return.append("Invoke time: ").append(dateFormat.format(cal.getTime())).append("\n");
		for(int i=2;i<a1.length;i++){
			StackTraceElement a=a1[i];
			String s=a.toString();
			Return.append(s).append("\n");
			length=Math.max(s.length(),length);
		}
		for(int b=0;b<length/4;b++)Return.append("_/\\_");
		
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
		return new EntityFlameFXM(getTheWorld(), x+0.5, y+0.5, z+0.5, xSpeed, ySpeed, zSpeed);
	}
	public static TargetPoint TargetPoint(TileEntity tile, int range){
		return new TargetPoint(tile.getWorld().provider.getDimensionId(), x(tile), y(tile), z(tile), range);
	}
	public static boolean instanceOf(Object tester, Object instance){
		return instanceOf(tester.getClass(), instance.getClass());
	}
	public static boolean instanceOf(Object tester, Class instance){
		return instanceOf(tester.getClass(), instance);
	}
	public static boolean instanceOf(Class tester, Class instance){
		try{
			tester.asSubclass(instance);
			return true;
		}catch(Exception ignored){}
		return false;
	}
	public static EntityItem dropBlockAsItem(World world, double x, double y, double z, ItemStack stack){
	    if(!world.isRemote&&!world.restoringBlockSnapshots){
	        EntityItem entity=new EntityItem(world,x,y,z,stack);
	        entity.setPickupDelay(0);
	        spawnEntity(entity);
	        return entity;
	    }
		return null;
	}
	public static boolean AxisAlignedBBEqual(AxisAlignedBB box1, AxisAlignedBB box2){
		if(box1==box2) return true;
		return !isNull(box1, box2)&&box1.minX==box2.minX&&box1.minY==box2.minY&&box1.minZ==box2.minZ&&box1.maxX==box2.maxX&&box1.maxY==box2.maxY&&box1.maxZ==box2.maxZ;
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
		return new ScaledResolution(getMC()).getScaleFactor();
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
		return result.substring(0, result.length()-splitter.length());
	}
	
	private static long startTime;
	public static void startTime(){
		startTime=System.currentTimeMillis();
	}
	public static long endTime(){
		return System.currentTimeMillis()-startTime;
	}
	public static void printTime(){
		PrintUtil.println(endTime());
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
	public static Vec2i[] arrangeStrings(final String[]strings,int lines,int marginX,int marginY){
		FontRenderer fr=Font.FR();
		Vec2i[] result=new Vec2i[strings.length];
		int columns=(int)Math.floor(strings.length/(float)lines)+1;
		String[][] formattedStrings=new String[columns][lines];
		
		
		int[] longestInColumn=new int[columns],columnOffsets=new int[columns];
		for(int i=0;i<columns;i++){
			for(int j=0;j<lines;j++){
				int id=i*(columns+1)+j;
				if(id<strings.length)formattedStrings[i][j]=strings[id];
			}
		}
		for(int i=0;i<formattedStrings.length;i++)while(ArrayUtils.contains(formattedStrings[i], null))formattedStrings[i]=ArrayUtils.removeElement(formattedStrings[i], null);
		for(int i=0;i<columns;i++)for(int j=0;j<formattedStrings[i].length;j++)longestInColumn[i]=Math.max(longestInColumn[i], fr.getStringWidth(formattedStrings[i][j]));
		for(int i=0;i<columns;i++){
			columnOffsets[i]=marginX;
			for(int j=0;j<i;j++)columnOffsets[i]+=longestInColumn[j]+marginX;
		}
		
		for(int i=0;i<strings.length;i++)result[i]=new Vec2i(columnOffsets[(i/lines)%columns], (i%lines)*(fr.FONT_HEIGHT+marginY)+marginY);
		return result;
	}
}
