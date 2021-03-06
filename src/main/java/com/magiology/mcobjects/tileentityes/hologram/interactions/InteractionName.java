package com.magiology.mcobjects.tileentityes.hologram.interactions;

import com.magiology.mcobjects.tileentityes.hologram.HoloObject;
import com.magiology.util.utilobjects.ObjectHolder;

public class InteractionName<Host extends HoloObject> extends AbstractInteraction<Host>{
	
	public InteractionName(){
		super("name","Sample name");
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
		StringBuilder result=new StringBuilder();
		for(int i=2;i<wordsIn.length;i++)result.append(wordsIn[i]+" ");
		return result.toString();
	}
	
	@Override
	public boolean correctWords(String[] wordsIn) throws Exception{
		if(!wordsIn[1].equals(wordsGet[1]))return false;
		boolean isGet=getOrSet(wordsIn),correctLenght;
		if(isGet)correctLenght=commandLenghtGet==wordsIn.length;
		else correctLenght=commandLenghtSet>=wordsIn.length;
		return correctLenght;
	}
}
