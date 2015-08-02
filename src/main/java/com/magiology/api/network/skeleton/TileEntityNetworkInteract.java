package com.magiology.api.network.skeleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import com.magiology.api.SavableData;
import com.magiology.api.SavableData.SavableDataHandeler;
import com.magiology.api.network.NetworkBaseInterface;
import com.magiology.api.network.NetworkBaseInterface.DataOutput.DataOutputDesc;
import com.magiology.api.network.NetworkInterfaceProvider;
import com.magiology.api.network.RedstoneData;
import com.magiology.core.init.MItems;
import com.magiology.forgepowered.packets.SavableDataWithKeyPacket;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkPointerContainer;
import com.magiology.mcobjects.tileentityes.network.interfaces.registration.InterfaceBinder;
import com.magiology.mcobjects.tileentityes.network.interfaces.registration.InterfaceBinder.TileToInterfaceHelper;
import com.magiology.objhelper.DoubleObject;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;
import com.magiology.objhelper.helpers.SideHelper;

public abstract class TileEntityNetworkInteract extends TileEntityNetwork implements NetworkBaseInterface{
	
	private Map<String,Object> interactData=new HashMap<String,Object>();
	
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		int dataSize=NBT.getInteger("DS");

		List<SavableData> data=SavableDataHandeler.loadDataFromNBT(NBT, "ID",0);
		getData().clear();
		for(int i=0;i<dataSize;i++){
			setInteractData(NBT.getString("key"+i), data.get(i));
		}
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
		SavableDataHandeler.saveDataToNBT(savableData, NBT, "ID");
		for(int i=0;i<dataSize;i++)NBT.setString("key"+i, savableDataKeys.get(i));
	}
	@Override
	public NetworkInterfaceProvider getInterfaceProvider(){
		int orientation=SideHelper.convert(getOrientation());
		return InterfaceBinder.get(worldObj, SideHelper.offset(orientation, pos));
	}
	private List<InteractType> interactTypes=new ArrayList<InteractType>();
	@Override
	public List<InteractType> getInteractTypes(){
		return interactTypes;
	}
	@Override
	public Map<String,Object> getData(){
		return interactData;
	}
	public abstract void onNetworkActionInvoked(String action, int dataSize, Object... data);
	@Override
	public boolean hasInteractType(InteractType interactType){
		return interactTypes.contains(interactType);
	}
	@Override
	public long getActiveCard(){
		return getInterfaceProvider()!=null?TileToInterfaceHelper.getCard(this,getInterfaceProvider()):-2;
	}
	@Override
	public Object getInteractData(String string){
		return interactData.get(string);}
	public abstract void onInterfaceProviderActionInvoked(NetworkInterfaceProvider interfaceProvider, String action, int dataSize, Object... data);
	@Override
	public void setInteractType(InteractType interactType){
		if(interactType==null)interactTypes.remove(interactType);else if(!hasInteractType(interactType))interactTypes.add(interactType);
	}
	@Override
	public void onNetworkActionInvoked(String action, Object... data){
		if(H.isNull(getBrain(),action,data)||action.isEmpty()||data.length==0)return;onNetworkActionInvoked(action, data.length, data);
	}
	@Override
	public void setInteractData(String string,Object object){
		
		if(object instanceof SavableData){
			interactData.put(string, object);
			if(hasWorldObj()&&!H.isRemote(this))Helper.sendMessage(new SavableDataWithKeyPacket(this,string, (SavableData)object));
		}else{
			interactData.put(string, object);
		}
		
		if(hasWorldObj()){
			if(object instanceof RedstoneData){
				worldObj.notifyBlockOfStateChange(pos, blockType);
				int side=SideHelper.convert(getOrientation());
				BlockPos pos1=SideHelper.offset(side, pos);
				if(H.getBlock(worldObj, pos1).isOpaqueCube())worldObj.notifyBlockOfStateChange(pos, H.getBlock(worldObj, pos1));
			}
		}
	}
	@Override
	public boolean canInteractWithProvider(NetworkInterfaceProvider provider){
		InteractType[] interactTypes=provider.getInteractTypes();
		for(InteractType i:interactTypes)if(!hasInteractType(i))return false;
		return true;
	}
	@Override
	public void onInterfaceProviderActionInvoked(NetworkInterfaceProvider interfaceProvider, String action, Object... data){
		if(H.isNull(interfaceProvider,getBrain(),action,data)||action.isEmpty()||data.length==0)return;
		onInterfaceProviderActionInvoked(interfaceProvider, action, data.length, data);
	}
	
	@Override
	public DoubleObject<DataOutput,Object> getInterfaceDataOutput(){
		if(getInterfaceProvider()==null)return new DoubleObject();
		return TileToInterfaceHelper.getDataOutput(this, getInterfaceProvider(), DataOutputDesc.Redstone);
	}
	@Override
	public boolean isCompatabile(){
		return true;
	}
	@Override
	public List<TileEntityNetworkPointerContainer> getPointerContainers(){
		List<TileEntityNetworkPointerContainer> result=new ArrayList<TileEntityNetworkPointerContainer>();
		TileEntity[] tiles=SideHelper.getTilesOnSides(this);
		for(TileEntity a:tiles)if(a instanceof TileEntityNetworkPointerContainer)result.add((TileEntityNetworkPointerContainer)a);
		return result;
	}

	@Override
	public List<ItemStack> getPointers(){
		List<ItemStack> result=new ArrayList<ItemStack>();
		List<TileEntityNetworkPointerContainer> containers=getPointerContainers();
		for(TileEntityNetworkPointerContainer a:containers){
			for(int b=0;b<a.getSizeInventory();b++){
				if(Helper.isItemInStack(MItems.NetworkPointer, a.getStackInSlot(b)))result.add(a.getStackInSlot(b));
			}
		}
		return result;
	}
}
