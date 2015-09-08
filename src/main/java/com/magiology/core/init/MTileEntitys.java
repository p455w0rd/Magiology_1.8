package com.magiology.core.init;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.magiology.mcobjects.tileentityes.TileEntityBFCPowerOut;
import com.magiology.mcobjects.tileentityes.TileEntityBedrockBreaker;
import com.magiology.mcobjects.tileentityes.TileEntityBigFurnaceCore;
import com.magiology.mcobjects.tileentityes.TileEntityControlBlock;
import com.magiology.mcobjects.tileentityes.TileEntityDontLookAtMe;
import com.magiology.mcobjects.tileentityes.TileEntityEnergizedLapisOre;
import com.magiology.mcobjects.tileentityes.TileEntityFireExhaust;
import com.magiology.mcobjects.tileentityes.TileEntityFireGun;
import com.magiology.mcobjects.tileentityes.TileEntityFireLamp;
import com.magiology.mcobjects.tileentityes.TileEntityFireMatrixReceaver;
import com.magiology.mcobjects.tileentityes.TileEntityFireMatrixTransferer;
import com.magiology.mcobjects.tileentityes.TileEntityFirePipe;
import com.magiology.mcobjects.tileentityes.TileEntityOreStructureCore;
import com.magiology.mcobjects.tileentityes.TileEntityPLauncher;
import com.magiology.mcobjects.tileentityes.TileEntityRareSpacePipe;
import com.magiology.mcobjects.tileentityes.TileEntityRemotePowerCounter;
import com.magiology.mcobjects.tileentityes.TileEntitySmartCrafter;
import com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL1;
import com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL100;
import com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL2;
import com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL3;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkConductor;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkInterface;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkPointerContainer;
import com.magiology.render.tilerender.RenderBFCPowerOut;
import com.magiology.render.tilerender.RenderBateryL1;
import com.magiology.render.tilerender.RenderBateryL100;
import com.magiology.render.tilerender.RenderBateryL2;
import com.magiology.render.tilerender.RenderBateryL3;
import com.magiology.render.tilerender.RenderBedrockBreaker;
import com.magiology.render.tilerender.RenderEnergizedLapisOre;
import com.magiology.render.tilerender.RenderFireExhaust;
import com.magiology.render.tilerender.RenderFireGun;
import com.magiology.render.tilerender.RenderFireLamp;
import com.magiology.render.tilerender.RenderFireMatrixReceaver;
import com.magiology.render.tilerender.RenderFireMatrixTransferer;
import com.magiology.render.tilerender.RenderFirePipe;
import com.magiology.render.tilerender.RenderOreStructureCore;
import com.magiology.render.tilerender.RenderRemotePowerCounter;
import com.magiology.render.tilerender.hologram.RenderHologramProjector;
import com.magiology.render.tilerender.network.RenderNetworkConductor;
import com.magiology.render.tilerender.network.RenderNetworkController;
import com.magiology.render.tilerender.network.RenderNetworkInterface;
import com.magiology.render.tilerender.network.RenderNetworkPointerContainer;
import com.magiology.util.utilobjects.m_extension.TileEntityM;

public class MTileEntitys{

	public static void init(){
		register(TileEntityFireLamp.class);
		register(TileEntityOreStructureCore.class);
		register(TileEntityFirePipe.class);
		register(TileEntityFireGun.class);
		register(TileEntityFireExhaust.class);
		register(TileEntityBedrockBreaker.class);
		register(TileEntityBigFurnaceCore.class);
		register(TileEntityBateryL1.class);
		register(TileEntityBateryL2.class);
		register(TileEntityBateryL3.class);
		register(TileEntityBateryL100.class);
		register(TileEntityPLauncher.class);
		register(TileEntityBFCPowerOut.class);
		register(TileEntityEnergizedLapisOre.class);
		register(TileEntityDontLookAtMe.class);
		register(TileEntityControlBlock.class);
		register(TileEntityRemotePowerCounter.class);
		register(TileEntityFireMatrixTransferer.class);
		register(TileEntityFireMatrixReceaver.class);
		register(TileEntitySmartCrafter.class);
		register(TileEntityHologramProjector.class);
		register(TileEntityNetworkController.class);
		register(TileEntityNetworkConductor.class);
		register(TileEntityNetworkInterface.class);
		register(TileEntityNetworkPointerContainer.class);
		register(TileEntityRareSpacePipe.class);
	}
	public static void initRenders(){
		bindTileWRender(TileEntityFireLamp.class,               new RenderFireLamp());
		bindTileWRender(TileEntityOreStructureCore.class,       new RenderOreStructureCore());
		bindTileWRender(TileEntityFirePipe.class,               new RenderFirePipe());
		bindTileWRender(TileEntityFireGun.class,                new RenderFireGun());
		bindTileWRender(TileEntityFireExhaust.class,            new RenderFireExhaust());
		bindTileWRender(TileEntityBedrockBreaker.class,         new RenderBedrockBreaker());
		bindTileWRender(TileEntityBateryL1.class,               new RenderBateryL1());
		bindTileWRender(TileEntityBateryL2.class,               new RenderBateryL2());
		bindTileWRender(TileEntityBateryL3.class,               new RenderBateryL3());
		bindTileWRender(TileEntityBateryL100.class,             new RenderBateryL100());
		bindTileWRender(TileEntityBFCPowerOut.class,            new RenderBFCPowerOut());
		bindTileWRender(TileEntityEnergizedLapisOre.class,      new RenderEnergizedLapisOre());
		bindTileWRender(TileEntityRemotePowerCounter.class,     new RenderRemotePowerCounter());
		bindTileWRender(TileEntityFireMatrixTransferer.class,   new RenderFireMatrixTransferer());
		bindTileWRender(TileEntityFireMatrixReceaver.class,     new RenderFireMatrixReceaver());
		bindTileWRender(TileEntityHologramProjector.class,      new RenderHologramProjector());
		bindTileWRender(TileEntityNetworkConductor.class,       new RenderNetworkConductor());
		bindTileWRender(TileEntityNetworkController.class,      new RenderNetworkController());
		bindTileWRender(TileEntityNetworkInterface.class,       new RenderNetworkInterface());
		bindTileWRender(TileEntityNetworkPointerContainer.class,new RenderNetworkPointerContainer());
		bindTileWRender(TileEntityRareSpacePipe.class,          new RenderNetworkConductor());
	}
	private static<T extends TileEntityM> void register(Class<T> clazz){
		String name=clazz.getSimpleName().substring("TileEntity".length());
		GameRegistry.registerTileEntity(clazz, "TE"+name);
	}
	public static void bindTileWRender(Class<?extends TileEntity>tileEntityClass, TileEntitySpecialRenderer specialRenderer){
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass,specialRenderer);
	}
	public static void setCustomRenderers(){
//		RenderFireLampISBRH a=new RenderFireLampISBRH(RenderingRegistry.getNextAvailableRenderId());
//		ISBRH.registerBlockRender(MBlocks.FireLamp, a);
//		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MBlocks.FireLamp),a);
		
	}

}
