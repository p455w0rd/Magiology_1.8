package com.magiology.api.lang;

import com.magiology.mcobjects.items.ProgramContainer.Program;


public interface ICommandInteract{
	public void sendCommand();
	public Object onCommandReceive(String command);
	public String getName();
	public void setName(String name);
	public Program getActivationTarget();
	public void setActivationTarget(Program com);
	public boolean isFullBlown();
}
