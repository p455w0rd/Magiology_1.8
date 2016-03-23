package com.magiology.handlers.animationhandlers.thehand.animation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.handlers.animationhandlers.thehand.HandData;
import com.magiology.handlers.animationhandlers.thehand.HandPosition;
import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.LinearAnimation;
import com.magiology.util.utilobjects.codeinsert.ObjectProcessor;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HandAnimation extends HandAnimationBase{
	
	public HandData wantedPos,speed;
	public AnimationPart[] animationData;
	
	private boolean isRunning;
	private long timeStarted;
	
	public HandAnimation(HandPosition startEnd, AnimationPart[] animationData, String name){
		this(startEnd, startEnd, animationData, name);
	}
	public HandAnimation(HandPosition start, HandPosition end, AnimationPart[] animationData, String name){
		super(start, end, name);
		wantedPos=(HandData)start.data.clone();
		this.animationData=animationData;
	}
	
	
	
	public void update(long time){
		if(isDone()){
			end();
			return;
		}
		
		speed=new HandData();
		int timeRunning=(int)(time-timeStarted);
		boolean noInvoke=true;
		for(AnimationPart animationPart:animationData)if(animationPart.isActive(timeRunning)){
			noInvoke=false;
			animationPart.runner.pocess(speed, animationPart);
		}
		if(noInvoke)isRunning=false;
		wantedPos.set(wantedPos.add(speed));
	}
	private void end(){
		TheHandHandler.setActivePositionId(UtilM.getThePlayer(), end.posInRegistry());
	}
	public boolean isDone(){
		return !isRunning;
	}
	public void start(){
		if(!canStart())return;
		wantedPos.set(start.data);
		timeStarted=UtilM.getWorldTime();
		isRunning=true;
	}
	
	@Override
	public boolean canStart(){
		return super.canStart()&&!isRunning;
	}
	
	public LinearAnimation toLinearAnimation(int quality){
		int tickCount=0;
		List<HandData> data=new ArrayList<HandData>();
		
		wantedPos.set(start.data);
		timeStarted=0;
		isRunning=true;
		
		data.add((HandData)wantedPos.clone());
		int count=0;
		while(isRunning){
			update(count);
			tickCount++;
			if(tickCount>quality){
				data.add((HandData)wantedPos.clone());
				tickCount=0;
			}
			count++;
		}
		data.add((HandData)wantedPos.clone());
		PrintUtil.println(data.size());
		DoubleObject<HandData[], Float>[] data1=new DoubleObject[data.size()];
		Iterator<HandData> data2=data.iterator();
		for(int i=0;i<data1.length;i++)data1[i]=new DoubleObject<HandData[], Float>(new HandData[]{data2.next()}, (i+0F)/data1.length);
		return new LinearAnimation<HandData>(data1);
	}
	@Override
	public HandData getWantedPos(){
		return wantedPos;
	}
	
	
	public static class AnimationPart{
		protected static final AnimationPart[] blank=new AnimationPart[]{new AnimationPart(0, 0, 0, 0)};
		private int start,lenght,posID;
		private float speed;
		
		private ObjectProcessor<HandData> runner;
		
		private static ObjectProcessor[] processors;
		
		public AnimationPart(int pos, int start, int lenght, float speed){
			if(processors==null){
				processors=new ObjectProcessor[]{
						new ObjectProcessor<HandData>(){@Override public HandData pocess(HandData object, Object...objects){
							AnimationPart instance=(AnimationPart)objects[0];
							object.base[instance.posID]+=instance.speed;
							return object;
						}},
						new ObjectProcessor<HandData>(){@Override public HandData pocess(HandData object, Object...objects){
							AnimationPart instance=(AnimationPart)objects[0];
							object.thumb[instance.posID]+=instance.speed;
							return object;
						}},
						new ObjectProcessor<HandData>(){@Override public HandData pocess(HandData object, Object...objects){
							AnimationPart instance=(AnimationPart)objects[0];
							object.fingers[0][instance.posID]+=instance.speed;
							return object;
						}},
						new ObjectProcessor<HandData>(){@Override public HandData pocess(HandData object, Object...objects){
							AnimationPart instance=(AnimationPart)objects[0];
							object.fingers[1][instance.posID]+=instance.speed;
							return object;
						}},
						new ObjectProcessor<HandData>(){@Override public HandData pocess(HandData object, Object...objects){
							AnimationPart instance=(AnimationPart)objects[0];
							object.fingers[2][instance.posID]+=instance.speed;
							return object;
						}},
						new ObjectProcessor<HandData>(){@Override public HandData pocess(HandData object, Object...objects){
							AnimationPart instance=(AnimationPart)objects[0];
							object.fingers[3][instance.posID]+=instance.speed;
							return object;
						}}
					};
			}
//			0:0-5 base=new float[]{0,0,0, 0,0,0};
//			1:6-10 thumb=new float[]{0,0,0, 0, 0};
//			fingers=new float[][]{ 2:11-14 {0,0, 0, 0}, 3 {0,0, 0, 0}, 4 {0,0, 0, 0}, 5 {0,0, 0, 0}};
			this.start=start;
			this.lenght=lenght;
			this.speed=speed;
			
			int processID=-1;
			
			if(pos>=0&&pos<6){
				processID=0;
				posID=pos;
			}
			else if(pos<11){
				processID=1;
				posID=pos-6;
			}
			else if(pos<15){
				processID=2;
				posID=pos-11;
			}
			else if(pos<19){
				processID=3;
				posID=pos-15;
			}
			else if(pos<23){
				processID=4;
				posID=pos-19;
			}
			else if(pos<27){
				processID=5;
				posID=pos-23;
			}
			runner=processors[processID];
			
		}
		
		private boolean isActive(int time){
			return time>=start&&time<=start+lenght;
		}
//		public static AnimationPart[] gen(int pos, float...startLenghtSpeed){
//			assert startLenghtSpeed.length%2!=0:"invalid data!";
//			AnimationPart[] result=new AnimationPart[startLenghtSpeed.length/2];
//			int start=0;
//			for(int i=0;i<result.length;i++){
//				result[i]=new AnimationPart(pos, start, (int)startLenghtSpeed[i*2], startLenghtSpeed[i*2+1]);
//				start+=(int)startLenghtSpeed[i*2];
//			}
//			return result;
//		}
		public static AnimationPart[] gen(int pos, float...startLenghtSpeed){
			assert startLenghtSpeed.length%2!=0:"invalid data!";
			List<AnimationPart> result=new ArrayList<>();
			int start=0,length=startLenghtSpeed.length/2;
			for(int i=0;i<length;i++){
				float speed=startLenghtSpeed[i*2+1];
				result.add(new AnimationPart(pos, start, (int)startLenghtSpeed[i*2], speed));
				start+=(int)startLenghtSpeed[i*2];
			}
			return result.toArray(new AnimationPart[0]);
		}
		
		/*
		 * private int start,lenght,posID;
		private float speed;
		
		private ObjectProcessor<HandData> runner;
		 */
		
		@Override
		public String toString(){
			return new StringBuilder("AnimationPart[")
				.append("start=").append(start)
				.append(", lenght=").append(lenght)
				.append(", posID=").append(posID)
				.append(", speed=").append(speed)
				.append(", runnerID=").append(ArrayUtils.indexOf(processors, runner))
			.append(']').toString();
		}
	}
}
