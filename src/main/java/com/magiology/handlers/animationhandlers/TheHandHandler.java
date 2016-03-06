package com.magiology.handlers.animationhandlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.magiology.client.render.itemrender.ItemRendererTheHand;
import com.magiology.core.init.MItems;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.ObjectHolder;
import com.magiology.util.utilobjects.ObjectProcessor;

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
	
	public static enum HandComonPositions{
		ErrorPos(new HandData()),
		ClosedFist(new ObjectProcessor<HandData>(){@Override public HandData pocess(HandData object, Object...objects){
			object.base[0]=0;
			object.base[1]=0;
			object.base[2]=0;
			
			object.base[3]=0;
			object.base[4]=0;
			object.base[5]=0;
			
			object.thumb[0]=28;
			object.thumb[1]=10;
			object.thumb[2]=20;
			object.thumb[3]=-20;
			object.thumb[4]=-90;
			
			object.fingers[0][1]=80;
			object.fingers[0][2]=110;
			object.fingers[0][3]=75;
			
			object.fingers[1][1]=80;
			object.fingers[1][2]=100;
			object.fingers[1][3]=90;
			
			object.fingers[2][1]=85;
			object.fingers[2][2]=95;
			object.fingers[2][3]=80;
			
			object.fingers[3][1]=95;
			object.fingers[3][2]=80;
			object.fingers[3][3]=110;
			return object;
		}}.pocess(new HandData())),
		WeaponHolder(new ObjectProcessor<HandData>(){@Override public HandData pocess(HandData data, Object...objects){
			data.base[0]=0;
			data.base[1]=-p*3;
			data.base[3]=30;
			data.base[4]=15;
			data.base[5]=40;
			
			data.thumb[0]=60;
			data.thumb[1]=10;
			data.thumb[2]=10;
			data.thumb[3]=-5;
			data.thumb[4]=-20;
			
			data.fingers[0][0]=-10;
			data.fingers[0][1]=3;
			data.fingers[0][2]=65;
			data.fingers[0][3]=10;
			
			data.fingers[1][0]=5;
			data.fingers[1][1]=20;
			data.fingers[1][2]=45;
			data.fingers[1][3]=20;
			
			data.fingers[2][0]=15;
			data.fingers[2][1]=25;
			data.fingers[2][2]=35;
			data.fingers[2][3]=15;
			
			data.fingers[3][0]=20;
			data.fingers[3][1]=25;
			data.fingers[3][2]=20;
			data.fingers[3][3]=20;
			return data;
		}}.pocess(new HandData())),
		LookAtSomething(new ObjectProcessor<HandData>(){@Override public HandData pocess(HandData object, Object...objects){
			
			
			
			return object;
		}}.pocess(new HandData())),
		NaturalPosition(new ObjectProcessor<HandData>(){@Override public HandData pocess(HandData data, Object...objects){
			data.base[1]=-p*7;
			data.base[3]=15;
			data.base[4]=0;
			data.base[5]=15;
			
			data.thumb[0]=20;
			data.thumb[1]=20;
			data.thumb[3]=-10;
			data.thumb[4]=-30;
			
			data.fingers[0][1]=12;
			data.fingers[0][2]=40;
			data.fingers[0][3]=5;
			
			data.fingers[1][1]=17;
			data.fingers[1][2]=20;
			data.fingers[1][3]=2;
			
			data.fingers[2][1]=17;
			data.fingers[2][2]=23;
			data.fingers[2][3]=2;
			
			data.fingers[3][1]=24;
			data.fingers[3][2]=15;
			data.fingers[3][3]=2;
			return data;
		}}.pocess(new HandData()));
		
		public HandData data;
		private HandComonPositions(HandData data){
			this.data=data;
		}
		/**THIS IS ONLY FOR TESTING!*/
		public void set(HandData data){
			this.data=data;
		}
	}
	
	
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
		int now=UtilM.getPosInArray(TheHandHandler.getActivePosition(player), values);
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
		if(UtilM.isNull(UtilM.getTheWorld()))return;
		if(!isActive(player))return;
		
		HandComonPositions handPos=getActivePosition(player);
		if(handPos==HandComonPositions.ErrorPos)setActivePositionId(player, 3);
		
		
		if(UtilM.isRemote(player))animate(player);
	}
	@SideOnly(Side.CLIENT)
	public static void animate(EntityPlayer player){
		setActivePositionId(player, 3);
		HandComonPositions pos=getActivePosition(player);
		PrintUtil.println(pos);
		renderer.secure();
		
		
		Map<String, HandData> data=getHandData();
		HandData 
			main=data.get("main"),
			actual=data.get("actual"),
			speed=handData.get("speed");
		data.get("prev").set((HandData)actual.clone());
		
		
		
		Map<String, HandData> handData=getHandData();
		
		handleSpeed(speed, main, pos.data, 4F, 1F, 0.6F);
		
		main.set(main.add(speed));
		
		
		updateNoise(player);
		actual.set(main.add(handData.get("noise")));
	}
	@SideOnly(Side.CLIENT)
	public static void handUseAnimation(EntityPlayer player){
		if(!UtilM.isRemote(player))return;
		//TODO: animations!
		
		
	}
	@SideOnly(Side.CLIENT)
	public static void actionAnimation(EntityPlayer player){
		if(!UtilM.isRemote(player))return;
		//TODO: animations!
		
		
	}
	public static boolean isActive(EntityPlayer player){
		if(!UtilM.isItemInStack(MItems.theHand, player.getCurrentEquippedItem()))return false;
		return player.getCurrentEquippedItem().hasTagCompound();
	}
	
	private static void handleSpeed(HandData speed, HandData pos,HandData wantedPos,float accelerationRot,float accelerationPos, float friction){
		
		final ObjectProcessor<Float> handler=new ObjectProcessor<Float>(){
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
				else speed1*=speed1;
				
				return UtilM.handleSpeedFolower(speed, pos, wantedPos, acceleration)*friction*speed1;
			}
		};
		
		for(int i=0;i<speed.base.length;i++)
			speed.base[i]=handler.pocess(speed.base[i], pos.base[i], wantedPos.base[i], i<3?accelerationPos:accelerationRot,friction);
		
		for(int i=0;i<speed.thumb.length;i++)
			speed.thumb[i]=handler.pocess(speed.thumb[i], pos.thumb[i], wantedPos.thumb[i], accelerationRot,friction);
		
		for(int i=0;i<speed.fingers.length;i++)for(int j=0;j<speed.fingers[i].length;j++)
			speed.fingers[i][j]=handler.pocess(speed.fingers[i][j], pos.fingers[i][j], wantedPos.fingers[i][j], accelerationRot,friction);
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
			if(UtilM.RInt(4)==0)noiseSpeed.base[i]=UtilM.CRandF(i<3?p*2:p);
		
		for(int i=0;i<noiseSpeed.thumb.length;i++)
			if(UtilM.RInt(4)==0)noiseSpeed.thumb[i]=UtilM.CRandF(3);
		
		for(int i=0;i<noiseSpeed.fingers.length;i++)
			for(int j=0;j<noiseSpeed.fingers[i].length;j++)
				if(UtilM.RInt(4)==0)noiseSpeed.fingers[i][j]=UtilM.CRandF(3);
	}
	
	public static class HandData{
		
		public float[] base,thumb,fingers[];
		
		public HandData(){
			this(true);
		}
		public HandData(boolean init){
			if(init){
				base=new float[]{0,0,0, 0,0,0};
				thumb=new float[]{0,0,0, 0, 0};
				fingers=new float[][]{{0,0, 0, 0},{0,0, 0, 0},{0,0, 0, 0},{0,0, 0, 0}};
			}
		}
		
		public HandData setForEach(Consumer<ObjectHolder<Float>> consumer){
			for(int i=0;i<base.length;i++){
				ObjectHolder<Float> num=new ObjectHolder<Float>(base[i]);
				consumer.accept(num);
				base[i]=num.getVar();
			}
			for(int i=0;i<thumb.length;i++){
				ObjectHolder<Float> num=new ObjectHolder<Float>(thumb[i]);
				consumer.accept(num);
				thumb[i]=num.getVar();
			}
			for(int i=0;i<fingers.length;i++){
				for(int j=0;j<fingers[i].length;j++){
					ObjectHolder<Float> num=new ObjectHolder<Float>(fingers[i][j]);
					consumer.accept(num);
					fingers[i][j]=num.getVar();
				}
			}
			return this;
		}
		
		public void set(HandData handData){
			base=handData.base;
			thumb=handData.thumb;
			fingers=handData.fingers;
		}
		public HandData add(HandData handData){
			HandData result=new HandData(false);
			result.base=UtilM.addToFloatArray(base, handData.base);
			result.thumb=UtilM.addToFloatArray(thumb, handData.thumb);
			result.fingers=UtilM.addToDoubleFloatArray(fingers, handData.fingers);
			return result;
		}
		@Override
		protected Object clone(){
			HandData result=new HandData(false);
			result.base=base.clone();
			result.thumb=thumb.clone();
			result.fingers=new float[fingers.length][0];
			for(int i=0;i<fingers.length;i++)fingers[i]=Arrays.copyOf(fingers[i], fingers[i].length);
			return result;
		}
	}
}
