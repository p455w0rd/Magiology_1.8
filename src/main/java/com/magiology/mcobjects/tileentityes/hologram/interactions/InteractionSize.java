package com.magiology.mcobjects.tileentityes.hologram.interactions;

import org.lwjgl.util.vector.*;

import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.util.utilobjects.*;

public class InteractionSize<Host extends HoloObject> extends AbstractInteraction<Host>{
	
	public InteractionSize(){
		super("size","0.69, 0.420");
	}

	@Override
	public Object get(Host host){
		return host.size.scale(1);
	}

	@Override
	public void set(Host host, ObjectHolder<Boolean> changed, Object setter){
		host.size=(Vector2f)setter;
		changed.setVar(true);
	}

	@Override
	public Object parseWords(String[] wordsIn)throws Exception{
		return new Vector2f(Float.parseFloat(wordsIn[2]), Float.parseFloat(wordsIn[3]));
	}

}
