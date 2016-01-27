package com.magiology.mcobjects.blocks;

import com.magiology.forgepowered.events.ForcePipeUpdate;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider.MultiColisionProviderRayTracer;
import com.magiology.util.utilobjects.m_extension.BlockContainerM;
import com.magiology.util.utilobjects.vectors.Vec3M;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

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
	public boolean isFullCube(){
        return false;
    }
//	@Override
//	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos){
//		AxisAlignedBB result=null;
//		TileEntity tile=worldIn.getTileEntity(pos);
//		AxisAlignedBB[] boxes=((MultiColisionProvider)tile).getActiveBoxes();
//		for(AxisAlignedBB i:boxes){
//			if(result==null)result=i;
//			else result=result.union(i);
//		}
//		if(result!=null)setBlockBounds((float)result.minX, (float)result.minY, (float)result.minZ, (float)result.maxX, (float)result.maxY, (float)result.maxZ);
//		else setBlockBounds(0, 0, 0, 0, 0, 0);
//		
//	}
	
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
		for(AxisAlignedBB anA:a){
			this.setBlockBounds((float)anA.minX, (float)anA.minY, (float)anA.minZ, (float)anA.maxX, (float)anA.maxY, (float)anA.maxZ);
			super.addCollisionBoxesToList(world, pos, state, mask, list, collidingEntity);
		}
    }
}
