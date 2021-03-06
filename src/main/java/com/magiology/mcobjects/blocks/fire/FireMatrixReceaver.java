package com.magiology.mcobjects.blocks.fire;

import com.magiology.mcobjects.tileentityes.TileEntityFireMatrixReceaver;
import com.magiology.util.utilclasses.UtilM;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class FireMatrixReceaver extends BlockContainer {

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos){
		UtilM.getBlockMetadata((World) world, pos);
		setBlockBounds(0, 0, 0, 1, 1, 1);
//		float p=1F/16F;
//		if(BM==0)	 setBlockBounds(p*5, p*14,p*2, p*11, 1,   p*14);
//		else if(BM==1)setBlockBounds(p*5, 0F,  p*2, p*11, p*2, p*14);
//		else if(BM==2)setBlockBounds(p*2, p*5, p*14, p*14, 0.7F, 1);
//		else if(BM==3)setBlockBounds(p*2, p*5, 0, p*14, 0.7F, p*2);
//		else if(BM==4)setBlockBounds(p*14  , p*5, p*2, 1, p*11,   p*14);
//		else if(BM==5)setBlockBounds(0  , p*5, p*2, p*2, p*11,   p*14);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state){
		UtilM.getBlockMetadata(world, pos);
		 
		 return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ).addCoord(pos.getX(), pos.getY(), pos.getZ());
	 }
	
	@Override
	public int getRenderType(){
		return -1;
	}
	@Override
	public boolean isOpaqueCube() {return false;}
	public FireMatrixReceaver(){
		super(Material.gourd);
		this.setHardness(0.2F).setHarvestLevel("pickaxe", 1);
	}
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int v1, EntityLivingBase placer){
		
		
		
		return world.getBlockState(pos).withProperty(UtilM.META, side.getIndex()+v1);
	}
	
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityFireMatrixReceaver();
	}
}
