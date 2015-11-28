package com.magiology.mcobjects.blocks.fire;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.mcobjects.tileentityes.*;

public class FireExhaust extends BlockContainer {
	private float p= 1F/16F;

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos pos){
		setBlockBounds(p*4.5F, p*5, p*4.5F, p*11.5F, 1, p*11.5F);}
	@Override
	public int getRenderType(){
		return -1;
		
	}
	@Override
	public boolean isOpaqueCube() {return false;}
	public FireExhaust() {
		super(Material.gourd);
		this.setHardness(0.2F).setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityFireExhaust();
	}
}
