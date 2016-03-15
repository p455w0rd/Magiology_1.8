package com.magiology.handlers.animationhandlers.thehand.animation;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.handlers.animationhandlers.thehand.HandData;
import com.magiology.handlers.animationhandlers.thehand.HandPosition;
import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.handlers.animationhandlers.thehand.animation.HandAnimation.AnimationPart;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.codeinsert.ObjectReturn;

public abstract class HandAnimationBase{

	public static HandAnimation rightClickAnimation=new HandAnimation(HandPosition.NaturalPosition, 
			new ObjectReturn<AnimationPart[]>(){
				@Override
				public AnimationPart[] process(){
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
			}.process());
	public static LinearHandAnimation chargeUp=new LinearHandAnimation(2,HandPosition.WeaponHolder, new ObjectReturn<AnimationPart[]>(){
		@Override
		public AnimationPart[] process(){
			return new AnimationPart[]{new AnimationPart(0, 0, 0, 0)};
		}
	}.process());
	
	
	protected final HandPosition start,end;
	
	public HandAnimationBase(HandPosition start, HandPosition end){
		this.start=start;
		this.end=end;
	}
	
	public boolean canStart(){
		return TheHandHandler.getActivePosition(UtilM.getThePlayer()).name.equals(start.name);
	}
	
	public abstract HandData getWantedPos();
}
