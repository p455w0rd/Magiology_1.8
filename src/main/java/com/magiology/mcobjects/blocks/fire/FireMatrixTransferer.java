package com.magiology.mcobjects.blocks.fire;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.mcobjects.tileentityes.*;
import com.magiology.util.utilclasses.UtilM.U;

public class FireMatrixTransferer extends BlockContainer {

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos){
		U.getBlockMetadata((World) world, pos);
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
   public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state){
			U.getBlockMetadata(world, pos);
		 
		 return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ).addCoord(pos.getX(), pos.getY(), pos.getZ());
	 }
	
	@Override
	public int getRenderType(){
		return -1;
	}
	@Override
	public boolean isOpaqueCube() {return false;}
	public FireMatrixTransferer(){
		super(Material.gourd);
		this.setHardness(0.2F).setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityFireMatrixTransferer();
	}
}
