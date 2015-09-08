package com.magiology.api.network;

import java.util.Iterator;
import java.util.List;

import net.minecraft.world.World;

import com.magiology.api.SavableData;

public class RedstoneData implements SavableData{
	public boolean isStrong=false,on=false;
	public int strenght=0;
	public NetworkData networkData=null;
	
	public RedstoneData(){}
	public RedstoneData(boolean isStrong, boolean on, int strenght){
		this.isStrong=isStrong;
		this.on=on;
		this.strenght=strenght;
	}
	
	public void prepareForNetwork(World world){
		networkData=new NetworkData(world);
	}
	public void prepareForNetwork(World world,long delay){
		networkData=new NetworkData(world, delay);
	}
	@Override
	public void writeData(List<Integer> integers, List<Boolean> booleans, List<Byte> bytes___, List<Long> longs___, List<Double> doubles_, List<Float> floats__, List<String> strings_, List<Short> shorts__){
		integers.add(strenght);
		booleans.add(on);
		booleans.add(isStrong);
		booleans.add(networkData!=null);
		if(networkData!=null){
			booleans.add(networkData.isOnTimer);
			booleans.add(networkData.shouldBeOn);
			booleans.add(networkData.eventInvoked);
			longs___.add(networkData.timeEnd);
		}
	}
	@Override
	public void readData(Iterator<Integer> integers, Iterator<Boolean> booleans, Iterator<Byte> bytes___, Iterator<Long> longs___,Iterator<Double> doubles_, Iterator<Float> floats__, Iterator<String> strings_, Iterator<Short> shorts__){
		strenght=integers.next();
		on=booleans.next();
		isStrong=booleans.next();
		boolean netDataNotNull=booleans.next();
		if(netDataNotNull){
			prepareForNetwork(null);
			networkData.isOnTimer=booleans.next();
			networkData.shouldBeOn=booleans.next();
			networkData.eventInvoked=booleans.next();
			networkData.timeEnd=longs___.next();
		}
	}
	
	public class NetworkData{
		public boolean isOnTimer=false,shouldBeOn=false,eventInvoked=false;
		public long timeEnd=0;
		public World world;
		public NetworkData(World world){
			this.world=world;
			isOnTimer=false;
			shouldBeOn=true;
		}
		public NetworkData(World world,long delay){
			this.world=world;
			isOnTimer=true;
			shouldBeOn=false;
			timeEnd=world.getTotalWorldTime()+delay;
		}
		
		public boolean update(){
			if(eventInvoked)return false;
			boolean prevShouldBeOn=shouldBeOn;
			
			if(isOnTimer&&world!=null)shouldBeOn=timeEnd>world.getTotalWorldTime();
			else shouldBeOn=true;
			
			eventInvoked=shouldBeOn!=prevShouldBeOn;
			return shouldBeOn!=prevShouldBeOn;
		}
	}
}