package com.magiology.api.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.magiology.api.lang.bridge.NetworkProgramHolderWrapper;
import com.magiology.api.lang.bridge.WorldWrapper;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

public class ProgramBase{
	
	private transient Invocable compiled;
	private transient List<CharSequence> log=new ArrayList<CharSequence>();
	private transient Tracker tracker=new Tracker();
	private String programName,src;
	
	public transient static final String jsJavaBridge;
	static{
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
		
		
		jsJavaBridge=txt.toString();
	}
	private static void add(StringBuilder txt, String line){
		txt.append(line).append("\n");
	}
	
	public Object run(String mainFuncName, Object[] args, Object[] environment){
		tracker.start("run");
		Object result=Run(mainFuncName, args, environment);
		tracker.end("run");
		return result;
	}
	private Object Run(String mainFuncName, Object[] args, Object[] environment){
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
								compiled.invokeFunction("setRunPos", o);
							}catch(Exception e){
								log(ProgramDataBase.err+e.getMessage());
							}
						}
					}
				}
			}
			if(!hasWorld)throw new IllegalStateException("There is no world instance! This is a bug!");
			try{
				compiled.invokeFunction("init", map);
			}catch(Exception e){
				log(ProgramDataBase.err+e.getMessage());
			}
			
			return compiled.invokeFunction(mainFuncName, args);
		}catch(Exception e){
			return ProgramDataBase.err+e.getMessage();
		}
	}
	
	private Invocable compile(CharSequence src){
		try{
			ScriptEngine engine=new ScriptEngineManager(null).getEngineByName("nashorn");
			engine.eval(jsJavaBridge+src.toString());
			return (Invocable)engine;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public void init(CharSequence src){
		tracker.start("comp");
		compiled=compile(src);
		tracker.end("comp");
	}

	public void log(String newLog){
		String side=FMLCommonHandler.instance().getEffectiveSide().toString();
		String[] Log=newLog.split("\n");
		for(String line:Log){
			getLog().add("["+side+"]: "+line);
		}
		while(getLog().size()>100){
			getLog().remove(getLog().size()-1);
		}
	}
	
	
	
	public List<CharSequence> getLog(){if(log==null)log=new ArrayList<CharSequence>();return log;}
	public void setLog(List<CharSequence> log){this.log=log;}
	
	public Invocable getCompiled(){return compiled;}
	
	public Tracker getTracker(){return tracker;}
}
