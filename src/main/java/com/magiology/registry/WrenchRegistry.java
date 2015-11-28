package com.magiology.registry;

import java.util.*;

import net.minecraft.item.*;

public class WrenchRegistry{
	private static List<Item> registered=new ArrayList<Item>();
	
	public static void registerWrench(Item item){
		if(isWrench(item))return;
		registered.add(item);
	}
	public static boolean isWrench(ItemStack stack){
		return stack!=null?registered.contains(stack.getItem()):false;
	}
	public static boolean isWrench(Item item){
		return item!=null?registered.contains(item):false;
	}
}
