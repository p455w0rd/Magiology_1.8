package com.magiology.api.network;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.magiology.api.network.NetworkBaseInterface.DataOutput.DataOutputDesc;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkPointerContainer;
import com.magiology.util.utilobjects.DoubleObject;

public interface NetworkBaseInterface{
	
	public void onInterfaceProviderActionInvoked(NetworkInterfaceProvider interfaceProvider,String action,Object... data);
	public void onNetworkActionInvoked(String action, Object... data);
	public TileEntityNetworkController getBrain();
	public NetworkInterfaceProvider getInterfaceProvider();
	public boolean canInteractWithProvider(NetworkInterfaceProvider provider);
	public Map<String,Object> getData();
	public Object getInteractData(String string);
	public  void  setInteractData(String string,Object object);
	public long getActiveCard();
	public TileEntity getHost();
	public List<TileEntityNetworkPointerContainer> getPointerContainers();
	public List<ItemStack> getPointers();
	
	public DoubleObject<DataOutput,Object> getInterfaceDataOutput();
	public boolean isCompatabile();
	public boolean hasInteractType(InteractType interactType);
	public List<InteractType> getInteractTypes();
	public void setInteractType(InteractType interactType);
	
	
	public static enum DataOutput{
		Int,Float,Boolean;
		public boolean isInt(){return this==Int;}
		public boolean isFloat(){return this==Float;}
		public boolean isBoolean(){return this==Boolean;}
		public static enum DataOutputDesc{
			Redstone,Power,Brightness,Color,Amount;
			public boolean isPower(){return this==Power;}
			public boolean isColor(){return this==Color;}
			public boolean isAmount(){return this==Amount;}
			public boolean isRedstone(){return this==Redstone;}
			public boolean isBrightness(){return this==Brightness;}
		}
	}
	public static enum InteractType{
		Redstone(null),
		Data(null),
		WorldInteract(null),
		EntityDetect(WorldInteract),
		Clicker(WorldInteract),
		BlockDetect(WorldInteract),
		BlockDetectAdvanced(BlockDetect),
		TileEntityDetect(WorldInteract),
		MinionInterface(EntityDetect);
		
		public final InteractType extension;
		private InteractType(InteractType extension){
			this.extension=extension;
		}
		public boolean isCompatible(InteractType... type){
			for(InteractType ty:type)if(isCompatible(ty))return true;
			return false;
		}
		public boolean isCompatible(InteractType type){
			if(type==this)return true;
			if(extension!=null&&extension.isCompatible(type))return true;
			return false;
		}
		public boolean isCompatabileWith(DataOutput output,DataOutputDesc desc){
			return false;
		}
		public boolean isCompatabileWith(DataOutput output){
			if(output==DataOutput.Int){
				return isCompatible(Redstone,EntityDetect,BlockDetect,TileEntityDetect,MinionInterface);
			}
			if(output==DataOutput.Float){
				return isCompatible(Redstone);
			}
			return false;
		}
	}
}
