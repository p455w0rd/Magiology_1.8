package com.magiology.util.renderers;

import com.magiology.util.utilclasses.*;

public class PartialTicks1F{
	
	public float value,prevValue;
	
	public PartialTicks1F(){}
	public PartialTicks1F(float value){
		this.value=prevValue=value;
	}
	
	public void update(float newValue){
		prevValue=value;
		value=newValue;
	}
	
	public float get(){
		return UtilM.calculatePos(prevValue, value);
	}
}
