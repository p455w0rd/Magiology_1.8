package com.magiology.clientdata;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.core.MReference;
import com.magiology.core.Magiology;
import com.magiology.exceptions.FileNotValidException;
import com.magiology.objhelper.helpers.Helper;

@SideOnly(Side.CLIENT)
public class InfoFile extends Magiology{
	
	public String fileName,prefix,
					line1="-----> "+MReference.MODID+" or "+MReference.NAME+" "+MReference.VERSION+" <-----",
					line2prep="> "+MReference.MODID.toUpperCase()+" AUTO GENERATED FILE <",
					line2="",
					line3prep="> Do not edit this file in any way! <",
					line3="",
					line4="Content->>";
	public String[] FileContent=null;
	List<FInter> data=new ArrayList<FInter>();
	
	boolean isInit=true;
	public TxtFileIntSaver GUITxtSize,GUIPos,GUIExitOn;
	public TxtFileBooleanSaver GUIOpen,GUIAR,GUIAA;
	
	
	public InfoFile(String fileName,String prefix){
	  this.fileName=fileName;this.prefix=prefix;
	}
	
	public void refreshContentFromFile(){
		
	}
	public void putContentToFile(){
		
	}
	
	public boolean setUpInfoFile(){
		int line=0;
		File file=new File(fileName+"."+prefix);
		boolean isNull=file==null&&file.exists()&&!file.isDirectory()&&file.canRead()&&file.canWrite()&&file.canExecute();
		GUIOpen=new TxtFileBooleanSaver(file, line++, true);
		GUIPos=new TxtFileIntSaver(file, line++, 1);
		GUITxtSize=new TxtFileIntSaver(file, line++, 18);
		GUIAR=new TxtFileBooleanSaver(file, line++, false);
		GUIAA=new TxtFileBooleanSaver(file, line++, false);
		GUIExitOn=new TxtFileIntSaver(file, line++, 0);
		data.add(GUIOpen);
		data.add(GUITxtSize);
		data.add(GUIPos);
		data.add(GUIAR);
		data.add(GUIAA);
		data.add(GUIExitOn);
		try{
			FileContent=null;
			line2="";
			line3="";
//			Helper.println("in null? ",isNull);
			
			calculateLines();
			String tab="     ";
			yellowLine("if(mods."+MReference.MODID+".isLoading()){");
			yellowLine(tab+"Info: FileName="+fileName+"."+prefix);
			yellowLine(tab+"Info: Searching for the info file!");
			FileContent=FileManager.readFile(file);
			if(FileContent==null){
				errorLine(tab+"Info: No info file!");
				yellowLine(tab+"Info: Creating a new one.");
				FileManager.writeCreateFile(file,FileContent,line1,line2,line3,line4,"","");
				sucessLine(tab+"Info: Info file created!");
			}
			yellowLine(tab+"Info: Double checking for the file!");
			FileContent=FileManager.readFile(file);
			boolean result=FileContent!=null;
			if(!result){
				errorLine(tab+"Error: InfoFile -->"+fileName+"<-- can not be found! THIS IS AN ERROR!");
				errorLine(tab+"Info: Ending the game!");
			}else{
				sucessLine(tab+"Info: InfoFile is loaded successfully!");
				yellowLine(tab+"if(mods."+MReference.MODID+".isChecking()){");
				yellowLine(tab+tab+"Info: InfoFile checking if file is valid!");
				if(FileContent[0].compareTo(line1)==0&&FileContent[1].compareTo(line2)==0&&FileContent[2].compareTo(line3)==0&&FileContent[3].compareTo(line4)==0&&(FileContent[4].equals("")||FileContent[4].startsWith("FLineOnNmb "))){
					sucessLine(tab+tab+"Info: InfoFile file is valid!");
					if(!isNull)for(FInter s:data)s.loadValue();
				}else{
					errorLine(tab+tab+"Error: InfoFile file is not valid!");
					yellowLine(tab+tab+"Info: Deleting InfoFile file!");
					file.delete();
					sucessLine(tab+tab+"Info: InfoFile deleted!");
					yellowLine(tab+tab+"Info: Generating a new InfoFile!");
					FileManager.writeCreateFile(file,null,line1,line2,line3,line4,"","");
					sucessLine(tab+tab+"Info: new InfoFile generated!");
				}
				yellowLine(tab+"}");
			}
			yellowLine("}");
			if(!isNull)for(FInter s:data){
				s.save();
				s.exit();
			}
			if(!isNull)data.clear();
			isInit=false;
			if(!result)Helper.throwException(new FileNotValidException());
			return result;
		}catch(Exception e){
//			errorLine("INFO FILE IS BLANK! RESTARTING THE LOADING!");
//			return setUpInfoFile();
		}
		return false;
	}
	private void yellowLine(String string){if(modInfGUI!=null)modInfGUI.addLine(modInfGUI.newLine(string, new Font(Font.SANS_SERIF, Font.PLAIN,15), Color.YELLOW));else Helper.println(string);}
	private void errorLine(String string){if(modInfGUI!=null)modInfGUI.addLine(modInfGUI.newLine(string, new Font(Font.SANS_SERIF, Font.BOLD+Font.ITALIC,15), Color.RED));else Helper.println(string);}
	private void sucessLine(String string){if(modInfGUI!=null)modInfGUI.addLine(modInfGUI.newLine(string, new Font(Font.SANS_SERIF, Font.PLAIN,15), Color.GREEN));else Helper.println(string);}
	public void calculateLines(){
		int[] lenghts={line1.length(),line2prep.length(),line3prep.length()};
		int[] diference={lenghts[0]-lenghts[1],lenghts[0]-lenghts[2]};
		//line1
		for(int i=0;i<diference[0];i++)if(i%2==0)line2=line2+"~";
		line2=line2+line2prep;
		for(int i=0;i<diference[0];i++)if(i%2!=0)line2=line2+"~";
		//line2
		for(int i=0;i<diference[1];i++)if(i%2==0)line3=line3+"~";
		line3=line3+line3prep;
		for(int i=0;i<diference[1];i++)if(i%2!=0)line3=line3+"~";
		
	}
	String lineID="FLineOnNmb ";
	public class TxtFileStringSaver implements FInter{
		@Override public void exit(){file.close();}
		public int lineId;
		public String string;
		Scanner file=null;
		public TxtFileStringSaver(File file,int lineId,String string){
			this.lineId=lineId+5;
			this.string=string;
			try{this.file=new Scanner(file);
			}catch(Exception e){}
		}
		@Override
		public void save(){
			if(file==null)return;
			FileContent[lineId]=lineID+""+(lineId-4)+"-> "+string.toString();
			String[] overrideFC=FileContent.clone();
			for(int a=0;a<4;a++)overrideFC[a]=null;
			FileManager.writeCreateFile(new File(fileName+"."+prefix),FileContent);
		}
		public String load(){
			file.findWithinHorizon(lineID+lineId, 9999999);
			string=file.next();
			return string;
		}
		@Override
		public void loadValue(){
			boolean isOkForLoading=false;
			while(isOkForLoading)try{if(!FileContent[lineId].contains(" "+(lineId-4)+"->"))save();
			}catch(ArrayIndexOutOfBoundsException e){FileContent=ArrayUtils.add(FileContent, "");}
			string=load();
		}
	}
	public class TxtFileIntSaver implements FInter{
		@Override public void exit(){file.close();}
		public int lineId;
		public int Int;
		Scanner file=null;
		public TxtFileIntSaver(File file,int lineId,int Int){
			this.lineId=lineId+5;
			this.Int=Int;
			try{this.file=new Scanner(file);
			}catch(Exception e){e.printStackTrace();}
		}
		@Override
		public void save(){
			if(file==null)return;
			while(FileContent.length<lineId+1){
				FileContent=ArrayUtils.add(FileContent, "");
			}
			FileContent[lineId]=lineID+""+(lineId-4)+"-> "+Int;
			String[] overrideFC=FileContent.clone();
			for(int a=0;a<4;a++)overrideFC[a]=null;
			FileManager.writeCreateFile(new File(fileName+"."+prefix),FileContent);
		}
		public int load(){
			String obj="A";
			try{
				file.findWithinHorizon(lineID+(lineId-4), 9999999);
				file.next();
				obj=file.next();
			}catch(Exception e){
				save();
			}
			try{
				Int=Integer.parseInt(obj);
			}catch(Exception e){
				e.printStackTrace();
				save();
//				Helper.println("failed",lineId);
				return -1;
			}
			return Int;
		}
		@Override
		public void loadValue(){
			boolean isOkForLoading=false;
			while(isOkForLoading)try{if(!FileContent[lineId].contains(" "+(lineId-4)+"->"))save();
			}catch(ArrayIndexOutOfBoundsException e){FileContent=ArrayUtils.add(FileContent, "");}
			Int=load();
		}
		
	}
	public class TxtFileBooleanSaver implements FInter{
		@Override public void exit(){if(file!=null)file.close();}
		public int lineId;
		public boolean Boolean;
		Scanner file=null;
		public TxtFileBooleanSaver(File file,int lineId,boolean Boolean){
			this.lineId=lineId+5;
			this.Boolean=Boolean;
			try{
				this.file=new Scanner(file);
			}catch(FileNotFoundException e){
				e.printStackTrace();
				file.toString();
			}
		}
		@Override
		public void save(){
			if(file==null)return;
//			while(FileContent.length<lineId){
//				FileContent=ArrayUtils.add(FileContent, "");
//			}
			FileContent[lineId]=lineID+""+(lineId-4)+"-> "+Boolean;
			String[] overrideFC=FileContent.clone();
			for(int a=0;a<4;a++)overrideFC[a]=null;
			FileManager.writeCreateFile(new File(fileName+"."+prefix),FileContent);
		}
		public boolean load(){
			String obj=null;
			try{
				file.findWithinHorizon(lineID+(lineId-4), 9999999);
				file.next();
				obj=file.next();
			}catch(Exception e){
				save();
			}
			try{
				Boolean=java.lang.Boolean.parseBoolean(obj);
			}catch(Exception e){
				e.printStackTrace();
				save();
//				Helper.println("failed",lineId);
//				Helper.throwException(new InvalidFileInteractionLineException());
			}
			return Boolean;
		}
		@Override
		public void loadValue(){
			boolean isOkForLoading=false;
			while(isOkForLoading)try{if(!FileContent[lineId].contains(" "+(lineId-4)+"->"))save();
			}catch(ArrayIndexOutOfBoundsException e){FileContent=ArrayUtils.add(FileContent, "");}
			Boolean=load();
		}
		
	}
	public interface FInter{
		public void exit();
		public void save();
		public void loadValue();
	}
}

