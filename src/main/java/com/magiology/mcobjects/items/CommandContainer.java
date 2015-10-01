package com.magiology.mcobjects.items;

import static com.magiology.util.utilobjects.NBTUtil.createNBT;
import static com.magiology.util.utilobjects.NBTUtil.getInt;
import static com.magiology.util.utilobjects.NBTUtil.getString;
import static com.magiology.util.utilobjects.NBTUtil.setInt;
import static com.magiology.util.utilobjects.NBTUtil.setString;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

import com.magiology.api.lapislang.LapisLangCompiler;
import com.magiology.api.lapislang.LapisProgram;
import com.magiology.api.network.Command;
import com.magiology.core.init.MGui;
import com.magiology.handlers.GuiHandlerM;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

public class CommandContainer extends Item{
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		createNBT(stack);
		String newline=System.getProperty("line.separator");
//		setCode(stack, 
//			"#name -> "+'"'+"redstone to percent"+'"'+";"+
//			newline+""+
//			newline+"in{"+
//			newline+"    boolean isStrong;"+
//			newline+"    float strength;"+
//			newline+"}"+
//			newline+""+
//			newline+"vars{"+
//			newline+"   float result; "+
//			newline+"}"+
//			newline+""+
//			newline+"out String main(){"+
//			newline+"    result=strength/15;"+
//			newline+"    return result;"+
//			newline+"}");
		if(player.isSneaking())GuiHandlerM.openGui(player, MGui.CommandContainerEditor, (int)player.posX, (int)player.posY, (int)player.posZ);
		else{
			try{
				LapisProgram lp=LapisLangCompiler.compile(getCode(stack));
				if(lp!=null){
					U.printInln(lp.run(U.RB(),U.RInt(15)+0F));
				}
				else Util.printInln("null");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return stack;
	}
	
	public static void setCode(ItemStack stack, String command){
		setString(stack, "com", command);
	}
	public static String getCode(ItemStack stack){
		createNBT(stack);
		return getString(stack, "com");
	}
	
	private static void setCommandName(ItemStack stack, String name){
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
	public static Command run(ItemStack stack){
		return new Command(getName(stack), getCode(stack), getPos(stack)).run();
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced){
		Command command=run(stack);
		tooltip.add("Name: "+(command.name.isEmpty()?"<empty>":command.name));
		if(GuiScreen.isShiftKeyDown()){
			if(command.code.replace('\n', ' ').replaceAll(" ", "").isEmpty())tooltip.add("Command: "+"<empty>");
			else{
				for(String s:command.code.split("\n")){
					tooltip.add(s);
				}
			}
		}
		tooltip.add("x: "+command.pos.getX()+" y: "+command.pos.getY()+" z: "+command.pos.getZ());
	}

	public static void copile(ItemStack stack){
		LapisProgram program=LapisLangCompiler.compile(getCode(stack));
	}
	
/*
#name -> "redstone to precent";

in{
    boolean isStrong;
    int strenght;
}

vars{
   float result; 
}

out String main(){
    result=strenght/15;
    return result;
}
*/
	
}
