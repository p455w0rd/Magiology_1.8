package com.magiology.util.utilobjects;

public class SimpleCounter {
	private int var=0;
	private boolean isFirst=true;
	public int getAndAdd(){
		if(isFirst)isFirst=false;
		else add();
		return var;
	}
	public int get(){
		return var;
	}
	public void add(){
		var++;
	}
}
