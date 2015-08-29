package com.magiology.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;

import com.magiology.objhelper.helpers.Helper;

public class IOReadableMap{
	
	public String fileDir;
	public Map<String, String> data=new HashMap<String, String>();
	
	public IOReadableMap(String filePath){
		fileDir=FilenameUtils.removeExtension(filePath)+".rhm";//rhm stands for "readable hash map"
	}
	
	public void readFromFile(){
		try{
			if(!new File(fileDir).exists())new File(fileDir).createNewFile();
			for(String string:Files.readAllLines(new File(fileDir).toPath())){
				string=string.substring(1, string.length()-1);
				String[] line=string.split('"'+", "+'"');
				data.put(line[0], line[1]);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void writeToFile(){
		File file=new File(fileDir);
		try{
			clear(file);
			PrintWriter out=new PrintWriter(fileDir);
			try{
				for(Entry<String, String> line:data.entrySet()){
					out.println('"'+line.getKey()+'"'+", "+'"'+line.getValue()+'"');
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public int getI(String string, int... onNull){
		String a=data.get(string);
		if(Helper.isInteger(a))return Integer.parseInt(a);
		return onNull.length==1?onNull[0]:0;
	}
	public boolean getB(String string, boolean... onNull){
		String a=data.get(string);
		if(Helper.isBoolean(a))return Boolean.parseBoolean(a);
		return onNull.length==1?onNull[0]:false;
	}
	public String getS(String string, String... onNull){
		String s=data.get(string);
		if(s!=null)return s;
		return onNull.length==1?onNull[0]:"";
	}
	public void set(String tag, int Int){
		data.put(tag, Int+"");
	}
	public void set(String tag, boolean Boolean){
		data.put(tag, Boolean+"");
	}
	public void set(String tag, String string){
		data.put(tag, string);
	}
	
	private void clear(File file)throws IOException{
		if(file.exists()){
			file.delete();
			file.createNewFile();
		}
	}
}

