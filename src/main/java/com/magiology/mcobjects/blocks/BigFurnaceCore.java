package com.magiology.mcobjects.blocks;

import net.minecraft.block.material.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;

import com.magiology.mcobjects.tileentityes.*;
import com.magiology.util.utilobjects.m_extension.*;

public class BigFurnaceCore extends BlockContainerM{
	
	public BigFurnaceCore() {
		super(Material.glass);
		this.setHardness(10F).setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityBigFurnaceCore();
	}
    
}