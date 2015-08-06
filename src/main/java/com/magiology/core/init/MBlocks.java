package com.magiology.core.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.magiology.core.MReference;
import com.magiology.mcobjects.blocks.BFCPowerOut;
import com.magiology.mcobjects.blocks.BateryGeneric;
import com.magiology.mcobjects.blocks.BedrockBreaker;
import com.magiology.mcobjects.blocks.BigFurnaceCore;
import com.magiology.mcobjects.blocks.ControlBlock;
import com.magiology.mcobjects.blocks.DontLookAtMe;
import com.magiology.mcobjects.blocks.FakeAir;
import com.magiology.mcobjects.blocks.FireExhaust;
import com.magiology.mcobjects.blocks.FireGun;
import com.magiology.mcobjects.blocks.FireLamp;
import com.magiology.mcobjects.blocks.FireMatrixReceaver;
import com.magiology.mcobjects.blocks.FireMatrixTransferer;
import com.magiology.mcobjects.blocks.FirePipe;
import com.magiology.mcobjects.blocks.HologramProjector;
import com.magiology.mcobjects.blocks.OreLevelX;
import com.magiology.mcobjects.blocks.OreStructureCore;
import com.magiology.mcobjects.blocks.ParticleLauncher;
import com.magiology.mcobjects.blocks.PileODust;
import com.magiology.mcobjects.blocks.RemotePowerCounter;
import com.magiology.mcobjects.blocks.SmartCrafter;
import com.magiology.mcobjects.blocks.energizedLapisOre;
import com.magiology.mcobjects.blocks.network.NetworkConductor;
import com.magiology.mcobjects.blocks.network.NetworkController;
import com.magiology.mcobjects.blocks.network.NetworkInterface;
import com.magiology.mcobjects.blocks.network.NetworkPointerContainer;

public class MBlocks{

	//block references
	public static Block
		IronLevel2,IronLevel3,IronLevel4,IronLevel5,SuperDuperIron, GoldLevel2,GoldLevel3,GoldLevel4,
		GoldLevel5,SuperDuperGold,DiamondLevel2,DiamondLevel3,DiamondLevel4,DiamondLevel5,SuperDuperDiamond,
		LapisLazuliLevel2,LapisLazuliLevel3,LapisLazuliLevel4,LapisLazuliLevel5,SuperDuperLapisLazuli,
		RedstoneLevel2,RedstoneLevel3,RedstoneLevel4,RedstoneLevel5,SuperDuperRedstone,NetherQuartzLevel2,
		NetherQuartzLevel3,NetherQuartzLevel4,NetherQuartzLevel5,SuperDuperNetherQuartz,EmeraldLevel2,
		EmeraldLevel3,EmeraldLevel4,EmeraldLevel5,SuperDuperEmerald,CoalLevel2,CoalLevel3,CoalLevel4,
		CoalLevel5,SuperDuperCoal,FireLamp,DiscoFlor,DiscoFlorPlatform,OreStructureCore,FirePipe,FireGun,
		FireExhaust,FakeAir,bedrockBreaker,PileODust,BigFurnaceCore,BFCPowerOut,BatteryL1,BatteryL2,BatteryL3,
		BateryL100,particleLauncher,DontLookAtMe,energizedLapisOre,ControlBlock,RemotePowerCounter,
		FireMatrixTransferer,FireMatrixReceaver,smartCrafter,hologramProjector,
		networkController,networkConductor,networkPointerContainer,networkInterface;

	public static void preInit(){
		blocksInit();
		oresInit();
	}

	public static void blocksInit(){
		
		smartCrafter=init(new SmartCrafter().setBlockName("smartCrafter").setCreativeTab(MCreativeTabs.Whwmmt_core).setStepSound(Block.soundTypeMetal));
		FireMatrixTransferer=init(new FireMatrixTransferer().setBlockName("FireMatrixTransferer").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeStone));
		FireMatrixReceaver=init(new FireMatrixReceaver().setBlockName("FireMatrixReceaver").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeStone));
		FireLamp=init(new FireLamp().setBlockName("FireLamp").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeGlass));
		ControlBlock=init(new ControlBlock().setBlockName("ControlBlock").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeGlass));
		RemotePowerCounter=init(new RemotePowerCounter().setBlockName("RemotePowerCounter").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeStone));
		FireGun=init(new FireGun().setBlockName("FireGun").setStepSound(Block.soundTypeStone));
		FirePipe=init(new FirePipe().setBlockTextureName(MReference.MODID + ":" + "FirePipeIcon").setBlockName("FirePipe").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		FireExhaust=init(new FireExhaust().setBlockName("FireExhaust").setStepSound(Block.soundTypeStone));
		FakeAir=init(new FakeAir().setBlockName("FakeAir"));
		OreStructureCore=init(new OreStructureCore().setBlockName("OreStructureCore").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeGlass));
		BigFurnaceCore=init(new BigFurnaceCore().setBlockName("BigFurnaceCore").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal).setBlockTextureName("stonebrick"));
		particleLauncher=init(new ParticleLauncher().setBlockName("particleLauncher").setCreativeTab(MCreativeTabs.Whwmmt_core).setStepSound(Block.soundTypeMetal).setBlockTextureName("iron_block"));
		BatteryL1=init(new BateryGeneric(1).setBlockName("BatteryL1").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		BatteryL2=init(new BateryGeneric(2).setBlockName("BatteryL2").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		BatteryL3=init(new BateryGeneric(3).setBlockName("BatteryL3").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		BateryL100=init(new BateryGeneric(100).setBlockName("BateryL100").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		BFCPowerOut=init(new BFCPowerOut().setBlockName("BFCPowerOut").setStepSound(Block.soundTypeMetal));
		bedrockBreaker=init(new BedrockBreaker().setBlockName("bedrockBreaker").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		PileODust=init(new PileODust().setBlockName("PileODust").setCreativeTab(MCreativeTabs.Whwmmt_core).setStepSound(Block.soundTypeMetal).setBlockTextureName(MReference.MODID + ":" + "IronLevel2"));
		DontLookAtMe=init(new DontLookAtMe().setCreativeTab(MCreativeTabs.Whwmmt_core).setBlockName("DontLookAtMe").setBlockTextureName(MReference.MODID + ":" + "nista16x16"));
		energizedLapisOre=init(new energizedLapisOre().setCreativeTab(MCreativeTabs.Whwmmt_core).setBlockName("energizedLapisOre").setBlockTextureName("stone"));
		hologramProjector=init(new HologramProjector().setCreativeTab(MCreativeTabs.Whwmmt_core).setBlockName("hologramProjector"));
		networkController=init(new NetworkController().setCreativeTab(MCreativeTabs.Whwmmt_core).setBlockName("networkController"));
		networkConductor=init(new NetworkConductor().setCreativeTab(MCreativeTabs.Whwmmt_core).setBlockName("networkConductor"));
		networkInterface=init(new NetworkInterface().setCreativeTab(MCreativeTabs.Whwmmt_core).setBlockName("networkInterface"));
		networkPointerContainer=init(new NetworkPointerContainer().setCreativeTab(MCreativeTabs.Whwmmt_core).setBlockName("networkPointerContainer"));
	}

	static Block init(Block block){
		GameRegistry.registerBlock(block,block.getUnlocalizedName().substring("tile.".length(), block.getUnlocalizedName().length()));
		return block;
	}
	
	public static void oresInit(){
		IronLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1).setBlockName("IronLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "IronLevel2"));
		IronLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1).setBlockName("IronLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "IronLevel3"));
		IronLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2).setBlockName("IronLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "IronLevel4"));
		IronLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2).setBlockName("IronLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "IronLevel5"));
		SuperDuperIron=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3).setBlockName("SuperDuperIron").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperIron"));
		
		GoldLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1).setBlockName("GoldLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "GoldLevel2"));
		GoldLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1).setBlockName("GoldLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "GoldLevel3"));
		GoldLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2).setBlockName("GoldLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "GoldLevel4"));
		GoldLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2).setBlockName("GoldLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "GoldLevel5"));
		SuperDuperGold=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3).setBlockName("SuperDuperGold").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperGold"));
		
		DiamondLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1).setBlockName("DiamondLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "DiamondLevel2"));
		DiamondLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1).setBlockName("DiamondLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "DiamondLevel3"));
		DiamondLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2).setBlockName("DiamondLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "DiamondLevel4"));
		DiamondLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2).setBlockName("DiamondLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "DiamondLevel5"));
		SuperDuperDiamond=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3).setBlockName("SuperDuperDiamond").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperDiamond"));
		
		LapisLazuliLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1).setBlockName("LapisLazuliLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "LapisLazuliLevel2"));
		LapisLazuliLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1).setBlockName("LapisLazuliLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "LapisLazuliLevel3"));
		LapisLazuliLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2).setBlockName("LapisLazuliLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "LapisLazuliLevel4"));
		LapisLazuliLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2).setBlockName("LapisLazuliLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "LapisLazuliLevel5"));
		SuperDuperLapisLazuli=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3).setBlockName("SuperDuperLapisLazuli").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperLapisLazuli"));
		
		RedstoneLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1).setBlockName("RedstoneLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "RedstoneLevel2"));
		RedstoneLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1).setBlockName("RedstoneLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "RedstoneLevel3"));
		RedstoneLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2).setBlockName("RedstoneLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "RedstoneLevel4"));
		RedstoneLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2).setBlockName("RedstoneLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "RedstoneLevel5"));
		SuperDuperRedstone=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3).setBlockName("SuperDuperRedstone").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperRedstone"));
		
		EmeraldLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1).setBlockName("EmeraldLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "EmeraldLevel2"));
		EmeraldLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1).setBlockName("EmeraldLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "EmeraldLevel3"));
		EmeraldLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2).setBlockName("EmeraldLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "EmeraldLevel4"));
		EmeraldLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2).setBlockName("EmeraldLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "EmeraldLevel5"));
		SuperDuperEmerald=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3).setBlockName("SuperDuperEmerald").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperEmerald"));
		
		NetherQuartzLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1).setBlockName("NetherQuartzLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "NetherQuartzLevel2"));
		NetherQuartzLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1).setBlockName("NetherQuartzLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "NetherQuartzLevel3"));
		NetherQuartzLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2).setBlockName("NetherQuartzLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "NetherQuartzLevel4"));
		NetherQuartzLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2).setBlockName("NetherQuartzLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "NetherQuartzLevel5"));
		SuperDuperNetherQuartz=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3).setBlockName("SuperDuperNetherQuartz").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperNetherQuartz"));
		
		CoalLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1).setBlockName("CoalLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "CoalLevel2"));
		CoalLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1).setBlockName("CoalLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "CoalLevel3"));
		CoalLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2).setBlockName("CoalLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "CoalLevel4"));
		CoalLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2).setBlockName("CoalLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "CoalLevel5"));
		SuperDuperCoal=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3).setBlockName("SuperDuperCoal").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperCoal"));
	}
	
	
	
	
}
