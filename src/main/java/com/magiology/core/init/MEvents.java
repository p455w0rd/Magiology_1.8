package com.magiology.core.init;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.magiology.forgepowered.event.EntityEvents;
import com.magiology.forgepowered.event.GameLoopEvents;
import com.magiology.forgepowered.event.HighlightEvent;
import com.magiology.forgepowered.event.MouseEvents;
import com.magiology.forgepowered.event.RenderLoopEvents;
import com.magiology.forgepowered.event.TickEvents;
import com.magiology.render.shaders.core.ShaderRunner;

public class MEvents{
	
	public static RenderLoopEvents RenderLoopInstance;
	public static HighlightEvent HighlightInstance;
	public static GameLoopEvents GameLoopInstance;
	public static EntityEvents EntityInstance;
	public static MouseEvents MouseInstance;
	public static ShaderRunner shaderHandeler;
	
	public static void init(){
		EventRegister(0,
				HighlightInstance=new HighlightEvent(),
				EntityInstance=new EntityEvents(),
				RenderLoopInstance=new RenderLoopEvents(),
				MouseInstance=new MouseEvents(),
				GameLoopInstance=new GameLoopEvents(),
				shaderHandeler=new ShaderRunner()
		);
		EventRegister(1,
				TickEvents.instance
				);
	}
	public static void EventRegister(int RegisterId,Object... classes){
		if(classes.length>0)switch(RegisterId){
		case 0:for(int a=0;a<classes.length;a++)MinecraftForge.EVENT_BUS.register(classes[a]);break;
		case 1:for(int a=0;a<classes.length;a++)FMLCommonHandler.instance().bus().register(classes[a]);break;
		case 2:for(int a=0;a<classes.length;a++)MinecraftForge.TERRAIN_GEN_BUS.register(classes[a]);break;
		case 3:for(int a=0;a<classes.length;a++)MinecraftForge.ORE_GEN_BUS.register(classes[a]);break;
		default:for(int a=0;a<classes.length;a++)System.err.print("EVENT FAILED TO REGISTER! "+RegisterId+" IS A WRONG REGISTRATION ID!"+"\n");break;
		}else if(classes.length>0&&RegisterId>3&&RegisterId<0)System.err.print("AAAND YOU FAILED...\n");
		else System.err.print("NO EVENTS ADDED FOR REGISTRATION!\n");
	}
}
