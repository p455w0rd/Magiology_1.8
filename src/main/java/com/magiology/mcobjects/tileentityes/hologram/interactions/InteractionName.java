package com.magiology.mcobjects.tileentityes.hologram.interactions;

import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.util.utilobjects.*;

public class InteractionName<Host extends HoloObject> extends AbstractInteraction<Host>{
	
	public InteractionName(){
		super("size","0.69, 0.420");
	}

	@Override
	public Object get(Host host){
		return host.getName();
	}

	@Override
	public void set(Host host, ObjectHolder<Boolean> changed, Object setter){
		host.setName((String)setter);
		changed.setVar(true);
	}

	@Override
	public Object parseWords(String[] wordsIn)throws Exception{
		return wordsIn[2];
	}

}
