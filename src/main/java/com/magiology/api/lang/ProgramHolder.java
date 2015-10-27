package com.magiology.api.lang;

import static com.magiology.io.WorldData.WorkingProtocol.*;

import java.util.Map.Entry;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.magiology.io.WorldData;
import com.magiology.io.WorldData.FileContent;


public class ProgramHolder{
	
	public static final String err="<ERROR> (DO NOT RETURN SOMETHING THAT STARTS WITH THIS!): ";
	
	private static WorldData<String,StringBuilder> programs=new WorldData<String,StringBuilder>("jsPrograms","js","jsProg",SYNC,FROM_SERVER,SYNC_ON_LOAD,SYNC_ON_CHANGE);
	
	public static void registerProgram(int programId,CharSequence program){
		if(program==null||program.length()<0)return;
		programs.addFile(programId+"", new StringBuilder(program));
	}
	
	public static String removeProgram(int programId){
		return programs.removeFile(programId+"").toString();
	}
	
	
	public static Object run(int programId,Object[] args, CharSequence defultVars){
		return run(getCode(programId), args, defultVars);
	}
	public static Object run(CharSequence src,Object[] args, CharSequence defultVars){
		if(src==null||src.length()>0)return null;
    	try{
    		ScriptEngine engine=new ScriptEngineManager(null).getEngineByName("nashorn");
    		engine.eval(src.toString());
			return ((Invocable)engine).invokeFunction("main", args);
		}catch(Exception e){
			return err+e.getMessage();
		}
	}
	public static QuickRunProgram getProgram(int programId){
		return new QuickRunProgram(getCode(programId).toString());
	}
	public static int getNexAviableId(){
		int result=1;
		for(Entry<String,FileContent<StringBuilder>> i:programs.entrySet()){
			if(i.getKey().equals(result+""))result=Integer.parseInt(i.getKey().toString())+1;
		}
		return result;
	}
	public static String getCode(int programId){
		FileContent<CharSequence> code=programs.getFileContent(programId+"");
		return code!=null?code.text.toString():"";
	}
}
