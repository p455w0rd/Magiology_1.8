package com.magiology.mcobjects.blocks;

import java.util.List;

import com.magiology.forgepowered.event.ForcePipeUpdate;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider.MultiColisionProviderRayTracer;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockContainerMultiColision extends BlockContainerM{

	protected BlockContainerMultiColision(Material material){
		super(material);
		if(!(createNewTileEntity(null, 0) instanceof MultiColisionProvider))throw new IllegalStateException("BlockContainerMultiColision has to be provided with a TileEntity that implements MultiColisionProvider class!");
	}
	@Override
	public int getRenderType(){
		return -1;
	}
	@Override
	public boolean isOpaqueCube(){return false;}
    @Override
	public boolean renderAsNormalBlock(){return false;}
    
    @Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ){
    	ForcePipeUpdate.updatein3by3((World)world,x,y,z);
    }
    
    @Override
	public void onPostBlockPlaced(World world, int x, int y, int z, int md){
    	ForcePipeUpdate.updatein3by3(world,x,y,z);
	}
    
    @Override
	public MovingObjectPosition collisionRayTrace(World w, int x, int y, int z, Vec3 startVec, Vec3 endVec){
    	if(!MultiColisionProviderRayTracer.isRayTracing){
        	int id=MultiColisionProviderRayTracer.getRayTracedBoxId(w, x, y, z, startVec, endVec, getResetBoundsOptionalFix(w, x, y, z));
        	return MultiColisionProviderRayTracer.results[id];
    	}else return super.collisionRayTrace(w, x, y, z, startVec, endVec);
    }
    
    public abstract AxisAlignedBB getResetBoundsOptional(World world, int x, int y, int z);
    private AxisAlignedBB getResetBoundsOptionalFix(World world, int x, int y, int z){
    	AxisAlignedBB result=getResetBoundsOptional(world, x, y, z);
    	if(result==null)return null;
    	if(result.minX<0)result.minX=0;
    	if(result.minY<0)result.minY=0;
    	if(result.minZ<0)result.minZ=0;
    	if(result.maxX>1)result.maxX=1;
    	if(result.maxY>1)result.maxY=1;
    	if(result.maxZ>1)result.maxZ=1;
    	return result;
    }
    
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aaBB, List list, Entity entity){
		AxisAlignedBB[] a=((MultiColisionProvider)world.getTileEntity(x, y, z)).getActiveBoxes();
		for(int i=0;i<a.length;i++){
			this.setBlockBounds((float)a[i].minX,(float)a[i].minY,(float)a[i].minZ,(float)a[i].maxX,(float)a[i].maxY,(float)a[i].maxZ);
			super.addCollisionBoxesToList(world, x, y, z, aaBB, list, entity);
		}
    }
    
}
