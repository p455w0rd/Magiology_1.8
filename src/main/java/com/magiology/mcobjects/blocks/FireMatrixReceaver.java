package com.magiology.mcobjects.blocks;

import com.magiology.mcobjects.tileentityes.TileEntityFireMatrixReceaver;
import com.magiology.mcobjects.tileentityes.TileEntityRemotePowerCounter;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class FireMatrixReceaver extends BlockContainer {

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){
		int BM=world.getBlockMetadata(x, y, z);
		setBlockBounds(0, 0, 0, 1, 1, 1);
//		float p=1F/16F;
//		if(BM==0)     setBlockBounds(p*5, p*14,p*2, p*11, 1,   p*14);
//		else if(BM==1)setBlockBounds(p*5, 0F,  p*2, p*11, p*2, p*14);
//		else if(BM==2)setBlockBounds(p*2, p*5, p*14, p*14, 0.7F, 1);
//		else if(BM==3)setBlockBounds(p*2, p*5, 0, p*14, 0.7F, p*2);
//		else if(BM==4)setBlockBounds(p*14  , p*5, p*2, 1, p*11,   p*14);
//		else if(BM==5)setBlockBounds(0  , p*5, p*2, p*2, p*11,   p*14);
	}
	
	 @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z){
			int BM=world.getBlockMetadata(x, y, z);
//			float p=1F/16F;
//			if(BM==0)     setBlockBounds(p*5, p*14,p*2, p*11, 1,   p*14);
//			else if(BM==1)setBlockBounds(p*5, 0F,  p*2, p*11, p*2, p*14);
//			else if(BM==2)setBlockBounds(p*2, p*5, p*14, p*14, 0.7F, 1);
//			else if(BM==3)setBlockBounds(p*2, p*5, 0, p*14, 0.7F, p*2);
//			else if(BM==4)setBlockBounds(p*14  , p*5, p*2, 1, p*11,   p*14);
//			else if(BM==5)setBlockBounds(0  , p*5, p*2, p*2, p*11,   p*14);
		 
		 return AxisAlignedBB.getBoundingBox(x+this.minX, y+this.minY, z+this.minZ, x+this.maxX, y+this.maxY, z+this.maxZ);
	 }
	
	@Override
	public int getRenderType(){
		return -1;
	}
	@Override
	public boolean isOpaqueCube() {return false;}
    @Override
	public boolean renderAsNormalBlock() {return false;}
	public FireMatrixReceaver(){
		super(Material.gourd);
		this.setHardness(0.2F).setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public void onPostBlockPlaced(World world, int x10, int y10, int z10, int metadata){
		if(world.getTileEntity(x10, y10, z10)instanceof TileEntityRemotePowerCounter){
		}
	}
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int v1){
		
		
		
        return side+v1;
    }
	
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityFireMatrixReceaver();
	}
}
