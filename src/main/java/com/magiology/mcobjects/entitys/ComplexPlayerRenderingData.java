package com.magiology.mcobjects.entitys;

import java.util.ArrayList;

import com.magiology.handlers.animationhandlers.WingsFromTheBlackFireHandler.Positions;

import net.minecraft.entity.player.EntityPlayer;

public class ComplexPlayerRenderingData{
	// init section
	public final EntityPlayer player;
	public float playerBodyRotation;
	public int dataObjectId;
	public ComplexPlayerRenderingData(EntityPlayer player){this.player=player;}
	//endSection()----------------------------------------------------------------------------------
	//CyborgWingsFromTheBlackFire data section
	public class CyborgWingsFromTheBlackFireData{
		public final EntityPlayer player;
		public CyborgWingsFromTheBlackFireData(EntityPlayer player){this.player=player;}
		public boolean canFlap;
		public float playerAngle,prevPlayerAngle;
		public float[][]
				flapAnglesBase=new float[7][3],
				rotationAnglesBase=new float[7][3],
				calcRotationAnglesBase=new float[7][3],
				wantedRotationAnglesBase=new float[7][3],
				calcPrevRotationAnglesBase=new float[7][3];
		public Positions[] flap=new Positions[2];
	}
	//endSection()----------------------------------------------------------------------------------
	
	//register section
	public static ComplexPlayerRenderingData[] getAllData(){
		return RenderedPlayesList.toArray(new ComplexPlayerRenderingData[0]);
	}
	private static ArrayList<ComplexPlayerRenderingData> RenderedPlayesList=new ArrayList<ComplexPlayerRenderingData>();
	//get subSection										 |
	public static CyborgWingsFromTheBlackFireData getFastCyborgWingsFromTheBlackFireData(EntityPlayer player){
		ComplexPlayerRenderingData data=get(player);
		return data!=null?data.getCyborgWingsFromTheBlackFireData():null;
	}
	public static ComplexPlayerRenderingData get(EntityPlayer player){
		for(ComplexPlayerRenderingData a:RenderedPlayesList)if(a.player==player)return a;
		return null;
	}
	//endSubSection()-----------------------------------------
	public static ComplexPlayerRenderingData registerEntityPlayerRenderer(EntityPlayer player){
		for(ComplexPlayerRenderingData a:RenderedPlayesList)if(a.player==player)return null;
		ComplexPlayerRenderingData var=new ComplexPlayerRenderingData(player);
		RenderedPlayesList.add(var);
		return var;
	}
	public static void enshure(EntityPlayer player){
		if(get(player)==null)registerEntityPlayerRenderer(player);
	}
	//endSection()----------------------------------------------------------------------------------
	//inside register section
	
	
	private CyborgWingsFromTheBlackFireData cyborgWingsFromTheBlackFireData;
	public CyborgWingsFromTheBlackFireData getCyborgWingsFromTheBlackFireData(){
		if(cyborgWingsFromTheBlackFireData!=null)return cyborgWingsFromTheBlackFireData;
		cyborgWingsFromTheBlackFireData=new CyborgWingsFromTheBlackFireData(player);
		return cyborgWingsFromTheBlackFireData;
	}
	//endSection()----------------------------------------------------------------------------------
}
