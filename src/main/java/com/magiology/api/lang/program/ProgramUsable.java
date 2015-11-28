package com.magiology.api.lang.program;

import java.util.*;

import javax.script.*;

import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.*;

import com.magiology.api.lang.bridge.*;
import com.magiology.io.WorldData.FileContent;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.m_extension.*;

public class ProgramUsable{
	
	private Invocable compiled;
	private List<CharSequence> log=new ArrayList<CharSequence>();
	private Tracker tracker=new Tracker();
	private ProgramSerializable saveableData;
	
	public static final String jsJavaBridge;
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
	public ProgramSerializable getSaveableData(){
		if(saveableData==null){
			int id=UtilM.getMapKey(ProgramDataBase.useablePrograms, this);
			FileContent i=ProgramDataBase.programs.getFileContent(id+"");
			saveableData=i==null?new ProgramSerializable("", ""):(ProgramSerializable)i.content;
		}
		return saveableData;
	}
}
