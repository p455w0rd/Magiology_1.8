package com.magiology.handlers.animationhandlers.thehand.animation;

import com.magiology.handlers.animationhandlers.thehand.HandData;
import com.magiology.handlers.animationhandlers.thehand.HandPosition;
import com.magiology.handlers.animationhandlers.thehand.animation.HandAnimation.AnimationPart;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.LinearAnimation;

public class LinearHandAnimation extends HandAnimationBase{
	
	public LinearAnimation<HandData> data;
	
	public LinearHandAnimProgressHandler progressHandler;
	
	public LinearHandAnimation(int quality,HandPosition startEnd, LinearHandAnimProgressHandler progressHandler, AnimationPart[] animationData, String name){
		this(quality, startEnd, startEnd, progressHandler, animationData ,name);
	}
	public LinearHandAnimation(int quality,HandPosition start, HandPosition end, LinearHandAnimProgressHandler progressHandler, AnimationPart[] animationData, String name){
		super(start, end, name);
		this.progressHandler=progressHandler;
		HandAnimation anim=new HandAnimation(start, end, animationData,"");
		this.data=anim.toLinearAnimation(quality);
	}
	
	public void update(){
		progressHandler.update();
	}
	
	
	@Override
	public HandData getWantedPos(){
		return data.get(MathUtil.snap(progressHandler.getProgress(), 0, 1))[0];
	}
}