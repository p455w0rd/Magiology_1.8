package com.magiology.handelers;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class DownloadingHandeler{
	public static void downladAssets(){
		
	}
	public static void downloadUpdater(){
		
	}
	public static String findValue(String tag){
		try{
			URL url=new URL("https://raw.githubusercontent.com/LapisSea/Magiology_1.8/master/src/main/java/com/magiology/core/MReference.java");
			Scanner s=new Scanner(url.openStream());
			String value=null;
			try{
				s.findWithinHorizon(tag+"=", 9999);
				value=s.next();
				value=value.substring(1, value.length()-2);
			}catch(Exception e){}
			s.close();
			return value;
		}catch(Exception ex){}
		return null;
	}
	public static void downloadAndSaveToDisc(String ZipName,String ZipURL){
		try{
			URL url = new URL(ZipURL);
			URLConnection urlCon = url.openConnection();
			InputStream is = urlCon.getInputStream();
			FileOutputStream fos = new FileOutputStream(ZipName);
			byte[] buffer = new byte[1000];         
			int bytesRead = is.read(buffer);
			while (bytesRead > 0){
				fos.write(buffer, 0, bytesRead);
				bytesRead = is.read(buffer);
			}
			is.close();
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
