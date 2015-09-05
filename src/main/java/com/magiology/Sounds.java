package com.magiology;

import com.magiology.util.utilclasses.Helper;

public enum Sounds{
	WingSwingFX("swing1","swing2"),
	WindFX("wind"),
	PopFX("poop"),
	WeirdFX("weird");
	private Sounds(String... names){
		soundNames=names;
	}
	@Override
	public String toString(){
		String result=null;
		if(soundNames.length==1)result=soundNames[0];
		else result=soundNames[Helper.RInt(soundNames.length)];
		return result;
	}
	public String[] soundNames;
}
