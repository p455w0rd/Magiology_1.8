package com.magiology.api.lapislang;

import static com.magiology.util.utilclasses.Util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.magiology.api.Function;

public class LapisProgram{
	Map<String,String> tags;
	public List<Var> in=new ArrayList<Var>(),vars=new ArrayList<Var>();
	public List<Function> func=new ArrayList<Function>();
	@Override
	public String toString(){
		return "LapisProgram{\n\ttags: "+tags+"\n\tin: "+in+"\n\tvars: "+vars+"\n\tfuncs:"+func+"\n}";
	}
	public Object run(Object...in){
		try{
			for(int i=0;i<this.in.size();i++){
				Var variable=this.in.get(i);
				if(Instanceof(variable.getType(),in[i].getClass()))variable.value=in[i];
				else throw new IllegalArgumentException(variable.getType()+" cannot be set to "+in[i].getClass());
			}
			return func.get(0).run();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}