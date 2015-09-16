package com.magiology.api.connection;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.EnumFacing;

import com.magiology.util.utilclasses.SideUtil;

//IConnection code holder
public class Connection implements IConnection{
	
	private final EnumFacing face;
	private final ConnectionType type;
	private final IConnectionProvider host;
	
	private boolean main,in,out,inEnabled,outEnabled,banned,willRender=true,thePowerOfTheDarkSide=true;
	
	private Map<String, String> extra=new HashMap<String, String>();
	
	public Connection(IConnectionProvider host,ConnectionType type,int side){
		this(host, type, EnumFacing.getFront(side));
	}
	public Connection(IConnectionProvider host,ConnectionType type,EnumFacing side){
		this.type=type;
		this.face=side;
		this.host=host;
	}
	
	@Override
	public boolean hasOpposite(){
		IConnection[] mates=getMates();
		return mates[SideUtil.getOppositeSide(getFaceI())].isActive();
	}
	@Override
	public boolean isActive(){
		return getMain()||(getIn()||getOut());
	}
	//top of the morning dear ladies
	private int getActiveMates(){
		int result=0;
		for(IConnection i:getMates())if(i.isActive())result++;
		return result;
	}
	@Override
	public void clear(){
		setMain(false);
		setIn(false);
		setOut(false);
	}

	@Override public boolean getIn(){return in;}
	@Override public boolean getOut(){return out;}
	@Override public boolean getMain(){return main;}
	@Override public void setIn(boolean var){in=var;}
	@Override public void setOut(boolean var){out=var;}
	@Override public boolean isBanned(){return banned;}
	@Override public void setMain(boolean var){main=var;}
	@Override public EnumFacing getFaceEF(){return face;}
	@Override public ConnectionType getType(){return type;}
	@Override public int getFaceI(){return face.getIndex();}
	@Override public void setBanned(boolean var){banned=var;}
	@Override public boolean willRender(){return willRender;}
	@Override public boolean getInEnabled(){return inEnabled;}
	@Override public boolean getOutEnabled(){return outEnabled;}
	@Override public IConnectionProvider getHost(){return host;}
	@Override public void setInEnabled(boolean var){inEnabled=var;}
	@Override public boolean isEnding(){return getActiveMates()==1;}
	@Override public void setWillRender(boolean var){willRender=var;}
	@Override public void setOutEnabled(boolean var){outEnabled=var;}
	@Override public Map<String, String> getAllExtra(){return extra;}
	@Override public boolean hasForce(){return thePowerOfTheDarkSide;}
	@Override public Object getExtra(String tag){return extra.get(tag);}
	@Override public boolean isIntersection(){return getActiveMates()>2;}
	@Override public void setForce(boolean var){thePowerOfTheDarkSide=var;}
	@Override public IConnection[] getMates(){return host.getConnections();}
	@Override public void setExtra(String tag, String obj){extra.put(tag, obj);}
}