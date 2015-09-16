package com.magiology.mcobjects.tileentityes.network;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.api.connection.IConnection;
import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.NetworkBaseInterface;
import com.magiology.api.network.skeleton.TileEntityNetwork;
import com.magiology.core.init.MItems;
import com.magiology.forgepowered.event.ForcePipeUpdate;
import com.magiology.util.utilclasses.NetworkUtil;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.SlowdownUtil;

public class TileEntityNetworkPointerContainer extends TileEntityNetwork implements ISidedInventory,IUpdatePlayerListBox{
	
	private SlowdownUtil optimizer=new SlowdownUtil(40);
	public ItemStack[] slots=new ItemStack[9];
	
	public TileEntityNetworkPointerContainer(){}
	
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		Util.saveItemsToNBT(NBT, "inventory", slots);
	}
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		slots=Util.loadItemsFromNBT(NBT, "inventory", slots);
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
		UpdateablePipeHandeler.setConnections(connections, this);
		
		int side=SideUtil.convert(getOrientation());
		side=SideUtil.getOppositeSide(side);
		for(int i=0;i<6;i++)setAccessibleOnSide(i, i==side);
		setColisionBoxes();
	}
	@Override
	public void setColisionBoxes(){
		int side=getOrientation();
		
		switch (side){
		case 2:side=4;break;
		case 4:side=3;break;
		case 3:side=2;break;
		}
		
		collisionBoxes=new AxisAlignedBB[]{
				connections[5]!=null?new AxisAlignedBB(p*11,p*6, p*6, 1,    p*10, p*10):null,
				connections[1]!=null?new AxisAlignedBB(p*6, p*11,p*6, p*10, 1,    p*10):null,
				connections[2]!=null?new AxisAlignedBB(p*6, p*6, 0,   p*10, p*10, p*5 ):null,
				connections[3]!=null?new AxisAlignedBB(p*6, p*6, p*11,p*10, p*10, 1   ):null,
				connections[0]!=null?new AxisAlignedBB(p*6, 0,   p*6, p*10, p*5,  p*10):null,
				connections[4]!=null?new AxisAlignedBB(0,   p*6, p*6, p*5,  p*10, p*10):null,
						new AxisAlignedBB(p*5, p*5, p*5, p*11, p*11, p*11),
		};
//		Helper.printInln(side);
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				switch(side){
				case 4:{
					collisionBoxes=ArrayUtils.add(collisionBoxes,
							new AxisAlignedBB(p*9.5-i*p*2, p*9.5-j*p*2, p*4.8, p*10.5-i*p*2, p*10.5-j*p*2, p*10));
				}break;
				case 2:{
					collisionBoxes=ArrayUtils.add(collisionBoxes,
							new AxisAlignedBB(p*9.5-i*p*2, p*9.5-j*p*2, p*6, p*10.5-i*p*2, p*10.5-j*p*2, p*11.2));
				}break;
				case 3:{
					collisionBoxes=ArrayUtils.add(collisionBoxes,
							new AxisAlignedBB(p*4.8, p*9.5-j*p*2, p*9.5-i*p*2, p*10, p*10.5-j*p*2, p*10.5-i*p*2));
				}break;
				case 5:{
					collisionBoxes=ArrayUtils.add(collisionBoxes,
							new AxisAlignedBB(p*6, p*9.5-j*p*2, p*9.5-i*p*2, p*11.2, p*10.5-j*p*2, p*10.5-i*p*2));
				}break;
				case 0:{
					collisionBoxes=ArrayUtils.add(collisionBoxes,
							new AxisAlignedBB(p*9.5-i*p*2, p*4.8, p*9.5-j*p*2, p*10.5-i*p*2, p*10, p*10.5-j*p*2));
				}break;
				case 1:{
					collisionBoxes=ArrayUtils.add(collisionBoxes,
							new AxisAlignedBB(p*9.5-i*p*2, p*6, p*9.5-j*p*2, p*10.5-i*p*2, p*11.2, p*10.5-j*p*2));
				}break;
				}
			}
		}
	}
	@Override
	public void getValidTileEntitys(List<Class> included, List<Class> excluded){
		included.add(NetworkBaseInterface.class);
		excluded.add(ISidedNetworkComponent.class);
	}
	@Override
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array, int side){
		return !(clazz.equals(ISidedNetworkComponent.class))&&NetworkUtil.canConnect(this, (ISidedNetworkComponent)tile);
	}
	
	@Override
	public int getSizeInventory(){
		return 9;
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
		return Util.isItemInStack(MItems.NetworkPointer, player.getCurrentEquippedItem());
	}
	@Override
	public void openInventory(EntityPlayer player){
		
	}
	@Override
	public void closeInventory(EntityPlayer player){
		
	}
	@Override
	public boolean isItemValidForSlot(int id, ItemStack stack){
		return Util.isItemInStack(MItems.NetworkPointer, stack);
	}
	public NetworkBaseInterface getBoundedBaseInterface(){
		int side=SideUtil.convert(getOrientation());
		TileEntity test=worldObj.getTileEntity(SideUtil.offset(side, pos));
		if(test instanceof NetworkBaseInterface)return (NetworkBaseInterface)test;
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
		if(side.getIndex()==SideUtil.convert(getOrientation()))return new int[]{0,1,2,3,4,5,6,7,8};
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,EnumFacing side){
		return side.getIndex()==SideUtil.convert(getOrientation());
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack,EnumFacing direction){
		return canInsertItem(index, stack,direction);
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
