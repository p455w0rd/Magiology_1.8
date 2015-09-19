package com.magiology.mcobjects.tileentityes.network.interfaces;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.magiology.api.network.InterfaceTileEntitySaver;
import com.magiology.api.network.NetworkBaseInterface;
import com.magiology.api.network.NetworkBaseInterface.DataOutput;
import com.magiology.api.network.NetworkBaseInterface.DataOutput.DataOutputDesc;
import com.magiology.api.network.NetworkBaseInterface.InteractType;
import com.magiology.api.network.NetworkInterfaceProvider;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.DoubleObject;

public class TileHologramProjectorInterface implements NetworkInterfaceProvider,InterfaceTileEntitySaver{
	@Override public TileEntity getBoundTile(){return tile;}
	@Override public void setBoundTile(TileEntity tile){this.tile=tile;}
	public TileEntity tile;
	
	
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		cardList.put(tile, NBT.getLong("card"));
	}
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		NBT.setLong("card", getCard());
	}

	@Override
	public void onNetworkActionInvoked(NetworkBaseInterface Interface, String action, Object... data){
		Util.println("hi");
	}
	
	@Override
	public TileEntityNetworkController getBrain(){
		NetworkBaseInterface interface1=getConnectedInterface();
		return interface1!=null?interface1.getBrain():null;
	}
	
	@Override
	public NetworkBaseInterface getConnectedInterface(){
		TileEntity[] tiles=SideUtil.getTilesOnSides(tile);
		for(int i=0;i<tiles.length;i++){
			if(tiles[i] instanceof NetworkBaseInterface){
				NetworkInterfaceProvider Interface=((NetworkBaseInterface)tiles[i]).getInterfaceProvider();
				if(Interface instanceof InterfaceTileEntitySaver){
					return (NetworkBaseInterface)tiles[i];
				}
			}
		}
		return null;
	}
	

	@Override
	public InteractType[] getInteractTypes(){
		return new InteractType[]{InteractType.Data,InteractType.TileEntityDetect};
	}
	
	@Override
	public DoubleObject<DataOutput,Object> getData(DataOutputDesc desc){
		return null;
	}

	@Override
	public long getCard(){
		return getCard(tile);
	}
	private static Map<TileEntity,Long> cardList=new HashMap<TileEntity,Long>();
	public static long getCard(TileEntity tileEntity){
		Long long1=cardList.get(tileEntity);
		if(long1!=null)return long1;
		Long id;
		do{
			id=Util.RL();
		}while(cardList.containsValue(id)&&id!=-1&&id!=0&&id!=-2);
		cardList.put(tileEntity, id);
		return id;
	}
}
