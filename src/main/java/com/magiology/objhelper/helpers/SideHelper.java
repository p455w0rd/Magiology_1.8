package com.magiology.objhelper.helpers;

import java.util.Random;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class SideHelper{
	
	static int[] x={0,0,0,1,0,-1};
	static int[] y={1,-1,0,0,0,0};
	static int[] z={0,0,-1,0,1,0};
	static Random rand = new Random();
	
//	switch(side){
//	case 0:{y+=1;}break;
//	case 1:{y-=1;}break;
//	case 2:{z-=1;}break;
//	case 3:{x+=1;}break;
//	case 4:{z+=1;}break;
//	case 5:{x-=1;}break;
//	}
	
	public static int getOppositeSide(int side){
		int result=-1;
		switch(side){
		case 0:result=1;break;
		case 1:result=0;break;
		case 2:result=4;break;
		case 3:result=5;break;
		case 4:result=2;break;
		case 5:result=3;break;
		}
		return result;
	}
	
	
	public static int X(int side,int xCoord){
		if(side>5)return 1234;
		int s=0;
		
		s=x[side];
		
		return s+xCoord;
	}
	
	public static int Y(int side,int yCoord){
		if(side>5)return 1234;
		int s=0;
		
		s=y[side];
		
		return s+yCoord;
	}
	
	public static int Z(int side,int zCoord){
		if(side>5)return 1234;
		int s=0;
		
		s=z[side];
		
		return s+zCoord;
	}
	
	public static int[] randomizeSides(){
		int[] side=new int[6];
		side[0]=rand.nextInt(6);
		do{side[1]=rand.nextInt(6);}while(side[1]==side[0]);
		do{side[2]=rand.nextInt(6);}while(side[2]==side[0]
										||side[2]==side[1]);
		do{side[3]=rand.nextInt(6);}while(side[3]==side[0]
										||side[3]==side[1]
										||side[3]==side[2]);
		do{side[4]=rand.nextInt(6);}while(side[4]==side[0]
										||side[4]==side[1]
										||side[4]==side[2]
										||side[4]==side[3]);
		do{side[5]=rand.nextInt(6);}while(side[5]==side[0]
										||side[5]==side[1]
									    ||side[5]==side[2]
										||side[5]==side[3]
										||side[5]==side[4]);
		return side;
	}


	public static int ForgeDirgetOrientationInverted(ForgeDirection fDir){
		if(fDir==null)return -1;
		else if(fDir==ForgeDirection.DOWN) return 0;
		else if(fDir==ForgeDirection.UP)   return 1;
		else if(fDir==ForgeDirection.NORTH)return 2;
		else if(fDir==ForgeDirection.SOUTH)return 3;
		else if(fDir==ForgeDirection.WEST) return 4;
		else if(fDir==ForgeDirection.EAST) return 5;
		return -1;
	}
	public static int DOWN(){
		return ForgeDirgetOrientationInverted(ForgeDirection.DOWN);
	}
	public static int UP(){
		return ForgeDirgetOrientationInverted(ForgeDirection.UP);
	}
	public static int NORTH(){
		return ForgeDirgetOrientationInverted(ForgeDirection.NORTH);
	}
	public static int SOUTH(){
		return ForgeDirgetOrientationInverted(ForgeDirection.SOUTH);
	}
	public static int WEST(){
		return ForgeDirgetOrientationInverted(ForgeDirection.WEST);
	}
	public static int EAST(){
		return ForgeDirgetOrientationInverted(ForgeDirection.EAST);
	}
	public static TileEntity[] getTilesOnSides(TileEntity tileEntity){
		TileEntity[] result=new TileEntity[6];
		if(tileEntity!=null)for(int i=0;i<6;i++)result[i]=tileEntity.getWorldObj().getTileEntity(X(i, tileEntity.xCoord), Y(i, tileEntity.yCoord), Z(i, tileEntity.zCoord));
		return result;
	}


	public static int convert(int side){
		switch(side){
		case 0:return 0;
		case 1:return 1;
		case 2:return 4;
		case 3:return 2;
		case 4:return 3;
		case 5:return 5;
		}
		return -1;
	}
}
