package com.magiology.util.utilclasses;

import java.io.*;


public class FileUtil{
	
	public static StringBuilder getFileTxt(File file){
		try{
			BufferedReader br=new BufferedReader(new FileReader(file));
			StringBuilder result=new StringBuilder();
			String line=null;
			while((line=br.readLine())!=null)result.append(line).append("\n");
			br.close();
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setFileTxt(File file,String content){
		try{
			file.createNewFile();
			PrintWriter out=new PrintWriter(file);
			out.print(content);
			out.flush();
			out.close();
		}catch(Exception e){
			UtilM.println(file);
			e.printStackTrace();
		}
	}
	public static Object getFileObj(File file){
		try{
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(file));
			Object result=null;
			try{
				result=in.readObject();
			}catch(Exception e){
				e.printStackTrace();
			}
			in.close();
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setFileObj(File file,Serializable content){
		try{
			file.createNewFile();
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(file));
			try{
				out.writeObject(content);
			}catch(Exception e){
				UtilM.println(file);
				e.printStackTrace();
			}
			out.close();
		}catch(Exception e){
			UtilM.println(file);
			e.printStackTrace();
		}
	}
}
