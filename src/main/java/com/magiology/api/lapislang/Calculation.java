package com.magiology.api.lapislang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.math.ObjectCalculator;
import com.magiology.util.utilclasses.math.ObjectCalculator.Calculator;

public class Calculation implements Operator{
	public List<Operator> parts;
	public List<Character> functions;
	public List<Calculator> calculators=new ArrayList<Calculator>();
	public Variable saver;
	
	public Calculation(Variable saver,List<Operator> parts1, List<Character> functions1){
		this.functions=functions1;
		this.parts=parts1;
		this.saver=saver==null?new Variable("subReturn", '!', null):saver;
		int min=Integer.MAX_VALUE;
		for(Character character:functions){
			if(!priorities.containsKey(character))throw new IllegalStateException("impossible function: "+character);
			min=Math.min(min, priorities.get(character));
		}
		for(int i=0;i<functions.size();i++){
			if(priorities.get(functions.get(i))>min){
				int start=i,end=-1;
				List<Operator> subParts=new ArrayList<Operator>();
				subParts.add(parts.get(i));
				while(i<functions.size()&&priorities.get(functions.get(i))>min){
					subParts.add(parts.get(end=i+1));
					i++;
				}
				for(int j=0;j<end-start;j++){
					parts.remove(start);
					functions.remove(start);
				}
				List<Character> subFunctions=functions.subList(start, end-1);
				parts.add(start, new Calculation(new Variable("subReturn", '!', null), subParts, subFunctions));
			}
		}
	}
	@Override
	public Object run(){
		try{
			if(calculators.isEmpty()){
				Object result=null;
				for(int i=0;i<parts.size()-1;i++){
					Calculator newCalc=ObjectCalculator.getCalculator(parts.get(i).run(), result==null?parts.get(i+1).run():result, functions.get(i));
					result=newCalc.calc(parts.get(i).run(), result==null?parts.get(i+1).run():result);
					calculators.add(newCalc);
				}
				char type='q';
				if(result.equals("boolean"))type='b';
				else if(result.equals("int"))type='i';
				else if(result.equals("long"))type='l';
				else if(result.equals("float"))type='f';
				else if(result.equals("double"))type='d';
				else if(result.equals("String"))type='s';
				saver.type=type;
			}
			Object result=null;
			
			for(int i=0;i<calculators.size();i++){
				Calculator calc=calculators.get(i);
				result=calc.calc(parts.get(i).run(), result==null?parts.get(i+1).run():result);
			}
			
			saver.value=result;
			return saver.value;
		}catch(Exception e){e.printStackTrace();}
		Util.printInln(parts.size());
		return null;
	}
	@Override
	public String toString(){
		return "\n\tCalculation{\n\t\tparts: "+parts+"'\n\t}";
	}
	private static Map<Character,Integer> priorities=new HashMap<Character,Integer>();
	static{
		priorities.put('+', 0);
		priorities.put('-', 0);
		priorities.put('*', 1);
		priorities.put('/', 1);
		priorities.put('%', 1);
	}
}