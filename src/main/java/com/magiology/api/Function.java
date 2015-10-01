package com.magiology.api;

import static com.magiology.util.utilclasses.Util.printlnAndReturn;

import java.util.ArrayList;
import java.util.List;

import com.magiology.api.lapislang.Calculation;
import com.magiology.api.lapislang.LapisProgram;
import com.magiology.api.lapislang.Operator;
import com.magiology.api.lapislang.Variable;

public class Function implements Operator{
	public List<Operator> operations=new ArrayList<Operator>();
	public Function(LapisProgram lp, String functionContent){
		// result=strength/15
		// return result
		String[] lines=functionContent.split(";");
		for(int i=0;i<lines.length;i++){
			String line=lines[i];
			String[] split=line.split("=|\\*|\\+|\\-|\\/");
			Variable productContainer=null, left=null, right=null;
			char function='!';
			boolean isReturn=true;
			try{
				if(line.charAt(split[0].length())=='=')isReturn=false;
			}catch(Exception e){}

			List<Variable> variables=new ArrayList<Variable>();
			variables.addAll(lp.in);
			variables.addAll(lp.vars);
			if(!isReturn){
				for(Variable variable:variables){
					if(variable.name.equals(split[0]))productContainer=variable;
					if(variable.name.equals(split[1]))left=variable;
					if(variable.name.equals(split[2]))right=variable;
				}
				if(left==null){
					boolean isNum;float num;
					try{num=Float.parseFloat(split[1]);isNum=true;
					}catch(Exception e){isNum=false;num=-1;}
					if(isNum)left=new Variable("num", 'f', num);
				}
				if(right==null){
					boolean isNum;float num;
					try{num=Float.parseFloat(split[2]);isNum=true;
					}catch(Exception e){isNum=false;num=-1;}
					if(isNum)right=new Variable("num", 'f', num);
				}
			}else{
				split=line.split(" ");
				productContainer=new Variable(split[0], 'r', null);
				right=new Variable("stringConverter", 's', "");
				for(Variable variable:variables){
					if(variable.name.equals(split[1]))left=variable;
				}
			}
			
			if(isReturn){
				if(!line.contains("return"))throw new IllegalStateException("return in wrong place");
				List<Operator> vars=new ArrayList<Operator>();
				vars.add(left);
				vars.add(right);
				List<Character> functions=new ArrayList<Character>();
				functions.add('+');
				operations.add(new Calculation(productContainer, vars, functions));
			}else{
				List<Operator> vars=new ArrayList<Operator>();
				vars.add(left);
				vars.add(right);
				List<Character> functions=new ArrayList<Character>();
				functions.add(line.charAt(split[0].length()+1+split[1].length()));
				operations.add(new Calculation(productContainer, vars, functions));
			}
		}
	}
	@Override
	public Object run(){
		for(int i=0;i<operations.size()-1;i++){
			Operator calc=operations.get(i);
			calc.run();
		}
		return printlnAndReturn(operations.get(operations.size()-1).run());
	}
	@Override
	public String toString(){
		return "Calculation{calcs: "+operations+"}";
	}
}
