package com.magiology.handelers;

import static com.magiology.core.MReference.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.minecraft.client.model.PositionTextureVertex;

import com.magiology.modedmcstuff.ColorF;
import com.magiology.objhelper.helpers.Helper.H;
import com.magiology.objhelper.helpers.renderers.NoramlisedVertixBuffer;

public class BumpMapHandeler{
	private static Map<String,Float[][]> bumpMaps=new HashMap<String,Float[][]>();
	private static Map<String,Float> bumpMapsIntensity=new HashMap<String,Float>();
	
	
	/**
	 * loads all .png files if they have this is matched in their names: starts with bumpMap_, ends with some number (as intensity of the bump map) following by .png
	 */
	public static void loadBumpMaps(){
		try{
			Files.walk(Paths.get("mods\\1.7.10\\"+MODID)).forEach(filePath->{
			    if(Files.isRegularFile(filePath)){
			    	String
			    		fileName=filePath.getFileName().toString(),
			    		key="",
			    		nameParts[]=fileName.split("_");
			    	float intensity=-1;
			    	if(fileName.endsWith(".png")&&fileName.startsWith("bumpMap_")&&nameParts.length==3){
			    		nameParts[nameParts.length-1]=nameParts[nameParts.length-1].substring(0, nameParts[nameParts.length-1].length()-4);
			    		
			    		key=nameParts[1];
			    		try{
				    		intensity=Float.parseFloat(nameParts[nameParts.length-1]);
						}catch(Exception e){
							if(nameParts[nameParts.length-1].equals("pixel"))intensity=H.p;
							else if(nameParts[nameParts.length-1].toLowerCase().equals("p"))intensity=H.p;
						}
					    if(intensity>0&&!key.isEmpty()){
					    	BufferedImage img=null;
					    	try{img=ImageIO.read(filePath.toFile());}catch(Exception e){e.printStackTrace();}
					    	Float[][] pixels=new Float[img.getWidth()][img.getHeight()];
					    	
					    	for(int x=0;x<img.getWidth();x++){
					    		for(int y=0;y<img.getHeight();y++){
					    			Color pixel1=new Color(img.getRGB(x, y),true);
					    			ColorF pixel=ColorF.convert(pixel1);
					    			pixels[x][y]=(pixel.r+pixel.g+pixel.b-1.5F)/((1-pixel.a)*3);
						    	}
					    	}
					    	bumpMaps.put(key, pixels);
					    	bumpMapsIntensity.put(key, intensity);
					    }
			    	}
			    }
			});
		}catch(Exception e){
//			throw new NullPointerException(e.getMessage());
			e.printStackTrace();
		}
	}
	public static float[][] getBumpMap(String keyIdentifier){
		if(!bumpMaps.containsKey(keyIdentifier))return new float[0][0];
		
		Float[][] result1=bumpMaps.get(keyIdentifier);
		float[][] result=new float[result1.length][result1[0].length];
		float intensity=bumpMapsIntensity.get(keyIdentifier);
		for(int x=0;x<result1.length;x++){
			for(int y=0;y<result1[0].length;y++){
				result[x][y]=intensity*result1[x][y];
			}
		}
		return result;
	}
}
