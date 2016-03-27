package com.magiology.mcobjects.tileentityes.corecomponents;

import java.util.ArrayList;
import java.util.List;

import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

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
		
		public static MovingObjectPosition getRayTracedBoxId(World world, BlockPos pos, Vec3M startVec, Vec3M endVec){
			TileEntity tester=world.getTileEntity(pos);
			if(!(tester instanceof MultiColisionProvider)){
				PrintUtil.println("There is no instance of ISidedColisionProvider at"+"("+pos.toString()+")!",UtilM.getStackTrace());
				return null;
			}
			//start
			MultiColisionProvider tile=(MultiColisionProvider)tester;
			Block block=UtilM.getBlock(world, pos);
			
			AxisAlignedBB[] aciveBoxes=tile.getActiveBoxes();
			
			DoubleObject<Integer, MovingObjectPosition> hit=rayTrace(startVec, endVec, pos, aciveBoxes);
			
			if(hit.obj1==-1){
				tile.setPointedBox(null);
				return null;
			}
			AxisAlignedBB box=aciveBoxes[hit.obj1];
			tile.setPointedBox(box);
			return hit.obj2;
		}
		public static DoubleObject<Integer, MovingObjectPosition> rayTrace(Vec3M start, Vec3M end, BlockPos pos, AxisAlignedBB...boxes){
			Vec3M bPos=new Vec3M(pos);
			return rayTrace(start.sub(bPos), end.sub(bPos), boxes);
		}
		public static DoubleObject<Integer, MovingObjectPosition> rayTrace(Vec3M start, Vec3M end, AxisAlignedBB...boxes){
			List<Vec3> results=new ArrayList<>();
			List<MovingObjectPosition> hits=new ArrayList<>();
			Vec3 start1=start.conv(), end1=end.conv();
			for(AxisAlignedBB box:boxes){
				MovingObjectPosition hit=box.calculateIntercept(start1, end1);
				hits.add(hit);
				if(hit!=null&&hit.hitVec!=null)results.add(hit.hitVec);
				else results.add(null);
			}
			
			Vec3   closest=end1;
			int    closestID=-1;
			double closestDistance=Double.MAX_VALUE;
			for(int i=0,l=results.size();i<l;i++){
				Vec3 newVec=results.get(i);
				if(newVec!=null){
					double newDistance=closest.distanceTo(newVec);
					if(newDistance<closestDistance){
						closestDistance=newDistance;
						closest=newVec;
						closestID=i;
					}
				}
			}
			if(closestID==-1)return new DoubleObject<Integer, MovingObjectPosition>(-1, null);
			
			return new DoubleObject<Integer, MovingObjectPosition>(closestID, hits.get(closestID));
		}
		public static int getPointedId(MultiColisionProvider provider){
			if(provider.getPointedBox()!=null){
				AxisAlignedBB box1=provider.getPointedBox();
				AxisAlignedBB[] boxes=provider.getBoxes();
				for(int i=0;i<boxes.length;i++){
					AxisAlignedBB box2=provider.getBoxes()[i];
					if(UtilM.axisAlignedBBEqual(box1, box2)){
						return i;
					}
				}
			}
			return -1;
		}
	}
}
