package com.magiology.api.lapislang;

import java.util.List;

import com.magiology.api.Function;


public class LapisLangCompiler{
	
	public static LapisProgram compile(String program){
		String newline=System.getProperty("line.separator");
		program=program.replaceAll(newline, "");
		while(program.contains("  "))program=program.replace("  ", " ");
		program=LapisLangCompiler.removeSpacesFrom(program,'{','}','(',')','=',';','*','/','+','-',newline.charAt(0),'%','!','>','<','@','#');
		
		String name="";
		LapisProgram lp=new LapisProgram();
		try{
			String[] lines=program.split(";");
			for(String line:lines){
				line=line.trim();
				if(!line.startsWith("\\")){
					if(line.contains("\\"))line=line.substring(0, line.indexOf("\\"));
					if(line.length()>0){
						{//name
							if(line.startsWith("#name")){
								String newLine=line.substring(0, line.indexOf('"'));
								String[] words=newLine.split("->");
								if(words[0].equals("#name")){
									name=line.substring(line.indexOf('"')+1, line.lastIndexOf('"'));
								}
							}
						}
					}
				}
			}
			
			String[] in=LapisLangCompiler.getMapInBrackets(program,"in");
			if(in!=null)LapisLangCompiler.stringMapToVars(in,lp.in);
			
			String[] vars=LapisLangCompiler.getMapInBrackets(program,"vars");
			if(vars!=null)LapisLangCompiler.stringMapToVars(vars,lp.vars);
			
			String main=LapisLangCompiler.getInBrackets(program,"out String main()");
			if(main!=null){
				if(!main.contains("return "))return null;
				lp.func.add(new Function(lp,main));
			}else return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		lp.name=name;
		return lp;
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

	public static void stringMapToVars(String[] map, List<Variable> listSaver){
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
				if(type!='q')listSaver.add(new Variable(temp[1], type, 0));
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
}
