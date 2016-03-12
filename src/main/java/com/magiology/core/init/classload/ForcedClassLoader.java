package com.magiology.core.init.classload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.core.MReference;
import com.magiology.core.Magiology;
import com.magiology.util.utilclasses.FileUtil;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM;


public class ForcedClassLoader{
	
	private static String 
		startPath="..\\src\\main\\java\\",
		blacklist[]={
			"com.magiology.client.render.itemrender.ItemRendererFirePipe",
			"com.magiology.client.render.itemrender.ItemRendererGenericUpgrade",
			"com.magiology.client.render.itemrender.ItemRendererHelmet42",
			"com.magiology.client.render.itemrender.ItemRendererPants42",
			"com.magiology.client.render.itemrender.ItemRendererPowerCounter",
			"com.magiology.client.render.tilerender.isbhrrender.ISBRH",
			"com.magiology.client.render.tilerender.isbhrrender.RenderFireLampISBRH"
		};
	private static File thisClass=new File(startPath+getPathToClass(ClassList.class));
	
	private static int startPathLength=startPath.length();
	
	public static void load(){
		
		generateAndInject();
		
		ClassLoader loader=ForcedClassLoader.class.getClassLoader();
		PrintUtil.println("Starting to load all classes from "+MReference.NAME+"!");
		UtilM.startTime();
		List<String> failed=new ArrayList<>();
		for(String clazz:ClassList.classes){
			try{
				loader.loadClass(clazz);
			}catch(Exception e){
				if(!ArrayUtils.contains(blacklist, clazz))failed.add(e.getClass().getSimpleName()+": "+clazz);
			}
		}
		PrintUtil.println("Loading of all classes is done in "+UtilM.endTime()+"ms.");
		if(failed.isEmpty()){
			PrintUtil.println("No classes have failed to load! ^_^");
			setError(false);
		}
		else{
			setError(true);
			PrintUtil.println("But some classes have failed to load!");
			PrintUtil.println("This is not fatal or a big deal but it coud cause some problems in rare cases.");
			PrintUtil.println("Failed class list:");
			for(String string:failed)PrintUtil.println(string);
			PrintUtil.println("You may want to refresh the class list!");
		}
	}
	
	private static void setError(boolean b){
		if(!Magiology.isDev())return;
			
		StringBuilder clazzB=FileUtil.getFileTxt(thisClass);
		String clazz=clazzB.toString();
		
		int start=clazz.indexOf("error=")+"error=".length(),end=start;
		while(clazz.charAt(end)!=';')end++;
		
		clazzB.replace(start, end, b+"");
		String newClass=clazzB.toString();
		if(!clazz.equals(newClass))FileUtil.setFileTxt(thisClass, newClass);
	}
	private static void generateAndInject(){
		if(!ClassList.error||!Magiology.isDev())return;
		try{
			
			List<String> fileNames=getFileNames(new ArrayList<>(), new File(startPath+"com\\magiology").toPath());
			
			
			List<String> beforeList=new ArrayList<>(),afterList=new ArrayList<>();
			String tabs="\t";
			BufferedReader br=new BufferedReader(new FileReader(thisClass));
			
			StringBuilder originalFile1=new StringBuilder();
			
			boolean listStarted=false,listEnded=false;
			{
				String line=null;
				while((line=br.readLine())!=null){
					originalFile1.append(line).append("\n");
					if(!listStarted){
						beforeList.add(line);
						if(line.endsWith("classes={")){
							listStarted=true;
							int tabCount=0;
							while(line.length()>=tabCount&&line.charAt(tabCount)=='\t'){
								tabCount++;
								tabs+="\t";
							}
						}
					}
					if(!listEnded){
						if(line.endsWith("};"))listEnded=true;
					}
					if(listEnded){
						afterList.add(line);
					}
					
				}
			}
			br.close();
			
			String originalFile=originalFile1.toString();
			
			if(afterList.get(afterList.size()-1).isEmpty())afterList.remove(afterList.size()-1);
			
			final StringBuilder newFile=new StringBuilder();
			
			beforeList.forEach(line->newFile.append(line).append("\n"));
			
			for(int i=0;i<fileNames.size();i++){
				String line=fileNames.get(i);
				if(i!=fileNames.size()-1)line+=",";
				char[] line1=line.toCharArray();
				
				newFile.append(tabs);
				for(int j=0;j<line1.length;j++){
					if(line1[j]=='\\')newFile.append('.');
					else newFile.append(line1[j]);
				}
				newFile.append("\n");
			}
			
			afterList.forEach(line->newFile.append(line).append("\n"));
			
			setError(false);
			if(!newFile.equals(originalFile)){
				FileUtil.setFileTxt(thisClass, newFile.toString());
				UtilM.exit(404);
			}
			
		}catch(IOException e){
		}
	}
	private static String getPathToClass(Class clazz){
	    return clazz.getName().replace(".", "/") + ".java";
	}
	
	private static List<String> getFileNames(List<String> fileNames, Path dir){
		try(DirectoryStream<Path> stream=Files.newDirectoryStream(dir)){
			for(Path path:stream){
				if(path.toFile().isDirectory()){
					getFileNames(fileNames, path);
				}else{
					String path1=path.toString();
					if(path1.endsWith(".java"))fileNames.add('"'+path1.substring(startPathLength, path1.length()-5)+"\"");
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return fileNames;
	} 
	
	
}
