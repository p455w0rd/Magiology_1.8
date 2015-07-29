package com.magiology.objhelper.helpers;

import net.minecraft.util.EnumChatFormatting;
/**
 * FontEffectHelper
 * */
public class FontEH{
	public final static int length=22;
	public static EnumChatFormatting AQUA=EnumChatFormatting.AQUA;
	public static EnumChatFormatting BLACK=EnumChatFormatting.BLACK;
	public static EnumChatFormatting BLUE=EnumChatFormatting.BLUE;
	public static EnumChatFormatting BOLD=EnumChatFormatting.BOLD;
	public static EnumChatFormatting DARK_AQUA=EnumChatFormatting.DARK_AQUA;
	public static EnumChatFormatting DARK_BLUE=EnumChatFormatting.DARK_BLUE;
	public static EnumChatFormatting DARK_GRAY=EnumChatFormatting.DARK_GRAY;
	public static EnumChatFormatting DARK_GREEN=EnumChatFormatting.DARK_GREEN;
	public static EnumChatFormatting DARK_PURPLE=EnumChatFormatting.DARK_PURPLE;
	public static EnumChatFormatting DARK_RED=EnumChatFormatting.DARK_RED;
	public static EnumChatFormatting GOLD=EnumChatFormatting.GOLD;
	public static EnumChatFormatting GRAY=EnumChatFormatting.GRAY;
	public static EnumChatFormatting GREEN=EnumChatFormatting.GREEN;
	public static EnumChatFormatting ITALIC=EnumChatFormatting.ITALIC;
	public static EnumChatFormatting LIGHT_PURPLE=EnumChatFormatting.LIGHT_PURPLE;
	public static EnumChatFormatting OBFUSCATED=EnumChatFormatting.OBFUSCATED;
	public static EnumChatFormatting RED=EnumChatFormatting.RED;
	public static EnumChatFormatting RESET=EnumChatFormatting.RESET;
	public static EnumChatFormatting STRIKETHROUGH=EnumChatFormatting.STRIKETHROUGH;
	public static EnumChatFormatting UNDERLINE=EnumChatFormatting.UNDERLINE;
	public static EnumChatFormatting WHITE=EnumChatFormatting.WHITE;
	public static EnumChatFormatting YELLOW=EnumChatFormatting.YELLOW;
	
	public static EnumChatFormatting getEffById(int id){
		switch (id){
		case 0: return AQUA;
		case 1: return BLACK;
		case 2: return BLUE;
		case 3: return BOLD;
		case 4: return DARK_AQUA;
		case 5: return DARK_BLUE;
		case 6: return DARK_GRAY;
		case 7: return DARK_GREEN;
		case 8: return DARK_PURPLE;
		case 9: return DARK_RED;
		case 10:return GOLD;
		case 11:return GRAY;
		case 12:return GREEN;
		case 13:return ITALIC;
		case 14:return LIGHT_PURPLE;
		case 15:return OBFUSCATED;
		case 16:return RED;
		case 17:return RESET;
		case 18:return STRIKETHROUGH;
		case 19:return UNDERLINE;
		case 20:return WHITE;
		case 21:return YELLOW;
		}
		return null;
	}
	public static EnumChatFormatting getRandEff(){
		return getEffById(Helper.RInt(length));
	}
	
	public static EnumChatFormatting getRandColor(){
		int i=0;
		do{i=Helper.RInt(length);}while(i==13||i==15||i==17||i==18||i==19);
		return getEffById(i);
	}
	public static EnumChatFormatting getRandEffect(){
		int i[]={13,15,17,18,19};
		return getEffById(i[Helper.RInt(i.length)]);
	}
	
}
