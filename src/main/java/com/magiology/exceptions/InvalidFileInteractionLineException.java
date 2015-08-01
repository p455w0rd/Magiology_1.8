package com.magiology.exceptions;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class InvalidFileInteractionLineException extends Exception{
	
	public InvalidFileInteractionLineException(){
		super();
		FMLCommonHandler.instance().exitJava(404, false);
	}
	public InvalidFileInteractionLineException(String message){
		super(message);
	}
}
