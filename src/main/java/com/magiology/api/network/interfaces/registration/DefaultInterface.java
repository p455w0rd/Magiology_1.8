package com.magiology.api.network.interfaces.registration;

import com.magiology.api.network.BasicWorldNetworkInterface;
import com.magiology.api.network.NetworkBaseInterface.DataOutput;
import com.magiology.api.network.NetworkBaseInterface.DataOutput.DataOutputDesc;
import com.magiology.util.utilobjects.DoubleObject;

public class DefaultInterface implements BasicWorldNetworkInterface{

	@Override
	public DoubleObject<DataOutput,Object> getData(DataOutputDesc desc){
		return null;
	}
	
}
