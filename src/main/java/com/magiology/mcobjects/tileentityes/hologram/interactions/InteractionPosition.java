package com.magiology.mcobjects.tileentityes.hologram.interactions;

import org.lwjgl.util.vector.*;

import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.util.utilobjects.*;

public class InteractionPosition<Host extends HoloObject> extends AbstractInteraction<Host>{
	
	public InteractionPosition(){
		super("pos","0.69, 0.420");
	}

	@Override
	public Object get(Host host){
		return host.position;
	}

	@Override
	public void set(Host host, ObjectHolder<Boolean> changed, Object setter){
		host.position=(Vector2f)setter;
	}

	@Override
	public Object parseWords(String[] wordsIn)throws Exception{
		return new Vector2f(Float.parseFloat(wordsIn[4]), Float.parseFloat(wordsIn[5]));
	}

}
