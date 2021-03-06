package com.magiology.api.power;

public interface ISidedPower extends PowerCore{
	public boolean getIn(int id);
	public void setReceaveOnSide(int direction,boolean bolean);
	
	public boolean getOut(int id);
	public void setSendOnSide(int direction,boolean bolean);
	
	public boolean getAllowedSender(int id);
	public void setAllowedSender(boolean Boolean,int id);
	
	public boolean getAllowedReceaver(int id);
	public void setAllowedReceaver(boolean Boolean,int id);
	
	public boolean getBannedSide(int id);
	public void setBannedSide(boolean Boolean,int id);
}
