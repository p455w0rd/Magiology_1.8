package com.magiology.handlers.animationhandlers.thehand;

import java.util.ArrayList;
import java.util.List;

import com.magiology.util.utilobjects.codeinsert.ObjectProcessor;

public class HandPosition{
	private static float p=1F/16F;
	

	private static final List<HandPosition> registry=new ArrayList<>();
	private static HandPosition[] values;
	private static boolean isCompiled=false;
	
	public static void registerPosition(HandPosition pos){
		assert !isCompiled:"It is to late to register!";
		assert !registry.contains(pos):"This position is already registered!";
		boolean nameMatch=false;
		for(HandPosition handPosition:registry){
			if(pos.name.equals(handPosition.name)){
				nameMatch=true;
				break;
			}
		}
		assert !nameMatch:"Position with is already registered with \""+pos.name+"\"!";
		
		pos.posInRegistry=registry.size();
		registry.add(pos);
	}
	public static void compile(){
		isCompiled=true;
		values=registry.toArray(new HandPosition[0]);
	}
	public static HandPosition[] values(){
		return values;
	}
	
	
	public static HandPosition
		ErrorPos=new HandPosition(new HandData(),"ErrorPos"),
		ClosedFist=new HandPosition(new ObjectProcessor<HandData>(){@Override public HandData pocess(HandData object, Object...objects){
			object.base[0]=0;
			object.base[1]=0;
			object.base[2]=0;
			
			object.base[3]=0;
			object.base[4]=0;
			object.base[5]=0;
			
			object.thumb[0]=28;
			object.thumb[1]=10;
			object.thumb[2]=20;
			object.thumb[3]=-20;
			object.thumb[4]=-90;
			
			object.fingers[0][1]=80;
			object.fingers[0][2]=110;
			object.fingers[0][3]=75;
			
			object.fingers[1][1]=80;
			object.fingers[1][2]=100;
			object.fingers[1][3]=90;
			
			object.fingers[2][1]=85;
			object.fingers[2][2]=95;
			object.fingers[2][3]=80;
			
			object.fingers[3][1]=95;
			object.fingers[3][2]=80;
			object.fingers[3][3]=110;
			return object;
		}}.pocess(new HandData()),"ClosedFist"),
		WeaponHolder=new HandPosition(new ObjectProcessor<HandData>(){@Override public HandData pocess(HandData data, Object...objects){
			data.base[1]=-p*3;
			data.base[3]=-30;
			data.base[4]=-5;
			
			data.thumb[0]=60;
			data.thumb[1]=10;
			data.thumb[2]=10;
			data.thumb[3]=-5;
			data.thumb[4]=-20;
			
			data.fingers[0][0]=-10;
			data.fingers[0][1]=3;
			data.fingers[0][2]=65;
			data.fingers[0][3]=10;
			
			data.fingers[1][0]=5;
			data.fingers[1][1]=20;
			data.fingers[1][2]=45;
			data.fingers[1][3]=20;
			
			data.fingers[2][0]=15;
			data.fingers[2][1]=25;
			data.fingers[2][2]=35;
			data.fingers[2][3]=15;
			
			data.fingers[3][0]=20;
			data.fingers[3][1]=25;
			data.fingers[3][2]=20;
			data.fingers[3][3]=20;
			return data;
		}}.pocess(new HandData()),"WeaponHolder"),
		LookAtSomething=new HandPosition(new ObjectProcessor<HandData>(){@Override public HandData pocess(HandData data, Object...objects){
			data.base[0]=p*11;
			data.base[1]=-p*8;
			data.base[2]=-0.1F;
			data.base[3]=-15;
			data.base[4]=0;
			data.base[5]=170;
			
			data.thumb[0]=15;
			data.thumb[1]=40;
			data.thumb[2]=15;
			data.thumb[3]=-10;
			data.thumb[4]=-25;
			
			data.fingers[0][1]=15;
			data.fingers[0][2]=55;
			data.fingers[0][3]=5;
			
			data.fingers[1][1]=40;
			data.fingers[1][2]=25;
			data.fingers[1][3]=5;
			
			data.fingers[2][1]=35;
			data.fingers[2][2]=30;
			data.fingers[2][3]=7;
			
			data.fingers[3][1]=50;
			data.fingers[3][2]=15;
			data.fingers[3][3]=5;
			return data;
		}}.pocess(new HandData()),"LookAtSomething"),
		NaturalPosition=new HandPosition(new ObjectProcessor<HandData>(){@Override public HandData pocess(HandData data, Object...objects){
			data.base[1]=-p*7;
			data.base[3]=-15;
			data.base[4]=0;
			data.base[5]=5;

			data.thumb[0]=22;
			data.thumb[1]=32;
			data.thumb[3]=-15;
			data.thumb[4]=-30;
			
			data.fingers[0][1]=12;
			data.fingers[0][2]=40;
			data.fingers[0][3]=5;
			
			data.fingers[1][1]=17;
			data.fingers[1][2]=20;
			data.fingers[1][3]=2;
			
			data.fingers[2][1]=17;
			data.fingers[2][2]=23;
			data.fingers[2][3]=2;
			
			data.fingers[3][1]=24;
			data.fingers[3][2]=15;
			data.fingers[3][3]=2;
			return data;
		}}.pocess(new HandData()),"NaturalPosition");

	static{
		registerPosition(ErrorPos);
		registerPosition(ClosedFist);
		registerPosition(WeaponHolder);
		registerPosition(LookAtSomething);
		registerPosition(NaturalPosition);
	}
	
	
	public final HandData data;
	public final String name;
	private int posInRegistry;
	
	private HandPosition(HandData data, String name){
		this.data=data;
		this.name=name;
	}
	public int posInRegistry(){
		return posInRegistry;
	}
}