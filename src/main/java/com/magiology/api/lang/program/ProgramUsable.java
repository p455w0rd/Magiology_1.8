package com.magiology.api.lang.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.magiology.api.lang.bridge.NetworkProgramHolderWrapper;
import com.magiology.api.lang.bridge.WorldWrapper;
import com.magiology.io.WorldData.FileContent;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.Tracker;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ProgramUsable{
	
	private Invocable compiled;
	private List<CharSequence> log=new ArrayList<CharSequence>();
	private ProgramSerializable saveableData;
	
	public static final String jsJavaBridge=new Object(){public String toString(){
		StringBuilder txt=new StringBuilder();
		//functions
		add(txt,"function run_funct(pos, name, args){");
		add(txt,"return Java.type('"+NetworkProgramHolderWrapper.class.getName()+"').run(pos, name, args);");
		add(txt,"}");
		
		add(txt,"function setRunPos(pos){");
		add(txt,"runPos=pos;");
		add(txt,"}");
		
		//classes
		add(txt,"World=Java.type('"+WorldWrapper.class.getName()+"');");
		add(txt,"BlockPos=Java.type('"+BlockPosM.class.getName()+"');");
		add(txt,"EnumFacing=Java.type('"+EnumFacing.class.getName()+"');");
		//vars
		add(txt,"runPos=\"undefined\";");
		
//		add(txt,"");
		
		return txt.toString();
	}}.toString();
	public static final int jsJavaBridgeLines=jsJavaBridge.split("\n").length;
	
	
	private static void add(StringBuilder txt, String line){
		txt.append(line).append("\n");
	}
	
	public Object run(String mainFuncName, Object[] args, Object[] environment){
		Object result=run(compiled, getLog(), mainFuncName, args, environment);
		return result;
	}
	public static Object run(Invocable program, List<CharSequence> log, String mainFuncName, Object[] args, Object[] environment){
		try{
			Map<String, Object> map=new HashMap<String, Object>();
			boolean hasRunPos=false,hasWorld=false;
			int posId=0;
			for(Object o:environment){
				if(o instanceof World){
					WorldWrapper.setBlockAccess((World)o);
					hasWorld=true;
				}else{
					if(o instanceof Vec3i){
						if(hasRunPos)map.put("blockPos-"+(posId++), o);
						else{
							map.put("runPos", o);
							hasRunPos=true;
							try{
								program.invokeFunction("setRunPos", o);
							}catch(Exception e){
								log(ProgramDataBase.err+e.getMessage(),log);
							}
						}
					}
				}
			}
			if(!hasWorld)throw new IllegalStateException("There is no world instance! This is a bug!");
			try{
				program.invokeFunction("init", map);
			}catch(Exception e){
				log(ProgramDataBase.err+e.getMessage(),log);
			}
			
			return program.invokeFunction(mainFuncName, args);
		}catch(Exception e){
			return ProgramDataBase.err+e.getMessage();
		}
	}
	
	public static Invocable compile(CharSequence src) throws ScriptException{
		ScriptEngine engine=new ScriptEngineManager(null).getEngineByName("nashorn");
		engine.eval(jsJavaBridge+src.toString());
		return (Invocable)engine;
		
	}
	public void init(CharSequence src){
		try{
			compiled=compile(src);
		}catch(ScriptException e){
		}
	}

	public void log(String newLog){
		log(newLog, getLog());
	}
	public static void log(String newLog, List<CharSequence> log){
		String side=FMLCommonHandler.instance().getEffectiveSide().toString();
		String[] Log=newLog.split("\n");
		for(String line:Log){
			log.add("["+side+"]: "+line);
		}
		while(log.size()>100){
			log.remove(log.size()-1);
		}
	}
	
	
	
	public List<CharSequence> getLog(){if(log==null)log=new ArrayList<CharSequence>();return log;}
	public void setLog(List<CharSequence> log){this.log=log;}
	
	public Invocable getCompiled(){return compiled;}
	
	public ProgramSerializable getSaveableData(){
		if(saveableData==null){
			int id=UtilM.getMapKey(ProgramDataBase.useablePrograms, this);
			FileContent i=ProgramDataBase.programs.getFileContent(id+"");
			saveableData=i==null?new ProgramSerializable("", ""):(ProgramSerializable)i.content;
		}
		return saveableData;
	}
}
