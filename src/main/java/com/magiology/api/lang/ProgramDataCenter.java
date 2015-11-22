package com.magiology.api.lang;

import static com.magiology.io.WorldData.WorkingProtocol.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.magiology.io.WorldData;
import com.magiology.io.WorldData.FileContent;


public class ProgramDataCenter{
	
	public static final String err="<ERROR> (DO NOT RETURN SOMETHING THAT STARTS WITH THIS!): ";
	
	private static WorldData<String,StringBuilder> programs=new WorldData<String,StringBuilder>("jsPrograms","js","jsProg",SYNC,FROM_SERVER,SYNC_ON_LOAD,SYNC_ON_CHANGE);
	private static Map<Integer,List<CharSequence>> log=new HashMap<Integer,List<CharSequence>>();
	public static void loadClass(){programs.getFileContent("");}
	
	//TODO: run-------------------------------------------------------------------------------------------
	public static Object run(int programId,Object[] args, CharSequence defultVars){
		Object result=run(code_get(programId), args, defultVars);
		if(result!=null){
			List<CharSequence> Log=new ArrayList<CharSequence>();
			for(String line:(result+"").split("\n"))Log.add(line);
			log(programId, Log);
		}
		return result;
	}
	public static Object run(CharSequence src,Object[] args, CharSequence defultVars){
		if(src==null||src.length()<0)return null;
    	try{
    		ScriptEngine engine=new ScriptEngineManager(null).getEngineByName("nashorn");
    		engine.eval(src.toString()+defultVars.toString());
			return ((Invocable)engine).invokeFunction("main", args);
		}catch(Exception e){
			return err+e.getMessage();
		}
	}
	
	//TODO: code------------------------------------------------------------------------------------------
	public static void code_register(int programId,CharSequence program){
		if(program==null||program.length()<0)return;
		programs.addFile(programId+"", new StringBuilder(program));
	}
	
	public static String code_remove(int programId){
		return programs.removeFile(programId+"").toString();
	}
	
	public static QuickRunProgram code_quick(int programId){
		return new QuickRunProgram(code_get(programId).toString());
	}
	
	public static String code_get(int programId){
		FileContent<CharSequence> code=programs.getFileContent(programId+"");
		return code!=null?code.text.toString():"";
	}
	
	public static int code_aviableId(){
		int result=1;
		for(Entry<String,FileContent<StringBuilder>> i:programs.entrySet()){
			if(i.getKey().equals(result+""))result=Integer.parseInt(i.getKey().toString())+1;
		}
		return result;
	}
	
	public static List<CharSequence> log_get(int programId){
		List<CharSequence> result=log.get(programId);
		if(result==null)result=new ArrayList<CharSequence>();
		return result;
	}
	public static void log(int programId, List<CharSequence> Log){
		if(Log.size()==0)return;
//		if(UtilM.isRemote()?Minecraft.getMinecraft().isIntegratedServerRunning():false)return;
		
		List<CharSequence> existingLog=log.get(programId);
		if(existingLog!=null)existingLog.addAll(Log);
		else log.put(programId, Log);
		if(existingLog!=null)while(existingLog.size()>50)existingLog.remove(existingLog.size()-1);
	}
	public static void log_clear(int programId){
		List<CharSequence> result=log.get(programId);
		if(result!=null)result.clear();
	}
	
}
