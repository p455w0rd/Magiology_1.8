package com.magiology.mcobjects.tileentityes.corecomponents;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import org.apache.commons.lang3.*;

import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.vectors.*;

public interface MultiColisionProvider{
	
	public void getBoxesOnSide(List<AxisAlignedBB> result,int side);
	public void getExpectedBoxesOnSide(List<AxisAlignedBB> result,int side);
	public AxisAlignedBB getMainBox();
	
	public void setColisionBoxes();
	public AxisAlignedBB[] getExpectedColisionBoxes();
	
	public void setPointedBox(AxisAlignedBB box);
	public AxisAlignedBB getPointedBox();
	
	public void setPrevPointedBox(AxisAlignedBB box);
	public AxisAlignedBB getPrevPointedBox();
	
	public void detectAndSendChanges();
	
	public AxisAlignedBB[] getActiveBoxes();
	public AxisAlignedBB[] getBoxes();
	
	public class MultiColisionProviderRayTracer{
		private MultiColisionProviderRayTracer(){}
		public static boolean isRayTracing=false;
		
    	public static MovingObjectPosition[] results={};
    	public static AxisAlignedBB[] selectedBoxes={};
		public static int getRayTracedBoxId(World world, BlockPos pos, Vec3M startVec, Vec3M endVec,AxisAlignedBB resetBoundsOptional){
			TileEntity tester=world.getTileEntity(pos);
			if(!(tester instanceof MultiColisionProvider)){
				UtilM.println("There is no instance of ISidedColisionProvider at"+"("+pos.toString()+")!",UtilM.getStackTrace());
				return -1;
			}
	    	results=new MovingObjectPosition[0];
	    	selectedBoxes=new AxisAlignedBB[0];
	    	isRayTracing=true;
			//start
			MultiColisionProvider tile=(MultiColisionProvider)tester;
			Block block=UtilM.getBlock(world, pos);
			
	    	AxisAlignedBB[] aciveBoxes=tile.getActiveBoxes();
	    	if(aciveBoxes==null||aciveBoxes.length==0){
	    		//fail switch
				UtilM.println("ISidedColisionProviderRayTracer could not resolve a valid box!",UtilM.getStackTrace());
				isRayTracing=false;
				return -1;
	    	}
	    	
			for(int i=0;i<aciveBoxes.length;i++){
				block.setBlockBounds((float)aciveBoxes[i].minX,(float)aciveBoxes[i].minY,(float)aciveBoxes[i].minZ,(float)aciveBoxes[i].maxX,(float)aciveBoxes[i].maxY,(float)aciveBoxes[i].maxZ);
	       		results=ArrayUtils.add(results, block.collisionRayTrace(world, pos, startVec.conv(), endVec.conv()));
	    		selectedBoxes=ArrayUtils.add(selectedBoxes,aciveBoxes[i]);
	    		
			}
			
			if(results.length==0){
				if(resetBoundsOptional!=null)block.setBlockBounds((float)resetBoundsOptional.minX,(float)resetBoundsOptional.minY,(float)resetBoundsOptional.minZ,(float)resetBoundsOptional.maxX,(float)resetBoundsOptional.maxY,(float)resetBoundsOptional.maxZ);
				tile.setPointedBox(null);
				isRayTracing=false;
				return -1;
			}
			
			double smallest=10000;
		    int id=0;
			if(results.length>0){
				for(int id1=0;id1<results.length;id1++){
					MovingObjectPosition result1=results[id1];
					if(result1!=null&&result1.hitVec!=null){
						double distance=startVec.conv().distanceTo(result1.hitVec);
						if(distance<smallest){
							smallest=distance;
							id=id1;
						}
					}
				}
			}
		    if(resetBoundsOptional!=null)block.setBlockBounds((float)resetBoundsOptional.minX,(float)resetBoundsOptional.minY,(float)resetBoundsOptional.minZ,(float)resetBoundsOptional.maxX,(float)resetBoundsOptional.maxY,(float)resetBoundsOptional.maxZ);
		    else if(selectedBoxes.length>0)block.setBlockBounds((float)selectedBoxes[id].minX,(float)selectedBoxes[id].minY,(float)selectedBoxes[id].minZ,(float)selectedBoxes[id].maxX,(float)selectedBoxes[id].maxY,(float)selectedBoxes[id].maxZ);
		    try{
		    	tile.setPointedBox(selectedBoxes[id]);
			}catch(Exception e){
				UtilM.println("Error: max value without crash",selectedBoxes.length-1," and the used value is",id);
				UtilM.println("selectedBoxes size",selectedBoxes.length,"results size",results.length);
				e.printStackTrace();
			}
		    isRayTracing=false;
		    return id;
		}
		public static int getPointedId(MultiColisionProvider provider){
			if(provider.getPointedBox()!=null){
				AxisAlignedBB box1=provider.getPointedBox();
				AxisAlignedBB[] boxes=provider.getBoxes();
				for(int i=0;i<boxes.length;i++){
					AxisAlignedBB box2=provider.getBoxes()[i];
					if(UtilM.AxisAlignedBBEqual(box1, box2)){
						return i;
					}
				}
			}
			return -1;
		}
	}
}
