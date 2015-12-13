package com.magiology.util.utilobjects;

public class ValueTracker<T>{
	private Runnable onChange;
	private T oldValue;
	private boolean useEquals;
	
	public ValueTracker(Runnable onChange,boolean useEquals){
		this.onChange=onChange;
		this.useEquals=useEquals;
	}
	public void updateValue(T newValue){
		if(useEquals?(!newValue.equals(oldValue)):(newValue!=oldValue)){
			onChange.run();
		}
		oldValue=newValue;
	}
}
