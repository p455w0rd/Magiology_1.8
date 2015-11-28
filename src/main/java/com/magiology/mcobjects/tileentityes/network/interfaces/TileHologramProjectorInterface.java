package com.magiology.mcobjects.tileentityes.network.interfaces;

import java.util.*;

import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;

import com.magiology.api.lang.*;
import com.magiology.api.network.*;
import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.mcobjects.tileentityes.network.*;
import com.magiology.util.utilclasses.*;

public class TileHologramProjectorInterface implements WorldNetworkInterface,InterfaceTileEntitySaver{
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
	public void onNetworkActionInvoked(NetworkInterface Interface, String action, Object... data){
		UtilM.println("hi");
	}
	
	@Override
	public TileEntityNetworkController getBrain(){
		NetworkInterface interface1=getConnectedInterface();
		return interface1!=null?interface1.getBrain():null;
	}
	
	@Override
	public NetworkInterface getConnectedInterface(){
		TileEntity[] tiles=SideUtil.getTilesOnSides(tile);
		for(int i=0;i<tiles.length;i++){
			if(tiles[i] instanceof NetworkInterface){
				WorldNetworkInterface Interface=((NetworkInterface)tiles[i]).getInterfaceProvider();
				if(Interface instanceof InterfaceTileEntitySaver){
					return (NetworkInterface)tiles[i];
				}
			}
		}
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
			id=UtilM.RL();
		}while(cardList.containsValue(id)&&id!=-1&&id!=0&&id!=-2);
		cardList.put(tileEntity, id);
		return id;
	}
	@Override
	public List<ICommandInteract> getCommandInteractors(){
		List<ICommandInteract> result=new ArrayList<ICommandInteract>();
		for(HoloObject i:((TileEntityHologramProjector)tile).holoObjects)if(i instanceof ICommandInteract)result.add((ICommandInteract)i);
		return result;
	}
}
