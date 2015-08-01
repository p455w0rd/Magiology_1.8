package com.magiology.exceptions;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class FileNotValidException extends Exception{
	
	public FileNotValidException(){
		super();
		FMLCommonHandler.instance().exitJava(404, false);
	}
	public FileNotValidException(String message){
		super(message);
	}
}
