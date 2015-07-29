package com.magiology.clientdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;

public class FileManager{
	public static boolean writeCreateFile(String fileName,String prefix,String[]fileContent,String...content){return writeCreateFile(new File(fileName+"."+prefix), fileContent, content);}
	public static boolean writeCreateFile(File file1,String[]fileContent,String...content){try{
		PrintWriter pw=new PrintWriter(file1);
		if(content!=null)for(String x:content)if(x!=null)pw.println(x);
		if(fileContent!=null)for(String x:fileContent)if(x!=null)pw.println(x);
		pw.close();
		return true;
	}catch(FileNotFoundException e){e.printStackTrace();return false;}}
	public static String[] readFile(String fileName,String prefix){return readFile(new File(fileName+"."+prefix));}
	public static String[] readFile(File file1){
		if(file1.exists()&&!file1.isDirectory());else return null;
		String[] result=null;
		boolean hasNextLine=true;
		Scanner file=null;
		try{
			file=new Scanner(file1);
		}catch(Exception e){return null;}
		while(hasNextLine){
			String line="";
			try{line=file.nextLine();
			}catch(NoSuchElementException e){hasNextLine=false;}catch(IllegalStateException e){hasNextLine=false;}
			if(result==null)result=new String[]{line};
			else result=ArrayUtils.add(result, line);
		}
		if(file!=null){
			file.close();
			if(result.length==0)return null;
		}
		return result;
	}
}
