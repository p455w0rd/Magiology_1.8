package com.magiology.mcobjects.tileentityes.hologram;

import com.magiology.mcobjects.items.ProgramContainer.Program;


public interface ICommandInteract{
	public void sendCommand();
	public Object onCommandReceive(Program command);
	public String getName();
	public void setName(String name);
	public Program getActivationTarget();
	public void setActivationTarget(Program com);
}
