package com.magiology.mcobjects.blocks;


import java.util.Random;

import com.magiology.core.init.MItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class PileODust extends Block {
	
	float p=1F/16F;
	
	@Override
	public boolean isOpaqueCube() {return false;}
    @Override
	public boolean renderAsNormalBlock() {return false;}
	
    @Override
	public int getRenderType(){
    	return 0;
    }
    
	public PileODust()
	{
		super(Material.ground);
		this.setHardness(0.1F).setHarvestLevel("pickaxe", 1);
		this.setBlockBounds(p*5.5F, p*0F, p*5.5F, p*10.5F, p*1F, p*10.5F);
	}
	@Override
	public Item getItemDropped(int p1, Random rand, int p2){
		return MItems.BedrockDust;
	}
}