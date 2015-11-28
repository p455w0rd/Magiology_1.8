package com.magiology.mcobjects.blocks;


import java.util.*;

import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.item.*;

import com.magiology.core.init.*;
import com.magiology.util.utilobjects.m_extension.*;

public class PileODust extends BlockM {
	
	float p=1F/16F;
	
	@Override
	public boolean isOpaqueCube() {return false;}
	
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
	public Item getItemDropped(IBlockState state, Random rand, int fortune){
		return MItems.BedrockDust;
	}
}