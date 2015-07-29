package com.magiology.objhelper.partialtickshelper;

import com.magiology.forgepowered.event.RenderLoopEvents;

public class PartialTicksFloat{
	public float var,prevVar;
	public PartialTicksFloat(float var){
		this.var=var;
	}
	public PartialTicksFloat tickUpdate(float var){
		prevVar=var;
		this.var=var;
		return this;
	}
	public PartialTicksFloat tickUpdateAdd(float var){
		prevVar=var;
		this.var+=var;
		return this;
	}
	public float getRenderVar(){
		return prevVar+(var-prevVar)*RenderLoopEvents.partialTicks;
	}
	public PartialTicksFloat setVar(float var){
		this.var=var;
		 prevVar=var;
		return this;
	}
}
