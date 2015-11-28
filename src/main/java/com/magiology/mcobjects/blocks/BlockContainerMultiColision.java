package com.magiology.mcobjects.blocks;

import java.util.*;

import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.forgepowered.events.*;
import com.magiology.mcobjects.tileentityes.corecomponents.*;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider.MultiColisionProviderRayTracer;
import com.magiology.util.utilobjects.m_extension.*;
import com.magiology.util.utilobjects.vectors.*;

public abstract class BlockContainerMultiColision extends BlockContainerM{

	protected BlockContainerMultiColision(Material material){
		super(material);
		if(!(createNewTileEntity(null, 0) instanceof MultiColisionProvider))throw new IllegalStateException("BlockContainerMultiColision has to be provided with a TileEntity that implements MultiColisionProvider class!");
	}
	@Override
	public int getRenderType(){return -1;}
	@Override
	public boolean isOpaqueCube(){return false;}
    
    @Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
    	ForcePipeUpdate.updatein3by3((World)world,pos);
    }
    
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state){
    	ForcePipeUpdate.updatein3by3(world,pos);
	}
    
    @Override
	public MovingObjectPosition collisionRayTrace(World w, BlockPos pos, Vec3 startVec, Vec3 endVec){
    	if(!MultiColisionProviderRayTracer.isRayTracing){
        	int id=MultiColisionProviderRayTracer.getRayTracedBoxId(w, pos, Vec3M.conv(startVec), Vec3M.conv(endVec), getResetBoundsOptionalFix(w, pos));
        	return MultiColisionProviderRayTracer.results[id];
    	}else return super.collisionRayTrace(w, pos, startVec, endVec);
    }
    
    public abstract AxisAlignedBB getResetBoundsOptional(World world, BlockPos pos);
    private AxisAlignedBB getResetBoundsOptionalFix(World world, BlockPos pos){
    	AxisAlignedBB result=getResetBoundsOptional(world, pos);
    	if(result==null)return null;
    	result=result.union(new AxisAlignedBB(0, 0, 0, 1, 1, 1));
    	return result;
    }
    
    @Override
    public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity){
		AxisAlignedBB[] a=((MultiColisionProvider)world.getTileEntity(pos)).getActiveBoxes();
		for(int i=0;i<a.length;i++){
			this.setBlockBounds((float)a[i].minX,(float)a[i].minY,(float)a[i].minZ,(float)a[i].maxX,(float)a[i].maxY,(float)a[i].maxZ);
			super.addCollisionBoxesToList(world, pos, state, mask, list, collidingEntity);
		}
    }
    
}
