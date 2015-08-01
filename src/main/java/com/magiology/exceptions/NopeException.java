package com.magiology.exceptions;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class NopeException extends Exception{
	
	public NopeException(){
		super();
		FMLCommonHandler.instance().exitJava(404, false);
	}
	public NopeException(String message){
		super(message);
	}
}
