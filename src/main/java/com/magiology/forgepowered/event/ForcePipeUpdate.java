package com.magiology.forgepowered.event;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile;
import com.magiology.util.utilclasses.Util.U;

public class ForcePipeUpdate{
	
	public static void updatein3by3(World world,BlockPos pos){
		if(U.isNull(world,pos))return;
		for(int x1=-1;x1<2;x1++)
			for(int y1=-1;y1<2;y1++)
				for(int z1=-1;z1<2;z1++)
					updatePipe(world, pos.add(x1,y1,z1));
	}
	
	
	
	public static void updatePipe(World world, BlockPos pos){
		if(U.isNull(world,pos))return;
    	TileEntity tile=world.getTileEntity(pos);
    	if(tile instanceof UpdateableTile){
//    		H.spawnEntityFX(new EntityFlameFX(world, x+0.5, y+0.5, z+0.5, 0, 0.1, 0));
    		((UpdateableTile)tile).updateConnections();
    	}
    }
}
