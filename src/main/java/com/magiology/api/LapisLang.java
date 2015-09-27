package com.magiology.api;

import com.magiology.util.utilclasses.Util;

public class LapisLang{
	public static String getName(String program){
		try{
			String[] lines=program.split("\n");
			for(String line:lines){
				if(!line.startsWith("\\")){
					if(line.trim().length()>0){
						if(line.trim().startsWith("#name")){
							String newLine=line.substring(0, line.indexOf('"'));
							String[] words=newLine.split(" ");
							if(words[0].equals("#name")&&words[1].equals("->")){
								return line.substring(line.indexOf('"')+1, line.lastIndexOf('"'));
							}
							return null;
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static LapisProgram compile(String program){
		return null;
	}
	public static class LapisProgram{
		
	}
}
