package com.magiology.core.init;

import com.magiology.core.MReference;
import com.magiology.mcobjects.blocks.BFCPowerOut;
import com.magiology.mcobjects.blocks.BateryGeneric;
import com.magiology.mcobjects.blocks.BedrockBreaker;
import com.magiology.mcobjects.blocks.BigFurnaceCore;
import com.magiology.mcobjects.blocks.ControlBlock;
import com.magiology.mcobjects.blocks.DontLookAtMe;
import com.magiology.mcobjects.blocks.EnergizedLapisOre;
import com.magiology.mcobjects.blocks.FakeAir;
import com.magiology.mcobjects.blocks.HologramProjector;
import com.magiology.mcobjects.blocks.OreLevelX;
import com.magiology.mcobjects.blocks.OreStructureCore;
import com.magiology.mcobjects.blocks.ParticleLauncher;
import com.magiology.mcobjects.blocks.PileODust;
import com.magiology.mcobjects.blocks.RareSpacePipe;
import com.magiology.mcobjects.blocks.RemotePowerCounter;
import com.magiology.mcobjects.blocks.SmartCrafter;
import com.magiology.mcobjects.blocks.fire.FireExhaust;
import com.magiology.mcobjects.blocks.fire.FireGun;
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
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
		FireMatrixTransferer,FireMatrixReceaver,smartCrafter,hologramProjector,rareSpacePipe,
		networkController,networkConductor,networkPointerContainer,networkInterface,networkCommandCenter;

	public static void preInit(){
		blocksInit();
		oresInit();
	}

	public static void blocksInit(){
		smartCrafter=init(new SmartCrafter().setUnlocalizedName("smartCrafter").setCreativeTab(MCreativeTabs.Whwmmt_core).setStepSound(Block.soundTypeMetal));
		FireMatrixTransferer=init(new FireMatrixTransferer().setUnlocalizedName("FireMatrixTransferer").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeStone));
		FireMatrixReceaver=init(new FireMatrixReceaver().setUnlocalizedName("FireMatrixReceaver").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeStone));
		FireLamp=init(new FireLamp().setUnlocalizedName("FireLamp").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeGlass));
		ControlBlock=init(new ControlBlock().setUnlocalizedName("ControlBlock").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeGlass));
		RemotePowerCounter=init(new RemotePowerCounter().setUnlocalizedName("RemotePowerCounter").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeStone));
		FireGun=init(new FireGun().setUnlocalizedName("FireGun").setStepSound(Block.soundTypeStone));
		FirePipe=init(new FirePipe().setBlockTextureName(MReference.MODID + ":" + "FirePipeIcon").setUnlocalizedName("FirePipe").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		FireExhaust=init(new FireExhaust().setUnlocalizedName("FireExhaust").setStepSound(Block.soundTypeStone));
		FakeAir=init(new FakeAir().setUnlocalizedName("FakeAir"));
		OreStructureCore=init(new OreStructureCore().setUnlocalizedName("OreStructureCore").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeGlass));
		BigFurnaceCore=init(new BigFurnaceCore().setBlockTextureName("stonebrick").setStepSound(Block.soundTypeMetal).setUnlocalizedName("BigFurnaceCore").setCreativeTab(MCreativeTabs.Whwmmt_power));
		particleLauncher=init(new ParticleLauncher().setBlockTextureName("iron_block").setUnlocalizedName("particleLauncher").setCreativeTab(MCreativeTabs.Whwmmt_core).setStepSound(Block.soundTypeMetal));
		BatteryL1=init(new BateryGeneric(1).setUnlocalizedName("BatteryL1").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		BatteryL2=init(new BateryGeneric(2).setUnlocalizedName("BatteryL2").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		BatteryL3=init(new BateryGeneric(3).setUnlocalizedName("BatteryL3").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		BateryL100=init(new BateryGeneric(100).setUnlocalizedName("BateryL100").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		BFCPowerOut=init(new BFCPowerOut().setUnlocalizedName("BFCPowerOut").setStepSound(Block.soundTypeMetal));
		bedrockBreaker=init(new BedrockBreaker().setUnlocalizedName("bedrockBreaker").setCreativeTab(MCreativeTabs.Whwmmt_power).setStepSound(Block.soundTypeMetal));
		PileODust=init(new PileODust().setBlockTextureName(MReference.MODID + ":" + "IronLevel2").setUnlocalizedName("PileODust").setCreativeTab(MCreativeTabs.Whwmmt_core).setStepSound(Block.soundTypeMetal));
		DontLookAtMe=init(new DontLookAtMe().setBlockTextureName(MReference.MODID + ":" + "nista16x16").setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("DontLookAtMe"));
		energizedLapisOre=init(new EnergizedLapisOre().setBlockTextureName("stone").setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("energizedLapisOre"));
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
	
	public static void oresInit(){
		IronLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1, "IronLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "IronLevel2"));
		IronLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1, "IronLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "IronLevel3"));
		IronLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2, "IronLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "IronLevel4"));
		IronLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2, "IronLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "IronLevel5"));
		SuperDuperIron=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3, "SuperDuperIron").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperIron"));
		
		GoldLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1, "GoldLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "GoldLevel2"));
		GoldLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1, "GoldLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "GoldLevel3"));
		GoldLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2, "GoldLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "GoldLevel4"));
		GoldLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2, "GoldLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "GoldLevel5"));
		SuperDuperGold=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3, "SuperDuperGold").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperGold"));
		
		DiamondLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1, "DiamondLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "DiamondLevel2"));
		DiamondLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1, "DiamondLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "DiamondLevel3"));
		DiamondLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2, "DiamondLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "DiamondLevel4"));
		DiamondLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2, "DiamondLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "DiamondLevel5"));
		SuperDuperDiamond=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3, "SuperDuperDiamond").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperDiamond"));
		
		LapisLazuliLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1, "LapisLazuliLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "LapisLazuliLevel2"));
		LapisLazuliLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1, "LapisLazuliLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "LapisLazuliLevel3"));
		LapisLazuliLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2, "LapisLazuliLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "LapisLazuliLevel4"));
		LapisLazuliLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2, "LapisLazuliLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "LapisLazuliLevel5"));
		SuperDuperLapisLazuli=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3, "SuperDuperLapisLazuli").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperLapisLazuli"));
		
		RedstoneLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1, "RedstoneLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "RedstoneLevel2"));
		RedstoneLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1, "RedstoneLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "RedstoneLevel3"));
		RedstoneLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2, "RedstoneLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "RedstoneLevel4"));
		RedstoneLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2, "RedstoneLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "RedstoneLevel5"));
		SuperDuperRedstone=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3, "SuperDuperRedstone").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperRedstone"));
		
		EmeraldLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1, "EmeraldLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "EmeraldLevel2"));
		EmeraldLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1, "EmeraldLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "EmeraldLevel3"));
		EmeraldLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2, "EmeraldLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "EmeraldLevel4"));
		EmeraldLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2, "EmeraldLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "EmeraldLevel5"));
		SuperDuperEmerald=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3, "SuperDuperEmerald").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperEmerald"));
		
		NetherQuartzLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1, "NetherQuartzLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "NetherQuartzLevel2"));
		NetherQuartzLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1, "NetherQuartzLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "NetherQuartzLevel3"));
		NetherQuartzLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2, "NetherQuartzLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "NetherQuartzLevel4"));
		NetherQuartzLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2, "NetherQuartzLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "NetherQuartzLevel5"));
		SuperDuperNetherQuartz=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3, "SuperDuperNetherQuartz").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperNetherQuartz"));
		
		CoalLevel2=init(new OreLevelX(Material.ground, 0.1, 2, "pickaxe", 1, "CoalLevel2").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "CoalLevel2"));
		CoalLevel3=init(new OreLevelX(Material.ground, 0.2, 4, "pickaxe", 1, "CoalLevel3").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "CoalLevel3"));
		CoalLevel4=init(new OreLevelX(Material.ground, 0.3, 8, "pickaxe", 2, "CoalLevel4").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "CoalLevel4"));
		CoalLevel5=init(new OreLevelX(Material.ground, 0.4, 16, "pickaxe", 2, "CoalLevel5").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "CoalLevel5"));
		SuperDuperCoal=init(new OreLevelX(Material.ground, 0.5, 32, "pickaxe", 3, "SuperDuperCoal").setCreativeTab(MCreativeTabs.Whwmmt_ores).setBlockTextureName(MReference.MODID + ":" + "SuperDuperCoal"));
	}
	
	
	
	
}
