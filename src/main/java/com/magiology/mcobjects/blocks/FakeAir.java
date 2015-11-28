package com.magiology.mcobjects.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class FakeAir extends Block {

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos pos){
		setBlockBounds(0, 0, 0, 0, 0, 0);}
	
	@Override
	public boolean isOpaqueCube() {return false;}
	public FakeAir() {
		super(Material.glass);
		this.setLightLevel(1F).setHardness(0.2F).setHarvestLevel("pickaxe", 1);
	}

//	@Override
//	public void randomDisplayTick(World world, BlockPos pos, Random random) {
//		world.spawnParticle("portal", x+(world.rand.nextFloat()*1), y+(world.rand.nextFloat()*1), z+(world.rand.nextFloat()*1), 0, 0, 0);
//    }
}
