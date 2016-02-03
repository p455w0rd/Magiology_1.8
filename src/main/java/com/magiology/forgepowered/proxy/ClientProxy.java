package com.magiology.forgepowered.proxy;

import com.magiology.core.init.MGui;
import com.magiology.core.init.MItems;
import com.magiology.core.init.MTileEntitys;
import com.magiology.handlers.KeyHandler;
import com.magiology.util.utilobjects.m_extension.BlockM;
import com.magiology.util.utilobjects.m_extension.ItemM;

import net.minecraftforge.fml.common.FMLCommonHandler;


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

