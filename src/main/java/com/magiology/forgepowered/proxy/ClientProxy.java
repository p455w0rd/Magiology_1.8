package com.magiology.forgepowered.proxy;

import net.minecraftforge.fml.common.*;

import com.magiology.core.init.*;
import com.magiology.handlers.*;
import com.magiology.util.utilobjects.m_extension.*;


public class ClientProxy extends CommonProxy{
	@Override
	public void registerProxy(){
		FMLCommonHandler.instance().bus().register(new KeyHandler());
		MTileEntitys.initRenders();
		MItems.initRenders();
		MGui.registerGuis();
		MTileEntitys.setCustomRenderers();
		
		BlockM.registerBlockModels();
		ItemM.registerItemModels();
	}
}

