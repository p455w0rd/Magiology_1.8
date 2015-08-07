package com.magiology.mcobjects.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.magiology.mcobjects.tileentityes.TileEntityDontLookAtMe;

public class DontLookAtMe extends BlockContainer {
	
	float p=1F/16F;
	@Override
	public int getRenderType(){return -1;}
	@Override
	public boolean isOpaqueCube() {return false;}
	public DontLookAtMe() {
		super(Material.iron);
		this.setHardness(100F).setHarvestLevel("pickaxe", 1);
		this.setBlockBounds(p*3, p*3, p*3, 1-p*3, 1-p*3, 1-p*3);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityDontLookAtMe();
	}

}
