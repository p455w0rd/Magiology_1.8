package com.magiology.mcobjects.blocks;

import com.magiology.mcobjects.tileentityes.TileEntityBigFurnaceCore;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BigFurnaceCore extends BlockContainer {
	
	public BigFurnaceCore() {
		super(Material.glass);
		this.setHardness(10F).setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityBigFurnaceCore();
	}
    
}