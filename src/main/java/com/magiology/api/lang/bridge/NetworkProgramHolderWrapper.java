package com.magiology.api.lang.bridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.magiology.api.lang.JSProgramContainer.Program;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkProgramHolder;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

public class NetworkProgramHolderWrapper{
	private static TileEntityNetworkProgramHolder instance;
	public static void setInstance(TileEntityNetworkProgramHolder instance1){
		instance=instance1;
	}
	public static Object run(BlockPosM pos,String name,Object args1){
		try{
			if(instance==null)return "There is no program container! This is an error! Report it to the developer!";
			TileEntityNetworkController brain=instance.getBrain();
			if(brain==null)return "The program container is not connected to a controller!";
			DoubleObject<Program,TileEntityNetworkProgramHolder> program=brain.getProgram(pos, name);
			if(program==null)return "Can't find function at: "+pos+"with name: "+name;
			
			List<Object> argsList=new ArrayList<Object>();
			for(String i:((Map<String,Object>)args1).keySet())argsList.add(((Map<String,Object>)args1).get(i));
			TileEntityNetworkProgramHolder instance2=instance;
			Object result=program.obj1.run(program.obj2,argsList.toArray(), new Object[]{program.obj2.getWorld(),pos});
			instance=instance2;
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "undefined";
	}
}