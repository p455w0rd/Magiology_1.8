package com.magiology.objhelper.partialtickshelper;

import com.magiology.forgepowered.event.RenderLoopEvents;

public class PartialTicksDouble{
	double var,prevVar;
	public PartialTicksDouble(double var){
		this.var=var;
	}
	public void tickUpdate(double var){
		prevVar=var;
		this.var=var;
	}
	public double getRenderVar(){
		return prevVar+(var-prevVar)*RenderLoopEvents.partialTicks;
	}
	public void setVar(double var){
		this.var=var;
		 prevVar=var;
	}
}
