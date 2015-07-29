package com.magiology.forgepowered.proxy;

import com.magiology.core.init.MGui;
import com.magiology.core.init.MItems;
import com.magiology.core.init.MTileEntitys;
import com.magiology.handelers.KeyHandler;

import cpw.mods.fml.common.FMLCommonHandler;


public class ClientProxy extends CommonProxy{
	@Override
	public void registerProxies(){
		FMLCommonHandler.instance().bus().register(new KeyHandler());
		MTileEntitys.initRenders();
		MItems.initRenders();
		MGui.registerGuis();
	}
}

