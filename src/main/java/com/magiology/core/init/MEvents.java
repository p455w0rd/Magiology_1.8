package com.magiology.core.init;

import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.client.gui.custom.OnOffGuiButton.GuiButtonClickEvent;
import com.magiology.client.render.shaders.core.*;
import com.magiology.forgepowered.events.*;
import com.magiology.forgepowered.events.client.*;
import com.magiology.util.utilclasses.*;

public class MEvents{
	
	@SideOnly(Side.CLIENT)
	public static RenderEvents renderEvents;
	@SideOnly(Side.CLIENT)
	public static HighlightEvent highlightEvent;
	@SideOnly(Side.CLIENT)
	public static MouseEvents mouseEvents;
	@SideOnly(Side.CLIENT)
	public static ShaderRunner shaderHandler;
	
	public static GameEvents gameEvents;
	public static EntityEvents entityEvents;
	
	
	public static void init(){
		common();
		if(UtilM.isRemote())client();
		else server();
	}
	
	public static void common(){
		entityEvents=new EntityEvents();
		gameEvents=new GameEvents();
		
		MinecraftForge.EVENT_BUS.register(gameEvents);
		MinecraftForge.EVENT_BUS.register(entityEvents);
		FMLCommonHandler.instance().bus().register(TickEvents.instance);
	}
	
	public static void client(){
		highlightEvent=new HighlightEvent();
		renderEvents=new RenderEvents();
		mouseEvents=new MouseEvents();
		shaderHandler=new ShaderRunner();
		
		MinecraftForge.EVENT_BUS.register(renderEvents);
		MinecraftForge.EVENT_BUS.register(mouseEvents);
		MinecraftForge.EVENT_BUS.register(highlightEvent);
		MinecraftForge.EVENT_BUS.register(shaderHandler);
		MinecraftForge.EVENT_BUS.register(GuiButtonClickEvent.get());
	}
	
	public static void server(){
		
	}
	
//	public static void EventRegister(int RegisterId,Object... classes){
//		if(classes.length>0)switch(RegisterId){
//		case 0:for(int a=0;a<classes.length;a++)MinecraftForge.EVENT_BUS.register(classes[a]);break;
//		case 1:for(int a=0;a<classes.length;a++)FMLCommonHandler.instance().bus().register(classes[a]);break;
//		case 2:for(int a=0;a<classes.length;a++)MinecraftForge.TERRAIN_GEN_BUS.register(classes[a]);break;
//		case 3:for(int a=0;a<classes.length;a++)MinecraftForge.ORE_GEN_BUS.register(classes[a]);break;
//		}
//	}
}
