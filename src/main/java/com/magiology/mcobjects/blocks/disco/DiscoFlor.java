package com.magiology.mcobjects.blocks.disco;

import java.util.Random;

import com.magiology.core.init.MBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DiscoFlor extends Block {
	
	float p=1F/16F;
	
	public DiscoFlor() {
		super(Material.ground);
		setLightLevel(0.6F);
		setHardness(4F);
		setHarvestLevel("pickaxe", 3);
		setBlockTextureName("OLWNPASRS" + ":" + "DiscoFlor");
	}
	@Override
	public int quantityDropped(Random par1Random)
    {
        return 0;
    }
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int x, int y, int z){
		setBlockBounds(0F, 0F, 0F, 1F, p/6, 1F);
		}
	@Override
	public boolean isOpaqueCube() {return false;}
    @Override
	public boolean renderAsNormalBlock() {return false;}
    
    @Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
    	
    	for (int la = 0; la < 1; ++la) {
			int l = world.getBlockMetadata(x, y, z);
			double v0=(random.nextFloat() - 0.5F) * 1D;
			double v1=(random.nextFloat() - 0.5F) * 0.3D;
			double v2=(random.nextFloat() - 0.5F) * 1D;
			double d0 = x + 0.5F + v0;
            double d1 = (y + 0.3F + v1);
            double d2 = z + 0.5F + v2;
            double d3 = 0.2199999988079071D;
            double d4 = 0.27000001072883606D;
            if (l == 1)      {world.spawnParticle("explode", d0 - d4, d1 + d3, d2, v0/9D, -0.03D, v2/9D);}
            else if (l == 2) {world.spawnParticle("explode", d0 + d4, d1 + d3, d2, v0/9D, -0.03D, v2/9D);}
            else if (l == 3) {world.spawnParticle("explode", d0, d1 + d3, d2 - d4, v0/9D, -0.03D, v2/9D);}
            else if (l == 4) {world.spawnParticle("explode", d0, d1 + d3, d2 + d4, v0/9D, -0.03D, v2/9D);}
            else             {world.spawnParticle("explode", d0, d1, d2,           v0/9D, -0.03D, v2/9D);}
            }
    	for (int la = 0; la < 2; ++la) {
			int l = world.getBlockMetadata(x, y, z);
			double v0=(random.nextFloat() - 0.5F) * 1D;
			double v1=(random.nextFloat() - 0.5F) * 0.3D;
			double v2=(random.nextFloat() - 0.5F) * 1D;
			double d0 = x + 0.5F + v0;
            double d1 = (y + 0.3F + v1);
            double d2 = z + 0.5F + v2;
            double d3 = 0.2199999988079071D;
            double d4 = 0.27000001072883606D;
            if (l == 1)      {world.spawnParticle("explode", d0 - d4, d1 + d3, d2, v0/9D, -0.03D, v2/9D);}
            else if (l == 2) {world.spawnParticle("explode", d0 + d4, d1 + d3, d2, v0/9D, -0.03D, v2/9D);}
            else if (l == 3) {world.spawnParticle("explode", d0, d1 + d3, d2 - d4, v0/9D, -0.03D, v2/9D);}
            else if (l == 4) {world.spawnParticle("explode", d0, d1 + d3, d2 + d4, v0/9D, -0.03D, v2/9D);}
            else             {world.spawnParticle("explode", d0, d1, d2,           v0/9D, -0.03D, v2/9D);}
            }
    	for (int la = 0; la < 1; ++la) {
			int l = world.getBlockMetadata(x, y, z);
			double v0=(random.nextFloat() - 0.5F) * 1D;
			double v1=(random.nextFloat() - 0.5F) * 0.3D;
			double v2=(random.nextFloat() - 0.5F) * 1D;
			double d0 = x + 0.5F + v0;
            double d1 = (y + 0.3F + v1);
            double d2 = z + 0.5F + v2;
            double d3 = 0.2199999988079071D;
            double d4 = 0.27000001072883606D;
            if (l == 1)      {world.spawnParticle("explode", d0 - d4, d1 + d3, d2, v0/9D, -0.03D, v2/9D);}
            else if (l == 2) {world.spawnParticle("explode", d0 + d4, d1 + d3, d2, v0/9D, -0.03D, v2/9D);}
            else if (l == 3) {world.spawnParticle("explode", d0, d1 + d3, d2 - d4, v0/9D, -0.03D, v2/9D);}
            else if (l == 4) {world.spawnParticle("explode", d0, d1 + d3, d2 + d4, v0/9D, -0.03D, v2/9D);}
            else             {world.spawnParticle("explode", d0, d1, d2,           v0/9D, -0.03D, v2/9D);}
            }
    }
    @Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block l){

			if (world.getBlock(x, y-1, z)==MBlocks.DiscoFlorPlatform){}
        else
        {
        	world.setBlock(x, y, z, Blocks.air, 0, 2);
        	world.setBlock(x, y-1, z, Blocks.stonebrick, 0, 2);
        }
	}
}