package com.magiology.forgepowered.proxy;

import java.util.Iterator;

import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.util.RegistrySimple;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.magiology.core.init.MGui;
import com.magiology.core.init.MItems;
import com.magiology.core.init.MTileEntitys;
import com.magiology.handelers.KeyHandler;
import com.magiology.mcobjects.BlockM;
import com.magiology.mcobjects.ItemM;
import com.magiology.util.utilclasses.DataStalker;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.Helper;
import com.magiology.util.utilclasses.Helper.H;


public class ClientProxy extends CommonProxy{
	@Override
	public void registerProxies(){
		FMLCommonHandler.instance().bus().register(new KeyHandler());
		MTileEntitys.initRenders();
		MItems.initRenders();
		MGui.registerGuis();
		
		BlockM.registerBlockModels();
		ItemM.registerItemModels();
	}
}

