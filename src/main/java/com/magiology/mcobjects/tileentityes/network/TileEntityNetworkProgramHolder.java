package com.magiology.mcobjects.tileentityes.network;

import java.util.*;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.gui.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;

import com.magiology.api.connection.*;
import com.magiology.api.network.*;
import com.magiology.api.network.skeleton.*;
import com.magiology.core.init.*;
import com.magiology.forgepowered.events.*;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.*;

public class TileEntityNetworkProgramHolder extends TileEntityNetwork implements ISidedInventory,IUpdatePlayerListBox{
	
	private SlowdownUtil optimizer=new SlowdownUtil(40);
	public ItemStack[] slots=new ItemStack[16];
	
	public TileEntityNetworkProgramHolder(){}
	
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		UtilM.saveItemsToNBT(NBT, "inventory", slots);
	}
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		slots=UtilM.loadItemsFromNBT(NBT, "inventory", slots);
	}
	
	@Override
	public void update(){
		if(getBrain()==null){
			findBrain();
			ForcePipeUpdate.updatePipe(worldObj, pos);
		}
		checkBrainConnection();
		if(optimizer.isTimeWithAddProgress())updateConnections();
	}
	@Override
	public void initNetworkComponent(){}
	@Override
	public void updateConnections(){
		EnumFacing[] sides=new EnumFacing[6];
		UpdateablePipeHandler.setConnections(sides, this);
		for(int i=0;i<sides.length;i++)connections[i].setMain(sides[i]!=null);
		setColisionBoxes();
	}
	@Override
	public void setColisionBoxes(){
		collisionBoxes=new AxisAlignedBB[]{
				connections[5].getMain()?new AxisAlignedBB(p*13,p*6, p*6, 1,    p*10, p*10):null,
				connections[1].getMain()?new AxisAlignedBB(p*6, p*13,p*6, p*10, 1,    p*10):null,
				connections[2].getMain()?new AxisAlignedBB(p*6, p*6, 0,   p*10, p*10, p*3 ):null,
				connections[3].getMain()?new AxisAlignedBB(p*6, p*6, p*13,p*10, p*10, 1   ):null,
				connections[0].getMain()?new AxisAlignedBB(p*6, 0,   p*6, p*10, p*3,  p*10):null,
				connections[4].getMain()?new AxisAlignedBB(0,   p*6, p*6, p*3,  p*10, p*10):null,
				new AxisAlignedBB(p*3, p*3, p*3, p*13, p*13, p*13),
		};
	}
	@Override
	public void getValidTileEntitys(List<Class> included, List<Class> excluded){
		included.add(NetworkBaseComponent.class);
	}
	@Override
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array, int side){
		return NetworkUtil.canConnect(this, (NetworkBaseComponent)tile);
	}
	
	@Override
	public int getSizeInventory(){
		return slots.length;
	}
	@Override
	public ItemStack getStackInSlot(int id){
		return slots[id];
	}
	@Override
	public ItemStack decrStackSize(int v1, int v2){
		if(this.slots[v1]!=null){
			ItemStack ItemS=null;
			
			if(this.slots[v1].stackSize<=v2){
				ItemS=this.slots[v1];
				this.slots[v1]=null;
				return ItemS;
			}else{
				ItemS=this.slots[v1].splitStack(v2);
				if(this.slots[v1].stackSize==0){
					this.slots[v1]=null;
				}
			}
			return ItemS;
		}
		return null;
	}
	@Override
	public ItemStack getStackInSlotOnClosing(int v1){
		if(this.slots[v1]!=null){
			ItemStack ItemS=this.slots[v1];
			this.slots[v1]=null;
			return ItemS;
		}
		return null;
	}
	@Override
	public void setInventorySlotContents(int v1, ItemStack stack){
		this.slots[v1]=stack;
		if(stack!=null&&stack.stackSize>this.getInventoryStackLimit()){
			stack.stackSize=this.getInventoryStackLimit();
		}
		
	}
	@Override
	public String getName(){
		return "NetworkPointerContainer";
	}
	@Override
	public boolean hasCustomName(){
		return true;
	}
	@Override
	public int getInventoryStackLimit(){
		return 1;
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer player){
		return UtilM.isItemInStack(MItems.networkPointer, player.getCurrentEquippedItem());
	}
	@Override
	public void openInventory(EntityPlayer player){
		
	}
	@Override
	public void closeInventory(EntityPlayer player){
		
	}
	@Override
	public boolean isItemValidForSlot(int id, ItemStack stack){
		return UtilM.isItemInStack(MItems.networkPointer, stack);
	}
	public NetworkInterface getBoundedBaseInterface(){
		int side=getOrientation();
		TileEntity test=worldObj.getTileEntity(SideUtil.offset(side, pos));
		if(test instanceof NetworkInterface)return (NetworkInterface)test;
		return null;
	}

	@Override
	public int getField(int id){
		return 0;
	}

	@Override
	public void setField(int id, int value){
		
	}

	@Override
	public int getFieldCount(){
		return 0;
	}

	@Override
	public void clear(){
		
	}

	@Override
	public IChatComponent getDisplayName(){
		return null;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side){
		int[] result=new int[getSizeInventory()];
		for(int i=0;i<getSizeInventory();i++)result[i]=i;
		return result;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,EnumFacing side){
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack,EnumFacing direction){
		return true;
	}

	@Override
	public IConnection[] getConnections(){
		return null;
	}

	@Override
	public boolean isStrate(EnumFacing facing){
		return false;
	}
}
