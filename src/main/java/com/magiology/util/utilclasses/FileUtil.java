package com.magiology.util.utilclasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;


public class FileUtil{

	public static StringBuilder readWholeFile(File file){
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
	
	public static void writeToWholeFile(File file,String content){
		try{
			file.createNewFile();
			PrintWriter out=new PrintWriter(file);
			out.print(content);
			out.flush();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
