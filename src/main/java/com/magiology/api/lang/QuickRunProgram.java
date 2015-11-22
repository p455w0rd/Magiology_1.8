package com.magiology.api.lang;


public class QuickRunProgram{
	
	public final String src;
	
	protected QuickRunProgram(String src){
		this.src=src;
	}
	
	public Object run(Object[] args,Object[] environment){
		return ProgramDataCenter.run(src, args,ProgramHandeler.defultVars(environment));
	}
}
