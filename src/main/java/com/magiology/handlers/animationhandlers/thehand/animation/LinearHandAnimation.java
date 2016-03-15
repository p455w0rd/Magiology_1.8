package com.magiology.handlers.animationhandlers.thehand.animation;

import com.magiology.handlers.animationhandlers.thehand.HandData;
import com.magiology.handlers.animationhandlers.thehand.HandPosition;
import com.magiology.handlers.animationhandlers.thehand.animation.HandAnimation.AnimationPart;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.LinearAnimation;

public class LinearHandAnimation extends HandAnimationBase{
	
	public LinearAnimation<HandData> data;
	protected float progess,progessMul=1;
	
	public LinearHandAnimation(int quality,HandPosition startEnd, AnimationPart...animationData){
		this(quality, startEnd, startEnd, animationData);
	}
	public LinearHandAnimation(int quality,HandPosition start, HandPosition end, AnimationPart...animationData){
		super(start, end);
		HandAnimation anim=new HandAnimation(start, end, animationData);
		this.data=anim.toLinearAnimation(quality);
	}
	public LinearHandAnimation setProgessMul(float mul){
		progessMul=mul;
		return this;
	}
	public void setProgess(float progess){
		this.progess=progess;
	}
	
	@Override
	public HandData getWantedPos(){
		return data.get(MathUtil.snap(progess*progessMul, 0, 1))[0];
	}
}