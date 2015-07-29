package com.magiology.forgepowered.event;

import com.magiology.mcobjects.tileentityes.corecomponents.UpdateablePipe;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ForcePipeUpdate{
	
	public static void updatein3by3(World world,int x,int y,int z){
		for(int x1=-1;x1<2;x1++)
			for(int y1=-1;y1<2;y1++)
				for(int z1=-1;z1<2;z1++)
					updatePipe(world, x+x1, y+y1, z+z1);
	}
	
	
	
	public static void updatePipe(World world, int x, int y, int z){
    	TileEntity tile=world.getTileEntity(x, y, z);
    	if(tile instanceof UpdateablePipe){
//    		H.spawnEntityFX(new EntityFlameFX(world, x+0.5, y+0.5, z+0.5, 0, 0.1, 0));
    		((UpdateablePipe)tile).updateConnections();
    	}
    }
}
