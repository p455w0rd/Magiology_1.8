package com.magiology.api.network;

import com.magiology.api.network.NetworkBaseInterface.DataOutput;
import com.magiology.api.network.NetworkBaseInterface.DataOutput.DataOutputDesc;
import com.magiology.objhelper.DoubleObject;

public interface BasicNetworkInterfaceProvider{
	public DoubleObject<DataOutput,Object> getDataOutput(DataOutputDesc desc);
}