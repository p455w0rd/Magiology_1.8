package com.magiology.objhelper;


public class NumberFormater{
	
	public static String formatNumber(int num){
		return NumberFormater.convert_To(NumberFormater.add_ToNumber(num), ',');
	}
	public static String formatNumber(double num){
		return NumberFormater.convert_To(NumberFormater.add_ToNumber(num), ',');
	}
	
	public static String convert_To(String num,char c){
		return num.replace('_', c);
	}
	
	public static String add_ToNumber(int num){
		String s=add_ToNumber(num+0.0);
		return s;
	}
	public static String add_ToNumber(double num){
		try{
			String numS="";
			char[] numC=(""+(long)num).toCharArray();
			int splitter=numC.length%3;
			for(int i=numC.length-1;i>=0;i--){
				numS=((numC.length-1)-i!=0&&i%3==splitter?"_":"")+numC[i]+numS;
			}
			return numS+(num+"").substring(numC.length, (num+"").length());
		}catch(Exception e){
			e.printStackTrace();
			return ""+num;
		}
		
	}
	
}
