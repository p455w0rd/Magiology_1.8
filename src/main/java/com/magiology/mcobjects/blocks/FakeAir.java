package com.magiology.mcobjects.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class FakeAir extends Block {

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int x, int y, int z){
		setBlockBounds(0, 0, 0, 0, 0, 0);}
	
	@Override
	public boolean isOpaqueCube() {return false;}
    @Override
	public boolean renderAsNormalBlock() {return false;}
	public FakeAir() {
		super(Material.glass);
		this.setLightLevel(1F).setHardness(0.2F).setHarvestLevel("pickaxe", 1);
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		world.spawnParticle("portal", x+(world.rand.nextFloat()*1), y+(world.rand.nextFloat()*1), z+(world.rand.nextFloat()*1), 0, 0, 0);
    }
}
