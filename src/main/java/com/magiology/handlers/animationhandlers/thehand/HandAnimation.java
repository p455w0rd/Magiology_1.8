package com.magiology.handlers.animationhandlers.thehand;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.codeinsert.ObjectProcessor;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HandAnimation{
	
	public static HandAnimation rightClickAnimation=new HandAnimation(HandPosition.NaturalPosition, HandPosition.NaturalPosition, 
			new ObjectProcessor<AnimationPart[]>(){

				@Override
				public AnimationPart[] pocess(AnimationPart[] object, Object...objects){
					AnimationPart[] animData=            AnimationPart.gen(12,       3,-2,  2,-1,  1,-0.5F,  2,1,  3,2);
					animData=ArrayUtils.addAll(animData, AnimationPart.gen(13, 2,0,  3,-3,  2,-2,  1,-1,  2,2,  3,3));
					animData=ArrayUtils.addAll(animData, AnimationPart.gen(14, 5,0,  4,-0.5F,  4,0.5F));
					
					animData=ArrayUtils.addAll(animData, AnimationPart.gen(16,       3,-2,  2,-1,  1,-0.5F,  2,1,  3,2));
					animData=ArrayUtils.addAll(animData, AnimationPart.gen(17, 2,0,  3,-1,  2,-2,  1,-1,  2,2,  3,1));
					animData=ArrayUtils.addAll(animData, AnimationPart.gen(18, 5,0,  4,-0.5F,  4,0.5F));
					
					animData=ArrayUtils.addAll(animData, AnimationPart.gen(20,       3,-2,  2,-1,  1,-0.5F,  2,1,  3,2));
					animData=ArrayUtils.addAll(animData, AnimationPart.gen(21, 2,0,  3,-1,  2,-2,  1,-1,  2,2,  3,1));
					animData=ArrayUtils.addAll(animData, AnimationPart.gen(22, 5,0,  4,-0.5F,  4,0.5F));

					animData=ArrayUtils.addAll(animData, AnimationPart.gen(24,       3,-2,  2,-1,  1,-0.5F,  2,1,  3,2));
					animData=ArrayUtils.addAll(animData, AnimationPart.gen(25, 2,0,  3,-1,  2,-2,  1,-1,  2,2,  3,1));
					animData=ArrayUtils.addAll(animData, AnimationPart.gen(26, 5,0,  4,-0.5F,  4,0.5F));
					
					animData=ArrayUtils.addAll(animData, AnimationPart.gen(7,        3,2,  2,1,  2,0.5F,  2,-1,  3,-2));
					animData=ArrayUtils.addAll(animData, AnimationPart.gen(10,       3,4,  2,2,  2,1F,  2,-2,  3,-4));
					
					animData=ArrayUtils.addAll(animData, AnimationPart.gen(2,        3,U.p*2,  2,U.p*1F,  2,-U.p*1F,  3,-U.p*2F));
					animData=ArrayUtils.addAll(animData, AnimationPart.gen(3,        4,-1,  4,1F));
					return animData;
				}
				
			}.pocess(null));
	
	
	private final HandPosition start,end;
	public HandData wantedPos,speed;
	public AnimationPart[] animationData;
	
	private boolean isRunning;
	private long timeStarted;
	
	public HandAnimation(HandPosition start, HandPosition end, AnimationPart...animationData){
		this.start=start;
		this.end=end;
		wantedPos=(HandData)start.data.clone();
		this.animationData=animationData;
	}
	
	
	
	public void update(){
		if(isDone()){
			TheHandHandler.setActivePositionId(UtilM.getThePlayer(), end.posInRegistry());
			return;
		}
		
		speed=new HandData();
		int timeRunning=(int)(UtilM.getWorldTime()-timeStarted);
		boolean noInvoke=true;
		for(AnimationPart animationPart:animationData)if(animationPart.isActive(timeRunning)){
			noInvoke=false;
			animationPart.runner.pocess(speed, animationPart);
		}
		if(noInvoke)isRunning=false;
		wantedPos.set(wantedPos.add(speed));
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
	
	public boolean canStart(){
		return TheHandHandler.getActivePosition(UtilM.getThePlayer()).name.equals(start.name)&&!isRunning;
	}
	
	
	
	public static class AnimationPart{
		private int start,lenght,posID;
		private float speed;
		
		private ObjectProcessor<HandData> runner;
		
		private static ObjectProcessor[] processors={
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
		
		public AnimationPart(int pos, int start, int lenght, float speed){
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
		public static AnimationPart[] gen(int pos, float...startLenghtSpeed){
			assert startLenghtSpeed.length%2!=0:"invalid data!";
			AnimationPart[] result=new AnimationPart[startLenghtSpeed.length/2];
			int start=0;
			for(int i=0;i<result.length;i++){
				result[i]=new AnimationPart(pos, start, (int)startLenghtSpeed[i*2], startLenghtSpeed[i*2+1]);
				start+=(int)startLenghtSpeed[i*2];
			}
			return result;
		}
	}
}
