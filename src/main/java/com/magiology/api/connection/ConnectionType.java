package com.magiology.api.connection;

public enum ConnectionType{
	Item(0),Fluid(1),Energy(2),Space(3);
	public int id;
	private ConnectionType(int id){
	}
}