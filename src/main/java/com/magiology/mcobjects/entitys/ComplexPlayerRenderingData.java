package com.magiology.mcobjects.entitys;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.handelers.animationhandelers.WingsFromTheBlackFireHandeler.Positions;

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
	//PowerHand data section
	public class PowerHandData{
		public final EntityPlayer player;
		public PowerHandData(EntityPlayer player){this.player=player;}
		public float[] 
				handRotation={0,0,0,0,0,0},
				handRotationSpeed={0,0,0,0,0,0},
				handRotationCalc={0,0,0,0,0,0},
				prevHandRotationCalc={0,0,0,0,0,0},
				noiserHandSpeed={0,0,0,0,0,0};
		public float 
				thirdPresonPos=0,
				thirdPresonPosSpeed=0;
		public PowerHandData_sub_fingerData[] fingerData={new PowerHandData_sub_fingerData(),new PowerHandData_sub_fingerData(),new PowerHandData_sub_fingerData(),new PowerHandData_sub_fingerData(),new PowerHandData_sub_fingerData()};
	}
	public class PowerHandData_sub_fingerData{
		public float[][] 
				xyzPosRot=new float[3][6],
				xyzPosRotSpeed=new float[3][6],
				xyzPosRotNoise=new float[3][6],
				calcXyzPosRot=new float[3][6],
				prevcalcXyzPosRot=new float[3][6];
	}
	//endSection()----------------------------------------------------------------------------------
	
	//register section
	public static ComplexPlayerRenderingData[] getAllData(){
		ComplexPlayerRenderingData[] result={};
		for(ComplexPlayerRenderingData a:RenderedPlayesList)result=ArrayUtils.add(result, a);
		return result;
	}
	private static ArrayList<ComplexPlayerRenderingData> RenderedPlayesList=new ArrayList<ComplexPlayerRenderingData>();
	//get subSection                                         |
	public static CyborgWingsFromTheBlackFireData getFastCyborgWingsFromTheBlackFireData(EntityPlayer player){
		ComplexPlayerRenderingData data=get(player);
		return data!=null?data.getCyborgWingsFromTheBlackFireData():null;
	}
	public static PowerHandData getFastPowerHandData(EntityPlayer player){
		ComplexPlayerRenderingData data=get(player);
		return data!=null?data.getPowerHandData():null;
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

	private PowerHandData powerHandData;
	public PowerHandData getPowerHandData(){
		if(powerHandData!=null)return powerHandData;
		powerHandData=new PowerHandData(player);
		return powerHandData;
	}
	private CyborgWingsFromTheBlackFireData cyborgWingsFromTheBlackFireData;
	public CyborgWingsFromTheBlackFireData getCyborgWingsFromTheBlackFireData(){
		if(cyborgWingsFromTheBlackFireData!=null)return cyborgWingsFromTheBlackFireData;
		cyborgWingsFromTheBlackFireData=new CyborgWingsFromTheBlackFireData(player);
		return cyborgWingsFromTheBlackFireData;
	}
	//endSection()----------------------------------------------------------------------------------
}
