package com.magiology.handlers.animationhandlers.thehand;

import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.ObjectProcessor;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HandAnimation{
	
	private final HandPosition start,end;
	public HandData wantedPos,speed;
	private AnimationPart[] animationData;
	
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
			end();
			return;
		}
		
		speed=new HandData();
		int timeRunning=(int)(UtilM.getWorldTime()-timeStarted);
		for(AnimationPart animationPart:animationData){
			if(animationPart.isActive(timeRunning))animationPart.runner.pocess(speed, animationPart);
		}
		wantedPos.set(wantedPos.add(speed));
	}
	
	public boolean isDone(){
		return !isRunning;
	}
	
	private void end(){
		TheHandHandler.setActivePositionId(UtilM.getThePlayer(), end.posInRegistry());
	}
	
	public boolean canStart(){
		return TheHandHandler.getActivePosition(UtilM.getThePlayer()).name.equals(start.name);
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
				processID=2;
				posID=pos-15;
			}
			else if(pos<23){
				processID=3;
				posID=pos-19;
			}
			else if(pos<27){
				processID=4;
				posID=pos-23;
			}
			
			runner=processors[processID];
			
		}
		
		private boolean isActive(long time){
			return time>=start&&time<=start+lenght;
		}
	}
}
