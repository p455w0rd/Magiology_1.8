package com.magiology.api.lapislang;

import static com.magiology.util.utilclasses.Util.Instanceof;

import java.util.ArrayList;
import java.util.List;

import com.magiology.api.Function;

public class LapisProgram{
		public String name;
		public List<Var> in=new ArrayList<Var>(),vars=new ArrayList<Var>();
		public List<Function> func=new ArrayList<Function>();
		@Override
		public String toString(){
			return "LapisProgram{\n\tname: "+name+"\n\tin: "+in+"\n\tvars: "+vars+"\n\tfuncs:"+func+"\n}";
		}
		public Object run(Object...in){
			try{
				for(int i=0;i<this.in.size();i++){
					Var variable=this.in.get(i);
					if(Instanceof(variable.getType(),in[i].getClass()))variable.value=in[i];
				}
				return func.get(0).run();
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
	}