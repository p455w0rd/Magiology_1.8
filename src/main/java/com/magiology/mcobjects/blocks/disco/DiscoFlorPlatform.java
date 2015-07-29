package com.magiology.mcobjects.blocks.disco;

import java.util.Random;

import com.magiology.core.init.MBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class DiscoFlorPlatform extends Block {
	public DiscoFlorPlatform() {
		super(Material.ground);
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
	public void onNeighborBlockChange(World world, int x, int y, int z, Block l){

			if(world.getBlock(x, y+1, z)!=MBlocks.DiscoFlor){
        		world.setBlock(x, y, z, Blocks.stonebrick, 0, 2);
        }
	}
   }
