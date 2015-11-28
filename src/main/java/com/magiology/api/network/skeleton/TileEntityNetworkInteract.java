package com.magiology.api.network.skeleton;

import java.util.*;

import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;

import com.magiology.api.*;
import com.magiology.api.SavableData.SavableDataHandler;
import com.magiology.api.network.*;
import com.magiology.api.network.interfaces.registration.*;
import com.magiology.api.network.interfaces.registration.InterfaceBinder.TileToInterfaceHelper;
import com.magiology.core.init.*;
import com.magiology.forgepowered.packets.packets.*;
import com.magiology.mcobjects.tileentityes.network.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.UtilM.U;

public abstract class TileEntityNetworkInteract extends TileEntityNetwork implements NetworkInterface,Messageable{
	
	private Map<String,Object> interactData=new HashMap<String,Object>();
	
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);

		List<SavableData> data=SavableDataHandler.loadDataFromNBT(NBT, "ID",0);
		getData().clear();
		for(SavableData i:data)setInteractData(NBT.getString("key"+i), i);
	}
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		
		List<SavableData> savableData=new ArrayList<SavableData>();
		List<String> savableDataKeys=new ArrayList<String>();
		for(String key:getData().keySet()){
			Object obj=getData().get(key);
			if(obj instanceof SavableData){
				savableData.add((SavableData)obj);
				savableDataKeys.add(key);
			}
		}
		int dataSize=savableData.size();
		NBT.setInteger("DS", dataSize);
		SavableDataHandler.saveDataToNBT(savableData, NBT, "ID");
		for(int i=0;i<dataSize;i++)NBT.setString("key"+i, savableDataKeys.get(i));
	}
	@Override
	public WorldNetworkInterface getInterfaceProvider(){
		int orientation=SideUtil.getOppositeSide(getOrientation());
		return InterfaceBinder.get(worldObj, SideUtil.offsetNew(orientation, pos));
	}
	@Override
	public Map<String,Object> getData(){
		return interactData;
	}
	@Override
	public long getActiveCard(){
		return getInterfaceProvider()!=null?TileToInterfaceHelper.getCard(this,getInterfaceProvider()):-2;
	}
	@Override
	public Object getInteractData(String string){
		return interactData.get(string);
	}
	
	@Override
	public void setInteractData(String string,Object object){
		
		if(object instanceof SavableData){
			interactData.put(string, object);
			if(hasWorldObj()&&!U.isRemote(this))UtilM.sendMessage(new SavableDataWithKeyPacket(this,string, (SavableData)object));
		}else{
			interactData.put(string, object);
		}
		
		if(hasWorldObj()){
			if(object instanceof Redstone){
				worldObj.notifyBlockOfStateChange(pos, blockType);
				int side=getOrientation();
				BlockPos pos1=SideUtil.offsetNew(side, pos);
				if(U.getBlock(worldObj, pos1).isOpaqueCube())worldObj.notifyBlockOfStateChange(pos, U.getBlock(worldObj, pos1));
			}
		}
	}
	
	@Override
	public void onMessageReceved(String action){
		if(getBrain()==null)return;
		messageReceved(action);
	}
	public abstract void messageReceved(String action);
	
	@Override
	public void sendMessage(){
		
	}
	
	@Override
	public List<TileEntityNetworkRouter> getPointerContainers(){
		List<TileEntityNetworkRouter> result=new ArrayList<TileEntityNetworkRouter>();
		TileEntity[] tiles=SideUtil.getTilesOnSides(this);
		for(TileEntity a:tiles)if(a instanceof TileEntityNetworkRouter)result.add((TileEntityNetworkRouter)a);
		return result;
	}

	@Override
	public List<ItemStack> getPointers(){
		List<ItemStack> result=new ArrayList<ItemStack>();
		List<TileEntityNetworkRouter> containers=getPointerContainers();
		for(TileEntityNetworkRouter a:containers){
			for(int b=0;b<a.getSizeInventory();b++){
				if(UtilM.isItemInStack(MItems.networkPointer, a.getStackInSlot(b)))result.add(a.getStackInSlot(b));
			}
		}
		return result;
	}
}
