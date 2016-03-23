package com.magiology.handlers.animationhandlers.thehand.animation;

import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.util.utilclasses.math.MathUtil;

public abstract class LinearHandAnimProgressHandler{
	protected float progress;
	protected int timeHeld;
	private boolean holding,held;
	
	public final void setHolding(boolean holding){
		held=this.holding;
		this.holding=holding;
		if(held!=this.holding){
			if(holding)onHoldingStart();
			else onHoldingEnd();
		}
	}
	protected void passOnAnimation(HandAnimationBase animation){
		timeHeld=2;
		TheHandHandler.activeAnimation=animation;
	}
	
	public abstract void update();
	public abstract void onHoldingStart();
	public abstract void onHoldingEnd();
	public abstract float getProgress();
	public abstract boolean willRestrictItemSwitching();
	public abstract boolean isInactive();
	
	public static abstract class LinearHandAnimProgressHandlerClassic extends LinearHandAnimProgressHandler{
		public abstract int getTimeForAnimation();
		protected int timeMul=-1;
		
		
		@Override
		public boolean willRestrictItemSwitching(){
			return true;
		}
		@Override
		public void update(){
			timeHeld=MathUtil.snap(timeHeld+timeMul, 0, getTimeForAnimation());
			progress=timeHeld/(float)getTimeForAnimation();
		}
		
		@Override
		public float getProgress(){
			return progress;
		}

		@Override
		public void onHoldingStart(){
			timeMul=1;
		}

		@Override
		public void onHoldingEnd(){
			timeMul=-1;
		}
		@Override
		public boolean isInactive(){
			return timeHeld==0;
		}
	}
}