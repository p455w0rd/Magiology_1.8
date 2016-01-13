package com.magiology.util.utilclasses;

public class PrintUtil{
	
	public static void println(){
		LogUtil.info("");
	}
	public static void println(Object obj){
		LogUtil.info(toString(obj));
	}
	public static void println(Object... objs){
		LogUtil.info(toString(objs));
	}
	
	public static void printlnEr(){
		LogUtil.error("");
	}
	public static void printlnEr(Object obj){
		LogUtil.error(toString(obj));
	}
	public static void printlnEr(Object... objs){
		LogUtil.error(toString(objs));
	}
	
	public static void printlnInf(){
		LogUtil.info("");
	}
	public static void printlnInf(Object obj){
		LogUtil.info(toString(obj));
	}
	public static void printlnInf(Object... objs){
		LogUtil.info(toString(objs));
	}
	
	public static<T> T printlnAndReturn(T obj){
		println(obj);
		return obj;
	}

	public static void printStackTrace(){
		println(UtilM.getStackTrace());
	}

	public static void printIsRemote(Object worldContainer){
		println(UtilM.isRemote(worldContainer));
	}
	
	
	
	
	private static String toString(Object obj){
		StringBuilder print=new StringBuilder();
		
		if(obj!=null){
			if(isArray(obj))print.append(arrayToString(obj));
			else print.append(obj);
		}else print.append("null");
		
		return print.toString();
	}
	
	private static String toString(Object... objs){
		StringBuilder print=new StringBuilder();
		
		if(objs!=null)for(int i=0;i<objs.length;i++){
			Object a=objs[i];
			if(isArray(a))print.append(arrayToString(a));
			else print.append(a+(i==objs.length-1?"":" "));
		}else print.append("null");
		
		return print.toString();
	}
	
	private static StringBuilder arrayToString(Object array){
		StringBuilder print=new StringBuilder();
		
		print.append("[");
		
		if(array instanceof boolean[]){
			boolean[] b=(boolean[])array;
			for(int i=0;i<b.length;i++){
				boolean c=b[i];
				if(isArray(c))print.append(arrayToString(c));
				else print.append(c+(i==b.length-1?"":" "));
			}
		}
		else if(array instanceof float[]){
			float[] b=(float[])array;
			for(int i=0;i<b.length;i++){
				float c=b[i];
				if(isArray(c))print.append(arrayToString(c));
				else print.append(c+(i==b.length-1?"":" "));
			}
		}
		else if(array instanceof int[]){
			int[] b=(int[])array;
			for(int i=0;i<b.length;i++){
				int c=b[i];
				if(isArray(c))print.append(arrayToString(c));
				else print.append(c+(i==b.length-1?"":" "));
			}
		}
		else if(array instanceof double[]){
			double[] b=(double[])array;
			for(int i=0;i<b.length;i++){
				double c=b[i];
				if(isArray(c))print.append(arrayToString(c));
				else print.append(c+(i==b.length-1?"":" "));
			}
		}
		else if(array instanceof Object[]){
			Object[] b=(Object[])array;
			for(int i=0;i<b.length;i++){
				Object c=b[i];
				if(isArray(c))print.append(arrayToString(c));
				else print.append(c+(i==b.length-1?"":" "));
			}
		}else throw new IllegalStateException("Given object is not an array!");
		
		print.append("]");
		
		return print;
	}
	private static boolean isArray(Object obj){
		return obj!=null&&obj.getClass().isArray();
	}
}
