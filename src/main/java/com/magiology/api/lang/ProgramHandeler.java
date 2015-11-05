package com.magiology.api.lang;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.magiology.api.lang.bridge.WorldWrapper;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.ObjectHolder;
import com.magiology.util.utilobjects.m_extension.BlockPosM;


public class ProgramHandeler{
	
	public static Object[] compileArgs(String source, ObjectHolder<Integer>... errorPos){
		if(errorPos.length==1)errorPos[0].setVar(-1);
		else if(errorPos.length!=0)throw new IllegalStateException("You can't use more than 1 ObjectHolder's!");
		if(source==null||source.isEmpty())return new Object[0];
		List<Object> result=new ArrayList<Object>();
		
		String newline=System.getProperty("line.separator");
		
		source=source.replaceAll(newline, "");
		while(source.contains("  "))source=source.replaceAll("  ", " ");
		source=removeSpacesFrom(source,'{','}','(',')','=',';','*','/','+','-',newline.charAt(0),'%','!','>','<','@','#');
		
		String[] args=source.split(";");
		
		for(int i=0;i<args.length;i++)try{
			String arg=args[i];
			String[] words=arg.split(" ");
			if(words.length==2){
				String type=words[0],value=words[1];
				if(words[0].equals("boolean")){
					if(U.isBoolean(value))result.add(Boolean.parseBoolean(value));
				}
				else if(type.equals("int"))result.add(Integer.parseInt(value));
				else if(type.equals("long"))result.add(Long.parseLong(value));
				else if(type.equals("float"))result.add(Float.parseFloat(value));
				else if(type.equals("double"))result.add(Double.parseDouble(value));
				else if(type.equals("String"))result.add(value);
			}
		}catch(Exception e){
			if(errorPos.length==1)errorPos[0].setVar(i);
		}
		return result.toArray(new Object[result.size()]);
	}
	public static String removeSpacesFrom(String raw,char...cs){
		String result=new String(raw);
		for(char c:cs){
			while(result.contains(c+" "))result=result.replace(c+" ", c+"");
			while(result.contains(" "+c))result=result.replace(" "+c, c+"");
		}
		return result;
	}
	private static String worldJSVar=loadWorld(),blockPosJSvar=loadPos();
	public static CharSequence defultVars(Object[] environment){
		StringBuilder result=new StringBuilder();
		IBlockAccess world=null;
		Vec3i runPos=null;
		for(Object o:environment){
			if(o instanceof IBlockAccess)world=(IBlockAccess)o;
			if(o instanceof Vec3i)runPos=(Vec3i)o;
		}
		if(world!=null||runPos!=null)result.append(new DefaultJavaScriptLoader(environment).result);
		return result;
	}
	private static class DefaultJavaScriptLoader{
		public String result;
		private DefaultJavaScriptLoader(Object[] environment){
			StringBuilder res=new StringBuilder();
			for(Object o:environment){
				if(o instanceof World){
					res.append(worldJSVar);
					WorldWrapper.setBlockAccess((World)o);
				}
				if(o instanceof Vec3i){
					res.append(loadPos((Vec3i)o));
				}
			}
			result=res.toString();
		}
	}
	private static String loadWorld(){try{
		return 
			"World=Java.type('"+WorldWrapper.class.getName()+"');\n"+
			"BlockPos=Java.type('"+BlockPosM.class.getName()+"');\n"+
			"EnumFacing=Java.type('"+EnumFacing.class.getName()+"');\n";
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}

	private static String loadPos(Vec3i...vec3i){
		if(vec3i.length==0)
		return "BlockPos=Java.type('"+BlockPosM.class.getName()+"');\n"
			 + "runPos=new BlockPos(";
		else return blockPosJSvar+vec3i[0].getX()+","+vec3i[0].getY()+","+vec3i[0].getZ()+");";
	}
}
