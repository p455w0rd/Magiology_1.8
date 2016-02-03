package com.magiology.util.utilobjects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class NBTUtil{
	public static boolean hasKey(ItemStack stack, String key){
		return hasNBT(stack)&&getNBT(stack).hasKey(key);
	}
	public static void removeTag(ItemStack stack, String key){
		if(hasNBT(stack))getNBT(stack).removeTag(key);
	}
	public static void setLong(ItemStack stack, String key, long keyValue){
		createNBT(stack);
		getNBT(stack).setLong(key, keyValue);
	}
	public static String getString(ItemStack stack, String key){
		return getNBT(stack).getString(key);
	}
	public static void setString(ItemStack stack, String key, String keyValue){
		createNBT(stack);
		getNBT(stack).setString(key, keyValue);
	}
	public static boolean getBoolean(ItemStack stack, String key){
		return getNBT(stack).getBoolean(key);
	}
	public static void setBoolean(ItemStack stack, String key, boolean keyValue){
		createNBT(stack);
		getNBT(stack).setBoolean(key, keyValue);
	}
	public static byte getByte(ItemStack stack, String key){
		return getNBT(stack).getByte(key);
	}
	public static void setByte(ItemStack stack, String key, byte keyValue){
		createNBT(stack);
		getNBT(stack).setByte(key, keyValue);
	}
	public static short getShort(ItemStack stack, String key){
		return getNBT(stack).getShort(key);
	}
	public static void setShort(ItemStack stack, String key, short keyValue){
		createNBT(stack);
		getNBT(stack).setShort(key, keyValue);
	}
	public static int getInt(ItemStack stack, String key){
		return getNBT(stack).getInteger(key);
	}
	public static void setInt(ItemStack stack, String key, int keyValue){
		createNBT(stack);
		getNBT(stack).setInteger(key, keyValue);
	}
	public static long getLong(ItemStack stack, String key){
		return getNBT(stack).getLong(key);
	}
	public static float getFloat(ItemStack stack, String key){
		return getNBT(stack).getFloat(key);
	}
	public static void setFloat(ItemStack stack, String key, float keyValue){
		createNBT(stack);
		getNBT(stack).setFloat(key, keyValue);
	}
	public static double getDouble(ItemStack stack, String key){
		return getNBT(stack).getDouble(key);
	}
	public static void setDouble(ItemStack stack, String key, double keyValue){
		createNBT(stack);
		getNBT(stack).setDouble(key, keyValue);
	}
	
	public static NBTTagCompound createNBT(ItemStack a){
		if(!a.hasTagCompound())a.setTagCompound(new NBTTagCompound());
		return getNBT(a);
	}
	public static boolean hasNBT(ItemStack stack){
		return stack!=null&&getNBT(stack)!=null;
	}
	public static NBTTagCompound getNBT(ItemStack stack){
		return stack.getTagCompound();
	}
	@Deprecated
	public void writeNbtToPacketBuffer(NBTTagCompound nbt, PacketBuffer buff){
//		Set<String> keys=nbt.getKeySet();
//		for(String key:keys){
//			if(nbt.getInteger(key)){
//				ByteBufUtils.writeUTF8String(buff, key);
//			}
//			
//		}
	}
	@Deprecated
	public void readNbtFromPacketBuffer(NBTTagCompound nbt, PacketBuffer buff){
		
		
		
	}
}
