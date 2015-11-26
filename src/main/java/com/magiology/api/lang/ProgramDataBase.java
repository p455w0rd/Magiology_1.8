package com.magiology.api.lang;

import static com.magiology.io.WorldData.WorkingProtocol.FROM_SERVER;
import static com.magiology.io.WorldData.WorkingProtocol.SYNC;
import static com.magiology.io.WorldData.WorkingProtocol.SYNC_ON_CHANGE;
import static com.magiology.io.WorldData.WorkingProtocol.SYNC_ON_LOAD;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.magiology.io.WorldData;
import com.magiology.io.WorldData.FileContent;


public class ProgramDataBase{
	
	public static final String err="<ERROR> (DO NOT RETURN SOMETHING THAT STARTS WITH THIS!): ";
	
	private static WorldData<String,StringBuilder> programs=new WorldData<String,StringBuilder>("jsPrograms","js","jsProg",SYNC,FROM_SERVER,SYNC_ON_LOAD,SYNC_ON_CHANGE);
	private static Map<Integer, ProgramBase> programData=new HashMap<Integer, ProgramBase>();
	
	public static void loadClass(){programs.getFileContent("");}
	
	//TODO: run-------------------------------------------------------------------------------------------
	public static Object run(int programId,Object[] args, Object[] environment){
		Object result=null;
		try{
			ProgramBase program=getProgram(programId);
    		result=program.run("main", args, environment);
		}catch(Exception e){
			result=err+e.getMessage();
		}
		if(result!=null)log(programId, result+"");
		return result;
	}
	
	private static ProgramBase getProgram(int programId){
		ProgramBase result=programData.get(programId);
		if(result==null){
			result=new ProgramBase();
			result.init(ProgramDataBase.code_get(programId));
			programData.put(programId, result);
		}
		return result;
	}

	//TODO: code------------------------------------------------------------------------------------------
	public static void code_set(int programId,CharSequence program){
		if(program==null||program.length()<0)return;
		programs.addFile(programId+"", new StringBuilder(program));
		getProgram(programId).init(ProgramDataBase.code_get(programId));
	}
	
	public static String code_remove(int programId){
		programData.remove(programId);
		return programs.removeFile(programId+"").toString();
	}
	
	public static String code_get(int programId){
		FileContent<StringBuilder> code=programs.getFileContent(programId+"");
		String s=String.valueOf(code.text);
		return code!=null?s:"";
	}
	
	public static int code_aviableId(){
		int result=1;
		for(Entry<String,FileContent<StringBuilder>> i:programs.entrySet()){
			if(i.getKey().equals(result+""))result=Integer.parseInt(i.getKey().toString())+1;
		}
		return result;
	}
	
	public static void log(int programId, String string){
		if(string.isEmpty())return;
		getProgram(programId).log(string);
	}
	public static void log_clear(int programId){
		getProgram(programId).setLog(new ArrayList<CharSequence>());
	}
	public static List<CharSequence> log_get(int programId){
		List<CharSequence> result=getProgram(programId).getLog();
		if(result==null)result=new ArrayList<CharSequence>();
		if(result.isEmpty())result.add(new StringBuilder());
		return result;
	}
	
}
