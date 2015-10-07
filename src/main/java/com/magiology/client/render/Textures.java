package com.magiology.client.render;


import java.util.ArrayList;

import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.magiology.core.MReference;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;

@SideOnly(Side.CLIENT)
public class Textures{
	private static TextureManager re=U.getMC().renderEngine;
	private static boolean isInit=false;
	private static ArrayList<String> failedTextures=new ArrayList<String>();
	private static String textur="WTF!?!!";
	public static ResourceLocation
	Helmet42Model,GuiArmorEditor,firePipeCore,FirePipeConection,FirePipeConectionEnd,Pants42Model,PowerCounterCore,PowerCounterFront,PowerCounterSide1,PowerCounterEnergyBar,vanillaBrick,BateryL1Core,
	BateryL2Core,BateryL3Core,BateryL100Core,BedrockBreakerBase,BedrockBreakerLegSide,BedrockBreakerLegFront,BedrockBreakerLegBack,BedrockBreakerLegTopBottom,BedrockBreakerLegLaser,BigFurnaceOutput,
	EnergizedLapisOre,FireExhaust,FireGunGun,FireGunGear,FireLampSide,FireLampUpNDown,FireMatrixReceaverBase,FireMatrixTransfererBase,FirePipeConectionFSL,FirePipeConectionFF,FirePipeConecterBase,
	FirePipeConecterInMe,FirePipeConecterOutOfMe,OreStructureCore,StatsGui1,
	//hand
	handBaseTop,handBaseSide,handBaseSide2,handBaseBotom,
	handThumbTop,handThumbBottom,handThumbEnd,handThumbStart,handThumbTxtClip,handThumbSide,
	handNormalFingerTop,handNormalFingerBottom,handnormalFingerEnd,handNormalFingerStart,handNormalFingerTxtClip,handNormalFingerSide,
	//particle
	SmoothBuble1,SmoothBuble1Add2,SmoothBuble1Add1,FireHD,SmoothBuble2,SmoothBuble3,
	
	//gui
	ISidedIns,
	//TODO
	WingColors
	;
	
	public static void postInit(){
		//---textur stands for "no texture and it should be made as soon as possible"---\\
		Util.println("TEXTURE INIT STARTED!");
		if(isInit)return;isInit=true;
		Helmet42Model=             getResource(MReference.MODID,"/models/textures/Helmet42Model.png");
		GuiArmorEditor=            getResource(MReference.MODID,"/textures/gui/GuiArmorEditor.png");
		firePipeCore=              getResource(MReference.MODID,"/textures/models/firepipe/FirePipeCore.png");
		FirePipeConection=         getResource(MReference.MODID,"/textures/models/firepipe/FirePipeConection.png");
		FirePipeConectionEnd=      getResource(MReference.MODID,"/textures/models/firepipe/FirePipeConectionEnd.png");
		Pants42Model=              getResource(MReference.MODID,"/models/textures/Pants42Model.png");
		PowerCounterCore=          getResource(MReference.MODID,"/textures/items/PowerCounter.png");
		PowerCounterFront=         getResource(MReference.MODID,"/textures/models/powercounter/PowerCounter1.png");
		PowerCounterSide1=         getResource(MReference.MODID,"/textures/models/PowerCounter/PowerCounter2.png");
		PowerCounterEnergyBar=     getResource(MReference.MODID,"/textures/models/PowerCounter/EnergyBar.png");
		vanillaBrick=              GetResource("textures/blocks/stonebrick.png");
		BateryL1Core=              getResource(MReference.MODID,textur);
		BateryL2Core=              getResource(MReference.MODID,textur);
		BateryL3Core=              getResource(MReference.MODID,textur);
		BateryL100Core=            getResource(MReference.MODID,textur);
		BedrockBreakerBase=        getResource(MReference.MODID,"/textures/models/bedrock_breaker_base.png");
		BedrockBreakerLegSide=     getResource(MReference.MODID,"/textures/models/bedrock_breaker_leg_side.png");
		BedrockBreakerLegFront=    getResource(MReference.MODID,"/textures/models/bedrock_breaker_leg_front.png");
		BedrockBreakerLegBack=     getResource(MReference.MODID,"/textures/models/bedrock_breaker_leg_back.png");
		BedrockBreakerLegTopBottom=getResource(MReference.MODID,"/textures/models/bedrock_breaker_leg_top_bottom.png");
		BedrockBreakerLegLaser=    getResource(MReference.MODID,"/textures/models/bedrock_breaker_leg_laser.png");
		BigFurnaceOutput=          getResource(MReference.MODID,textur);
		EnergizedLapisOre=         getResource(MReference.MODID,"/textures/models/energized_lapis_ore.png");
		FireExhaust=               getResource(MReference.MODID,"/textures/models/fire_exhaust.png");
		FireGunGun=                getResource(MReference.MODID,"/textures/models/gun.png");
		FireGunGear=               getResource(MReference.MODID,"/textures/models/gear.png");
		FireLampSide=              getResource(MReference.MODID,"/textures/blocks/FireLamp.png");
		FireLampUpNDown=           getResource(MReference.MODID,"/textures/blocks/FireLampTop.png");
		FireMatrixReceaverBase=    getResource(MReference.MODID,"/textures/models/fire_matrix_receaver_base.png");
		FireMatrixTransfererBase=  getResource(MReference.MODID,textur);
		FirePipeConectionFSL=      getResource(MReference.MODID,"/textures/models/firepipe/FirePipeConectionFSL.png");
		FirePipeConectionFF=       getResource(MReference.MODID,"/textures/models/firepipe/FirePipeConectionFF.png");
		FirePipeConecterBase=      getResource(MReference.MODID,"/textures/models/firepipe/FirePipeConecter.png");
		FirePipeConecterInMe=      getResource(MReference.MODID,"/textures/models/firepipe/FirePipeConecterIn.png");
		FirePipeConecterOutOfMe=   getResource(MReference.MODID,"/textures/models/firepipe/FirePipeConecterOut.png");
		OreStructureCore=          getResource(MReference.MODID,"/textures/models/ore_structure_core.png");
		StatsGui1=                 getResource(MReference.MODID,"/textures/gui/statsgui/StatsGui1.png");
		handBaseTop=               getResource(MReference.MODID,"/textures/items/thehand/baseTop.png");
		handBaseSide=              getResource(MReference.MODID,"/textures/items/thehand/baseSide.png");
		handBaseSide2=             getResource(MReference.MODID,"/textures/items/thehand/baseSide2.png");
		handBaseBotom=             getResource(MReference.MODID,"/textures/items/thehand/baseBotom.png");
		handThumbTop=              getResource(MReference.MODID,"/textures/items/thehand/thumbTop.png");
		handThumbBottom=           getResource(MReference.MODID,"/textures/items/thehand/thumbBotom.png");
		handThumbEnd=              getResource(MReference.MODID,"/textures/items/thehand/thumbEnd.png");
		handThumbStart=            getResource(MReference.MODID,"/textures/items/thehand/thumbStart.png");
		handThumbTxtClip=          getResource(MReference.MODID,"/textures/items/thehand/thumbTextureClip.png");
		handThumbSide=             getResource(MReference.MODID,"/textures/items/thehand/thumbSide.png");
		handNormalFingerTop=       getResource(MReference.MODID,"/textures/items/thehand/normalFingerTop.png");
		handNormalFingerBottom=    getResource(MReference.MODID,"/textures/items/thehand/normalFingerBotom.png");
		handnormalFingerEnd=       getResource(MReference.MODID,"/textures/items/thehand/normalFingerEnd.png");
		handNormalFingerStart=     getResource(MReference.MODID,"/textures/items/thehand/normalFingerStart.png");
		handNormalFingerTxtClip=   getResource(MReference.MODID,"/textures/items/thehand/normalFingerTextureClip.png");
		handNormalFingerSide=      getResource(MReference.MODID,"/textures/particle/fire_hd.png");
		SmoothBuble1=              getResource(MReference.MODID,"/textures/particle/smooth_buble1.png");
		SmoothBuble2=              getResource(MReference.MODID,"/textures/particle/smooth_buble2.png");
		SmoothBuble3=              getResource(MReference.MODID,"/textures/particle/smooth_buble3.png");
		SmoothBuble1Add1=          getResource(MReference.MODID,"/textures/particle/smooth_buble1_add1.png");
		SmoothBuble1Add2=          getResource(MReference.MODID,"/textures/particle/smooth_buble1_add2.png");
		FireHD=                    getResource(MReference.MODID,"/textures/particle/fire_hd.png");
		ISidedIns=                 getResource(MReference.MODID,"/textures/gui/ISidedIns.png");
		WingColors=                getResource(MReference.MODID,"/models/textures/wingColorMap.png");
		
		Util.println("TEXTURE INIT ENDED!");
		Util.println("----\n--------\n------------");
		Util.println("Magiology error list:");
		if(failedTextures.size()>0)for(String er:failedTextures){
			Util.println("---> "+er);
		}else Util.println("Yay! there is no errors!");
		Util.println("------------\n--------\n----");
	}
	public static ResourceLocation GetResource(String FolderPath){return getResource("",FolderPath);}
	public static ResourceLocation getResource(String modID,String AfterModIDFolderPath){
		ResourceLocation result=new ResourceLocation(modID,AfterModIDFolderPath);
		boolean isLoaded=re.loadTexture(result, new SimpleTexture(result));
		if(!isLoaded){
			if(result.getResourcePath().equals(textur))failedTextures.add("Texture is not added and it is in plans to be added!");
			else failedTextures.add("Texture failed to load at: "+result.toString());
		}
		return result;
	}
	
}