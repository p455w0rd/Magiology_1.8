package com.magiology.forgepowered.event;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.magiology.mcobjects.tileentityes.corecomponents.UpdateablePipe;

public class ForcePipeUpdate{
	
	public static void updatein3by3(World world,BlockPos pos){
		for(int x1=-1;x1<2;x1++)
			for(int y1=-1;y1<2;y1++)
				for(int z1=-1;z1<2;z1++)
					updatePipe(world, pos.add(pos));
	}
	
	
	
	public static void updatePipe(World world, BlockPos pos){
    	TileEntity tile=world.getTileEntity(pos);
    	if(tile instanceof UpdateablePipe){
//    		H.spawnEntityFX(new EntityFlameFX(world, x+0.5, y+0.5, z+0.5, 0, 0.1, 0));
    		((UpdateablePipe)tile).updateConnections();
    	}
    }
}
