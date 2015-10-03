package com.magiology.api.lapislang;

import static com.magiology.util.utilclasses.Util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3i;

import com.magiology.api.Function;
import com.magiology.mcobjects.items.CommandContainer;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.OhBabyATriple;


public class LapisLangCompiler{
	
	public static LapisProgram compile(ItemStack programContainer){
		LapisProgram program=compile(CommandContainer.getCode(programContainer));
		if(program==null)return null;
		String name=program.tags.get("name"),Pos=program.tags.get("pos");
		CommandContainer.setCommandName(programContainer, name!=null?name:"<NULL>");
		String[] pos=Pos!=null?Pos.split(","):null;
		try{
			CommandContainer.setPos(programContainer, new Vec3i(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(pos[2])));
		}catch(Exception e){
			e.printStackTrace();
		}
		return program;
	}
	public static LapisProgram compile(String program){
		try{
			String newline=System.getProperty("line.separator");
			program=program.replaceAll(newline, "");
			while(program.contains("  "))program=program.replaceAll("  ", " ");
			program=LapisLangCompiler.removeSpacesFrom(program,'{','}','(',')','=',';','*','/','+','-',newline.charAt(0),'%','!','>','<','@','#');
			
			LapisProgram lp=new LapisProgram();
			Map<String,String> tags=new HashMap<String,String>();
			
			parseTags(program,tags);
			String[] in=LapisLangCompiler.getMapInBrackets(program,"in");
			if(in!=null)LapisLangCompiler.stringMapToVars(in,lp.in);
			
			String[] vars=LapisLangCompiler.getMapInBrackets(program,"vars");
			if(vars!=null)LapisLangCompiler.stringMapToVars(vars,lp.vars);
			
			String main=LapisLangCompiler.getInBrackets(program,"out main()");
			if(main!=null){
				if(!main.contains("return "))return null;
				lp.func.add(new Function(lp,main));
			}else return null;
			
			lp.tags=tags;
			return lp;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private static void parseTags(String source, Map<String,String> tags){
		String[] lines=source.split(";");
		for(String line:lines){
			if(line.startsWith("#")&&line.contains("->")&&line.contains("\"")){
				List<OhBabyATriple<Integer,Integer,String>> content=detectAndExtractString(line);
				if(content.size()==1){
					String name=line.substring(line.indexOf("#")+1,line.indexOf("->"));
					name.substring(booleanToInt(name.startsWith(" ")), name.length()-booleanToInt(name.endsWith(" ")));
					tags.put(name, content.get(0).obj3.substring(1, content.get(0).obj3.length()-1));
				}else break;
			}else break;
		}
	}
	public static String getInBrackets(String program, String bracketName){
		if(program.contains(bracketName)){
			int start=-1,end=-1;
			for(int i=program.indexOf(bracketName)+bracketName.length();i<program.length();i++){
				char ch=program.charAt(i);
				if(start==-1&&ch=='{')start=i+1;
				if(end==-1&&ch=='}'){
					end=i;
				}
				if(start!=-1&&end!=-1)continue;
			}
			return program.substring(start, end);
		}
		return null;
	}

	public static void stringMapToVars(String[] map, List<Var> listSaver){
		for(int i=0;i<map.length;i++){
			String[] temp=map[i].substring(map[i].startsWith(" ")?1:0, map[i].endsWith(" ")?map[i].length()-1:map[i].length()).split(" ");
			if(temp.length==2){
				char type='q';
				if(temp[0].equals("boolean"))type='b';
				else if(temp[0].equals("int"))type='i';
				else if(temp[0].equals("long"))type='l';
				else if(temp[0].equals("float"))type='f';
				else if(temp[0].equals("double"))type='d';
				else if(temp[0].equals("String"))type='s';
				if(type!='q')listSaver.add(new Var(temp[1], type, 0));
			}
		}
	}

	public static String[] getMapInBrackets(String program, String bracketName){
		String content=getInBrackets(program, bracketName);
		if(content!=null)return content.split(";");
		return null;
	}

	public static String removeSpacesFrom(String raw,char...cs){
		String result=new String(raw);
		for(char c:cs){
			while(result.contains(c+" "))result=result.replace(c+" ", c+"");
			while(result.contains(" "+c))result=result.replace(" "+c, c+"");
		}
		return result;
	}
	
	public static DoubleObject<Calculation,Boolean> getcalculationFromLine(LapisProgram lp, String line){
		List<Var> aviableVars=new ArrayList<Var>();
		aviableVars.addAll(lp.in);
		aviableVars.addAll(lp.vars);
		
		//handle return
		if(line.contains("return ")){
			line=line.replace("return ", "");
			List vars,functions;
			//using this so java unloads the data var instantly
			{
				//phrase variables and functions from the dank vars provided by the program
				DoubleObject<List<Operator>,List<Character>> data=parseVars(line, aviableVars);
				//export
				vars=data.obj1;
				functions=data.obj2;
			}
			//make a dummy saver because it is just returning and it is not actually used & build a calculation from phrased mgl vars
			return new DoubleObject<Calculation,Boolean>(new Calculation(new Var("return", 'r', null), vars, functions), true);
		}
		//handle normal calculation
		else if(line.contains("=")){
			String split[]=line.split("="),left=split[0],right=split[1];
			if(split.length!=2)return new DoubleObject<Calculation,Boolean>(null, false);
			
			List vars,functions;
			//using this so java unloads the data var instantly
			{
				//phrase variables and functions from the dank vars provided by the program
				DoubleObject<List<Operator>,List<Character>> data=parseVars(right, aviableVars);
				//export
				vars=data.obj1;
				functions=data.obj2;
			}
			return new DoubleObject<Calculation,Boolean>(new Calculation((Var)parseVars(left, aviableVars).obj1.get(0), vars, functions), false);
		}
		return new DoubleObject<Calculation,Boolean>(null, false);
	}
	private static DoubleObject<List<Operator>,List<Character>> parseVars(String source, List<Var> aviableVars){
		List<OhBabyATriple<Integer,Integer,String>> strings=detectAndExtractString(source);
		String[] split=source.split("\\*|\\+|\\-|\\/");
		List<Operator>  vars	 =new ArrayList<Operator>();
		List<Character> functions=new ArrayList<Character>();
		int pos=-1;
		for(int i=0;i<split.length-1;i++){
			pos+=split[i].length()+1;
			functions.add(source.charAt(pos));
		}
		for(String varName:split){
			boolean isString=false;
			if(!strings.isEmpty())for(int i=0;i<strings.size();i++){
				if(strings.get(i).obj3.equals(varName)){
					i=strings.size();
					isString=true;
					vars.add(new Var("str", 's', varName.substring(1, varName.length()-1)));
				}
			}
			if(!isString){
				boolean added=false;
				for(int i=0;i<aviableVars.size();i++){
					Var aviableVar=aviableVars.get(i);
					if(aviableVar.name.equals(varName)){
						vars.add(aviableVar);
						i=aviableVars.size();
						added=true;
					}
				}
				if(!added){
					Var numVar=null;
					boolean found=false, isInt=false,isFloat=false,isDouble=false;
					int Int=-1;
					float Float1=-1;
					double Double1=-1;
					if(!found)try{
						Int=Integer.parseInt(varName.endsWith("I")?varName.substring(0, varName.length()-1):varName);
						found=isInt=true;
					}catch(Exception e){}
					if(!found)try{
						Float1=Float.parseFloat(varName.endsWith("F")?varName.substring(0, varName.length()-1):varName);
						found=isFloat=true;
					}catch(Exception e){}
					if(!found)try{
						Double1=Double.parseDouble(varName.endsWith("D")?varName.substring(0, varName.length()-1):varName);
						found=isDouble=true;
					}catch(Exception e){}
					if(found){
						if(isInt)numVar=new Var("i", 'i', Int);
						else if(isFloat)numVar=new Var("f", 'f', Float1);
						else if(isDouble)numVar=new Var("d", 'd', Double1);
						if(numVar!=null)vars.add(numVar);
					}
				}
			}
		}
		return new DoubleObject<List<Operator>,List<Character>>(vars,functions);
	}
	
	//sorry for FPS references but MGL and Illuminati told me to do it
	/**
	 * @param source
	 * @return 1. Integer indicates the start of the string, 2. Integer indicates the end of the string String indicates the content of the string
	 */
	private static List<OhBabyATriple<Integer,Integer,String>> detectAndExtractString(String source){
		List<OhBabyATriple<Integer,Integer,String>> quadFeed=new ArrayList<OhBabyATriple<Integer,Integer,String>>();
		
		char[] chars=source.toCharArray();
		boolean isInBracket=false;
		int start=-1,count=0;
		for(int i=0;i<chars.length;i++){
			if(chars[i]=='"'){
				isInBracket=!isInBracket;
				if(isInBracket){
					start=i;
					count++;
				}
				else{
					quadFeed.add(new OhBabyATriple<Integer,Integer,String>(start, i+1, source.substring(start, i+1)));
					count++;
				}
			}
		}
		if(count%2!=0)throw new IllegalArgumentException("source has a wrong amount of \"!");
		return quadFeed;
	}
}
