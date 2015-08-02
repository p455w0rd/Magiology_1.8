package com.magiology.mcobjects.tileentityes;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;

import com.magiology.core.init.MBlocks;
import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityM;
import com.magiology.objhelper.SlowdownHelper;

public class TileEntityFireExhaust extends TileEntityM{
	
	
	SlowdownHelper optimizer=new SlowdownHelper(5);
	public TileEntityFireExhaust(){}
	
	
	@Override
	public void updateEntity(){
		if(optimizer.isTimeWithAddProgress()){
			if(worldObj.getBlock(xCoord, yCoord-4, zCoord)!=MBlocks.OreStructureCore){
				Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(pos, worldObj.getBlock(pos), 0);
				worldObj.setBlock(pos, Blocks.air);
				worldObj.setTileEntity(pos, null);
			}
		}
	}
	
}
