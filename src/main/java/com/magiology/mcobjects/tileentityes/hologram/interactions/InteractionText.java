package com.magiology.mcobjects.tileentityes.hologram.interactions;

import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.util.utilobjects.*;

public class InteractionText<Host extends HoloObject> extends AbstractInteraction<Host>{
	
	public InteractionText(){
		super("size","0.69, 0.420");
	}

	@Override
	public Object get(Host host){
		return ((StringContainer)host).getString();
	}

	@Override
	public void set(Host host, ObjectHolder<Boolean> changed, Object setter){
		((StringContainer)host).setString((String)setter);
		changed.setVar(true);
	}

	@Override
	public Object parseWords(String[] wordsIn)throws Exception{
		StringBuilder result=new StringBuilder();
		for(int i=2;i<wordsIn.length;i++)result.append(wordsIn[i]+" ");
		return result.toString();
	}

}
