package com.magiology.mcobjects.tileentityes.hologram.interactions;

import com.magiology.api.lang.*;
import com.magiology.util.utilobjects.*;

public abstract class AbstractInteraction<Host extends ICommandInteract>{
	
	protected final String exampleGet,exampleSet,wordsGet[],wordsSet[];
	protected final int commandLenghtSet,commandLenghtGet,dataSize;
	
	public AbstractInteraction(String name,String inData){
		wordsGet=(this.exampleGet="get "+name           ).split(" ");
		wordsSet=(this.exampleSet="set "+name+" "+inData).split(" ");
		commandLenghtSet=wordsSet.length;
		commandLenghtGet=wordsGet.length;
		dataSize=commandLenghtSet-commandLenghtGet;
	}
	
	protected boolean getOrSet(String[] wordsIn)throws Exception{
		if(wordsIn[0].toLowerCase().equals("set"))return false;
		if(wordsIn[0].toLowerCase().equals("get"))return true;
		throw new IllegalStateException("Invalid command!");
	}
	public boolean correctWords(String[] wordsIn)throws Exception{
		if(!wordsIn[1].equals(wordsGet[1]))return false;
		boolean isGet=getOrSet(wordsIn),correctLenght;
		if(isGet)correctLenght=commandLenghtGet==wordsIn.length;
		else correctLenght=commandLenghtSet==wordsIn.length;
	
		return correctLenght;
	}
	
	public abstract Object get(Host host);
	public abstract void set(Host host, ObjectHolder<Boolean> changed, Object setter);
	public abstract Object parseWords(String[] wordsIn)throws Exception;
	
}
