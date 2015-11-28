package com.magiology.core.init;

import net.minecraft.creativetab.*;
import net.minecraft.item.*;

public class MCreativeTabs{

	//creative tabs references
	public static CreativeTabs Whwmmt_core,Whwmmt_ores,Whwmmt_power,Whwmmt_upgrades;
	
	public static void preInit(){
		Whwmmt_core=new CreativeTabs(CreativeTabs.getNextID(),"Magiology"){@Override public Item getTabIconItem(){return Item.getItemFromBlock(MBlocks.FireLamp);}};
	    Whwmmt_ores=new CreativeTabs(CreativeTabs.getNextID(),"Magiology Ores"){@Override public Item getTabIconItem(){return Item.getItemFromBlock(MBlocks.SuperDuperLapisLazuli);}};
	    Whwmmt_power=new CreativeTabs(CreativeTabs.getNextID(),"Magiology Power"){@Override public Item getTabIconItem(){return Item.getItemFromBlock(MBlocks.FirePipe);}};
	    Whwmmt_upgrades=new CreativeTabs(CreativeTabs.getNextID(),"Magiology Upgrades"){@Override public Item getTabIconItem(){return MItems.FireBarrel;}};
		setImageNameForBackgrounds("custom_gui.png", MCreativeTabs.Whwmmt_core, MCreativeTabs.Whwmmt_ores, MCreativeTabs.Whwmmt_power, MCreativeTabs.Whwmmt_upgrades);
	}
	private static void setImageNameForBackgrounds(String imgName,CreativeTabs... creativeTabs){
    	for(int a=0;a<creativeTabs.length;a++)creativeTabs[a].setBackgroundImageName(imgName);
    }
}
