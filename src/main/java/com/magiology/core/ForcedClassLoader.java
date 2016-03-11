package com.magiology.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.util.utilclasses.FileUtil;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM;


public class ForcedClassLoader{
	
	private static String startPath="..\\src\\main\\java\\";
	private static int startPathLength=startPath.length();
	
	public static void load(){
		
		generateAndInject();
		
		ClassLoader loader=ForcedClassLoader.class.getClassLoader();
		PrintUtil.println("Starting to load all classes from "+MReference.NAME+"!");
		UtilM.startTime();
		List<String> failed=new ArrayList<>();
		for(String clazz:classes){
			try{
				loader.loadClass(clazz);
			}catch(Exception e){
				if(!ArrayUtils.contains(blacklist, clazz))failed.add(e.getClass().getSimpleName()+": "+clazz);
			}
		}
		PrintUtil.println("Loading of all classes is done in "+UtilM.endTime()+"ms.");
		if(failed.isEmpty())PrintUtil.println("No classes have failed to load! ^_^");
		else{
			PrintUtil.println("But some classes have failed to load!");
			PrintUtil.println("This is not fatal or a big deal but it coud cause some problems in rare cases.");
			PrintUtil.println("Failed class list:");
			for(String string:failed)PrintUtil.println(string);
			PrintUtil.println("You may want to refresh the class list!");
		}
	}
	
	private static void generateAndInject(){
		try{
			File thisClass=new File(startPath+getPathToThis());
			
			if(!thisClass.exists()||!thisClass.isFile())return;
			
			List<String> fileNames=getFileNames(new ArrayList<>(), new File(startPath+"com\\magiology").toPath());
			
			
			List<String> beforeList=new ArrayList<>(),afterList=new ArrayList<>();
			String tabs="\t";
			BufferedReader br=new BufferedReader(new FileReader(thisClass));
			
			StringBuilder originalFile1=new StringBuilder();
			
			boolean listStarted=false,listEnded=false;
			{
				String line=null;
				while((line=br.readLine())!=null){
					originalFile1.append(line).append("\n");
					if(!listStarted){
						beforeList.add(line);
						if(line.endsWith("classes={")){
							listStarted=true;
							int tabCount=0;
							while(line.length()>=tabCount&&line.charAt(tabCount)=='\t'){
								tabCount++;
								tabs+="\t";
							}
						}
					}
					if(!listEnded){
						if(line.endsWith("};"))listEnded=true;
					}
					if(listEnded){
						afterList.add(line);
					}
					
				}
			}
			br.close();
			
			String originalFile=originalFile1.toString();
			
			if(afterList.get(afterList.size()-1).isEmpty())afterList.remove(afterList.size()-1);
			
			final StringBuilder newFile=new StringBuilder();
			
			beforeList.forEach(line->newFile.append(line).append("\n"));
			
			for(int i=0;i<fileNames.size();i++){
				String line=fileNames.get(i);
				if(i!=fileNames.size()-1)line+=",";
				char[] line1=line.toCharArray();
				
				newFile.append(tabs);
				for(int j=0;j<line1.length;j++){
					if(line1[j]=='\\')newFile.append('.');
					else newFile.append(line1[j]);
				}
				newFile.append("\n");
			}
			
			afterList.forEach(line->newFile.append(line).append("\n"));
			
			if(!newFile.equals(originalFile)){
				FileUtil.setFileTxt(thisClass, newFile.toString());
				UtilM.exit(404);
			}
			
		}catch(IOException e){
		}
	}
	private static String getPathToThis(){
	    return ForcedClassLoader.class.getName().replace(".", "/") + ".java";
	}
	
	private static List<String> getFileNames(List<String> fileNames, Path dir){
		try(DirectoryStream<Path> stream=Files.newDirectoryStream(dir)){
			for(Path path:stream){
				if(path.toFile().isDirectory()){
					getFileNames(fileNames, path);
				}else{
					String path1=path.toString();
					if(path1.endsWith(".java"))fileNames.add('"'+path1.substring(startPathLength, path1.length()-5)+"\"");
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return fileNames;
	} 
	
	private static String[] 
		blacklist={
				"com.magiology.client.render.itemrender.ItemRendererFirePipe",
				"com.magiology.client.render.itemrender.ItemRendererGenericUpgrade",
				"com.magiology.client.render.itemrender.ItemRendererHelmet42",
				"com.magiology.client.render.itemrender.ItemRendererPants42",
				"com.magiology.client.render.itemrender.ItemRendererPowerCounter",
				"com.magiology.client.render.tilerender.isbhrrender.ISBRH",
				"com.magiology.client.render.tilerender.isbhrrender.RenderFireLampISBRH"
		},
		classes={
			"com.magiology.api.connection.Connection",
			"com.magiology.api.connection.ConnectionType",
			"com.magiology.api.connection.IConnection",
			"com.magiology.api.connection.IConnectionFactory",
			"com.magiology.api.connection.IConnectionProvider",
			"com.magiology.api.lang.bridge.CommandInteractorList",
			"com.magiology.api.lang.bridge.InterfaceWrapper",
			"com.magiology.api.lang.bridge.NetworkProgramHolderWrapper",
			"com.magiology.api.lang.bridge.WorldWrapper",
			"com.magiology.api.lang.ICommandInteract",
			"com.magiology.api.lang.JSProgramContainer",
			"com.magiology.api.lang.program.ProgramCommon",
			"com.magiology.api.lang.program.ProgramDataBase",
			"com.magiology.api.lang.program.ProgramSerializable",
			"com.magiology.api.lang.program.ProgramUsable",
			"com.magiology.api.network.BasicWorldNetworkInterface",
			"com.magiology.api.network.interfaces.registration.DefaultInterface",
			"com.magiology.api.network.interfaces.registration.InterfaceBinder",
			"com.magiology.api.network.interfaces.registration.InterfaceRegistration",
			"com.magiology.api.network.InterfaceTileEntitySaver",
			"com.magiology.api.network.ISidedNetworkComponent",
			"com.magiology.api.network.Messageable",
			"com.magiology.api.network.NetworkBaseComponent",
			"com.magiology.api.network.NetworkInteractBaseComponent",
			"com.magiology.api.network.NetworkInterface",
			"com.magiology.api.network.Redstone",
			"com.magiology.api.network.skeleton.TileEntityNetwork",
			"com.magiology.api.network.skeleton.TileEntityNetworkInteract",
			"com.magiology.api.network.skeleton.TileEntityNetworkPow",
			"com.magiology.api.network.WorldNetworkInterface",
			"com.magiology.api.power.ISidedPower",
			"com.magiology.api.power.PowerCore",
			"com.magiology.api.power.PowerProducer",
			"com.magiology.api.power.PowerUpgrades",
			"com.magiology.api.power.SixSidedBoolean",
			"com.magiology.api.SavableData",
			"com.magiology.api.updateable.Updater",
			"com.magiology.client.gui.container.ArmorContainer",
			"com.magiology.client.gui.container.CommandCenterContainer",
			"com.magiology.client.gui.container.ContainerEmpty",
			"com.magiology.client.gui.container.ControlBockContainer",
			"com.magiology.client.gui.container.GuiObjectCustomizeContainer",
			"com.magiology.client.gui.container.ISidedPowerInstructorContainer",
			"com.magiology.client.gui.container.UpgradeContainer",
			"com.magiology.client.gui.custom.DownloadingIcon",
			"com.magiology.client.gui.custom.guiparticels.GuiStandardFX",
			"com.magiology.client.gui.custom.hud.FakeMessageHUD",
			"com.magiology.client.gui.custom.hud.HandModeChangerHUD",
			"com.magiology.client.gui.custom.hud.HUD",
			"com.magiology.client.gui.custom.hud.MainMenuUpdateNotificationHUD",
			"com.magiology.client.gui.custom.hud.SoulFlameHUD",
			"com.magiology.client.gui.custom.hud.StatsDisplayHUD",
			"com.magiology.client.gui.custom.hud.WingModeChangerHUD",
			"com.magiology.client.gui.custom.OnOffGuiButton",
			"com.magiology.client.gui.gui.CraftingGridWOutput",
			"com.magiology.client.gui.gui.GuiArmor",
			"com.magiology.client.gui.gui.GuiCenterContainer",
			"com.magiology.client.gui.gui.GuiContainerAndGuiParticles",
			"com.magiology.client.gui.gui.GuiControlBock",
			"com.magiology.client.gui.gui.GuiHologramProjectorMain",
			"com.magiology.client.gui.gui.GuiHoloObjectEditor",
			"com.magiology.client.gui.gui.GuiISidedPowerInstructor",
			"com.magiology.client.gui.gui.GuiJSProgramEditor",
			"com.magiology.client.gui.gui.GuiUpgrade",
			"com.magiology.client.gui.GuiUpdater",
			"com.magiology.client.gui.guiutil.container.ControlBockContainerSlot",
			"com.magiology.client.gui.guiutil.container.CustomSlot",
			"com.magiology.client.gui.guiutil.container.FakeContainer",
			"com.magiology.client.gui.guiutil.container.OnlyShiftClickSlot",
			"com.magiology.client.gui.guiutil.container.UpgItemContainer",
			"com.magiology.client.gui.guiutil.container.UpgItemSlot",
			"com.magiology.client.gui.guiutil.gui.buttons.CleanButton",
			"com.magiology.client.gui.guiutil.gui.buttons.ColoredGuiButton",
			"com.magiology.client.gui.guiutil.gui.buttons.CustomButton",
			"com.magiology.client.gui.guiutil.gui.buttons.InvisivleGuiButton",
			"com.magiology.client.gui.guiutil.gui.buttons.TexturedColoredButton",
			"com.magiology.client.gui.guiutil.gui.ColorSlider",
			"com.magiology.client.gui.guiutil.gui.CraftingGridSlot",
			"com.magiology.client.gui.guiutil.gui.DrawThatSexyDotHelper",
			"com.magiology.client.gui.guiutil.gui.GuiJavaScriptEditor",
			"com.magiology.client.gui.guiutil.gui.GuiSlider",
			"com.magiology.client.gui.guiutil.gui.GuiTextEditor",
			"com.magiology.client.render.aftereffect.AfterRenderRenderer",
			"com.magiology.client.render.aftereffect.LongAfterRenderRenderer",
			"com.magiology.client.render.aftereffect.LongAfterRenderRendererBase",
			"com.magiology.client.render.aftereffect.RenderFirePipeGlow",
			"com.magiology.client.render.aftereffect.RenderFirePipePriorityCube",
			"com.magiology.client.render.aftereffect.RenderNetworkPointerContainerHighlight",
			"com.magiology.client.render.aftereffect.TwoDotsLineRender",
			"com.magiology.client.render.entityrender.BallOfEnergyRenderer",
			"com.magiology.client.render.font.FontRendererMBase",
			"com.magiology.client.render.font.FontRendererMClipped",
			"com.magiology.client.render.itemrender.ItemRendererFirePipe",
			"com.magiology.client.render.itemrender.ItemRendererGenericUpgrade",
			"com.magiology.client.render.itemrender.ItemRendererHelmet42",
			"com.magiology.client.render.itemrender.ItemRendererPants42",
			"com.magiology.client.render.itemrender.ItemRendererPowerCounter",
			"com.magiology.client.render.itemrender.ItemRendererTheHand",
			"com.magiology.client.render.models.entitys.BallOfEnergyModel",
			"com.magiology.client.render.models.ModelWingsFromTheBlackFire",
			"com.magiology.client.render.models.SimpleCube",
			"com.magiology.client.render.shaders.BlurRenderer",
			"com.magiology.client.render.shaders.ColorCutRenderer",
			"com.magiology.client.render.shaders.ColorRenderer",
			"com.magiology.client.render.shaders.core.ShaderAspectRenderer",
			"com.magiology.client.render.shaders.core.ShaderRunner",
			"com.magiology.client.render.Textures",
			"com.magiology.client.render.tilerender.isbhrrender.ISBRH",
			"com.magiology.client.render.tilerender.isbhrrender.RenderFireLampISBRH",
			"com.magiology.client.render.tilerender.network.RenderNetworkConductor",
			"com.magiology.client.render.tilerender.network.RenderNetworkController",
			"com.magiology.client.render.tilerender.network.RenderNetworkInterface",
			"com.magiology.client.render.tilerender.network.RenderNetworkRouter",
			"com.magiology.client.render.tilerender.RenderBateryL1",
			"com.magiology.client.render.tilerender.RenderBateryL100",
			"com.magiology.client.render.tilerender.RenderBateryL2",
			"com.magiology.client.render.tilerender.RenderBateryL3",
			"com.magiology.client.render.tilerender.RenderBFCPowerOut",
			"com.magiology.client.render.tilerender.RenderFireLamp",
			"com.magiology.client.render.tilerender.RenderFireMatrixReceaver",
			"com.magiology.client.render.tilerender.RenderFireMatrixTransferer",
			"com.magiology.client.render.tilerender.RenderFirePipe",
			"com.magiology.client.render.tilerender.RenderHologramProjector",
			"com.magiology.client.render.tilerender.RenderMultiBox",
			"com.magiology.core.Config",
			"com.magiology.core.ForcedClassLoader",
			"com.magiology.core.init.MBlocks",
			"com.magiology.core.init.MCreativeTabs",
			"com.magiology.core.init.MEntitys",
			"com.magiology.core.init.MEvents",
			"com.magiology.core.init.MGui",
			"com.magiology.core.init.MInterfaces",
			"com.magiology.core.init.MItems",
			"com.magiology.core.init.MPackets",
			"com.magiology.core.init.MRecepies",
			"com.magiology.core.init.MTileEntitys",
			"com.magiology.core.Magiology",
			"com.magiology.core.MReference",
			"com.magiology.core.MUpdater",
			"com.magiology.forgepowered.events.client.CustomRenderedItem",
			"com.magiology.forgepowered.events.client.FirstPersonItemRederer",
			"com.magiology.forgepowered.events.client.HighlightEvent",
			"com.magiology.forgepowered.events.client.MouseEvents",
			"com.magiology.forgepowered.events.client.RenderEvents",
			"com.magiology.forgepowered.events.EntityEvents",
			"com.magiology.forgepowered.events.ForcePipeUpdate",
			"com.magiology.forgepowered.events.GameEvents",
			"com.magiology.forgepowered.events.GenericPacketEvents",
			"com.magiology.forgepowered.events.SpecialMovmentEvents",
			"com.magiology.forgepowered.events.SpecialPlayerParicleHandler",
			"com.magiology.forgepowered.events.TickEvents",
			"com.magiology.forgepowered.packets.core.AbstractPacket",
			"com.magiology.forgepowered.packets.core.AbstractToClientMessage",
			"com.magiology.forgepowered.packets.core.AbstractToServerMessage",
			"com.magiology.forgepowered.packets.packets.ClickHologramPacket",
			"com.magiology.forgepowered.packets.packets.generic.GenericServerIntPacket",
			"com.magiology.forgepowered.packets.packets.generic.GenericServerStringPacket",
			"com.magiology.forgepowered.packets.packets.generic.GenericServerVoidPacket",
			"com.magiology.forgepowered.packets.packets.HologramProjectorUpload",
			"com.magiology.forgepowered.packets.packets.HoloObjectUploadPacket",
			"com.magiology.forgepowered.packets.packets.NotifyPointedBoxChangePacket",
			"com.magiology.forgepowered.packets.packets.OpenGuiPacket",
			"com.magiology.forgepowered.packets.packets.OpenProgramContainerInGui",
			"com.magiology.forgepowered.packets.packets.RightClickBlockPacket",
			"com.magiology.forgepowered.packets.packets.SavableDataWithKeyPacket",
			"com.magiology.forgepowered.packets.packets.SendPlayerDataPacket",
			"com.magiology.forgepowered.packets.packets.TileRedstone",
			"com.magiology.forgepowered.packets.packets.UploadPlayerDataPacket",
			"com.magiology.forgepowered.proxy.ClientProxy",
			"com.magiology.forgepowered.proxy.CommonProxy",
			"com.magiology.forgepowered.proxy.ServerProxy",
			"com.magiology.handlers.animationhandlers.thehand.HandAnimation",
			"com.magiology.handlers.animationhandlers.thehand.HandData",
			"com.magiology.handlers.animationhandlers.thehand.HandPosition",
			"com.magiology.handlers.animationhandlers.thehand.TheHandHandler",
			"com.magiology.handlers.animationhandlers.WingsFromTheBlackFireHandler",
			"com.magiology.handlers.crafting.CustomCrafterRegistry",
			"com.magiology.handlers.EnhancedRobot",
			"com.magiology.handlers.GenericPacketEventHandler",
			"com.magiology.handlers.GuiHandlerM",
			"com.magiology.handlers.KeyHandler",
			"com.magiology.handlers.MyWorldData",
			"com.magiology.handlers.obj.handler.revived.yayformc1_8.AdvancedModelLoader",
			"com.magiology.handlers.obj.handler.revived.yayformc1_8.IModelCustom",
			"com.magiology.handlers.obj.handler.revived.yayformc1_8.IModelCustomLoader",
			"com.magiology.handlers.obj.handler.revived.yayformc1_8.ModelFormatException",
			"com.magiology.handlers.obj.handler.revived.yayformc1_8.obj.Face",
			"com.magiology.handlers.obj.handler.revived.yayformc1_8.obj.GroupObject",
			"com.magiology.handlers.obj.handler.revived.yayformc1_8.obj.ObjModelLoader",
			"com.magiology.handlers.obj.handler.revived.yayformc1_8.obj.TextureCoordinate",
			"com.magiology.handlers.obj.handler.revived.yayformc1_8.obj.Vertex",
			"com.magiology.handlers.obj.handler.revived.yayformc1_8.obj.WavefrontObject",
			"com.magiology.handlers.obj.handler.revived.yayformc1_8.techne.TechneModel",
			"com.magiology.handlers.obj.handler.revived.yayformc1_8.techne.TechneModelLoader",
			"com.magiology.handlers.ParticleHandler",
			"com.magiology.handlers.PlayerClothPhysiscHandeler",
			"com.magiology.handlers.PremiumHandeler",
			"com.magiology.handlers.web.DownloadingHandler",
			"com.magiology.handlers.web.MediaFireDownlader",
			"com.magiology.io.IOReadableMap",
			"com.magiology.io.WorldData",
			"com.magiology.io.ZipManager",
			"com.magiology.mcobjects.blocks.BateryGeneric",
			"com.magiology.mcobjects.blocks.BFCPowerOut",
			"com.magiology.mcobjects.blocks.BigFurnaceCore",
			"com.magiology.mcobjects.blocks.BlockContainerMultiColision",
			"com.magiology.mcobjects.blocks.ControlBlock",
			"com.magiology.mcobjects.blocks.fire.FireLamp",
			"com.magiology.mcobjects.blocks.fire.FireMatrixReceaver",
			"com.magiology.mcobjects.blocks.fire.FireMatrixTransferer",
			"com.magiology.mcobjects.blocks.fire.FirePipe",
			"com.magiology.mcobjects.blocks.HologramProjector",
			"com.magiology.mcobjects.blocks.JSProgrammer",
			"com.magiology.mcobjects.blocks.network.NetworkCommandHolder",
			"com.magiology.mcobjects.blocks.network.NetworkConductor",
			"com.magiology.mcobjects.blocks.network.NetworkController",
			"com.magiology.mcobjects.blocks.network.NetworkInterface",
			"com.magiology.mcobjects.blocks.network.NetworkRouter",
			"com.magiology.mcobjects.blocks.RareSpacePipe",
			"com.magiology.mcobjects.effect.EntityBaseFX",
			"com.magiology.mcobjects.effect.EntityCustomfireFX",
			"com.magiology.mcobjects.effect.EntityFacedFX",
			"com.magiology.mcobjects.effect.EntityFollowingBubleFX",
			"com.magiology.mcobjects.effect.EntityFXM",
			"com.magiology.mcobjects.effect.EntityFXMDeprecated",
			"com.magiology.mcobjects.effect.EntityMovingParticleFX",
			"com.magiology.mcobjects.effect.EntitySmoothBubleFX",
			"com.magiology.mcobjects.effect.EntitySparkFX",
			"com.magiology.mcobjects.effect.GuiParticle",
			"com.magiology.mcobjects.entitys.ClientFakePlayer",
			"com.magiology.mcobjects.entitys.ComplexPlayerRenderingData",
			"com.magiology.mcobjects.entitys.EntityBallOfEnergy",
			"com.magiology.mcobjects.entitys.ExtendedPlayerData",
			"com.magiology.mcobjects.items.armor.CyborgWingsFromTheBlackFireItem",
			"com.magiology.mcobjects.items.FireHammer",
			"com.magiology.mcobjects.items.IPowerSidenessInstructor",
			"com.magiology.mcobjects.items.ItemContainer",
			"com.magiology.mcobjects.items.NetworkPointer",
			"com.magiology.mcobjects.items.PowerCounter",
			"com.magiology.mcobjects.items.ProgramContainer",
			"com.magiology.mcobjects.items.TheHand",
			"com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL1",
			"com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL100",
			"com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL2",
			"com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL3",
			"com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider",
			"com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow",
			"com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPowGen",
			"com.magiology.mcobjects.tileentityes.corecomponents.TileEntityConnectionProvider",
			"com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile",
			"com.magiology.mcobjects.tileentityes.hologram.Button",
			"com.magiology.mcobjects.tileentityes.hologram.Field",
			"com.magiology.mcobjects.tileentityes.hologram.HoloObject",
			"com.magiology.mcobjects.tileentityes.hologram.interactions.AbstractInteraction",
			"com.magiology.mcobjects.tileentityes.hologram.interactions.InteractionColor",
			"com.magiology.mcobjects.tileentityes.hologram.interactions.InteractionName",
			"com.magiology.mcobjects.tileentityes.hologram.interactions.InteractionPosition",
			"com.magiology.mcobjects.tileentityes.hologram.interactions.InteractionScale",
			"com.magiology.mcobjects.tileentityes.hologram.interactions.InteractionSize",
			"com.magiology.mcobjects.tileentityes.hologram.interactions.InteractionSlide",
			"com.magiology.mcobjects.tileentityes.hologram.interactions.InteractionText",
			"com.magiology.mcobjects.tileentityes.hologram.Point",
			"com.magiology.mcobjects.tileentityes.hologram.Slider",
			"com.magiology.mcobjects.tileentityes.hologram.StringContainer",
			"com.magiology.mcobjects.tileentityes.hologram.TextBox",
			"com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector",
			"com.magiology.mcobjects.tileentityes.network.interfaces.TileHologramProjectorInterface",
			"com.magiology.mcobjects.tileentityes.network.TileEntityNetworkConductor",
			"com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController",
			"com.magiology.mcobjects.tileentityes.network.TileEntityNetworkInterface",
			"com.magiology.mcobjects.tileentityes.network.TileEntityNetworkProgramHolder",
			"com.magiology.mcobjects.tileentityes.network.TileEntityNetworkRouter",
			"com.magiology.mcobjects.tileentityes.TileEntityBateryGeneric",
			"com.magiology.mcobjects.tileentityes.TileEntityBFCPowerOut",
			"com.magiology.mcobjects.tileentityes.TileEntityBigFurnaceCore",
			"com.magiology.mcobjects.tileentityes.TileEntityControlBlock",
			"com.magiology.mcobjects.tileentityes.TileEntityFireLamp",
			"com.magiology.mcobjects.tileentityes.TileEntityFireMatrixReceaver",
			"com.magiology.mcobjects.tileentityes.TileEntityFireMatrixTransferer",
			"com.magiology.mcobjects.tileentityes.TileEntityFirePipe",
			"com.magiology.mcobjects.tileentityes.TileEntityRareSpacePipe",
			"com.magiology.registry.events.PlayerWrenchEvent",
			"com.magiology.registry.WrenchRegistry",
			"com.magiology.Sounds",
			"com.magiology.structures.BlockAt",
			"com.magiology.structures.Structure",
			"com.magiology.structures.Structures",
			"com.magiology.structures.SymmetryBoot",
			"com.magiology.util.renderers.GL11DebugUtil",
			"com.magiology.util.renderers.GL11U",
			"com.magiology.util.renderers.glstates.GlState",
			"com.magiology.util.renderers.glstates.GlStateCell",
			"com.magiology.util.renderers.MultiTransfromModel",
			"com.magiology.util.renderers.OpenGLM",
			"com.magiology.util.renderers.PartialTicks1F",
			"com.magiology.util.renderers.Renderer",
			"com.magiology.util.renderers.ShinySurfaceRenderer",
			"com.magiology.util.renderers.tessellatorscripts.CubeModel",
			"com.magiology.util.renderers.tessellatorscripts.NormalCubeModel",
			"com.magiology.util.renderers.tessellatorscripts.PlateModel",
			"com.magiology.util.renderers.tessellatorscripts.SidedModel",
			"com.magiology.util.renderers.tessellatorscripts.TexturedTriangle",
			"com.magiology.util.renderers.TessUtil",
			"com.magiology.util.renderers.VertexBufferM",
			"com.magiology.util.renderers.VertexModel",
			"com.magiology.util.renderers.VertexRenderer",
			"com.magiology.util.utilclasses.DataStalker",
			"com.magiology.util.utilclasses.FileUtil",
			"com.magiology.util.utilclasses.FontEffectUtil",
			"com.magiology.util.utilclasses.Get",
			"com.magiology.util.utilclasses.LogUtil",
			"com.magiology.util.utilclasses.math.ArrayMath",
			"com.magiology.util.utilclasses.math.CricleUtil",
			"com.magiology.util.utilclasses.math.MatrixUtil",
			"com.magiology.util.utilclasses.math.PartialTicksUtil",
			"com.magiology.util.utilclasses.NetworkUtil",
			"com.magiology.util.utilclasses.PhysicsUtil",
			"com.magiology.util.utilclasses.PowerUtil",
			"com.magiology.util.utilclasses.PrintUtil",
			"com.magiology.util.utilclasses.SideUtil",
			"com.magiology.util.utilclasses.SpecialPlayerUtil",
			"com.magiology.util.utilclasses.UtilM",
			"com.magiology.util.utilobjects.ArrayListLog",
			"com.magiology.util.utilobjects.ColorF",
			"com.magiology.util.utilobjects.ConvenientMap",
			"com.magiology.util.utilobjects.DoubleObject",
			"com.magiology.util.utilobjects.EntityPosAndBB",
			"com.magiology.util.utilobjects.IndexedModel",
			"com.magiology.util.utilobjects.LinearAnimation",
			"com.magiology.util.utilobjects.MatrixStack",
			"com.magiology.util.utilobjects.m_extension.BlockContainerM",
			"com.magiology.util.utilobjects.m_extension.BlockM",
			"com.magiology.util.utilobjects.m_extension.BlockPosM",
			"com.magiology.util.utilobjects.m_extension.effect.EntityCloudFXM",
			"com.magiology.util.utilobjects.m_extension.effect.EntityFlameFXM",
			"com.magiology.util.utilobjects.m_extension.effect.EntityLavaFXM",
			"com.magiology.util.utilobjects.m_extension.effect.EntitySmokeFXM",
			"com.magiology.util.utilobjects.m_extension.GuiContainerM",
			"com.magiology.util.utilobjects.m_extension.ItemM",
			"com.magiology.util.utilobjects.m_extension.ItemRendererM",
			"com.magiology.util.utilobjects.m_extension.SimpleNetworkWrapperM",
			"com.magiology.util.utilobjects.m_extension.TileEntityM",
			"com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM",
			"com.magiology.util.utilobjects.NBTUtil",
			"com.magiology.util.utilobjects.ObjectHolder",
			"com.magiology.util.utilobjects.ObjectProcessor",
			"com.magiology.util.utilobjects.OhBabyATriple",
			"com.magiology.util.utilobjects.SimpleCounter",
			"com.magiology.util.utilobjects.SlowdownUtil",
			"com.magiology.util.utilobjects.Tracker",
			"com.magiology.util.utilobjects.ValueTracker",
			"com.magiology.util.utilobjects.vectors.AngularVec3",
			"com.magiology.util.utilobjects.vectors.physics.PhysicsFloat",
			"com.magiology.util.utilobjects.vectors.physics.PhysicsVec3F",
			"com.magiology.util.utilobjects.vectors.physics.real.AbstractRealPhysicsVec3F",
			"com.magiology.util.utilobjects.vectors.physics.real.entitymodel.Colideable",
			"com.magiology.util.utilobjects.vectors.physics.real.entitymodel.EntityModelColider",
			"com.magiology.util.utilobjects.vectors.physics.real.entitymodel.EntityPlayerModelColider",
			"com.magiology.util.utilobjects.vectors.physics.real.GeometryUtil",
			"com.magiology.util.utilobjects.vectors.physics.real.RealPhysicsMesh",
			"com.magiology.util.utilobjects.vectors.physics.real.RealPhysicsVec3F",
			"com.magiology.util.utilobjects.vectors.Plane",
			"com.magiology.util.utilobjects.vectors.Pos",
			"com.magiology.util.utilobjects.vectors.QuadUV",
			"com.magiology.util.utilobjects.vectors.QuadUVGenerator",
			"com.magiology.util.utilobjects.vectors.RayDeprecated",
			"com.magiology.util.utilobjects.vectors.TwoDots",
			"com.magiology.util.utilobjects.vectors.Vec2i",
			"com.magiology.util.utilobjects.vectors.Vec3M",
			"com.magiology.util.utilobjects.vectors.X1y1z1x2y2z2",
			"com.magiology.windowsgui.ModInfoGUI",
			"com.magiology.windowsgui.SoundPlayer"
		};
	
}
