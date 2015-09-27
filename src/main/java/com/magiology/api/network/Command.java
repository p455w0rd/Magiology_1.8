package com.magiology.api.network;

import net.minecraft.util.Vec3i;

import com.magiology.util.utilobjects.m_extension.BlockPosM;

public class Command{
	
	public String name,result,code;
	public final BlockPosM pos;
	
	public Command(String name, String code, Vec3i pos){
		this.name=name;
		this.code=code;
		this.pos=new BlockPosM(pos);
	}
	@Override
	public String toString(){
		return "Command{name: "+name+", code: "+result+", pos: "+pos+"}";
	}
	public static enum KeyWord{
		SET,
		GET,
		TRUE,
		FALSE,
		CAN_HAVE,
		
		REDSTONE,
		SIZE,
		SCALE,
		TEXT,
		POSITION,
		COLOR,
		R,
		G,
		B,
		A,
		NAME;
		
		public boolean match(String string){
			return toString().equals(string.toUpperCase());
		}
		public static KeyWord getByName(String string){
			for(KeyWord i:values())if(i.match(string))return i;
			return null;
		}
	}
	public Command run(Object... in){
		result=code;
		return this;
	}
}
