package com.magiology.api.lang;

import com.magiology.api.lang.bridge.NetworkProgramHolderWrapper;
import com.magiology.api.lang.program.ProgramDataBase;
import com.magiology.client.render.tilerender.network.RenderNetworkInterface;
import com.magiology.client.render.tilerender.network.RenderNetworkRouter;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkProgramHolder;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public interface JSProgramContainer{
	
	public void initID(ItemStack stack);
	
	public void setName(ItemStack stack, String name);
	public String getName(ItemStack stack);
	
	public BlockPosM getPos(ItemStack stack);
	public void setPos(ItemStack stack, Vec3i pos);
	
	public String getTag(ItemStack stack, String tag);
	public void setTag(ItemStack stack, String tag, String content);
	
	public int getId(ItemStack stack);
	public void setId(ItemStack stack, int content);
	
	public Program getProgram(ItemStack stack);
	
	public void run(TileEntityNetworkProgramHolder holder,World world,ItemStack stack,Object...args);
	
	
	
	public static class Program{
		
		public String name,result,argsSrc;
		public BlockPosM pos;
		public int programId;
		
		public Program(String name, int programId, Vec3i pos){
			this.name=name;
			this.programId=programId;
			this.pos=new BlockPosM(pos);
		}
		@Override
		public String toString(){
			return "Program{name: "+name+", result: "+result+", pos: "+pos+", program id: "+programId+"}";
		}
		public Object run(TileEntityNetworkProgramHolder holder, Object[] args, Object[] environment){
			NetworkProgramHolderWrapper.setInstance(holder);
			Object x=ProgramDataBase.run(programId, args, environment);
			result=x+"";
			return x;
		}
	}
}
