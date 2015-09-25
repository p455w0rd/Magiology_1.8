package com.magiology.mcobjects.tileentityes.hologram;

import com.magiology.api.network.Command;

public interface ICommandInteract{
	public void sendCommand();
	public Object onCommandReceive(Command command);
	public String getName();
	public void setName(String name);
}
