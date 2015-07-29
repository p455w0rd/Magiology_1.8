package com.magiology.render;


import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import com.magiology.core.MReference;
import com.magiology.handelers.BumpMapHandeler;
import com.magiology.objhelper.helpers.Helper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Textures{
	private static TextureManager re=Minecraft.getMinecraft().renderEngine;
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
	ISidedIns
	;
	
	public static void postInit(){
		//---textur stands for "no texture and it should be made as soon as possible"---\\
		Helper.println("TEXTURE INIT STARTED!");
		BumpMapHandeler.loadBumpMaps();
		if(isInit)return;isInit=true;
		Helmet42Model=             GetResource(MReference.MODID,"/models/textures/Helmet42Model.png");
		GuiArmorEditor=            GetResource(MReference.MODID,"/textures/gui/GuiArmorEditor.png");
		firePipeCore=              GetResource(MReference.MODID,"/textures/models/firepipe/FirePipeCore.png");
		FirePipeConection=         GetResource(MReference.MODID,"/textures/models/firepipe/FirePipeConection.png");
		FirePipeConectionEnd=      GetResource(MReference.MODID,"/textures/models/firepipe/FirePipeConectionEnd.png");
		Pants42Model=              GetResource(MReference.MODID,"/models/textures/Pants42Model.png");
		PowerCounterCore=          GetResource(MReference.MODID,"/textures/items/PowerCounter.png");
		PowerCounterFront=         GetResource(MReference.MODID,"/textures/models/powercounter/PowerCounter1.png");
		PowerCounterSide1=         GetResource(MReference.MODID,"/textures/models/PowerCounter/PowerCounter2.png");
		PowerCounterEnergyBar=     GetResource(MReference.MODID,"/textures/models/PowerCounter/EnergyBar.png");
		vanillaBrick=              GetResource("textures/blocks/stonebrick.png");
		BateryL1Core=              GetResource(MReference.MODID,textur);
		BateryL2Core=              GetResource(MReference.MODID,textur);
		BateryL3Core=              GetResource(MReference.MODID,textur);
		BateryL100Core=            GetResource(MReference.MODID,textur);
		BedrockBreakerBase=        GetResource(MReference.MODID,"/textures/models/bedrock_breaker_base.png");
		BedrockBreakerLegSide=     GetResource(MReference.MODID,"/textures/models/bedrock_breaker_leg_side.png");
		BedrockBreakerLegFront=    GetResource(MReference.MODID,"/textures/models/bedrock_breaker_leg_front.png");
		BedrockBreakerLegBack=     GetResource(MReference.MODID,"/textures/models/bedrock_breaker_leg_back.png");
		BedrockBreakerLegTopBottom=GetResource(MReference.MODID,"/textures/models/bedrock_breaker_leg_top_bottom.png");
		BedrockBreakerLegLaser=    GetResource(MReference.MODID,"/textures/models/bedrock_breaker_leg_laser.png");
		BigFurnaceOutput=          GetResource(MReference.MODID,textur);
		EnergizedLapisOre=         GetResource(MReference.MODID,"/textures/models/energized_lapis_ore.png");
		FireExhaust=               GetResource(MReference.MODID,"/textures/models/fire_exhaust.png");
		FireGunGun=                GetResource(MReference.MODID,"/textures/models/gun.png");
		FireGunGear=               GetResource(MReference.MODID,"/textures/models/gear.png");
		FireLampSide=              GetResource(MReference.MODID,"/textures/blocks/FireLamp.png");
		FireLampUpNDown=           GetResource(MReference.MODID,"/textures/blocks/FireLampTop.png");
		FireMatrixReceaverBase=    GetResource(MReference.MODID,"/textures/models/fire_matrix_receaver_base.png");
		FireMatrixTransfererBase=  GetResource(MReference.MODID,textur);
		FirePipeConectionFSL=      GetResource(MReference.MODID,"/textures/models/firepipe/FirePipeConectionFSL.png");
		FirePipeConectionFF=       GetResource(MReference.MODID,"/textures/models/firepipe/FirePipeConectionFF.png");
		FirePipeConecterBase=      GetResource(MReference.MODID,"/textures/models/firepipe/FirePipeConecter.png");
		FirePipeConecterInMe=      GetResource(MReference.MODID,"/textures/models/firepipe/FirePipeConecterIn.png");
		FirePipeConecterOutOfMe=   GetResource(MReference.MODID,"/textures/models/firepipe/FirePipeConecterOut.png");
		OreStructureCore=          GetResource(MReference.MODID,"/textures/models/ore_structure_core.png");
		StatsGui1=                 GetResource(MReference.MODID,"/textures/gui/statsgui/StatsGui1.png");
		handBaseTop=               GetResource(MReference.MODID,"/textures/items/thehand/baseTop.png");
		handBaseSide=              GetResource(MReference.MODID,"/textures/items/thehand/baseSide.png");
		handBaseSide2=             GetResource(MReference.MODID,"/textures/items/thehand/baseSide2.png");
		handBaseBotom=             GetResource(MReference.MODID,"/textures/items/thehand/baseBotom.png");
		handThumbTop=              GetResource(MReference.MODID,"/textures/items/thehand/thumbTop.png");
		handThumbBottom=           GetResource(MReference.MODID,"/textures/items/thehand/thumbBotom.png");
		handThumbEnd=              GetResource(MReference.MODID,"/textures/items/thehand/thumbEnd.png");
		handThumbStart=            GetResource(MReference.MODID,"/textures/items/thehand/thumbStart.png");
		handThumbTxtClip=          GetResource(MReference.MODID,"/textures/items/thehand/thumbTextureClip.png");
		handThumbSide=             GetResource(MReference.MODID,"/textures/items/thehand/thumbSide.png");
		handNormalFingerTop=       GetResource(MReference.MODID,"/textures/items/thehand/normalFingerTop.png");
		handNormalFingerBottom=    GetResource(MReference.MODID,"/textures/items/thehand/normalFingerBotom.png");
		handnormalFingerEnd=       GetResource(MReference.MODID,"/textures/items/thehand/normalFingerEnd.png");
		handNormalFingerStart=     GetResource(MReference.MODID,"/textures/items/thehand/normalFingerStart.png");
		handNormalFingerTxtClip=   GetResource(MReference.MODID,"/textures/items/thehand/normalFingerTextureClip.png");
		handNormalFingerSide=      GetResource(MReference.MODID,"/textures/particle/fire_hd.png");
		SmoothBuble1=              GetResource(MReference.MODID,"/textures/particle/smooth_buble1.png");
		SmoothBuble2=              GetResource(MReference.MODID,"/textures/particle/smooth_buble2.png");
		SmoothBuble3=              GetResource(MReference.MODID,"/textures/particle/smooth_buble3.png");
		SmoothBuble1Add1=          GetResource(MReference.MODID,"/textures/particle/smooth_buble1_add1.png");
		SmoothBuble1Add2=          GetResource(MReference.MODID,"/textures/particle/smooth_buble1_add2.png");
		FireHD=                    GetResource(MReference.MODID,"/textures/particle/fire_hd.png");
		ISidedIns=                 GetResource(MReference.MODID,"/textures/gui/ISidedIns.png");
		
		Helper.println("TEXTURE INIT ENDED!");
		Helper.println("----\n--------\n------------");
		Helper.println("Magiology error list:");
		if(failedTextures.size()>0)for(String er:failedTextures){
			Helper.println("---> "+er);
		}else Helper.println("Yay! there is no errors!");
		Helper.println("------------\n--------\n----");
	}
	public static ResourceLocation GetResource(String FolderPath){return GetResource("",FolderPath);}
	public static ResourceLocation GetResource(String modID,String AfterModIDFolderPath){
		ResourceLocation result=new ResourceLocation(modID,AfterModIDFolderPath);
		boolean isLoaded=re.loadTexture(result, new SimpleTexture(result));
		if(!isLoaded){
			if(result.getResourcePath().equals(textur))failedTextures.add("Texture is not added and it is in plans to be added!");
			else failedTextures.add("Texture failed to load at: "+result.toString());
		}
		return result;
	}
	
}
