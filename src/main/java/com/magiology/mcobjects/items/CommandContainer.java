package com.magiology.mcobjects.items;

import static com.magiology.util.utilobjects.NBTUtil.*;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

import com.magiology.api.network.Command;
import com.magiology.core.init.MGui;
import com.magiology.handelers.GuiHandelerM;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

public class CommandContainer extends Item{
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		createNBT(stack);
		GuiHandelerM.openGui(player, MGui.CommandContainerEditor, (int)player.posX, (int)player.posY, (int)player.posZ);
		return stack;
	}
	
	public static void setCode(ItemStack stack, String command){
		setString(stack, "com", command);
	}
	public static String getCode(ItemStack stack){
		createNBT(stack);
		return getString(stack, "com");
	}
	
	public static void setCommandName(ItemStack stack, String name){
		setString(stack, "nam", name);
	}
	public static String getName(ItemStack stack){
		createNBT(stack);
		return getString(stack, "nam");
	}
	
	public static BlockPosM getPos(ItemStack stack){
		return new BlockPosM(
			getInt(stack, "x"),
			getInt(stack, "y"), 
			getInt(stack, "z"));
	}
	public static void setPos(ItemStack stack, Vec3i pos){
		setInt(stack, "x", pos.getX());
		setInt(stack, "y", pos.getY());
		setInt(stack, "z", pos.getZ());
	}
	public static Command getCommand(ItemStack stack){
		return new Command(getName(stack), getCode(stack), getPos(stack));
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced){
		Command command=getCommand(stack);
		tooltip.add("Name: "+(command.name.isEmpty()?"<empty>":command.name));
		tooltip.add("Command: "+(command.code.isEmpty()?"<empty>":command.code));
		tooltip.add("x: "+command.pos.getX()+" y: "+command.pos.getY()+" z: "+command.pos.getZ());
	}
}
