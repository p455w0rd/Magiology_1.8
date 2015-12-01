package com.magiology.mcobjects.tileentityes.hologram.interactions;

import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.util.utilobjects.*;

public class InteractionColor<Host extends HoloObject> extends AbstractInteraction<Host>{
	
	public InteractionColor(){
		super("size","0.69, 0.420");
	}

	@Override
	public Object get(Host host){
		return host.color;
	}

	@Override
	public void set(Host host, ObjectHolder<Boolean> changed, Object setter){
		host.color=(ColorF)setter;
		changed.setVar(true);
	}

	@Override
	public Object parseWords(String[] wordsIn)throws Exception{
		return new ColorF(Float.parseFloat(wordsIn[2]), Float.parseFloat(wordsIn[3]), Float.parseFloat(wordsIn[4]), Float.parseFloat(wordsIn[5]));
	}

}
