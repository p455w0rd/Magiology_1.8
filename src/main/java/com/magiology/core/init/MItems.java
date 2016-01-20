package com.magiology.core.init;

import net.minecraft.item.*;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.client.*;
import net.minecraftforge.common.util.*;
import net.minecraftforge.fml.common.registry.*;

import com.magiology.client.render.itemrender.*;
import com.magiology.core.*;
import com.magiology.handlers.animationhandlers.*;
import com.magiology.mcobjects.items.*;
import com.magiology.mcobjects.items.armor.*;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.Container;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.UpgradeType;
import com.magiology.util.utilclasses.*;

public class MItems{

	//normal Item references
	public static Item
		BedrockDust,BedrockIngot,EquivalentRightClickWandOfMagic,DiscoFlorPlacer,
		PowerCounter,FireHammer,FireBarrel,TheHand,IPowerSidenessInstructor,
		networkPointer,commandContainer;

	//multiple leveled Item upgrade references
	public static Item[] capacityUpgrades=new Item[5],rangeUpgrades=new Item[2],speedUpgrades=new Item[5];
	
	//one leveled Item upgrade references
	public static Item controlUpgrades,priorityUpgrades,flightUpgrades,thermalVisionUpgrades;
	
	//armor Item/Material references
	public static ArmorMaterial pants_42A,helmet_42A,WingsFTBFA;
	public static Item          pants_42I,helmet_42I,WingsFTBFI;
	
	public static void preInit(){
		register();
		armorRegister();
	}
	
	public static void register(){
		//Random items
		EquivalentRightClickWandOfMagic=init(new EquivalentRightClickWandOfMagic().setTextureName(MReference.MODID + ":" + "EquivalentRightClickWandOfMagic").setUnlocalizedName("EquivalentRightClickWandOfMagic").setCreativeTab(MCreativeTabs.Whwmmt_core).setMaxStackSize(1));
		PowerCounter=init(new PowerCounter().setTextureName(MReference.MODID + ":" + "PowerCounter").setUnlocalizedName("PowerCounter").setCreativeTab(MCreativeTabs.Whwmmt_power).setMaxStackSize(1));
		IPowerSidenessInstructor=init(new IPowerSidenessInstructor().setTextureName(MReference.MODID + ":" + "PowerCounter").setUnlocalizedName("IPowerSidenessInstructor").setCreativeTab(MCreativeTabs.Whwmmt_power).setMaxStackSize(1));
		BedrockDust=init(new BedrockDust().setTextureName(MReference.MODID + ":" + "BedrockDust").setUnlocalizedName("BedrockDust").setCreativeTab(MCreativeTabs.Whwmmt_core).setMaxStackSize(17));
		FireHammer=init(new FireHammer().setTextureName(MReference.MODID + ":" + "FireHammer").setUnlocalizedName("FireHammer").setCreativeTab(MCreativeTabs.Whwmmt_power).setMaxStackSize(1));
		FireBarrel=init(new FireBarrel(Container.FireBarrel).setTextureName(MReference.MODID + ":" + "FireBarrel").setUnlocalizedName("FireBarrel").setCreativeTab(MCreativeTabs.Whwmmt_power).setMaxStackSize(1));
		TheHand=init(new TheHand().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("TheHand").setMaxStackSize(1));
		networkPointer=init(new NetworkPointer().setUnlocalizedName("NetworkPointer").setCreativeTab(MCreativeTabs.Whwmmt_core).setMaxStackSize(1));
		commandContainer=init(new ProgramContainer().setUnlocalizedName("CommandContainer").setCreativeTab(MCreativeTabs.Whwmmt_core).setMaxStackSize(1));
		//Upgrades
		controlUpgrades=initGIU(new GenericItemUpgrade(1,UpgradeType.Control,null));
		priorityUpgrades=initGIU(new GenericItemUpgrade(1,UpgradeType.Priority,null));
		flightUpgrades=initGIU(new GenericItemUpgrade(1,UpgradeType.Flight,null));
		thermalVisionUpgrades=initGIU(new GenericItemUpgrade(1,UpgradeType.ThermalVision,null));
		//Upgrade arrays
		for(int a=0;a<capacityUpgrades.length;a++)capacityUpgrades[a]=initGIU(new GenericItemUpgrade(a+1,UpgradeType.Capacity,null));
		for(int a=0;a<rangeUpgrades.length;a++)rangeUpgrades[a]=initGIU(new GenericItemUpgrade(a+1,UpgradeType.Range,null));
		for(int a=0;a<speedUpgrades.length;a++)speedUpgrades[a]=initGIU(new GenericItemUpgrade(a+1,UpgradeType.Speed,null));
		
	}

	public static void armorRegister(){
		pants_42A=add("pants42", 1, new int[]{2, 6, 5, 2}, 30);
		helmet_42A=add("helmet42", 1, new int[]{2, 6, 5, 2}, 30);
		WingsFTBFA=add("DFTheWings", 1, new int[]{2, 6, 5, 2}, 30);
		
		pants_42I =init(new Pants_42("pants_42", pants_42A, "pants_42", 2,MCreativeTabs.Whwmmt_core));
		helmet_42I=init(new Helmet_42("helmet42", helmet_42A, "helmet42", 0,MCreativeTabs.Whwmmt_core));
		WingsFTBFI=init(new CyborgWingsFromTheBlackFireItem("WingsFTBF", WingsFTBFA, "WingsFTBF", 1,MCreativeTabs.Whwmmt_core));
	}private static ArmorMaterial add(String name, int durability, int[] reductionAmounts, int enchantability){
		return EnumHelper.addArmorMaterial(name, name, durability, reductionAmounts, enchantability);
	}
	
	public static void initRenders(){
//		bindItemWRender(Item.getItemFromBlock(MBlocks.FirePipe), new ItemRendererFirePipe());
//		bindItemWRender(PowerCounter, new ItemRendererPowerCounter());
//		bindItemWRender(pants_42I, new ItemRendererPants42());
//		bindItemWRender(helmet_42I, new ItemRendererHelmet42());
//		bindItemWRender(TheHand, TheHandHandler.getRenderer());
//		bindItemWRender(Magiology., new ItemRenderer);
	}
	
	private static GenericItemUpgrade initGIU(GenericItemUpgrade item){
		item.registerItemUpgrade();
		return item;
	}
	private static <T extends Item>T init(T item){
		GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
		return item;
	}

//	public static void setGenericUpgradeRenderer(Item item){
//		if(item instanceof GenericItemUpgrade)MinecraftForgeClient.registerItemRenderer(item, new ItemRendererGenericUpgrade());
//		else PrintUtil.printlnEr("Item: "+item.getUnlocalizedName()+" cannot be registered as a GenericUpgrade renderer!\n");
//	}

//	public static void bindItemWRender(Item item, IItemRenderer renderer){
//		MinecraftForgeClient.registerItemRenderer(item,renderer);
//	}
}
