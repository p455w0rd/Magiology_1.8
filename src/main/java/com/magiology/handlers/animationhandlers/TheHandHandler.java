package com.magiology.handlers.animationhandlers;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.magiology.core.init.MItems;
import com.magiology.forgepowered.event.client.RenderLoopEvents;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData.PowerHandData;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData.PowerHandData_sub_fingerData;
import com.magiology.mcobjects.entitys.EntityBallOfEnergy;
import com.magiology.mcobjects.entitys.EntitySubatomicWorldDeconstructor;
import com.magiology.render.itemrender.ItemRendererTheHand;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;

public class TheHandHandler{
	static float p=1F/16F;
	static ArrayList<BufferedEvent>events=new ArrayList(); 
	public static TheHandHandler instance=new TheHandHandler();
	public static ItemRendererTheHand getRenderer(){return renderer;}
	static ItemRendererTheHand renderer=new ItemRendererTheHand();
	public static enum HandComonPositions{
		ErrorPos(new float[]{0,0,0,0,0,0},
				new float[][]{{0,0,0},{0,0,0}},new float[][][]{
				{{0,0,0},{0,0,0},{0,0,0}},
				{{0,0,0},{0,0,0},{0,0,0}},
				{{0,0,0},{0,0,0},{0,0,0}},
				{{0,0,0},{0,0,0},{0,0,0}}}),
		ClosedFist(new float[]{-10,0,0,0,0,0},
				new float[][]{{-30,-20,0},{0,-80,0}},new float[][][]{
				{{-60,0,0},{-80,0,0},{-60,0,0}},
				{{-60,0,0},{-80,0,0},{-60,0,0}},
				{{-60,0,0},{-80,0,0},{-60,0,0}},
				{{-60,0,0},{-80,0,0},{-60,0,0}}}),
		WeaponHolder(new float[]{30,0,0,0,1,-10},
				new float[][]{{-35,20,0},{0,-10,0}},new float[][][]{
				{{-15,0,0},{-15,0,0},{-10,0,0}},
				{{-15,0,0},{-15,0,0},{-10,0,0}},
				{{-15,0,0},{-15,0,0},{-10,0,0}},
				{{-15,0,0},{-15,0,0},{-10,0,0}}}),
		ReadyForAction(new float[]{30,0,0,0,1,-10},
				new float[][]{{-35,20,0},{0,-10,0}},new float[][][]{
				{{-15,0,0},{-15,0,0},{-10,0,0}},
				{{-15,0,0},{-15,0,0},{-10,0,0}},
				{{-15,0,0},{-15,0,0},{-10,0,0}},
				{{-15,0,0},{-15,0,0},{-10,0,0}}}),
		NaturalPosition(new float[]{0,0,0,0,0,0},
				new float[][]{{-20,25,0},{0,-20,0}},new float[][][]{
				{{-10,0,0},{-20,0,0},{-5,0,0}},
				{{-20,0,0},{-10,0,0},{-5,0,0}},
				{{-20,0,0},{-10,0,0},{-5,0,0}},
				{{-25,0,0},{-10,0,0},{0,0,0}}});
		
		public float[] handRotations;
		public float[][] thumbRotations;
		public float[][][] otherFingersRotations;
		private HandComonPositions(float[] handRotations,float[][] thumbRotations,float[][][] otherFingersRotations){
			this.thumbRotations=thumbRotations;
			this.otherFingersRotations=otherFingersRotations;
			this.handRotations=handRotations;
		}
		/**THIS IS ONLY FOR TESTING!*/
		public void set(float[] handRotations,float[][] thumbRotations,float[][][] otherFingersRotations){
			this.thumbRotations=thumbRotations;
			this.otherFingersRotations=otherFingersRotations;
			this.handRotations=handRotations;
		}
	}
	public static float[][][] 
			     noiser=new float[][][]{{{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0,0,0,0}}},
			noiserSpeed=new float[][][]{{{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0,0,0,0}}};
	
	
	public static HandComonPositions getActivePosition(EntityPlayer player){
		if(!isActive(player))return null;
		int id=player.getCurrentEquippedItem().getTagCompound().getInteger("AP");
		if(id<0)return HandComonPositions.ErrorPos;
		if(id>=HandComonPositions.values().length)return HandComonPositions.ErrorPos;
		return HandComonPositions.values()[id];
	}
	public static HandComonPositions getLastActivePosition(EntityPlayer player){
		if(!isActive(player))return null;
		int id=player.getCurrentEquippedItem().getTagCompound().getInteger("LAP");
		if(id<0)return HandComonPositions.ErrorPos;
		if(id>=HandComonPositions.values().length)return HandComonPositions.ErrorPos;
		return HandComonPositions.values()[id];
	}
	public static HandComonPositions nextPosition(EntityPlayer player){
		if(!isActive(player))return null;
		HandComonPositions[] values=HandComonPositions.values();
		int now=Util.getPosInArray(TheHandHandler.getActivePosition(player), values);
		now++;
		if(now==values.length)now=0;
		if(values[now]==HandComonPositions.ErrorPos)now++;
		if(now==values.length)now=0;
		TheHandHandler.setActivePositionId(player, now);
		return values[now];
	}
	public static void setActivePositionId(EntityPlayer player,int id){
		if(!isActive(player))return;
		player.getCurrentEquippedItem().getTagCompound().setInteger("LAP", player.getCurrentEquippedItem().getTagCompound().getInteger("AP"));
		player.getCurrentEquippedItem().getTagCompound().setInteger( "AP", id);
	}
	
	
	public static void update(EntityPlayer player){
		if(Util.isNull(Util.getTheWorld()))return;
		if(!isActive(player))return;
		HandComonPositions pos=getActivePosition(player);
		PowerHandData data=ComplexPlayerRenderingData.getFastPowerHandData(player);
		if(data==null)return;
		if(pos==HandComonPositions.ErrorPos)TheHandHandler.setActivePositionId(player, 3);
		
		
		//updates and activates the buffered events
		for(BufferedEvent event:events){
			if(event!=null&&!event.isActivated){
				if(event.string!=null&&event.objects!=null)customEventHandler(player, event);
				else event.activate(player.worldObj.getTotalWorldTime());
			}
		}
		if(Util.isRemote(player)&&isActive(player))animate(player,data);
	}
	@SideOnly(Side.CLIENT)
	public static void animate(EntityPlayer player, PowerHandData data){
		if(!Util.isRemote(player))return;
		if(renderer.base==null||renderer.fingers==null)renderer.init();
		HandComonPositions pos=getActivePosition(player);
		data.prevHandRotationCalc=data.handRotationCalc.clone();
		
		long wTime=Util.getTheWorld().getTotalWorldTime();
		if(wTime%5==0)generateNewNoiseValue();
		
		//updates the bobbing of the 3.camPos render
		if(data.thirdPresonPos<p*10)data.thirdPresonPosSpeed+=0.0015;
		else if(data.thirdPresonPos>p*10)data.thirdPresonPosSpeed-=0.0015;
		data.thirdPresonPos+=data.thirdPresonPosSpeed;
		
		//for every finger
		for(int finger=0;finger<renderer.fingers.length;finger++){
			PowerHandData_sub_fingerData a1=data.fingerData[finger];
			a1.prevcalcXyzPosRot=a1.calcXyzPosRot.clone();
			
			float[][][] rotations2=pos.otherFingersRotations;
			if(finger==0){
				float[][] rotations1=pos.thumbRotations;
				for(int a11=0;a11<2;a11++)for(int a12=0;a12<3;a12++){
					a1.xyzPosRot[a11][3+a12]+=a1.xyzPosRotSpeed[a11][3+a12];
					a1.xyzPosRot[a11][3+a12]=(float)Util.slowlyEqalize(a1.xyzPosRot[a11][3+a12], rotations1[a11][a12], 2);
					a1.xyzPosRotSpeed[a11][3+a12]*=0.8;
				}
				a1.xyzPosRotNoise[1][4]=(float)Util.slowlyEqalize(a1.xyzPosRotNoise[1][4], noiserSpeed[finger][0][1], 1);
				a1.xyzPosRotNoise[0][4]=(float)Util.slowlyEqalize(a1.xyzPosRotNoise[0][4], noiserSpeed[finger][1][1], 1);
				a1.xyzPosRot[0][4]=Util.keepValueInBounds(a1.xyzPosRot[0][4], -4, 65);
				a1.xyzPosRot[1][4]=Util.keepValueInBounds(a1.xyzPosRot[1][4], -80, 2);
				a1.xyzPosRot[0][3]=Util.keepValueInBounds(a1.xyzPosRot[0][3], -45, 5);
			}else{
				//for every box in the finger
				for(int b=0;b<a1.xyzPosRot.length;b++){
					float[][] rotations=rotations2[finger-1];
					for(int a11=0;a11<3;a11++){
						a1.xyzPosRot[b][3+a11]+=a1.xyzPosRotSpeed[b][3+a11];
						a1.xyzPosRotSpeed[b][3+a11]*=0.8;
						a1.xyzPosRot[b][3+a11]=(float)Util.slowlyEqalize(a1.xyzPosRot[b][3+a11], rotations[b][a11], 2);
					}
					a1.xyzPosRotNoise[b][3]=(float)Util.slowlyEqalize(a1.xyzPosRotNoise[b][3], noiserSpeed[finger][b][0], 2);
					a1.xyzPosRot[b][3]=Util.keepValueInBounds(a1.xyzPosRot[b][3], -90, 5);
				}
			}
			a1.calcXyzPosRot=Util.addToDoubleFloatArray(a1.xyzPosRot,a1.xyzPosRotNoise);
		}
		//updates noise of hand
		for(int g=0;g<2;g++)for(int h=0;h<data.noiserHandSpeed.length;h++){
			if(data.handRotationSpeed[h]>noiser[5][0][h])data.noiserHandSpeed[h]-=0.002;
			else data.noiserHandSpeed[h]+=0.002;
			data.noiserHandSpeed[h]*=0.9;
			
			data.handRotationSpeed[h]+=data.noiserHandSpeed[h];
		}
		//updates hand rotations
		for(int g=0;g<10;g++)for(int h=0;h<data.handRotation.length;h++){
			float speed=Math.abs(pos.handRotations[h]-getLastActivePosition(player).handRotations[h]);
			if(speed<=0)speed=1;
			data.handRotation[h]=(float)Util.slowlyEqalize(data.handRotation[h],pos.handRotations[h], (0.05*(Util.RF()*0.5+0.5)*speed/8));
		}
		//makes the holding animation
		if(player.isUsingItem()){
			boolean bol1=pos.equals(HandComonPositions.ReadyForAction)||pos.equals(HandComonPositions.NaturalPosition);
			for(int finger=0;finger<renderer.fingers.length;finger++){
				PowerHandData_sub_fingerData a1=data.fingerData[finger];
				if(finger==0){
					for(int a11=0;a11<2;a11++)if(bol1)if(Math.abs(a1.xyzPosRotSpeed[a11][4])<0.8)a1.xyzPosRotSpeed[a11][4]-=1.3+Util.RF()*2;
				}else{
					for(int b=0;b<a1.xyzPosRot.length;b++)if(bol1)if(Math.abs(a1.xyzPosRotSpeed[b][3])<0.8)a1.xyzPosRotSpeed[b][3]-=1.3+Util.RF()*2;
				}
			}
		}
		data.handRotationCalc=Util.addToFloatArray(data.handRotation,data.handRotationSpeed);
		//updates speed of the noise
		for(int a1=0;a1<noiser.length;a1++)for(int a2=0;a2<noiser[a1].length;a2++)for(int a3=0;a3<noiser[a1][a2].length;a3++)
			noiserSpeed[a1][a2][a3]=(float)Util.slowlyEqalize(noiserSpeed[a1][a2][a3], noiser[a1][a2][a3], 0.05);
	}
	@SideOnly(Side.CLIENT)
	public static void handUseAnimation(EntityPlayer player){
		if(!Util.isRemote(player))return;
		PowerHandData data=ComplexPlayerRenderingData.getFastPowerHandData(player);
		if(data==null)return;
		RenderLoopEvents.disabledEquippItemAnimationTime=2;
		boolean bol1=getActivePosition(player).equals(HandComonPositions.ReadyForAction)||getActivePosition(player).equals(HandComonPositions.NaturalPosition);
		for(int finger=0;finger<data.fingerData.length;finger++){
			PowerHandData_sub_fingerData a1=data.fingerData[finger];
			if(finger==0){
				for(int a11=0;a11<2;a11++)if(bol1)if(Math.abs(a1.xyzPosRotSpeed[a11][4])<0.8)a1.xyzPosRotSpeed[a11][4]=-4F;
			}else for(int b=0;b<a1.xyzPosRot.length;b++)if(bol1)if(Math.abs(a1.xyzPosRotSpeed[b][3])<0.8)a1.xyzPosRotSpeed[b][3]=-4;
		}
		if(bol1){
			if(Math.abs(data.handRotationSpeed[5])<3)addANewEvent(player,U.getMC().theWorld.getTotalWorldTime()+2,"noiserHandSpeed",-0.05F,true,5);
			if(Math.abs(data.noiserHandSpeed[5])<1)addANewEvent(player,U.getMC().theWorld.getTotalWorldTime()+9,"noiserHandSpeed",0.05F,true,5);
		}
	}
	@SideOnly(Side.CLIENT)
	public static void actionAnimation(EntityPlayer player){
		if(!Util.isRemote(player))return;
		handUseAnimation(player);
		long tim=Util.getTheWorld().getTotalWorldTime();

		PowerHandData data=ComplexPlayerRenderingData.getFastPowerHandData(player);
		if(data==null)return;
		boolean bol1=getActivePosition(player).equals(HandComonPositions.ReadyForAction)||getActivePosition(player).equals(HandComonPositions.NaturalPosition);
		for(int finger=0;finger<data.fingerData.length;finger++){
			PowerHandData_sub_fingerData a1=data.fingerData[finger];
			if(finger==0){
				for(int a11=0;a11<2;a11++){
					if(bol1)a1.xyzPosRotSpeed[a11][4]=-6F;
				}
			}else{
				for(int b=0;b<a1.xyzPosRot.length;b++)if(bol1)a1.xyzPosRotSpeed[b][3]=-6;
			}
		}
		
		for(int a11=0;a11<3;a11++){
			for(int finger=0;finger<renderer.fingers.length-1;finger++){
				addANewEvent(player,tim+6,
						BufferedEventTargetEnum.FINGER, new int[]{finger+1,a11}, BufferedEventVariableEnum.SPEED, 
						BufferedEventVariableEnum.X_ROTATION, 6.4F*(Util.RF()*0.3F+0.7F), true);
				addANewEvent(player,tim+8,
						BufferedEventTargetEnum.FINGER, new int[]{finger+1,a11}, BufferedEventVariableEnum.SPEED, 
						BufferedEventVariableEnum.X_ROTATION, 6F  *(Util.RF()*0.3F+0.7F), true);
				addANewEvent(player,tim,
						BufferedEventTargetEnum.FINGER, new int[]{finger+1,a11}, BufferedEventVariableEnum.SPEED, 
						BufferedEventVariableEnum.X_ROTATION, -1  *(Util.RF()*0.3F+0.7F), true);
			}
			addANewEvent(player,tim+6,
					BufferedEventTargetEnum.FINGER, new int[]{0,a11}, BufferedEventVariableEnum.SPEED, 
					BufferedEventVariableEnum.Y_ROTATION, 4.4F*(Util.RF()*0.3F+0.7F), true);
			addANewEvent(player,tim+8,
					BufferedEventTargetEnum.FINGER, new int[]{0,a11}, BufferedEventVariableEnum.SPEED, 
					BufferedEventVariableEnum.Y_ROTATION, 4F  *(Util.RF()*0.3F+0.7F), true);
			addANewEvent(player,tim,
					BufferedEventTargetEnum.FINGER, new int[]{0,a11}, BufferedEventVariableEnum.SPEED, 
					BufferedEventVariableEnum.Y_ROTATION, -1  *(Util.RF()*0.3F+0.7F), true);
		}
	}
	@SideOnly(Side.CLIENT)
	public class BufferedEvent{
		public BufferedEventTargetEnum target;
		public int[] id;
		public BufferedEventVariableEnum variable;
		public BufferedEventVariableEnum xyz;
		public long time;
		public boolean isActivated=false,isAdding;
		public float action;
		public String string=null;
		public Object[] objects;
		public EntityPlayer player;
		public BufferedEvent(EntityPlayer player,long time,String string,Object...objects){
			this.time=time;
			this.string=string;
			this.objects=objects;
			this.player=player;
		}
		public BufferedEvent(EntityPlayer player,long time,BufferedEventTargetEnum target,int[] id,BufferedEventVariableEnum variable,BufferedEventVariableEnum xyz,float action,boolean isAdding){
			this.time=time;
			this.target=target;
			this.id=id;
			this.variable=variable;
			this.xyz=xyz;
			this.action=action;
			this.isAdding=isAdding;
			this.player=player;
		}
		public void activate(long time){
			PowerHandData data=ComplexPlayerRenderingData.getFastPowerHandData(player);
			if(data==null)return;
			if(this.time>time||isActivated)return;
			isActivated=true;
			if(target==BufferedEventTargetEnum.FINGER){
				if(id.length!=2)return;
				if(variable==BufferedEventVariableEnum.SPEED){
					if(isAdding)data.fingerData[id[0]].xyzPosRotSpeed[id[1]][xyz.id]+=action;
					else        data.fingerData[id[0]].xyzPosRotSpeed[id[1]][xyz.id] =action;
				}else if(variable==BufferedEventVariableEnum.POSITION){
					if(isAdding)data.fingerData[id[0]].xyzPosRot[id[1]][xyz.id]+=action;
					else        data.fingerData[id[0]].xyzPosRot[id[1]][xyz.id] =action;
				}
			}else if(target==BufferedEventTargetEnum.HAND){
				if(variable==BufferedEventVariableEnum.SPEED){
					if(isAdding)data.handRotationSpeed[xyz.id]+=action;
					else        data.handRotationSpeed[xyz.id] =action;
				}else if(variable==BufferedEventVariableEnum.POSITION){
					if(isAdding)data.handRotation[xyz.id]+=action;
					else        data.handRotation[xyz.id] =action;
				}
			}
		}
	}
	@SideOnly(Side.CLIENT)
	public enum BufferedEventTargetEnum{FINGER,HAND;}
	@SideOnly(Side.CLIENT)
	public enum BufferedEventVariableEnum{
		X(0),Y(1),Z(2),X_ROTATION(3),Y_ROTATION(4),Z_ROTATION(5),SPEED,POSITION;
		public int id=-1;
		private BufferedEventVariableEnum(int id){this.id=id;}
		private BufferedEventVariableEnum(){}
	}
	public static void customEventHandler(EntityPlayer player,BufferedEvent event){
		PowerHandData data=ComplexPlayerRenderingData.getFastPowerHandData(player);
		if(data==null)return;
		if(event.time>U.getMC().theWorld.getTotalWorldTime()||event.isActivated)return;
		event.isActivated=true;
		try{
			String name=event.string;
			float action=Float.parseFloat(event.objects[0].toString());
			boolean isAdding=event.objects.length>1?Util.isBoolean(event.objects[1].toString())?Boolean.parseBoolean(event.objects[1].toString()):false:false;
			if(name.equals("noiserHandSpeed")){
				if(isAdding)data.noiserHandSpeed[Integer.parseInt(event.objects[2].toString())]+=action;
				else        data.noiserHandSpeed[Integer.parseInt(event.objects[2].toString())] =action;
			}else if(name.equals("spawnProjectile")){
				int timeHeld=(Integer)event.objects[0];
				EntityBallOfEnergy entity=new EntityBallOfEnergy(player.worldObj, player, 2F+timeHeld/300F,timeHeld);
				Util.spawnEntity(entity);
				entity.motionX+=player.motionX*0.8;
				entity.motionY+=player.motionY*0.8;
				entity.motionZ+=player.motionZ*0.8;
			}else if(name.equals("spawnEntitySubatomicWorldDeconstructor")){
				EntitySubatomicWorldDeconstructor entity=new EntitySubatomicWorldDeconstructor(player.worldObj, player, 1);
				Util.spawnEntity(entity);
				entity.motionX+=player.motionX*0.8;
				entity.motionY+=player.motionY*0.8;
				entity.motionZ+=player.motionZ*0.8;
			}
		}catch(Exception e){e.printStackTrace();}
		
	}
	public static void addANewEvent(EntityPlayer player,long time,String string,Object...objects){
		BufferedEvent event=instance.new BufferedEvent(player,time,string,objects);
		boolean isAdded=false;
		int ID=0;
		for(int id=0;id<events.size();id++){
			BufferedEvent eve=events.get(id);
			if(eve==null){
				isAdded=true;
				ID=id;continue;
			}else if(eve.isActivated){
				isAdded=true;
				ID=id;continue;
			}
		}
		if(isAdded)events.set(ID, event);
		else events.add(event);
	}
	/**
	 * @param time
	 * @param target
	 * @param Id (if target=finger than add {whatFinger,whatBox}, if not than add null)
	 * @param variable
	 * @param xyz
	 */
	public static void addANewEvent(EntityPlayer player, long time,BufferedEventTargetEnum target,int[] Id,BufferedEventVariableEnum variable,BufferedEventVariableEnum xyz,float action,boolean isAdding){
		BufferedEvent event=instance.new BufferedEvent(player,time, target, Id, variable, xyz,action,isAdding);
		boolean isAdded=false;
		int ID=0;
		for(int id=0;id<events.size();id++){
			BufferedEvent eve=events.get(id);
			if(eve==null){
				isAdded=true;
				ID=id;continue;
			}else if(eve.isActivated){
				isAdded=true;
				ID=id;continue;
			}
		}
		if(isAdded)events.set(ID, event);
		else events.add(event);
	}
	public static boolean isActive(EntityPlayer player){
		if(!Util.isItemInStack(MItems.TheHand, player.getCurrentEquippedItem()))return false;
		return player.getCurrentEquippedItem().hasTagCompound();
	}
	@SideOnly(Side.CLIENT)
	public static void generateNewNoiseValue(){
		for(int a1=0;a1<noiser.length;a1++){
			for(int a2=0;a2<noiser[a1].length;a2++){
				for(int a3=0;a3<noiser[a1][a2].length;a3++){
					if(Util.RInt(4)==0)noiser[a1][a2][a3]=Util.CRandF(8);
				}
			}
		}
	}
}
