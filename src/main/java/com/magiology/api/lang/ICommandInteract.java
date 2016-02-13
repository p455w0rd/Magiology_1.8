package com.magiology.api.lang;

import com.magiology.api.lang.program.ProgramUsable;

public interface ICommandInteract{
	public void sendCommand();
	public Object onCommandReceive(String command);
	public String getName();
	public void setName(String name);
	public boolean isFullBlown();
	
	public String getProgramName();
	public void setProgramName(String name);
	public void setArgs(String args);
	public String getArgs();
}
