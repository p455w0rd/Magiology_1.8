package com.magiology.mcobjects.items;

import static com.magiology.util.utilobjects.NBTUtil.*;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

import com.magiology.api.lang.ProgramHandeler;
import com.magiology.api.lang.ProgramHolder;
import com.magiology.core.init.MGui;
import com.magiology.handlers.GuiHandlerM;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

public class ProgramContainer extends Item{
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		createNBT(stack);
		initId(stack);
		if(player.isSneaking())GuiHandlerM.openGui(player, MGui.CommandContainerEditor, (int)player.posX, (int)player.posY, (int)player.posZ);
		else{
			try{
				getProgram(stack).run(new Object[]{},new Object[]{world,pos});
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return true;
    }
	
	public static void setCommandName(ItemStack stack, String name){
		if(U.isRemote())return;
		initId(stack);
		setTag(stack, "name", name);
	}
	public static void initId(ItemStack stack){
		if(U.isRemote())return;
		if(getId(stack)<1)setId(stack, ProgramHolder.code_aviableId());
	}

	public static String getName(ItemStack stack){
		createNBT(stack);
		initId(stack);
		String name=getTag(stack, "name");
		return name==null?"error-noName":name;
	}
	
	public static BlockPosM getPos(ItemStack stack){
		try{
			initId(stack);
			String[] pos=getTag(stack,"pos").split(",");
			return new BlockPosM(Integer.parseInt(pos[0].replaceAll(" ", "")),Integer.parseInt(pos[1].replaceAll(" ", "")),Integer.parseInt(pos[2].replaceAll(" ", "")));
		}catch(Exception e){
			return null;
		}
	}
	public static void setPos(ItemStack stack, Vec3i pos){
		if(U.isRemote())return;
		initId(stack);
		setTag(stack, "pos", pos.getX()+", "+pos.getY()+", "+pos.getZ());
	}
	
	public static String getTag(ItemStack stack, String tag){
		initId(stack);
		return getString(stack, tag);
	}
	public static void setTag(ItemStack stack, String tag, String content){
		initId(stack);
		setString(stack, tag, content);
	}
	
	public static int getId(ItemStack stack){
		return getInt(stack, "id");
	}
	private static void setId(ItemStack stack, int content){
		setInt(stack, "id", content);
	}
	public static Program getProgram(ItemStack stack){
		initId(stack);
		return new Program(getName(stack), getId(stack), getPos(stack));
	}
	public static void run(World world,ItemStack stack,Object...args){
		initId(stack);
		try{
			getProgram(stack).run(args,new Object[]{world});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced){
		Program program=getProgram(stack);
		try{
			tooltip.add("Name: "+(program.name==null||program.name.isEmpty()?"<empty>":program.name));
			tooltip.add("x: "+program.pos.getX()+" y: "+program.pos.getY()+" z: "+program.pos.getZ());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
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
		public Program run(Object[] args, Object[] environment){
			result=ProgramHolder.run(programId, args,ProgramHandeler.defultVars(environment)).toString();
			return this;
		}
	}
}
