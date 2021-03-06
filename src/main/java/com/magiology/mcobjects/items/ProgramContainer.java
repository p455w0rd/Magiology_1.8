package com.magiology.mcobjects.items;

import static com.magiology.util.utilobjects.NBTUtil.*;

import java.util.List;

import com.magiology.api.lang.JSProgramContainer;
import com.magiology.api.lang.program.ProgramDataBase;
import com.magiology.api.lang.program.ProgramSerializable;
import com.magiology.api.lang.program.ProgramUsable;
import com.magiology.core.init.MCreativeTabs;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
	
	public void initID(ItemStack stack){
		if(U.isRemote())return;
		if(getId(stack)<1)setId(stack, ProgramDataBase.code_aviableId());
	}
	
	
	public int getId(ItemStack stack){
		return getInt(stack, "id");
	}
	public void setId(ItemStack stack, int content){
		setInt(stack, "id", content);
	}
	public ProgramUsable getProgram(ItemStack stack){
		initID(stack);
		return ProgramDataBase.getProgram(getId(stack));
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced){
		ProgramUsable program=getProgram(stack);
		if(program!=null){
			ProgramSerializable data=program.getSaveableData();
			if(data!=null)
			tooltip.add("Name: "+(data.programName==null||data.programName.toString().isEmpty()?"<empty>":data.programName));
		}
	}
}
