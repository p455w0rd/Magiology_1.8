package com.magiology.mcobjects.items;

import static com.magiology.util.utilobjects.NBTUtil.*;

import java.util.List;

import com.magiology.api.lang.JSProgramContainer;
import com.magiology.api.lang.program.ProgramDataBase;
import com.magiology.core.init.MCreativeTabs;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkProgramHolder;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class ProgramContainer extends Item implements JSProgramContainer{
	
	public ProgramContainer(){
		setUnlocalizedName("CommandContainer").setCreativeTab(MCreativeTabs.Whwmmt_core).setMaxStackSize(1);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		createNBT(stack);
		initID(stack);
		return false;
	}
	
	public void setName(ItemStack stack, String name){
		initID(stack);
		ProgramDataBase.getProgram(getId(stack)).getSaveableData().programName=name;
	}
	public void initID(ItemStack stack){
		if(U.isRemote())return;
		if(getId(stack)<1)setId(stack, ProgramDataBase.code_aviableId());
	}
	
	public String getName(ItemStack stack){
		createNBT(stack);
		initID(stack);
		try{
			return ProgramDataBase.getProgram(getId(stack)).getSaveableData().programName+"";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "error-noName";
	}
	
	public BlockPosM getPos(ItemStack stack){
		try{
			initID(stack);
			String[] pos=getTag(stack,"pos").split(",");
			return new BlockPosM(Integer.parseInt(pos[0].replaceAll(" ", "")),Integer.parseInt(pos[1].replaceAll(" ", "")),Integer.parseInt(pos[2].replaceAll(" ", "")));
		}catch(Exception e){
			return null;
		}
	}
	public void setPos(ItemStack stack, Vec3i pos){
		if(U.isRemote())return;
		initID(stack);
		setTag(stack, "pos", pos.getX()+", "+pos.getY()+", "+pos.getZ());
	}
	
	public String getTag(ItemStack stack, String tag){
		initID(stack);
		return getString(stack, tag);
	}
	public void setTag(ItemStack stack, String tag, String content){
		initID(stack);
		setString(stack, tag, content);
	}
	
	public int getId(ItemStack stack){
		return getInt(stack, "id");
	}
	public void setId(ItemStack stack, int content){
		setInt(stack, "id", content);
	}
	public Program getProgram(ItemStack stack){
		initID(stack);
		return new Program(getName(stack), getId(stack), getPos(stack));
	}
	public void run(TileEntityNetworkProgramHolder holder,World world,ItemStack stack,Object...args){
		initID(stack);
		try{
			getProgram(stack).run(holder,args,new Object[]{world});
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
}
