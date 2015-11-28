package com.magiology.core.init;

import com.magiology.api.network.interfaces.registration.*;
import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.mcobjects.tileentityes.network.interfaces.*;

public class MInterfaces{
	public static void init(){
		InterfaceRegistration.registerInterfaceToTileEntity(new TileHologramProjectorInterface(), TileEntityHologramProjector.class);
	}
}
