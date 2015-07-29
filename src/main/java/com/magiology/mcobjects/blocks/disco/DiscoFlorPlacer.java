package com.magiology.mcobjects.blocks.disco;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.magiology.core.init.MBlocks;
import com.magiology.mcobjects.effect.EntityCustomfireFX;
import com.magiology.objhelper.helpers.Helper;

public class DiscoFlorPlacer extends Item{
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float x2, float y2, float z2){
		Helper.spawnEntityFX(new EntityCustomfireFX(world, x, y, y, 0,-1, 0, true, 7));
		int size=1;
		
		if(side==1)
		{
			for(int x1=size;x1>=0;x1--)
			{
				for(int z1=size;z1>=0;z1--)
				{
					if(world.getBlock( x-x1, y,   z+z1)==Blocks.stonebrick&&world.isAirBlock(x, y+1, z)==true){
						world.setBlock(x-x1, y+1, z+z1, MBlocks.DiscoFlor);
						world.setBlock(x-x1, y,   z+z1, MBlocks.DiscoFlorPlatform);
						world.setBlock(x-x1, y+1, z-z1, MBlocks.DiscoFlor);
						world.setBlock(x-x1, y,   z-z1, MBlocks.DiscoFlorPlatform);
						world.setBlock(x+x1, y+1, z+z1, MBlocks.DiscoFlor);
						world.setBlock(x+x1, y,   z+z1, MBlocks.DiscoFlorPlatform);
						world.setBlock(x+x1, y+1, z-z1, MBlocks.DiscoFlor);
						world.setBlock(x+x1, y,   z-z1, MBlocks.DiscoFlorPlatform);
						world.spawnParticle("smoke", x+x1+0.5, y+1+0.5, z+z1+0.5,0,0,0);
					}
				}
			}
		}
		if(side==1)return true;
		else return false;
	}
}
