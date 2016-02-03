package com.magiology.core.init;

import com.magiology.core.MReference;
import com.magiology.mcobjects.blocks.BFCPowerOut;
import com.magiology.mcobjects.blocks.BateryGeneric;
import com.magiology.mcobjects.blocks.BigFurnaceCore;
import com.magiology.mcobjects.blocks.ControlBlock;
import com.magiology.mcobjects.blocks.HologramProjector;
import com.magiology.mcobjects.blocks.PileODust;
import com.magiology.mcobjects.blocks.RareSpacePipe;
import com.magiology.mcobjects.blocks.fire.FireLamp;
import com.magiology.mcobjects.blocks.fire.FireMatrixReceaver;
import com.magiology.mcobjects.blocks.fire.FireMatrixTransferer;
import com.magiology.mcobjects.blocks.fire.FirePipe;
import com.magiology.mcobjects.blocks.network.NetworkCommandHolder;
import com.magiology.mcobjects.blocks.network.NetworkConductor;
import com.magiology.mcobjects.blocks.network.NetworkController;
import com.magiology.mcobjects.blocks.network.NetworkInterface;
import com.magiology.mcobjects.blocks.network.NetworkRouter;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MBlocks{

	//block references
	public static Block
		FireLamp,DiscoFlor,DiscoFlorPlatform,FirePipe,
		PileODust,BigFurnaceCore,BFCPowerOut,BatteryL1,BatteryL2,BatteryL3,
		BateryL100,ControlBlock,
		FireMatrixTransferer,FireMatrixReceaver,hologramProjector,rareSpacePipe,
		networkController,networkConductor,networkPointerContainer,networkInterface,networkCommandCenter;

	public static void preInit(){
		blocksInit();
	}

	public static void blocksInit(){
		FireMatrixTransferer=init(new FireMatrixTransferer().setUnlocalizedName("FireMatrixTransferer").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeStone));
		FireMatrixReceaver=init(new FireMatrixReceaver().setUnlocalizedName("FireMatrixReceaver").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeStone));
		FireLamp=init(new FireLamp().setUnlocalizedName("FireLamp").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeGlass));
		ControlBlock=init(new ControlBlock().setUnlocalizedName("ControlBlock").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeGlass));
		FirePipe=init(new FirePipe().setBlockTextureName(MReference.MODID + ":" + "FirePipeIcon").setUnlocalizedName("FirePipe").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		BigFurnaceCore=init(new BigFurnaceCore().setBlockTextureName("stonebrick").setStepSound(Block.soundTypeMetal).setUnlocalizedName("BigFurnaceCore").setCreativeTab(MCreativeTabs.Whwmmt_power));
		BatteryL1=init(new BateryGeneric(1).setUnlocalizedName("BatteryL1").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		BatteryL2=init(new BateryGeneric(2).setUnlocalizedName("BatteryL2").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		BatteryL3=init(new BateryGeneric(3).setUnlocalizedName("BatteryL3").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		BateryL100=init(new BateryGeneric(100).setUnlocalizedName("BateryL100").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		BFCPowerOut=init(new BFCPowerOut().setUnlocalizedName("BFCPowerOut").setStepSound(Block.soundTypeMetal));
		PileODust=init(new PileODust().setBlockTextureName(MReference.MODID + ":" + "IronLevel2").setUnlocalizedName("PileODust").setCreativeTab(MCreativeTabs.Whwmmt_core).setStepSound(Block.soundTypeMetal));
		hologramProjector=init(new HologramProjector().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("hologramProjector"));
		networkController=init(new NetworkController().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("networkController"));
		networkConductor=init(new NetworkConductor().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("networkConductor"));
		networkInterface=init(new NetworkInterface().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("networkInterface"));
		networkPointerContainer=init(new NetworkRouter().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("networkPointerContainer"));
		rareSpacePipe=init(new RareSpacePipe().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("rareSpacePipe"));
		networkCommandCenter=init(new NetworkCommandHolder().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("networkCommandCenter"));
	}

	static Block init(Block block){
		GameRegistry.registerBlock(block,block.getUnlocalizedName().substring("tile.".length(), block.getUnlocalizedName().length()));
		return block;
	}
}
