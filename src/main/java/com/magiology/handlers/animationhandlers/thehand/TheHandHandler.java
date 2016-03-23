package com.magiology.handlers.animationhandlers.thehand;

import java.util.HashMap;
import java.util.Map;

import com.magiology.client.render.itemrender.ItemRendererTheHand;
import com.magiology.core.init.MItems;
import com.magiology.handlers.animationhandlers.thehand.animation.CommonHand;
import com.magiology.handlers.animationhandlers.thehand.animation.HandAnimation;
import com.magiology.handlers.animationhandlers.thehand.animation.HandAnimationBase;
import com.magiology.handlers.animationhandlers.thehand.animation.LinearHandAnimation;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.codeinsert.ObjectProcessor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TheHandHandler{
	static float p=1F/16F;
	
	private static Map<String, HandData> handData;
	public static Map<String, HandData> getHandData(){
		if(handData!=null)return handData;
		handData=new HashMap<>();
		handData.put("prev", new HandData());
		handData.put("actual", new HandData());
		
		handData.put("main", new HandData());
		handData.put("wanted", new HandData());
		handData.put("speed", new HandData());
		
		handData.put("noise", new HandData());
		handData.put("noiseSpeed", new HandData());
		handData.put("noiseWanted", new HandData());
		return handData;
	}
	public static HandData getRenderHandData(){
//		handData=null;
		Map<String, HandData> handData=getHandData();
		HandData
			prev=handData.get("prev"),
			actual=handData.get("actual");
		if(prev.fingers[0].length==0)prev.set(actual);
		
		HandData result=new HandData(false);
		result.base=PartialTicksUtil.calculatePos(prev.base,actual.base);
		result.thumb=PartialTicksUtil.calculatePos(prev.thumb,actual.thumb);
		result.fingers=PartialTicksUtil.calculatePos(prev.fingers,actual.fingers);
		
		return result;
	}
	
	
	public static TheHandHandler instance=new TheHandHandler();
	public static ItemRendererTheHand getRenderer(){return renderer;}
	static ItemRendererTheHand renderer=new ItemRendererTheHand();
	public static HandAnimationBase activeAnimation;
	
	
	
	public static HandPosition getActivePosition(EntityPlayer player){
		if(!isActive(player))return null;
		int id=player.getCurrentEquippedItem().getTagCompound().getInteger("AP");
		if(id<0)return CommonHand.errorPos;
		if(id>=HandPosition.values().length)return CommonHand.errorPos;
		return HandPosition.values()[id];
	}
	public static HandPosition getLastActivePosition(EntityPlayer player){
		if(!isActive(player))return null;
		int id=player.getCurrentEquippedItem().getTagCompound().getInteger("LAP");
		if(id<0)return CommonHand.errorPos;
		if(id>=HandPosition.values().length)return CommonHand.errorPos;
		return HandPosition.values()[id];
	}
	public static HandPosition nextPosition(EntityPlayer player){
		if(!isActive(player))return null;
		HandPosition[] values=HandPosition.values();
		int now=UtilM.getPosInArray(TheHandHandler.getActivePosition(player), values);
		now++;
		if(now==values.length)now=0;
		if(values[now]==CommonHand.errorPos)now++;
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
		if(UtilM.isNull(UtilM.getTheWorld()))return;
		if(!isActive(player))return;
		
		HandPosition handPos=getActivePosition(player);
		if(handPos==CommonHand.errorPos)setActivePositionId(player, 3);
//		setActivePositionId(player, 2);
		
		
		if(UtilM.isRemote(player))animate(player);
	}
	@SideOnly(Side.CLIENT)
	public static void animate(EntityPlayer player){
		
//		setActivePositionId(player, 2);
//		PrintUtil.println(pos.name);
		renderer.secure();
		
		Map<String, HandData> data=getHandData();
		HandData 
			main=data.get("main"),
			actual=data.get("actual"),
			speed=handData.get("speed");
		data.get("prev").set((HandData)actual.clone());
		
		HandData wanted=null;
//		PrintUtil.println(activeAnimation);
		if(activeAnimation!=null){
			
			if(activeAnimation instanceof HandAnimation){
				HandAnimation anim=(HandAnimation)activeAnimation;
				if(!anim.isDone())anim.update(UtilM.getWorldTime());
				if(anim.isDone())activeAnimation=null;
			}
			else if(activeAnimation instanceof LinearHandAnimation){
				
				LinearHandAnimation animation=(LinearHandAnimation)activeAnimation;
				boolean holding=player.isUsingItem();
				animation.progressHandler.setHolding(holding);
				animation.update();
//				if(animation.progressHandler.getProgress()>0.99F)PrintUtil.println(activeAnimation.getWantedPos().sub(CommonHand.WeaponHolder.data));
				if(animation.progressHandler.isInactive())activeAnimation=null;
			}
			if(activeAnimation!=null){
				wanted=activeAnimation.getWantedPos();
			}
		}
		if(wanted==null)wanted=getActivePosition(player).data;
		
		handleSpeed(speed, main, wanted, 14F, 0.2F, 0.6F);
		
		main.set(main.add(speed));
		
		updateNoise(player);
		actual.set(main.add(data.get("noise")));
	}
	
	
	@SideOnly(Side.CLIENT)
	public static void handUseAnimation(EntityPlayer player){
		if(activeAnimation!=null)return;
		activeAnimation=CommonHand.rightClickAnimation;
		CommonHand.rightClickAnimation.start();
	}
	@SideOnly(Side.CLIENT)
	public static void actionAnimation(EntityPlayer player){
		if(activeAnimation!=null)return;
		activeAnimation=CommonHand.chargeUp;
	}
	public static boolean isActive(EntityPlayer player){
		if(!UtilM.isItemInStack(MItems.theHand, player.getCurrentEquippedItem()))return false;
		return player.getCurrentEquippedItem().hasTagCompound();
	}
	private static final ObjectProcessor<Float> speedHandler=new ObjectProcessor<Float>(){
		@Override
		public Float pocess(Float speed, Object...objects){
			float 
				pos=(float)objects[0],
				wantedPos=(float)objects[1],
				acceleration=(float)objects[2],
				friction=(float)objects[3],
				diff=Math.abs(wantedPos-pos),
				act=acceleration,
				speed1=(diff/act);
			
			if(speed1>1)speed1=1;
			else speed1=(speed1*speed1+speed1)/2;
			
			return UtilM.handleSpeedFolower(speed, pos, wantedPos, acceleration)*friction*speed1;
		}
	};
	private static void handleSpeed(HandData speed, HandData pos,HandData wantedPos,float accelerationRot,float accelerationPos, float friction){
		
		for(int i=0;i<speed.base.length;i++)
			speed.base[i]=speedHandler.pocess(speed.base[i], pos.base[i], wantedPos.base[i], i<3?accelerationPos:accelerationRot,friction);
		
		for(int i=0;i<speed.thumb.length;i++)
			speed.thumb[i]=speedHandler.pocess(speed.thumb[i], pos.thumb[i], wantedPos.thumb[i], accelerationRot,friction);
		
		for(int i=0;i<speed.fingers.length;i++)for(int j=0;j<speed.fingers[i].length;j++)
			speed.fingers[i][j]=speedHandler.pocess(speed.fingers[i][j], pos.fingers[i][j], wantedPos.fingers[i][j], accelerationRot,friction);
	}
	
	
	public static void updateNoise(EntityPlayer player){
		
		HandData
			noise=handData.get("noise"),
			noiseSpeed=handData.get("noiseSpeed"),
			noiseWanted=handData.get("noiseWanted");
		
		if(UtilM.getWorldTime(player)%40==0)generateNewNoiseValue();
		
		handleSpeed(noiseSpeed, noise, noiseWanted, 0.01F, 0.00001F, 0.95F);
		
		noise.set(noise.add(noiseSpeed));
	}
	
	@SideOnly(Side.CLIENT)
	public static void generateNewNoiseValue(){
		HandData noiseSpeed=getHandData().get("noiseWanted");
		for(int i=0;i<noiseSpeed.base.length;i++)
			if(RandUtil.RI(4)==0)noiseSpeed.base[i]=RandUtil.CRF(i<3?p*2:p);
		
		for(int i=0;i<noiseSpeed.thumb.length;i++)
			if(RandUtil.RI(4)==0)noiseSpeed.thumb[i]=RandUtil.CRF(3);
		
		for(int i=0;i<noiseSpeed.fingers.length;i++){
			float noise=RandUtil.CRF(3);
			for(int j=0;j<noiseSpeed.fingers[i].length;j++){
				if(j==0)noiseSpeed.fingers[i][j]=RandUtil.CRF(3);
				else noiseSpeed.fingers[i][j]=noise;
			}
		}
	}
	public static void init(){
		CommonHand.load();
		HandPosition.compile();
	}
	
}
