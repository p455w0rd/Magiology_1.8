package com.magiology.mcobjects.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.magiology.mcobjects.tileentityes.TileEntityEnergizedLapisOre;

public class EnergizedLapisOre extends BlockContainerM {
	
	
	@Override
	public boolean isOpaqueCube() {return false;}
	public EnergizedLapisOre(){
		super(Material.ground);
		this.setLightLevel(1F).setHardness(0.2F).setHarvestLevel("pickaxe", 1);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityEnergizedLapisOre();
	}

}
