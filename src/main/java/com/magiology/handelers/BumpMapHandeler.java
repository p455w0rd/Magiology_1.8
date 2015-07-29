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
	public static void addVertexesWithUVs4WithBumpMap(String bumpId,PositionTextureVertex[] plane,NoramlisedVertixBuffer buf){
		float[][] bump=getBumpMap(bumpId);
		float bumpX=bump.length,bumpY=bump[0].length;
		float width1=(float) (plane[2].vector3D.xCoord-plane[0].vector3D.xCoord);
		float width2=(float) (plane[3].vector3D.xCoord-plane[1].vector3D.xCoord);
		float heihgt1=(float)(plane[2].vector3D.yCoord-plane[0].vector3D.yCoord);
		float heihgt2=(float)(plane[3].vector3D.yCoord-plane[1].vector3D.yCoord);
		float depth1=(float) (plane[2].vector3D.zCoord-plane[0].vector3D.zCoord);
		float depth2=(float) (plane[3].vector3D.zCoord-plane[1].vector3D.zCoord);
		
		float txt1x=(float) (plane[2].texturePositionX-plane[0].texturePositionX);
		float txt2x=(float) (plane[3].texturePositionX-plane[1].texturePositionX);
		float txt1y=(float) (plane[2].texturePositionY-plane[0].texturePositionY);
		float txt2y=(float) (plane[3].texturePositionY-plane[1].texturePositionY);
		
//		buf.addVertexWithUV(plane[0].vector3D.xCoord, plane[0].vector3D.yCoord+0.01, plane[0].vector3D.zCoord, plane[0].texturePositionX, plane[0].texturePositionY);
//		buf.addVertexWithUV(plane[1].vector3D.xCoord, plane[1].vector3D.yCoord+0.01, plane[1].vector3D.zCoord, plane[1].texturePositionX, plane[1].texturePositionY);
//		buf.addVertexWithUV(plane[2].vector3D.xCoord, plane[2].vector3D.yCoord+0.01, plane[2].vector3D.zCoord, plane[2].texturePositionX, plane[2].texturePositionY);
//		buf.addVertexWithUV(plane[3].vector3D.xCoord, plane[3].vector3D.yCoord+0.01, plane[3].vector3D.zCoord, plane[3].texturePositionX, plane[3].texturePositionY);
		
		for(int x=0;x<bump.length-1;x++){
			for(int y=0;y<bump[0].length-1;y++){
				{
					buf.addVertexWithUV(
							plane[0].vector3D.xCoord+width1*x/bumpX,
							plane[0].vector3D.yCoord+heihgt1*x/bumpX,
							plane[0].vector3D.zCoord+depth1*y/bumpY,
							plane[0].texturePositionX+txt1x*x/bumpX,
							plane[0].texturePositionY+txt1y*y/bumpY);
					buf.addVertexWithUV(
							plane[1].vector3D.xCoord+width2*x/bumpX,
							plane[1].vector3D.yCoord+heihgt2*x/bumpX,
							plane[1].vector3D.zCoord+depth2*y/bumpY,
							plane[1].texturePositionX+txt2x*x/bumpX,
							plane[1].texturePositionY+txt2y*y/bumpY);
					buf.addVertexWithUV(
							plane[2].vector3D.xCoord-width1+width1*(x+1)/bumpX,
							plane[2].vector3D.yCoord-heihgt1+heihgt1*(x+1)/bumpX,
							plane[2].vector3D.zCoord-depth1+depth1*y/bumpY,
							plane[2].texturePositionX-txt1x+txt1x*x/bumpX,
							plane[2].texturePositionY-txt1y+txt1y*y/bumpY);
					buf.addVertexWithUV(
							plane[3].vector3D.xCoord-width2+width2*(x+1)/bumpX,
							plane[3].vector3D.yCoord-heihgt2+heihgt2*(x+1)/bumpX,
							plane[3].vector3D.zCoord-depth2+depth2*y/bumpY,
							plane[3].texturePositionX-txt2x+txt2x*x/bumpX,
							plane[3].texturePositionY-txt2y+txt2y*y/bumpY);

				}
			}
		}
	}
}
