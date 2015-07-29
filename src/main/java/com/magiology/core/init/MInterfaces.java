package com.magiology.core.init;

import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.mcobjects.tileentityes.network.interfaces.TileHologramProjectorInterface;
import com.magiology.mcobjects.tileentityes.network.interfaces.registration.InterfaceRegistration;

public class MInterfaces{
	public static void init(){
		InterfaceRegistration.registerInterfaceToTileEntity(new TileHologramProjectorInterface(), TileEntityHologramProjector.class);
	}
}
