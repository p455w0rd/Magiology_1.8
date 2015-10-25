package com.magiology.api.lang;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.minecraft.world.World;

import com.magiology.core.MReference;
import com.magiology.util.utilclasses.FileUtil;
import com.magiology.util.utilclasses.Util;


public class ProgramHolder{
	
	private static Map<Integer,StringBuilder> programs=new HashMap<Integer,StringBuilder>();
	public static final String err="<ERROR> (DO NOT RETURN SOMETHING THAT STARTS WITH THIS!): ";
		
	public static void registerProgram(int programId,CharSequence program){
		if(program==null||program.length()<0)return;
		programs.put(programId, new StringBuilder(program));
	}
	
	public static String removeProgram(int programId){
		
		return programs.remove(programId).toString();
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
		for(Entry<Integer,StringBuilder> i:programs.entrySet()){
			if(i.getKey()==result)result=i.getKey()+1;
		}
		return result;
	}
	public static String getCode(int programId){
		StringBuilder code=programs.get(programId);
		return code!=null?code.toString():"";
	}
	public static void save(World world){
		if(Util.isRemote())return;
		
		String worldFile=world.getSaveHandler().getWorldDirectory().getName();
		File mainPath=new File("saves/"+MReference.NAME+"/"+worldFile+"/jsPrograms");
		mainPath.mkdirs();
		
		File[] listOfFiles=mainPath.listFiles();
		for(File program:listOfFiles)if(program.isFile())program.delete();
		for(Entry<Integer,StringBuilder> program:programs.entrySet()){
			File prFile=new File(mainPath+"/"+program.getKey()+".js");
			FileUtil.writeToWholeFile(prFile, program.getValue().toString());
		}
	}
	public static void load(World world){
		if(Util.isRemote())return;

		String worldFile=world.getSaveHandler().getWorldDirectory().getPath();
		File mainPath=new File("saves/"+MReference.NAME+"/"+worldFile+"/jsPrograms");
		mainPath.mkdirs();
		
		File[] listOfFiles=mainPath.listFiles();
		for(File program:listOfFiles)try{
			if(program.isFile())programs.put(Integer.parseInt(program.getName().substring(0, program.getName().lastIndexOf('.'))), FileUtil.readWholeFile(program));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
