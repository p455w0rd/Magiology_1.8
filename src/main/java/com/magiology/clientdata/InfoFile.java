package com.magiology.clientdata;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.magiology.core.Magiology;
import com.magiology.objhelper.helpers.Helper;

@SideOnly(Side.CLIENT)
public class InfoFile extends Magiology{
	
	public String fileDir;
	public Map<String, String> data=new HashMap<String, String>();
	
	public InfoFile(String fileName,String prefix){
		fileDir=fileName+"."+prefix;
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
	public void sync(){
		writeToFile();
	}
	
	public int getI(String string){
		String a=infoFile.data.get(string);
		if(Helper.isInteger(a))return Integer.parseInt(a);
		return 0;
	}
	public boolean getB(String string){
		String a=infoFile.data.get(string);
		if(Helper.isBoolean(a))return Boolean.parseBoolean(a);
		return false;
	}
	public void set(String tag, int Int){
		data.put(tag, Int+"");
	}
	public void set(String tag, boolean Boolean){
		data.put(tag, Boolean+"");
	}
	
	private void clear(File file)throws IOException{
		if(file.exists()){
			file.delete();
			file.createNewFile();
		}
	}
	
	private void yellowLine(String string){if(modInfGUI!=null)modInfGUI.addLine(modInfGUI.newLine(string, new Font(Font.SANS_SERIF, Font.PLAIN,15), Color.YELLOW));else Helper.println(string);}
	private void errorLine(String string){if(modInfGUI!=null)modInfGUI.addLine(modInfGUI.newLine(string, new Font(Font.SANS_SERIF, Font.BOLD+Font.ITALIC,15), Color.RED));else Helper.println(string);}
	private void sucessLine(String string){if(modInfGUI!=null)modInfGUI.addLine(modInfGUI.newLine(string, new Font(Font.SANS_SERIF, Font.PLAIN,15), Color.GREEN));else Helper.println(string);}
}

