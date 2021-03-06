package com.magiology.api.connection;

import java.util.Map;

import net.minecraft.util.EnumFacing;

public interface IConnection{
	public boolean getMain();
	public void setMain(boolean var);
	
	public boolean getIn();
	public void setIn(boolean var);
	public boolean getOut();
	public void setOut(boolean var);
	public boolean getInEnabled();
	public void setInEnabled(boolean var);
	public boolean getOutEnabled();
	public void setOutEnabled(boolean var);
	
	//I have the force! Taaaaaa! ta taaaaa.
	public boolean hasForce();
	public void setForce(boolean var);
	
	public boolean isBanned();
	public void setBanned(boolean var);
	
	public EnumFacing getFaceEF();
	public int getFaceI();
	
	public boolean hasOpposite();
	
	public boolean isEnding();
	public boolean isIntersection();
	public boolean isActive();
	
	public ConnectionType getType();
	
	public Map<String, String> getAllExtra();
	public Object getExtra(String tag);
	public void setExtra(String tag, String obj);
	
	public IConnection[] getMates();
	public IConnectionProvider getHost();
	
	public void clear();
	
	public boolean willRender();
	public void setWillRender(boolean var);
}
