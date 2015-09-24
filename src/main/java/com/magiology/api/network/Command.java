package com.magiology.api.network;

import net.minecraft.util.Vec3i;

import com.magiology.util.utilobjects.m_extension.BlockPosM;

public class Command{
	
	public final String name,code;
	public final BlockPosM pos;
	
	public Command(String name, String code, Vec3i pos){
		this.name=name;
		this.code=code;
		this.pos=new BlockPosM(pos);
	}
	@Override
	public String toString(){
		return "Command{name: "+name+", code: "+code+", pos: "+pos+"}";
	}
}
